import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class MyServerImpl extends UnicastRemoteObject implements MyServerInt {
    List<MyClientCallback> clients = new CopyOnWriteArrayList<>();
    private TicTacToe game = null;
    private String playerX = null;
    private String playerO = null;
    // Added map to store real client names.
    private ConcurrentHashMap<MyClientCallback, String> clientNames = new ConcurrentHashMap<>();

    protected MyServerImpl() throws RemoteException {
        super();
    }

    @Override
    public void registerClient(MyClientCallback client) throws RemoteException {
        clients.add(client);
        // Store the client's real name from MyClientCallbackImpl if possible.
        if (client instanceof MyClientCallbackImpl) {
            clientNames.put(client, ((MyClientCallbackImpl) client).clientName);
        } else {
            clientNames.put(client, client.toString());
        }
        System.out.println("Client registered: " + clientNames.get(client));
    }

    @Override
    public void broadcastMessage(String message, MyClientCallback sender) throws RemoteException {
        // Handle Tic Tac Toe commands
        if (message.startsWith("/g")) {
            String[] parts = message.split(" ");
            try {
                if (parts[1].equalsIgnoreCase("start")) {
                    if (game == null) {
                        game = new TicTacToe();
                        String senderName = getClientName(sender);
                        playerX = senderName;
                        sender.receiveMessage(
                                "Tic Tac Toe game started. You are X. Waiting for opponent to join with '/g join'");
                    } else {
                        sender.receiveMessage("A game is already in progress.");
                    }
                } else if (parts[1].equalsIgnoreCase("join")) {
                    if (game != null && playerO == null) {
                        String senderName = getClientName(sender);
                        // Changed checks to provide a status update instead of an error
                        if (senderName.equals(playerX)) {
                            sender.receiveMessage(
                                    "You started the game. Waiting for an opponent to join with '/g join'.");
                        } else if (senderName.equals(playerO)) {
                            sender.receiveMessage(
                                    "You already joined the game. Game starting: " + "PlayerX" + " (X) vs "
                                            + "playerO" + " (O). " + "playerX" + " to move.");
                        } else {
                            playerO = senderName;
                            for (MyClientCallback client : clients) {
                                client.receiveMessage("Player " + "playerO" + " joined. Game starting: " +
                                        "PlayerX" + " (X) vs " + "playerO" + " (O). " + "PlayerX" + " to move.");
                            }
                        }
                    } else {
                        sender.receiveMessage("No game to join or game already has two players.");
                    }
                } else if (parts[1].equalsIgnoreCase("move")) {
                    if (game == null || playerO == null) {
                        sender.receiveMessage("Game not ready. Start/join a game first.");
                    } else {
                        String senderName = getClientName(sender);
                        char expected = game.getCurrentPlayer();
                        String expectedPlayer = (expected == 'X') ? playerX : playerO;
                        if (!senderName.equals(expectedPlayer)) {
                            expectedPlayer = (expected == 'X') ? "playerX" : "playerO";
                            sender.receiveMessage("It's not your turn. Waiting for " + expectedPlayer);
                        } else {
                            if (parts.length < 4) {
                                sender.receiveMessage("Invalid move command. Usage: /g move row col");
                            } else {
                                int row = Integer.parseInt(parts[2]);
                                int col = Integer.parseInt(parts[3]);
                                if (!game.makeMove(row, col)) {
                                    sender.receiveMessage("Invalid move. Try again.");
                                } else {
                                    String boardStr = game.getBoardAsString();
                                    for (MyClientCallback client : clients) {
                                        client.receiveMessage(boardStr);
                                    }
                                    if (game.checkWin()) {
                                        for (MyClientCallback client : clients) {
                                            expectedPlayer = (expected == 'X') ? "playerX" : "playerO";
                                            client.receiveMessage("Player " + expectedPlayer + " wins!");
                                        }
                                        game = null;
                                        playerX = null;
                                        playerO = null;
                                    } else if (game.isBoardFull()) {
                                        for (MyClientCallback client : clients) {
                                            client.receiveMessage("Game is a draw!");
                                        }
                                        game = null;
                                        playerX = null;
                                        playerO = null;
                                    } else {
                                        game.switchPlayer();
                                        char next = game.getCurrentPlayer();
                                        String nextPlayer = (next == 'X') ? "playerX" : "playerO";
                                        for (MyClientCallback client : clients) {
                                            client.receiveMessage("Next turn: " + nextPlayer);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    sender.receiveMessage("Unknown Tic Tac Toe command.");
                }
            } catch (Exception e) {
                sender.receiveMessage("Error processing command: " + e.getMessage());
            }
            return;
        }

        // Normal broadcast code
        System.out.println("Broadcasting message: " + message);
        for (MyClientCallback client : clients) {
            if (!client.equals(sender)) {
                try {
                    client.receiveMessage(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Helper method to extract a client name based on our stored mapping.
    private String getClientName(MyClientCallback client) {
        String name = clientNames.get(client);
        return (name != null) ? name : client.toString();
    }
}
