package com.dabdm.travelplan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class used to store static methods that store and fetch objects in the internal
 * memory of the device
 */
public class StorageHelper {
	/**
	 * Saves object of type Travel to the internal storage of the mobile
	 * 
	 * @param filesDir directory of files storage
	 * @param itineraryName name of the itinerary to save
	 * @param travel object to save
	 */
	public static void saveTravelObject(File filesDir, String itineraryName, Travel travel)
    {
        try
        {
           FileOutputStream fileOut =
           new FileOutputStream(filesDir + "/" + itineraryName);
           ObjectOutputStream out =
                              new ObjectOutputStream(fileOut);
           out.writeObject(travel);
           out.close();
            fileOut.close();
        }catch(IOException i)
        {
            i.printStackTrace();
        }
    }
    
	/**
	 * Gets travel object from the internal storage
	 * 
	 * @param filesDir directory of files storage
	 * @param itineraryName name of the itinerary to save
	 * @return
	 */
    public static Travel getTravelObject(File filesDir, String itineraryName)
    {
        Travel travel = null;
        try
        {
           FileInputStream fileIn =
                            new FileInputStream(filesDir + "/" + itineraryName);
           ObjectInputStream in = new ObjectInputStream(fileIn);
           travel = (Travel) in.readObject();
           in.close();
           fileIn.close();
        }catch(IOException i)
        {
           i.printStackTrace();
        }catch(ClassNotFoundException c)
        {
           System.out.println("class not found");
           c.printStackTrace();
        }
        return travel;
    }
}