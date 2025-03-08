import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the server interface handling client communication and game
 * logic.
 */
public class MyServerImpl extends UnicastRemoteObject implements MyServerInt {
    // Single thread-safe collection for client management
    private final Map<MyClientCallback, Client> clientMap = new ConcurrentHashMap<>();

    // Game state
    private TicTacToe game = null;
    private Client playerX = null;
    private Client playerO = null;

    protected MyServerImpl() throws RemoteException {
        super();
    }

    @Override
    public void registerClient(MyClientCallback callback) throws RemoteException {
        // Get the client name from callback's toString() method
        // This works because MyClientCallbackImpl.toString() returns clientName
        String clientName;
        try {
            clientName = callback.toString();
            if (clientName == null || clientName.trim().isEmpty() ||
                    clientName.contains("Proxy") || clientName.contains("RemoteObjectInvocationHandler")) {
                // Fallback if toString doesn't return a proper name
                clientName = "Client" + (clientMap.size() + 1);
            }
        } catch (Exception e) {
            System.err.println("Error getting client name: " + e.getMessage());
            clientName = "Client" + (clientMap.size() + 1);
        }

        // Create a Client object to represent this client
        Client client = new Client(clientName, callback);

        // Store the client in our collection
        clientMap.put(callback, client);

        System.out.println("Client registered: " + client.getName());

        // Announce the new user to all clients
        broadcastToAllClients("User " + client.getName() + " has joined the chat");
    }

    @Override
    public void broadcastMessage(String message, MyClientCallback sender) throws RemoteException {
        Client client = getClientByCallback(sender);
        if (client == null) {
            System.err.println("Unknown client tried to send message: " + sender);
            return;
        }

        // Handle game commands
        if (message.startsWith("/g")) {
            handleGameCommand(message, client);
            return;
        }

        // Handle regular chat messages
        System.out.println("Broadcasting: " + message);
        broadcastToOtherClients(message, client);
    }

    /**
     * Get the Client object associated with a callback reference.
     */
    private Client getClientByCallback(MyClientCallback callback) {
        return clientMap.get(callback);
    }

    /**
     * Get all registered clients.
     */
    private Collection<Client> getAllClients() {
        return clientMap.values();
    }

    /**
     * Send a message to all clients except the sender.
     */
    private void broadcastToOtherClients(String message, Client sender) {
        for (Client client : getAllClients()) {
            if (!client.equals(sender)) {
                try {
                    client.getCallback().receiveMessage(message);
                } catch (RemoteException e) {
                    System.err.println("Error sending message to client: " + client.getName());
                    removeDisconnectedClient(client.getCallback());
                }
            }
        }
    }

    /**
     * Send a message to all clients including the sender.
     */
    private void broadcastToAllClients(String message) {
        for (Client client : getAllClients()) {
            try {
                client.getCallback().receiveMessage(message);
            } catch (RemoteException e) {
                System.err.println("Error sending message to client: " + client.getName());
                removeDisconnectedClient(client.getCallback());
            }
        }
    }

    /**
     * Remove a disconnected client from our collection
     */
    private void removeDisconnectedClient(MyClientCallback callback) {
        Client client = clientMap.remove(callback);
        if (client != null) {
            System.out.println("Removed disconnected client: " + client.getName());

            // Handle player disconnection from game
            if ((playerX != null && client.getName().equals(playerX.getName())) ||
                    (playerO != null && client.getName().equals(playerO.getName()))) {
                String message = "Player " + client.getName() + " disconnected. Game ended.";
                broadcastToAllClients(message);
                resetGame();
            }
        }
    }

    /**
     * Send a message to a specific client.
     */
    private void sendToClient(Client client, String message) {
        try {
            client.getCallback().receiveMessage(message);
        } catch (RemoteException e) {
            System.err.println("Error sending message to client: " + client.getName());
            removeDisconnectedClient(client.getCallback());
        }
    }

    /**
     * Handle game-related commands.
     */
    private void handleGameCommand(String message, Client sender) {
        String[] parts = message.split(" ");
        if (parts.length < 2) {
            sendToClient(sender, "Invalid game command");
            return;
        }

        String command = parts[1].toLowerCase();

        try {
            switch (command) {
                case "start":
                    handleStartGame(sender);
                    break;
                case "join":
                    handleJoinGame(sender);
                    break;
                case "move":
                    handleMakeMove(parts, sender);
                    break;
                default:
                    sendToClient(sender, "Unknown game command: " + command);
            }
        } catch (Exception e) {
            sendToClient(sender, "Error processing command: " + e.getMessage());
        }
    }

    private void handleStartGame(Client sender) {
        if (game == null) {
            game = new TicTacToe();
            playerX = sender;
            sendToClient(sender, "Tic Tac Toe game started. You are X. Waiting for opponent to join with '/g join'");
        } else {
            sendToClient(sender, "A game is already in progress.");
        }
    }

    private void handleJoinGame(Client sender) {
        if (game == null) {
            sendToClient(sender, "No game has been started. Use '/g start' to create a game.");
            return;
        }

        if (playerO != null) {
            sendToClient(sender, "Game already has two players.");
            return;
        }

        if (playerX != null && sender.getName().equals(playerX.getName())) {
            sendToClient(sender, "You started the game. Waiting for an opponent to join.");
            return;
        }

        // Join as player O
        playerO = sender;

        // Use client names directly, never client objects
        String gameStartMessage = String.format(
                "Player %s joined as O. Game starting: %s (X) vs %s (O). %s to move.",
                playerO.getName(), playerX.getName(), playerO.getName(), playerX.getName());

        System.out.println("Game start message: " + gameStartMessage);
        broadcastToAllClients(gameStartMessage);
    }

    private void handleMakeMove(String[] parts, Client sender) {
        if (game == null || playerO == null) {
            sendToClient(sender, "Game not ready. Start/join a game first.");
            return;
        }

        // Verify it's the player's turn
        char currentPlayerSymbol = game.getCurrentPlayer();
        Client expectedPlayer = (currentPlayerSymbol == 'X') ? playerX : playerO;

        if (!sender.getName().equals(expectedPlayer.getName())) {
            sendToClient(sender, "It's not your turn. Waiting for " + expectedPlayer.getName());
            return;
        }

        // Validate and process the move
        if (parts.length < 4) {
            sendToClient(sender, "Invalid move command. Usage: /g move row col");
            return;
        }

        try {
            int row = Integer.parseInt(parts[2]);
            int col = Integer.parseInt(parts[3]);

            if (!game.makeMove(row, col)) {
                sendToClient(sender, "Invalid move. Try again.");
                return;
            }

            // Show the updated board to all players
            String boardState = game.getBoardAsString();
            broadcastToAllClients(boardState);

            // Check for win or draw
            if (game.checkWin()) {
                broadcastToAllClients("Player " + sender.getName() + " wins!");
                resetGame();
            } else if (game.isBoardFull()) {
                broadcastToAllClients("Game is a draw!");
                resetGame();
            } else {
                // Continue to next player's turn
                game.switchPlayer();
                Client nextPlayer = (game.getCurrentPlayer() == 'X') ? playerX : playerO;
                broadcastToAllClients("Next turn: " + nextPlayer.getName());
            }
        } catch (NumberFormatException e) {
            sendToClient(sender, "Invalid row or column. Use numbers.");
        }
    }

    private void resetGame() {
        game = null;
        playerX = null;
        playerO = null;
        broadcastToAllClients("Game has been reset. Start a new game with '/g start'");
    }
}
