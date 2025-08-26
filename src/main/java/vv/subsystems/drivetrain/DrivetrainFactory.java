package vv.subsystems.drivetrain;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.swerve.SwerveModuleConstantsFactory;

import vv.config.DrivetrainConfig.MotorConfig;
import vv.config.DrivetrainConfig.SwerveModuleConfig;
import vv.config.VVConfig;

public class DrivetrainFactory {
    
    public static Drivetrain createDrivetrain(VVConfig config) {
        SwerveDrivetrainConstants drivetrainConstants = createDrivetrainConstants(config);

        var moduleConstantCreator = createSwerveModuleConstantsFactory(config);
        var swerveModules = new SwerveModuleConstants[] {
            createSwerveModuleConstants(moduleConstantCreator, config.drivetrain().frontLeftModule()),
            createSwerveModuleConstants(moduleConstantCreator, config.drivetrain().frontRightModule()),
            createSwerveModuleConstants(moduleConstantCreator, config.drivetrain().backLeftModule()),
            createSwerveModuleConstants(moduleConstantCreator, config.drivetrain().backRightModule())
        };

        return new Drivetrain(config, drivetrainConstants, swerveModules);
    }

    private static SwerveDrivetrainConstants createDrivetrainConstants(VVConfig config) {
        var kCANBus = new CANBus("", config.drivetrain().hootFilepath());
        return new SwerveDrivetrainConstants()
            .withCANBusName(kCANBus.getName())
            .withPigeon2Id(config.drivetrain().pigeon().canid())
            .withPigeon2Configs(null);
    }

    private static SwerveModuleConstantsFactory<TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration> createSwerveModuleConstantsFactory(
        VVConfig config
    ) {
        var constants = config.drivetrain().constants();
        var driveMotor = config.drivetrain().constants().driveMotor();
        var steerMotor = config.drivetrain().constants().steerMotor();
        var canCoderConfig = new CANcoderConfiguration();

        var driveMotorType = driveMotor.driveMotorArrangement().orElseThrow(() -> new IllegalArgumentException("driveMotorArrangement must be specified"));
        var steerMotorType = steerMotor.steerMotorArrangement().orElseThrow(() -> new IllegalArgumentException("steerMotorArrangement must be specified"));

        return new SwerveModuleConstantsFactory<TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration>()
            .withCouplingGearRatio(constants.couplingGearRatio())
            .withWheelRadius(constants.wheelRadius())
            .withSlipCurrent(constants.slipCurrent())
            .withSpeedAt12Volts(constants.freeSpeedAt12Volts())
            .withFeedbackSource(constants.steerFeedbackType())
            .withEncoderInitialConfigs(canCoderConfig)
            .withDriveMotorGearRatio(driveMotor.gearRatio())
            .withDriveMotorGains(new Slot0Configs()
                .withKP(driveMotor.kp())
                .withKI(driveMotor.ki())
                .withKD(driveMotor.kd())
                .withKS(driveMotor.ks())
                .withKV(driveMotor.kv())
                .withKA(driveMotor.ka())
                .withGravityType(driveMotor.gravityType())
                .withStaticFeedforwardSign(driveMotor.staticFeedforwardSign())
            )
            .withDriveMotorClosedLoopOutput(driveMotor.closedLoopOutputType())
            .withDriveMotorType(driveMotorType)
            .withDriveMotorInitialConfigs(createTalonFXConfiguration(driveMotor))
            .withDriveInertia(driveMotor.inertia())
            .withDriveFrictionVoltage(driveMotor.frictionVoltage())
            .withSteerMotorGearRatio(steerMotor.gearRatio())
            .withSteerMotorGains(new Slot0Configs()
                .withKP(steerMotor.kp())
                .withKI(steerMotor.ki())
                .withKD(steerMotor.kd())
                .withKS(steerMotor.ks())
                .withKV(steerMotor.kv())
                .withKA(steerMotor.ka())
                .withGravityType(steerMotor.gravityType())
                .withStaticFeedforwardSign(steerMotor.staticFeedforwardSign())
            )            
            .withSteerMotorClosedLoopOutput(steerMotor.closedLoopOutputType())
            .withSteerMotorType(steerMotorType)
            .withSteerMotorInitialConfigs(createTalonFXConfiguration(steerMotor))
            .withSteerInertia(steerMotor.inertia())
            .withSteerFrictionVoltage(steerMotor.frictionVoltage());
    }

    private static SwerveModuleConstants<TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration> createSwerveModuleConstants(
        SwerveModuleConstantsFactory<TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration> factory,
        SwerveModuleConfig config
    ) {
        return factory.createModuleConstants(
            config.steerCanId(),
            config.driveCanId(),
            config.encoderCanId(),
            config.encoderOffsetRotations(),
            config.locationX(),
            config.locationY(),
            config.driveMotorInverted(),
            config.steerMotorInverted(),
            config.encoderInverted()
        );
    }
        
    private static TalonFXConfiguration createTalonFXConfiguration(MotorConfig motor) {
        var talonConfig = new TalonFXConfiguration();
        if (motor.currentLimit().isPresent()) {
            var limitConfig = motor.currentLimit().get();
            var talonLimitConfig = new CurrentLimitsConfigs();
            limitConfig.statorLimit().ifPresent((current) -> {
                talonLimitConfig.withStatorCurrentLimitEnable(true);
                talonLimitConfig.withStatorCurrentLimit(current);
            });
            talonConfig.withCurrentLimits(talonLimitConfig);
        }
        return talonConfig;
    }
}
