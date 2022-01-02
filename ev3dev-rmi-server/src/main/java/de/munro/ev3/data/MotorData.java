package de.munro.ev3.data;

import de.munro.ev3.rmi.RemoteEV3;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MotorData {

    public enum MotorStatus {
        prepared,
        running,
        toBeStopped,
        stopped
    }

    private MotorStatus motorStatus;
    private Integer speed;
    private RemoteEV3.Command command;
    private RemoteEV3.Instruction instruction;
    private final Map<RemoteEV3.Command, Integer> positions;

    /**
     * Constructor
     * @param commands to be executed by the motor
     * @param speed current speed
     */
    public MotorData(RemoteEV3.Command[] commands, int speed) {
        this.speed = speed;
        this.positions = new ConcurrentHashMap<>();

        for (RemoteEV3.Command command : commands) {
            positions.put(command, 0);
        }
    }

    /**
     * Gets the motor status
     * @return motorStatus
     */
    public MotorStatus getMotorStatus() {
        return motorStatus;
    }

    /**
     * Sets the motor status
     * @param motorStatus motor status
     */
    public void setMotorStatus(MotorStatus motorStatus) {
        this.motorStatus = motorStatus;
    }

    /**
     * Gets the speed
     * @return speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Sets the speed
     * @param speed in degrees per second
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Gets the command
     * @return command
     */
    public RemoteEV3.Command getCommand() {
        return this.command;
    }

    /**
     * Sets the command
     * @param cmd command
     */
    public void setCommand(RemoteEV3.Command cmd) {
        this.command = cmd;
    }

    /**
     * Gets the instruction
     * @return instruction
     */
    public RemoteEV3.Instruction getInstruction() {
        return instruction;
    }

    /**
     * Sets the instruction
     * @param instruction instruction
     */
    public void setInstruction(RemoteEV3.Instruction instruction) {
        this.instruction = instruction;
    }

    /**
     * Gets the position
     * @param cmd command
     * @return value of cmd position
     */
    public Integer getPosition(RemoteEV3.Command cmd) {
        return positions.get(cmd);
    }

    /**
     * Sets the position for cmd
     * @param cmd command
     * @param value value
     */
    public void setPosition(RemoteEV3.Command cmd, Integer value) {
        positions.replace(cmd, value);
    }

    /**
     * writes current settings to property file
     */
    public void write() {
    }

    /**
     * Sets the positions
     * @param properties properties
     */
    public void setPositions(Properties properties) {
        for (RemoteEV3.Command cmd : positions.keySet()) {
            Integer value = Integer.parseInt(properties.get(cmd.toString()).toString());
            positions.replace(cmd, value);
        }
    }

    /**
     * Gets the positions
     * @return positions
     */
    public Map<RemoteEV3.Command, Integer> getPositions() {
        return positions;
    }

    /**
     * Gets the properties
     * @return properties
     */
    public Properties getProperties() {
        Properties properties = new Properties();
        for (RemoteEV3.Command cmd : getPositions().keySet()) {
            log.debug("getPositions: {},{}", cmd, positions.get(cmd));
            properties.put(cmd.toString(), positions.get(cmd).toString());
        }
        return properties;
    }

    /**
     * check all properties
     * @param properties for motorData members
     * @return true, if all properties are set
     */
    public boolean verify(Properties properties) {
        for (RemoteEV3.Command cmd : getPositions().keySet()) {
            if (properties.get(cmd.toString()) == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * @link Object#toString
     */
    @Override
    public String toString() {
        String posStr = "[";
        for (RemoteEV3.Command cmd : positions.keySet()) {
            posStr += ", ("+cmd+","+positions.get(cmd)+")";
        }
        posStr = posStr.replaceFirst(",","") +"]";
        return "{" + "\n" +
                "\tmotorStatus: " + motorStatus + "\n" +
                "\tcommand:     " + command + "\n" +
                "\tspeed        " + speed + "\n" +
                "\tpositions:   " + posStr + "\n" +
                "}";
    }
}
