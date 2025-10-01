package vv.lesson3.controls;

import java.util.function.Consumer;

import edu.wpi.first.wpilibj.simulation.XboxControllerSim;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import vv.config.VVConfig;
import vv.lesson3.arm.ArmSubsystem;

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

    public void setupTriggers(ArmSubsystem arm) {
        controller.x().onTrue(arm.snapUp());
		controller.b().onTrue(arm.snapDown());
		controller.leftTrigger().whileTrue(arm.runUp());
    }

    public CommandXboxController controls() {
        return controller;
    }

    public void simulate(Consumer<XboxControllerSim> consumer) {
        consumer.accept(sim);
        sim.notifyNewData();
    }
}