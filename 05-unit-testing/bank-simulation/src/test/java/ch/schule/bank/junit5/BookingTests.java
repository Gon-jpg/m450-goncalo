package ch.schule.bank.junit5;

import ch.schule.Booking;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingTests {

    @Test
    void getDateReturnsConstructorValue() {
        Booking booking = new Booking(100, 500);

        assertEquals(100, booking.getDate());
    }

    @Test
    void getAmountReturnsConstructorValue() {
        Booking booking = new Booking(100, 500);

        assertEquals(500, booking.getAmount());
    }
}
