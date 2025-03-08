import java.rmi.Naming;
import java.util.Scanner;

public class MyClientMain2 {
    public static void main(String[] args) {
        System.setProperty("java.security.policy",
                "C:\\Users\\Greg\\IdeaProjects\\rmi-2\\new-rmi-2\\rmi-2\\zadanie3\\server.policy");
        System.setSecurityManager(new SecurityManager());

        try {
            MyServerInt myRemoteObject = (MyServerInt) Naming.lookup("//localhost:1096/ABC");
            MyClientCallbackImpl callback = new MyClientCallbackImpl("Client2");
            myRemoteObject.registerClient(callback);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Client2 Chat started. Type messages, or use commands:");
            System.out.println(" '/g start' -> start a Tic Tac Toe game");
            System.out.println(" '/g join'  -> join an ongoing game");
            System.out.println(" '/g move row col' -> make a move");
            System.out.println("Enter 'exit' to quit:");
            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                if ("exit".equalsIgnoreCase(message))
                    break;
                if (message.trim().startsWith("/g"))
                    myRemoteObject.broadcastMessage(message, callback);
                else
                    myRemoteObject.broadcastMessage("Client2: " + message, callback);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
