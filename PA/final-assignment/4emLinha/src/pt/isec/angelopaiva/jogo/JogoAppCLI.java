package pt.isec.angelopaiva.jogo;

import pt.isec.angelopaiva.jogo.iu.texto.JogoCLI;
import pt.isec.angelopaiva.jogo.logica.StateMachine;

public class JogoAppCLI {
    public static void main(String[] args) {
        StateMachine sm = new StateMachine();
        JogoCLI jogoCLI = new JogoCLI(sm);

        jogoCLI.start();
    }
}
