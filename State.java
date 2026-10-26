public class State {
    private String abbreviation;

    public State(String abbreviation){
        this.abbreviation = abbreviation;
    }

    public String toString() {
        return abbreviation + ": ";
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