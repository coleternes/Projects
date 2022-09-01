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
        String shotData;

        // Main game loop
        while (p._running) {
            // If it's my turn, shoot at a position and send it to the server
            if (isMyTurn) {
                // Determine the player's shot position
                int[] shotPosition = p.fireShot();

                // Send the position to the server
                serverSender.writeBytes("" + shotPosition[0] + '\n');
                serverSender.writeBytes("" + shotPosition[1] + '\n');

                // When ready, receive the shot data
                shotData = serverReceiver.readLine();
                int shipNumber = Integer.parseInt(serverReceiver.readLine());
                System.out.print("\n");
                if (shotData.toLowerCase().equals("sunk"))
                    System.out.print("SHIP " + shipNumber + " HAS ");
                System.out.print(shotData.toUpperCase() + "\n");

                // Update the player's _shot_board
                p.updateShotBoard(shotPosition, shotData);
            }
            // Otherwise, wait to receive the eneimies shot
            else {
                System.out.println("\nWaiting for enemy's shot...");

                // When ready, receive the enemy's shot
                int row = Integer.parseInt(serverReceiver.readLine());
                int col = Integer.parseInt(serverReceiver.readLine());
                System.out.println("\nEnemy shot at " + p.positionToInput(row, col));
                int[] shotPosition = new int[] {row, col};

                // Determine the shot data based on the position
                int shipNumber = p.getShipAtPosition(shotPosition);
                shotData = p.updateShipBoard(shotPosition);
                System.out.print("\n");
                if (shotData.toLowerCase().equals("sunk"))
                    System.out.print("SHIP " + shipNumber + " HAS ");
                System.out.print(shotData.toUpperCase() + "\n");

                // Send the shot data and ship number to the server
                serverSender.writeBytes(shotData + '\n');
                serverSender.writeBytes("" + shipNumber + '\n');
            }

            // Send the server the game status
            serverSender.writeBoolean(p._running);

            // Print both boards to the screen
            p.printBoards();

            // Inverse isMyTurn at the end of each round if the shot is a miss
            if (shotData.toLowerCase().equals("miss"))
                isMyTurn = !isMyTurn;
        }

        // Determine the victor
        if (p._victory)
            System.out.println("\n\nYou win!\n");
        else
            System.out.println("\n\nYou lost!\n");

        // Close the socket after game completion
        clientSocket.close();
    }
}
