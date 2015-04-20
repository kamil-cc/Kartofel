package kartofel.logika;

/**
 * Instancja klasy jest zwracana po wykonaniu ruchu, zawiera wszystkie zmiany po kliknięciu przycisku
 * @author Kamil Burzyński
 */
public class Wynik {

    /**
     * Numer komunikatu
     */
    public int komunikat;
    /**
     * Ilość ruchów wykonanych przez gracza myszą
     */
    public int ruchyMysz;
    /**
     * Ilość ruchów wykonanych przez gracza klawiaturą
     */
    public int ruchyKlaw;
    /**
     * Punkty zdobyte przez grającego myszą
     */
    public int punktyMysz;
    /**
     * Punkty zdobyte przez grającego klawiaturą
     */
    public int punktyKlaw;
    /**
     * Sterowanie klawiaturą: true - można sterować klawiszami, false - nie można
     */
    public boolean klawiatura;
    /**
     * Pozycja x poprzedniego kliknięcia
     */
    public int popx;
    /**
     * Pozycja y poprzedniego kliknięcia
     */
    public int popy;
    /**
     * Przyrost punktów po wykonaniu ruchu
     */
    public int zdobyte;

    /**
     * Tworzy instancję klasy zawierającej wszystkie parametry podane w argumentach
     * @param kom Numer komunikatu
     * @param ruchM Ilość ruchów gracza posługującego się myszą
     * @param ruchK Ilość ruchów gracza posługującego się klawiaturą
     * @param klaw Kolejka sterowania
     * @param px Współrzędna X klikniętego przycisku
     * @param py Współrzędna Y klikniętego przycisku
     * @param pktM Ilość punktów gracza posługującego się myszą
     * @param pktK Ilość punktów gracza posługującego się klawiaturą
     * @param przyrost Przyrost punktów po wykonaniu konkretnego ruchu
     */
    public Wynik(int kom, int ruchM, int ruchK, boolean klaw, int px, int py, int pktM, int pktK, int przyrost) {
        komunikat = kom;
        ruchyMysz = ruchM;
        ruchyKlaw = ruchK;
        klawiatura = klaw;
        popx = px;
        popy = py;
        punktyMysz = pktM;
        punktyKlaw = pktK;
        zdobyte = przyrost;
    }
}
