package vv.lesson4;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import vv.config.VVConfig;
import vv.lesson4.controls.DriverControls;
import vv.lesson4.drivetrain.Drivetrain;
import vv.lesson4.drivetrain.DrivetrainFactory;

public class Robot extends TimedRobot {

    public static Robot instance = null;

    VVConfig config;
    DriverControls driverControls;
    Drivetrain drivetrain;

    private Robot() {
        config = VVConfig.readFromDeployDirectory("practice-robot.properties");
        driverControls = new DriverControls(config);
        drivetrain = DrivetrainFactory.createDrivetrain(config);
        setupTriggers();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void testInit() {
        // Put the command you want to test here
    }

    private void setupTriggers() {
        driverControls.setupTriggers(config, drivetrain);
    }

    public static Robot start() {
        if (Robot.instance == null) {
            Robot.instance = new Robot();
        }
        return Robot.instance;
    }
}