package kartofel.interfejs;

import java.io.IOException;
import kartofel.klasyWyliczeniowe.Przyciski;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import kartofel.klasaMain.Main;

/**
 * Klasa wyświetlająca Planszę z grą. Zawiera prywatną klasę wewnętrzną
 * przechowującą tablicę przycisków.
 * @author Kamil Burzyński
 */
public class Tabela extends JPanel {

    private final Macierz macierz; //Referencja na obiekt prywatnej klasy Macierz
    private final int rozmiar; //Rozmiar macierzy
    private final int iloscPrzyciskow; //Ilość rodzajów ikon
    private final Gra refgra; //Referencja na obiekt typu gra (potrzebana do KeyListener)
    private final Image[] obrazki; //Tablica przechowująca obrazki
    private final ImageIcon[] ikony; //Tablica przechowująca ikony
    private final String[] sciezkiIkon;
    private final InputStream[] strumienieIkon;

    /**
     * Klasa prywatna do przechowywania przycisków
     */
    private class Macierz extends JPanel { 

        private final JButton[][] komorki;//Tablica referencji na przyciski JButtonJButton

        /**
         * Konstruktor klasy prywatnej
         */
        public Macierz() {
            super(); //Konstruktor domyślny JPanel
            komorki = new JButton[rozmiar][rozmiar]; //Nowa tablica referencji na przyciski
            for (int i = 0; i < rozmiar; i++) {
                for (int j = 0; j < rozmiar; j++) {
                    if (j < rozmiar - i) {//Lewa górna część macierzy
                        komorki[i][j] = new JButton(zwrocIkone(Przyciski.PUSTY)); //Ikona na przycisku
                        komorki[i][j].setEnabled(true); //Przycisk aktywny
                        //rozmiar obrazka 24x16
                        komorki[i][j].setPreferredSize(new Dimension(24, 16));//Ustawia odpowiedni (mniejszy) rozmiar
                        komorki[i][j].setPressedIcon(zwrocIkone(Przyciski.ZAZNACZONY)); //Ikona widoczna przy klikaniu przycisku
                        komorki[i][j].setToolTipText("Wiersz: " + Integer.toString(i + 1) + "; Kolumna: " + Integer.toString(j + 1));//"Ludzka" numeracja
                    } else {
                        komorki[i][j] = new JButton(" ");//W pozostałych komórkach są spacje
                        komorki[i][j].setEnabled(false);//Pozostałe przyciski nieaktywne
                    }
                    komorki[i][j].setHorizontalAlignment((int) CENTER_ALIGNMENT); //Wyśrodkowanie zawartości JButton
                    komorki[i][j].setVerticalAlignment((int) CENTER_ALIGNMENT); //Wyśrodkowanie pionowe
                    komorki[i][j].addActionListener(refgra);//Dodanie obsługi akcji 
                    komorki[i][j].addKeyListener(refgra); //Dodanie słuchacza akcji
                    this.add(komorki[i][j]);//Dodanie do panelu tego przycisku
                }
            }
            this.addKeyListener(refgra); //Dodanie keylistnera do bieżącego panelu
            this.setLayout(new GridLayout(rozmiar, rozmiar));//Ustalenie ilości komórek menadżera rozkładu
        }
    }//Koniec deklaracji prywatnej klasy

    /**
     * Tworzy nową tabelę
     * @param gra Referencja na obiekt typu gra
     * @param rozm Rozmiar macierzy
     * @throws java.io.IOException
     */
    public Tabela(Gra gra, int rozm) throws IOException {
        super(); //Konstruktor domyślny JPanel
        rozmiar = rozm; //Rozmiar macierzy
        iloscPrzyciskow = 18; //Ilość ikon przycisków
        refgra = gra; //Referencja na obiekt typu gra (potrzebna do keylistner)
        sciezkiIkon = new String[iloscPrzyciskow]; //Tablica zawierająca ścieżki do poszczególnych ikon
        strumienieIkon = new InputStream[iloscPrzyciskow];//Tablica ze strumieniami do czytania obrazków
        obrazki = new Image[iloscPrzyciskow];//Tablica z obiektami image
        ikony = new ImageIcon[iloscPrzyciskow];//Tablica z ikonami przycisków
        for (int i = 0; i < iloscPrzyciskow; i++) {
            sciezkiIkon[i] = new String("/kartofel/ikony/" + Integer.toString(i) + ".png");
            strumienieIkon[i] = Main.class.getResourceAsStream(sciezkiIkon[i]);
            obrazki[i] = ImageIO.read(strumienieIkon[i]);
            ikony[i] = new ImageIcon(obrazki[i]);
        }
        macierz = new Macierz(); //Tworzenie nowej tabeli
        this.addKeyListener(gra); //Nasłuchiwanie klawiszy w obiekcie Tabela
        this.add(macierz); //Dodawanie tabeli do JPanel
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));//Menadżer rozkładu
    }

    /**
     * Wyświetla tablicę z przyciskami, ustawiając odpowiednie obrazki na przyciskach
     * @param wej Dwuwymiarowa tablica typu Przyciski[][]
     */
    public void wyswietlTabele(Przyciski[][] wej) {//Wyświetla tablicę
        for (int i = 0; i < rozmiar; i++) {
            for (int j = 0; j < rozmiar; j++) {
                if (j < rozmiar - i) { //Tylko lewa górna część macierzy
                    macierz.komorki[i][j].setIcon(zwrocIkone(wej[i][j]));
                }
            }
        }
    }

    /**
     * Zaznacza przycisk podany w argumentach (ustawia focus)
     * @param x Parametr X komórki
     * @param y Parametr Y komórki
     * @return 
     */
    public boolean zaznaczKomorke(int x, int y) { 
        if (macierz.komorki[x][y].isEnabled()) {
            macierz.komorki[x][y].requestFocus();
            return true;//Udało się zaznaczyć
        }
        return false;//Nie udało się zaznaczyć
    }

    private ImageIcon zwrocIkone(Przyciski ikona) { //Zwraca ikonę zgodnie z podanym argumentem
        switch (ikona) {
            case PUSTY:
                return ikony[0];
            case KROPKA:
                return ikony[1];
            case PION:
                return ikony[2];
            case POZIOM:
                return ikony[3];
            case PIONPOZIOM:
                return ikony[4];
            case SKOSLP:
                return ikony[5];
            case SKOSPL:
                return ikony[6];
            case SKOSLPPL:
                return ikony[7];
            case POZIOMSKOSLP:
                return ikony[8];
            case POZIOMSKOSPL:
                return ikony[9];
            case POZIOMSKOSLPPL:
                return ikony[10];
            case PIONSKOSLP:
                return ikony[11];
            case PIONSKOSPL:
                return ikony[12];
            case PIONSKOSLPPL:
                return ikony[13];
            case PIONPOZIOMSKOSLP:
                return ikony[14];
            case PIONPOZIOMSKOSPL:
                return ikony[15];
            case PIONPOZIOMSKOSLPPL:
                return ikony[16];
            case ZAZNACZONY:
                return ikony[17];
        }
        //System.out.println("NULL");
        return null; //Błąd
    }
}
