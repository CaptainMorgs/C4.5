import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Owls.BarnOwl;
import Owls.LongEaredOwl;
import Owls.Owl;
import Owls.SnowyOwl;

public class CSVLoader {

	public static ArrayList<BarnOwl> barnOwlList = new ArrayList<>();
	public static ArrayList<SnowyOwl> snowyOwlList = new ArrayList<>();
	public static ArrayList<LongEaredOwl> longEaredOwlList = new ArrayList<>();

	
	 public static void main(String[] args) {
		 
	 
	String csvFileLocation = "C:/Users/John/Downloads/owls15.csv";
	
	BufferedReader br = null;
	String line = "";
	String splitter = ",";
	
	try {

        br = new BufferedReader(new FileReader(csvFileLocation));
        while ((line = br.readLine()) != null) {

            // use comma as separator
            String[] csvLine = line.split(splitter);

      //      System.out.println("Owl [body-length= " + csvLine[0] + " , wing-length=" + csvLine[1] + " , body-width=" + csvLine[2]+ " , wing-width=" + csvLine[3]+ " , type=" + csvLine[4] + "]");
       
            switch (csvLine[4]){
            case "BarnOwl": 
            	BarnOwl barnOwl = new BarnOwl(Double.parseDouble(csvLine[0]), Double.parseDouble(csvLine[1]), Double.parseDouble(csvLine[2]), Double.parseDouble(csvLine[3]));
            	barnOwlList.add(barnOwl);
            case "SnowyOwl": 
            	SnowyOwl snowyOwl = new SnowyOwl(Double.parseDouble(csvLine[0]), Double.parseDouble(csvLine[1]), Double.parseDouble(csvLine[2]), Double.parseDouble(csvLine[3]));
            	snowyOwlList.add(snowyOwl);
            case "LongEaredOwl": 
            	LongEaredOwl longEaredOwl = new LongEaredOwl(Double.parseDouble(csvLine[0]), Double.parseDouble(csvLine[1]), Double.parseDouble(csvLine[2]), Double.parseDouble(csvLine[3]));
            	longEaredOwlList.add(longEaredOwl);
            }
        }

    } catch (FileNotFoundException exception) {
    	exception.printStackTrace();
        
    } catch (IOException e) {
        e.printStackTrace();
        
    } finally {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	System.out.println("Number of barn owls added: " + barnOwlList.size());
    for(Owl owl : barnOwlList) {
    	System.out.println(owl.toString());
    }
    System.out.println();
    
    System.out.println("Number of snowy owls added: " + snowyOwlList.size());
    for(Owl owl : snowyOwlList) {
    	System.out.println(owl.toString());
    }
    System.out.println();

    System.out.println("Number of long eared owls added: " + longEaredOwlList.size());
    for(Owl owl : longEaredOwlList) {
    	System.out.println(owl.toString());
    }
    
	 }
}