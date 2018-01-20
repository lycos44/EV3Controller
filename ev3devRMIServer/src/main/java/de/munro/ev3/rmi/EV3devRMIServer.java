package de.munro.ev3.rmi;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.sensors.Battery;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class EV3devRMIServer  {
    public static void main(final String[] args){

        System.out.println("Creating Motor A & B");
        //beep();
        final EV3LargeRegulatedMotor motorDrive = new EV3LargeRegulatedMotor(MotorPort.A);//drive
        final EV3LargeRegulatedMotor motorClimb = new EV3LargeRegulatedMotor(MotorPort.B);//climb

        //To Stop the motor in case of pkill java for example
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Emergency Stop");
                motorDrive.stop();
            }
        }));

        System.out.println("Defining the Stop mode");
        motorDrive.brake();

        System.out.println("Defining motor speed");
        final int motorSpeed = 200;
        motorDrive.setSpeed(motorSpeed);

        System.out.println("Go Forward with the motors");
        motorDrive.forward();

        Delay.msDelay(1000);

        System.out.println("Stop motors");
        motorDrive.stop();

        System.out.println("Go Backward with the motors");
        motorDrive.backward();

        Delay.msDelay(1000);

        System.out.println("Stop motors");
        motorDrive.stop();

        System.out.println("Checking Battery");
        System.out.println("Votage: " + Battery.getInstance().getVoltage());

        System.exit(0);
    }
}
