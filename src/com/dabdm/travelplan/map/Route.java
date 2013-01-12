package com.dabdm.travelplan.map;

/**
 * Represent one of the possible routes in the object return by the Google Directions API
 * Used to get a Java object from the JSON response 
 */
public class Route {
    
    private OverviewPolyline overview_polyline;

    public OverviewPolyline getOverview_polyline() {
	return overview_polyline;
    }

    public void setOverview_polyline(OverviewPolyline overview_polyline) {
	this.overview_polyline = overview_polyline;
    }
}
