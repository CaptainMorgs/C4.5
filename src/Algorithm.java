

import java.util.Collections;
import java.util.List;

import Owls.Owl;

public class Algorithm {
	
	//https://github.com/lahendrix/ai-algorithms/blob/master/other-ai-algorithms/ID3.java
	
	public static List<Owl> owlList;
	
	private static  List<Owl> trainingOwlList;

	private static List<Owl> testingOwlList;

	
	public static void main(String args[]) {
		
		//load the csv
		CSVLoader2.loadCSV();
		
		//get the list of owls from the csv file
		owlList = CSVLoader2.getOwlList();
		
		//shuffle the list
		Collections.shuffle(owlList);
		
		//TODO taken off internet
		int trainingSetSize = (int)Math.floor((owlList.size() * 0.4));
		
		//split list into (40%)training and (60%)testing
		trainingOwlList = owlList.subList(0, trainingSetSize);
		
		//TODO check that all owls are unique 
		testingOwlList = owlList.subList(trainingSetSize, owlList.size());
		
		//print stuff
		printStuff();
		
		
	}

	private static void printStuff() {
		
		System.out.println("Training List Size: " + trainingOwlList.size());
	    for(Owl owl : trainingOwlList) {
	    	System.out.println(owl.toString());
	    }
	    System.out.println();

	    System.out.println("Testing List Size: " + testingOwlList.size());
	    for(Owl owl : testingOwlList) {
	    	System.out.println(owl.toString());
	    }
	}
}
