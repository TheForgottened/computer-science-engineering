package com.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {
	private static final String FILE_NAME = "fogo.txt";
	
	private static ArrayList<Divisao> divisaoList = new ArrayList<>();
	private static ArrayList<Aspersor> aspersorList = new ArrayList<>();
	private static ArrayList<Fogo> fogoList = new ArrayList<>();
	
    public static final void main(String[] args) {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-rules");
        	
        	if (!loadDataFromFile()) {
        		System.out.println("Ficheiro " + FILE_NAME + " não existe!");
        		return;
        	}

        	/*
        	Divisao d1 = new Divisao("cozinha");
        	Divisao d2 = new Divisao("escritório");
        	Divisao d3 = new Divisao("sala");
        	kSession.insert(d1);
        	kSession.insert(d2);
        	kSession.insert(d3);
        	
        	Aspersor asp1 = new Aspersor(d1);
        	Aspersor asp2 = new Aspersor(d2);
        	Aspersor asp3 = new Aspersor(d3); // A sala não tem aspersor
        	kSession.insert(asp1);
        	kSession.insert(asp2);
        	kSession.insert(asp3);
        	
        	Fogo f1 = new Fogo(d1);
        	Fogo f2 = new Fogo(d2);
        	Fogo f3 = new Fogo(d3);
        	kSession.insert(f1);
        	kSession.insert(f2);
        	kSession.insert(f3);
        	*/
        	
        	for (Divisao d : divisaoList) kSession.insert(d);
        	for (Aspersor a : aspersorList) kSession.insert(a);
        	for (Fogo f : fogoList) kSession.insert(f);
        	
        	kSession.fireAllRules();
        	
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    private static boolean loadDataFromFile() {
    	File file = new File(FILE_NAME);
    	
    	if (!file.exists()) return false;
    	
    	try {
			FileInputStream fileInputStream = new FileInputStream(file);
			Scanner scanner = new Scanner(fileInputStream);
			
			while (scanner.hasNextLine()) {
				String linha = scanner.nextLine();
				String[] temp = linha.split(";");
				
				divisaoList.add(new Divisao(temp[0]));
				
				if (temp[1].equals("sim")) aspersorList.add(new Aspersor(divisaoList.get(divisaoList.size() - 1)));
				
				if (temp[2].equals("sim")) fogoList.add(new Fogo(divisaoList.get(divisaoList.size() - 1)));
			}
			
			scanner.close();
			fileInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return true;
    }
}
