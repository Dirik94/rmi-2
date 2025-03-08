import java.rmi.Naming;
import java.util.Scanner;

public class MyClientMain2 {
    public static void main(String[] args) {
        System.setProperty("java.security.policy", "/home/onetwothree/rmi2/rmi-2/zadanie2/server.policy");
        System.setSecurityManager(new SecurityManager());

        try {
            MyServerInt myRemoteObject = (MyServerInt) Naming.lookup("//192.168.17.128:1096/ABC");
            MyClientCallbackImpl callback = new MyClientCallbackImpl("Client2");
            myRemoteObject.registerClient(callback);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Client2 Chat started. Type messages (enter 'exit' to quit):");
            while (true) {
                String message = scanner.nextLine();
                if ("exit".equalsIgnoreCase(message))
                    break;
                myRemoteObject.broadcastMessage("Client2: " + message, callback);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
