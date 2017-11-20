import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;

import Owls.Owl;
import Owls.Sample;

public class BestFieldCalculator {

	private static Double splitValue;

	private static boolean debug = Algorithm.debug;

	public static Double bestSplitValue;

	// Labels to classify as
	private static String label1 = "BarnOwl";
	private static String label2 = "LongEaredOwl";
	private static String label3 = "SnowyOwl";

	/*
	 * Returns best attribute owls, remaining attributes Returns null if no
	 * attribute has positive information gain Returns null if no candidate
	 * splits remaining
	 */
	public static String getFieldToSplitOn(List<Sample> samples, List<String> fields) {
		double maxInfoGain = 0.0;
		String bestAtt = null;

		for (String field : fields) {
			Double infoGain = getMaxInformationGain(samples, field);
			System.out.println("Gain of attribute " + field + " is " + infoGain);

			if (infoGain > maxInfoGain) {
				maxInfoGain = infoGain;
				bestAtt = field;
				// bestSplitValue gets split value of numeric attribute with
				// highest gain
				bestSplitValue = splitValue;
			}
		}
		if (maxInfoGain <= 0.0)
			return null;

		if (debug) {
			for (String attribute : fields) {
				System.out.println("Gain of field " + attribute + " is " + maxInfoGain);
			}
		}

		return bestAtt;
	}

	/**
	 * Calculates the max information gain of an attribute by testing on a
	 * number of split values
	 * 
	 * @param samples
	 * @param attribute
	 * @return
	 */
	private static double getMaxInformationGain(List<Sample> samples, String attribute) {
		if (samples.isEmpty())
			return 0.0;

		// Get list of possible split points
		// TODO use something other than a tree set
		TreeSet<Double> values = new TreeSet<Double>();
		for (Sample sample : samples)
			values.add(sample.getFeature(attribute));

		List<Double> possibleSplitValues = new ArrayList<Double>();
		// TODO change this
		Iterator<Double> num1 = values.iterator();
		Iterator<Double> num2 = values.iterator();
		if (num2.hasNext())
			num2.next();
		while (num2.hasNext())
			possibleSplitValues.add((num1.next() + num2.next()) / 2);

		if (debug)
			System.out.println("Possible Splits " + possibleSplitValues.toString());

		// Find maximum gain from among possible split points
		double maxInfoGain = 0.0;
		splitValue = 0.0;
		for (Double split : possibleSplitValues) {
			double infoGain = getEntropy(samples) - getEntropy(samples, attribute, split);

			if (debug)
				System.out.println("gain " + infoGain);
			if (infoGain > maxInfoGain) {
				maxInfoGain = infoGain;
				splitValue = split;
			}
		}
		return maxInfoGain;
	}

	/**
	 * Gets the entropy of splitting an attribute on a split value
	 * 
	 * @param samples
	 * @param feature
	 * @param splitValue
	 * @return
	 */
	private static double getEntropy(List<Sample> samples, String feature, double splitValue) {

		if (samples.isEmpty())
			return 0.0;

		// Divide the list based on the split point
		List<Sample> samplesLessThan = new ArrayList<Sample>();
		List<Sample> samplesGreaterThan = new ArrayList<Sample>();

		for (Sample sample : samples) {
			double value = sample.getFeature(feature);
			if (value <= splitValue)
				samplesLessThan.add(sample);
			else
				samplesGreaterThan.add(sample);
		}

		if (debug) {
			System.out.println("No of samples less than split value " + samplesLessThan.size());
			System.out.println("No of samples greater than split value " + samplesGreaterThan.size());
		}

		double ratioGreater = (double) samplesLessThan.size() / (double) samples.size();
		double ratioLess = (double) samplesGreaterThan.size() / (double) samples.size();

		if (debug) {
			System.out.println("No of samples less than split value as a fraction " + ratioLess);
			System.out.println("No of samples greater than split value as a fraction " + ratioGreater);
			System.out.println("");
		}

		return ratioGreater * getEntropy(samplesLessThan) + ratioLess * getEntropy(samplesGreaterThan);
	}

	/**
	 * Calculates the entropy of a list of samples
	 * 
	 * @param samples
	 * @return
	 */
	private static double getEntropy(List<Sample> samples) {

		if (samples.isEmpty())
			return 0.0;

		Map<Sample, Integer> sampleMap = new HashMap<Sample, Integer>();

		for (Sample sample : samples) {
			Integer val = sampleMap.get(sample);
			sampleMap.put(sample, val == null ? 1 : val + 1);
		}

		Map<Sample, Double> mapOfRatios = new HashMap<Sample, Double>();

		for (Entry<Sample, Integer> e : sampleMap.entrySet()) {
			mapOfRatios.put(e.getKey(), (double) e.getValue() / (double) sampleMap.size());
		}

		double entropyVal = 0.0;

		for (Entry<Sample, Double> e : mapOfRatios.entrySet()) {
			entropyVal += e.getValue() * getLog(e.getValue());
		}

		return -(entropyVal);
	}

	/**
	 * Calculates the log base 2 of a number
	 * 
	 * @param x
	 * @return
	 */
	private static double getLog(double x) {
		if (x == 0.0)
			return 0.0;
		return (Math.log(x) / Math.log(2.0));
	}
}
