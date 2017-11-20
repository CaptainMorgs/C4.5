
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import Owls.Owl;
import Owls.Sample;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Algorithm {

	public static boolean debug = true;

	private List<Sample> sampleList, trainingSampleList, testingSampleList;

	private List<String> attributes = new ArrayList<String>();

	public static boolean outputToFile = false;

	public static int numCorrectlyClassified, numIncorrectlyClassified, total;

	public static double successRate;
	public static List<Result> results = new ArrayList<>();

	public static double classificationAccuracyAvg = 0.0;

	public static double trainingSize = 0.4;

	public void runC45() {

		CSVLoader csvLoader = new CSVLoader();
		csvLoader.loadCSV();

		sampleList = csvLoader.getSampleList();

		attributes = csvLoader.getFeatureNames();
		if (debug)
			System.out.println("Starting attributes " + attributes.toString());

		testTenTimes(sampleList, attributes);
	}

	/**
	 * Run the C4.5 alg on test data 10 times with different random divisions 10
	 * times
	 * 
	 * @param sampleList
	 * 
	 * @param decisionTree
	 */
	private void testTenTimes(List<Sample> sampleList, List<String> attributes) {

		results.clear();

		for (int i = 0; i < 10; i++) {

			// Could use a better method for randomly selecting data but asked
			// lecturer
			// and he said it was out of scope for the assignment
			Collections.shuffle(sampleList);

			// TODO taken off internet
			int trainingSetSize = (int) Math.floor((sampleList.size() * 0.4));

			trainingSampleList = sampleList.subList(0, trainingSetSize);

			testingSampleList = sampleList.subList(trainingSetSize, sampleList.size());

			if (debug)
				System.out.println("Calling generateDecisionTree with fields " + attributes.toString());

			Node<List<Sample>> decisionTree = generateDecisionTree(trainingSampleList, trainingSampleList, attributes);

			Result result = testWithDecisionTree(testingSampleList, decisionTree);

			results.add(result);
		}

		outputPredictedAndActualValuesToFile(results, "results.txt");
	}

	/**
	 * Writes predicted and actual values to a file
	 * 
	 * @param results
	 * @param fileName
	 */
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
	 * Tests a decision tree with a list of samples
	 * 
	 * @param samples
	 * @param decisionTree
	 * @return
	 */
	private static Result testWithDecisionTree(List<Sample> samples, Node<List<Sample>> decisionTree) {

		int correctlyClassified = 0;
		double accuracy = 0.0;
		String fileSummary = "";

		for (Sample sample : samples) {
			String predicted = classify(sample, decisionTree);
			if (predicted.equals(sample.getClassifier()))
				correctlyClassified++;

			String line = "Actual = " + sample.getClassifier() + ", predicted = " + predicted + "\n";
			fileSummary += line;
		}

		accuracy = (double) correctlyClassified / (double) samples.size();
		Result result = new Result(correctlyClassified, samples.size() - correctlyClassified, accuracy, samples.size(),
				fileSummary);
		return result;
		// results.add(result);

	}

	/**
	 * Gets the average classification accuracy value from a list of results
	 * 
	 * @param results
	 * @return
	 */
	public static double getAverageClassificationAccuracy(List<Result> results) {
		Double total = 0.0;
		for (Result result : results) {
			total += result.getClassificationAccuracy();
		}
		classificationAccuracyAvg = total / (double) results.size();
		return classificationAccuracyAvg;
	}

	/**
	 * Classifies a sample
	 * 
	 * @param sample
	 * @param node
	 * @return
	 */
	private static String classify(Sample sample, Node node) {
		// if a node is a leaf then return the nodes attribute name
		if (node.isLeaf) {
			return node.attributeName;
		}
		// traverse the tree either left or right by comparing the the samples
		// value to the nodes split value
		// TODO tidy up
		double sampleValue = sample.getFeature(node.attributeName);

		if (sampleValue <= node.splitValue)
			return classify(sample, (Node) node.children.get(0));

		else
			return classify(sample, (Node) node.children.get(1));

	}

	/**
	 * Generate a C4.5 decision tree using samples and the list of fields
	 * 
	 * @param samples
	 * @param parentSamples
	 * @param fields
	 * @return
	 */
	private static Node<List<Sample>> generateDecisionTree(List<Sample> samples, List<Sample> parentSamples,
			List<String> fields) {

		List<String> localFields = new ArrayList<String>(fields);

		System.out.println("Starting localAttributes " + localFields.toString());
		// if the list of owls is empty return the most frequent type of owl in
		// the nodes parent
		if (samples.isEmpty()) {
			if (debug)
				System.out.println("owls is empty");
			return new Node(getMostFrequentType(parentSamples), true);
		}

		// if the list of owls all have the same type then return that type,
		// calculating inf gain would be pointless
		if (checkAllSameLabel(samples)) {
			if (debug)
				System.out.println("all same label");
			return new Node(samples.get(0).getClassifier(), true);
		}
		// calc attribute with the highest information gain
		String bestFeature = BestFieldCalculator.getFieldToSplitOn(samples, localFields);

		// if bestAttribute is null return most frequent type in owls
		if (bestFeature == null) {
			if (debug)
				System.out.println("best attribute is null");
			return new Node(getMostFrequentType(samples), true);
		}
		// TODO think of alternative to bestAttribute
		// make the root node with the bestAttribute to split on and make the
		// node not a leaf node
		Node root = new Node(bestFeature, false);

		// Split owls based on best attribute
		// TODO not use subsets just use two lists

		LinkedHashMap<String, List<Sample>> subsets = new LinkedHashMap<String, List<Sample>>();

		double currentSplitValue = BestFieldCalculator.bestSplitValue;

		root.splitValue = currentSplitValue;

		List<Sample> samplesLessThan = new ArrayList<Sample>();
		List<Sample> samplesGreaterThan = new ArrayList<Sample>();

		// loop through owls and add them to the right list by comparing them to
		// the split value
		for (Sample sample : samples) {

			double value = sample.getFeature(bestFeature);

			if (value <= currentSplitValue)
				samplesLessThan.add(sample);
			else
				samplesGreaterThan.add(sample);
		}

		subsets.put("samplesLessThan", samplesLessThan);
		subsets.put("samplesGreaterThan", samplesGreaterThan);

		if (debug) {
			System.out.println("");
			System.out.println(
					"Subsets partitioned on " + bestFeature + " with a split value of " + currentSplitValue + " ");
			System.out.println("");
			System.out.println("Less than list of size " + subsets.get("samplesLessThan").size());
		}

		List<Sample> lessThanSamples = subsets.get("samplesLessThan");

		if (debug) {
			for (Sample sample : lessThanSamples)
				System.out.print(sample.getFeature(bestFeature) + " with type " + sample.getClassifier() + " ");
			System.out.println("");
			System.out.println("");
			System.out.println("Greater than list of size " + subsets.get("samplesGreaterThan").size());
		}

		List<Sample> greaterThanSamples = subsets.get("samplesGreaterThan");

		for (Sample sample : greaterThanSamples)
			System.out.print(sample.getFeature(bestFeature) + " with type " + sample.getClassifier() + " ");

		// remove the attribute from the list so that is removed from
		// consideration
		localFields.remove(bestFeature);
		if (debug) {
			System.out.println("");
			System.out.println("");
			System.out.println("New localFields list with bestFeature removed " + localFields.toString());
		}
		// Iterate through possible values of bestAttribute
		for (String key : subsets.keySet()) {

			// Add child to subtree of root
			root.addChild(generateDecisionTree(subsets.get(key), samples, localFields));

			// child.splitValue = currentSplitValue;

		}
		return root;
	}

	// }

	// TODO rewrite
	/**
	 * Gets the most frequently occurring element of a list of samples
	 * 
	 * @param samples
	 * @return
	 */
	private static String getMostFrequentType(List<Sample> samples) {

		if (samples.isEmpty()) {
			if(debug)
				System.out.println("empty list");
			return null;
		}

		Map<Sample, Integer> map = new HashMap();

		for (Sample sample : samples) {
			Integer val = map.get(sample);
			map.put(sample, val == null ? 1 : val + 1);
		}

		Entry<Sample, Integer> max = null;

		for (Entry<Sample, Integer> e : map.entrySet()) {
			if (max == null || e.getValue() > max.getValue())
				max = e;
		}

		return max.getKey().getClassifier();

	}
	
	

	/**
	 * Checks if a list of samples all have the same label, e.g. all of type
	 * BarnOwl
	 * 
	 * @param samples
	 * @return
	 */
	private static boolean checkAllSameLabel(List<Sample> samples) {

		if (samples.isEmpty()) {
			if(debug)
				System.out.println("empty list");
			return false;
		}

		Map<Sample, Integer> map = new HashMap();

		for (Sample sample : samples) {
			Integer val = map.get(sample);
			map.put(sample, val == null ? 1 : val + 1);
		}

		Entry<Sample, Integer> max = null;

		for (Entry<Sample, Integer> e : map.entrySet()) {
			if (max == null || e.getValue() > max.getValue())
				max = e;
		}

		if (max.getValue() == samples.size()) {
			return true;
		}

		else
			return false;
	}

}
