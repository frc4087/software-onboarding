package vv.lesson1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import vv.lesson1.controls.DriverControls;
import vv.lesson1.controls.OperatorControls;
import static vv.utils.TestSetup.CONFIG;
import static vv.utils.TestSetup.resetSimulationState;

public class ControllerTest {

    DriverControls driverControls;
    OperatorControls operatorControls;

    @BeforeEach
    @SuppressWarnings("unused")
    void beforeEach() {
        resetSimulationState();
        driverControls = new DriverControls(CONFIG);
        operatorControls = new OperatorControls(CONFIG);
    }

    @Test
    void opShouldVibrateDriverOnBumperPress() {
        // Arrange
        operatorControls.setupTriggers(driverControls);

        // Act
        operatorControls.simulate((c) -> c.setRightBumperButton(true));
        CommandScheduler.getInstance().run();

        // Assert
        assertEquals(
            true,
            operatorControls.controls().getHID().getRightBumperButton(),
            "Operator controller should have right bumper pressed"
        );
        assertEquals(
            CONFIG.controllers().rumbleIntensity(),
            driverControls.sim().getRumble(GenericHID.RumbleType.kBothRumble),
            0.01,
            "Driver controller should be rumbling"
        );
    }

    @Test
    void opShouldStopVibratingDriverOnBumperRelease() {
        // Arrange
        //operatorControls.setupTriggers(driverControls, null);
        //operatorControls.simulate((c) -> c.setRightBumperButton(true));
        CommandScheduler.getInstance().run();

        // Act
        operatorControls.simulate((c) -> c.setRightBumperButton(false));
        CommandScheduler.getInstance().run();

        // Assert
        assertEquals(
            false,
            operatorControls.controls().getHID().getRightBumperButton(),
            "Operator controller should not have right bumper pressed"
        );
        assertEquals(
            0,
            driverControls.sim().getRumble(GenericHID.RumbleType.kBothRumble),
            0.01,
            "Driver controller should not be rumbling"
        );
    }

}