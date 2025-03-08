import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class MyServerMain {
    public static void main(String[] args) {
        try {
            // Set all properties first
            System.setProperty("java.security.policy", "/home/onetwothree/rmi2/rmi-2/zadanie2/server.policy");
            System.setProperty("java.rmi.server.codebase",
                    "file:///home/onetwothree/rmi2/rmi-2/zadanie2/bin");
            System.out.println("Codebase: " + System.getProperty("java.rmi.server.codebase"));
            System.setProperty("java.rmi.server.hostname", "192.168.17.128");

            // Then enable the security manager
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }

            // Create registry and register objects
            LocateRegistry.createRegistry(1096);
            MyServerImpl obj1 = new MyServerImpl();
            Naming.rebind("//192.168.17.128:1096/ABC", obj1);
            System.out.println("Serwer oczekuje ...");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
