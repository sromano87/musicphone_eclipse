package commons.interfaces;

import java.util.List;

import commons.dataClasses.Destination;
import commons.dataClasses.Recommendation;

public interface IRecommender {
	
	IConnector getConnector();

	List<Recommendation> getRecommendations() throws Exception;

	List<Destination> getDestinationsForArtists(String artist);

	List<Destination> buildItineraryForArtists(List<String> artists);

}
