package co.sumup.jobprocessing.exception;

/**
 * Thrown when there are duplicated keys.
 */
public class DublicateKeyException extends RuntimeException {

    /**
     * Creates {@link DublicateKeyException}
     *
     * @param message exception message
     */
    public DublicateKeyException(String message) {
        super(message);
    }
}
