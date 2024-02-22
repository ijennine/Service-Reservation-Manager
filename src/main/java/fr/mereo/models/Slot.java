package fr.mereo.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Slot<T extends Comparable<T>> {

    private T start;
    private T end;
    private Integer value; // Could also be modelled by a Float

    public String toString() {
        return "{[" + start + ", " + end + "]"  + ", q=" + value + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Slot)) return false;
        Slot<?> slot = (Slot<?>) o;
        return Objects.equals(getStart(), slot.getStart()) && Objects.equals(getEnd(), slot.getEnd()) && Objects.equals(getValue(), slot.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStart(), getEnd(), getValue());
    }
}
