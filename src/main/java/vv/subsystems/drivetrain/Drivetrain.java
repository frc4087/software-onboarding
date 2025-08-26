package vv.subsystems.drivetrain;

import java.util.function.Supplier;

import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.swerve.SwerveDrivetrain;
import com.ctre.phoenix6.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Seconds;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import vv.config.VVConfig;

@Logged
public class Drivetrain extends SwerveDrivetrain<TalonFX, TalonFX, CANcoder> implements Subsystem {
    private final double discretizationDelta;
    private final double maxLinearSpeedMps;
    private final double maxRadsPerSecond;

    public Drivetrain(
        VVConfig config,
        SwerveDrivetrainConstants drivetrainConstants,
        SwerveModuleConstants<?, ?, ?>[] swerveModules
    ) {
        super(
            TalonFX::new,           // Drive motor constructor
            TalonFX::new,           // Steer motor constructor
            CANcoder::new,          // Encoder enstructor
            drivetrainConstants,
            swerveModules
        );
        var delta = config.drivetrain().discretizationDelta();
        maxLinearSpeedMps = config.drivetrain().constants().maxLinearSpeed().abs(MetersPerSecond);
        maxRadsPerSecond = config.drivetrain().constants().maxRotationsPerSecond().abs(RadiansPerSecond);
        config.drivetrain().constants().maxRotationsPerSecond();
        this.discretizationDelta = Seconds.convertFrom(delta.baseUnitMagnitude(), delta.unit());
        if (Utils.isSimulation()) {
            handleSimulation(config);
        }        
    }

    public Command applyRequest(Supplier<SwerveRequest> requestSupplier) {
        return run(() -> this.setControl(requestSupplier.get()));
    }

    public void stop() {
        driveRobotRelative(0,0,0);
    }

    /**
     * @param vx Linear velocity in the X direction (m/s)
     * @param vy Linear velocity in the Y direction (m/s)
     * @param omega Angular velocity (rad/s)
     */
    public void driveRobotRelative(double vx, double vy, double omega) {
        ChassisSpeeds targetSpeeds = ChassisSpeeds.discretize(
            clampLinearVelocity(vx), 
            clampLinearVelocity(vy),
            clampAngularVelocity(omega),
            this.discretizationDelta
        );
        this.setControl(new SwerveRequest.ApplyRobotSpeeds()
            .withSpeeds(targetSpeeds)
            .withDesaturateWheelSpeeds(true)
        );
    }

    /**
     * @param vx Linear velocity in the X direction (m/s)
     * @param vy Linear velocity in the Y direction (m/s)
     * @param omega Angular velocity (rad/s)
     */
    public void driveFieldRelative(double vx, double vy, double omega) {
        ChassisSpeeds targetSpeeds = ChassisSpeeds.discretize(
            clampLinearVelocity(vx), 
            clampLinearVelocity(vy),
            clampAngularVelocity(omega),
            this.discretizationDelta
        );
        this.setControl(new SwerveRequest.ApplyFieldSpeeds()
            .withSpeeds(targetSpeeds)
            .withDesaturateWheelSpeeds(true)
        );
    }

    private void handleSimulation(VVConfig config) {
        var simRate = config.simulation().simLoopPeriodFreq();
        for (var module : getModules()) {
            module.getDriveMotor().getPosition().setUpdateFrequency(simRate);
            module.getDriveMotor().getVelocity().setUpdateFrequency(simRate);
            module.getSteerMotor().getPosition().setUpdateFrequency(simRate);
            module.getSteerMotor().getVelocity().setUpdateFrequency(simRate);
            module.getEncoder().getPosition().setUpdateFrequency(simRate);
            module.getEncoder().getVelocity().setUpdateFrequency(simRate);
        }
        
        final double simLoopPeriod = config.simulation().simLoopPeriodFreq().asPeriod().baseUnitMagnitude();
        Notifier simNotifier = new Notifier(new Runnable() {
            private double lastSimTime = Utils.getCurrentTimeSeconds();
            @Override
            public void run() {
                final double currentTime = Utils.getCurrentTimeSeconds();
                double deltaTime = currentTime - lastSimTime;
                updateSimState(deltaTime, RobotController.getBatteryVoltage());
                lastSimTime = currentTime;
            }
        });
        simNotifier.startPeriodic(simLoopPeriod);
    }

    public void logPose() {
        System.out.println(
            String.format("Drivetrain @ (%.3fm, %.3fm, %.2f deg)",
            this.getState().Pose.getMeasureX().baseUnitMagnitude(),
            this.getState().Pose.getMeasureY().baseUnitMagnitude(),
            this.getState().Pose.getRotation().getDegrees()
        ));
    }

    @Logged
    public double getPoseX() {
        return this.getState().Pose.getX();
    }

    @Logged
    public double getPoseY() {
        return this.getState().Pose.getX();
    }

    @Logged
    public Rotation2d getRot() {
        return this.getState().Pose.getRotation();
    }



    public double clampLinearVelocity(double v) {
        return clamp(v, -maxLinearSpeedMps, maxLinearSpeedMps);
    }

    public double clampAngularVelocity(double v) {
        return clamp(v, -maxRadsPerSecond, maxRadsPerSecond);
    }

    private static double clamp(double value, double lowerBound, double upperBound) {
        return Math.min(Math.max(lowerBound, value), upperBound);
    }
}
