package app;

import player.PlayerUI;
import recommender.RecommenderUI;
import gps.GpsUI;

public class App {

	private PlayerUI playerUI;
	private RecommenderUI recommenderUI;
	private GpsUI gpsUI;

	public static void main(String[] args) {
		new App();
	}

	public App() {
		playerUI = PlayerUI.createAndShowGUI(this);
		recommenderUI = RecommenderUI.createAndShowGUI(this);
		gpsUI = GpsUI.createAndShowGUI(this);
	}

	public PlayerUI getPlayerUI() {
		return playerUI;
	}

	public RecommenderUI getRecommenderUI() {
		return recommenderUI;
	}

	public GpsUI getGpsUI() {
		return gpsUI;
	}

}
