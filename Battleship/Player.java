import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    // Member variables
    private boolean _running;
    private boolean _victory;
    private Board _ship_board;
    private Board _shot_board;
    private List<Ship> _ships;
    private List<int[]> _shots;
    private Helper _helper;

    // Default constructor
    public Player() {
        // Instantiate the default game states
        this._running = true;
        this._victory = false;

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
    }

    // Method that randomizes the ship's positions
    public void randomizeBoard() {
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

        // User input buffered reader
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("BOARD SETUP");
        while (true) {
            // Reset the boolean
            validShip = false;

            // Print options
            this._ship_board.printBoard();
            System.out.println("Please select an option:\nR - Ready Up\n1 - Move Ship 1\n2 - Move Ship 2\n3 - Move Ship 3\n4 - Move Ship 4");
            menuInput = userInput.readLine();

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
                position = this._helper.inputToPosition(userInput.readLine());
                System.out.println("\nEnter the orientation:");
                orientation = this._helper.inputToOrientation(userInput.readLine());

                // Place the ship on the board
                this._ship_board.placeShip(currShip, position, orientation, true);
            }
        }
    }

    /*
    // Method that validates the player's input and sends the message to the server
    public String inputShot() {
        //
    }

    // Method that fires a shot at the enemy player's board
    public void fireShot(int[] position) {
        //
    }

    // Method to adjust the game's state after each round
    public boolean determineGameState() {
        //
    }
    */
}
