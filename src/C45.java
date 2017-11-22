
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class runs the C4.5 algorithm
 * 
 * @author John
 *
 */
public class C45 {

	public static boolean debug = false;

	private List<Sample> sampleList, trainingSampleList, testingSampleList;

	private List<String> attributes = new ArrayList<String>();

	public static boolean outputToFile = false;

	public static int numCorrectlyClassified, numIncorrectlyClassified, total;

	public static double successRate;
	public static List<Result> results = new ArrayList<>();

	public static double classificationAccuracyAvg = 0.0;

	public static double trainingSize = 0.4;

	public static List<MyNode<List<Sample>>> decisionTreeList = new ArrayList<>();

	/**
	 * Runs the C4.5 algorithm
	 */
	public void runC45() {

		CSVLoader.loadCSV();

		sampleList = CSVLoader.getSampleList();

		attributes = CSVLoader.getFeatureNames();

		if (debug)
			System.out.println("Starting attributes " + attributes.toString());

		testTenTimes(sampleList, attributes);

		GraphGenerator.generateGraph();

	}

	/**
	 * Run the C4.5 alg on test data 10 times with different random divisions 10
	 * times. Could improve by parameterising the no of runs.
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
			int trainingSetSize = 0;

			try {
				trainingSetSize = (int) Math.floor((sampleList.size() * trainingSize));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

			trainingSampleList = sampleList.subList(0, trainingSetSize);

			testingSampleList = sampleList.subList(trainingSetSize, sampleList.size());

			if (debug)
				System.out.println("Calling generateDecisionTree with fields " + attributes.toString());

			MyNode<List<Sample>> decisionTree = DecisionTreeGenerator.generateDecisionTree(trainingSampleList,
					trainingSampleList, attributes);

			Result result = applyDecisionTree(testingSampleList, decisionTree);

			results.add(result);

			decisionTreeList.add(decisionTree);
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
			PrintWriter out = null;
			try {
				out = new PrintWriter(fileName);
				for (Result result : results) {
					out.println(result.getFileSummary());
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				out.close();
			}
		}
	}

	/**
	 * Applies a decision tree to a list of samples
	 * 
	 * @param samples
	 * @param decisionTree
	 * @return
	 */
	private static Result applyDecisionTree(List<Sample> samples, MyNode<List<Sample>> decisionTree) {

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
	private static String classify(Sample sample, MyNode<List<Sample>> node) {
		// if a node is a leaf then return the nodes attribute name
		if (node.isLeaf) {
			return node.attributeName;
		}
		// traverse the tree either left or right by comparing the the samples
		// value to the nodes split value
		double sampleValue = sample.getFeature(node.attributeName);

		if (sampleValue <= node.splitValue)
			return classify(sample, (MyNode<List<Sample>>) node.children.get(0));

		else
			return classify(sample, (MyNode<List<Sample>>) node.children.get(1));

	}
}