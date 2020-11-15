package de.munro.ev3.motor;

import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public abstract class Motor {
    private static final Logger LOG = LoggerFactory.getLogger(Motor.class);
    protected static final String DOWN_POSITION = "downPosition";
    protected static final String UP_POSITION = "upPosition";
    protected static final String LEFTMOST_POSITION = "leftmostPosition";
    protected static final String RIGHTMOST_POSITION = "rightmostPosition";
    protected static final String HOME_POSITION = "homePosition";

    public enum Rotation {
        ahead,
        reverse,
        stalled
    }

    public enum Polarity {
        NORMAL,
        INVERSED
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
    private Properties properties = new Properties();

    /**
     * Constructor
     */
    public Motor(Polarity polarity, MotorType motorType) {
        this.polarity = polarity;
        this.motorType = motorType;
    }

    /**
     * @return properties config values
     */
    public Properties getProperties() {
        return properties;
    };

    /**
     * information about the member motor instance
     *
     * @return true, if the member motor is not null
     */
    public boolean isInitialized() {
        LOG.debug("isInitialized()");
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
        LOG.debug("forward.polarity: {}", getPolarity());
        setRotation(Rotation.stalled);
        switch (getPolarity()) {
            case NORMAL:
                LOG.debug("getMotor().forward()");
                getMotor().forward();
                break;
            case INVERSED:
                LOG.debug("getMotor().backward()");
                getMotor().backward();
                break;
        }
    }

    /**
     * calls @link BaseRegulatedMotor#backward() or @link BaseRegulatedMotor#forward()
     * depending on the polarity of the current motor instance
     */
    public void backward() {
        LOG.debug("backward.polarity: {}", getPolarity());
        switch (getPolarity()) {
            case NORMAL:
                LOG.debug("getMotor().backward()");
                getMotor().backward();
                break;
            case INVERSED:
                LOG.debug("getMotor().forward()");
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
        LOG.debug("rotateTillStopped()");
        if (getRotation().equals(rotation)) {
            LOG.debug("Tried to rotate in stalled direction");
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
        LOG.debug("{}.stop()", getMotorType());
        getMotor().stop();
    }

    /**
     * @link BaseRegulatedMotor#brake()
     */
    public void brake() {
        LOG.debug("{}.brake()", getMotorType());
        getMotor().brake();
    }

    /**
     * @link BaseRegulatedMotor#rotateTo()
     */
    public void rotateTo(int angle) {
        LOG.debug("rotate({})", angle);
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
        LOG.debug("resetTachoCount()");
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

    private String getPropertiesFilename(Class clazz) {
        return "config/"+clazz.getSimpleName() + ".properties";
    }

    /**
     * read status information of the motor to the property file
     */
    public boolean readPropertyFile() {
        LOG.debug("readPropertyFile()");
        try (InputStream inputStream = new FileInputStream(getPropertiesFilename(this.getClass()))) {

            getProperties().load(inputStream);
            LOG.debug("properties({})", getProperties());

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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
}
