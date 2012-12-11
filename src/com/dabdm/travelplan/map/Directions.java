package com.dabdm.travelplan.map;

import java.util.ArrayList;

/**
 * Represent the object return by the Google Directions API
 * Used to get a Java object from the JSON response *
 */
public class Directions {
    private String status;
    private ArrayList<Route> routes;
    
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public ArrayList<Route> getRoutes() {
        return routes;
    }
    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }
}
