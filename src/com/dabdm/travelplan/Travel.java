package com.dabdm.travelplan;

import java.io.Serializable;
import java.util.ArrayList;

import com.dabdm.travelplan.places.Place;
import com.google.android.gms.maps.model.LatLng;

/**
 * Class representing one of the user's travel
 * Contains data about the places to visit and the calculated itineraries
 */
public class Travel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private ArrayList<Place> places;
    private ArrayList<String> itineraries = new ArrayList<String>();
    private String travelName; // The name of the project
    private String placeName; // The place (city) you visit
    private LatLng placeCoordinates; // The coordinates of the place you visit
    private int radius; // Radius (in meters) from the placeCoordinates where to search for places to visit
    private int duration; // Travel duration in days
    private String transportMode; // walking, driving, transit, bicycling
    
    public String getTravelName() {
        return travelName;
    }
    public void setTravelName(String travelName) {
        this.travelName = travelName;
    }
    public String getPlaceName() {
        return placeName;
    }
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
    public LatLng getPlaceCoordinates() {
        return placeCoordinates;
    }
    public void setPlaceCoordinates(LatLng placeCoordinates) {
        this.placeCoordinates = placeCoordinates;
    }
    public int getRadius() {
        return radius;
    }
    public void setRadius(int radius) {
        this.radius = radius;
    }
    public String getTransportMode() {
        return transportMode;
    }
    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }    
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public ArrayList<Place> getPlaces() {
        return places;
    }
    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }
    public ArrayList<String> getItineraries() {
        return itineraries;
    }
    public void setItineraries(ArrayList<String> itineraries) {
        this.itineraries = itineraries;
    }
    public void addItinerary(String encodedItinerary) {
	this.itineraries.add(encodedItinerary);
    }
}
