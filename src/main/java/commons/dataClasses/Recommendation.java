package commons.dataClasses;

public class Recommendation implements Comparable<Recommendation> {

	/// a recommendation consists of an artist name weighted by the number of fans
	/// who have that artist as a top artist
	private String artist;
	private int fanCount;

	public Recommendation(String artist, int fanCount) {
		super();
		this.artist = artist;
		this.fanCount = fanCount;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public int getFanCount() {
		return fanCount;
	}

	public void setFanCount(int fanCount) {
		this.fanCount = fanCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Recommendation) {
			Recommendation recommendation = (Recommendation) obj;
			if (this.getArtist().equals(recommendation.getArtist())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(Recommendation recommendation) {
		if (recommendation == null) {
			throw new NullPointerException();
		}
		if (this.getFanCount() > recommendation.getFanCount()) {
			return -1;
		} else if (this.getFanCount() < recommendation.getFanCount()) {
			return 1;
		} else {
			return 0;
		}
	}

}
