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
        int gameNumber = 1;

        try {
            // Server is listening on port 6789
            serverSocket = new ServerSocket(6789);
            serverSocket.setReuseAddress(true);

            // Infinite loop to always listen for client requests
            while (true) {
                // Accept player1
                Socket clientSocket1 = serverSocket.accept();
                ClientHandler player1 = new ClientHandler(clientSocket1, true);
                System.out.println("[Server] Player1 joined");

                // Accept player2
                Socket clientSocket2 = serverSocket.accept();
                ClientHandler player2 = new ClientHandler(clientSocket2, false);
                System.out.println("[Server] Player2 joined");

                // Instantiate the new game and run it on a new thread
                System.out.println("[Server] Game " + gameNumber + " started");
                Game newGame = new Game(player1, player2, gameNumber);
                new Thread(newGame).start();

                // Increment the game gameNumber
                gameNumber++;
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

        /*
        // Copy constructor (enables deep copy)
        public ClientHandler(ClientHandler player) {
            this._client_socket = player._client_socket;
            this._client_receiver = player._client_receiver;
            this._client_sender = player._client_sender;
            this._is_my_turn = player._is_my_turn;
        }
        */

        // Method to determine turn order
        public void sendTurnOrder() throws Exception {
            this._client_sender.writeBoolean(this._is_my_turn);
        }
    }

    private static class Game implements Runnable {
        // Member variables
        private final ClientHandler _player1;
        private final ClientHandler _player2;
        private final int _game_number;
        private boolean _player1_running;
        private boolean _player2_running;

        public Game(ClientHandler player1, ClientHandler player2, int gameNumber) {
            this._player1 = player1;
            this._player2 = player2;
            this._game_number = gameNumber;
            this._player1_running = true;
            this._player2_running = true;
        }

        public void run() {
            try {
                // Send the turn orders to initiate the game
                this._player1.sendTurnOrder();
                this._player2.sendTurnOrder();

                // While loop to run until the game is over
                while (this._player1_running && this._player2_running) {
                    // Check if it's player1's turn
                    if (this._player1._is_my_turn) {
                        // Grab player1's shot position
                        int row = Integer.parseInt(this._player1._client_receiver.readLine());
                        int col = Integer.parseInt(this._player1._client_receiver.readLine());
                        System.out.println("[Game " + this._game_number + "] Player 1's Shot Position = (" + row + ", " + col + ")");

                        // Send the shot position to player2
                        this._player2._client_sender.writeBytes("" + row + '\n');
                        this._player2._client_sender.writeBytes("" + col + '\n');

                        // Grab the shot data from player2
                        String shotData = this._player2._client_receiver.readLine();
                        int shipNumber = Integer.parseInt(this._player2._client_receiver.readLine());
                        System.out.println("[Game " + this._game_number + "] Player 1's Shot Data = " + shotData);

                        // Send the shot data to player1
                        this._player1._client_sender.writeBytes(shotData + '\n');
                        this._player1._client_sender.writeBytes("" + shipNumber + '\n');

                        // Inverse the player's turn order only when the shot has missed
                        if (shotData.toLowerCase().equals("miss")) {
                            this._player1._is_my_turn = false;
                            this._player2._is_my_turn = true;
                        }
                    }
                    // Otherwise, it is player2's turn
                    else {
                        // Grab player2's shot position
                        int row = Integer.parseInt(this._player2._client_receiver.readLine());
                        int col = Integer.parseInt(this._player2._client_receiver.readLine());
                        System.out.println("[Game " + this._game_number + "] Player 2's Shot Position = (" + row + ", " + col + ")");

                        // Send the shot position to player1
                        this._player1._client_sender.writeBytes("" + row + '\n');
                        this._player1._client_sender.writeBytes("" + col + '\n');

                        // Grab the shot data from player1
                        String shotData = this._player1._client_receiver.readLine();
                        int shipNumber = Integer.parseInt(this._player1._client_receiver.readLine());
                        System.out.println("[Game " + this._game_number + "] Player 2's Shot Data = " + shotData);

                        // Send the shot data to player2
                        this._player2._client_sender.writeBytes(shotData + '\n');
                        this._player2._client_sender.writeBytes("" + shipNumber + '\n');

                        // Inverse the player's turn order only when the shot has missed
                        if (shotData.toLowerCase().equals("miss")) {
                            this._player1._is_my_turn = true;
                            this._player2._is_my_turn = false;
                        }
                    }

                    // Update players game states
                    this._player1_running = this._player1._client_receiver.read() != 0;
                    this._player2_running = this._player2._client_receiver.read() != 0;
                }

                System.out.println("[Server] Game " + this._game_number + " ended");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
