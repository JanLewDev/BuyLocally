package com.example.firstprototype;

public class ProducerModel {

    // declare all attributes
    private int id;
    private String companyName;
    private String firstName;
    private String surname;
    private String description;
    private String type;
    private String location;

    // constructors
    public ProducerModel(int id, String companyName, String firstName,
                         String surname, String description, String type, String location){
        this.id = id;
        this.companyName = companyName;
        this.firstName = firstName;
        this.surname = surname;
        this.description = description;
        this.type = type;
        this.location = location;
    }

    public ProducerModel(){

    }

    // a toString method to output data if needed
    @Override
    public String toString() {
        return "ProducerModel{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", description='" + description + '\'' +
                ", goodsType='" + type + '\'' +
                ", location=" + location +
                '}';
    }


    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}