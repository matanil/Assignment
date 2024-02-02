import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class PropertiesClient {
    private String serverAddress;
    private int serverPort;

    public PropertiesClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void processPropertiesFile(Path filePath, String regexFilter) {
        Properties props = new Properties();
        try (InputStream input = Files.newInputStream(filePath)) {
            props.load(input);
            // Filter properties based on regex
            Pattern pattern = Pattern.compile(regexFilter);
            Properties filteredProps = new Properties();
            filteredProps.setProperty("fileName", filePath.getFileName().toString());
            for (String propName : props.stringPropertyNames()) {
                if (pattern.matcher(propName).matches()) {
                    filteredProps.setProperty(propName, props.getProperty(propName));
                }
            }
            
            // Send filtered properties to server
            sendPropertiesToServer(filteredProps);
            // Delete the file
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendPropertiesToServer(Properties props) {
        // Implement socket communication to send properties to the server

		 try (Socket socket = new Socket(serverAddress, serverPort);
	         ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {

	        // Send properties object
	        objectOutputStream.writeObject(props);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    }
}