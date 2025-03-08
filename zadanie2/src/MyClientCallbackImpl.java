import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MyClientCallbackImpl extends UnicastRemoteObject implements MyClientCallback {
    String clientName;

    protected MyClientCallbackImpl(String clientName) throws RemoteException {
        super();
        this.clientName = clientName;
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        System.out.println(clientName + " received: " + message);
    }
}
