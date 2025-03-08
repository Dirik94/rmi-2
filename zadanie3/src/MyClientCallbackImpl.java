import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Implementation of the callback interface for client-side message reception.
 */
public class MyClientCallbackImpl extends UnicastRemoteObject implements MyClientCallback {
    private final String clientName;

    protected MyClientCallbackImpl(String clientName) throws RemoteException {
        super();
        this.clientName = clientName;
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        System.out.println(clientName + " received: " + message);
    }

    @Override
    public String toString() {
        return clientName;
    }
}
