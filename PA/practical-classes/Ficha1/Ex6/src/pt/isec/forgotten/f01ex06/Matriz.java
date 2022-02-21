package pt.isec.forgotten.f01ex06;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Matriz {
    float [][] matriz; // default: null
    int linhas, colunas; // default: 0

    public Matriz (int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;

        matriz = new float [linhas][colunas];

        for (int i = 0; i < linhas; i++)
            Arrays.fill(matriz[i], 1.0f * i);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Matriz newm = new Matriz(linhas, colunas);

        for (int i = 0; i < linhas; i++)
            newm.matriz[i] = Arrays.copyOf(matriz[i], matriz[i].length);

        return newm;
    }

/*  public Matriz(Matriz m) {
        this.linhas = m.linhas;
        this.colunas = m.colunas;
        // this.matriz = m.matriz; // cuidado

        for (int i = 0; i < linhas; i++)
            this.matriz[i] = Arrays.copyOf(m.matriz[i], m.matriz[i].length);
    } */

/*  @Override
    public String toString() {
        return "Matriz {" +
                "matriz =" + Arrays.toString(matriz) +
                ", linhas =" + linhas +
                ", colunas =" + colunas +
                " }";
    } */

    @Override
    public String toString() {
     // String str = "";
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
             // str += matriz[i][j] + " ";
                str.append(String.format("%5.2f", matriz[i][j]));
                str.append(" ");
            }

            str.append("\n");
        }

        return str.toString();
    }

    public boolean setValor(int linha, int coluna, float valor) {
        if (linha < 0 || linha >= linhas || coluna < 0 || coluna >= colunas)
            return false;

        matriz[linha][coluna] = valor;
        return true;
    }

    public boolean acumula(Matriz m) {
        if (linhas != m.linhas || colunas != m.colunas)
            return false;

        for (int i = 0; i < linhas; i++)
            for (int j = 0; j < colunas; j++)
                matriz[i][j] += m.matriz[i][j];

        return true;
    }

    public Matriz adiciona(Matriz m) {
        if (linhas != m.linhas || colunas != m.colunas)
            return null;

        Matriz res = null;

        try {
            res = (Matriz) this.clone();
            res.acumula(m);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static Matriz adiciona(Matriz m1, Matriz m2) {
        return m1.adiciona(m2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matriz matriz1 = (Matriz) o;

        if (linhas != matriz1.linhas) return false;
        if (colunas != matriz1.colunas) return false;
        return Arrays.deepEquals(matriz, matriz1.matriz);
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(matriz);
        result = 31 * result + linhas;
        result = 31 * result + colunas;
        return result;
    }

    public int getLinhas() { return this.linhas; } // return matriz.length
    public int getColunas() { return this.colunas; } // return matriz[0].length
}
