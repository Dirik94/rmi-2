import java.rmi.Naming;
import java.util.Scanner;

public class MyClientMain {
    public static void main(String[] args) {
        System.setProperty("java.security.policy", "C:\\Users\\Greg\\IdeaProjects\\rmi-2\\zadanie1\\server.policy");
        System.setSecurityManager(new SecurityManager());

        try {
            MyServerInt myRemoteObject = (MyServerInt) Naming.lookup("//localhost:1096/ABC");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Enter json key to look for user data: ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting...");
                    break;
                }

                String result = myRemoteObject.getDbData(input);
                System.out.println("Sent to server: " + input);
                System.out.println("Received response from server: " + result);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
