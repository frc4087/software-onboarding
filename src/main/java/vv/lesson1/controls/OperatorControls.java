package vv.lesson1.controls;

import java.util.function.Consumer;

import edu.wpi.first.wpilibj.simulation.XboxControllerSim;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import vv.config.VVConfig;

public class OperatorControls {
    private final CommandXboxController controller;
    private final XboxControllerSim sim;

    public OperatorControls(
        VVConfig config
    ) {
        var opConfig = config.controllers().operator();
        
        controller = new CommandXboxController(opConfig.port());
        sim = new XboxControllerSim(controller.getHID());
    }

    public void setupTriggers(DriverControls driverControls) {
        controller.rightBumper().whileTrue(driverControls.rumble());
    }

    public CommandXboxController controls() {
        return controller;
    }

    public void simulate(Consumer<XboxControllerSim> consumer) {
        consumer.accept(sim);
        sim.notifyNewData();
    }
}
