package kartofel.klasaMain;

import java.io.IOException;
import javax.swing.UnsupportedLookAndFeelException;
import kartofel.interfejs.Gra;

/**
 * Klasa główna programu, zawiera główny wątek, uruchamia grę
 * @author Kamil Burzyński
 */public class Main {

/**
 * Tworzy i uruchamia grę
 * @param args
 * @throws ClassNotFoundException
 * @throws InstantiationException
 * @throws IllegalAccessException
 * @throws UnsupportedLookAndFeelException
 * @throws IOException
 */
    public static void main(String[] args) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IOException {
        Gra gra = new Gra();//Tworzy grę
        gra.nowaGra();//Uruchamia grę
    }
}
