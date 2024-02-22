package fr.mereo.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Data
public class SlotList<T extends Comparable<T>> {
    List<Slot<T>> slots = new ArrayList<>();

    public void addSlot(Slot<T> slot) {
        slots.add(slot);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SlotList)) return false;
        SlotList<?> slotList = (SlotList<?>) o;
        return Objects.equals(getSlots(), slotList.getSlots());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSlots());
    }
}
