package kartofel.interfejs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import kartofel.klasyWyliczeniowe.Przeciwnik;

/**
 * Klasa odpowiedzialna za tworzenie menu
 * @author Kamil Burzyński
 */
public class PasekMenu extends JPanel implements ActionListener {

    private final JMenuBar pasekMenu;
    private final JMenu menuPlik;
    private final JMenu menuPrzeciwnik;
    private final JMenu menuInformacje;
    private final ButtonGroup grupaRadio;
    private final JRadioButtonMenuItem komputerLokalny;
    private final JRadioButtonMenuItem osobaLokalna;
    private final JMenuItem menuNowaGra;
    private final JMenuItem zamknijProgram;
    private final JMenuItem wyswietlPomoc;
    private final JMenuItem wyswietlAutora;
    private Gra gra;

    /**
     * Tworzy pasek menu w grze
     * @param refgra Referencja do głownego obiektu gry
     */
    public PasekMenu(Gra refgra) {
        gra = refgra;//Referencja do obiektu klasy Gra (JFrame)
        pasekMenu = new JMenuBar();//Tworzy nowy pasek menu
        pasekMenu.addKeyListener(gra);
        menuPlik = new JMenu("Plik");//Tworzy menu 'Plik'
        menuPlik.addKeyListener(gra);
        menuPrzeciwnik = new JMenu("Przeciwnik");//Tworzy menu 'Przeciwnik'
        menuPrzeciwnik.addKeyListener(gra);
        menuInformacje = new JMenu("Informacje");//Tworzy menu 'Informacje'
        menuInformacje.addKeyListener(gra);

        menuNowaGra = new JMenuItem("Nowa gra");//Tworzy opcję 'Nowa gra'
        menuNowaGra.addKeyListener(gra);
        zamknijProgram = new JMenuItem("Zakończ program");//Tworzy opcję 'Zakończ program'
        zamknijProgram.addKeyListener(gra);

        grupaRadio = new ButtonGroup();//Tworzy grupę przycisków typu 'Radio'
        komputerLokalny = new JRadioButtonMenuItem("Komputer");
        komputerLokalny.addKeyListener(gra);
        osobaLokalna = new JRadioButtonMenuItem("Osoba przy komputerze");
        osobaLokalna.addKeyListener(gra);

        wyswietlAutora = new JMenuItem("Autor programu");//Tworzy opcję 'Autorzy programu'
        wyswietlAutora.addKeyListener(gra);
        wyswietlPomoc = new JMenuItem("Pomoc");//Tworzy opcję 'Pomoc'
        wyswietlPomoc.addKeyListener(gra);

        menuPlik.add(menuNowaGra);//Dodaje opcję 'menuNowaGra' do 'menuPlik'
        menuPlik.add(zamknijProgram);//Dodaje opcję 'zamknijProgram' do 'menuPlik'

        grupaRadio.add(komputerLokalny);//Grupuje przyciski radio
        grupaRadio.add(osobaLokalna);
        osobaLokalna.setSelected(true);
        menuPrzeciwnik.add(komputerLokalny);
        menuPrzeciwnik.add(osobaLokalna);

        menuInformacje.add(wyswietlPomoc);//Dodaje opcję 'wyswietlPomoc' do 'menuInformacje'
        menuInformacje.add(wyswietlAutora);//Dodaje opcję 'wyswietlAutora' do 'menuInformacje'

        pasekMenu.add(menuPlik);//Dodaje 'menuPlik' do paska menu
        pasekMenu.add(menuPrzeciwnik);////Dodaje 'menuPrzeciwnik' do paska menu
        pasekMenu.add(menuInformacje);//Dodaje 'menuInformacje' do paska menu

        //Obsługa zdarzeń

        /**
         * Tworzy nową grę
         */
        menuNowaGra.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                gra.nowaGra();//Wywołuje funkcję
            }
        });

        /**
         * Zamyka program
         */
        zamknijProgram.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                gra.zakoncz();//Wywołuje funkcję
            }
        });

        /**
         * Zmienia tryb
         */
        komputerLokalny.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                gra.nowaGra();
            }
        });

        /**
         * Zmienia tryb
         */
        osobaLokalna.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                gra.nowaGra();
            }
        });


        /**
         * Wyświetla pomoc na panelu
         */
        wyswietlPomoc.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                gra.zwrocKomunikat("Dwóch użytkowników naprzemiennie wykonuje ruchy, klikając odpowiedne pole");//Wyświetla komunikat
            }
        });

        /**
         * Wyświetla informacje o autorze na panelu
         */
        wyswietlAutora.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                gra.zwrocKomunikat("Autor: Kamil Burzyński");//Wyświetla komunikat
            }
        });
    }

    /**
     * Służy do pobrania referencji na pasek menu
     * @return referencja na JMenuBar
     */
    public JMenuBar pobierzMenu() {
        return pasekMenu;
    }

    /**
     * Podaje przeciwnika z którym gramy
     * @return Nazwa przeciwnika
     */
    public Przeciwnik podajZaznaczenie() {
        if (komputerLokalny.isSelected()) {
            return Przeciwnik.KOMPUTER;
        } else {
            return Przeciwnik.OSOBA;
        }
    }

    /**
     * Niezdefiniowana akcja
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
