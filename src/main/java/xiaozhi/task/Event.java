package xiaozhi.task;

import java.time.LocalDate;

import xiaozhi.util.Dates;

/**
 * A task that spans from one date to another.
 */
public class Event extends Task {
    protected LocalDate fromDate;
    protected LocalDate toDate;

    /**
     * Creates an event spanning the given dates.
     *
     * @param description What the event is.
     * @param fromDate Date the event starts.
     * @param toDate Date the event ends.
     */
    public Event(String description, LocalDate fromDate, LocalDate toDate) {
        super(description);
        assert fromDate != null && toDate != null
                : "Event's from/to dates should never be null; both are always the result of "
                + "Dates.parse(), which either returns a valid date or throws.";
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    @Override
    public String toSaveFormat() {
        return super.toSaveFormat() + " | " + fromDate + " | " + toDate;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + Dates.format(fromDate) + " to: " + Dates.format(toDate) + ")";
    }
}
