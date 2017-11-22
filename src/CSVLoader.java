import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class loads in a .csv file
 * 
 * @author John
 *
 */
public class CSVLoader {

	private static ArrayList<Sample> sampleList = new ArrayList<>();

	private static List<String> featureNames = new ArrayList<String>();

	public static String classifierFeature;

	private static int classifierIndex = 4;

	public static String pathToCSV = "owls15.csv";

	private static boolean debug;

	public static void loadCSV() {
		debug = C45.debug;

		sampleList.clear();

		BufferedReader br = null;
		String line = "";
		String splitter = ",";
		String[] csvLine = null;
		int i = 0;
		int j = 0;
		try {

			br = new BufferedReader(new FileReader(pathToCSV));

			// this approach assumes the classifer feature is the last
			// column in the file
			// Assuming the first row is the column names
			if ((line = br.readLine()) != null)
				for (i = 0; i < classifierIndex; i++) {
					csvLine = line.split(splitter);
					featureNames.add(csvLine[i]);

				}
			if (debug)
				System.out.println(featureNames.toString());
			// set the classifierFeature to the last column
			classifierFeature = csvLine[i];

			while ((line = br.readLine()) != null) {

				// use comma as separator
				csvLine = line.split(splitter);

				Sample sample = new Sample();

				sample.setFeatureNames(featureNames);

				for (j = 0; j < i; j++) {
					sample.getFeatures().add(Double.parseDouble(csvLine[j]));
				}
				sample.setClassifier(csvLine[j]);

				if (debug)
					System.out.println(sample.toString());

				sampleList.add(sample);

				MyGui.fileNotFound = false;
			}

		} catch (FileNotFoundException exception) {
			MyGui.fileNotFound = true;
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
		if (debug) {
			System.out.println("Number of samples added: " + sampleList.size());
			System.out.println();
		}
	}

	public static List<String> getFeatureNames() {
		return featureNames;
	}

	public static ArrayList<Sample> getSampleList() {
		return sampleList;
	}
}