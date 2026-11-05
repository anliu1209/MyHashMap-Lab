public class StateInfo{
    private String name;
    private String capitalCity;
    private int population;
    private final DLList<String> pictureUrls = new DLList<>();

    public StateInfo(String name, String capitalCity, int population){
        this.name = name;
        this.capitalCity = capitalCity;
        this.population = population;
    }

    @Override
    public String toString() {
        return name + " | Capital: " + capitalCity + " | Population: " + String.format("%,d", population);
    }

    public String getName() {
        return name;
    }
    public String getCapitalCity() {
        return capitalCity;
    }
    public int getPopulation() {
        return population;
    }

    public DLList<String> getPictureUrls() {
        return pictureUrls;
    }
}
