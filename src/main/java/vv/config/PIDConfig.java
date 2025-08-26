package vv.config;

import java.util.Objects;
import java.util.Properties;

import static edu.wpi.first.math.util.Units.degreesToRadians;
import static vv.config.PropertyReaders.readDoubleProperty;

/**
 * @param translationalConstraints Constraints for translational movement (meters, m/s, m/s^2).
 * @param rotationalConstraints Constraints for rotational movement (radians, rad/s, rad/s^2).
 */
public record PIDConfig(
    PIDConstraints translationalConstraints,
    PIDGains translationalGains,
    PIDConstraints rotationalConstraints,
    PIDGains rotationalGains
) {
    public PIDConfig {
        Objects.requireNonNull(translationalConstraints);
        Objects.requireNonNull(translationalGains);
        Objects.requireNonNull(rotationalConstraints);
        Objects.requireNonNull(rotationalGains);
    }

    public static PIDConfig fromProperties(Properties props) {
        var degConstraints = PIDConstraints.fromProperties(props, "rotational");
        var radConstraints = new PIDConstraints(
            degreesToRadians(degConstraints.tolerance()),
            degreesToRadians(degConstraints.maxV()),
            degreesToRadians(degConstraints.maxA())
        );
        return new PIDConfig(
            PIDConstraints.fromProperties(props, "translational"),
            PIDGains.fromProperties(props, "translational"),
            radConstraints,
            PIDGains.fromProperties(props, "rotational")
        );
    }

    public static record PIDConstraints(
        Double tolerance,
        Double maxV,
        Double maxA
    ) {
        public PIDConstraints {
            Objects.requireNonNull(tolerance);
            Objects.requireNonNull(maxV);
            Objects.requireNonNull(maxA);
        }

        static PIDConstraints fromProperties(Properties props, String type) {
            var prefix = String.format("pid.%s.", type);
            return new PIDConstraints(
                readDoubleProperty(props, prefix + "tolerance"),
                readDoubleProperty(props, prefix + "max.v"),
                readDoubleProperty(props, prefix + "max.a")
            );
        }
    }

    public static record PIDGains(
        Double kP,
        Double kI,
        Double kD
    ) {
        public PIDGains {
            Objects.requireNonNull(kP);
            Objects.requireNonNull(kI);
            Objects.requireNonNull(kD);
        }

        static PIDGains fromProperties(Properties props, String type) {
            var prefix = String.format("pid.%s.", type);
            return new PIDGains(
                readDoubleProperty(props, prefix + "kp"),
                readDoubleProperty(props, prefix + "ki"),
                readDoubleProperty(props, prefix + "kd")
            );
        }
    }
}
