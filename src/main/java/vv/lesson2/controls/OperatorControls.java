package vv.lesson2.controls;

import java.util.function.Consumer;

import edu.wpi.first.wpilibj.simulation.XboxControllerSim;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import vv.config.VVConfig;
import vv.lesson2.climber.Climber;

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

    public void setupTriggers(Climber climber) {
        controller.povUp().whileTrue(climber.runPivotForward());
        controller.povDown().whileTrue(climber.runPivotBackward());
        controller.povRight().whileTrue(climber.runRollers());
    }

    public CommandXboxController controls() {
        return controller;
    }

    public void simulate(Consumer<XboxControllerSim> consumer) {
        consumer.accept(sim);
        sim.notifyNewData();
    }
}