package projects.notes;

import java.io.Serial;
import java.io.Serializable;

public record Note(String title, String description) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return title;
    }
}
