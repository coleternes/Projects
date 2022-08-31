public class Ship {
    // Member variables
    private int _length;
    private int[] _position;
    private Direction _orientation;
    private int _ship_number;
    private int _hits;

    // Default constructor
    public Ship() {
        this.setLength(-1);
        this.setPosition(new int[] {-1, -1});
        this.setOrientation(Direction.EMPTY);
        this.setShipNumber(-1);
        this.setHits(-1);
    }

    // Overloaded constructor
    public Ship(int length, Direction orientation, int[] position, int shipNumber) {
        this.setLength(length);
        this.setPosition(position);
        this.setOrientation(orientation);
        this.setShipNumber(shipNumber);
        this.setHits(0);
    }

    // Length mutator and accessor
    public int getLength() {
        return this._length;
    }
    public void setLength(int length) {
        this._length = length;
    }

    // Position mutator and accessor
    public int[] getPosition() {
        return this._position;
    }
    public void setPosition(int[] position) {
        this._position = position;
    }

    // Orientation mutator and accessor
    public Direction getOrientation() {
        return this._orientation;
    }
    public void setOrientation(Direction orientation) {
        this._orientation = orientation;
    }

    // Ship number mutator and accessor
    public int getShipNumber() {
        return this._ship_number;
    }
    public void setShipNumber(int shipNumber) {
        this._ship_number = shipNumber;
    }

    // Hits mutator and accessor
    public int getHits() {
        return this._hits;
    }
    public void setHits(int hits) {
        this._hits = hits;
    }

    // Increment hits
    public void incrementHits() {
        this._hits++;
    }

    // Check if the ship is sunk
    public boolean isSunk() {
        return this._hits == this._length;
    }
}
