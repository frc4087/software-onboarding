
package vv.config;

import java.util.Objects;
import java.util.Properties;

import static vv.config.PropertyReaders.readDoubleProperty;
import static vv.config.PropertyReaders.readIntegerProperty;
import static vv.config.PropertyReaders.readBooleanProperty;

public record ClimberConfig(
    PivotConfig pivotConfig,
    RollerConfig rollerConfig
) {
    public ClimberConfig {
        Objects.requireNonNull(pivotConfig, "Pivot config cannot be null");
        Objects.requireNonNull(rollerConfig, "Roller config cannot be null");
    }

    public static ClimberConfig fromProperties(Properties props) {
        return new ClimberConfig(
            PivotConfig.fromProperties(props),
            RollerConfig.fromProperties(props)
        );
    }

    public static record PivotConfig(
        Integer id,
        Double speed,
        Boolean inverted,
        Double statorCurrentLimit,
        Double supplyCurrentLimit
    ) {
        public PivotConfig {
            Objects.requireNonNull(id, "Pivot id cannot be null");
            Objects.requireNonNull(speed, "Pivot speed cannot be null");
            Objects.requireNonNull(inverted, "Pivot inverted cannot be null");
            Objects.requireNonNull(statorCurrentLimit, "Pivot statorCurrentLimit cannot be null");
            Objects.requireNonNull(supplyCurrentLimit, "Pivot supplyCurrentLimit cannot be null");
        }
    
        public static PivotConfig fromProperties(Properties props) {
            return new PivotConfig(
                readIntegerProperty(props, "climber.pivot.id"),
                readDoubleProperty(props, "climber.pivot.speed"),
                readBooleanProperty(props, "climber.pivot.inverted"),
                readDoubleProperty(props, "climber.pivot.current.stator.limit.amps"),
                readDoubleProperty(props, "climber.pivot.current.supply.limit.amps")
            );
        }
    }

    public static record RollerConfig(
        Integer id,
        Double speed,
        Boolean inverted,
        Double statorCurrentLimit,
        Double supplyCurrentLimit
    ) {
        public RollerConfig {
            Objects.requireNonNull(id, "Roller id cannot be null");
            Objects.requireNonNull(speed, "Roller speed cannot be null");
            Objects.requireNonNull(inverted, "Roller inverted cannot be null");
            Objects.requireNonNull(statorCurrentLimit, "Roller statorCurrentLimit cannot be null");
            Objects.requireNonNull(supplyCurrentLimit, "Roller supplyCurrentLimit cannot be null");
        }
    
        public static RollerConfig fromProperties(Properties props) {
            return new RollerConfig(
                readIntegerProperty(props, "climber.roller.id"),
                readDoubleProperty(props, "climber.roller.speed"),
                readBooleanProperty(props, "climber.roller.inverted"),
                readDoubleProperty(props, "climber.roller.current.stator.limit.amps"),
                readDoubleProperty(props, "climber.roller.current.supply.limit.amps")
            );
        }
    }
}
