import java.io.Serializable;

/**
 * Represents a client in the chat and game system.
 * This class separates the client identity from its RMI implementation.
 */
public class Client implements Serializable {
    private final String name;
    private final transient MyClientCallback callback;

    public Client(String name, MyClientCallback callback) {
        this.name = name;
        this.callback = callback;
    }

    public String getName() {
        return name;
    }

    public MyClientCallback getCallback() {
        return callback;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Client other = (Client) obj;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
