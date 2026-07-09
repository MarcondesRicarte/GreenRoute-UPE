package br.upe.greenroute.exceptions;

public class GreenRouteException extends RuntimeException {
    public GreenRouteException(String message) {
        super((message));
    }
    public GreenRouteException(String message, Throwable cause) {
        super(message, cause);
    }
}