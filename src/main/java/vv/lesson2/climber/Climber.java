package vv.lesson2.climber;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import vv.config.VVConfig;

@Logged
public class Climber extends SubsystemBase {

    private final double pivotSpeed;
    private final double rollerSpeed;
    
    private final TalonFX pivotMotor;
    private final TalonFX rollerMotor;

    public Climber(VVConfig config) {

        pivotSpeed = config.climber().pivotConfig().speed();
        rollerSpeed = config.climber().rollerConfig().speed();

        pivotMotor = new TalonFX(config.climber().pivotConfig().id());
        rollerMotor = new TalonFX(config.climber().rollerConfig().id());

        setUpMotors(config);
    }

    public void setUpMotors(VVConfig config) {

        pivotMotor.getConfigurator().apply(new CurrentLimitsConfigs()
            .withStatorCurrentLimit(config.climber().pivotConfig().statorCurrentLimit())
            .withSupplyCurrentLimit(config.climber().pivotConfig().supplyCurrentLimit())
            .withStatorCurrentLimitEnable(true)
            .withSupplyCurrentLimitEnable(true)
        );

        rollerMotor.getConfigurator().apply(new CurrentLimitsConfigs()
            .withStatorCurrentLimit(config.climber().rollerConfig().statorCurrentLimit())
            .withSupplyCurrentLimit(config.climber().rollerConfig().supplyCurrentLimit())
            .withStatorCurrentLimitEnable(true)
            .withSupplyCurrentLimitEnable(true)
        );

        if(config.climber().pivotConfig().inverted()) {
            pivotMotor.getConfigurator().apply(new TalonFXConfiguration()
                .withMotorOutput(new MotorOutputConfigs().withInverted(InvertedValue.CounterClockwise_Positive))
            );
        } else {
            pivotMotor.getConfigurator().apply(new TalonFXConfiguration()
                .withMotorOutput(new MotorOutputConfigs().withInverted(InvertedValue.Clockwise_Positive))
            );
        }

        if(config.climber().rollerConfig().inverted()) {
            rollerMotor.getConfigurator().apply(new TalonFXConfiguration()
                .withMotorOutput(new MotorOutputConfigs().withInverted(InvertedValue.CounterClockwise_Positive))
            );
        } else {
            rollerMotor.getConfigurator().apply(new TalonFXConfiguration()
                .withMotorOutput(new MotorOutputConfigs().withInverted(InvertedValue.Clockwise_Positive))
            );
        }

        pivotMotor.setNeutralMode(NeutralModeValue.Brake);
        rollerMotor.setNeutralMode(NeutralModeValue.Brake);
    }

    public Command runPivotForward() {
        return this.runEnd(
            () -> pivotMotor.set(pivotSpeed),
            () -> stop()
        );
    }

    public Command runPivotBackward() {
        return this.runEnd(
            () -> pivotMotor.set(-pivotSpeed),
            () -> stop()
        );
    }

    public Command runRollers() {
        return this.runEnd(
            () -> rollerMotor.set(rollerSpeed),
            () -> stop()
        );
    }

    public void stop() {
        pivotMotor.set(0.0);
        rollerMotor.set(0.0);
    }
}
