package com.dabdm.travelplan.places;

import java.io.Serializable;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Represent a place from the Google Places API
 * TODO complete it with the missing attributes
 */
public class Place implements Serializable {
    
    private static final long serialVersionUID = 1354423837706732968L;
    
    private String formatted_address;
    private String vicinity;
    private String icon;
    private String id;
    private String name;
    private float rating;
    private String reference;
    private String[] types;
    private PlaceDetails details;
    private Geometry geometry;
    

    /**
     * @return a description message for that place
     */
    public String getSnippet() {
	// TODO build what should really be displayed in the marker bubble on the map
	return "test description for " + getName();
    }
    
    /**
     * @return the Place coordinates as a LatLng
     */
    public LatLng getCoordinates() {
	return new LatLng(this.getGeometry().getLocation().getLat(), this.getGeometry().getLocation().getLng());
    }
    
    /**
     * Return the formatted address in a correct format to use it in a direction request
     * @return the address with the correct format
     */
    public String getAddressForRequest() {
	Log.i("test", getFormatted_address());
	String result = getFormatted_address().replaceAll(",\\s", ",");
	return result.replaceAll("\\s", "+");
    }
    
    public double getLat() {
	return this.getGeometry().getLocation().getLat();
    }
    
    public double getLng() {
	return this.getGeometry().getLocation().getLng();
    }
    
    public String getFormatted_address() {
        return formatted_address;
    }
    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }    
    public String getVicinity() {
        return vicinity;
    }
    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public String[] getTypes() {
        return types;
    }
    public void setTypes(String[] types) {
        this.types = types;
    }
    
    public PlaceDetails getDetails() {
        return details;
    }

    public void setDetails(PlaceDetails details) {
        this.details = details;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
    
    
    
    public static class Geometry implements Serializable
    {
        private static final long serialVersionUID = 627479979363856000L;
	
	private Location location;

	public Location getLocation() {
	    return location;
	}

	public void setLocation(Location location) {
	    this.location = location;
	}
    }
 
    public static class Location implements Serializable
    {
        private static final long serialVersionUID = -3448797627560173693L;
        
	private double lat;
        private double lng;
        
	public double getLat() {
	    return lat;
	}
	public void setLat(double lat) {
	    this.lat = lat;
	}
	public double getLng() {
	    return lng;
	}
	public void setLng(double lng) {
	    this.lng = lng;
	}
    }
    
}
