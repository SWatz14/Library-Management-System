

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface LibraryManager extends Remote {
    List<String> searchBooks(String keyword) throws RemoteException;
    String borrowBook(String bookId, String memberId) throws RemoteException;
    String returnBook(String bookId, String memberId, String returnDate) throws RemoteException;
    List<String> getBorrowedBooks(String memberId) throws RemoteException;
    String addBook(String title, String author, int copies) throws RemoteException;
    List<String> viewAllBooks() throws RemoteException; 
}

