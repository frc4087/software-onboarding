package vv.subsystems.climber;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;

public class ClimberConstants {
    
    // Speeds are between -1.0 and 1.0
    public static double rollerSpeed = 1.0;
    public static double pivotSpeed = 0.5;

    public static int pivotMotorID = 43;
    public static int rollerMotorID = 44;

    public static final TalonFXConfiguration pivotConfigs = new TalonFXConfiguration()
        .withMotorOutput(new MotorOutputConfigs().withInverted(InvertedValue.Clockwise_Positive));
    
    public static final TalonFXConfiguration rollerConfigs = new TalonFXConfiguration()
        .withMotorOutput(new MotorOutputConfigs().withInverted(InvertedValue.Clockwise_Positive));

    public static final CurrentLimitsConfigs pivotCurrentLimits = new CurrentLimitsConfigs()
        .withStatorCurrentLimit(120)
        .withSupplyCurrentLimit(40)
        .withStatorCurrentLimitEnable(true)
        .withSupplyCurrentLimitEnable(true);

    public static final CurrentLimitsConfigs rollerCurrentLimits = new CurrentLimitsConfigs()
        .withStatorCurrentLimit(120)
        .withSupplyCurrentLimit(40)
        .withStatorCurrentLimitEnable(true)
        .withSupplyCurrentLimitEnable(true);

}
