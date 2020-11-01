package co.sumup.jobprocessing.task;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Represents a task with its dependencies
 */
public class DependentTask {

    @NotNull
    private String name;

    @NotNull
    private String command;

    private List<String> requires;

    public DependentTask() {
    }

    public DependentTask(String name, String command, List<String> requires) {
        this.name = name;
        this.command = command;
        this.requires = requires;
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

    public List<String> getRequires() {
        return requires;
    }

    public void setRequires(List<String> requires) {
        this.requires = requires;
    }
}
