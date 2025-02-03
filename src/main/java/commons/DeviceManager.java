package commons;

import commons.dataClasses.GeoPoint;
import commons.interfaces.IGps;
import commons.interfaces.IPlayer;
import static java.lang.Math.*;

public class DeviceManager {

	private final static double EARTH_RADIUS_KM = 6371.01;
	private final static double EARTH_RADIUS_MI = 3958.76;

	public final static String KM = "km";
	public final static String MI = "mi";

	private IPlayer Player;
	private IGps Gps;

	private static DeviceManager instance = null;

	private DeviceManager() {
	}

	public static DeviceManager getInstance() {
		if (instance == null) {
			instance = new DeviceManager();
		}
		return instance;
	}

	public IPlayer getPlayer() {
		return Player;
	}

	public void setPlayer(IPlayer player) {
		Player = player;
	}

	public IGps getGps() {
		return Gps;
	}

	public void setGps(IGps gps) {
		Gps = gps;
	}

	public static double computeDistance(GeoPoint geoPoint1, GeoPoint geoPoint2, String unit) {

		double latR1Deg;
		double lonR1Deg;
		double latR2Deg;
		double lonR2Deg;

		try {
			latR1Deg = Double.parseDouble(geoPoint1.getLatitude());
			lonR1Deg = Double.parseDouble(geoPoint1.getLongitude());
			latR2Deg = Double.parseDouble(geoPoint2.getLatitude());
			lonR2Deg = Double.parseDouble(geoPoint2.getLongitude());
		} catch (NumberFormatException nfe) {
			return -1;
		} catch (NullPointerException npe) {
			return -1;
		}

		if (isWrogLatitude(latR1Deg) || isWrogLatitude(latR2Deg) || isWrogLongitude(lonR1Deg)
				|| isWrogLongitude(lonR2Deg)) {
			return -1;
		}

		double latR1 = toRadians(latR1Deg);
		double lonR1 = toRadians(lonR1Deg);

		double latR2 = toRadians(latR2Deg);
		double lonR2 = toRadians(lonR2Deg);

		double deltaLatR = latR2 - latR1;
		double deltaLonR = lonR2 - lonR1;

		double a = sqrt(pow(sin(deltaLatR / 2), 2) + (cos(latR1) * cos(latR2) * pow(sin(deltaLonR / 2), 2)));

		if (unit != null) {
			if (unit.equalsIgnoreCase(KM)) {
				return 2 * asin(min(1.0, a)) * EARTH_RADIUS_KM;
			} else if (unit.equalsIgnoreCase(MI)) {
				return 2 * asin(min(1.0, a)) * EARTH_RADIUS_MI;
			}
		}
		return -1;
	}

	private static boolean isWrogLatitude(double lat) {
		if (lat < -90 || lat > 90) {
			return true;
		}
		return false;
	}

	private static boolean isWrogLongitude(double lon) {
		if (lon < -180 || lon > 180) {
			return true;
		}
		return false;
	}

}
