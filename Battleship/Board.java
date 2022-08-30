import java.util.Stack;

public class Board {
    // Member variales
    private int _size;
    private char[][] _board;

    // Default constructor
    public Board(int size) {
        this._size = size;
        this._board = new char[size][size];

        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                this._board[x][y] = '~';
            }
        }
    }

    // Method to print the board to the command line
    public void printBoard() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int x = 0; x < this._size; ++x) {
            switch (x) {
                case 0:
                    System.out.print("A ");
                    break;
                case 1:
                    System.out.print("B ");
                    break;
                case 2:
                    System.out.print("C ");
                    break;
                case 3:
                    System.out.print("D ");
                    break;
                case 4:
                    System.out.print("E ");
                    break;
                case 5:
                    System.out.print("F ");
                    break;
                case 6:
                    System.out.print("G ");
                    break;
                case 7:
                    System.out.print("H ");
                    break;
                case 8:
                    System.out.print("I ");
                    break;
                case 9:
                    System.out.print("J ");
                    break;
            }
            for (int y = 0; y < this._size; ++y) {
                System.out.print(this._board[x][y] + " ");
            }
            System.out.print("\n");
        }
    }

    // Method to determine the current position of the ship in the iteration
    private int[] deltaPos(Ship ship, int itr) {
        // Need to make a deep copy: https://www.geeksforgeeks.org/deep-shallow-lazy-copy-java-examples/
        int[] tmpPos = new int[ship.getPosition().length];
        for (int i = 0; i < ship.getPosition().length; ++i) {
            tmpPos[i] = ship.getPosition()[i];
        }

        // deltaPos[0] = row, deltaPos[1] = col
        switch (ship.getOrientation()) {
            case NORTH:
                tmpPos[0] -= itr;
                break;
            case SOUTH:
                tmpPos[0] += itr;
                break;
            case EAST:
                tmpPos[1] += itr;
                break;
            case WEST:
                tmpPos[1] -= itr;
                break;
        }

        return tmpPos;
    }

    // Method to place the ship on the board
    public void placeShip(Ship ship, int[] newPosition, Direction newOrientation) {
        // Track positions if an error is to occur
        int[] oldPosition = ship.getPosition();
        Direction oldOrientation = ship.getOrientation();
        Stack<int[]> oldPosStack = new Stack<int[]>();
        Stack<int[]> newPosStack = new Stack<int[]>();

        // Iterate through the old positions to temporarily store the values in case of need to revert
        for (int i = 0; i < ship.getLength(); ++i) {
            int[] deltaPos = this.deltaPos(ship, i);
            this._board[deltaPos[0]][deltaPos[1]] = '~';
            oldPosStack.push(deltaPos);
        }

        // Set the new position and orientation
        ship.setPosition(newPosition);
        ship.setOrientation(newOrientation);

        // Attempt to place the ship with the given position and orientation
        try {
            // Iterate through each position of the ship
            for (int i = 0; i < ship.getLength(); ++i) {
                int[] deltaPos = this.deltaPos(ship, i);

                // Check if a ship is already placed at deltaPos
                if (this._board[deltaPos[0]][deltaPos[1]] == 'S') {
                    throw new CollisionException("CollisionException", "Collision detected");
                }

                // Place the ship
                this._board[deltaPos[0]][deltaPos[1]] = 'S';
                newPosStack.push(deltaPos);
            }
        }
        // Catch placement errors if they should occur
        catch (Exception e) {
            if (e instanceof ArrayIndexOutOfBoundsException)
                System.out.println("\nShip out of bounds; reverting position\n\n");
            else if (e instanceof CollisionException)
                System.out.println("\nShip collides with other ship, reverting position\n\n");
            else
                System.out.println("\nUnkown error, reverting position\n\n");

            // Revert the position and orientation
            ship.setPosition(oldPosition);
            ship.setOrientation(oldOrientation);

            // Nullify the new positions
            while (!newPosStack.empty()) {
                int[] tmpPos = newPosStack.pop();
                this._board[tmpPos[0]][tmpPos[1]] = '~';
            }

            // Place back the old positions
            while (!oldPosStack.empty()) {
                int[] tmpPos = oldPosStack.pop();
                this._board[tmpPos[0]][tmpPos[1]] = 'S';
            }
        }
    }

    // Method to place a shot on the board
    public void placeShot(int[] position) {
        try {
            switch (this._board[position[0]][position[1]]) {
                case '~':
                    this._board[position[0]][position[1]] = 'M'; // Miss
                    break;
                case 'S':
                    this._board[position[0]][position[1]] = 'H'; // Hit
                    break;
                default:
                    System.out.println("Position hit previously");
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Shot out of bounds");
        }
    }
}
