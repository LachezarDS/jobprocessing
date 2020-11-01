package co.sumup.jobprocessing.exception;

/**
 * Thrown when a wrong task name is passed as dependency of a task.
 */
public class NoSuchTaskException extends RuntimeException {

    /**
     * Creates {@link NoSuchTaskException}
     *
     * @param taskName name of the task which was not found
     */
    public NoSuchTaskException(String taskName) {
        super(String.format("There is no task with name '%s'.", taskName));
    }
}
