package com.example.pemil.smarthack.Models;


/**
 * @atotputerNICA
 *
 * User class.
 */

//@IgnoreExtraProperties
public class User {

    private String name;
    private String country;
    private String birthday;
    private Long telephone;
    private String email;
    private String id;
    private String URI;
    private String account;

    public User(String name, String country, String birthday,
                Long telephone, String email, String id, String URI, String account) {
        this.name = name;
        this.country = country;
        this.birthday = birthday;
        this.telephone = telephone;
        this.email = email;
        this.id = id;
        this.URI = URI;
        this.account = account;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Long getTelephone() {
        return telephone;
    }

    public void setTelephone(Long telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", birthday='" + birthday + '\'' +
                ", telephone=" + telephone +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", URI='" + URI + '\'' +
                ", account='" + account + '\'' +
                '}';
    }
}
