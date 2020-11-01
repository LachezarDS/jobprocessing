package co.sumup.jobprocessing.task;

/**
 * Represents a task
 */
public class SimpleTask {
    private String name;
    private String command;

    public SimpleTask() {
    }

    public SimpleTask(String name, String command) {
        this.name = name;
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
