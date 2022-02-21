package pt.isec.forgotten.f02ex01;

import java.util.Locale;
import java.util.Random;

public class HangmanModel {
    private static final int MAX_TRIES = 7;

    private String word; // palavra a descobrir
    private StringBuffer situation; // estado da descoberta da palavra
    private StringBuilder used; // letras usadas até ao momento
    private int tries, successes;

    public HangmanModel() {
        tries = 0;
        successes = 0;
        used = new StringBuilder();
        word = HangmanDict.getWord(new Random().nextInt(HangmanDict.getSize()));

        assert word != null;
        situation = new StringBuffer("*".repeat(word.length()));
    }

    public String getSituation() { return situation.toString(); }
    public String getUsed() { return used.toString(); }
    public String getWord() { return word; }
    public int getTries() { return tries; }
    public int getSuccesses() { return successes; }

    public boolean finished() { return guessed() || tries == MAX_TRIES; }

    public boolean guessed() { return word.equalsIgnoreCase(situation.toString()); }

    public void setOption(String str) {
        if (finished()) {
            return;
        }

        if (str.isBlank()) {
            return;
        }

        tries++;
        str = str.toUpperCase();

        if (str.length() > 1) {
            if (str.equals(word)) {
                situation = new StringBuffer(word);
            }

            return;
        }

        char chStr = str.charAt(0);
        String init = situation.toString();
        used.append(chStr);

        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == chStr) {
                situation.setCharAt(i, chStr);
            }
        }

        if (!init.equalsIgnoreCase(situation.toString())) {
            successes++;
        }
    }
}
