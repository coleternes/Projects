public class Helper {
    public int inputToShip(String input) {
        int shipIndex;
        try {
            shipIndex = Integer.parseInt(input) - 1;
        }
        catch (Exception e) {
            shipIndex = -1;
        }

        return shipIndex;
    }

    public int[] inputToPosition(String input) {
        int[] position = new int[2];

        if (input.charAt(0) == 'a' || input.charAt(0) == 'A') {
            position[0] = 0;
        }
        else if (input.charAt(0) == 'b' || input.charAt(0) == 'B') {
            position[0] = 1;
        }
        else if (input.charAt(0) == 'c' || input.charAt(0) == 'C') {
            position[0] = 2;
        }
        else if (input.charAt(0) == 'd' || input.charAt(0) == 'D') {
            position[0] = 3;
        }
        else if (input.charAt(0) == 'e' || input.charAt(0) == 'E') {
            position[0] = 4;
        }
        else if (input.charAt(0) == 'f' || input.charAt(0) == 'F') {
            position[0] = 5;
        }
        else if (input.charAt(0) == 'g' || input.charAt(0) == 'G') {
            position[0] = 6;
        }
        else if (input.charAt(0) == 'h' || input.charAt(0) == 'H') {
            position[0] = 7;
        }
        else if (input.charAt(0) == 'i' || input.charAt(0) == 'I') {
            position[0] = 8;
        }
        else if (input.charAt(0) == 'j' || input.charAt(0) == 'J') {
            position[0] = 9;
        }
        else {
            position[0] = -1;
            position[1] = -1;
        }

        try {
            position[1] = Integer.parseInt(input.substring(1)) - 1;
        }
        catch (Exception e) {
            position[0] = -1;
            position[1] = -1;
        }

        return position;
    }

    public Direction inputToOrientation(String input) {
        if (input.toLowerCase().equals("n") || input.toLowerCase().equals("north")) {
            return Direction.NORTH;
        }
        else if (input.toLowerCase().equals("s") || input.toLowerCase().equals("south")) {
            return Direction.SOUTH;
        }
        else if (input.toLowerCase().equals("e") || input.toLowerCase().equals("east")) {
            return Direction.EAST;
        }
        else if (input.toLowerCase().equals("w") || input.toLowerCase().equals("west")) {
            return Direction.WEST;
        }
        else {
            return Direction.EMPTY;
        }
    }

    public String positionToInput(int row, int col) {
        String input = "";

        switch (row) {
            case 0:
                input += "A";
                break;
            case 1:
                input += "B";
                break;
            case 2:
                input += "C";
                break;
            case 3:
                input += "D";
                break;
            case 4:
                input += "E";
                break;
            case 5:
                input += "F";
                break;
            case 6:
                input += "G";
                break;
            case 7:
                input += "H";
                break;
            case 8:
                input += "I";
                break;
            case 9:
                input += "J";
                break;
        }

        col += 1;
        input += col;

        return input;
    }
}
