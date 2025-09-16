package vv;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import vv.subsystems.climber.Climber;

public class RobotContainer {
    
    public CommandXboxController operatorController = new CommandXboxController(0);

    public Climber climber = new Climber();

    public RobotContainer() {
		DriverStation.silenceJoystickConnectionWarning(true);

		setUpController();
	}

    public void setUpController() {
        operatorController.povUp().whileTrue(climber.runPivotForward());
        operatorController.povDown().whileTrue(climber.runPivotBackward());
    }

}
