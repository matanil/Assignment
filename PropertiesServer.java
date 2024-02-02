import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesServer {
    private int port;
    private Path directoryToWrite;

    public PropertiesServer(int port, String directoryToWrite) {
        this.port = port;
        this.directoryToWrite = Paths.get(directoryToWrite);
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                // Handle client connection in a new thread
                 new Thread(new ClientHandler(clientSocket, directoryToWrite)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
	private Path directoryToWrite;

         public ClientHandler(Socket socket, Path directoryToWrite) {
            this.clientSocket = socket;
            this.directoryToWrite = directoryToWrite;
        }

        @Override
        public void run() {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream())) {
                Properties props = (Properties) objectInputStream.readObject();

                // Write properties to file
                writePropertiesToFile(props);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void writePropertiesToFile(Properties props) {
            // Ensure directory exists
	    if (!Files.exists(directoryToWrite)) {
	        try {
	            Files.createDirectories(directoryToWrite);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return;
	        }
	    }

	    String fileName = props.getProperty("fileName");
	    props.remove("fileName");
	    // Define the path to the output file
	    Path outputFile = directoryToWrite.resolve(fileName);
	    // Write the properties to the file
	    try (OutputStream output = Files.newOutputStream(outputFile)) {
	        props.store(output, "Filtered properties");
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
        }
    }

    public static void main(String[] args) {
    	
    	// Assuming the path to the server-config.properties file is passed as the first argument
        if (args.length < 1) {
            return;
        }
        String configFilePath = args[0];
        
        Properties config = new Properties();
        try {
            config.load(new FileInputStream(configFilePath));
        } catch (IOException e) {
            System.err.println("Failed to load configuration file.");
            e.printStackTrace();
            return;
        }
        
        String directoryToWrite = config.getProperty("directory.to.write");
        int port = Integer.parseInt(config.getProperty("server.port", "8080")); // Default to 8080 if not specified
        
        PropertiesServer server = new PropertiesServer(port, directoryToWrite);
        server.startServer();
    }
}
