import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DirectoryMonitorClient {
    public static void main(String[] args) {
   	
    	// Assuming the path to the client-config.properties file is passed as the first argument
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
        
        String directoryToMonitor = config.getProperty("directory.to.watch");
        String regexPattern = config.getProperty("regex.pattern");
        String serverAddress = config.getProperty("server.address");
        int serverPort = Integer.parseInt(config.getProperty("server.port", "8080")); // Default to 8080 if not specified
        
        // Instantiate your client with server connection details
        PropertiesClient propertiesClient = new PropertiesClient(serverAddress, serverPort);
        
        // Instantiate and start the directory watcher service in a new thread
        DirectoryWatcherService directoryWatcherService = new DirectoryWatcherService(directoryToMonitor, regexPattern, propertiesClient);
        Thread directoryWatcherThread = new Thread(directoryWatcherService);
        directoryWatcherThread.start();
    }
}