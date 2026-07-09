package exceptions;

public class VeiculoNaoEncontradoException extends RuntimeException {
    public VeiculoNaoEncontradoException(int id) {
        super("Veículo com ID " + id + " não encontrado.");
    }
}