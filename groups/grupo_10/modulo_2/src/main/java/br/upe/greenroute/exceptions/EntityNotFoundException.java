package br.upe.greenroute.exceptions;

public class EntityNotFoundException extends GreenRouteException{
    public EntityNotFoundException(String entityType, int id) {
    super(entityType+" com ID: "+id+" não foi encontrado(a) no sistema!");
    }
}
