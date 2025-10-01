package vv.lesson3.arm;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import vv.config.ArmConfig;

public class ArmMotor {
    private final TalonFX motor;
	private final MotionMagicVoltage motorControl = new MotionMagicVoltage(0);

    public ArmMotor(ArmConfig.MotorConfig config) {
        var talonFXConfigs = new TalonFXConfiguration();
		talonFXConfigs.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        talonFXConfigs.Slot0.kS = 0.0;
		talonFXConfigs.Slot0.kG = 0.25;
		talonFXConfigs.Slot0.kV = 0.0;
		talonFXConfigs.Slot0.kP = 1.875;
		talonFXConfigs.Slot0.kI = 0.0;
		talonFXConfigs.Slot0.kD = 0.0;

        talonFXConfigs.MotionMagic.MotionMagicCruiseVelocity = 160.0;
		talonFXConfigs.MotionMagic.MotionMagicAcceleration = 240.0;
		talonFXConfigs.MotionMagic.MotionMagicJerk = 3200.0;

        var limitConfigs = new CurrentLimitsConfigs();
        limitConfigs.StatorCurrentLimit = config.statorCurrentLimit().baseUnitMagnitude();
		limitConfigs.StatorCurrentLimitEnable = true;

		limitConfigs.SupplyCurrentLimit = config.supplyCurrentLimit().baseUnitMagnitude();
		limitConfigs.SupplyCurrentLimitEnable = true;

		motor = new TalonFX(config.port());
		motor.getConfigurator().apply(talonFXConfigs, 0.050);
        motor.getConfigurator().apply(limitConfigs);
        motor.setNeutralMode(NeutralModeValue.Brake);
		motor.setPosition(0.0);
    }

	/**
	 * @param relativeRotations from starting position
	 */
    public void setControlPosition(double relativeRotations) {
		motor.setControl(motorControl.withSlot(0).withPosition(relativeRotations));
    }

	/**
	 * @param speed in [-1, 1]
	 */
	public void setSpeed(double speed) {
		motor.set(speed);
	}

	public void reset() {
		motor.set(0.0);
		motor.setPosition(0.0);
	}
}
