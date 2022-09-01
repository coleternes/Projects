import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    // Member variables
    public boolean _running;
    public boolean _victory;
    private BufferedReader _user_input;
    private Board _ship_board;
    private Board _shot_board;
    private List<Ship> _ships;
    private List<int[]> _shots;
    private Helper _helper;
    private int _enemy_ship_counter;

    // Default constructor
    public Player() {
        // Instantiate the default game states
        this._running = true;
        this._victory = false;

        // Instantiate the buffered reader
        this._user_input = new BufferedReader(new InputStreamReader(System.in));

        // Instantiate empty boards
        this._ship_board = new Board(10);
        this._shot_board = new Board(10);

        // Instantiate 4 ships at random positions
        this._ships = new ArrayList<Ship>();
        this._ships.add(new Ship(2, Direction.SOUTH, new int[] {0, 0}, 1));
        this._ships.add(new Ship(2, Direction.SOUTH, new int[] {0, 1}, 2));
        this._ships.add(new Ship(3, Direction.SOUTH, new int[] {0, 2}, 3));
        this._ships.add(new Ship(4, Direction.SOUTH, new int[] {0, 3}, 4));
        // Temporarily place the ships
        for (Ship ship : this._ships) {
            this._ship_board.placeShip(ship, ship.getPosition(), ship.getOrientation(), false);
        }
        this.randomizeBoard();

        // Instantiate the shots list
        this._shots = new ArrayList<int[]>();

        // Instantiate the helper object
        this._helper = new Helper();

        // Instantiate the number of enemy ships
        this._enemy_ship_counter = 4;
    }

    // Method that randomizes the ship's positions
    private void randomizeBoard() {
        // Place each ship in the list on the board
        for (Ship ship : this._ships) {
            // Continue to randomize the ships position and orientation
            // until the ship can be placed
            while (true) {
                Random rand = new Random();
                int[] randomPosition = new int[2];
                Direction randomOrientation;

                // Random position
                randomPosition[0] = rand.nextInt(10);
                randomPosition[1] = rand.nextInt(10);

                // Random orientation
                float prob = rand.nextFloat();
                if (prob < 0.25)
                    randomOrientation = Direction.NORTH;
                else if (prob < 0.5)
                    randomOrientation = Direction.SOUTH;
                else if (prob < 0.75)
                    randomOrientation = Direction.EAST;
                else
                    randomOrientation = Direction.WEST;

                if (this._ship_board.placeShip(ship, randomPosition, randomOrientation, false))
                    break;
            }
        }
    }

    // Method that setups the player's board at the start of the game
    public void setupBoard() throws Exception {
        String menuInput;
        Ship currShip = new Ship();
        boolean validShip;
        int[] position;
        Direction orientation;

        System.out.println("BOARD SETUP");
        while (true) {
            // Reset the boolean
            validShip = false;

            // Print options
            this._ship_board.printBoard();
            System.out.println("Please select an option:\nR - Ready Up\n1 - Move Ship 1\n2 - Move Ship 2\n3 - Move Ship 3\n4 - Move Ship 4");
            menuInput = this._user_input.readLine();

            // Check menuInput for ready up
            if (menuInput.toLowerCase().equals("r")) {
                break;
            }
            // Otherwise, check menuInput for ship index
            else {
                try {
                    currShip = this._ships.get(this._helper.inputToShip(menuInput));
                    validShip = true;
                }
                catch (Exception e) {
                    System.out.println("\nCommand not recognized\n\n");
                }
            }

            // If ship is valid, continue to position and orientation
            if (validShip) {
                System.out.println("\nEnter the position:");
                position = this._helper.inputToPosition(this._user_input.readLine());
                System.out.println("\nEnter the orientation:");
                orientation = this._helper.inputToOrientation(this._user_input.readLine());

                // Place the ship on the board
                this._ship_board.placeShip(currShip, position, orientation, true);
            }
        }
    }

    // Method that validates the player's shot position input and returns it
    public int[] fireShot() throws Exception {
        System.out.println("\nEnter the shot position:");
        int[] shotPosition = this._helper.inputToPosition(this._user_input.readLine());
        return shotPosition;
    }

    // Method that takes the shot position and data to update the shot board
    public void updateShotBoard(int[] shotPosition, String shotData) {
        // Update the shot board's information
        this._shot_board.placeShot(shotPosition, shotData);

        // If a ship was sunk, subtract from the enemy ship counter
        if (shotData.toLowerCase().equals("sunk")) {
            this._enemy_ship_counter -= 1;
        }

        // If the ship enemy counter is 0 or less, the game is over and the player has won
        if (this._enemy_ship_counter <= 0) {
            this._running = false;
            this._victory = true;
        }
    }

    // Method that takes the shot position and determines the shot data
    public String updateShipBoard(int[] shotPosition) {
        // Define the shot data value
        String shotData;
        int shipNumber = this._ship_board.isHit(shotPosition);

        // Check if the shot is a miss
        if (shipNumber == -1)
            shotData = "miss";
        // Otherwise, the shot is a hit
        else {
            // Get the ship
            Ship tmpShip = null;
            for (Ship ship : this._ships) {
                if (ship.getShipNumber() == shipNumber) {
                    tmpShip = ship;
                    break;
                }
            }

            // Increment the hits on the ship
            tmpShip.incrementHits();

            // Determine if the hit also sunk the ship
            if (tmpShip.isSunk()) {
                // Set shotData to sunk
                shotData = "sunk";

                // Remove tmpShip from this._ships
                this._ships.remove(tmpShip);

                // Check if the game is over
                if (this._ships.isEmpty()) {
                    this._running = false;
                    this._victory = false;
                }
            }
            else {
                // Set shotData to hit
                shotData = "hit";
            }
        }

        // Update the ship board's information
        this._ship_board.placeShot(shotPosition, shotData);

        return shotData;
    }

    // Method to print both boards
    public void printBoards() {
        this._ship_board.printBoard();
        this._shot_board.printBoard();
    }

    // Method to convert position to board
    public String positionToInput(int row, int col) {
        return this._helper.positionToInput(row, col);
    }

    // Method to grab ship number
    public int getShipAtPosition(int[] shotPosition) {
        return this._ship_board.isHit(shotPosition);
    }
}
