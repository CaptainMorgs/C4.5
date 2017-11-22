import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DecisionTreeGenerator {

	private static boolean debug;

	/**
	 * Generate a C4.5 decision tree using samples and the list of fields
	 * 
	 * @param samples
	 * @param parentSamples
	 * @param fields
	 * @return
	 */
	public static MyNode<List<Sample>> generateDecisionTree(List<Sample> samples, List<Sample> parentSamples,
			List<String> fields) {

		debug = C45.debug;

		List<String> localFields = new ArrayList<String>(fields);

		if (debug)
			System.out.println("Starting localAttributes " + localFields.toString());

		// if the list of owls is empty return the most frequent type of owl in
		// the nodes parent
		if (samples.isEmpty()) {
			if (debug)
				System.out.println("owls is empty");
			return new MyNode<List<Sample>>(getMostFrequentType(parentSamples), true);
		}

		// if the list of owls all have the same type then return that type,
		// calculating inf gain would be pointless
		if (checkAllSameLabel(samples)) {
			if (debug)
				System.out.println("all same label");
			return new MyNode<List<Sample>>(samples.get(0).getClassifier(), true);
		}

		// calc attribute with the highest information gain
		String bestFeature = BestFieldCalculator.getFieldToSplitOn(samples, localFields);

		// if bestAttribute is null return most frequent type in owls
		if (bestFeature == null) {
			if (debug)
				System.out.println("best attribute is null");
			return new MyNode<List<Sample>>(getMostFrequentType(samples), true);
		}

		// make the root node with the bestFeature to split on and make the
		// node not a leaf node
		MyNode<List<Sample>> root = new MyNode<List<Sample>>(bestFeature, false);

		// Split owls based on best attribute
		// TODO not use subsets just use two lists

		HashMap<String, List<Sample>> hashMap = new HashMap<String, List<Sample>>();

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

		hashMap.put("samplesLessThan", samplesLessThan);
		hashMap.put("samplesGreaterThan", samplesGreaterThan);

		if (debug) {
			System.out.println("");
			System.out.println(
					"Subsets partitioned on " + bestFeature + " with a split value of " + currentSplitValue + " ");
			System.out.println("");
			System.out.println("Less than list of size " + hashMap.get("samplesLessThan").size());
		}

		List<Sample> lessThanSamples = hashMap.get("samplesLessThan");

		if (debug) {
			for (Sample sample : lessThanSamples)
				System.out.print(sample.getFeature(bestFeature) + " with type " + sample.getClassifier() + " ");
			System.out.println("");
			System.out.println("");
			System.out.println("Greater than list of size " + hashMap.get("samplesGreaterThan").size());
		}

		List<Sample> greaterThanSamples = hashMap.get("samplesGreaterThan");

		for (Sample sample : greaterThanSamples)
			if (debug)
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
		for (String key : hashMap.keySet()) {

			// Add child to subtree of root
			root.addChild(generateDecisionTree(hashMap.get(key), samples, localFields));

		}
		return root;
	}

	/**
	 * Gets the most frequently occurring element of a list of samples
	 * 
	 * @param samples
	 * @return
	 */
	private static String getMostFrequentType(List<Sample> samples) {

		if (samples.isEmpty()) {
			if (debug)
				System.out.println("empty list");
			return null;
		}

		Map<Sample, Integer> sampleCount = new HashMap<Sample, Integer>();

		for (Sample sample : samples) {
			Integer val = sampleCount.get(sample);
			sampleCount.put(sample, val == null ? 1 : val + 1);
		}

		Entry<Sample, Integer> max = null;

		for (Entry<Sample, Integer> entry : sampleCount.entrySet()) {
			if (max == null || entry.getValue() > max.getValue())
				max = entry;
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
			if (debug)
				System.out.println("empty list");
			return false;
		}

		Map<Sample, Integer> sampleCount = new HashMap<Sample, Integer>();

		for (Sample sample : samples) {
			Integer val = sampleCount.get(sample);
			sampleCount.put(sample, val == null ? 1 : val + 1);
		}

		Entry<Sample, Integer> maxCount = null;

		for (Entry<Sample, Integer> entry : sampleCount.entrySet()) {
			if (maxCount == null || entry.getValue() > maxCount.getValue())
				maxCount = entry;
		}
		// if the maxCount is the same as the size of the list, they are all the
		// same type
		if (maxCount.getValue() == samples.size()) {
			return true;
		}

		else
			return false;
	}
}
