package kartofel.logika;

import java.util.Random;
import kartofel.klasyWyliczeniowe.Przeciwnik;
import kartofel.klasyWyliczeniowe.Przyciski;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Klasa testująca logikę programu
 * @author Kamil Burzyński
 */
public class LogikaTest {

    /**
     * Konstruktor (nie robi nic)
     */
    public LogikaTest() {
    }
    

    /**
     * Testuje metodę getPlansza z klasy Logika.
     */
    @Test
    public void testGetPlansza() {
        System.out.println("Test getPlansza rozpoczęty");
        Logika instance = new Logika(Przeciwnik.OSOBA, true, 10, new Random());
        Przyciski p = Przyciski.PUSTY;
        Przyciski k = Przyciski.KROPKA;
        Przyciski n = null;
        Przyciski[][] expResult = {
            {p, p, p, p, p, p, p, p, p, k},
            {p, p, p, p, p, p, p, p, p, n},
            {p, p, p, p, p, p, p, p, n, n},
            {p, p, p, p, p, p, p, n, n, n},
            {p, p, p, p, p, p, n, n, n, n},
            {p, p, p, p, p, n, n, n, n, n},
            {p, p, p, p, n, n, n, n, n, n},
            {p, p, p, n, n, n, n, n, n, n},
            {p, p, n, n, n, n, n, n, n, n},
            {k, n, n, n, n, n, n, n, n, n}
        };
        Przyciski[][] result = instance.getPlansza();
        assertArrayEquals(expResult, result);
        
        Logika instance2 = new Logika(Przeciwnik.OSOBA, true, 3, new Random());
        Przyciski[][] expResult2 = {
            {p, p, p},
            {p, p, n},
            {p, n, n}
        };
        Przyciski[][] result2 = instance2.getPlansza();
        assertArrayEquals(expResult2, result2);
        System.out.println("Test getPlansza zakończony");
    }

    /**
     * Testuje metodę getWynik z klasy logika Logika.
     */
    @Test
    public void testGetWynik() {
        System.out.println("Test getWynik rozpoczęty");
        Logika instance = new Logika(Przeciwnik.OSOBA, true, 10, new Random());
        instance.getWynik(0, 0);//Symulacja ruchów użytkowników
        instance.getWynik(1, 0);
        instance.getWynik(2, 0);
        instance.getWynik(3, 0);
        instance.getWynik(4, 0);
        instance.getWynik(5, 0);
        instance.getWynik(6, 0);
        instance.getWynik(7, 0);
        Wynik result = instance.getWynik(8, 0);
        assertEquals(4, result.komunikat);
        assertEquals(5, result.ruchyMysz);
        assertEquals(4, result.ruchyKlaw);
        assertEquals(true, result.klawiatura);
        assertEquals(8, result.popx);
        assertEquals(0, result.popy);
        assertEquals(10, result.punktyMysz);
        assertEquals(0, result.punktyKlaw);
        assertEquals(10, result.zdobyte);

        Wynik result1 = instance.getWynik(8, 0);
        assertEquals(8, result1.komunikat);

        Przyciski result2[][] = instance.getPlansza();
        Przyciski p = Przyciski.PUSTY;
        Przyciski pi = Przyciski.PION;
        Przyciski k = Przyciski.KROPKA;
        Przyciski n = null;
        Przyciski[][] expResult = {
            {pi, p, p, p, p, p, p, p, p, k},
            {pi, p, p, p, p, p, p, p, p, n},
            {pi, p, p, p, p, p, p, p, n, n},
            {pi, p, p, p, p, p, p, n, n, n},
            {pi, p, p, p, p, p, n, n, n, n},
            {pi, p, p, p, p, n, n, n, n, n},
            {pi, p, p, p, n, n, n, n, n, n},
            {pi, p, p, n, n, n, n, n, n, n},
            {pi, p, n, n, n, n, n, n, n, n},
            {pi, n, n, n, n, n, n, n, n, n}
        };
        assertArrayEquals(expResult, result2);
        System.out.println("Test getWynik zakończony");
    }

    /**
     * Testuje metodę ktoryRozpoczyna z klasy Logika.
     */
    @Test
    public void testKtoryRozpoczyna() {
        System.out.println("Test ktoryRozpoczyna rozpoczęty");
        Logika instance = new Logika(Przeciwnik.OSOBA, true, 10, new Random());
        boolean expResult = true;
        boolean result = instance.ktoryRozpoczyna();
        assertEquals(expResult, result);

        Logika instance2 = new Logika(Przeciwnik.OSOBA, false, 10, new Random());
        boolean expResult2 = false;
        boolean result2 = instance2.ktoryRozpoczyna();
        assertEquals(expResult2, result2);
        System.out.println("Test ktoryRozpoczyna zakończony");
    }

    /**
     * Testuje metodę czyKomputerRozpoczyna z klasy Logika.
     */
    @Test
    public void testCzyKomputerRozpoczyna() {
        System.out.println("Test czyKomputerRozpoczyna rozpoczęty");
        Logika instance = new Logika(Przeciwnik.OSOBA, true, 10, new Random());
        boolean expResult = false;
        boolean result = instance.czyKomputerRozpoczyna();
        assertEquals(expResult, result);

        Logika instance2 = new Logika(Przeciwnik.OSOBA, false, 10, new Random());
        boolean expResult2 = false;
        boolean result2 = instance2.czyKomputerRozpoczyna();
        assertEquals(expResult2, result2);

        Logika instance3 = new Logika(Przeciwnik.KOMPUTER, true, 10, new Random());
        boolean expResult3 = false;
        boolean result3 = instance3.czyKomputerRozpoczyna();
        assertEquals(expResult3, result3);

        Logika instance4 = new Logika(Przeciwnik.KOMPUTER, false, 10, new Random());
        boolean expResult4 = true;
        boolean result4 = instance4.czyKomputerRozpoczyna();
        assertEquals(expResult4, result4);
        System.out.println("Test czyKomputerRozpoczyna zakończony");
    }

    /**
     * Testuje metodę graZKomputerem z klasy Logika.
     */
    @Test
    public void testgraZKomputerem() {
        System.out.println("Test graZKomputerem rozpoczęty");
        Logika instance = new Logika(Przeciwnik.OSOBA, false, 10, new Random());
        boolean expResult = false;
        boolean result = instance.graZKomputerem();
        assertEquals(expResult, result);

        Logika instance2 = new Logika(Przeciwnik.KOMPUTER, false, 10, new Random());
        boolean expResult2 = true;
        boolean result2 = instance2.graZKomputerem();
        assertEquals(expResult2, result2);
        System.out.println("Test graZKomputerem zakończony");
    }
}
