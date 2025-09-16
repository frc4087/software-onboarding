package vv.subsystems.climber;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

@Logged
public class Climber extends SubsystemBase {
    
    private final TalonFX pivotMotor = new TalonFX(ClimberConstants.pivotMotorID);
    private final TalonFX rollerMotor = new TalonFX(ClimberConstants.rollerMotorID);

    public Climber() {
        pivotMotor.getConfigurator().apply(ClimberConstants.pivotCurrentLimits);
        rollerMotor.getConfigurator().apply(ClimberConstants.rollerCurrentLimits);

        pivotMotor.getConfigurator().apply(ClimberConstants.pivotConfigs);
        rollerMotor.getConfigurator().apply(ClimberConstants.rollerConfigs);
    }

    public Command runPivotForward() {
        return this.runEnd(
            () -> pivotMotor.set(ClimberConstants.pivotSpeed),
            () -> stop()
        );
    }

    public Command runPivotBackward() {
        return this.runEnd(
            () -> pivotMotor.set(-ClimberConstants.pivotSpeed),
            () -> stop()
        );
    }

    public Command runRollers() {
        return this.runEnd(
            () -> rollerMotor.set(-ClimberConstants.rollerSpeed),
            () -> stop()
        );
    }

    public void stop() {
        pivotMotor.set(0.0);
        rollerMotor.set(0.0);
    }
}
