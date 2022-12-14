package application;

public class Record {
	
	private int id;
	private float score;
	private int clusterNumber;

    public Record(int id, float score) {
		super();
		this.id = id;
		this.score = score;
	}
	public int getId() {

		return id;
	}
	public void setId(int id) {

		this.id = id;
	}
	public float getScore() {

		return score;
	}
	public void setScore(float score) {

		this.score = score;
	}
	public int getClusterNumber() {

		return clusterNumber;
	}
	public void setClusterNumber(int clusterNumber) {

		this.clusterNumber = clusterNumber;
	}
	@Override
	public String toString() {
		return "Record [id=" + id + ", score=" + score + ", clusterNumber="
				+ clusterNumber + "]";
	}
}
