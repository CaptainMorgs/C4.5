
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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

	private static List<Owl> owlList;

	private static List<Owl> trainingOwlList;

	private static List<Owl> testingOwlList;

	private static List<String> attributes = new ArrayList<String>();

	public static boolean outputToFile = false;

	// Labels to classify as
	private static String label1 = "BarnOwl";
	private static String label2 = "LongEaredOwl";
	private static String label3 = "SnowyOwl";

	private static Double bestSplitValue;

	public static int numCorrectlyClassified;
	public static int numIncorrectlyClassified;
	public static int total;
	public static double successRate;
	public static List<Result> results = new ArrayList<>();
	public static double classificationAccuracyAvg = 0.0;
	public static double trainingSize = 0.4;

	public static void main(String args[]) {

		attributes.add("bodyLength");
		attributes.add("wingLength");
		attributes.add("bodyWidth");
		attributes.add("wingWidth");

		// load the csv
		CSVLoader.loadCSV();

		// get the list of owls from the csv file
		List<Owl> owlList = CSVLoader.getOwlList();

		// shuffle the list
		Collections.shuffle(owlList);

		// TODO taken off internet
		int trainingSetSize = (int) Math.floor((owlList.size() * trainingSize));

		// split list into (40%)training and (60%)testing
		trainingOwlList = owlList.subList(0, trainingSetSize);

		// TODO check that all owls are unique
		testingOwlList = owlList.subList(trainingSetSize, owlList.size());

		System.out.println("No of owls in training set " + trainingOwlList.size());
		System.out.println("");

		Node<List<Owl>> decisionTree = generateDecisionTree(trainingOwlList, trainingOwlList);

		System.out.println("");
		decisionTree.print();

		testWithDecisionTree(testingOwlList, decisionTree);

	}

	public static void runC45() {

		CSVLoader.loadCSV();

		owlList = CSVLoader.getOwlList();

		// shuffle the list
		// Collections.shuffle(owlList);

		// TODO taken off internet
		// int trainingSetSize = (int) Math.floor((owlList.size() * 0.4));

		// split list into (40%)training and (60%)testing
		// trainingOwlList = owlList.subList(0, trainingSetSize);

		// TODO check that all owls are unique
		// testingOwlList = owlList.subList(trainingSetSize, owlList.size());

		// System.out.println("No of owls in training set " +
		// trainingOwlList.size());
		// System.out.println("");

		// Node<List<Owl>> decisionTree = generateDecisionTree(trainingOwlList,
		// trainingOwlList);

		testTenTimes();

		// System.out.println("");
		// decisionTree.print();

		// testWithDecisionTree(testingOwlList, decisionTree);
	}

	/**
	 * Run the C4.5 alg on test data 10 times with different random divisions 10
	 * times
	 * 
	 * @param decisionTree
	 */
	private static void testTenTimes() {

		results.clear();

		for (int i = 0; i < 10; i++) {
			randomlyDivideData();

			Node<List<Owl>> decisionTree = generateDecisionTree(trainingOwlList, trainingOwlList);

			Result result = testWithDecisionTree(testingOwlList, decisionTree);

			results.add(result);
		}

		outputPredictedAndActualValuesToFile(results, "results.txt");
	}

	private static void outputPredictedAndActualValuesToFile(List<Result> results, String fileName) {
		if (outputToFile) {
			try {
				PrintWriter out = new PrintWriter(fileName);
				for (Result result : results) {
					out.println(result.getFileSummary());
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Shuffles the data read from the csv file randomly and partitions it into
	 * training and testing lists
	 */
	private static void randomlyDivideData() {
		// shuffle the list randomly
		Collections.shuffle(owlList);

		// TODO taken off internet
		int trainingSetSize = (int) Math.floor((owlList.size() * 0.4));

		trainingOwlList = owlList.subList(0, trainingSetSize);

		testingOwlList = owlList.subList(trainingSetSize, owlList.size());

		// TODO make this better
		attributes.add("bodyLength");
		attributes.add("wingLength");
		attributes.add("bodyWidth");
		attributes.add("wingWidth");

	}

	// Tests a list of owls, prints the results
	private static Result testWithDecisionTree(List<Owl> owls, Node<List<Owl>> decisionTree) {

		int correctlyClassified = 0;
		double accuracy = 0.0;
		String fileSummary = "";

		for (Owl owl : owls) {
			String predicted = classify(owl, decisionTree);
			if (predicted.equals(owl.getType()))
				correctlyClassified++;

			String line = "Actual = " + owl.getType() + ", predicted = " + predicted + "\n";
			fileSummary += line;
		}

		accuracy = (double) correctlyClassified / (double) owls.size();
		Result result = new Result(correctlyClassified, owls.size() - correctlyClassified, accuracy, owls.size(),
				fileSummary);
		return result;
		// results.add(result);

	}

	public static double getAverageClassificationAccuracy(List<Result> results) {
		Double total = 0.0;
		for (Result result : results) {
			total += result.getClassificationAccuracy();
		}
		classificationAccuracyAvg = total / (double) results.size();
		return classificationAccuracyAvg;
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
		String bestAttribute = BestAttributeCalculator.getAttributeToSplitOn(owls, attributes);

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

		double currentSplitValue = BestAttributeCalculator.bestSplitValue;

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

}
