package com.dyetica.app.model;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jess on 03/09/2016.
 */
public class User {

    private int id;
    private String name;
    private String username;
    private String email;
    private int block;
    private Timestamp registerDate;
    private Timestamp lastvisitDate;
    private String apiKey;
    private String firstSurname;
    private String secondSurname;
    private String dateBirthday;
    private String postalCode;
    private static User user;

    public User() {
    }

    public static synchronized User getInstance(User user){
        if (null == user){
            user = new User(user);
        }
        return user;
    }

    private User(User user){
        this.user = user;
    }

    private User(int id, String name, String username, String email, int block, Timestamp registerDate, Timestamp lastvisitDate, String apiKey, String firstSurname, String secondSurname, String dateBirthday, String postalCode) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.block = block;
        this.registerDate = registerDate;
        this.lastvisitDate = lastvisitDate;
        this.apiKey = apiKey;
        this.firstSurname = firstSurname;
        this.secondSurname = secondSurname;
        this.dateBirthday = dateBirthday;
        this.postalCode = postalCode;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getLastvisitDate() {
        return lastvisitDate;
    }

    public void setLastvisitDate(Timestamp lastvisitDate) {
        this.lastvisitDate = lastvisitDate;
    }

    public Timestamp getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Timestamp registerDate) {
        this.registerDate = registerDate;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
    }

    public String getDateBirthday() {
        return dateBirthday;
    }

    public void setDateBirthday(String dateBirthday) {
      this.dateBirthday = dateBirthday;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", block=" + block +
                ", registerDate=" + registerDate +
                ", lastvisitDate=" + lastvisitDate +
                ", apiKey='" + apiKey + '\'' +
                ", firstSurname='" + firstSurname + '\'' +
                ", secondSurname='" + secondSurname + '\'' +
                ", dateBirthday='" + dateBirthday + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null)
            return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (apiKey != null ? !apiKey.equals(user.apiKey) : user.apiKey != null) return false;
        if (firstSurname != null ? !firstSurname.equals(user.firstSurname) : user.firstSurname != null)
            return false;
        if (secondSurname != null ? !secondSurname.equals(user.secondSurname) : user.secondSurname != null)
            return false;
        if (dateBirthday != null ? !dateBirthday.equals(user.dateBirthday) : user.dateBirthday != null)
            return false;
        return postalCode != null ? postalCode.equals(user.postalCode) : user.postalCode == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + block;
        result = 31 * result + (registerDate != null ? registerDate.hashCode() : 0);
        result = 31 * result + (lastvisitDate != null ? lastvisitDate.hashCode() : 0);
        result = 31 * result + (apiKey != null ? apiKey.hashCode() : 0);
        result = 31 * result + (firstSurname != null ? firstSurname.hashCode() : 0);
        result = 31 * result + (secondSurname != null ? secondSurname.hashCode() : 0);
        result = 31 * result + (dateBirthday != null ? dateBirthday.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        return result;
    }
}
