package vv.lesson2.controls;

import java.util.function.Consumer;

import edu.wpi.first.wpilibj.simulation.XboxControllerSim;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import vv.config.VVConfig;

public class DriverControls {
    private final CommandXboxController controller;
    private final XboxControllerSim sim;

    public DriverControls(
        VVConfig config
    ) {
        var driverConfig = config.controllers().driver();
        controller = new CommandXboxController(driverConfig.port());
        sim = new XboxControllerSim(controller.getHID());
    }

    public CommandXboxController controls() {
        return controller;
    }

    public XboxControllerSim sim() {
        return sim;
    }

    public void simulate(Consumer<XboxControllerSim> consumer) {
        consumer.accept(sim);
        sim.notifyNewData();
    }
}