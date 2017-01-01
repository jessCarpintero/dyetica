package com.dyetica.app.model;

import java.sql.Timestamp;

/**
 * Created by Jess on 31/12/2016.
 */

public class Statistics {
    private long _id;
    private int user_id;
    private String last_access;
    private Statistics statistics;

    public static synchronized Statistics getInstance(Statistics statistics){
        if (null == statistics){
            statistics = new Statistics(statistics);
        }
        return statistics;
    }

    private Statistics(Statistics statistics){
        this.statistics = statistics;
    }


    public Statistics(long _id, int user_id, String last_access) {
        this._id = _id;
        this.user_id = user_id;
        this.last_access = last_access;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getLast_access() {
        return last_access;
    }

    public void setLast_access(String last_access) {
        this.last_access = last_access;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "_id=" + _id +
                ", user_id=" + user_id +
                ", last_access=" + last_access +
                '}';
    }
}
