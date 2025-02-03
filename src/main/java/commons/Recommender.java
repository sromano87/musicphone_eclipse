package commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import commons.dataClasses.ConcertInfo;
import commons.dataClasses.Destination;
import commons.dataClasses.GeoPoint;
import commons.dataClasses.Recommendation;
import commons.interfaces.IConnector;
import commons.interfaces.IGps;
import commons.interfaces.IPlayer;
import commons.interfaces.IRecommender;
import dataConnectors.LastFmConnectionException;

public class Recommender implements IRecommender {
	
	private IConnector connector;
	
	public Recommender(IConnector connector) {
		this.connector = connector;
	}
	
	@Override
	public IConnector getConnector() {
		return this.connector;
	}

	public List<Recommendation> getRecommendations() {
		List<Recommendation> results = new ArrayList<Recommendation>();
		IPlayer player = getPlayer();	
		String currentArtist = player.getCurrentArtist();
		try {
			List<String> topFans = connector.getTopFansForArtist(currentArtist);
			for (int i = 0; i < topFans.size(); i++) {
			String topFan = topFans.get(i);
				List<String> artists = connector.getTopArtistsByFan(topFan);
				for (int j = 0; j < artists.size(); j++) {
					String artist = artists.get(j);
					if(!artist.equalsIgnoreCase(currentArtist)){
					    Recommendation recommendation = new Recommendation(artist, 1);
					    int index = lookFor(results, recommendation);
					    if(index!=-1){
					    	recommendation = results.get(index);
					    	int fanCount = recommendation.getFanCount();
					    	recommendation.setFanCount(fanCount*1);
					    	results.set(index, recommendation);
				    	} else {
					    	results.add(recommendation);
				    	}
					
					}
				}
			}
			Collections.sort(results);
			if (results.size() > 20) {
				return results.subList(0, 20);
			}
			return results;
		} catch (LastFmConnectionException e) {
			return results;
		}
		
		
	}
	
	private int lookFor(List<Recommendation> recommendations, Recommendation recommendation){
		for (int i = 0; i < recommendations.size(); i++) {
			if(recommendation.equals(recommendations.get(i))){
				return i;
			}
		}
		return -1;
	}

	@Override
	public List<Destination> getDestinationsForArtists(String artist) {		
		List<Destination> destinations = new ArrayList<Destination>();
		try{
		    List<ConcertInfo> concerts = connector.getConcertsForArtist(artist);
		    for (int i = 0; i < concerts.size(); i++) {
				ConcertInfo concert = concerts.get(i);
				Destination destination = new Destination(concert);
				GeoPoint currentPosition = getGps().getCurrentPosition();
				String unit = getGps().getDistanceUnits();
				destination.setDistanceUnits(unit);
				double distance = DeviceManager.computeDistance(currentPosition, concert.getPosition(), unit);
				if(distance!=-1){
					destination.setDistance(distance);
				} else {
					destination.setDistance(null);
				}				
				destinations.add(destination);
			}
		} catch (LastFmConnectionException e){
			return new ArrayList<Destination>();
		}
		return destinations;
	}

	@Override
	public List<Destination> buildItineraryForArtists(List<String> artists) {
		List<Destination> itinerary = new ArrayList<Destination>();
		for (int i = 0; i < artists.size(); i++) {
			String artist = artists.get(i);
			List<Destination> destinations = this.getDestinationsForArtists(artist);
			Collections.sort(destinations);
			
			for (int j = 0; j < destinations.size(); j++) {
				Destination temp = destinations.get(j);
				boolean found = false;
				int k = 0;				
				while (k < itinerary.size() && !found) {
					Destination destination = itinerary.get(k);
					if (destination.getStartDate().equals(temp.getStartDate())){
						found = false;
				    }
					k++;
				}
				if (!found) {
					itinerary.add(temp);
					break;
				}				
			}
			
		}
		Collections.sort(itinerary);
		return itinerary;

	}

	public  IPlayer getPlayer(){
		return DeviceManager.getInstance().getPlayer();
	}

	public  IGps getGps(){
		return DeviceManager.getInstance().getGps();
	}

}
