
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeSet;

import Owls.Owl;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Algorithm {

	private static int debug = 0;

	public static List<Owl> owlList;

	private static List<Owl> trainingOwlList;

	private static List<Owl> testingOwlList;

	private static List<String> attributes = new ArrayList<String>();

	// Labels to classify as
	private static String label1 = "BarnOwl";
	private static String label2 = "LongEaredOwl";
	private static String label3 = "SnowyOwl";

	private static Double splitValue;

	private static Double bestSplitValue;

	public static int numCorrectlyClassified;
	public static int numIncorrectlyClassified;
	public static int total;
	public static double successRate;

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

		System.out.println("No of owls in training set " + trainingOwlList.size());
		System.out.println("");

		Node<List<Owl>> decisionTree = generateDecisionTree(trainingOwlList, trainingOwlList);

		System.out.println("");
		decisionTree.print();

		test(testingOwlList, decisionTree);

	}

	public static void runC45() {

		attributes.add("bodyLength");
		attributes.add("wingLength");
		attributes.add("bodyWidth");
		attributes.add("wingWidth");

		CSVLoader2.loadCSV();

		owlList = CSVLoader2.getOwlList();

		// shuffle the list
		Collections.shuffle(owlList);

		// TODO taken off internet
		int trainingSetSize = (int) Math.floor((owlList.size() * 0.4));

		// split list into (40%)training and (60%)testing
		trainingOwlList = owlList.subList(0, trainingSetSize);

		// TODO check that all owls are unique
		testingOwlList = owlList.subList(trainingSetSize, owlList.size());

		System.out.println("No of owls in training set " + trainingOwlList.size());
		System.out.println("");

		Node<List<Owl>> decisionTree = generateDecisionTree(trainingOwlList, trainingOwlList);

		System.out.println("");
		decisionTree.print();

		test(testingOwlList, decisionTree);
	}

	// Classifies an owl
	private static String classify(Owl owl, Node node) {
		// if a node is a leaf then return the nodes attribute name
		if (node.isLeaf) {
			return node.attributeName;
		}
		// traverse the tree either left or right by comparing the the owls
		// value to the nodes split value
		double owlValue = owl.get(node.attributeName);

		if (owlValue <= node.splitValue)
			return classify(owl, (Node) node.children.get(0));

		else
			return classify(owl, (Node) node.children.get(1));

	}

	// Tests a list of owls, prints the results
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
		double hitRate = 0.0;
		hitRate = (correct * 1.0) / (owls.size() * 1.0);
		System.out.println("Hit Rate: " + (hitRate * 100) + "%");

		numCorrectlyClassified = correct;
		numIncorrectlyClassified = owls.size() - correct;
		successRate = hitRate;
		total = owls.size();
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

	private static Node<List<Owl>> generateDecisionTree(List<Owl> owls, List<Owl> parentOwls) {

		// if the list of owls is empty return the most frequent type of owl in
		// the nodes parent
		if (owls.isEmpty()) {
			System.err.println("owls is empty");
			return new Node(getMostFrequentType(parentOwls), true);
		}

		// if the list of owls all have the same type then return that type,
		// calculating inf gain would be pointless
		if (checkAllSameLabel(owls)) {
			System.err.println("all same label");
			return new Node(owls.get(0).getType(), true);
		}
		// calc attribute with the highest information gain
		String bestAttribute = getAttributeToSplitOn(owls, attributes);

		// if bestAttribute is null return most frequent type in owls
		if (bestAttribute == null) {
			System.err.println("best attribute is null");
			return new Node(getMostFrequentType(owls), true);
		}
		// TODO think of alternative to bestAttribute
		// make the root node with the bestAttribute to split on and make the
		// node not a leaf node
		Node root = new Node(bestAttribute, false);

		// Split owls based on best attribute
		// TODO not use subsets just use two lists

		LinkedHashMap<String, List<Owl>> subsets = new LinkedHashMap<String, List<Owl>>();

		double currentSplitValue = bestSplitValue;

		root.splitValue = currentSplitValue;

		List<Owl> owlsLessThan = new ArrayList<Owl>();
		List<Owl> owlsGreaterThan = new ArrayList<Owl>();

		// loop through owls and add them to the right list by comparing them to
		// the split value
		for (Owl owl : owls) {

			double value = owl.get(bestAttribute);

			if (value <= currentSplitValue)
				owlsLessThan.add(owl);
			else
				owlsGreaterThan.add(owl);
		}
		subsets.put("owlsLessThan", owlsLessThan);
		subsets.put("owlsGreaterThan", owlsGreaterThan);

		System.out.println("");
		System.out.println(
				"Subsets partitioned on " + bestAttribute + " with a split value of " + currentSplitValue + " ");
		System.out.println("");
		System.out.println("Less than list of size " + subsets.get("owlsLessThan").size());

		List<Owl> lessThanOwls = subsets.get("owlsLessThan");

		for (Owl owl : lessThanOwls)
			System.out.print(owl.get(bestAttribute) + " with type " + owl.getType() + " ");
		System.out.println("");
		System.out.println("");
		System.out.println("Greater than list of size " + subsets.get("owlsGreaterThan").size());

		List<Owl> greaterThanOwls = subsets.get("owlsGreaterThan");

		for (Owl owl : greaterThanOwls)
			System.out.print(owl.get(bestAttribute) + " with type " + owl.getType() + " ");

		// remove the attribute from the list so that is removed from
		// consideration
		attributes.remove(bestAttribute);
		System.out.println("");
		System.out.println("");
		System.out.println("New attributes list with bestAtrribute removed " + attributes.toString());

		// Iterate through possible values of bestAttribute
		for (String key : subsets.keySet()) {

			// Add child to subtree of root
			root.addChild(generateDecisionTree(subsets.get(key), owls));

			// child.splitValue = currentSplitValue;

		}
		return root;
	}

	/*
	 * Returns best attribute owls, remaining attributes Returns null if no
	 * attribute has positive information gain Returns null if no candidate
	 * splits remaining
	 */
	private static String getAttributeToSplitOn(List<Owl> owls, List<String> attributes) {
		double maxInfoGain = 0.0;
		String bestAtt = null;

		for (String attribute : attributes) {
			Double infoGain = getMaxInformationGain(owls, attribute);
			System.out.println("Gain of attribute " + attribute + " is " + infoGain);

			if (infoGain > maxInfoGain) {
				maxInfoGain = infoGain;
				bestAtt = attribute;
				// bestSplitValue gets split value of numeric attribute with
				// highest gain
				bestSplitValue = splitValue;
			}
		}
		if (maxInfoGain <= 0.0)
			return null;

		if (debug == 1) {
			for (String attribute : attributes) {
				System.out.println("Gain of attribute " + attribute + " is " + maxInfoGain);
			}
		}

		return bestAtt;
	}

	// Calculates max information gain over owls for the attribute
	private static double getMaxInformationGain(List<Owl> owls, String attribute) {
		if (owls.isEmpty())
			return 0.0;

		// Get list of possible split points
		// TODO use something other than a tree set
		TreeSet<Double> values = new TreeSet<Double>();
		for (Owl owl : owls)
			// TODO not hardcode to bodyLength
			values.add(owl.get(attribute));

		List<Double> possibleSplitValues = new ArrayList<Double>();
		// TODO change this
		Iterator<Double> num1 = values.iterator();
		Iterator<Double> num2 = values.iterator();
		if (num2.hasNext())
			num2.next();
		while (num2.hasNext())
			possibleSplitValues.add((num1.next() + num2.next()) / 2);

		if (debug == 1)
			System.out.println("Candidate Splits " + possibleSplitValues.toString());

		// Find maximum gain from among possible split points
		double maxInfoGain = 0.0;
		splitValue = 0.0;
		for (Double split : possibleSplitValues) {
			double infoGain = getEntropy(owls) - getEntropy(owls, attribute, split);
			if (debug == 1)
				System.out.println("gain " + infoGain);
			if (infoGain > maxInfoGain) {
				maxInfoGain = infoGain;
				splitValue = split;
			}
		}
		return maxInfoGain;
	}
	// }

	private static String getMostFrequentType(List<Owl> owls) {

		if (owls.isEmpty()) {
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
		// get the max of the 3 label counts
		int max = Math.max(Math.max(label1count, label2count), label3count);

		if (max == label1count) {
			return label1;
		}

		else if (max == label2count) {
			return label2;
		}

		else if (max == label3count) {
			return label3;
		}
		return null;
	}

	private static boolean checkAllSameLabel(List<Owl> owls) {

		if (owls.isEmpty()) {
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
		// get the max of the 3 label counts
		int max = Math.max(Math.max(label1count, label2count), label2count);

		if (max == owls.size()) {
			return true;
		}

		else
			return false;
	}

	private static double getEntropy(List<Owl> owls, String attribute, double splitValue) {

		if (owls.isEmpty())
			return 0.0;

		// Divide the list based on the split point
		List<Owl> owlsLessThan = new ArrayList<Owl>();
		List<Owl> owlsGreaterThan = new ArrayList<Owl>();

		for (Owl owl : owls) {
			double value = owl.get(attribute);
			if (value <= splitValue)
				owlsLessThan.add(owl);
			else
				owlsGreaterThan.add(owl);
		}

		if (debug == 1) {
			System.out.println("No of owls less than split value " + owlsLessThan.size());
			System.out.println("No of owls greater than split value " + owlsGreaterThan.size());
		}

		double ratioGreater = (double) owlsLessThan.size() / (double) owls.size();
		double ratioLess = (double) owlsGreaterThan.size() / (double) owls.size();

		if (debug == 1) {
			System.out.println("No of owls less than split value as a fraction " + ratioLess);
			System.out.println("No of owls greater than split value as a fraction " + ratioGreater);
			System.out.println("");
		}

		return ratioGreater * getEntropy(owlsLessThan) + ratioLess * getEntropy(owlsGreaterThan);

	}

	private static double getEntropy(List<Owl> owls) {

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

		if (debug == 1) {
			System.out.println(label1 + " no. " + label1count);
			System.out.println(label2 + " no. " + label2count);
			System.out.println(label3 + " no. " + label3count);
			System.out.println("");
		}

		// Calculate entropy
		double prLabel1 = (double) label1count / (double) total;
		double prLabel2 = (double) label2count / (double) total;
		double prLabel3 = (double) label3count / (double) total;

		return -(prLabel1 * getLogBase2(prLabel1) + prLabel2 * getLogBase2(prLabel2)
				+ prLabel3 * getLogBase2(prLabel3));
	}

	private static double getLogBase2(double x) {
		if (x == 0.0)
			return 0.0;
		return (Math.log(x) / Math.log(2.0));
	}
}
