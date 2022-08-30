import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void main(String[] argv) throws Exception {
        // Instantiate the player object
        Player p = new Player();

        p.setupBoard();
    }
}
