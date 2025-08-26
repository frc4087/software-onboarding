package vv.config;

import java.util.Objects;
import java.util.Properties;

import static edu.wpi.first.units.Units.Hertz;
import edu.wpi.first.units.measure.Frequency;
import static vv.config.PropertyReaders.readDoubleProperty;

public record SimulationConfig(
    Frequency signalUpdateFreq,
    Frequency simLoopPeriodFreq
) {
    public SimulationConfig {
        Objects.requireNonNull(signalUpdateFreq);
        Objects.requireNonNull(simLoopPeriodFreq);
    }

    public static SimulationConfig fromProperties(Properties props) {
        return new SimulationConfig(
            Hertz.of(readDoubleProperty(props, "simulation.signal.update.hz")),
            Hertz.of(readDoubleProperty(props, "simulation.loop.update.hz"))
        );
    }
}
