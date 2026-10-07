package ch.tbz.m450.util;

import ch.tbz.m450.repository.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AddressComparatorTest {

    private AddressComparator comparator;
    private Address meier;
    private Address zbinden;

    @BeforeEach
    void setUp() {
        comparator = new AddressComparator();
        meier = new Address(1, "Hans", "Meier", "0791111111", new Date());
        zbinden = new Address(2, "Anna", "Zbinden", "0792222222", new Date());
    }

    @Test
    void firstAddressComesFirst() {
        assertTrue(comparator.compare(meier, zbinden) < 0);
    }

    @Test
    void secondAddressComesFirst() {
        assertTrue(comparator.compare(zbinden, meier) > 0);
    }

    @Test
    void sameLastnameSortsByFirstname() {
        Address peterMeier = new Address(3, "Peter", "Meier", "0793333333", new Date());

        assertTrue(comparator.compare(meier, peterMeier) < 0);
    }

    @Test
    void sameLastnameSortsByFirstnameReversed() {
        Address peterMeier = new Address(3, "Peter", "Meier", "0793333333", new Date());

        assertTrue(comparator.compare(peterMeier, meier) > 0);
    }

    @Test
    void sameLastAndFirstnameSortsById() {
        Address sameNameHigherId = new Address(5, "Hans", "Meier", "0795555555", new Date());

        assertTrue(comparator.compare(meier, sameNameHigherId) < 0);
        assertTrue(comparator.compare(sameNameHigherId, meier) > 0);
    }

    @Test
    void identicalAddressesReturnZero() {
        Address copy = new Address(1, "Hans", "Meier", "0791111111", new Date());

        assertEquals(0, comparator.compare(meier, copy));
    }

    @Test
    void nullLastnameSortsFirst() {
        Address noLastname = new Address(4, "Max", null, "0794444444", new Date());

        assertTrue(comparator.compare(noLastname, meier) < 0);
        assertTrue(comparator.compare(meier, noLastname) > 0);
    }

    @Test
    void nullFirstnameSortsFirstWithSameLastname() {
        Address noFirstname = new Address(6, null, "Meier", "0796666666", new Date());

        assertTrue(comparator.compare(noFirstname, meier) < 0);
        assertTrue(comparator.compare(meier, noFirstname) > 0);
    }
}
