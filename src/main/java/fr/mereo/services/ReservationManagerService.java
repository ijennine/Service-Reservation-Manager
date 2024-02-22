package fr.mereo.services;

import fr.mereo.models.SlotList;
import fr.mereo.models.Slot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ReservationManagerService<T extends Comparable<T>> {

    /**
     * Combines the slots of two SlotLists into one, while adding the quantities of
     * intersecting slots
     *
     * @param slotList1 first SlotList
     * @param slotList2 second SlotList
     * @return combination of slotList1 and slotList2
     */
    public SlotList<T> combineSlots(SlotList<T> slotList1, SlotList<T> slotList2) {
        SlotList<T> result = new SlotList<>();

        TreeSet<T> bounds = new TreeSet<>();
        bounds.addAll(getBoundsFromSlots(slotList1.getSlots()));
        bounds.addAll(getBoundsFromSlots(slotList2.getSlots()));

        List<Slot<T>> slots = createSlotsFromBounds(bounds);

        slots.forEach(slot -> {
            setSlotValueFromIntersectingSlots(slot, slotList1.getSlots());
            setSlotValueFromIntersectingSlots(slot, slotList2.getSlots());
        });

        // remove slots with empty values
        slots = slots.stream().filter(slot -> slot.getValue() != 0).collect(Collectors.toList());

        combineNeighbourSlotsWithSameQuantity(slots);

        result.setSlots(slots);

        return result;
    }

    /**
     * Determines weather the demand (represented by a SlotList) can be satisfied by the
     * offer (represented by a SlotList as well)
     *
     * @param demand SlotList representing the demand
     * @param offer  SlotList representing the offer
     * @return true if demand can be satisfied, else false
     */
    public boolean demandCanBeSatisfied(SlotList<T> demand, SlotList<T> offer) {
        List<Slot<T>> possibleSlots;
        Slot<T> remainigDemand;

        for (Slot<T> demandSlot : demand.getSlots()) {

            // Filter out all the Slots that have quantities smaller than the quantity of the demand Slot
            possibleSlots = offer.getSlots().stream()
                    .filter(slot -> slot.getValue() >= demandSlot.getValue())
                    .collect(Collectors.toList());

            if (possibleSlots.isEmpty()) return false;

            remainigDemand = new Slot<>(demandSlot.getStart(), demandSlot.getEnd(), demandSlot.getValue());
            for (Slot<T> offerSlot : possibleSlots) {
                if ((remainigDemand.getStart().compareTo(offerSlot.getStart()) > 0
                        || remainigDemand.getStart().compareTo(offerSlot.getStart()) == 0)
                        && remainigDemand.getStart().compareTo(offerSlot.getEnd()) < 0) {

                    remainigDemand.setStart(offerSlot.getEnd());
                }

                // If the there is no remaining demand, then the demand Slot can be satisfied
                // and there is no need to keep searching
                if (remainigDemand.getEnd().compareTo(remainigDemand.getStart()) <= 0) break;
            }

            // If at least one demand Slot can't be satisfied, then the whole demand can't be satisfied
            if (remainigDemand.getEnd().compareTo(remainigDemand.getStart()) > 0) return false;
        }

        return true;
    }

    /**
     * Returns the upper and the lower bound of each Slot
     */
    private TreeSet<T> getBoundsFromSlots(List<Slot<T>> slots) {
        TreeSet<T> bounds = new TreeSet<>();

        slots.forEach(slot -> {
            bounds.add(slot.getStart());
            bounds.add(slot.getEnd());
        });

        return bounds;
    }

    /**
     * Creates slots from a list of bounds
     * Example using integers :
     * Given the bounds {1, 3, 4}, returns Slots : [1, 3] and [3,4]
     */
    private List<Slot<T>> createSlotsFromBounds(TreeSet<T> bounds) {
        List<Slot<T>> slots = new ArrayList<>();

        Iterator<T> iterator = bounds.iterator();
        T prev = iterator.next();
        while (iterator.hasNext()) {
            T current = iterator.next();
            slots.add(new Slot<>(prev, current, 0));
            prev = current;
        }

        return slots;
    }

    /**
     * Sets the sum of the values of the slots that intersect with a given slot
     */
    private void setSlotValueFromIntersectingSlots(Slot<T> slot, List<Slot<T>> slots) {
        for (Slot<T> slot1 : slots) {
            if (slot.getStart().compareTo(slot1.getEnd()) < 0 && slot1.getStart().compareTo(slot.getEnd()) < 0) {
                slot.setValue(slot.getValue() + slot1.getValue());
            }
        }
    }

    /**
     * Combines neighbour Slots with the same value into a single slot
     * Example using Integers :
     * Slots : [1,2] with value 2 and [2,3] with value 2 become single slot [1,3] with value 2
     */
    private void combineNeighbourSlotsWithSameQuantity(List<Slot<T>> slots) {
        Iterator<Slot<T>> iterator = slots.iterator();
        Slot<T> prev = iterator.next();
        while (iterator.hasNext()) {
            Slot<T> current = iterator.next();

            if (prev.getEnd().equals(current.getStart()) && prev.getValue().equals(current.getValue())) {
                prev.setEnd(current.getEnd());
                iterator.remove();
            } else {
                prev = current;
            }
        }
    }
}
