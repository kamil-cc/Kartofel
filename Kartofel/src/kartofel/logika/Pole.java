package kartofel.logika;

import kartofel.klasyWyliczeniowe.Przyciski;

/**
 * Klasa przechowyje pojedyncze pole z planszy i wszystkie wartości z nim związane
 * @author Kamil Burzyński
 */
public class Pole {

    private boolean kropka;
    private boolean pion;
    private boolean poziom;
    private boolean skoslp;
    private boolean skospl;

    /**
     * Tworzy nowe pole, wszystkie skreślenia ustawia jako false
     */
    public Pole() {
        kropka = false;
        pion = false;
        poziom = false;
        skoslp = false;
        skospl = false;
    }

    /**
     * Zwraca stałą definiującą rodzaj pola (jest 18 rodzajów)
     * @return Stała z klasy Przyciski
     */
    public Przyciski zwrocPrzycisk() {
        if (kropka) {
            if (pion == false && poziom == false && skoslp == false && skospl == false) {
                return Przyciski.KROPKA;
            }
            if (pion == true && poziom == false && skoslp == false && skospl == false) {
                return Przyciski.PION;
            }
            if (pion == false && poziom == true && skoslp == false && skospl == false) {
                return Przyciski.POZIOM;
            }
            if (pion == true && poziom == true && skoslp == false && skospl == false) {
                return Przyciski.PIONPOZIOM;
            }
            if (pion == false && poziom == false && skoslp == true && skospl == false) {
                return Przyciski.SKOSLP;
            }
            if (pion == false && poziom == false && skoslp == false && skospl == true) {
                return Przyciski.SKOSPL;
            }
            if (pion == false && poziom == false && skoslp == true && skospl == true) {
                return Przyciski.SKOSLPPL;
            }
            if (pion == false && poziom == true && skoslp == true && skospl == false) {
                return Przyciski.POZIOMSKOSLP;
            }
            if (pion == false && poziom == true && skoslp == false && skospl == true) {
                return Przyciski.POZIOMSKOSPL;
            }
            if (pion == false && poziom == true && skoslp == true && skospl == true) {
                return Przyciski.POZIOMSKOSLPPL;
            }
            if (pion == true && poziom == false && skoslp == true && skospl == false) {
                return Przyciski.PIONSKOSLP;
            }
            if (pion == true && poziom == false && skoslp == false && skospl == true) {
                return Przyciski.PIONSKOSPL;
            }
            if (pion == true && poziom == false && skoslp == true && skospl == true) {
                return Przyciski.PIONSKOSLPPL;
            }
            if (pion == true && poziom == true && skoslp == true && skospl == false) {
                return Przyciski.PIONPOZIOMSKOSLP;
            }
            if (pion == true && poziom == true && skoslp == false && skospl == true) {
                return Przyciski.PIONPOZIOMSKOSPL;
            }
            if (pion == true && poziom == true && skoslp == true && skospl == true) {
                return Przyciski.PIONPOZIOMSKOSLPPL;
            }
        }
        return Przyciski.PUSTY;
    }

    /**
     * Ustawia kropkę na danym polu
     */
    public void ustawKropke() {
        kropka = true;
    }

    /**
     * Ustawia skreślenie poziome na danym polu
     */
    public void ustawPoziom() {
        poziom = true;
    }

    /**
     * Ustawia skreślenie pionowe na danym polu
     */
    public void ustawPion() {
        pion = true;
    }

    /**
     * Ustawia skreślenie "z lewej na prawą" na danym polu
     */
    public void ustawSkoslp() {
        skoslp = true;
    }

    /**
     * Ustawia skreślenie "z prawej na lewą" na danym polu
     */
    public void ustawSkospl() {
        skospl = true;
    }

    /**
     * Zwraca true jeśli pole jest skreślone pionowo
     * @return true - tak, false - nie
     */
    public boolean zwrocPion() {
        return pion;
    }

    /**
     * Zwraca true jeśli pole jest skreślone poziomo
     * @return true - tak, false - nie
     */
    public boolean zwrocPoziom() {
        return poziom;
    }

    /**
     * Zwraca true jeśli pole jest skreślone "z prawej na lewą"
     * @return true - tak, false - nie
     */
    public boolean zwrocSkosPl() {
        return skospl;
    }

    /**
     * Zwraca true jeśli pole jest skreślone "z lewej na prawą"
     * @return true - tak, false - nie
     */
    public boolean zwrocSkosLp() {
        return skoslp;
    }
}
