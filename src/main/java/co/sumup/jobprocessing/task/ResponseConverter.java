package co.sumup.jobprocessing.task;

import java.util.List;

/**
 * Converts the response to a specific representation
 */
public interface ResponseConverter {

    /**
     * Converts list of {@link SimpleTask} to a specific representation
     * @param tasks list of {@link SimpleTask}
     * @return converted response as string
     */
    String convert(List<SimpleTask> tasks);
}
