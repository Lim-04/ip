package xiaozhi.task;

import java.time.LocalDate;

import xiaozhi.util.Dates;

public class Deadline extends Task {
    protected LocalDate by;

    public Deadline(String description, LocalDate by) {
        super(description);
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
