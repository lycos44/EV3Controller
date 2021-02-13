package de.munro.ev3.motor;

import de.munro.ev3.data.MotorData;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

@Slf4j
public abstract class Motor {
    
    protected static final String DOWN_POSITION = "downPosition";
    protected static final String UP_POSITION = "upPosition";
    protected static final String LEFTMOST_POSITION = "leftmostPosition";
    protected static final String RIGHTMOST_POSITION = "rightmostPosition";
    protected static final String HOME_POSITION = "homePosition";
    protected static final String IMPROVE_HOME_POSITION = "improveHomePosition";

    public enum Rotation {
        ahead,
        reverse,
        stalled
    }

    public enum Polarity {
        normal,
        inversed
    }

    public enum MotorType {
        drive(MotorPort.A),
        climbBack(MotorPort.B),
        steering(MotorPort.C),
        climbFront(MotorPort.D);

        private final Port port;

        MotorType(Port port) {
            this.port = port;
        }

        public Port getPort() {
            return port;
        }
    }

    private final Polarity polarity;
    private final MotorType motorType;
    private Rotation rotation = Rotation.stalled;
    private final Properties properties = new Properties();

    /**
     * Constructor
     * @param polarity the motor rotates in which direction
     * @param motorType describes what kind of motor this instance realizes
     */
    public Motor(Polarity polarity, MotorType motorType) {
        this.polarity = polarity;
        this.motorType = motorType;
    }

    /**
     * react on changes in the current status
     */
    public abstract void workOutMotorData();

    /**
     * @return properties config values
     */
    public Properties getProperties() {
        return properties;
    };

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
        return getMotor().getSpeed();
    }

    /**
     * @link BaseRegulatedMotor#setSpeed()
     */
    public void setSpeed(int speed) {
        getMotor().setSpeed(speed);
    }

    /**
     * @return motor
     */
    abstract BaseRegulatedMotor getMotor();

    /**
     * @return polarity
     */
    public Polarity getPolarity() {
        return polarity;
    }

    /**
     * @return motorType
     */
    public MotorType getMotorType() {
        return motorType;
    }

    /**
     * @return motorType
     */
    public Rotation getRotation() {
        return rotation;
    }

    /**
     * @param rotation direction the motor rotates
     */
    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    /**
     * provides information about the status of the motor
     *
     * @return true, if the motor has to be stopped
     */
    abstract boolean is2BeStopped();

    /**
     * @link BaseRegulatedMotor#isStalled()
     */
    public boolean isStalled() {
        return getMotor().isStalled();
    }

    /**
     * create a new motor instance
     *
     * @return EV3MediumRegulatedMotor
     */
    abstract BaseRegulatedMotor createMotor();

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
        while (!is2BeStopped()) {
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
     * check whether all necessary properties could be read
     */
    public abstract boolean verifyProperties();

    /**
     * build the properties filename
     * @param clazz classname of the current instance
     * @return properties filename
     */
    private String getPropertiesFilename(Class clazz) {
        return "config/"+clazz.getSimpleName() + ".properties";
    }

    /**
     * read status information of the motor to the property file
     */
    public boolean readPropertyFile() {
        log.debug("readPropertyFile()");
        try (InputStream inputStream = new FileInputStream(getPropertiesFilename(this.getClass()))) {

            getProperties().load(inputStream);
            log.debug("properties({})", getProperties());

        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return verifyProperties();
    }

    /**
     * write status information of the motor to the property file
     */
    public void writePropertyFile() {
        File propertiesFile = new File(getPropertiesFilename(this.getClass()));
        propertiesFile.getParentFile().mkdirs();
        try (OutputStream outputStream = new FileOutputStream(propertiesFile)) {

            getProperties().store(outputStream, null);

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
     * concat all info interesting for logging
     * @return message to be logged
     */
    public abstract void logStatus();

    /**
     * @return current motorData
     */
    public abstract MotorData getMotorData();
}
