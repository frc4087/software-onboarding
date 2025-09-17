package vv.lesson1;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import vv.config.VVConfig;
import vv.lesson1.controls.DriverControls;
import vv.lesson1.controls.OperatorControls;

public class Robot extends TimedRobot{
    
    public static Robot instance = null;

    VVConfig config;
    DriverControls driverControls;
    OperatorControls operatorControls;

    private Robot() {
        config = VVConfig.readFromDeployDirectory("practice-robot.properties");
        driverControls = new DriverControls(config);
        operatorControls = new OperatorControls(config);

        setupTriggers();
    }

    public static Robot start() {
        if (Robot.instance == null) {
            Robot.instance = new Robot();
        }
        return Robot.instance;
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    private void setupTriggers(){
        operatorControls.setupTriggers(driverControls);
    }

}