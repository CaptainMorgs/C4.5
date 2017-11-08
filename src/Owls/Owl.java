package Owls;

public abstract class Owl {

	private double bodyLength, wingLength, bodyWidth, wingWidth;
	
	public Owl(double bodyLength, double wingLength, double bodyWidth, double wingWidth) {
		this.bodyLength = bodyLength;
		this.wingLength = wingLength;
		this.bodyWidth = bodyWidth;
		this.wingWidth = wingWidth;
	}

	public double getBodyLength() {
		return bodyLength;
	}

	public void setBodyLength(double bodyLength) {
		this.bodyLength = bodyLength;
	}

	public double getWingLength() {
		return wingLength;
	}

	public void setWingLength(double wingLength) {
		this.wingLength = wingLength;
	}

	public double getBodyWidth() {
		return bodyWidth;
	}

	public void setBodyWidth(double bodyWidth) {
		this.bodyWidth = bodyWidth;
	}

	public double getWingWidth() {
		return wingWidth;
	}

	public void setWingWidth(double wingWidth) {
		this.wingWidth = wingWidth;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " [bodyLength=" + bodyLength + ", wingLength=" + wingLength + ", bodyWidth=" + bodyWidth
				+ ", wingWidth=" + wingWidth + "]";
	}
}
