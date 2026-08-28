package xiaozhi.task;

import java.time.LocalDate;

import xiaozhi.util.Dates;

/**
 * A task that spans from one date to another.
 */
public class Event extends Task {
    protected LocalDate from;
    protected LocalDate to;

    /**
     * Creates an event spanning the given dates.
     *
     * @param description What the event is.
     * @param from Date the event starts.
     * @param to Date the event ends.
     */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    @Override
    public String toSaveFormat() {
        return super.toSaveFormat() + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + Dates.format(from) + " to: " + Dates.format(to) + ")";
    }
}
