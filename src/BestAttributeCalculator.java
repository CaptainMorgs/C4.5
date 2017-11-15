import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import Owls.Owl;

public class BestAttributeCalculator {

	private static Double splitValue;

	private static int debug = 0;
	
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
	public static String getAttributeToSplitOn(List<Owl> owls, List<String> attributes) {
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
