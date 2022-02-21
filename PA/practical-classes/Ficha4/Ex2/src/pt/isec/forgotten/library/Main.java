package pt.isec.forgotten.library;

import pt.isec.forgotten.library.model.Library;
import pt.isec.forgotten.library.model.LibrarySet;
import pt.isec.forgotten.library.ui.LibraryUI;

import java.io.*;
import java.util.Scanner;

public class Main {
    static boolean test() {
        int i = 0;

        try {
            File f = new File("bruh.dat");

            OutputStream os;
            InputStream is;
            FileInputStream fis = new FileInputStream(f);
            FileOutputStream fos = new FileOutputStream(f);
            DataInputStream dis = new DataInputStream(fis);
            DataOutputStream dos = new DataOutputStream(fos);
            ObjectInputStream ois = new ObjectInputStream(dis);
            ObjectOutputStream oos = new ObjectOutputStream(dos);

            Writer wr;
            Reader re;
            FileWriter fw = new FileWriter(f);
            FileReader fr = new FileReader(f);
            BufferedWriter bw = new BufferedWriter(fw);
            BufferedReader br = new BufferedReader(fr);
            PrintWriter pw = new PrintWriter(bw);

            Scanner sc = new Scanner(br);

            pw.println("bruh bruh");

            String ler = sc.nextLine();

            sc.close();
            bw.close();

            i = 10;
        } catch (FileNotFoundException e) {
            System.err.println("File does not exist");
            i = 5;
        } catch (IOException e) {
            System.err.println("Other error");
        } catch (Exception e) {
            System.err.println("Another one - DJ Khaled");
        } finally {
            System.err.println("I always run :)");
        }
        i += 5;

        return true;
    }

    public static void main(String[] args) {
        //Library library = new LibraryList("DEIS-ISEC-List");
        Library library = new LibrarySet("DEIS-ISEC-Set");
        //Library library = new LibraryMap("DEIS-ISEC-Map");
        LibraryUI libUI = new LibraryUI(library);
        libUI.start();
    }
}
