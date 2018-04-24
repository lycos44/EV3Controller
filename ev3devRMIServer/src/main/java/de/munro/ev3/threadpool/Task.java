package de.munro.ev3.threadpool;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Task {

    public enum MotorType {
        camera,
        climb,
        drive,
        steering
    }
    public enum ActionType {
        init
    }
    private ActionType actionType;
    private Map<MotorType, Boolean> assignedTo;

    public Task(ActionType actionType, MotorType ... types) {
        this.actionType = actionType;
        assignedTo = new HashMap<>();
        Arrays.stream(MotorType.values()).forEach(motorType -> assignedTo.put(motorType, false));
        Arrays.stream(types).forEach(motorType -> assignedTo.replace(motorType, true));
    }

    public boolean isAssignedTo(MotorType motorType) {
        return assignedTo.get(motorType);
    }

    public void unsetAssignedTo(MotorType motorType) {
        assignedTo.replace(motorType, false);
    }

    public boolean isDone() {
        return !assignedTo.values().stream().anyMatch(a -> a);
    }

    public ActionType getActionType() {
        return actionType;
    }
}
