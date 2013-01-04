package com.dabdm.places;

/**
 * Class for handling the response from a places search
 * Used to build a Java object from the JSON answer with gson
 * TODO Complete it with missing attributes
 */
public class PlacesList {
    
    private String status;
    private Place[] results;
    
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Place[] getResults() {
        return results;
    }
    public void setResults(Place[] results) {
        this.results = results;
    }
}
