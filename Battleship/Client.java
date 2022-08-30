import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    //private Player _player;

    public static void main(String[] argv) {
        /*
        this._player = new Player();
        this._player.PrintBoard();
        */

        Player p = new Player();
        p.printBoard();
    }
}
