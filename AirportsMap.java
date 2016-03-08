package Airports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/** AirportsMap
 * An application with an interactive map displaying the world's airports.
 * Author: Benjamin Tanz
 * Builds on code of the UC San Diego Intermediate Software Development MOOC team 
 * Date: March 8, 2016
 * */


public class AirportsMap extends PApplet{

	private static final long serialVersionUID = 1L;

	private UnfoldingMap map;
	
	// path to airport data 
	private String airportsURL = "airports.dat";
	private String routesURL = "routes.dat"; 
	
	protected int routeThreshold = 400; // airports below the route threshold are not displayed 
	
	private CommonMarker lastSelected;
	private List<Marker> airportMarkers;

	public void setup()
	{
		// ** 1 INITIALIZE APPLET AND MAP **
		// set size of applet
		size(900, 700, OPENGL);
		
		// instantiate new unfolding map 
		map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
		
		// add event dispatchers 
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// ** 2 INITIALIZE AND ADD AIRPORT MAP MARKERS AND FEATURES
	    airportMarkers = new ArrayList<Marker>();
		
	    List<PointFeature> airports = ParseFeed.parseAirports(this, airportsURL);
	    List<ShapeFeature> routes = ParseFeed.parseRoutes(this, routesURL);
	    
	    
	    // ** 3 FIND THE MOST IMPORTANT AIRPORTS BASED ON THE COUNT OF INCOMING AND OUTGOING ROUTES **
	    HashMap<Integer, Integer> airportNumRoutes = new HashMap<Integer, Integer>();
	    int airID; 
	    for(PointFeature feature : airports) {
	    	airID = Integer.parseInt(feature.getId());
	    	airportNumRoutes.put(airID, 1);
	    	
	    	for(ShapeFeature route : routes){
	    		
	    		int source = Integer.parseInt((String)route.getProperty("source"));
	    		int destination = Integer.parseInt((String)route.getProperty("destination"));
	    		
	    		if (source == airID || destination == airID)
	    		{
	    			airportNumRoutes.put(airID, airportNumRoutes.get(airID) + 1);
	    		}		    		
	    	}
	    	
	    	feature.addProperty("routeCount", (int)airportNumRoutes.get(airID));
	    	
	    	if ((int)feature.getProperty("routeCount") > 400)
	    	{
	    		airportMarkers.add(new AirportMarker(feature));
	    	}
	    }
	    
	  
	    
	    //for(PointFeature feature : airports) {

	    	
	    //}
	    
	    map.addMarkers(airportMarkers);
	    
	}
	
	public void draw()
	{
		// draw the map and add keys
		map.draw();
		addKey();
	}
	
	
	// method that responds to mouseMove events
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		selectMarkerIfHover(airportMarkers);
		
	}
	
	// helper method for selecting and undselecting
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}
	
	
	// helper method to draw key in GUI
	private void addKey() {	
		
		fill(255, 250, 240);
		
		int xbase = 25;
		int ybase = 50;
		
		rect(xbase, ybase, 150, 120);
		
		float ellScaledSize_400 = (400 / 400) * AirportMarker.ELL_SIZE;
		float ellScaledSize_800 = (800 / 400) * AirportMarker.ELL_SIZE;
		float ellScaledSize_1200 = (1200 / 400) * AirportMarker.ELL_SIZE;
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Airport Size Key", xbase+25, ybase+25);
		
		fill(color(51, 153, 255));
		ellipse(xbase+35, ybase+50, ellScaledSize_400, ellScaledSize_400);
		ellipse(xbase+35, ybase+70, ellScaledSize_800, ellScaledSize_800);
		ellipse(xbase+35, ybase+90, ellScaledSize_1200, ellScaledSize_1200);
		
		textAlign(LEFT, CENTER);
		fill(0, 0, 0);
		text("400 Routes", xbase+50, ybase+50);
		text("600 Routes", xbase+50, ybase+70);
		text("1200 Routes", xbase+50, ybase+90);
		
		
		
	}
	
}


