package com.dyetica.app.model;

/**
 * Created by Jess on 06/09/2016.
 */
public class Information {

    private int id;
    private String html;
    private String screen;
    private Information information;

    public static synchronized Information getInstance(Information information){
        if (null == information){
            information = new Information(information);
        }
        return information;
    }

    private Information(Information information){
        this.information = information;
    }

    public Information(int id, String html, String screen, Information information) {
        this.id = id;
        this.html = html;
        this.screen = screen;
        this.information = information;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    @Override
    public String toString() {
        return "Information{" +
                "id=" + id +
                ", html='" + html + '\'' +
                ", screen='" + screen + '\'' +
                ", information=" + information +
                '}';
    }
}
