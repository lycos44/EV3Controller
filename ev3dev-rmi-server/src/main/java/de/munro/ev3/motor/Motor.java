package de.munro.ev3.motor;

import de.munro.ev3.data.MotorData;
import de.munro.ev3.rmi.RemoteEV3;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class Motor {

    public enum Rotation {
        ahead,
        reverse,
        stalled
    }

    public enum Polarity {
        normal,
        inversed
    }

    private final Polarity polarity;
    private final RemoteEV3.MotorType motorType;
    private Rotation rotation = Rotation.stalled;
    private RemoteEV3.Instruction lastInstruction = null;
    private final MotorData motorData;
    private final Map<RemoteEV3.MotorType, Port> motorPorts = new ConcurrentHashMap<RemoteEV3.MotorType, Port>();

    /**
     * Constructor
     * @param polarity the motor rotates in which direction
     * @param motorType describes what kind of motor this instance realizes
     * @param motorData data
     */
    public Motor(Polarity polarity, RemoteEV3.MotorType motorType, MotorData motorData) {
        this.polarity = polarity;
        this.motorType = motorType;
        this.motorData = motorData;

        motorPorts.put(RemoteEV3.MotorType.drive, MotorPort.A);
        motorPorts.put(RemoteEV3.MotorType.liftBack, MotorPort.B);
        motorPorts.put(RemoteEV3.MotorType.steering, MotorPort.C);
        motorPorts.put(RemoteEV3.MotorType.liftFront, MotorPort.D);

        motorData.setMotorStatus(MotorData.MotorStatus.prepared);
    }

    /**
     * Gets the motorData
     * @return motorData
     */
    public MotorData getMotorData() {
        return motorData;
    }

    /**
     * react on action requests
     */
    public void takeAction() {
        if (lastInstruction != getMotorData().getInstruction()) {
            log.debug("takeAction: {}", getMotorData().getInstruction());
            lastInstruction = getMotorData().getInstruction();
        }

        Properties properties;
        switch (getMotorData().getInstruction()) {
            case perform:
                rotate(getMotorData().getCommand());
                break;
            case read:
                properties = readPropertyFile();
                if (getMotorData().verify(properties)) {
                    getMotorData().setPositions(properties);
                    log.debug(getMotorData().toString());
                }
                break;
            case write:
                properties = getMotorData().getProperties();
                if (getMotorData().verify(properties)) {
                    log.debug(properties.toString());
                    writePropertyFile(properties);
                }
                break;
        }
    }

    /**
     * Gets the motor port
     * @param motorType motor type
     * @return port
     */
    public Port getMotorPort(RemoteEV3.MotorType motorType) {
        return motorPorts.get(motorType);
    }

    /**
     * execute rotation of the motor
     * @param cmd instruction for the motor
     */
    protected void rotate(RemoteEV3.Command cmd) {
        log.debug("rotate: {}, {}", cmd, getMotorData().getPosition(cmd));
        rotateTo(getMotorData().getPosition(cmd));
    }

    /**
     * motor is ready to do his job
     * @return true, if the member motor is not null
     */
    public boolean isInitialized() {
        log.debug("isInitialized()");
        return null != getMotor();
    }

    /**
     * @link BaseRegulatedMotor#getSpeed()
     */
    public int getSpeed() {
        return getMotorData().getSpeed();
    }

    /**
     * @link BaseRegulatedMotor#setSpeed()
     */
    public void setSpeed(int speed) {
        getMotorData().setSpeed(speed);
        getMotor().setSpeed(speed);
    }

    /**
     * Get the motor
     * @return motor
     */
    abstract BaseRegulatedMotor getMotor();

    /**
     * Set the polarity
     * @return polarity
     */
    public Polarity getPolarity() {
        return polarity;
    }

    /**
     * Set the motorType
     * @return motorType
     */
    public RemoteEV3.MotorType getMotorType() {
        return motorType;
    }

    /**
     * Get the rotation
     * @return motorType
     */
    public Rotation getRotation() {
        return rotation;
    }

    /**
     * Sets the rotation
     * @param rotation direction the motor rotates
     */
    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    /**
     * @link BaseRegulatedMotor#isStalled()
     */
    public boolean isStalled() {
        return getMotor().isStalled();
    }

    /**
     * create a new motor instance
     * @return BaseRegulatedMotor
     */
    abstract BaseRegulatedMotor createMotor();
//    {
//        log.debug("createMotor: {},{}", this.getMotorType(), this.getMotorPort(this.getMotorType()));
//        try {
//            switch (this.getMotorType()) {
//                case steering:
//                case liftFront:
//                    return new EV3MediumRegulatedMotor(this.getMotorPort(this.getMotorType()));
//                case drive:
//                case liftBack:
//                    return new EV3LargeRegulatedMotor(this.getMotorPort(this.getMotorType()));
//            }
//        } catch (RuntimeException e) {
//            log.error("Create motor: ", e);
//        }
//
//        return null;
//    }

    /**
     * calls {@link BaseRegulatedMotor#backward()} or {@link BaseRegulatedMotor#forward()}
     * depending on the polarity of the current motor instance
     */
    public void forward() {
        log.debug("forward.polarity: {}", getPolarity());
        setRotation(Rotation.stalled);
        switch (getPolarity()) {
            case normal:
                log.debug("getMotor().forward()");
                getMotor().forward();
                break;
            case inversed:
                log.debug("getMotor().backward()");
                getMotor().backward();
                break;
        }
    }

    /**
     * calls @link BaseRegulatedMotor#backward() or @link BaseRegulatedMotor#forward()
     * depending on the polarity of the current motor instance
     */
    public void backward() {
        log.debug("backward.polarity: {}", getPolarity());
        switch (getPolarity()) {
            case normal:
                log.debug("getMotor().backward()");
                getMotor().backward();
                break;
            case inversed:
                log.debug("getMotor().forward()");
                getMotor().forward();
                break;
        }
    }

    /**
     * rotate until the call of is2BeStopped provides true
     *
     * @param rotation direction the motor rotates
     */
    public void rotateTillStopped(Rotation rotation) {
        log.debug("rotateTillStopped()");
        if (getRotation().equals(rotation)) {
            log.debug("Tried to rotate in stalled direction");
            return;
        }
        switch (rotation) {
            case ahead:
                forward();
                break;
            case reverse:
                backward();
        }
        while (!isStalled()) {
        }
        setRotation(rotation);
        stop();
    }

    /**
     * @link BaseRegulatedMotor#stop()
     */
    public void stop() {
        log.debug("{}.stop()", getMotorType());
        getMotor().stop();
    }

    /**
     * @link BaseRegulatedMotor#brake()
     */
    public void brake() {
        log.debug("{}.brake()", getMotorType());
        getMotor().brake();
    }

    /**
     * @link BaseRegulatedMotor#rotateTo()
     */
    public void rotateTo(int angle) {
        log.debug("rotate({})", angle);
        getMotor().rotateTo(angle);
    }

    /**
     * @link BaseRegulatedMotor#getTachoCount()
     */
    public int getTachoCount() {
        return getMotor().getTachoCount();
    }

    /**
     * @link BaseRegulatedMotor#resetTachoCount()
     */
    public void resetTachoCount() {
        log.debug("resetTachoCount()");
        getMotor().resetTachoCount();
    }

    /**
     * initialize status of motor, i.e., find home position
     */
    public abstract void init();

    /**
     * build the properties filename
     * @return properties filename
     */
    private String getPropertiesFilename() {
        return "config/" + this.getMotorType() + ".properties";
    }

    /**
     * read motor properties from property file
     * @return properties
     */
    public Properties readPropertyFile() {
        log.debug("readPropertyFile()");
        Properties properties = new Properties();
        String propertiesFilename = getPropertiesFilename();
        log.debug("propertiesFilename: {}", propertiesFilename);
        try (InputStream inputStream = new FileInputStream(propertiesFilename)) {

            properties.load(inputStream);
            log.debug("properties({})", properties);

        } catch (IOException e) {
            return null;
        }
        return properties;
    }

    /**
     * write status information of the motor to the property file
     */
    public void writePropertyFile(Properties properties) {
        File propertiesFile = new File(getPropertiesFilename());
        propertiesFile.getParentFile().mkdirs();
        try (OutputStream outputStream = new FileOutputStream(propertiesFile)) {

            properties.store(outputStream, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * convert int position to string property value
     * @param position value
     * @return converted string
     */
    protected String toString(int position) {
        return Integer.toString(position);
    }

    /**
     * @link Object#toString
     */
    @Override
    public String toString() {
        return "{\n" +
            "\tpolarity: " + polarity + "\n" +
            "\tmotorType: " + motorType + "\n" +
            "\tmotorData: " + motorData + "\n" +
            "}\n";
    }
}
