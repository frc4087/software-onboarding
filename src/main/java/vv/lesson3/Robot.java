package vv.lesson3;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import vv.config.VVConfig;
import vv.lesson3.arm.ArmSubsystem;
import vv.lesson3.controls.OperatorControls;

public class Robot extends TimedRobot{
    
    public static Robot instance = null;

    VVConfig config;
    OperatorControls operatorControls;
    ArmSubsystem arm;

    private Robot() {
        config = VVConfig.readFromDeployDirectory("practice-robot.properties");
        operatorControls = new OperatorControls(config);
        arm = new ArmSubsystem(config.arm());
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
        operatorControls.setupTriggers(arm);
    }

}