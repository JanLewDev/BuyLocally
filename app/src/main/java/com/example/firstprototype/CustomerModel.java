package com.example.firstprototype;

import java.util.Random;

public class CustomerModel {

    private int id;
    private String firstName;
    private String surname;
    private String email;
    private String password;
    private int resetcode;

    // constructors

    public CustomerModel(int id, String firstName, String surname, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.email = email;
        this.password = password;
        int i = new Random().nextInt(900000) + 100000;
        this.resetcode = i;
    }

    public CustomerModel() {
    }

    // toString for outputting the data of a customer

    @Override
    public String toString() {
        return "CustomerModel{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", resetcode=" + resetcode +
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getResetcode() {
        return resetcode;
    }

    public void setResetcode(int resetcode) {
        this.resetcode = resetcode;
    }
}
