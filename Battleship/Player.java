import java.util.ArrayList;
import java.util.List;

public class Player {
    // Member variables
    private boolean _running;
    private boolean _victory;
    private Board _ship_board;
    private Board _shot_board;
    private List<Ship> _ships;
    private List<int[]> _shots;

    // Default constructor
    public Player() {
        // Instantiate the default game states
        this._running = true;
        this._victory = false;

        // Instantiate empty boards
        this._ship_board = new Board(10);
        this._shot_board = new Board(10);

        // Instantiate 4 ships at default positions
        this._ships = new ArrayList<Ship>();
        this._ships.add(new Ship(2, Direction.SOUTH, new int[] {0, 0}));
        this._ships.add(new Ship(2, Direction.SOUTH, new int[] {0, 1}));
        this._ships.add(new Ship(3, Direction.SOUTH, new int[] {0, 2}));
        this._ships.add(new Ship(4, Direction.SOUTH, new int[] {0, 3}));

        // Place each ship in the list on the board
        for (Ship ship : this._ships) {
            this._ship_board.placeShip(ship, ship.getPosition());
        }

        // Instantiate the shots list
        this._shots = new ArrayList<int[]>();
    }

    // tmp
    public void printBoard() {
        this._ship_board.printBoard();
    }

    /*
    // Method that will setup the player's board at the start of the game
    public void setupBoard() {
        //
    }

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
