package pt.isec.forgotten.dictionary;

public class Main {
    public static void main(String[] args) {
        Dictionary d = new Dictionary();
        d.add("ENGLISH", "LIVRO", "Book");
        d.add("FRENCH", "LIVRO", "livre");
        d.add("PORTUGUESE", "LIVRO", "livro");
        d.add("ENGLISH", "ANO", "year");
        d.add("FRENCH", "ANO", "an");
        d.add("PORTUGUESE", "ANO", "ano");
        d.setLanguage("ARABIAN");
        System.out.println(d.get("ANO")); // year
        d.setLanguage("portuguese");
        System.out.println(d.get("ANO")); // ano
        d.setLanguage("fRencH");
        System.out.println(d.get("LIVRO")); // livre
    }
}
