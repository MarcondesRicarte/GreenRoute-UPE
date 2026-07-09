package br.upe.greenroute.exceptions;

public class AIServiceException extends GreenRouteException {
    public AIServiceException(String message) {
        super(message);
    }

    public AIServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
