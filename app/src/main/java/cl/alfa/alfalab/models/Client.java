package cl.alfa.alfalab.models;

import java.io.Serializable;

public class Client implements Serializable {

    private String firstname,
            lastname,
            instagram,
            email;

    public Client(){}
    public Client(String firstname, String lastname, String email, String instagram) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.instagram = instagram;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
