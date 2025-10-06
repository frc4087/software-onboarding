package vv.config;

import java.util.Objects;
import java.util.Properties;

import static edu.wpi.first.units.Units.Amps;
import edu.wpi.first.units.measure.Current;
import static vv.config.PropertyReaders.readDoubleProperty;
import static vv.config.PropertyReaders.readIntegerProperty;

public record ArmConfig(
    MotorConfig motor,
    Double upSetpoint,
    Double downSetpoint,
    Double resetSpeed
) {
    public ArmConfig {
        Objects.requireNonNull(motor, "Arm motor config cannot be null");
        Objects.requireNonNull(upSetpoint, "Arm upSetpoint cannot be null");
        Objects.requireNonNull(downSetpoint, "Arm downSetpoint cannot be null");
        Objects.requireNonNull(resetSpeed, "Arm resetSpeed cannot be null");
    }

    public static ArmConfig fromProperties(Properties props) {
        return new ArmConfig(
            MotorConfig.fromProperties(props),
            readDoubleProperty(props, "arm.setpoint.up"),
            readDoubleProperty(props, "arm.setpoint.down"),
            readDoubleProperty(props, "arm.reset.speed")
        );
    }

    public static record MotorConfig(
        Integer port,
        Current statorCurrentLimit,
        Current supplyCurrentLimit
    ) {
        public MotorConfig {
            Objects.requireNonNull(port, "Motor port config cannot be null");
            Objects.requireNonNull(statorCurrentLimit, "statorCurrentLimit config cannot be null");
            Objects.requireNonNull(supplyCurrentLimit, "supplyCurrentLimit config cannot be null");
        }

        public static MotorConfig fromProperties(Properties props) {
            var statorCurrent = readDoubleProperty(props, "arm.motor.limits.stator.current.amps");
            var supplyCurrent = readDoubleProperty(props, "arm.motor.limits.supply.current.amps");
            
            return new MotorConfig(
                readIntegerProperty(props, "arm.motor.port"),
                Current.ofBaseUnits(statorCurrent, Amps),
                Current.ofBaseUnits(supplyCurrent, Amps)          
            );
        }
    }

}
