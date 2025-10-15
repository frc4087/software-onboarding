package vv.lesson4;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import vv.lesson4.commands.MakeSwerveRequest;
import vv.lesson4.commands.MoveRobotRelative;
import vv.lesson4.drivetrain.Drivetrain;
import vv.lesson4.drivetrain.DrivetrainFactory;
import static vv.utils.TestSetup.CONFIG;
import static vv.utils.TestSetup.isFinished;
import static vv.utils.TestSetup.resetSimulationState;

public class DrivetrainTests {
    
    Drivetrain drivetrain;

    @BeforeEach
    @SuppressWarnings("unused")
    void beforeEach() {
        resetSimulationState();
        drivetrain = DrivetrainFactory.createDrivetrain(CONFIG);
        drivetrain.register();
    }

    @Test
    @Disabled
    /**
     * Enable this test to see the inaccuracy of the open-loop request.
     * See {@link DrivetrainTests#testMoveForward1MeterCloseLoop} for an
     * example of a closed-loop request that works correctly.
     */
    void testMoveForward1MeterOpenLoop() {        
        // Arrange
        var targetDistance = 1;
        SwerveRequest forwardRequest = new SwerveRequest.RobotCentric()
            .withVelocityX(1.0)
            .withVelocityY(0.0)  
            .withRotationalRate(0.0);

        Command cmd = new MakeSwerveRequest(drivetrain, forwardRequest).withTimeout(targetDistance);
        double startX = drivetrain.getState().Pose.getMeasureX().baseUnitMagnitude();
        double startY = drivetrain.getState().Pose.getMeasureY().baseUnitMagnitude();
        double startRotation = drivetrain.getState().Pose.getRotation().getDegrees();

        // Act
        drivetrain.logPose();
        CommandScheduler.getInstance().schedule(cmd);
        await().atMost(5, TimeUnit.SECONDS).until(() -> {
            CommandScheduler.getInstance().run();
            return isFinished(cmd);
        });
        drivetrain.logPose();

        // Assert
        double finalX = drivetrain.getState().Pose.getMeasureX().baseUnitMagnitude();
        double finalY = drivetrain.getState().Pose.getMeasureY().baseUnitMagnitude();
        double finalRotation = drivetrain.getState().Pose.getRotation().getDegrees();
        
        System.out.println("Rel Error X: " + (finalX - startX - targetDistance)/targetDistance * 100);

        assertEquals(targetDistance, Math.abs(finalX - startX), 0.5, "Didn't move far enough in X");
        assertEquals(startY, finalY, 0.5, "Should not have moved in Y");
        assertEquals(startRotation, finalRotation, 1, "Should not have rotated");    
    }

        /**
     * A closed-loop version of {@link DrivetrainTests#testMoveForward1MeterOpenLoop}.
     * This uses default PID controllers in {@link MoveRobotRelative} to calculate
     * the required velocities and to determine whether the the robot has moved far
     * enough.
     */
    @Test
    void testMoveForward1MeterCloseLoop() {        
        // Arrange
        long targetDistance = 1;
        var move = new Transform2d(new Translation2d(targetDistance, 0), Rotation2d.kZero);
        Command cmd = new MoveRobotRelative(CONFIG, drivetrain, move);
        
        double startX = drivetrain.getState().Pose.getMeasureX().baseUnitMagnitude();
        double startY = drivetrain.getState().Pose.getMeasureY().baseUnitMagnitude();
        double startRotation = drivetrain.getState().Pose.getRotation().getDegrees();
        drivetrain.logPose();

        // Act
        CommandScheduler.getInstance().schedule(cmd);
        try {
            await().atMost(5, TimeUnit.SECONDS).until(() -> {
                CommandScheduler.getInstance().run();
                return isFinished(cmd);
            });
        } catch (Exception e) {
            System.err.println("Command did not finish in time: " + e.getMessage());
        } finally {
            System.out.println("=== Final Pose ===");
            drivetrain.logPose();
        }

        // Assert
        double finalX = drivetrain.getState().Pose.getMeasureX().baseUnitMagnitude();
        double finalY = drivetrain.getState().Pose.getMeasureY().baseUnitMagnitude();
        double finalRotation = drivetrain.getState().Pose.getRotation().getDegrees();
        
        System.out.println("Rel Error X: " + (finalX - startX - targetDistance)/targetDistance * 100 + "%");

        assertEquals(targetDistance, Math.abs(finalX - startX), .1, "Didn't move far enough in X");
        assertEquals(startY, finalY, .1, "Should not have moved in Y");
        assertEquals(startRotation, finalRotation, 5, "Should not have rotated");    
    }
}
