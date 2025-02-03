package commons.interfaces;

import commons.dataClasses.GeoPoint;

public interface IGps {

	public GeoPoint getCurrentPosition();

	public String getDistanceUnits();

	public void setCurrentPosition(GeoPoint point);

	public void setDistanceUnits(String distanceUnits);
	
}
