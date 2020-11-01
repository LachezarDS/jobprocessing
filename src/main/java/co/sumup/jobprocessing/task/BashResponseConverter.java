package co.sumup.jobprocessing.task;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class converts the response body to bash script
 */
@Service
public class BashResponseConverter implements ResponseConverter {

    /**
     * Converts a list of {@link SimpleTask} to a bash script
     * @param tasks list of {@link SimpleTask}
     * @return bash script as string
     */
    @Override
    public String convert(List<SimpleTask> tasks) {
        StringBuilder sb = new StringBuilder("#!/usr/bin/env bash\n\n");
        for (SimpleTask task : tasks) {
            sb.append(task.getCommand()).append("\n");
        }
        return sb.toString();
    }
}
