
import java.util.ArrayList;
import java.util.List;

/**
 * Generic class to handle a row of data in a csv file, i.e. a sample to be
 * classified
 * 
 * @author John
 *
 */
public class Sample {

	// the name of the features
	private List<String> featureNames = new ArrayList<String>();

	// the features
	private List<Double> features = new ArrayList<Double>();

	// the classifier of the sample
	private String classifier;

	public Double getFeature(String string) {
		int index = featureNames.indexOf(string);
		if (index != -1) {
			return features.get(index);
		} else {
			System.err.println(string + " can not be found");
			return 0.0;
		}
	}

	public List<String> getFeatureNames() {
		return featureNames;
	}

	public void setFeatureNames(List<String> featureNames) {
		this.featureNames = featureNames;
	}

	public List<Double> getFeatures() {
		return features;
	}

	public void setFeatures(List<Double> features) {
		this.features = features;
	}

	public String getClassifier() {
		return classifier;
	}

	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}

	@Override
	public String toString() {
		return "Sample [featureNames=" + featureNames + ", features=" + features + ", classifier=" + classifier + "]";
	}

}
