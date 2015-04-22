package kartofel.logika;

import java.util.Random;
import kartofel.klasyWyliczeniowe.Przeciwnik;
import kartofel.klasyWyliczeniowe.Przyciski;

/**
 * Klasa przechowująca planszę na której toczy się gra
 *
 * @author Kamil Burzyński
 */
public class Logika {

    private boolean kolejka;
    private final int rozmiar;
    private final Pole[][] plansza;
    private final Przeciwnik przeciwnik;
    private int punktyK;
    private int punktyM;
    private int komunikat;
    private int przyrostPkt;
    private int licznikM;
    private int licznikK;
    private final Random rand;
    private boolean komputerZaczyna;
    private int ruchKomputeraX;
    private int ruchKomputeraY;

    /**
     * Tworzy nową planszę, jest wywoływany dla każdej nowej gry
     *
     * @param przec Przeciwnik (komputer lub osoba)
     * @param rozpoczyna Kto rozpoczyna grę, true - gracz z myszą, false gracz z
     * klawiaturą
     * @param rozm Rozmiar planszy
     * @param random
     */
    public Logika(Przeciwnik przec, boolean rozpoczyna, int rozm, Random random) {
        rozmiar = rozm;
        przeciwnik = przec;
        rand = random;
        kolejka = rozpoczyna;
        punktyK = 0;
        punktyM = 0;
        komunikat = 0;
        licznikK = 0;
        licznikM = 0;
        komputerZaczyna = false;
        plansza = new Pole[rozmiar][rozmiar];
        for (int i = 0; i < rozmiar; i++) {
            for (int j = 0; j < rozmiar; j++) {
                if (j < rozmiar - i) {
                    if ((i == 9 && j == 0) || (i == 0 && j == 9)) {
                        plansza[i][j] = new Pole();
                        plansza[i][j].ustawKropke();
                    } else {
                        plansza[i][j] = new Pole();
                    }
                }
            }
        }
        //Jeśli gra z komputerem i komputer zaczyna ->Ustaw kropkę z komputera
        if (!kolejka && przeciwnik == Przeciwnik.KOMPUTER) {
            int losX;
            int losY;
            do {//Komputer zaczyna stawiając kropkę w losowym miejscu
                losX = rand.nextInt(rozmiar);
                losY = rand.nextInt(rozmiar);
            } while (losY >= rozmiar - losX || plansza[losX][losY].zwrocPrzycisk() != Przyciski.PUSTY);
            plansza[losX][losY].ustawKropke();
            komputerZaczyna = true;
            kolejka = true;
        }
    }

    /**
     * Zwraca planszę do interfejsu użytkownika
     *
     * @return Dwuwymiarowa tablica typu Przyciski[rozmiar][rozmiar]
     */
    public Przyciski[][] getPlansza() {
        Przyciski[][] pom = new Przyciski[rozmiar][rozmiar];
        for (int i = 0; i < rozmiar; i++) {
            for (int j = 0; j < rozmiar; j++) {
                if (j < rozmiar - i) {
                    pom[i][j] = plansza[i][j].zwrocPrzycisk();
                }
            }
        }
        return pom;
    }

    /**
     * Sprawdza jaką modyfikację należy wprowadzić po kliknięciu jednego z
     * przycisków
     *
     * @param x Współrzędna x klikniętego przycisku
     * @param y Współrzędna y klikniętego przycisku
     * @return Zwraca obiekt typu Wynik z zapisanymi modyfikacjami
     */
    public Wynik getWynik(int x, int y) {//Wywoływana po każdym wykonaniu ruchu
        komunikat = 0;//Komunikat zwracany po wykonaniu ruchu
        if (przeciwnik == Przeciwnik.OSOBA) {//Jeśli dwie osoby grają przy komputerze
            if (ruchNiedozwolony(x, y)) {//Jeśli wykonał ruch, który nie jest dozwolony
                komunikat = 8;//Ustawiamy odpowiedni komunikat
                return new Wynik(komunikat, licznikM, licznikK, !kolejka, x, y, punktyM, punktyK, przyrostPkt);//Zwracamy bieżące wartości
            } else {//Postaw kropkę na wybranym polu
                plansza[x][y].ustawKropke();
            }
            if (kolejka) {//Zwiększa odpowiedniemu graczowi licznik ruchów
                licznikM++;
            } else {
                licznikK++;
            }
            przyrostPkt = liczPunkty();//Liczy przyrost punktów po danym ruchu
            if (przyrostPkt != 0) {//Jeśli któryś gracz zdobył punkty
                if (kolejka) {//Jeśli kolejka gracza z myszą
                    punktyM += przyrostPkt;//Zwiększ punkty gracza z myszą
                    komunikat = 4;//Ustaw komunikat o zdobyciu punktów przez gracza
                } else {//Analogicznie dla drugiego gracza
                    punktyK += przyrostPkt;
                    komunikat = 3;
                }
            } else {//Gracz nie zdobył punktów
                if (kolejka) {
                    komunikat = 2;//Komunikat o tym kto następny wykonuje ruchy
                } else {
                    komunikat = 1;
                }
            }
            if (!ruchMozliwy()) {//Czy następny ruch jest możliwy
                ktoWygrywa();//Jeśli nie ustal zwycięzcę
                return new Wynik(komunikat, licznikM, licznikK, !kolejka, x, y, punktyM, punktyK, przyrostPkt);//Zwróć komunikat o zwycięstwie
            }
            przesunKolejke();//Zmień kolejkę gracza
            return new Wynik(komunikat, licznikM, licznikK, !kolejka, x, y, punktyM, punktyK, przyrostPkt);//Zwróć komunikat o następnym ruchu
        } else if (przeciwnik == Przeciwnik.KOMPUTER) {//Gra z komputerem
            if (kolejka) {
                //Ruch gracza
                if (ruchNiedozwolony(x, y)) {//Jeśli wykonał ruch, który nie jest dozwolony
                    komunikat = 8;//Ustawiamy odpowiedni komunikat
                    return new Wynik(komunikat, licznikM, licznikK, !kolejka, x, y, punktyM, punktyK, przyrostPkt);//Zwracamy bieżące wartości
                } else {//Postaw kropkę na wybranym polu
                    plansza[x][y].ustawKropke();
                }
                licznikM++;
                przyrostPkt = liczPunkty();//Liczy przyrost punktów po danym ruchu
                if (przyrostPkt != 0) {//Jeśli któryś gracz zdobył punkty
                    punktyM += przyrostPkt;//Zwiększ punkty gracza z myszą
                    komunikat = 4;
                } else {
                    komunikat = 2;
                }
                if (!ruchMozliwy()) {//Czy następny ruch jest możliwy
                    ktoWygrywa();//Jeśli nie ustal zwycięzcę
                    return new Wynik(komunikat, licznikM, licznikK, !kolejka, x, y, punktyM, punktyK, przyrostPkt);//Zwróć komunikat o zwycięstwie
                }
                przesunKolejke();//Zmień kolejkę gracza
                //Ruch komputera
                przyrostPkt = 0;
                si();//Sztuczna inteligencja
                licznikK++;
                przyrostPkt = liczPunkty();//Liczy przyrost punktów po dany ruchu
                if (przyrostPkt != 0) {//Jeśli któryś gracz zdobył punkty
                    punktyK += przyrostPkt;//Zwiększ punkty gracza z myszą
                    komunikat = 9;
                } else {
                    komunikat = 10;
                    x = ++ruchKomputeraX;
                    y = ++ruchKomputeraY;
                }
                if (!ruchMozliwy()) {//Czy następny ruch jest możliwy
                    ktoWygrywa();//Jeśli nie ustal zwycięzcę
                    return new Wynik(komunikat, licznikM, licznikK, !kolejka, x, y, punktyM, punktyK, przyrostPkt);//Zwróć komunikat o zwycięstwie
                }
                przesunKolejke();
                return new Wynik(komunikat, licznikM, licznikK, !kolejka, x, y, punktyM, punktyK, przyrostPkt);
            }
        } else {
            //Tu gra sieciowa
        }
        return null;//Błąd
    }

    /**
     * Kto rozpoczyna rozgrywkę
     *
     * @return Zwraca true - rozpoczyna gracz sterujący myszą, false - sterujący
     * klawiaturą
     */
    public boolean ktoryRozpoczyna() {
        return kolejka;
    }

    /**
     * Czy komputer wykonał ruch jako pierwszy gracz
     *
     * @return true - tak, false - nie
     */
    public boolean czyKomputerRozpoczyna() {
        return komputerZaczyna;
    }

    /**
     * Jeśli gramy z komputerem zwraca true
     *
     * @return true - tak, false - nie
     */
    public boolean graZKomputerem() {
        return przeciwnik == Przeciwnik.KOMPUTER;
    }

    /**
     * Zmienia kolejkę na następną
     */
    private void przesunKolejke() {
        kolejka = !kolejka;
    }

    private boolean ruchNiedozwolony(int x, int y) {
        //Czy można wykonać taki ruch
        return plansza[x][y].zwrocPrzycisk() != Przyciski.PUSTY;
    }

    private void ktoWygrywa() {//Kto wygrywa grę
        if (punktyK == punktyM) {
            komunikat = 7;
        } else if (punktyK < punktyM) {
            komunikat = 5;
        } else {
            if (przeciwnik == Przeciwnik.KOMPUTER) {
                komunikat = 11;
            } else {
                komunikat = 6;
            }
        }
    }

    private void si() {//Sztuczna interligencja - wykonuje ruch komputera
        int[] poziomaPrzerwa = new int[rozmiar];
        int[] pionowaPrzerwa = new int[rozmiar];
        int[] skosPlPrzerwa = new int[rozmiar];
        int licznik;
        int wierszMin = rozmiar;
        int kolumnaMin = rozmiar;
        int skosPlMin = rozmiar;
        int brakujaceW = rozmiar;
        int brakujaceK = rozmiar;
        int brakujaceSkosPl = rozmiar;
        int losowy;
        int punktySkos;
        int punktyPion;
        int punktyPoziom;

        for (int i = 0; i < rozmiar; i++) {//Liczenie pustych pól w poziomie
            licznik = 0;
            for (int j = 0; j < rozmiar; j++) {
                if (j < rozmiar - i) {
                    if (plansza[i][j].zwrocPrzycisk() == Przyciski.PUSTY) {
                        licznik++;
                    }
                }
            }
            poziomaPrzerwa[i] = licznik;
        }

        for (int i = 0; i < rozmiar; i++) {//Liczenie pustych pól w pionie
            licznik = 0;
            for (int j = 0; j < rozmiar; j++) {
                if (j < rozmiar - i) {
                    if (plansza[j][i].zwrocPrzycisk() == Przyciski.PUSTY) {
                        licznik++;
                    }
                }
            }
            pionowaPrzerwa[i] = licznik;
        }

        for (int i = 0; i < rozmiar; i++) {//Liczenie pustych pól po skosie "z prawej na lewą"
            licznik = 0;
            for (int j = 0; j <= i; j++) {
                if (i == 0 && j == 0) {
                    break;
                }
                if (plansza[j][i - j].zwrocPrzycisk() == Przyciski.PUSTY) {
                    licznik++;
                }
            }
            skosPlPrzerwa[i] = licznik;
        }

        //Wybieranie minimum pustych pól z wierszy
        for (int i = 0; i < poziomaPrzerwa.length; i++) {
            int j = poziomaPrzerwa[i];
            if (j != 0) {
                if (j < brakujaceW) {
                    brakujaceW = j;
                    wierszMin = i;
                }
            }
        }
        //Wybieranie minimum pustych pól z kolumn
        for (int i = 0; i < pionowaPrzerwa.length; i++) {
            int j = pionowaPrzerwa[i];
            if (j != 0) {
                if (j < brakujaceK) {
                    brakujaceK = j;
                    kolumnaMin = i;
                }
            }
        }
        //Wybieranie minimum pustych pól ze skosu "z prawej na lewą"
        for (int i = 0; i < skosPlPrzerwa.length; i++) {
            int j = skosPlPrzerwa[i];
            if (j != 0) {
                if (j <= brakujaceSkosPl) {
                    brakujaceSkosPl = j;
                    skosPlMin = i;
                }
            }
        }

        //System.out.println("");
        //System.out.println("Wykonaj ruch");
        //System.out.println("Pion brakuje " + brakujaceK + " można zdobyć " + (rozmiar - kolumnaMin));
        //System.out.println("Poziom brakuje " + brakujaceW + " można zdobyć " + (rozmiar - wierszMin));
        //System.out.println("Skos brakuje " + brakujaceSkosPl + " można zdobyć " + (skosPlMin + 1));
        //Ustalanie punktów
        punktyPion = rozmiar - kolumnaMin;
        punktyPoziom = rozmiar - wierszMin;
        punktySkos = skosPlMin + 1;

        //Wybór ruchu
        if (brakujaceK > 1 && brakujaceW > 1 && brakujaceSkosPl == 1) {//Jeśli po skosie brakuje 1 elementu
            do {//Wykonaj ruch po skosie
                losowy = rand.nextInt(rozmiar);
            } while (losowy > skosPlMin || plansza[losowy][skosPlMin - losowy].zwrocPrzycisk() != Przyciski.PUSTY);
            plansza[losowy][skosPlMin - losowy].ustawKropke();
            return;
        } else if ((brakujaceK == 1 || brakujaceW == 1) && brakujaceSkosPl == 1) {//Jeśli w pionie lub w poziomie brakuje elementu i po skosie też
            if (punktySkos >= punktyPion || punktySkos >= punktyPoziom) {//Więcej punktów można zdobyć za skos
                do {//Wykonaj ruch po skosie
                    losowy = rand.nextInt(rozmiar);
                } while (losowy > skosPlMin || plansza[losowy][skosPlMin - losowy].zwrocPrzycisk() != Przyciski.PUSTY);
                plansza[losowy][skosPlMin - losowy].ustawKropke();
                return;
            }
        }

        //Nie wykonano ruchu po skosie, tyle samo pustych elementów w pionie i w poziomie
        if (brakujaceK == brakujaceW) {//Tyle samo brakujących elementów
            if (punktyPion > punktyPoziom) {//Więcej punktów za kolumnę
                brakujaceW++;
            } else if (punktyPion < punktyPoziom) {//Więcej punktów za wiersz
                brakujaceK++;
            } else {//Tyle samo punktów
                if (rand.nextBoolean()) {//Losowe przydzielanie
                    brakujaceW++;
                } else {
                    brakujaceK++;
                }
            }
        }

        //Wykonuje ruch w kolumnie lub w wierszu
        if (brakujaceK < brakujaceW) {//losujemy w kolumnie
            do {
                losowy = rand.nextInt(rozmiar);
            } while (kolumnaMin >= rozmiar - losowy || plansza[losowy][kolumnaMin].zwrocPrzycisk() != Przyciski.PUSTY);
            plansza[losowy][kolumnaMin].ustawKropke();
            ruchKomputeraX = losowy;
            ruchKomputeraY = kolumnaMin;
        } else {//losujemy w wierszu
            do {
                losowy = rand.nextInt(rozmiar);
            } while (losowy >= rozmiar - wierszMin || plansza[wierszMin][losowy].zwrocPrzycisk() != Przyciski.PUSTY);
            plansza[wierszMin][losowy].ustawKropke();
            ruchKomputeraX = wierszMin;
            ruchKomputeraY = losowy;
        }
    }

    private boolean ruchMozliwy() {//Sprawdza czy wszystkie pola zostały kliknięte
        for (int i = 0; i < rozmiar; i++) {
            for (int j = 0; j < rozmiar; j++) {
                if (j < rozmiar - i) {
                    if (plansza[i][j].zwrocPrzycisk() == Przyciski.PUSTY) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int liczPunkty() {//Liczy zdobyte punkty
        int pom = 0;
        pom += liczWiersze();
        pom += liczKolumny();
        pom += skosPl();
        pom += skosLp1();
        pom += skosLp2();
        return pom;
    }

    private int liczWiersze() {//Liczy punkty w wierszach
        boolean linia;
        for (int i = 0; i < rozmiar; i++) {//Poziomo
            linia = true;
            if (plansza[i][0].zwrocPoziom()) {//W tym wierszu już jest linia
                linia = false;
                continue;//W tym wierszu już jest poziome skreślenie
            }
            for (int j = 0; j < rozmiar; j++) {
                if (j < rozmiar - i) {
                    if (i == 9) {
                        linia = false;//Nie sprawdzam ostatniego wiersza
                        break;
                    }
                    if (plansza[i][j].zwrocPrzycisk() == Przyciski.PUSTY) {
                        linia = false;
                        break;//W tym wierszu nie będzie linii
                    }
                }
            }
            if (linia) {//Przyznanie punktów
                skreslLinie(i);
                return rozmiar - i;
            }
        }
        return 0;
    }

    private int liczKolumny() {//Liczy punkty w kolumnach
        boolean linia;
        for (int i = 0; i < rozmiar; i++) {//Pionowo
            linia = true;
            if (plansza[0][i].zwrocPion()) {//W tej kolumnie jest już linia
                linia = false;
                continue;//Już skreślone
            }
            for (int j = 0; j < rozmiar; j++) {
                if (j < rozmiar - i) {
                    if (i == 9) {
                        linia = false;//Nie sprawdzam ostatniej kolumny
                        break;
                    }
                    if (plansza[j][i].zwrocPrzycisk() == Przyciski.PUSTY) {
                        linia = false;
                        break;
                    }
                }
            }
            if (linia) {//Przyznanie punktów
                skreslKolumne(i);
                return rozmiar - i;
            }
        }
        return 0;
    }

    private int skosPl() {//Liczenie punktów po skosie
        boolean linia;
        for (int i = 0; i < rozmiar; i++) {
            linia = true;
            if (plansza[0][i].zwrocSkosPl()) {//Już skreślone
                linia = false;
                continue;
            }
            for (int j = 0; j <= i; j++) {
                if (i == 0 && j == 0) {
                    linia = false; //Nie sprawdzam elementu 0,0
                    break;
                }
                if (plansza[j][i - j].zwrocPrzycisk() == Przyciski.PUSTY) {
                    linia = false;
                    break;//Tu nie można skreślić
                }
            }
            if (linia) {//Liczenie punktów
                skreslPl(i);
                return i + 1;
            }
        }
        return 0;
    }

    private int skosLp1() {//Liczenie punktów po skosie
        boolean linia;
        int j;
        for (int i = 0; i < rozmiar; i++) {
            linia = true;
            if (plansza[i][0].zwrocSkosLp()) {//Tu już jest skreślenie
                linia = false;
                continue;
            }
            for (j = 0; j < rozmiar; j++) {
                if (i > 7) {
                    linia = false; //Nie sprawdzam jednoelementowych skosów
                    break;
                }
                if (j < rozmiar - (i + j)) {
                    if (plansza[i + j][j].zwrocPrzycisk() == Przyciski.PUSTY) {
                        linia = false;
                        break;//Tu nie będzie linii
                    }
                }
            }
            if (linia) {//Przyznawanie punktów
                skreslLpDol(i, j);
                if (i == 7 || i == 6) {
                    return 2;
                }
                if (i == 5 || i == 4) {
                    return 3;
                }
                if (i == 3 || i == 2) {
                    return 4;
                }
                if (i == 1 || i == 0) {
                    return 5;
                }
            }
        }
        return 0;
    }

    private int skosLp2() {
        boolean linia;
        int j;
        for (int i = 0; i < rozmiar; i++) {//Skos
            linia = true;
            if (plansza[0][i].zwrocSkosLp()) {//Już jest linia
                linia = false;
                continue;
            }
            for (j = 0; j < rozmiar; j++) {
                if (i > 7) {
                    linia = false; //Nie sprawdzam jednoelementowych skosów
                    break;
                }
                if (i == 0) {
                    linia = false;
                    break;//Już skreślone
                }
                if ((i + j) < rozmiar - j) {
                    if (plansza[j][i + j].zwrocPrzycisk() == Przyciski.PUSTY) {
                        linia = false;
                        break;//Tu nie można skreślić
                    }
                }
            }
            if (linia) {//Przyznawanie punktów
                skreslLpGora(i, j);
                if (i == 7 || i == 6) {
                    return 2;
                }
                if (i == 5 || i == 4) {
                    return 3;
                }
                if (i == 3 || i == 2) {
                    return 4;
                }
                if (i == 1) {
                    return 5;
                }
            }
        }
        return 0;
    }

    private void skreslLinie(int i) {//Skreśla linię podaną w argumencie
        for (int j = 0; j < rozmiar - i; j++) {
            plansza[i][j].ustawPoziom();
        }
    }

    private void skreslKolumne(int i) {//Skreśla kolumnę podaną w argumencie
        for (int j = 0; j < rozmiar - i; j++) {
            plansza[j][i].ustawPion();
        }
    }

    private void skreslPl(int i) {//Skreśla skos "z prawej na lewą"
        for (int j = 0; j <= i; j++) {
            plansza[j][i - j].ustawSkospl();
        }
    }

    private void skreslLpDol(int i, int j) {//Skreśla skos "z lewej na prawą" część poniżej przekątnej
        for (int k = j; k >= 0; k--) {
            if ((i + j - k) < rozmiar - (j - k)) {
                plansza[i + j - k][j - k].ustawSkoslp();
            }
        }
    }

    private void skreslLpGora(int i, int j) {//Skreśla skos "z lewej na prawą" część powyżej przekątnej
        for (int k = j; k >= 0; k--) {
            if ((i + j - k) < rozmiar - (j - k)) {
                plansza[j - k][i + j - k].ustawSkoslp();
            }
        }
    }
}
