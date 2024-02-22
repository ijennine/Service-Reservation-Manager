package fr.mereo.services;

import fr.mereo.models.Slot;
import fr.mereo.models.SlotList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ReservationManagerServiceTest {

    private final ReservationManagerService<Integer> reservationManagerService = new ReservationManagerService<>();

    @Test
    void givenOffers_thenReturnGlobalOffer() {
        SlotList<Integer> offer1 = new SlotList<>();
        offer1.addSlot(new Slot<>(0, 2, 1));
        offer1.addSlot(new Slot<>(3, 4, 2));

        SlotList<Integer> offer2 = new SlotList<>();
        offer2.addSlot(new Slot<>(5, 6, 3));
        offer2.addSlot(new Slot<>(1, 3, 2));

        SlotList<Integer> expected = new SlotList<>();
        expected.addSlot(new Slot<>(0, 1, 1));
        expected.addSlot(new Slot<>(1, 2, 3));
        expected.addSlot(new Slot<>(2, 4, 2));
        expected.addSlot(new Slot<>(5, 6, 3));

        SlotList<Integer> actual = reservationManagerService.combineSlots(offer1, offer2);

        assertEquals(actual, expected);
    }

    @ParameterizedTest
    @MethodSource("parameters")
    void givenGlobalOfferThenReturnIfDemandCanBeSatisfied(SlotList<Integer> demand, boolean expected) {
        SlotList<Integer> globalOffer = new SlotList<>();
        globalOffer.addSlot(new Slot<>(0, 1, 1));
        globalOffer.addSlot(new Slot<>(1, 2, 3));
        globalOffer.addSlot(new Slot<>(2, 4, 2));
        globalOffer.addSlot(new Slot<>(5, 6, 3));

        assertEquals(reservationManagerService.demandCanBeSatisfied(demand, globalOffer), expected);
    }

    static Stream<Arguments> parameters() {
        SlotList<Integer> demand1 = new SlotList<>();
        demand1.addSlot(new Slot<>(5, 6, 3));
        demand1.addSlot(new Slot<>(1, 3, 2));

        SlotList<Integer> demand2 = new SlotList<>();
        demand2.addSlot(new Slot<>(1, 2, 3));
        demand2.addSlot(new Slot<>(4, 6, 1));

        SlotList<Integer> demand3 = new SlotList<>();
        demand3.addSlot(new Slot<>(0, 2, 2));
        demand3.addSlot(new Slot<>(5, 6, 5));

        return Stream.of(
                Arguments.of(demand1, true),
                Arguments.of(demand2, false),
                Arguments.of(demand3, false)
        );
    }
}