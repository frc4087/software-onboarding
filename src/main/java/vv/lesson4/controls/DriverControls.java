package vv.lesson4.controls;

import java.util.function.Consumer;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import edu.wpi.first.wpilibj.simulation.XboxControllerSim;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import vv.config.VVConfig;
import vv.lesson4.drivetrain.Drivetrain;

public class DriverControls {
    private final CommandXboxController controller;
    private final XboxControllerSim sim;

    public DriverControls(
        VVConfig config
    ) {
        var driverConfig = config.controllers().driver();
        controller = new CommandXboxController(driverConfig.port());
        sim = new XboxControllerSim(controller.getHID());
    }

    public void setupTriggers(VVConfig config, Drivetrain drivetrain) {
        var maxLinearSpeed = config.drivetrain().constants().maxLinearSpeed().in(MetersPerSecond);
        var maxRotationalSpeed = config.drivetrain().constants().maxRotationsPerSecond().in(RadiansPerSecond);
        var translationalDeadband = config.controllers().driver().translationalDeadband();
        var rotationalDeadband = config.controllers().driver().rotationalDeadband();

        drivetrain.setDefaultCommand(
			drivetrain.applyRequest(() -> new SwerveRequest.FieldCentric()
		        .withDeadband(maxLinearSpeed * translationalDeadband)
                .withRotationalDeadband(maxRotationalSpeed * rotationalDeadband)
		        .withDriveRequestType(DriveRequestType.OpenLoopVoltage)
                .withVelocityX(drivetrain.clampLinearVelocity(-controller.getLeftY() * maxLinearSpeed))
				.withVelocityY(drivetrain.clampLinearVelocity(-controller.getLeftX() * maxLinearSpeed))
				.withRotationalRate(drivetrain.clampAngularVelocity(-controller.getRightX() * maxRotationalSpeed))
			)
            .withName("DriveWithController")
		);

    }

    public CommandXboxController controls() {
        return controller;
    }

    public XboxControllerSim sim() {
        return sim;
    }

    public void simulate(Consumer<XboxControllerSim> consumer) {
        consumer.accept(sim);
        sim.notifyNewData();
    }
}