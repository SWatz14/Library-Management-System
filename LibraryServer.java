//

import java.rmi.registry.LocateRegistry; // Import the LocateRegistry class from the java.rmi.registry package to create or get the registry on the localhost or specific host and port
import java.rmi.registry.Registry;
import java.rmi.RemoteException;


public class LibraryServer {
    public static void main(String[] args) {
        try {
            // Create and bind the LibraryManager implementation
            LibraryManager libraryManager = new LibraryServiceImpl();
            
            // Try to create a registry on port 1099
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(1900);
                System.out.println("RMI registry created on port 1900.");
            } catch (Exception e) {
                // If registry already exists, reuse it
                System.out.println("RMI registry already exists. Using existing registry.");
                registry = LocateRegistry.getRegistry(  1900);
            }
            
            // Bind the library manager object to the registry
            registry.rebind("LibraryManager", libraryManager);
            System.out.println("Library server is running and LibraryManager is bound.");

        } catch (RemoteException e) {
            System.err.println("RemoteException occurred while starting the server: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

