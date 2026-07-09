package exceptions;

public class CidadeNaoEncontradaException extends RuntimeException {
    public CidadeNaoEncontradaException(int id) {
        super("Cidade com ID " + id + " não encontrada.");
    }
}