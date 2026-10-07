package ch.tbz.m450.service;

import ch.tbz.m450.repository.Address;
import ch.tbz.m450.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceTest {

    private AddressRepository repo;
    private AddressService service;
    private Address meier;
    private Address zbinden;

    @BeforeEach
    void setUp() {
        repo = mock(AddressRepository.class);
        service = new AddressService(repo);
        meier = new Address(1, "Hans", "Meier", "0791111111", new Date());
        zbinden = new Address(2, "Anna", "Zbinden", "0792222222", new Date());
    }

    @Test
    void saveReturnsSavedAddressAndCallsRepository() {
        when(repo.save(meier)).thenReturn(meier);

        Address result = service.save(meier);

        assertSame(meier, result);
        verify(repo).save(meier);
    }

    @Test
    void getAllReturnsAddressesSortedByLastname() {
        when(repo.findAll()).thenReturn(List.of(zbinden, meier));

        List<Address> result = service.getAll();

        assertEquals(2, result.size());
        assertSame(meier, result.get(0));
        assertSame(zbinden, result.get(1));
    }

    @Test
    void getAddressFound() {
        when(repo.findById(1)).thenReturn(Optional.of(meier));

        Optional<Address> result = service.getAddress(1);

        assertTrue(result.isPresent());
        assertSame(meier, result.get());
    }

    @Test
    void getAddressNotFound() {
        when(repo.findById(99)).thenReturn(Optional.empty());

        Optional<Address> result = service.getAddress(99);

        assertTrue(result.isEmpty());
    }
}
