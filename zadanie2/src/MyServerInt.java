import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MyServerInt extends Remote {

    void registerClient(MyClientCallback client) throws RemoteException;

    void broadcastMessage(String message, MyClientCallback sender) throws RemoteException;
}
