package application;

public class Cluster {
	private float scoreCentroid;
	private int clusterNumber;
	
	public Cluster(int clusterNumber, float scoreCentroid) {
		super();
		this.clusterNumber = clusterNumber;
		this.scoreCentroid = scoreCentroid;
	}
    public float getScoreCentroid() {

		return scoreCentroid;
	}
	public void setScoreCentroid(float scoreCentroid) {

		this.scoreCentroid = scoreCentroid;
	}
	public int getClusterNumber() {

		return clusterNumber;
	}
	public void setClusterNumber(int clusterNumber) {

		this.clusterNumber = clusterNumber;
	}

	@Override
	public String toString() {
		return "Cluster [scoreCentroid=" + scoreCentroid + ", clusterNumber=" + clusterNumber + "]";
	}
	
	// Euclidean distance calculation
	public double calculateDistance(Record record) {
		return Math.sqrt(Math.pow((getScoreCentroid() - record.getScore()), 2));
    }

	// Binod Suman Academy YouTube Video on K-Mean Algorithm
	public void updateCentroid(Record record) {

		setScoreCentroid((getScoreCentroid() + record.getScore()) / 2);
	}
}