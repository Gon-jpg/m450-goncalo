package ch.tbz.m450.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    private Address address;
    private Date registrationDate;

    @BeforeEach
    void setUp() {
        registrationDate = new Date();
        address = new Address(1, "Max", "Muster", "0791234567", registrationDate);
    }

    @Test
    void constructorStoresAllValues() {
        assertEquals(1, address.getId());
        assertEquals("Max", address.getFirstname());
        assertEquals("Muster", address.getLastname());
        assertEquals("0791234567", address.getPhonenumber());
        assertEquals(registrationDate, address.getRegistrationDate());
    }

    @Test
    void setterChangesValue() {
        address.setFirstname("Anna");

        assertEquals("Anna", address.getFirstname());
        assertEquals("Muster", address.getLastname());
    }

    @Test
    void noArgsConstructorLeavesFieldsEmpty() {
        Address empty = new Address();

        assertEquals(0, empty.getId());
        assertNull(empty.getFirstname());
    }
}
