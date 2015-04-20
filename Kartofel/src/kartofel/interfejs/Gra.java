package kartofel.interfejs;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import kartofel.klasaMain.Main;
import kartofel.logika.Logika;
import kartofel.logika.Wynik;

/**
 * Klasa Gra odpowiada za integrację interfejsu i logiki, tworzy JFrame
 * @author Kamil Burzyński
 */
public class Gra extends JPanel implements ActionListener, KeyListener {

    /**
     * Klasa ustawia menadżerem rozkładu wszystkie elementy okna
     */
    private class Zawartosc extends JPanel {

        private Konsola konsola; //Referencja na konsolę
        private Tabela tabela; //Referencja na tabelę

        /**
         * Tworzy klasę odpowiedzialną za ułożenie konsoli i tabeli
         * @param gra referencja na obiekt typu gra (potrzebne do keylistner)
         * @throws IOException
         */
        public Zawartosc(Gra gra) throws IOException {
            super();//Konstruktor JPane
            konsola = new Konsola(gra); //Obiekt typu konsola
            tabela = new Tabela(gra, rozmiar); //Obiekt typu Tabela, z przekazaniem słuchacza i rozmiaru macierzy
            this.add(tabela); //Dodawanie do panelu obiektu tabela
            this.add(konsola); //Dodawanie do panelu obiektu konsola
            konsola.addKeyListener(gra);//Dodanie słuchacza klawiszy
            tabela.addKeyListener(gra);//Dodanie słuchacza klawiszy
            this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));//Menadżer rozkładu
        }
    }
    private Logika logika;//Logika gry
    private Dimension min;//Minimalny rozmiar okna
    private PasekMenu menu;//Referencja na pasek menu
    private JFrame frame;//Okno programu
    private SwingWorker swingworker;//Referencja na Java SwingWorker
    private Zawartosc zawartosc; //Prywatna klasa z treścią okna
    protected final int rozmiar; //Rozmir macierzy
    private boolean sterowanieKlawiaturą;//Czy akywne  jest sterownaie z klawiatury
    private int poprzedniX;//Poprzedni ruch wykonany przez gracza
    private int poprzedniY;//Poprzedni ruch wykonany przez gracza
    private int punktyMysz;//Punkty gracza wykonującego ruchy myszą
    private int punktyKlaw;//Punkty gracza wykonującego ruchy klawiaturą
    private int komunikat;//Numer komunikatu po wykonaniu ruchu
    private Random rand;//Losowa wartość (który gracz zaczyna)
    private Image ikona;//Ikona programu
    private final String sciezkaIkony;//Ścieżka do ikony programu
    private InputStream strumienIkony;//Strumień, w którym ikona jest otwarta

    /**
     * Konstruktor, tworzy okno i jego komponenty, ustala wygląd, rozpoczyna pierwszą grę
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws UnsupportedLookAndFeelException
     */
    public Gra() throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, UnsupportedLookAndFeelException, IOException {
        super();//Konstruktor JPane
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//Ustawienie wyglądu okna na domyślny systemowy
        JFrame.setDefaultLookAndFeelDecorated(true);//Ustawia typ okna
        rozmiar = 10;//Rozmiar macierzy z przyciskami
        sciezkaIkony = new String("/kartofel/ikony/kartofel.png");//Ścieżka do ikony
        strumienIkony = Main.class.getResourceAsStream(sciezkaIkony);//Strumień do ikony
        rand = new Random(); //Nowy obiekt do losowania
        frame = new JFrame("Gra w Kartofla");//Tworzy okno i ustawia tytuł
        frame.addKeyListener(this); //Dodanie słuchacza klawiszy
        this.addKeyListener(this); //Dodanie słuchacza klawiszy
        min = new Dimension(592, 392); //Zdefiniowanie minimalnego rozmiaru okna
        menu = new PasekMenu(this); //Stworzenie paska menu
        zawartosc = new Zawartosc(this);//Stworzenie klasy z zawartością (tabela i konsola)
        frame.setContentPane(zawartosc);//Ustawienie jako zawartość  stworzonej powyżej klasy
        zawartosc.addKeyListener(this);//Dodanie słuchacza klawiszy
        menu.addKeyListener(this);//Dodanie do menu słuchacza klawiszy
        frame.setJMenuBar(menu.pobierzMenu());//Ustawienie paska menu
        frame.setMinimumSize(min);//Ustawienie minimalnego rozmiaru
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Reakcja na zamknięcie okna
        ikona = ImageIO.read(strumienIkony); //Wczytanie obrazka ze strumienia
        frame.setIconImage(ikona);//Przypisanie ikony
        frame.pack();//Układanie elementów interfejsu
        frame.setVisible(true);//Ustawianie okna jako widoczne
        frame.setFocusable(true);//Ustawienie focusu
        frame.requestFocus();//Zażądanie fokusu
    }

    /**
     * Tworzy nową grę
     */
    public void nowaGra() {
        swingworker = null;//Przypisanie null do referencji (nie jest konieczne)
        swingworker = new SwingWorker<Void, Void>() {//Tworzenie klasy pochodnej SwingWorker i implementacja

            @Override
            protected Void doInBackground() throws Exception {//Zadania wykonywane w tle
                zawartosc.konsola.wyswietlKomunikatCzekaj();//Podaje na konsolę wiadomośc "Proszę czekać"
                zawartosc.konsola.resetCzasu();//Zeruje czas gry
                logika = new Logika(menu.podajZaznaczenie(), rand.nextBoolean(), rozmiar, rand);//Generowanie nowej logiki programu 
                switch (menu.podajZaznaczenie()) {//W zależności od przeciwnika wyświetl
                    case KOMPUTER:
                        zawartosc.konsola.wypiszPrzeciwnika("<Grasz z komputerem>");
                        break;
                    case OSOBA:
                        zawartosc.konsola.wypiszPrzeciwnika("<Grasz z drugą osobą>");
                        break;
                }
                return null;//Wymagane przez SwingWorker
            }

            @Override
            protected void done() {//Operacje wkonywane po zkończeniu działania metody "doInBackground()"
                zawartosc.konsola.wyswietlKomunikatCzekaj();//Przywraca poprzednią wiadomość na konsolę
                if (!logika.graZKomputerem()) {//Grają 2 osoby
                    if (logika.ktoryRozpoczyna()) {
                        zawartosc.konsola.wyswietlKomunikat("<Rozpoczyna gracz sterujący myszą>");
                        sterowanieKlawiaturą = false;
                    } else {
                        zawartosc.konsola.wyswietlKomunikat("<Rozpoczyna gracz sterujący klawiaturą>");
                        sterowanieKlawiaturą = true;
                    }
                } else {//Gra z komputerem
                    if (logika.czyKomputerRozpoczyna()) {
                        zawartosc.konsola.wyswietlKomunikat("<Komputer rozpoczął grę wykonując ruch>");
                        sterowanieKlawiaturą = false;
                    } else {
                        zawartosc.konsola.wyswietlKomunikat("<Rozpoczyna gracz sterujący myszą>");
                        sterowanieKlawiaturą = false;
                    }
                }
                zawartosc.konsola.ustawPunkty(0, 0);//Zeruje licznik punktów graczy
                wyswietl();//Wyświetla nową planszę
            }
        };
        swingworker.execute();//Wykonywanie SwingWorker
    }

    /**
     * Wyświetla zmodyfikowaną planszę na ekran
     */
    private void wyswietl() {//Alias
        zawartosc.tabela.wyswietlTabele(logika.getPlansza());
    }

    /**
     * Podaje komunikat na konsolę
     * @param wiadomosc tekst, który ma być wyświetlony
     */
    public void zwrocKomunikat(String wiadomosc) {
        zawartosc.konsola.wyswietlKomunikat(wiadomosc);
    }

    /**
     * Kończy program
     */
    public void zakoncz() {
        System.exit(0);
    }

    /**
     * Sprawdza, który przycisk (z macierzy) kliknął użytkownik, wywołuje funkcję modyfikującą
     * @param e Przycisk, który został kliknięty
     */
    public void actionPerformed(ActionEvent e) {//Parsowanie tooltipów
        JButton przycisk = (JButton) e.getSource();//Tworzy referencję na kliknięty przycisk
        String pozycja = przycisk.getToolTipText();//Pobiera tekst z tooltipów
        int offset = 0;//Przesunięcie tekstu w przypadku dwucyfrowej liczby pozy
        int pozx;//Współrzędna X przycisku
        int pozy = Integer.parseInt(pozycja.substring(8, 9));//Wyciąganie liczby ze stringa
        if (pozy == 1) {//Liczba to 1 lub 10
            if (pozycja.substring(9, 10).equals("0")) {//Liczba to 10
                pozy = 10;
                offset++;
            }
        }
        pozx = Integer.parseInt(pozycja.substring(20 + offset, 21 + offset));//Wyciąganie liczby ze stringa, dodaje offset jeśli potrzebny
        if (pozx == 1) {//Liczba to 1 lub 10
            if (pozycja.length() >= (22 + offset)) {//Sprawdzenie wyjścia poza długość stringa
                if (pozycja.substring(21 + offset, 22 + offset).equals("0")) {//Liczba to 10
                    pozx = 10;
                }
            }
        }
        pozx--;//Zmiana na indeksowanie tablic
        pozy--;
        modyfikuj(pozx, pozy);//Po każdym kliknięciu wywołaj funkcję modyfikującą planszę
    }

    /**
     * Kliknięcie przycisku o parametrach x i y
     * @param x pozycja przycisku
     * @param y pozycja przycisku
     */
    private void modyfikuj(final int x, final int y) {
        swingworker = null;//Przypisanie null do referencji (nie jest konieczne)
        swingworker = new SwingWorker<Void, Void>() {//Tworzenie klasy pochodnej SwingWorker i implementacja

            @Override
            protected Void doInBackground() throws Exception {//Zadania wykonywane w tle
                zawartosc.konsola.wyswietlKomunikatCzekaj();//Wyświetla komunikat czekaj
                Wynik wynik = logika.getWynik(y, x);//Właściwa modyfikacja i wyświetlenie komunikatu
                komunikat = wynik.komunikat;
                zawartosc.konsola.wyswietlKomunikatCzekaj();//Wyświetlenie poprzedniego komunikatu
                switch (komunikat) {
                    case 1:
                        zawartosc.konsola.wyswietlKomunikat("<Kolej gracza sterującego myszą>  <Sterujący myszą wykonał: " + wynik.ruchyMysz + " ruchów>");
                        break;
                    case 2:
                        zawartosc.konsola.wyswietlKomunikat("<Kolej gracza sterującego klawiaturą>  <Sterujący klawiaturą wykonał: " + wynik.ruchyKlaw + " ruchów>");
                        break;
                    case 3:
                        zawartosc.konsola.wyswietlKomunikat("<Kolej gracza sterującego myszą> <Sterujący klawiaturą zdobył:" + wynik.zdobyte + " punktów>");
                        break;
                    case 4:
                        zawartosc.konsola.wyswietlKomunikat("<Kolej gracza sterującego klawiaturą> <Sterujący myszą zdobył:" + wynik.zdobyte + " punktów>");
                        break;
                    case 5:
                        zawartosc.konsola.wyswietlKomunikat("<Wygrywa gracz sterujący myszą!>");
                        break;
                    case 6:
                        zawartosc.konsola.wyswietlKomunikat("<Wgrywa gracz sterujący klawiaturą!>");
                        break;
                    case 7:
                        zawartosc.konsola.wyswietlKomunikat("<Remis!>");
                        break;
                    case 8:
                        zawartosc.konsola.wyswietlKomunikat("<Nie można wykonać takiego ruchu>");
                        break;
                    case 9:
                        zawartosc.konsola.wyswietlKomunikat("<Komputer zdobył " + wynik.zdobyte + " punktów> <Ruch sterującego myszą>");
                        break;
                    case 10:
                        zawartosc.konsola.wyswietlKomunikat("<Komputer postawił kropkę na polu: " + wynik.popx + "," + wynik.popy + ">  <Ruch sterującego myszą>");
                        break;
                    case 11:
                        zawartosc.konsola.wyswietlKomunikat("<Wygrywa komputer>");
                        break;
                }
                sterowanieKlawiaturą = wynik.klawiatura;//Zmiana sterowania klawiaturą
                poprzedniX = wynik.popx;//Przypisanie wartości poprzedniego ruchu
                poprzedniY = wynik.popy;
                punktyMysz = wynik.punktyMysz;//Dopisanie punktów do licznika gracza
                punktyKlaw = wynik.punktyKlaw;
                return null;//Wymagane przez SwingWorker
            }

            @Override
            protected void done() {//Operacje wkonywane po zkończeniu wykonywania metody "doInBackground()"
                zawartosc.konsola.ustawPunkty(punktyMysz, punktyKlaw);//Ustawia punkty
                if (komunikat == 5 || komunikat == 6 || komunikat == 7 || komunikat == 11) {
                    zawartosc.konsola.zatrzymajCzas();
                }
                wyswietl();//Wyświetla planszę
            }
        };
        swingworker.execute();//Wykonywanie nowego wątku SwingWorker 
    }

    /**
     * Nieobsługiwana w programie
     * @param e
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Po wciśnięciu na klawiaturze strzałki wywołuje focus na odpowiedniej komórce
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        if (sterowanieKlawiaturą) {
            int kodKlawisza;
            int poziom = 0;//Położenie klawisza w poziomie
            int pion = 0;//Położenie klawisza w pionie
            kodKlawisza = e.getKeyCode();
            switch (kodKlawisza) {
                case KeyEvent.VK_UP:
                    pion--;
                    break;
                case KeyEvent.VK_DOWN:
                    pion++;
                    break;
                case KeyEvent.VK_LEFT:
                    poziom--;
                    break;
                case KeyEvent.VK_RIGHT:
                    poziom++;
                    break;
            }
            pion = pion + poprzedniX;//Przesunięcie w stosunku do poprzedniego klawisza
            poziom = poziom + poprzedniY;
            //Chodzenie "focusem" tylko w ustalonym zakresie
            if (pion == rozmiar) {//Korekta zakresu
                pion = 9;
            } else if (pion == -1) {
                pion = 0;
            }
            if (poziom == rozmiar) {//Korekta zakresu
                poziom = 9;
            } else if (poziom == -1) {
                poziom = 0;
            }
            if (zawartosc.tabela.zaznaczKomorke(pion, poziom)) {//Zaznacza ustaloną komórkę
                poprzedniX = pion;//Przypisanie wsółrzędnych poprzedniego ruchu tylko w przypadku gdy ruch był dozwolony
                poprzedniY = poziom;
            }
        }
    }

    /**
     * Nieobsługiwana w programie
     * @param e
     */
    public void keyReleased(KeyEvent e) {
    }
}
