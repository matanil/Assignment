import java.nio.file.*;
import java.util.List;

public class DirectoryWatcherService implements Runnable {
    private final Path pathToWatch;
    private final String regexFilter;
    private final PropertiesClient client;

    public DirectoryWatcherService(String path, String regexFilter, PropertiesClient client) {
        this.pathToWatch = Paths.get(path);
        this.regexFilter = regexFilter;
        this.client = client;
    }

    @Override
    public void run() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            pathToWatch.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            while (!Thread.currentThread().isInterrupted()) {
                WatchKey key = watchService.take();
                List<WatchEvent<?>> events = key.pollEvents();
               
                for (WatchEvent<?> event : events) {
                    Path fileName = (Path) event.context();
                    Path filePath = pathToWatch.resolve(fileName);

                    // Process the file if it's a properties file
                    if (filePath.toString().endsWith(".properties")) {
                        client.processPropertiesFile(filePath, regexFilter);
                    }
                }
                key.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
