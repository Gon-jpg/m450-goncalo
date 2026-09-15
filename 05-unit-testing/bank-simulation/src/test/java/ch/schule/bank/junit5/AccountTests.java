package ch.schule.bank.junit5;

import ch.schule.Account;
import ch.schule.Booking;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountTests {

    static class GenericAccount extends Account {
        GenericAccount(String id) {
            super(id);
        }
    }

    @Test
    void constructorSetsIdAndZeroBalance() {
        GenericAccount konto = new GenericAccount("XX-42");

        assertEquals("XX-42", konto.getId());
        assertEquals(0, konto.getBalance());
    }

    @Test
    void depositNegativeAmountIsRejected() {
        GenericAccount konto = new GenericAccount("XX-42");

        boolean ok = konto.deposit(1, -50);

        assertFalse(ok);
        assertEquals(0, konto.getBalance());
    }

    @Test
    void withdrawValidAmountDecreasesBalance() {
        GenericAccount konto = new GenericAccount("XX-42");
        konto.deposit(1, 100);

        boolean ok = konto.withdraw(2, 40);

        assertTrue(ok);
        assertEquals(60, konto.getBalance());
    }

    @Test
    void withdrawNegativeAmountIsRejected() {
        GenericAccount konto = new GenericAccount("XX-42");
        konto.deposit(1, 100);

        boolean ok = konto.withdraw(2, -50);

        assertFalse(ok);
        assertEquals(100, konto.getBalance());
    }

    @Test
    void withdrawEarlierThanLastBookingIsRejected() {
        GenericAccount konto = new GenericAccount("XX-42");
        konto.deposit(10, 100);

        boolean ok = konto.withdraw(5, 30);

        assertFalse(ok);
        assertEquals(100, konto.getBalance());
    }

    @Test
    void printDoesNotThrow() {
        GenericAccount konto = new GenericAccount("XX-42");
        konto.deposit(1, 100);

        assertDoesNotThrow(() -> konto.print());
        assertDoesNotThrow(() -> konto.print(1970, 1));
    }

    @Test
    void getSetBookingStoresValue() {
        GenericAccount konto = new GenericAccount("XX-42");
        Booking b = new Booking(1, 50);

        konto.setBooking(b);

        assertSame(b, konto.getBooking());
    }

    @Test
    void printByMonthSkipsBookingBeforeStartDate() {
        GenericAccount konto = new GenericAccount("XX-42");
        konto.deposit(-100, 50);

        assertDoesNotThrow(() -> konto.print(1970, 1));
    }

    @Test
    void printByMonthStopsAtEndDate() {
        GenericAccount konto = new GenericAccount("XX-42");
        konto.deposit(10, 50);
        konto.deposit(40, 20);

        assertDoesNotThrow(() -> konto.print(1970, 1));
    }

    @Test
    void getBookingDefaultsToNull() {
        GenericAccount konto = new GenericAccount("XX-42");

        assertNull(konto.getBooking());
    }
}
