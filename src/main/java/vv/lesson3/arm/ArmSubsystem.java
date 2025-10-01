package vv.lesson3.arm;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import vv.config.ArmConfig;

public class ArmSubsystem extends SubsystemBase {
    ArmMotor motor;
    private ArmState state = ArmState.RESET;
    ArmConfig config;

    public enum ArmState {
        SNAP_UP,
        SNAP_DOWN,
        RUNNING_UP,
        RESET
    }

    public ArmSubsystem(ArmConfig config) {
        this.config = config;
        motor = new ArmMotor(config.motor());
    }

    @Override
    public void periodic() {
        switch (state) {
            case SNAP_UP -> {
                this.motor.setControlPosition(config.upSetpoint());
            }
            case SNAP_DOWN -> {
                this.motor.setControlPosition(config.downSetpoint());
            }
            case RUNNING_UP -> {
                this.motor.setSpeed(config.resetSpeed());
            }
            case RESET -> {
                this.motor.reset();
            }
        }
    }

    public ArmState getState() {
        return state;
    }

    public void setState(ArmState state) {
        this.state = state;
    }

	public Command snapUp() {
		return this.run(() -> setState(ArmState.SNAP_UP))
            .withName("Arm::SnapUp");
	}

	public Command snapDown() {
		return this.run(() -> setState(ArmState.SNAP_DOWN))
            .withName("Arm::SnapDown");
	}

	public Command runUp() {
		return this.runEnd(
                () -> setState(ArmState.RUNNING_UP),
                () -> setState(ArmState.RESET)
            )
            .withName("Arm::RunUp");
	}
}
