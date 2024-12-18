// Code for the client side of the RMI application


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List; 

public class LibraryClient {
    public static void main(String[] args) {
        try {
            // Get the registry from localhost on port 1900
            Registry registry = LocateRegistry.getRegistry("localhost", 1900);

            // Look up the remote object from the registry
            LibraryManager libraryManager = (LibraryManager) registry.lookup("LibraryManager");

            // Search for books with the keyword "java"
            List<String> result = libraryManager.searchBooks("java");

            // Display the results
            if (result.isEmpty()) {
                System.out.println("No books found for the search keyword.");
            } else {
                for (String book : result) {
                    System.out.println(book);
                }
            }
        } catch (Exception e) {
            System.err.println("LibraryClient exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
