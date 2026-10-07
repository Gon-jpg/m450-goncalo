package ch.tbz.m450.controller;

import ch.tbz.m450.repository.Address;
import ch.tbz.m450.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressControllerTest {

    private AddressService service;
    private AddressController controller;
    private Address meier;
    private Address zbinden;

    @BeforeEach
    void setUp() {
        service = mock(AddressService.class);
        controller = new AddressController(service);
        meier = new Address(1, "Hans", "Meier", "0791111111", new Date());
        zbinden = new Address(2, "Anna", "Zbinden", "0792222222", new Date());
    }

    @Test
    void createAddressReturns201AndSavedAddress() {
        when(service.save(meier)).thenReturn(meier);

        ResponseEntity<Address> response = controller.createAddress(meier);

        assertEquals(201, response.getStatusCode().value());
        assertSame(meier, response.getBody());
    }

    @Test
    void getAddressesReturns200AndList() {
        List<Address> addresses = List.of(meier, zbinden);
        when(service.getAll()).thenReturn(addresses);

        ResponseEntity<List<Address>> response = controller.getAddresses();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(addresses, response.getBody());
    }

    @Test
    void getAddressFoundReturns200AndAddress() {
        when(service.getAddress(1)).thenReturn(Optional.of(meier));

        ResponseEntity<Address> response = controller.getAddress(1);

        assertEquals(200, response.getStatusCode().value());
        assertSame(meier, response.getBody());
    }

    @Test
    void getAddressNotFoundReturns404AndNoBody() {
        when(service.getAddress(99)).thenReturn(Optional.empty());

        ResponseEntity<Address> response = controller.getAddress(99);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }
}
