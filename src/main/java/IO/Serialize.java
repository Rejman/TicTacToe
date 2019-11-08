package IO;

import RL.Policy;

import java.io.*;
import java.util.HashMap;

public abstract class Serialize {

    public static Policy loadPolicy(String filename){
        Policy policy;
        // Deserialization
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            // Method for deserialization of object
            policy = (Policy) in.readObject();

            in.close();
            file.close();

            System.out.println("Policy "+filename+" has load");
            return policy;
        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }
        return null;
    }

    public static void savePolicy(String filename, Policy policy){
        // Serialization
        try
        {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);
            //Method for serialization of object
            out.writeObject(policy);
            out.close();
            file.close();
            System.out.println("Policy has saved as "+filename);

        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
    }
}
