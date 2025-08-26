package vv.pid;

import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import vv.config.VVConfig;

public class VVConstraints {
 
    public static Constraints generalTranlationalConstraint(VVConfig config) {
        var c = config.pid().translationalConstraints();
        return new Constraints(c.maxV(), c.maxA());
    }

    public static Constraints generalRotationalConstraint(VVConfig config) {
        var c = config.pid().rotationalConstraints();
        return new Constraints(c.maxV(), c.maxA());
    }
}
