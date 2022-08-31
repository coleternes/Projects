import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void main(String[] argv) throws Exception {
        // Instantiate the player object and setup the board
        Player p = new Player();
        p.setupBoard();

        // Attempt socket connection
        Socket clientSocket = null;
        try {
            clientSocket = new Socket("localhost", 6789);
        } catch (Exception e) {
            System.out.println("Failed to open socket connection");
            System.exit(0);
        }

        // Instantiate server input / outut streams
        BufferedReader serverReceiver =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        DataOutputStream serverSender = new DataOutputStream(clientSocket.getOutputStream());

        // Check if it's the client's turn to shoot
        // https://stackoverflow.com/questions/21365046/inputstream-read-read-wrong-boolean-value
        boolean isMyTurn = serverReceiver.read() != 0;
        System.out.println("isMyTurn = " + isMyTurn);

        // Main game loop
        while (p._running) {
            // TODO:
            // 1. Check if it's the client's turn to shoot
            // 2. Player 1 will be first to shoot...
            //    a. Player 1 inputs position
            //    b. Player 1 sends position to server
            //    c. Server sends position to Player 2
            //    d. Player 2 updates shot data via _ship_board
            //    e. Player 2 calculates _running & _victory
            //    f. Player 2 sends shot data to server
            //    g. Server sends shot data to Player 1
            //    h. Player 1 updates _ship_board
            //    i. Player 1 calculates _running & _victory
            // 3. Print the boards
            // 4. Inverse the isMyTurn value

            // If it's my turn, shoot at a position and send it to the server
            if (isMyTurn) {
                // Determine the player's shot position
                int[] shotPosition = p.fireShot();

                // Send the position to the server
                serverSender.writeBytes("" + shotPosition[0] + '\n');
                serverSender.writeBytes("" + shotPosition[1] + '\n');

                // When ready, receive the shot data
                String shotData = serverReceiver.readLine();
                System.out.println("Received shotdata = " + shotData);

                // Update the player's _shot_board
                p.updateShotBoard(shotPosition, shotData);
            }
            // Otherwise, wait to receive the eneimies shot
            else {
                System.out.println("\nWaiting for enemy's shot...");

                // When ready, receive the enemy's shot
                int row = Integer.parseInt(serverReceiver.readLine());
                int col = Integer.parseInt(serverReceiver.readLine());
                System.out.println("Received shotPosition: (" + row + ", " + col + ")");
                int[] shotPosition = new int[] {row, col};

                // Determine the shot data based on the position
                String shotData = p.updateShipBoard(shotPosition);

                // Send the shot data to the server
                serverSender.writeBytes(shotData + '\n');
            }

            // Send the server the game status
            serverSender.writeBoolean(p._running);

            // Print both boards to the screen
            p.printBoards();

            // Inverse isMyTurn at the end of each round
            isMyTurn = !isMyTurn;
        }

        // Determine the victor
        if (p._victory)
            System.out.println("You win!");
        else
            System.out.println("You lost!");

        // Close the socket after game completion
        clientSocket.close();
    }
}
