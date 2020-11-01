package co.sumup.jobprocessing.task;

import javax.validation.Valid;
import java.util.List;

/**
 * Represents a list of tasks
 */
public class TaskList {

    @Valid
    private List<DependentTask> tasks;

    public TaskList() {
    }

    public TaskList(List<DependentTask> dependentTasks) {
        this.tasks = dependentTasks;
    }

    public List<DependentTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<DependentTask> dependentTasks) {
        this.tasks = dependentTasks;
    }
}
