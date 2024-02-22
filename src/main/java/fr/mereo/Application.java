package fr.mereo;

import fr.mereo.models.SlotList;
import fr.mereo.models.Slot;
import fr.mereo.services.ReservationManagerService;

public class Application {

    public static void main(String[] args) {

        ReservationManagerService<Integer> reservationManagerService = new ReservationManagerService<>();

        SlotList<Integer> offer1 = new SlotList<>();
        offer1.addSlot(new Slot<>(0, 2, 1));
        offer1.addSlot(new Slot<>(3, 4, 2));

        SlotList<Integer> offer2 = new SlotList<>();
        offer2.addSlot(new Slot<>(1, 3, 2));
        offer2.addSlot(new Slot<>(5, 6, 3));

        SlotList<Integer> globalOffer = reservationManagerService.combineSlots(offer1, offer2);
        System.out.println("Global Offers : " + globalOffer);


        SlotList<Integer> demand1 = new SlotList<>();
        demand1.addSlot(new Slot<>(1, 3, 2));
        demand1.addSlot(new Slot<>(5, 6, 3));

        boolean demand1CanBeSatisfied = reservationManagerService.demandCanBeSatisfied(demand1, globalOffer);
        System.out.println("Demand 1 can be satisfied : " + demand1CanBeSatisfied);

        SlotList<Integer> demand2 = new SlotList<>();
        demand2.addSlot(new Slot<>(1, 2, 3));
        demand2.addSlot(new Slot<>(4, 6, 1));

        boolean demand2CanBeSatisfied = reservationManagerService.demandCanBeSatisfied(demand2, globalOffer);
        System.out.println("Demand 2 can be satisfied : " + demand2CanBeSatisfied);

        SlotList<Integer> demand3 = new SlotList<>();
        demand3.addSlot(new Slot<>(0, 2, 2));
        demand3.addSlot(new Slot<>(5, 6, 5));

        boolean demand3CanBeSatisfied = reservationManagerService.demandCanBeSatisfied(demand3, globalOffer);
        System.out.println("Demand 3 can be satisfied : " + demand3CanBeSatisfied);
    }
}
