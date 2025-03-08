import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * Server application that hosts the RMI service.
 */
public class MyServerMain {
    private static final int PORT = 1096;
    private static final String RMI_URL = "//localhost:" + PORT + "/ABC";
    private static final String SECURITY_POLICY = "server.policy";

    public static void main(String[] args) {
        try {
            // Set security properties
            System.setProperty("java.security.policy", SECURITY_POLICY);
            System.setProperty("java.rmi.server.codebase",
                    "file:/c:/Users/Greg/IdeaProjects/rmi-2/new-rmi-2/rmi-2/zadanie3/bin");

            // Initialize security manager
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }

            // Create registry and register the server implementation
            LocateRegistry.createRegistry(PORT);
            MyServerImpl serverImpl = new MyServerImpl();
            Naming.rebind(RMI_URL, serverImpl);

            System.out.println("Server started on port " + PORT);
            System.out.println("Press Ctrl+C to exit");

            // Add a shutdown hook to clean up
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Server shutting down...");
            }));

        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
