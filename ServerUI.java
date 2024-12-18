import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerUI {
    private JFrame frame;
    private JTextArea textArea;
    private Registry registry;

    public ServerUI() {
        // Set up the frame
        frame = new JFrame("Library Server");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Set up the text area for logs
        textArea = new JTextArea();
        textArea.setBounds(10, 10, 380, 200);
        textArea.setEditable(false);
        frame.add(textArea);

        // Start Server button
        JButton startButton = new JButton("Start Server");
        startButton.setBounds(10, 220, 180, 30);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> startServer()).start(); // Run the server in a separate thread
            }
        });
        frame.add(startButton);

        // Stop Server button
        JButton stopButton = new JButton("Stop Server");
        stopButton.setBounds(200, 220, 180, 30);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer(); // 
            }
        });
        frame.add(stopButton);

        frame.setVisible(true);
    }

    private void startServer() {
        try {
            appendText("Starting server...");
            LibraryManager libraryManager = new LibraryServiceImpl();
            registry = LocateRegistry.createRegistry(1900);
            registry.rebind("LibraryManager", libraryManager);
            appendText("Server started successfully on port 1900.");
        } catch (Exception ex) {
            appendText("Failed to start server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void stopServer() {
        try {
            if (registry != null) {
                registry.unbind("LibraryManager");
                appendText("Server stopped successfully.");
            } else {
                appendText("Server is not running.");
            }
        } catch (Exception ex) {
            appendText("Failed to stop server: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            System.exit(0); // Close the application
        }
    }

    private void appendText(String text) {
        textArea.append(text + "\n");
    }

    public static void main(String[] args) {
        new ServerUI();
    }
}

