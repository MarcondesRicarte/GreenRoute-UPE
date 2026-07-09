package exceptions;

public class EletropostoNaoEncontradoException extends RuntimeException {
    public EletropostoNaoEncontradoException(int id) {
        super("Eletroposto com ID " + id + " não encontrado.");
    }
}