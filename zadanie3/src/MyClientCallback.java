import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MyClientCallback extends Remote {
    // Called when a chat message is received
    void receiveMessage(String message) throws RemoteException;
}
