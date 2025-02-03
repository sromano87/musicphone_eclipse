package recommender;

import commons.Recommender;
import commons.interfaces.IConnector;
import commons.interfaces.IRecommender;

public class RecommenderAdapter {
	
	private IRecommender recommender;

	public IConnector getConnector() {
		return recommender.getConnector();
	}

	public IRecommender getRecommender() {
		return recommender;
	}

	public void setRecommender(IRecommender recommender) {
		this.recommender = recommender;
	}

	public RecommenderAdapter(IConnector c) {
		this.recommender = new Recommender(c);
	}

}
