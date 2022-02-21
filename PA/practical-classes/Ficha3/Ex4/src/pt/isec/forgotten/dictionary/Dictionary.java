package pt.isec.forgotten.dictionary;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Dictionary {
    private String currentLanguage;
    private Map<String, Map<String, String>> dict;

    public Dictionary() {
        this.dict = new HashMap<>();
        this.currentLanguage = "ENGLISH";
    }

    public void add(String language, String concept, String word) {
        if (!this.dict.containsKey(language.toUpperCase())) {
            this.dict.put(language.toUpperCase(), new HashMap<>());
        }

        this.dict.get(language.toUpperCase()).put(concept.toUpperCase(), word.toUpperCase());
    }

    public boolean setLanguage(String language) {
        if (!this.dict.containsKey(language.toUpperCase())) {
            return false;
        }

        this.currentLanguage = language.toUpperCase();
        return true;
    }

    public String get(String concept) {
        if (!this.dict.get(this.currentLanguage.toUpperCase()).containsKey(concept.toUpperCase())) {
            return "Concept not available.";
        }

        return this.dict.get(this.currentLanguage.toUpperCase()).get(concept.toUpperCase());
    }
}
