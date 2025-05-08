package fr.globalmarket.adapter.exception;

public class RepositoryOperationException extends RuntimeException {

    private static final String REPOSITORY_ERROR_MESSAGE = "Repository Error : ";

    public RepositoryOperationException(String message, Throwable cause) {
        super(REPOSITORY_ERROR_MESSAGE + message, cause);
    }

}
