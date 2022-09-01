import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

// Reference: https://www.geeksforgeeks.org/multithreaded-servers-in-java/
public class Server {
    public static void main(String[] argv) {
        // Define the server socket and players
        ServerSocket serverSocket = null;
        ClientHandler player1 = null;
        ClientHandler player2 = null;
        ClientHandler extra = null;
        boolean gameStarted = false;

        try {
            // Server is listening on port 6789
            serverSocket = new ServerSocket(6789);
            serverSocket.setReuseAddress(true);

            // Infinite loop to always listen for client requests
            while (true) {
                // Instantiate the client socket
                Socket clientSocket = serverSocket.accept();

                // Store the players
                if (player1 == null) {
                    // Instantiate player1
                    player1 = new ClientHandler(clientSocket, true);
                    System.out.println("Player1 joined");
                }
                else {
                    if (player2 == null) {
                        // Instantiate player2
                        player2 = new ClientHandler(clientSocket, false);
                        System.out.println("Player2 joined");

                        // Send the turn orders to initiate the game
                        player1.sendTurnOrder();
                        player2.sendTurnOrder();
                        gameStarted = true;
                    }
                    else {
                        // TODO: Send rejection message if game is running
                    }
                }

                // If the game has started, process the incoming and outgoing data
                if (gameStarted) {
                    // Store the game state of player1 and player2
                    boolean player1Running = true;
                    boolean player2Running = true;

                    // While loop to run until the game is over
                    while (player1Running && player2Running) {
                        // Check if it's player1's turn
                        if (player1._is_my_turn) {
                            // Grab player1's shot position
                            int row = Integer.parseInt(player1._client_receiver.readLine());
                            int col = Integer.parseInt(player1._client_receiver.readLine());
                            System.out.println("Player 1's Shot Position = (" + row + ", " + col + ")");

                            // Send the shot position to player2
                            player2._client_sender.writeBytes("" + row + '\n');
                            player2._client_sender.writeBytes("" + col + '\n');

                            // Grab the shot data from player2
                            String shotData = player2._client_receiver.readLine();
                            int shipNumber = Integer.parseInt(player2._client_receiver.readLine());
                            System.out.println("Player 1's Shot Data = " + shotData);

                            // Send the shot data to player1
                            player1._client_sender.writeBytes(shotData + '\n');
                            player1._client_sender.writeBytes("" + shipNumber + '\n');

                            if (shotData.toLowerCase().equals("miss")) {
                                // Inverse the player's turn order
                                player1._is_my_turn = false;
                                player2._is_my_turn = true;
                            }
                        }
                        // Otherwise, it is player2's turn
                        else {
                            // Grab player2's shot position
                            int row = Integer.parseInt(player2._client_receiver.readLine());
                            int col = Integer.parseInt(player2._client_receiver.readLine());
                            System.out.println("Player 2's Shot Position = (" + row + ", " + col + ")");

                            // Send the shot position to player1
                            player1._client_sender.writeBytes("" + row + '\n');
                            player1._client_sender.writeBytes("" + col + '\n');

                            // Grab the shot data from player1
                            String shotData = player1._client_receiver.readLine();
                            int shipNumber = Integer.parseInt(player1._client_receiver.readLine());
                            System.out.println("Player 2's Shot Data = " + shotData);

                            // Send the shot data to player2
                            player2._client_sender.writeBytes(shotData + '\n');
                            player2._client_sender.writeBytes("" + shipNumber + '\n');

                            if (shotData.toLowerCase().equals("miss")) {
                                // Inverse the player's turn order
                                player1._is_my_turn = true;
                                player2._is_my_turn = false;
                            }
                        }

                        // Update players game states
                        player1Running = player1._client_receiver.read() != 0;
                        player2Running = player2._client_receiver.read() != 0;
                    }

                    // At the end of the game, reset the players
                    player1 = null;
                    player2 = null;
                    gameStarted = false;
                    System.out.println("Game ended!");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class ClientHandler {
        // Member variables
        private final Socket _client_socket;
        public final BufferedReader _client_receiver;
        public final DataOutputStream _client_sender;
        public boolean _is_my_turn;

        // Default constructor
        public ClientHandler(Socket clientSocket, boolean isMyTurn) throws Exception {
            this._client_socket = clientSocket;
            this._client_receiver = new BufferedReader(new InputStreamReader(this._client_socket.getInputStream()));
            this._client_sender = new DataOutputStream(this._client_socket.getOutputStream());
            this._is_my_turn = isMyTurn;
        }

        // Method to determine turn order
        public void sendTurnOrder() throws Exception {
            this._client_sender.writeBoolean(this._is_my_turn);
        }
    }
}
