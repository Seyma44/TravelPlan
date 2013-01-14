package com.dabdm.travelplan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class StorageHelper {
	public static void travelSerialize(File filesDir, String itineraryName, Travel travel)
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
    
    public static Travel travelDeserialize(File filesDir, String itineraryName)
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
