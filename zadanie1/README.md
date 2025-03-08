# Building and Running the RMI Application on Java 8

## Prerequisites

- Ensure Java 8 is installed (`java -version`)
- Ensure Maven is installed and added to your PATH (`mvn -version`)

### Installing Maven using Command Prompt

If Maven is not installed, you can use one of these methods:

1. Using Chocolatey (if Chocolatey is installed):
   ```
   choco install maven
   ```
2. Manual installation:
   - Download Maven from https://maven.apache.org/download.cgi.
   - Extract the archive to a folder (e.g., C:\Program Files\Apache\maven).
   - Set the M2_HOME environment variable to that folder.
   - Add the Maven "bin" directory (e.g., C:\Program Files\Apache\maven\bin) to your PATH.

## Build Instructions

1. Open a command prompt in the project root directory:
   ```
   cd C:\Users\Greg\IdeaProjects\rmi-2
   ```
2. Build the project using Maven (this will download required dependencies):
   ```
   mvn clean package
   ```

## Manual Compilation Using javac

If you prefer to compile manually instead of using Maven, run the following commands from the source directory:

1. Open a command prompt and navigate to the resources folder:
   ```
   cd C:\Users\Greg\IdeaProjects\rmi-2\zadanie1\src\main\resources
   ```
2. Compile the Java files by including the JSON jar using the correct relative path:
   ```
   javac -source 1.8 -target 1.8 -cp "..\..\..\target\rmi-2\WEB-INF\lib\json-20210307.jar;." -d C:\Users\Greg\IdeaProjects\rmi-2\zadanie1\bin *.java
   ```

## Running the Server and Client

### Start the Server

1. In the project directory, run:

   cd C:\Users\Greg\IdeaProjects\rmi-2\zadanie1

   ```
   java "-Djava.security.policy=src\security.policy" -cp "bin;target\rmi-2\WEB-INF\lib\json-20210307.jar" MyServerMain
   ```

2. You should see a message indicating the server is waiting for client connections.

### Run the Client

1. Open a new command prompt in the same project directory.
2. Run:

   cd C:\Users\Greg\IdeaProjects\rmi-2\zadanie1

   ```
   java "-Djava.security.policy=src\security.policy" -cp bin MyClientMain
   ```

3. Check the console output on both ends for the request and response messages.
