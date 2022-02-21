package pt.isec.forgotten.f02ex02;

public class Main {
    public static void main(String[] args) {
        Document doc  = new Document("Document's Title");
        System.out.println(doc.addAuthor("John"));
        doc.addAuthor("Johnny");
        doc.addAuthor("Joseph");
        System.out.println(doc.addAuthor("John"));
        System.out.println("Tam: " + doc.getMaxTam());
        doc.addAuthor("John2");
        doc.addAuthor("Johnny2");
        System.out.println("Tam: " + doc.getMaxTam());
        doc.addAuthor("Joseph2");
        System.out.println("Tam: " + doc.getMaxTam());

        System.out.println("");
        System.out.println("List of Authors:");
        for (int i = 0; i < doc.getMaxTam(); i++) {
            System.out.println(doc.getAuthors()[i]);
        }

        doc.addText("Alvaro Nuno,     Santos,,..\n");
    }
}
