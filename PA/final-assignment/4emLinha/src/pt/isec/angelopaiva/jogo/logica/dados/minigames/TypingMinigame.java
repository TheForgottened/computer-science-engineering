package pt.isec.angelopaiva.jogo.logica.dados.minigames;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

public class TypingMinigame extends MinigameAdapter {
    private static final int NR_QUESTIONS = 5;

    ArrayList<String> wordList;
    String[] solutions;

    String answer;

    public TypingMinigame() {
        solutions = new String[NR_QUESTIONS];
        wordList = new ArrayList<>();

        try {
            if (!prepareWordList()) {
                useDefaultWordList();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        startTimer();
    }

    @Override
    public String getQuestion() {
        StringBuilder questions = new StringBuilder();
        int maxSeconds = 0;

        for (int i = 0; i < solutions.length; i++) {
            solutions[i] = getRandomWord();

            maxSeconds += solutions[i].length() + 1; // + 1 because of the extra " "

            questions.append(solutions[i]).append(" ");
        }

        super.setMaxSeconds(maxSeconds / 2);

        return questions.toString();
    }

    @Override
    public void setAnswer(String answer) {
        finished = true;
        stopTimer();
        this.answer = answer;
    }

    @Override
    public String getWording() { return "Escreva as palavras exatamente como são apresentadas"; }

    @Override
    public boolean hasWon() {
        if (answer == null || answer.isBlank()) return false;

        StringTokenizer st = new StringTokenizer(answer);

        for (String solution : solutions) if (st.hasMoreTokens() && !solution.equals(st.nextToken())) return false;

        return super.hasWon();
    }

    private boolean prepareWordList() throws FileNotFoundException {
        final String FILE_PATH = "5PlusWordList-PT.txt";

        File file = new File(FILE_PATH);
        if (!file.exists()) return false;

        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) wordList.add(sc.nextLine());

        sc.close();
        return true;
    }

    private String getRandomWord() { return wordList.get(ThreadLocalRandom.current().nextInt(wordList.size())); }

    public void useDefaultWordList() {
        wordList.add("preto");
        wordList.add("negro");
        wordList.add("prima");
        wordList.add("primo");
        wordList.add("gelado");
        wordList.add("música");
        wordList.add("curso");
        wordList.add("informático");
        wordList.add("tabela");
        wordList.add("programador");
        wordList.add("perdido");
        wordList.add("parede");
        wordList.add("casota");
        wordList.add("minerador");
        wordList.add("carro");
        wordList.add("carruagem");
        wordList.add("autocarro");
        wordList.add("comboio");
        wordList.add("irineu");
        wordList.add("peluche");
        wordList.add("boneco");
        wordList.add("escrever");
        wordList.add("saber");
        wordList.add("fazer");
        wordList.add("imaginar");
        wordList.add("imaginação");
        wordList.add("chorar");
        wordList.add("paragem");
        wordList.add("estrada");
        wordList.add("calculador");
        wordList.add("teclado");
        wordList.add("computador");
        wordList.add("portátil");
        wordList.add("derretido");
        wordList.add("derreter");
        wordList.add("água");
        wordList.add("garrafa");
        wordList.add("brinquedo");
        wordList.add("caneta");
        wordList.add("lápis");
        wordList.add("régua");
        wordList.add("fotografia");
        wordList.add("moldura");
        wordList.add("dormir");
        wordList.add("cobertor");
        wordList.add("condutor");
        wordList.add("conduzir");
        wordList.add("cerveja");
        wordList.add("vinho");
        wordList.add("semáforo");
        wordList.add("evento");
        wordList.add("prateleira");
        wordList.add("armário");
        wordList.add("cadeira");
        wordList.add("escritório");
        wordList.add("telhado");
        wordList.add("varanda");
        wordList.add("calçada");
        wordList.add("mirtilo");
        wordList.add("cenoura");
        wordList.add("tomate");
        wordList.add("alface");
        wordList.add("igreja");
        wordList.add("pátio");
        wordList.add("parque");
        wordList.add("festa");
        wordList.add("terra");
        wordList.add("paralelepípedo");
        wordList.add("pirâmide");
        wordList.add("retângulo");
        wordList.add("hexágono");
        wordList.add("pentágono");
        wordList.add("aborto");
        wordList.add("morte");
        wordList.add("desespero");
        wordList.add("dignidade");
        wordList.add("inteligência");
        wordList.add("rápido");
        wordList.add("lento");
        wordList.add("pássaro");
        wordList.add("oxigénio");
        wordList.add("carbono");
        wordList.add("hidrogénio");
        wordList.add("génio");
        wordList.add("telemóvel");
        wordList.add("telefone");
        wordList.add("impressora");
        wordList.add("digitalizadora");
        wordList.add("caderno");
        wordList.add("estojo");
        wordList.add("tesoura");
        wordList.add("disco");
        wordList.add("ficha");
        wordList.add("tomada");
        wordList.add("coluna");
        wordList.add("rádio");
        wordList.add("guitarra");
        wordList.add("baixo");
        wordList.add("piano");
        wordList.add("flauta");
        wordList.add("globo");
        wordList.add("continente");
        wordList.add("oceano");
        wordList.add("planeta");
        wordList.add("galáxia");
        wordList.add("universo");
        wordList.add("caveira");
        wordList.add("caverna");
        wordList.add("pedra");
        wordList.add("relva");
        wordList.add("minério");
        wordList.add("tocha");
        wordList.add("picareta");
        wordList.add("armadura");
        wordList.add("defesa");
        wordList.add("defender");
        wordList.add("óculos");
        wordList.add("caixa");
        wordList.add("carregador");
        wordList.add("gaveta");
        wordList.add("papel");
        wordList.add("tecla");
        wordList.add("televisão");
        wordList.add("monitor");
        wordList.add("placa");
        wordList.add("processador");
        wordList.add("aquecedor");
        wordList.add("secador");
        wordList.add("máquina");
        wordList.add("internet");
        wordList.add("aparelho");
        wordList.add("estátua");
        wordList.add("espelho");
        wordList.add("escova");
        wordList.add("pasta");
        wordList.add("dinheiro");
    }
}
