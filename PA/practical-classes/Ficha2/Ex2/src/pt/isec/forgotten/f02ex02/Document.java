package pt.isec.forgotten.f02ex02;

import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Document {
    private static final int INC_AUTHORS = 5;

    private String title;
    private String [] authors;
    private StringBuffer text;
    private int nrAuthors;

    public Document(String title) {
        this.title = title;
        authors = new String[INC_AUTHORS];
        text = new StringBuffer();
        nrAuthors = 0;
    }

    public String getTitle() {
        return title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public int getMaxTam() {
        return this.authors.length;
    }

    public String getText() {
        if (text == null)
            return "";

        return text.toString();
    }

    public boolean addAuthor(String author) {
        for (int i = 0; i < this.nrAuthors; i++) {
            if (author.equalsIgnoreCase(this.authors[i])) {
                return false;
            }
        }

        if (this.nrAuthors >= this.authors.length) {
            this.authors = Arrays.copyOf(this.authors, this.authors.length + INC_AUTHORS);
        }

        this.authors[this.nrAuthors++] = author;
        return true;
    }

    public void addText(String phrase) {
        if (text == null) {
            text = new StringBuffer();
        }

        text.append(phrase);
    }

    public boolean removeAuthors(String author) {
        for (int i = 0; i < this.nrAuthors; i++) {
            if (author.equalsIgnoreCase(this.authors[i])) {
                for (int j = i; j < this.nrAuthors - 1; j++) {
                    this.authors[j] = authors[j + 1];
                }

                this.authors[--this.nrAuthors] = null;

                if (this.authors.length >= this.nrAuthors + 2 * INC_AUTHORS)
                    this.authors = Arrays.copyOf(this.authors, this.nrAuthors + INC_AUTHORS);

                return true;
            }
        }

        return false;
    }

    public int wordCount() {
        int i = 0;

        Scanner sc = new Scanner(this.text.toString());
        sc.useDelimiter("[\\s,.]+");

        while (sc.hasNext()) {
            i++;
            sc.next();
        }

        return i;
    }

    public int wordCount2() {
        StringTokenizer st = new StringTokenizer(this.text.toString(), "\t\n., ");
        return st.countTokens();
    }
}
