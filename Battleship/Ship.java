public class Ship {
    // Member variables
    private int _length;
    private Direction _orientation;
    private int[] _position;

    // Default constructor
    public Ship(int length, Direction orientation, int[] position) {
        this._length = length;
        this._orientation = orientation;
        this._position = position;
    }

    // Length mutator and accessor
    public int getLength() {
        return this._length;
    }
    public void setLength(int length) {
        this._length = length;
    }

    // Orientation mutator and accessor
    public Direction getOrientation() {
        return this._orientation;
    }
    public void setOrientation(Direction orientation) {
        this._orientation = orientation;
    }

    // Position mutator and accessor
    public int[] getPosition() {
        return this._position;
    }
    public void setPosition(int[] position) {
        this._position = position;
    }
}
