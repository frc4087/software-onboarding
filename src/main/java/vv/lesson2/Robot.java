package vv.lesson2;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import vv.config.VVConfig;
import vv.lesson2.controls.DriverControls;
import vv.lesson2.controls.OperatorControls;
import vv.subsystems.climber.Climber;

public class Robot extends TimedRobot{

    VVConfig config;
    DriverControls driverControls;
    OperatorControls operatorControls;

    Climber climber;
    
    public Robot() {
        config = VVConfig.readFromDeployDirectory("practice-robot.properties");
        driverControls = new DriverControls(config);
        operatorControls = new OperatorControls(config);

        climber = new Climber(config);

        setupTriggers();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    private void setupTriggers() {
        operatorControls.setupTriggers(climber);
    }

}