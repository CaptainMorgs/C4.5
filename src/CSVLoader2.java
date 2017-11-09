import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Owls.BarnOwl;
import Owls.LongEaredOwl;
import Owls.Owl;
import Owls.SnowyOwl;

public class CSVLoader2 {

	private static ArrayList<Owl> owlList = new ArrayList<>();

	
	 public static ArrayList<Owl> getOwlList() {
		return owlList;
	}


	public static void setOwlList(ArrayList<Owl> owlList) {
		CSVLoader2.owlList = owlList;
	}


	public static void loadCSV() {
		 
	 
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
       
        
            	Owl owl = new Owl(Double.parseDouble(csvLine[0]), Double.parseDouble(csvLine[1]), Double.parseDouble(csvLine[2]), Double.parseDouble(csvLine[3]), csvLine[4]);
            	owlList.add(owl);
           
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
	
	System.out.println("Number of barn owls added: " + owlList.size());
   /* for(Owl owl : owlList) {
    	System.out.println(owl.toString());
    }*/
    System.out.println();
   
    }
    
	 }