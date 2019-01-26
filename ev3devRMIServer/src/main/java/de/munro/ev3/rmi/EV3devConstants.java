package de.munro.ev3.rmi;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

public class EV3devConstants {
    public static Port BACKWARD_SENSOR_PORT = SensorPort.S1;
    public static Port CAMERA_SENSOR_PORT = SensorPort.S2;
    public static Port COLOR_SENSOR_PORT = SensorPort.S3;
    public static Port DISTANCE_SENSOR_PORT = SensorPort.S4;

    public static Port DRIVE_MOTOR_PORT = MotorPort.A;
    public static Port CLIMB_MOTOR_PORT = MotorPort.B;
    public static Port STEERING_MOTOR_PORT = MotorPort.C;
    public static Port CAMERA_MOTOR_PORT = MotorPort.D;

    public static int SYSTEM_FINISHED_SUCCESSFULLY = 0;
    public static int SYSTEM_UNEXPECTED_ERROR = -1;
}
