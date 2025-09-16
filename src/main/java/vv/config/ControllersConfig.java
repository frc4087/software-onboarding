
package vv.config;

import java.util.Objects;
import java.util.Properties;

import static vv.config.PropertyReaders.readDoubleProperty;
import static vv.config.PropertyReaders.readIntegerProperty;

public record ControllersConfig(
    Double rumbleIntensity,
    DriverConfig driver,
    OperatorConfig operator
) {
    public ControllersConfig {
        Objects.requireNonNull(rumbleIntensity, "Rumble intensity cannot be null");
        assert rumbleIntensity >= 0 && rumbleIntensity <= 1 : "Rumble intensity must be between 0 and 1";
        Objects.requireNonNull(driver, "Driver config cannot be null");
        Objects.requireNonNull(operator, "Driver config cannot be null");
    }

    public static ControllersConfig fromProperties(Properties props) {
        return new ControllersConfig(
            readDoubleProperty(props, "controllers.rumble.intensity"),
            DriverConfig.fromProperties(props),
            OperatorConfig.fromProperties(props)
        );
    }

    public static record DriverConfig(
        Integer port,
        Double translationalDeadband,
        Double rotationalDeadband
    ) {
        public DriverConfig {
            Objects.requireNonNull(port, "Driver port cannot be null");
            Objects.requireNonNull(translationalDeadband, "Driver translationalDeadband cannot be null");
            Objects.requireNonNull(rotationalDeadband, "Driver rotationalDeadband cannot be null");
        }
    
        public static DriverConfig fromProperties(Properties props) {
            return new DriverConfig(
                readIntegerProperty(props, "controllers.driver.port"),
                readDoubleProperty(props, "controllers.driver.translational.deadband"),
                readDoubleProperty(props, "controllers.driver.rotational.deadband")
            );
        }
    }

    public static record OperatorConfig(
        Integer port,
        Double translationalDeadband,
        Double rotationalDeadband
    ) {
        public OperatorConfig {
            Objects.requireNonNull(port, "Operator port cannot be null");
            Objects.requireNonNull(translationalDeadband, "Operator translationalDeadband cannot be null");
            Objects.requireNonNull(rotationalDeadband, "Operator rotationalDeadband cannot be null");
        }
    
        public static OperatorConfig fromProperties(Properties props) {
            return new OperatorConfig(
                readIntegerProperty(props, "controllers.operator.port"),
                readDoubleProperty(props, "controllers.operator.translational.deadband"),
                readDoubleProperty(props, "controllers.operator.rotational.deadband")
            );
        }
    }
}
