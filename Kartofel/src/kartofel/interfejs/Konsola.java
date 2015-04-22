package kartofel.interfejs;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Klasa definiuje obiekt konsoli
 * @author Kamil Burzyński
 */
public class Konsola extends JPanel {

    /**
     * Klasa przechowująca pojedyncze pole tekstowe
     * Potrzebna do ustalenia odpowiedniego layoutu
     */
    private class PoleKomunikatu extends JPanel implements Runnable {

        public JTextField pole;

        /**
         * Konstruktor tworzący pole odpowiedzalne za wyświetlanie komunikatu
         * @param tekst String, który ma się wyświetlić w konsoli
         * @param kolumny Szerokość JTextField
         */
        public PoleKomunikatu(String tekst, int kolumny) {
            pole = new JTextField(tekst, kolumny);
            pole.addKeyListener(refgra);
            pole.setHorizontalAlignment((int) CENTER_ALIGNMENT);
            pole.setEditable(false);
            this.addKeyListener(refgra);
            this.add(pole);
            this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        }

        /**
         * Funkcja odpowiedzialna za mierzenie czasu gry
         */
        public void run() {
            long start = System.currentTimeMillis();
            long stop;
            int sek;
            int min;
            while (true) {
                stop = System.currentTimeMillis();
                sek = (int) ((stop - start) / 1000);
                min = (sek - sek % 60) / 60;
                powiadomieniaPunkty.pole.setText("<Czas gry:  " + ((min % 60) < 10 ? "0" + (min % 60) : (min % 60)) + ":" + ((sek % 60) < 10 ? "0" + (sek % 60) : (sek % 60))
                        + ">  <Punkty grającego klawiaturą: " + punktyKlaw + ">  <Punkty grającego myszką: " + punktyMysz + ">");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }
    }
    private final PoleKomunikatu powiadomieniaRuchy;//Pole tekstowe
    private final PoleKomunikatu powiadomieniaPunkty;
    private String bufor;//Bufor poprzedniego komunikatu
    private Thread czas;//Wątek potrzebny do mierzenia czasu
    private int punktyMysz;//Punkty graczy
    private int punktyKlaw;
    private String nazwaPrzeciwnika = null;//Wyświetlana nazwa przeciwnika
    private final int szerokosc; //Ilość kolumn JTextField
    private final Gra refgra;//Referencja na obiekt typu gra

    /**
     * Tworzy nową konsolę, na której wyświetlane są komunikaty
     * @param gra Referencja na obiekt typu gra
     */
    public Konsola(Gra gra) {
        refgra = gra;
        szerokosc = 70;//Ilość znaków JTextField
        powiadomieniaRuchy = new PoleKomunikatu("", szerokosc);//Komunikaty o kolejności ruchów
        powiadomieniaRuchy.addKeyListener(gra);
        this.add(powiadomieniaRuchy);

        powiadomieniaPunkty = new PoleKomunikatu("", szerokosc);//Powiadomienia o ilości punktów
        powiadomieniaPunkty.addKeyListener(gra);
        this.add(powiadomieniaPunkty);

        czas = new Thread(powiadomieniaPunkty);//Mierzenie czasu
        czas.start();

        this.addKeyListener(gra);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        bufor = null;
    }

    /**
     * Wyświetla wiadomość w konsoli (komponent JTextField)
     * Formatuje tekst w zależności od rodzaju komunikatu
     * @param wiadomosc Tekst, który ma zostać wypisany na ekran
     */
    public void wyswietlKomunikat(String wiadomosc) {
        if (wiadomosc.substring(0, 1).equals("<")) {
            powiadomieniaRuchy.pole.setText(nazwaPrzeciwnika + "  " + wiadomosc);
        } else {
            powiadomieniaRuchy.pole.setText("<" + wiadomosc + ">");
        }
    }

    /**
     * Podaje informacje o przeciwniku
     * @param tekst Nazwa przeciwnika
     */
    public void wypiszPrzeciwnika(String tekst) {
        nazwaPrzeciwnika = new String(tekst);
    }

    /**
     * Wyświetla komunikat "Proszę czekać" Po powtórnym wywołaniu wyświetla poprzedni komunikat (z bufora)
     */
    public void wyswietlKomunikatCzekaj() {
        if (bufor == null) {
            bufor = new String(powiadomieniaRuchy.pole.getText());
            powiadomieniaRuchy.pole.setForeground(Color.RED);
            powiadomieniaRuchy.pole.setText("Proszę czekać...");
        } else {
            powiadomieniaRuchy.pole.setForeground(Color.BLACK);
            powiadomieniaRuchy.pole.setText(new String(bufor));
            bufor = null;
        }
    }

    /**
     * Resetuje czas gry
     */
    public void resetCzasu() {
        if (czas.isAlive()) {//Jeżeli czas jest liczony
            czas.stop();//Zatrzymaj
        }
        czas = new Thread(powiadomieniaPunkty);//Uruchom nowy wątek
        czas.start();//Zacznij liczyć czas od nowa
    }

    /**
     * Ustawia punkty, które są potem wyświetlane
     * @param czerw Punkty gracza z myszą
     * @param nieb Punkty gracza z klawiaturą
     */
    public void ustawPunkty(int czerw, int nieb) {
        punktyMysz = czerw;
        punktyKlaw = nieb;
    }

    /**
     * Zatrzymuje czas w przypadku wygranej, ustawia końcowy komunikat o punktach
     */
    public void zatrzymajCzas() {
        String pom = new String(powiadomieniaPunkty.pole.getText());
        String subString = pom.substring(0, 18);
        subString += " <Punkty grającego klawiaturą: " + punktyKlaw + ">  <Punkty grającego myszką: " + punktyMysz + ">";
        powiadomieniaPunkty.pole.setText(subString);
        czas.stop();
    }
}
