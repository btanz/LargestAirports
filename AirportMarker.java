package Airports;

import java.util.List;
import java.lang.Math;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PConstants;
import processing.core.PGraphics;


/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * Benjamin Tanz
 * Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMarker extends CommonMarker {
	public static List<SimpleLinesMarker> routes;
	
	public static int ELL_SIZE = 5;  
	
	public float ellScaledSize;
	
	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {		
        pg.pushStyle();
		
		// draw triangle for each airport
		pg.fill(51, 153, 255);
		ellScaledSize = (getRouteCount() / 400) * ELL_SIZE;
		pg.ellipse(x, y, ellScaledSize, ellScaledSize);
		
		
		// Restore previous drawing style
		pg.popStyle();
		
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		 // show rectangle with title
		
		// show routes
		String name = getName();
		String countryCity = "Country: " + getCountry() + " City: " + getCity();
		String routes = "Nr. of Routes: " + getRouteCount();
		
		pg.pushStyle();
		
		pg.fill(255, 255, 255);
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);
		pg.rect(x, y-ELL_SIZE-39, Math.max(pg.textWidth(name), pg.textWidth(countryCity)) + 22, 50);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(name, x+3, y-ELL_SIZE-38);
		pg.text(countryCity, x+3, y - ELL_SIZE - 23);
		pg.text(routes, x+3, y - ELL_SIZE - 8);
		
		pg.popStyle();
		
	}
	
	private String getCity()
	{
		return getStringProperty("city");
	}
	
	private String getCountry()
	{
		return getStringProperty("country");
	}
	
	private String getName()
	{
		return getStringProperty("name");
	}
	
	private int getRouteCount()
	{
		return (int)getProperty("routeCount");
	}
	
	
}
