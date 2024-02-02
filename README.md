# Assignment
This application consists of two main components: a Client and a Server. The Client monitors a specified directory for new Java properties files. Upon detecting a new file, it filters the file's contents based on a configurable regex pattern for keys, sends the filtered properties to the Server, and then deletes the file. The Server receives filtered properties from the Client, reconstructs the properties file, and saves it to a specified directory.



# Getting Started
------------------------
**Prerequisites**

- Java JDK 11 or later
- IDE of your choice (optional) / command prompt
- input properties file with the property names matching the regEx "spring\\.[^\\.]+\\.[^\\.]+".

**Executing application**
- compile below java classes using the below commands 

  javac DirectoryMonitorClient.java
  javac PropertiesClient.java
  javac DirectoryWatcherService.java
  javac PropertiesServer.java


-> Before running the applications, configure both the Client and Server using their respective configuration files.
  - client-config.properties
  - server-config.properties


- Run client and server programs in 2 different command prompts
- Start client
	java DirectoryMonitorClient client-config.properties
- Start server
	java PropertiesServer server-config.properties


**Testing the Application**

To test the application, place a .properties file in the directory being monitored by the Client. If configured correctly, the Client will filter the file and send it to the Server, which will then write the filtered properties to the specified output directory.

- Regular Expression I have used to match the properties from the file is "spring\\.[^\\.]+\\.[^\\.]+".
- Make sure to have property names matching with the regular expression mentioned in the config files.

**Troubleshooting**

- Ensure both Client and Server configurations point to accessible directories and the Server's listening port is open.
- Verify the regex pattern matches the desired keys in the properties files.
- Check for any firewall or network issues that might prevent the Client and Server from communicating.


