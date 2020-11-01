package co.sumup.jobprocessing.task;

import java.util.List;

/**
 * Task sorting service
 */
public interface TaskSorter {

    /**
     * Accepts a list of {@link DependentTask} and sorts them in a new list.
     *
     * @param unorderedTasks A list of tasks
     * @return A list of sorted tasks
     */
    List<SimpleTask> sort(List<DependentTask> unorderedTasks);
}
