package vv;

import edu.wpi.first.wpilibj.RobotBase;
import vv.lesson3.Robot;

public class Main {
    public static void main(String[] args) {
        // Change the import (vv.lessonX.Robot) for each meeting
        RobotBase.startRobot(Robot::start);
    }
}
