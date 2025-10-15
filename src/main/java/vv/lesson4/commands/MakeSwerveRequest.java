package vv.lesson4.commands;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.Command;
import vv.lesson4.drivetrain.Drivetrain;

public class MakeSwerveRequest extends Command {
    private final Drivetrain drivetrain;
    private final SwerveRequest req;

    public MakeSwerveRequest(Drivetrain drivetrain, SwerveRequest req) {
        super();
        this.drivetrain = drivetrain;
        this.req = req;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        System.out.println("Starting MakeSwerveRequest command");
    }

    @Override
    public void execute() {
        drivetrain.setControl(req);
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("Ending MakeSwerveRequest command ");
        drivetrain.setControl(new SwerveRequest.Idle());
    }
}
