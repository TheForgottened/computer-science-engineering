package pt.isec.forgotten.f01e02;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bruh, isto é mesmo uma balada madjé");

        Scanner sc = new Scanner(System.in);

        int menor = 1, maior = 100, hipotese, resp;

        do {
            hipotese = (maior + menor) / 2;

            System.out.println("O número é o " + hipotese + "? " + "[0 - Sim; 1 - Não, é menor; 2 - Não, é maior]");

            try {
                resp = sc.nextInt();
            } catch (Exception a) {
                resp = -1;
            }

            switch (resp) {
                case 1 -> maior = hipotese - 1;
                case 2 -> menor = hipotese + 1;
            }
        } while (resp != 0 && maior >= menor);

        if (resp == 0)
            System.out.println("Ok... Descobri... O número era: " + hipotese);
        else
            System.out.println("Mentiroso!!");
    }
}
