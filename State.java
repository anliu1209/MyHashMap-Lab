import java.util.Locale;

public class State {
    private final String name;
    private final String abbreviation;

    public State(String abbreviation) {
        this("", abbreviation);
    }

    public State(String name, String abbreviation) {
        this.name = name;
        String letters = abbreviation.trim();
        if (!letters.matches("[A-Za-z]{2}")) {
            throw new IllegalArgumentException("Enter a two-letter state abbreviation.");
        }
        this.abbreviation = letters.toUpperCase(Locale.ROOT);
    }

    @Override
    public String toString() {
        return name.isEmpty() ? abbreviation : name + " (" + abbreviation + ")";
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    @Override
    public boolean equals(Object obj) {
        // Safe casting and type checking
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        State other = (State) obj;
        return this.abbreviation.equals(other.getAbbreviation());
    }

    @Override
    public int hashCode() {
        int first = abbreviation.charAt(0) - 'A';
        int second = abbreviation.charAt(1) - 'A';
        return first * 26 + second;
    }
}
