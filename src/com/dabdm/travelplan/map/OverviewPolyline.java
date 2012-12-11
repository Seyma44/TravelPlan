package com.dabdm.travelplan.map;

/**
 * Represent the overview_polyline attribute of a route in the object return by the Google Directions API
 * Used to get a Java object from the JSON response
 */
public class OverviewPolyline {
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
