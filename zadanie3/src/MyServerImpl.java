import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyServerImpl extends UnicastRemoteObject implements MyServerInt {
    List<MyClientCallback> clients = new CopyOnWriteArrayList<>();

    protected MyServerImpl() throws RemoteException {
        super();
    }

    @Override
    public void registerClient(MyClientCallback client) throws RemoteException {
        clients.add(client);
        System.out.println("Client registered: " + client);
    }

    @Override
    public void broadcastMessage(String message, MyClientCallback sender) throws RemoteException {
        System.out.println("Broadcasting message: " + message); // log messages by server
        for (MyClientCallback client : clients) {
            if (!client.equals(sender)) {
                try {
                    client.receiveMessage(message); // send message to all clients except the sender(there is only one
                                                    // other client in this case)
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
