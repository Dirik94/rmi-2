import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class MyServerMain {
    public static void main(String[] args) {
        try {
            // Set all properties first
            System.setProperty("java.security.policy",
                    "C:\\Users\\Greg\\IdeaProjects\\rmi-2\\new-rmi-2\\rmi-2\\zadanie3\\server.policy");
            System.setProperty("java.rmi.server.codebase",
                    "file:/c:/Users/Greg/IdeaProjects/rmi-2/new-rmi-2/rmi-2/zadanie3/bin");
            System.out.println("Codebase: " + System.getProperty("java.rmi.server.codebase"));

            // Then enable the security manager
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }

            // Create registry and register objects
            LocateRegistry.createRegistry(1096);
            MyServerImpl obj1 = new MyServerImpl();
            Naming.rebind("//localhost:1096/ABC", obj1);
            System.out.println("Serwer oczekuje ...");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
