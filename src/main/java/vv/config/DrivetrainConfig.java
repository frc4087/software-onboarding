package vv.config;

import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;
import com.ctre.phoenix6.swerve.SwerveModuleConstants.ClosedLoopOutputType;
import com.ctre.phoenix6.swerve.SwerveModuleConstants.DriveMotorArrangement;
import com.ctre.phoenix6.swerve.SwerveModuleConstants.SteerFeedbackType;
import com.ctre.phoenix6.swerve.SwerveModuleConstants.SteerMotorArrangement;

import static edu.wpi.first.units.Units.Amp;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.KilogramSquareMeters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Rotations;
import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Volts;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.units.measure.MomentOfInertia;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.units.measure.Voltage;
import static vv.config.PropertyReaders.readBooleanProperty;
import static vv.config.PropertyReaders.readDoubleProperty;
import static vv.config.PropertyReaders.readIntegerProperty;

public record DrivetrainConfig(
    String hootFilepath,
    PigeonConfig pigeon,
    Time discretizationDelta,
    Constants constants,
    SwerveModuleConfig frontLeftModule,
    SwerveModuleConfig frontRightModule,
    SwerveModuleConfig backLeftModule,
    SwerveModuleConfig backRightModule
) {
    public DrivetrainConfig {
        Objects.requireNonNull(hootFilepath);
        Objects.requireNonNull(pigeon);
        Objects.requireNonNull(discretizationDelta);
        Objects.requireNonNull(constants);
        Objects.requireNonNull(frontLeftModule);
        Objects.requireNonNull(frontRightModule);
        Objects.requireNonNull(backLeftModule);
        Objects.requireNonNull(backRightModule);
    }

    public static DrivetrainConfig fromProperties(Properties p) {
        var delta = readDoubleProperty(p, "drivetrain.discretization.delta.s");
        return new DrivetrainConfig(
            p.getProperty("drivetrain.canbus.hoot.filepath"),
            PigeonConfig.fromProperties(p),
            Time.ofBaseUnits(delta, Seconds),
            Constants.fromProperties(p),
            SwerveModuleConfig.fromProperties(p, "front.left"),
            SwerveModuleConfig.fromProperties(p, "front.right"),
            SwerveModuleConfig.fromProperties(p, "back.left"),
            SwerveModuleConfig.fromProperties(p, "back.right")
        );
    }

    static public record PigeonConfig(Integer canid) {
        public PigeonConfig {
            Objects.requireNonNull(canid);
        }

        public static PigeonConfig fromProperties(Properties p) {
            return new PigeonConfig(
                readIntegerProperty(p, "drivetrain.pigeon.canid")
            );
        }
    }

    static public record Constants(
        Double couplingGearRatio,
        Distance wheelRadius,
        Current slipCurrent,
        LinearVelocity freeSpeedAt12Volts,
        LinearVelocity maxLinearSpeed,
        AngularVelocity maxRotationsPerSecond,
        SteerFeedbackType steerFeedbackType,
        MotorConfig driveMotor,
        MotorConfig steerMotor
    ) {
        public Constants {
            Objects.requireNonNull(couplingGearRatio);
            Objects.requireNonNull(wheelRadius);
            Objects.requireNonNull(slipCurrent);
            Objects.requireNonNull(freeSpeedAt12Volts);
            Objects.requireNonNull(maxLinearSpeed);
            Objects.requireNonNull(maxRotationsPerSecond);
            Objects.requireNonNull(steerFeedbackType);
            Objects.requireNonNull(driveMotor);
            Objects.requireNonNull(steerMotor);
        }

        public static Constants fromProperties(Properties p) {
            return new Constants(
                readDoubleProperty(p, "drivetrain.constants.coupling.gear.ratio"),
                Inches.of(readDoubleProperty(p, "drivetrain.constants.wheel.radius.inches")),
                Amp.of(readDoubleProperty(p, "drivetrain.constants.slip.current.amps")),
                MetersPerSecond.of(readDoubleProperty(p, "drivetrain.constants.free.speed.at.12.volts.mps")),
                MetersPerSecond.of(readDoubleProperty(p, "drivetrain.constants.max.mps")),
                RadiansPerSecond.of(readDoubleProperty(p, "drivetrain.constants.max.rads.per.s")),
                SteerFeedbackType.valueOf(
                    Objects.requireNonNull(
                        p.getProperty("drivetrain.constants.steer.feedback.type"),
                    "drivetrain.constants.steer.feedback.type cannot be null"
                    )
                ),
                MotorConfig.fromProperties(p, "drive"),
                MotorConfig.fromProperties(p, "steer")
            );
        }
    }

    static public record MotorConfig(
        MomentOfInertia inertia,
        Voltage frictionVoltage,
        Double gearRatio,
        Double kp,
        Double ki,
        Double kd,
        Double ks,
        Double kv,
        Double ka,
        StaticFeedforwardSignValue staticFeedforwardSign,
        GravityTypeValue gravityType,
        ClosedLoopOutputType closedLoopOutputType,
        Optional<DriveMotorArrangement> driveMotorArrangement,
        Optional<SteerMotorArrangement> steerMotorArrangement,
        Optional<CurrentLimitConfig> currentLimit
    ) {
        public MotorConfig {
            Objects.requireNonNull(inertia);
            Objects.requireNonNull(frictionVoltage);
            Objects.requireNonNull(gearRatio);
            Objects.requireNonNull(kp);
            Objects.requireNonNull(ki);
            Objects.requireNonNull(kd);
            Objects.requireNonNull(ks);
            Objects.requireNonNull(kv);
            Objects.requireNonNull(ka);
            Objects.requireNonNull(staticFeedforwardSign);
            Objects.requireNonNull(gravityType);
            Objects.requireNonNull(closedLoopOutputType);
            Objects.requireNonNull(driveMotorArrangement);
            Objects.requireNonNull(currentLimit);
            assert driveMotorArrangement.isPresent() || steerMotorArrangement.isPresent()
                : "At least one of driveMotorArrangement or steerMotorArrangement must be provided";
        }

        static MotorConfig fromProperties(Properties p, String motorType) {
            var rawFfSign = p.getProperty(String.format("drivetrain.constants.%s.staticFeedforwardSign", motorType));
            Objects.requireNonNull(rawFfSign, String.format("drivetrain.constants.%s.staticFeedforwardSign cannot be null", motorType));
            var ffSign = rawFfSign.contains("closedLoop") ? StaticFeedforwardSignValue.UseClosedLoopSign : StaticFeedforwardSignValue.UseVelocitySign;
            
            var rawGravity = p.getProperty(String.format("drivetrain.constants.%s.gravity.type", motorType));
            Objects.requireNonNull(rawGravity, String.format("drivetrain.constants.%s.gravity.type cannot be null", motorType));
            var gravityType = rawGravity.contains("static") ? GravityTypeValue.Elevator_Static : GravityTypeValue.Arm_Cosine;

            var rawCLOutputType = p.getProperty(String.format("drivetrain.constants.%s.closed.loop.output.type", motorType));
            Objects.requireNonNull(rawCLOutputType, String.format("drivetrain.constants.%s.closed.loop.output.type cannot be null", motorType));
            var clOutputType = rawCLOutputType.contains("voltage") ? ClosedLoopOutputType.Voltage : ClosedLoopOutputType.TorqueCurrentFOC;

            var arrangementKey = String.format("drivetrain.constants.%s.motor.arrangement", motorType);
            var rawMotorArrangement = Objects.requireNonNull(p.getProperty(arrangementKey), arrangementKey + " cannot be null");
            p.getProperty(String.format("drivetrain.constants.%s.motor.arrangement", motorType));
            Optional<DriveMotorArrangement> driveMotorArrangement = Optional.empty();
            Optional<SteerMotorArrangement> steerMotorArrangement = Optional.empty();
            if (motorType.equals("drive")) {
                driveMotorArrangement = Optional.of(DriveMotorArrangement.valueOf(rawMotorArrangement));
            } else if (motorType.equals("steer")) {
                steerMotorArrangement = Optional.of(SteerMotorArrangement.valueOf(rawMotorArrangement));
            } else {
                throw new IllegalArgumentException(String.format("Invalid motor type: %s. Expected 'drive' or 'steer'.", motorType));
            }

            return new MotorConfig(
                KilogramSquareMeters.of(readDoubleProperty(p, String.format("drivetrain.constants.%s.inertia.kgmsqrd", motorType))),
                Volts.of(readDoubleProperty(p, String.format("drivetrain.constants.%s.friction.voltage", motorType))),
                readDoubleProperty(p, String.format("drivetrain.constants.%s.gear.ratio", motorType)),
                readDoubleProperty(p, String.format("drivetrain.constants.%s.gains.kp", motorType)),
                readDoubleProperty(p, String.format("drivetrain.constants.%s.gains.ki", motorType)),
                readDoubleProperty(p, String.format("drivetrain.constants.%s.gains.kd", motorType)),
                readDoubleProperty(p, String.format("drivetrain.constants.%s.gains.ks", motorType)),
                readDoubleProperty(p, String.format("drivetrain.constants.%s.gains.kv", motorType)),
                readDoubleProperty(p, String.format("drivetrain.constants.%s.gains.ka", motorType)),
                ffSign,
                gravityType,
                clOutputType,
                driveMotorArrangement,
                steerMotorArrangement,
                CurrentLimitConfig.fromProperties(p, motorType)
            );
        }
    }

    static public record CurrentLimitConfig(
        Optional<Current> statorLimit
    ) {
        public CurrentLimitConfig {
            Objects.requireNonNull(statorLimit);
        }

        public static Optional<CurrentLimitConfig> fromProperties(Properties p, String motorType) {
            var rawStatorEnabled = p.getProperty(String.format("drivetrain.constants.%s.current.stator.limit.enabled", motorType), "false");
            boolean statorEnabled = Boolean.parseBoolean(rawStatorEnabled);

            var statorLimitKey = String.format("drivetrain.constants.%s.current.stator.limit.amps", motorType);
            Optional<Current> statorLimit = Optional.empty();
            if (p.getProperty(statorLimitKey) != null && statorEnabled) {
                statorLimit = Optional.of(Amp.of(readDoubleProperty(p, statorLimitKey)));
            } 
            
            return statorLimit.isPresent() ? Optional.of(new CurrentLimitConfig(statorLimit)) : Optional.empty();
        }
    }

    static public record SwerveModuleConfig(
        Integer driveCanId,
        Boolean driveMotorInverted,
        Integer steerCanId,
        Boolean steerMotorInverted,
        Integer encoderCanId,
        Angle encoderOffsetRotations,
        Boolean encoderInverted,
        Distance locationX,
        Distance locationY
    ) {
        public SwerveModuleConfig {
            Objects.requireNonNull(driveCanId);
            Objects.requireNonNull(driveMotorInverted);
            Objects.requireNonNull(steerCanId);
            Objects.requireNonNull(steerMotorInverted);
            Objects.requireNonNull(encoderCanId);
            Objects.requireNonNull(encoderOffsetRotations);
            Objects.requireNonNull(encoderInverted);
            Objects.requireNonNull(locationX);
            Objects.requireNonNull(locationY);    
        }

        public static SwerveModuleConfig fromProperties(Properties p, String moduleName) {
            return new SwerveModuleConfig(
                readIntegerProperty(p, String.format("drivetrain.modules.%s.drive.canid", moduleName)),
                readBooleanProperty(p, String.format("drivetrain.modules.%s.drive.motor.inverted", moduleName)),
                readIntegerProperty(p, String.format("drivetrain.modules.%s.steer.canid", moduleName)),
                readBooleanProperty(p, String.format("drivetrain.modules.%s.steer.motor.inverted", moduleName)),
                readIntegerProperty(p, String.format("drivetrain.modules.%s.encoder.canid", moduleName)),
                Rotations.of(readDoubleProperty(p, String.format("drivetrain.modules.%s.encoder.offset.rotations", moduleName))),
                Boolean.valueOf(p.getProperty(String.format("drivetrain.modules.%s.encoder.inverted", moduleName), "false")),
                Inches.of(readDoubleProperty(p, String.format("drivetrain.modules.%s.location.x.inches", moduleName))),
                Inches.of(readDoubleProperty(p, String.format("drivetrain.modules.%s.location.y.inches", moduleName)))
            );
        }
    }
}
