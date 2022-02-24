package pt.isec.forgotten;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class Main {
    private static long stopTime;
    private static long startTime;
    private static FileWriter fileWriter;

    static void ex1b(long n){

        try {
            fileWriter.write("n = " + n + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        long soma=0;
        startTimer();
        for(long i=0;i<n;i++)
            soma++;
        System.out.println("Soma="+soma);
        stopTimer();
        showTime();
        try {
            fileWriter.write(getTimeAsString("ex1b") + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void ex1a(long n){

        try {
            fileWriter.write("n = " + n + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        long soma=0;
        startTimer();
        for(long i=0;i<n;i++)
            for(long j=0;j<n;j++)
                soma++;
        System.out.println("Soma="+soma);
        stopTimer();
        showTime();
        try {
            fileWriter.write(getTimeAsString("ex1a") + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void showTime() {
        long interval=stopTime-startTime;
        long secs=interval/1000000000L;
        long decs=interval-secs*1000000000L;
        decs/=100000000L;
        System.out.println("secs="+secs+"."+decs);
    }
    private static String getTimeAsString(String exercise) {
        long interval=stopTime-startTime;
        long secs=interval/1000000000L;
        long decs=interval-secs*1000000000L;
        decs/=100000000L;

        return exercise + "\t" + "secs="+secs+"."+decs;
    }
    private static void stopTimer() {
        stopTime=System.nanoTime();
    }
    private static void startTimer() {
        startTime=System.nanoTime();
    }
    public static void main(String[] args) {
        try {
            fileWriter = new FileWriter("results.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int n = 80000;

        ex1a( n );
        ex1a( 4*n );
        ex1b( n );
        ex1b( 4*n );

        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	/*	ex1b(2000000000L);
		ex1b(8000000000L);*/
    }
}
