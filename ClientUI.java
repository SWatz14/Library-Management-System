

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ClientUI {
    private JFrame frame;
    private LibraryManager libraryManager;

    public ClientUI() {
        try {
            // Connect to the RMI registry
            Registry registry = LocateRegistry.getRegistry("localhost", 1900);
            libraryManager = (LibraryManager) registry.lookup("LibraryManager");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error connecting to library server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }

        initialize();
    }

    private void initialize() {
        frame = new JFrame("Library Management System");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(2, 3));  // Adjust layout for 2 rows, 3 columns

        JButton searchButton = new JButton("Search Books");
        JButton borrowedButton = new JButton("View Borrowed Books");
        JButton addBookButton = new JButton("Add New Book");
        JButton viewAllBooksButton = new JButton("View All Books");
        JButton BorrowBookButton = new JButton("Borrow Book");

        // Set the button sizes
        searchButton.setPreferredSize(new Dimension(200, 50));
        borrowedButton.setPreferredSize(new Dimension(200, 50));
        addBookButton.setPreferredSize(new Dimension(200, 50));
        viewAllBooksButton.setPreferredSize(new Dimension(200, 50));
        BorrowBookButton.setPreferredSize(new Dimension(200, 50));

        // Add buttons to the frame
        frame.add(searchButton);
        frame.add(borrowedButton);
        frame.add(addBookButton);
        frame.add(viewAllBooksButton);
        frame.add(BorrowBookButton);

        // Add action listeners for each button
        searchButton.addActionListener(e -> searchBooks());
        borrowedButton.addActionListener(e -> viewBorrowedBooks());
        addBookButton.addActionListener(e -> addBook());
        viewAllBooksButton.addActionListener(e -> viewAllBooks());
        BorrowBookButton.addActionListener(e -> borrowBook());  // Add this line for Borrow Book button

        frame.setVisible(true);
    }

    private void searchBooks() {
        try {
            String keyword = JOptionPane.showInputDialog(frame, "Enter keyword to search for books:");
            if (keyword != null && !keyword.isEmpty()) {
                List<String> results = libraryManager.searchBooks(keyword);
                if (results.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No books found for the given keyword.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    StringBuilder result = new StringBuilder("Search Results:\n");
                    for (String book : results) {
                        result.append(book).append("\n");
                    }
                    JOptionPane.showMessageDialog(frame, result.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Keyword cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error searching for books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void viewBorrowedBooks() {
        try {
            String memberId = JOptionPane.showInputDialog(frame, "Enter Member ID:");
            if (memberId != null && !memberId.isEmpty()) {
                List<String> borrowedBooks = libraryManager.getBorrowedBooks(memberId);
                StringBuilder result = new StringBuilder();
                for (String book : borrowedBooks) {
                    result.append(book).append("\n");
                }
                JOptionPane.showMessageDialog(frame, result.toString(), "Borrowed Books", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Member ID cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error fetching borrowed books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void addBook() {
        try {
            JTextField titleField = new JTextField();
            JTextField authorField = new JTextField();
            JTextField copiesField = new JTextField();

            Object[] message = {
                "Title:", titleField,
                "Author:", authorField,
                "Copies:", copiesField
            };

            int option = JOptionPane.showConfirmDialog(frame, message, "Add New Book", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                int copies;

                try {
                    copies = Integer.parseInt(copiesField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid number of copies.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!title.isEmpty() && !author.isEmpty() && copies > 0) {
                    String result = libraryManager.addBook(title, author, copies);
                    JOptionPane.showMessageDialog(frame, result, "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "All fields must be filled correctly.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error adding book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void viewAllBooks() {
        try {
            List<String> books = libraryManager.viewAllBooks();
            StringBuilder result = new StringBuilder("Books in Library:\n");
            for (String book : books) {
                result.append(book).append("\n");
            }
            JOptionPane.showMessageDialog(frame, result.toString(), "Library Books", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error fetching books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void borrowBook() {
        try {
            String bookId = JOptionPane.showInputDialog(frame, "Enter Book ID:");
            String memberId = JOptionPane.showInputDialog(frame, "Enter Member ID:");
            if (bookId != null && memberId != null && !bookId.isEmpty() && !memberId.isEmpty()) {
                String result = libraryManager.borrowBook(bookId, memberId);
                JOptionPane.showMessageDialog(frame, result, "Borrow Book", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Book ID and Member ID cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error borrowing book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientUI::new);
    }
}
