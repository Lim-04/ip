package xiaozhi.task;

import java.time.LocalDate;

import xiaozhi.util.Dates;

/**
 * A task that must be done by a particular date.
 */
public class Deadline extends Task {
    protected LocalDate by;

    /**
     * Creates a deadline due by the given date.
     *
     * @param description What the deadline is.
     * @param by Date the deadline is due by.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        assert by != null
                : "Deadline's by-date should never be null; it is always the result of Dates.parse(), "
                + "which either returns a valid date or throws.";
        this.by = by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    public String toSaveFormat() {
        return super.toSaveFormat() + " | " + by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + Dates.format(by) + ")";
    }
}
