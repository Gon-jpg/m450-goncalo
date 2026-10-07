package ch.tbz.m450.util;

import ch.tbz.m450.repository.Address;

import java.util.Comparator;

public class AddressComparator implements Comparator<Address> {

    private static final Comparator<String> NULLS_FIRST = Comparator.nullsFirst(Comparator.<String>naturalOrder());

    private static final Comparator<Address> ORDER = Comparator
            .comparing(Address::getLastname, NULLS_FIRST)
            .thenComparing(Address::getFirstname, NULLS_FIRST)
            .thenComparingInt(Address::getId);

    @Override
    public int compare(Address a1, Address a2) {
        return ORDER.compare(a1, a2);
    }
}
