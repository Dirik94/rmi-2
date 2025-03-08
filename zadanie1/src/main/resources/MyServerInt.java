import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MyServerInt extends Remote {
    String getDbData(String text) throws RemoteException;
}
