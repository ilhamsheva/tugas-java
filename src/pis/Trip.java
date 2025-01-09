/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pis;

/**
 *
 * @author sheva
 */
public class Trip {
    private int id;
    private String place;

    // Constructor
    public Trip(int id, String place) {
        this.id = id;
        this.place = place;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
    
    // Override toString method
    @Override
    public String toString() {
        return this.place; // Menampilkan nama tempat di ComboBox
    }
}
