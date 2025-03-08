import java.rmi.Naming;
import java.util.Scanner;

public class MyClientMain {
    public static void main(String[] args) {
        System.setProperty("java.security.policy", "C:\\Users\\Greg\\IdeaProjects\\rmi-2\\zadanie2\\server.policy");
        System.setSecurityManager(new SecurityManager());

        try {
            MyServerInt myRemoteObject = (MyServerInt) Naming.lookup("//localhost:1096/ABC");
            MyClientCallbackImpl callback = new MyClientCallbackImpl("Client1");
            myRemoteObject.registerClient(callback);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Client1 Chat started. Type messages (enter 'exit' to quit):");
            while (true) {
                String message = scanner.nextLine();
                if ("exit".equalsIgnoreCase(message))
                    break;
                myRemoteObject.broadcastMessage("Client1: " + message, callback);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
