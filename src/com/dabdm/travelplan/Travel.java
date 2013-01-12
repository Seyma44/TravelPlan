package com.dabdm.travelplan;

import java.util.ArrayList;
import java.util.List;

import com.dabdm.travelplan.places.Place;

/**
 * Class representing one of the user's travel
 * Contains data about the places to visit and the calculated itineraries
 */
public class Travel {
    private List<Place> places;
    private boolean isCalculatedItinerary = false;
    private ArrayList<String> itineraries = new ArrayList<String>();
}
