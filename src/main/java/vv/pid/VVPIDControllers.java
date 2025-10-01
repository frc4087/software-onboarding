package vv.pid;

import edu.wpi.first.math.controller.ProfiledPIDController;
import vv.config.VVConfig;

public class VVPIDControllers {
    
    public static ProfiledPIDController generalTranslationController(VVConfig config) {
        var controller = new ProfiledPIDController(
            config.pid().translationalGains().kP(),
            config.pid().translationalGains().kI(),
            config.pid().translationalGains().kD(),
            VVConstraints.generalTranlationalConstraint(config)
        );
        controller.setTolerance(config.pid().translationalConstraints().tolerance());
        return controller;
    }

    public static ProfiledPIDController generalRotationController(VVConfig config) {
        var controller = new ProfiledPIDController(
            config.pid().rotationalGains().kP(),
            config.pid().rotationalGains().kI(),
            config.pid().rotationalGains().kD(),
            VVConstraints.generalRotationalConstraint(config)
        );
        controller.enableContinuousInput(-Math.PI, Math.PI);
        controller.setTolerance(config.pid().rotationalConstraints().tolerance());
        return controller;
    }
}