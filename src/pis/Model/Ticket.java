package pis.Model;

public class Ticket {
    private int id;
    private String name;
    private String fromPlace;
    private String toPlace;

    // Constructor
    public Ticket(int id, String name, String fromPlace, String toPlace) {
        this.id = id;
        this.name = name;
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }
}
