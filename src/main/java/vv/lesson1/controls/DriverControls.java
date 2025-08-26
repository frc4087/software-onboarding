package vv.lesson1.controls;

import java.util.function.Consumer;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.simulation.XboxControllerSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import vv.config.VVConfig;

public class DriverControls {
    private final CommandXboxController controller;
    private final XboxControllerSim sim;
    private final double rumbleIntensity;

    public DriverControls(
        VVConfig config
    ) {
        var driverConfig = config.controllers().driver();
        
        rumbleIntensity = config.controllers().rumbleIntensity();
        controller = new CommandXboxController(driverConfig.port());
        sim = new XboxControllerSim(controller.getHID());
    }

    public Command rumble() {
        return Commands.runEnd(
            () -> controller.getHID().setRumble(GenericHID.RumbleType.kBothRumble, rumbleIntensity),
            () -> controller.getHID().setRumble(GenericHID.RumbleType.kBothRumble, 0.0)
        );
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
