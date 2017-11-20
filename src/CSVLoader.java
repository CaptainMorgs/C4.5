import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Owls.Owl;
import Owls.Sample;

//TODO test with diff no. of attributes
public class CSVLoader {

	private  ArrayList<Sample> sampleList = new ArrayList<>();

	private  List<String> featureNames = new ArrayList<String>();

	public  String classifierFeature;

	public  int classifierIndex = 4;

	public  String pathToCSV = "owls15.csv";

	public ArrayList<Sample> getSampleList() {
		return sampleList;
	}

	/*public void setSampleList(ArrayList<Sample> owlList) {
		CSVLoader.sampleList = owlList;
	}*/

	public void loadCSV() {
		sampleList.clear();

		BufferedReader br = null;
		String line = "";
		String splitter = ",";
		String[] csvLine = null;
		int i = 0;
		int j = 0;
		try {

			br = new BufferedReader(new FileReader(pathToCSV));

			// TODO make better solution
			// this approach only works if the classifer feature is the last
			// column in the file
			// Assuming the first line is the column names
			if ((line = br.readLine()) != null)
				for (i = 0; i < classifierIndex; i++) {
					csvLine = line.split(splitter);
					featureNames.add(csvLine[i]);

				}
			System.out.println(featureNames.toString());
			// set the classifierFeature to the last column
			classifierFeature = csvLine[i];
			System.out.println(classifierFeature.toString());

			while ((line = br.readLine()) != null) {

				// use comma as separator
				csvLine = line.split(splitter);

				Sample sample = new Sample();
				// TODO not have to set them for every feature, they are the
				// same for each of them
				sample.setFeatureNames(featureNames);
				for (j = 0; j < i; j++) {
					sample.getFeatures().add(Double.parseDouble(csvLine[j]));
				}
				sample.setClassifier(csvLine[j]);

				System.out.println(sample.toString());

				sampleList.add(sample);

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

		System.out.println("Number of samples added: " + sampleList.size());
		System.out.println();
	}

	public List<String> getFeatureNames() {
		return featureNames;
	}

	/*
	 * public static void setFeatureNames(List<String> featureNames) {
	 * CSVLoader.featureNames = featureNames; }
	 */

}