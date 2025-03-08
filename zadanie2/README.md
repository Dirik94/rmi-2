# Building and Running the RMI Application on Java 8

## Prerequisites

- Ensure Java 8 is installed (`java -version`)

## Build Instructions

1. Open a command prompt in the project directory:
   ```
   cd C:\Users\Greg\IdeaProjects\rmi-2\zadanie2
   ```
2. Create a `bin` directory (if it does not exist):
   ```
   mkdir bin
   ```
3. Compile the Java source files:
   ```
   javac -source 1.8 -target 1.8 -d bin src/*.java //linux
   ```

## Running the Server and Client

### Start the Server

1. In the project directory, run:
   ```
   java -cp bin -Djava.security.policy=src/security.policy MyServerMain //linux
   ```
2. You should see a message indicating the server is waiting for client connections.

### Run the Client

1. Open a new command prompt in the same project directory.
2. Run:
   ```
   java -cp bin "-Djava.security.policy=src\security.policy" MyClientMain
   java -cp bin -Djava.security.policy=src/security.policy MyClientMain2 //linux
   ```
3. Check the console output on both ends for the request and response messages.
