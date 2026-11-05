public class State {
    private final String name;
    private final String abbreviation;

    public State(String abbreviation) {
        this("", abbreviation);
    }

    public State(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
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
        int first = Character.toLowerCase(abbreviation.charAt(0)) - 'a';
        int second = Character.toLowerCase(abbreviation.charAt(1)) - 'a';
        return first * 26 + second;
    }
}
