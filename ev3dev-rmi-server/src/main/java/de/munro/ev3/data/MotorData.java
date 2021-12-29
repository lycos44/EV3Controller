package de.munro.ev3.data;

import de.munro.ev3.rmi.RemoteEV3;
import lombok.extern.slf4j.Slf4j;

import javax.naming.InvalidNameException;
import java.util.*;

@Slf4j
public class MotorData {
    private boolean toBeStopped = false;
    private Integer speed;
    private RemoteEV3.Command command;
    private RemoteEV3.Instruction instruction;
    private final Position[] positions;

    /**
     * inner class Position
     */
    private static class Position {
        private RemoteEV3.Command command;
        private Integer value;

        private Position(RemoteEV3.Command command, Integer value) {
            this.command = command;
            this.value = value;
        }

        public RemoteEV3.Command getCommand() {
            return command;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "("+command+","+value+")";
        }
    }

    /**
     * Constructor
     * @param commands to be executed by the motor
     * @param speed current speed
     */
    public MotorData(RemoteEV3.Command[] commands, int speed) {
        this.speed = speed;
        this.positions = new Position[commands.length];
        for (int i = 0;i<commands.length;i++) {
            this.positions[i] = new Position(commands[i], 0);
        }
    }

    /**
     * Gets the isToBeStopped
     * @return isToBeStopped
     */
    public boolean isToBeStopped() {
        return toBeStopped;
    }

    /**
     * Sets the toBeStopped
     * @param toBeStopped
     */
    public void setToBeStopped(boolean toBeStopped) {
        this.toBeStopped = toBeStopped;
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
    public Integer getPosition(RemoteEV3.Command cmd) throws InvalidNameException {
        for (Position pos : positions) {
            if (pos.getCommand() == cmd) {
                return pos.getValue();
            }
        }

        InvalidNameException invalidNameException = new InvalidNameException("Unknown command: " + cmd);
        invalidNameException.printStackTrace();
        throw invalidNameException;
    }

    /**
     * Sets the position for cmd
     * @param cmd command
     * @param value value
     */
    public void setPosition(RemoteEV3.Command cmd, Integer value) throws InvalidNameException {
        for (Position pos : positions) {
            if (pos.getCommand() == cmd) {
                pos.setValue(value);
                return;
            }
        }

        InvalidNameException invalidNameException = new InvalidNameException("Unknown command: " + cmd);
        invalidNameException.printStackTrace();
        throw invalidNameException;
    }

    /**
     * Sets the positions
     * @param properties properties
     */
    public void setPositions(Properties properties) {
        for (Position pos : positions) {
            Integer value = Integer.parseInt(properties.get(pos.getCommand().toString()).toString());
            pos.setValue(value);
            log.debug("setPositions: {},{}", pos.getCommand(), pos.getValue());
        }
    }

    /**
     * Gets the positions
     * @return positions
     */
    public Position[] getPositions() {
        return positions;
    }

    /**
     * Gets the properties
     * @return properties
     */
    public Properties getProperties() throws InvalidNameException {
        Properties properties = new Properties();
        for (Position pos : getPositions()) {
            log.debug("getPositions: {},{}", pos.getCommand(), pos.getValue());
            properties.put(pos.getCommand().toString(), pos.getValue().toString());
        }
        return properties;
    }

    /**
     * check all properties
     * @param properties for motorData members
     * @return true, if all properties are set
     */
    public boolean verify(Properties properties) {
        for (Position position : getPositions()) {
            if (properties.get(position.getValue().toString()) == null) {
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
        for (Position pos : positions) {
            posStr += ","+pos.toString();
        }
        posStr = posStr.replaceFirst(",","") +"]";
        return "{" + "\n" +
                "\ttoBeStopped: " + toBeStopped + "\n" +
                "\tcommand:     " + command + "\n" +
                "\tspeed        " + speed + "\n" +
                "\tpositions:   " + posStr + "\n" +
                "}";
    }
}
