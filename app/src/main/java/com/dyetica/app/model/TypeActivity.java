package com.dyetica.app.model;

/**
 * Created by Jess on 06/09/2016.
 */
public class TypeActivity {

    private int id;
    private String activity;
    private float factor;

    public TypeActivity(int id, String activity, float factor) {
        this.id = id;
        this.activity = activity;
        this.factor = factor;
    }

    public TypeActivity() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public float getFactor() {
        return factor;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }

    @Override
    public String toString() {
        return "Information{" +
                "id=" + id +
                ", activity='" + activity + '\'' +
                ", factor='" + factor + '\'' +
                '}';
    }
}
