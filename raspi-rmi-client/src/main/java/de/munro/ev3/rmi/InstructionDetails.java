package de.munro.ev3.rmi;

public class InstructionDetails {

    private final static int INSTRUCTION_ID = 0;
    private final static int MOTOR_ID = 1;
    private final static int COMMAND_ID = 2;
    private final static int POSITION_ID = 3;

    private RemoteEV3.Instruction instruction;
    private RemoteEV3.MotorType motorType;
    private RemoteEV3.Command command;
    private Integer value;

    /**
     * Constructor
     * @param arguments command line arguments
     */
    public InstructionDetails(String[] arguments) {
        try {
            // instruction
            if (arguments != null && arguments.length > INSTRUCTION_ID) {
                this.instruction = RemoteEV3.Instruction.valueOf(arguments[INSTRUCTION_ID]);
            }
            // motor
            if (arguments != null && arguments.length > MOTOR_ID) {
                this.motorType = RemoteEV3.MotorType.valueOf(arguments[MOTOR_ID]);
            }
            // command
            if (arguments != null && arguments.length > COMMAND_ID) {
                this.command = RemoteEV3.Command.valueOf(arguments[COMMAND_ID]);
            }
            // value
            if (arguments != null && arguments.length > POSITION_ID) {
                this.value = Integer.valueOf(arguments[POSITION_ID]);
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
            this.command = null;
        }
    }

    /**
     * Gets the instruction
     * @return instruction
     */
    public RemoteEV3.Instruction getInstruction() {
        return instruction;
    }

    /**
     * Gets the command
     * @return command
     */
    public RemoteEV3.Command getCommand() {
        return command;
    }

    /**
     * Gets the motor type
     * @return motorType
     */
    public RemoteEV3.MotorType getMotorType() {
        return motorType;
    }

    /**
     * Gets the value
     * @return value
     */
    public Integer getValue() {
        return value;
    }

    /**
     * @link Object#toString
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("{").append("\n");
        stringBuilder
                .append("\t").append("instruction:  ").append(getInstruction()).append("\n");
        if (getMotorType() != null) stringBuilder
                .append("\t").append("motor:        ").append(getMotorType()).append("\n");
        if (getCommand() != null) stringBuilder
                .append("\t").append("command:      ").append(getCommand()).append("\n");
        if (getValue() != null) stringBuilder
                .append("\t").append("value:        ").append(getValue()).append("\n");
        stringBuilder
                .append("}").append("\n");
        return stringBuilder.toString();
    }
}
