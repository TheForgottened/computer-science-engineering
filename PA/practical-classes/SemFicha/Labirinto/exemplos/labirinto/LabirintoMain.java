package exemplos.labirinto;

import exemplos.labirinto.modelo.LabirintoGame;
import exemplos.labirinto.texto.VistaLabirintoTexto;

public class LabirintoMain {
    public static void main(String[] args) {
        GestaoLabirintoGame jogo = new GestaoLabirintoGame(3.6);
        VistaLabirintoTexto vista = new VistaLabirintoTexto(jogo);
        vista.corre();
    }
}
