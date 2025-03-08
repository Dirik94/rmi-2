import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import org.json.JSONObject;

public class MyServerImpl extends UnicastRemoteObject implements MyServerInt {

    protected MyServerImpl() throws RemoteException {
        super();
    }

    @Override
    public String getDbData(String key) throws RemoteException {
        try {
            Database db = new Database();
            return db.get(key);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
