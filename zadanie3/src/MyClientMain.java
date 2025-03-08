import java.rmi.Naming;
import java.util.Scanner;

/**
 * Client application for the first player (X).
 */
public class MyClientMain {
    private static final String SECURITY_POLICY = "server.policy";
    private static final String CLIENT_NAME = "Client1";
    private static final String RMI_URL = "//localhost:1096/ABC";

    public static void main(String[] args) {
        System.setProperty("java.security.policy", SECURITY_POLICY);
        System.setSecurityManager(new SecurityManager());

        try {
            runClient();
        } catch (Exception e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runClient() throws Exception {
        MyServerInt server = (MyServerInt) Naming.lookup(RMI_URL);
        MyClientCallbackImpl callback = new MyClientCallbackImpl(CLIENT_NAME);
        server.registerClient(callback);

        printInstructions();

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                if ("exit".equalsIgnoreCase(message))
                    break;

                // Send command without client prefix if game command
                if (message.trim().startsWith("/g"))
                    server.broadcastMessage(message, callback);
                else
                    server.broadcastMessage(CLIENT_NAME + ": " + message, callback);
            }
        }
    }

    private static void printInstructions() {
        System.out.println(CLIENT_NAME + " Chat started. Type messages, or use commands:");
        System.out.println(" '/g start' -> start a Tic Tac Toe game");
        System.out.println(" '/g join'  -> join an ongoing game");
        System.out.println(" '/g move row col' -> make a move (e.g., /g move 0 0)");
        System.out.println("Enter 'exit' to quit");
    }
}
