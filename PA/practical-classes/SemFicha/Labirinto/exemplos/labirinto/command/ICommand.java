package exemplos.labirinto.command;

public interface ICommand {
    // Operação representada por este comando
    //
    // Retorna true se a operação foi realizada com sucesso
    // E se a operação de undo for possível
    boolean execute();

    // Undo correspondente a esta operação
    //
    // Retorna true se o undo teve sucesso
    boolean undo();
}
