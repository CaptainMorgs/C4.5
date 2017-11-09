
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeSet;

import Owls.Owl;

public class Algorithm {

	// https://github.com/lahendrix/ai-algorithms/blob/master/other-ai-algorithms/ID3.java

	public static List<Owl> owlList;

	private static List<Owl> trainingOwlList;

	private static List<Owl> testingOwlList;

	//private static List<String> attributes = Arrays.asList("bodyLength", "wingLength", "bodyWidth", "wingWidth");

	private static List<String> attributes = new ArrayList<String>();
	
	// Labels to classify as
	private static String label1 = "BarnOwl";
	private static String label2 = "LongEaredOwl";
	private static String label3 = "SnowyOwl";
	
	private static Double splitValue;
	
	private static Double bestSplitValue;

	public static void main(String args[]) {
		
		attributes.add("bodyLength");
		attributes.add("wingLength");
		attributes.add("bodyWidth");
		attributes.add("wingWidth");


		// load the csv
		CSVLoader2.loadCSV();

		// get the list of owls from the csv file
		owlList = CSVLoader2.getOwlList();

		// shuffle the list
		Collections.shuffle(owlList);

		// TODO taken off internet
		int trainingSetSize = (int) Math.floor((owlList.size() * 0.4));

		// split list into (40%)training and (60%)testing
		trainingOwlList = owlList.subList(0, trainingSetSize);

		// TODO check that all owls are unique
		testingOwlList = owlList.subList(trainingSetSize, owlList.size());

		// print stuff
		// printStuff();

		System.out.println("No of owls in training set " + trainingOwlList.size());
		System.out.println("");

//		Double entropy = entropy(trainingOwlList, "", 2.5);
		
		System.out.println("Max info gain on bodyLength " + gain(trainingOwlList, "bodyLength"));
		
		System.out.println("Best Attribute is " + bestAttribute(trainingOwlList, attributes));

//		System.out.println("Entropy of bodyLength " + entropy);
		
		//0.9182958340544894
		
		//0.9182958340544894
		
		Node<List<Owl>> decisionTree = generateDecisionTree(trainingOwlList, trainingOwlList);
		
		System.out.println("");
		System.out.println(decisionTree.toString());
		decisionTree.print();
		
		test(testingOwlList, decisionTree);

	}
	
	// Classifies an instance (requires that a tree is already built)
		private static String classify(Owl owl, Node node) {
			if (node.terminal)
				return node.attributeName;
			
				double instanceValue = owl.get(node.attributeName);
				if (instanceValue <= node.splitValue)
					return classify(owl, (Node)node.children.get(0));
				else return classify(owl, (Node)node.children.get(1));
			
		}
		
		// Tests a set of instances, prints results
		public static void test(List<Owl> owls, Node<List<Owl>> decisionTree) {
			System.out.println("\n\nListed as: <predicted class> <actual class>");
			int correct = 0;
			for (Owl owl : owls) {
				String predicted = classify(owl, decisionTree);
				System.out.println(predicted + " " + owl.getType());
				if (predicted.equals(owl.getType())) 
					correct++;	
			}
			System.out.println("\nCorrect: " + correct);
			System.out.println("Total: " + owls.size());
			double hitRate = (correct * 1.0) / (owls.size() * 1.0);
			System.out.println("Hit Rate: " + (hitRate * 100) + "%");
		}

	private static void printStuff() {

		System.out.println("Training List Size: " + trainingOwlList.size());
		for (Owl owl : trainingOwlList) {
			System.out.println(owl.toString());
		}
		System.out.println();

		System.out.println("Testing List Size: " + testingOwlList.size());
		for (Owl owl : testingOwlList) {
			System.out.println(owl.toString());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Node<List<Owl>> generateDecisionTree(List<Owl> owls, List<Owl> parentOwls) {	
		
		if (owls.isEmpty()){
			System.err.println("owls is empty");
			return new Node(getMostFrequentType(parentOwls), true);
		}
		
		if(checkAllSameLabel(owls)){
			System.err.println("all same label");
			return new Node(owls.get(0).getType(), true);
		}
				String bestAttribute = bestAttribute(owls, attributes);
				if (bestAttribute == null) {
					//return new Node(pluralityValue(instances), true);
					System.err.println("best attribute is null");
			//		return new Node("best attribute is null");
			//		return null;
					return new Node(getMostFrequentType(owls), true);
				}
				//Make the root node with the whole owl list as its data and give it the label of bestAttribute
				Node root = new Node(bestAttribute, false);
				
				
				// Partition instances based on best attribute
				//TODO not use subsets just use two lists
				//TODO remove leq and gtr
				LinkedHashMap<String, List<Owl>> subsets = new LinkedHashMap<String, List<Owl>>();
				double currentSplitValue = bestSplitValue;
				
				//	root.numeric = true;
					root.splitValue = currentSplitValue;
					List<Owl> leq = new ArrayList<Owl>();
					List<Owl> gtr = new ArrayList<Owl>();
					for (Owl owl : owls) {
						
						double value = owl.get(bestAttribute);
						if (value <= currentSplitValue)
							leq.add(owl);
						else
							gtr.add(owl);
					}
					subsets.put("leq", leq);
					subsets.put("gtr", gtr);
					
					System.out.println("");
					System.out.println("Subsets partitioned on " + bestAttribute + " with a split value of " + currentSplitValue + " ");
					System.out.println("");
					System.out.println("Less than list of size " + subsets.get("leq").size());
					
					List<Owl> lessThanOwls = subsets.get("leq");
					
					for(Owl owl: lessThanOwls)
						System.out.print(owl.get(bestAttribute) + " with type " + owl.getType() + " ");
					System.out.println("");
					System.out.println("");
					System.out.println("Greater than list of size " + subsets.get("gtr").size());
					
					List<Owl> greaterThanOwls = subsets.get("gtr");
					
					for(Owl owl: greaterThanOwls)
						System.out.print(owl.get(bestAttribute) + " with type " + owl.getType() + " ");
					
					
					attributes.remove(bestAttribute);
					System.out.println("");
					System.out.println("");
					System.out.println("New attributes list with bestAtrribute removed " + attributes.toString());

					
					// Iterate through possible values of bestAttribute
					for(String key: subsets.keySet()) {
						// Copy attributes list and remove bestAttribute from copy
					//	List<String> remainingAttributes = new ArrayList<String>();
						
					
						//	remainingAttributes.addAll(attributes);
					//	if (!bestAttribute.numeric)
					//		remainingAttributes.remove(bestAttribute);
						// Add child to subtree of root
						//Node child = generateDecisionTree(subsets.get(key));
				//		if(!(subsets.get(key).isEmpty()))
							root.addChild(generateDecisionTree(subsets.get(key), owls));
						//if (bestAttribute.numeric) {
							//child.numeric = true;
							//child.splitValue = currentSplitValue;
						//	if (key.equals("leq"))
						//		child.leq = true;
						//	else
						//		child.leq = false;
						//} else
						//	child.value = key;
						//root.addChild(child);
					}
					return root;
				}
				//	return null;
				
	

	/*
	 * Returns best attribute for given instances, remaining attributes
	 * Returns null if no attribute has positive information gain
	 * Returns null if no candidate splits remaining
	 */
	private static String bestAttribute(List<Owl> instances, List<String> attributes) {
		double maxGain = Double.MIN_VALUE;
		String best = null;
		
		for (String attribute : attributes) {
			Double gain = gain(instances, attribute);
				System.err.println("Gain of attribute " + attribute + " is " + gain);

			if (gain > maxGain) {
				maxGain = gain;
				best = attribute;
				// bestSplitValue gets split value of numeric attribute with highest gain
				bestSplitValue = splitValue;
			}
		}
		if (maxGain <= 0.0)
			return null;
		/*for (String attribute : attributes) {
			System.out.println("Gain of attribute " + attribute + " is " + gain);
		}*/
		return best;
	}
	
	
	
	// Calculates (maximum) information gain over instance for the attribute
		private static double gain(List<Owl> owls, String attribute) {
			if (owls.isEmpty())
				return 0.0;
		//	if (!attribute.numeric)
		//		return entropy(instances) - conditionalEntropy(instances, attribute);
		//	else {
				// Get list of possible split points
			//TODO use something other than a tree set
				TreeSet<Double> values = new TreeSet<Double>();
				for (Owl owl : owls)
					//TODO not hardcode to bodyLength
					values.add(owl.get(attribute));
			//	System.out.println("Size of values " + values.size());
				
				List<Double> candidateSplits = new ArrayList<Double>();
				//TODO change this 
				Iterator<Double> num1 = values.iterator();
				Iterator<Double> num2 = values.iterator();
				if (num2.hasNext())
					num2.next();
				while (num2.hasNext())
					candidateSplits.add((num1.next() + num2.next()) / 2);
				
				/*System.out.println("Candidate Splits " + candidateSplits.toString());*/

				
				// Find maximum gain from among possible split points
				double maxGain = Double.MIN_VALUE;
				splitValue = Double.MIN_VALUE;
				for (Double split : candidateSplits) {
					double gain = entropy(owls) - entropy(owls, attribute, split);
		//			System.out.println("gain " + gain);
					if (gain > maxGain) {
						maxGain = gain;
						splitValue = split;
					}
			}
				return maxGain;	
			}
		//}
		
	private static String getMostFrequentType(List<Owl> owls) {
		
		if (owls.isEmpty()){
			System.err.println("empty list");
			return null;
		}
		// Obtain number of instances in each class
		int label1count = 0;
		int label2count = 0;
		int label3count = 0;

		for (Owl owl : owls) {
			if (owl.getType().equals(label1))
				label1count++;

			else if (owl.getType().equals(label2))
				label2count++;

			else if (owl.getType().equals(label3))
				label3count++;

			else
				System.err.println("Unable to classify, unknown type " + owl.getType());
		}
		//get the max of the 3 label counts
		int max = Math.max(Math.max(label1count, label2count), label3count);
		
		if(max == label1count){
			return label1;
		}
		
		else if(max == label2count){
			return label2;
		}
		
		else if(max == label3count){
			return label3;
		}
		return null;
	}
	
private static boolean checkAllSameLabel(List<Owl> owls) {
		
		if (owls.isEmpty()){
			System.err.println("empty list");
			return false;
		}
		// Obtain number of instances in each class
		int label1count = 0;
		int label2count = 0;
		int label3count = 0;

		for (Owl owl : owls) {
			if (owl.getType().equals(label1))
				label1count++;

			else if (owl.getType().equals(label2))
				label2count++;

			else if (owl.getType().equals(label3))
				label3count++;

			else
				System.err.println("Unable to classify, unknown type " + owl.getType());
		}
		//get the max of the 3 label counts
		int max = Math.max(Math.max(label1count, label2count), label2count);
		
		if(max == owls.size()){
			return true;
		}
		
		else return false;
	}

	
	
	private static double entropy(List<Owl> owls, String attribute, double splitValue) {

		if (owls.isEmpty())
			return 0.0;

		// Divide the list based on the split point
		List<Owl> leq = new ArrayList<Owl>();
		List<Owl> gtr = new ArrayList<Owl>();

		for (Owl owl : owls) {
			double value = owl.get(attribute);
			if (value <= splitValue)
				leq.add(owl);
			else
				gtr.add(owl);
		}

		/*System.out.println("No of owls less than split value " + leq.size());
		System.out.println("No of owls greater than split value " + gtr.size());*/

		double prLeq = (leq.size() * 1.0) / (owls.size() * 1.0);
		double prGtr = (gtr.size() * 1.0) / (owls.size() * 1.0);

		/*System.out.println("No of owls less than split value as a fraction " + prLeq);
		System.out.println("No of owls greater than split value as a fraction " + prGtr);
		System.out.println("");*/
		
		return prLeq * entropy(leq) + prGtr * entropy(gtr);

	}

	private static double entropy(List<Owl> owls) {

		if (owls.isEmpty())
			return 0.0;
		// Obtain number of instances in each class
		int total = owls.size();
		int label1count = 0;
		int label2count = 0;
		int label3count = 0;

		for (Owl owl : owls) {
			if (owl.getType().equals(label1))
				label1count++;

			else if (owl.getType().equals(label2))
				label2count++;

			else if (owl.getType().equals(label3))
				label3count++;

			else
				System.err.println("Unable to classify, unknown type " + owl.getType());
		}

//		System.out.println(label1 + " no. " + label1count);
//		System.out.println(label2 + " no. " + label2count);
//		System.out.println(label3 + " no. " + label3count);
//		System.out.println("");

		// Calculate entropy
		double prLabel1 = (label1count * 1.0) / (total * 1.0);
		double prLabel2 = (label2count * 1.0) / (total * 1.0);
		double prLabel3 = (label3count * 1.0) / (total * 1.0);

		return -(prLabel1 * logBase2(prLabel1) + prLabel2 * logBase2(prLabel2) + prLabel3 * logBase2(prLabel3));
	}

	private static double logBase2(double x) {
		if (x == 0.0)
			return 0.0;
		return (Math.log(x) / Math.log(2.0));
	}
}
