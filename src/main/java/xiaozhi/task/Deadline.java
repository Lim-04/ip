package xiaozhi.task;

import java.time.LocalDate;

import xiaozhi.util.Dates;

/**
 * A task that must be done by a particular date.
 */
public class Deadline extends Task {
    protected LocalDate byDate;

    /**
     * Creates a deadline due by the given date.
     *
     * @param description What the deadline is.
     * @param byDate Date the deadline is due by.
     */
    public Deadline(String description, LocalDate byDate) {
        super(description);
        assert byDate != null
                : "Deadline's by-date should never be null; it is always the result of Dates.parse(), "
                + "which either returns a valid date or throws.";
        this.byDate = byDate;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    public String toSaveFormat() {
        return super.toSaveFormat() + " | " + byDate;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + Dates.format(byDate) + ")";
    }
}
