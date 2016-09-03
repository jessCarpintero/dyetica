package com.dyetica.app.model;

import java.sql.Timestamp;

/**
 * Created by Jess on 03/09/2016.
 */
public class User {

    private int id;
    private String name;
    private String email;
    private int block;
    private Timestamp registerDate;
    private Timestamp lastvisitDate;
    private String api_key;

    public User(int id, String name, String email, int block, Timestamp registerDate, Timestamp lastvisitDate, String api_key){
        this.id = id;
        this.name = name;
        this.email = email;
        this.block = block;
        this.registerDate = registerDate;
        this.lastvisitDate = lastvisitDate;
        this.api_key = api_key;
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

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
