import System.MainSystem;
import System.Terminal;

public class Main {
    public static void main(String[] args) {
        MainSystem system = new MainSystem();
        Terminal terminal = new Terminal();
        terminal.wellcome();
        while (true) {
            system.selector();
        }
    }
}