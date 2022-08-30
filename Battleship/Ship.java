public class Ship {
    // Member variables
    private int _length;
    private int[] _position;
    private Direction _orientation;

    // Default constructor
    public Ship() {
        this.setLength(-1);
        this.setPosition(new int[] {-1, -1});
        this.setOrientation(Direction.EMPTY);
    }

    // Overloaded constructor
    public Ship(int length, Direction orientation, int[] position) {
        this.setLength(length);
        this.setPosition(position);
        this.setOrientation(orientation);
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
}
