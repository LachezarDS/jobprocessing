package co.sumup.jobprocessing.exception;

/**
 * Thrown when a cyclic dependency is found.
 */
public class CyclicDependencyException extends RuntimeException {

    public static final String CYCLIC_DEPENDENCY_MESSAGE = "The tasks cannot be ordered due to cyclic dependency.";

    /**
     * Creates {@link CyclicDependencyException}
     */
    public CyclicDependencyException() {
        super(CYCLIC_DEPENDENCY_MESSAGE);
    }
}
