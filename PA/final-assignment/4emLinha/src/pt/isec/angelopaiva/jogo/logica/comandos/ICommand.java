package pt.isec.angelopaiva.jogo.logica.comandos;

public interface ICommand {
    // Operação representada por este comando
    // Devolve true se a operação foi realizada com sucesso e se for possível dar undo
    boolean execute();

    // Undo corresponde à operação contrária representada pelo comando
    // Devolve true se foi realizado com sucesso
    boolean undo();
}
