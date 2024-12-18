


import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.*;

public class LibraryServiceImpl extends UnicastRemoteObject implements LibraryManager {
    private Map<String, Book> books; // Store books with their IDs as keys
    private Map<String, List<BorrowedBook>> borrowedBooks; // Map of user IDs to borrowed books

    protected LibraryServiceImpl() throws RemoteException {
        books = new HashMap<>();
        borrowedBooks = new HashMap<>();

    
        books.put("1", new Book("1", "Java Programming", "Author A", 5));
        books.put("2", new Book("2", "Intro to Python", "Author B", 3));
        books.put("3", new Book("3", "Distributed Systems", "Author C", 2));
        books.put("4", new Book("4", "Data Structures", "Author D", 4));
        books.put("5", new Book("5", "Algorithms", "Author E", 3));
    }

    @Override
    public List<String> searchBooks(String keyword) throws RemoteException {
        List<String> results = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                book.getAuthor().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(book.toString());
            }
        }
        return results;
    }

    @Override
    public String borrowBook(String bookId, String memberId) throws RemoteException {
        Book book = books.get(bookId);
        if (book == null) {
            return "Book not found.";
        }
        if (book.getCopies() <= 0) {
            return "No copies available.";
        }

        // Borrow the book
        book.setCopies(book.getCopies() - 1);
        borrowedBooks.putIfAbsent(memberId, new ArrayList<>());
        borrowedBooks.get(memberId).add(new BorrowedBook(bookId, new Date()));

        return "Book borrowed successfully.";
    }

    @Override
    public String returnBook(String bookId, String memberId, String returnDate) throws RemoteException {
        List<BorrowedBook> memberBorrowedBooks = borrowedBooks.get(memberId);
        if (memberBorrowedBooks == null) {
            return "No borrowed books found for the user.";
        }

        boolean bookFound = memberBorrowedBooks.removeIf(borrowedBook -> borrowedBook.getBookId().equals(bookId));
        if (bookFound) {
            // Return the book
            Book book = books.get(bookId);
            if (book != null) {
                book.setCopies(book.getCopies() + 1);
            }
            return "Book returned successfully.";
        }
        return "Book not found in borrowed list.";
    }

    @Override
    public List<String> getBorrowedBooks(String memberId) throws RemoteException {
        List<String> results = new ArrayList<>();
        List<BorrowedBook> memberBorrowedBooks = borrowedBooks.get(memberId);
        if (memberBorrowedBooks == null) {
            return Collections.singletonList("No borrowed books found.");
        }

        for (BorrowedBook borrowedBook : memberBorrowedBooks) {
            Book book = books.get(borrowedBook.getBookId());
            if (book != null) {
                results.add(book.toString() + " (Borrowed on: " + borrowedBook.getBorrowDate() + ")");
            }
        }
        return results;
    }

    @Override
    public String addBook(String title, String author, int copies) throws RemoteException {
        String newBookId = String.valueOf(books.size() + 1);
        books.put(newBookId, new Book(newBookId, title, author, copies));
        return "Book added successfully.";
    }

    @Override
    public List<String> viewAllBooks() throws RemoteException {
        List<String> results = new ArrayList<>();
        for (Book book : books.values()) {
            results.add(book.toString());
        }
        return results;
    }
}

class Book {
    private String id;
    private String title;
    private String author;
    private int copies;

    public Book(String id, String title, String author, int copies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.copies = copies;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    @Override
    public String toString() {
        return id + ": " + title + " by " + author + " (" + copies + " copies available)";
    }
}

class BorrowedBook {
    private String bookId;
    private Date borrowDate;

    public BorrowedBook(String bookId, Date borrowDate) {
        this.bookId = bookId;
        this.borrowDate = borrowDate;
    }

    public String getBookId() {
        return bookId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }
}


