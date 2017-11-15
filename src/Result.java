
public class Result {

	private int numCorrectlyClassified;

	private int numIncorrectlyClassified;

	private double classificationAccuracy;

	private int total;

	private String fileSummary;

	public Result(int numCorrectlyClassified, int numIncorrectlyClassified, double classificationAccuracy, int total,
			String fileSummary) {
		this.numCorrectlyClassified = numCorrectlyClassified;
		this.numIncorrectlyClassified = numIncorrectlyClassified;
		this.classificationAccuracy = classificationAccuracy;
		this.total = total;
		this.fileSummary = fileSummary;
	}

	public int getNumCorrectlyClassified() {
		return numCorrectlyClassified;
	}

	public void setNumCorrectlyClassified(int numCorrectlyClassified) {
		this.numCorrectlyClassified = numCorrectlyClassified;
	}

	public int getNumIncorrectlyClassified() {
		return numIncorrectlyClassified;
	}

	public void setNumIncorrectlyClassified(int numIncorrectlyClassified) {
		this.numIncorrectlyClassified = numIncorrectlyClassified;
	}

	public double getClassificationAccuracy() {
		return classificationAccuracy;
	}

	public void setClassificationAccuracy(double classificationAccuracy) {
		this.classificationAccuracy = classificationAccuracy;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getFileSummary() {
		return fileSummary;
	}

	public void setFileSummary(String fileSummary) {
		this.fileSummary = fileSummary;
	}

	@Override
	public String toString() {
		return "Result [numCorrectlyClassified=" + numCorrectlyClassified + ", numIncorrectlyClassified="
				+ numIncorrectlyClassified + ", classificationAccuracy=" + classificationAccuracy + ", total=" + total
				+ "]";
	}

}
