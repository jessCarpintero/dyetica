package com.dyetica.app.model;

import java.sql.Timestamp;

/**
 * Created by Jess on 02/10/2016.
 */
public class DieteticProfile {

    private int id;
    private int etapa;
    private int perfil_id;
    private int user_id;
    private String nombre;
    private int sexo;
    private String f_nac;
    private int talla;
    private int peso;
    private int actividad;
    private int constitucion;
    private int pregunta1;
    private int pregunta2;
    private int pregunta3;
    private int objetivo;
    private float ritmo;
    private int state;
    private Timestamp publish_up;
    private Timestamp publish_down;
    private int checked_out;
    private Timestamp checked_out_time;
    private String actualiza;
    private float kcaldia;
    private float cg;


    public DieteticProfile() {
    }

    public DieteticProfile(int id, int etapa, int perfilId, int userId, String nombre, int sexo, String f_nac, int talla, int peso, int actividad, int constitucion, int pregunta1, int pregunta2, int pregunta3, int objetivo, float ritmo, int state, Timestamp publish_up, Timestamp publish_down, int checked_out, Timestamp checked_out_time, String actualiza, float kcaldia, float cg) {
        this.id = id;
        this.etapa = etapa;
        this.perfil_id = perfilId;
        this.user_id = userId;
        this.nombre = nombre;
        this.sexo = sexo;
        this.f_nac = f_nac;
        this.talla = talla;
        this.peso = peso;
        this.actividad = actividad;
        this.constitucion = constitucion;
        this.pregunta1 = pregunta1;
        this.pregunta2 = pregunta2;
        this.pregunta3 = pregunta3;
        this.objetivo = objetivo;
        this.ritmo = ritmo;
        this.state = state;
        this.publish_up = publish_up;
        this.publish_down = publish_down;
        this.checked_out = checked_out;
        this.checked_out_time = checked_out_time;
        this.actualiza = actualiza;
        this.kcaldia = kcaldia;
        this.cg = cg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEtapa() {
        return etapa;
    }

    public void setEtapa(int etapa) {
        this.etapa = etapa;
    }

    public int getPerfil_id() {
        return perfil_id;
    }

    public void setPerfil_id(int perfil_id) {
        this.perfil_id = perfil_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getSexo() {
        return sexo;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    public String getF_nac() {
        return f_nac;
    }

    public void setF_nac(String f_nac) {
        this.f_nac = f_nac;
    }

    public int getTalla() {
        return talla;
    }

    public void setTalla(int talla) {
        this.talla = talla;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getActividad() {
        return actividad;
    }

    public void setActividad(int actividad) {
        this.actividad = actividad;
    }

    public int getConstitucion() {
        return constitucion;
    }

    public void setConstitucion(int constitucion) {
        this.constitucion = constitucion;
    }

    public int getPregunta1() {
        return pregunta1;
    }

    public void setPregunta1(int pregunta1) {
        this.pregunta1 = pregunta1;
    }

    public int getPregunta2() {
        return pregunta2;
    }

    public void setPregunta2(int pregunta2) {
        this.pregunta2 = pregunta2;
    }

    public int getPregunta3() {
        return pregunta3;
    }

    public void setPregunta3(int pregunta3) {
        this.pregunta3 = pregunta3;
    }

    public int getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(int objetivo) {
        this.objetivo = objetivo;
    }

    public float getRitmo() {
        return ritmo;
    }

    public void setRitmo(float ritmo) {
        this.ritmo = ritmo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Timestamp getPublish_up() {
        return publish_up;
    }

    public void setPublish_up(Timestamp publish_up) {
        this.publish_up = publish_up;
    }

    public Timestamp getPublish_down() {
        return publish_down;
    }

    public void setPublish_down(Timestamp publish_down) {
        this.publish_down = publish_down;
    }

    public int getChecked_out() {
        return checked_out;
    }

    public void setChecked_out(int checked_out) {
        this.checked_out = checked_out;
    }

    public Timestamp getChecked_out_time() {
        return checked_out_time;
    }

    public void setChecked_out_time(Timestamp checked_out_time) {
        this.checked_out_time = checked_out_time;
    }

    public String getActualiza() {
        return actualiza;
    }

    public void setActualiza(String actualiza) {
        this.actualiza = actualiza;
    }

    public float getKcaldia() {
        return kcaldia;
    }

    public void setKcaldia(float kcaldia) {
        this.kcaldia = kcaldia;
    }

    public float getCg() {
        return cg;
    }

    public void setCg(float cg) {
        this.cg = cg;
    }

    @Override
    public String toString() {
        return "DieteticProfile{" +
                "id=" + id +
                ", etapa=" + etapa +
                ", perfil_id=" + perfil_id +
                ", user_id=" + user_id +
                ", nombre='" + nombre + '\'' +
                ", sexo=" + sexo +
                ", f_nac='" + f_nac + '\'' +
                ", talla=" + talla +
                ", peso=" + peso +
                ", actividad=" + actividad +
                ", constitucion=" + constitucion +
                ", pregunta1=" + pregunta1 +
                ", pregunta2=" + pregunta2 +
                ", pregunta3=" + pregunta3 +
                ", objetivo=" + objetivo +
                ", ritmo=" + ritmo +
                ", state=" + state +
                ", publish_up=" + publish_up +
                ", publish_down=" + publish_down +
                ", checked_out=" + checked_out +
                ", checked_out_time=" + checked_out_time +
                ", actualiza='" + actualiza + '\'' +
                ", kcaldia=" + kcaldia +
                ", cg=" + cg +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DieteticProfile profile = (DieteticProfile) o;

        if (id != profile.id) return false;
        if (perfil_id != profile.perfil_id) return false;
        if (user_id != profile.user_id) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + etapa;
        result = 31 * result + perfil_id;
        result = 31 * result + user_id;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + sexo;
        result = 31 * result + (f_nac != null ? f_nac.hashCode() : 0);
        result = 31 * result + talla;
        result = 31 * result + peso;
        result = 31 * result + actividad;
        result = 31 * result + constitucion;
        result = 31 * result + pregunta1;
        result = 31 * result + pregunta2;
        result = 31 * result + pregunta3;
        result = 31 * result + objetivo;
        result = 31 * result + (ritmo != +0.0f ? Float.floatToIntBits(ritmo) : 0);
        result = 31 * result + state;
        result = 31 * result + (publish_up != null ? publish_up.hashCode() : 0);
        result = 31 * result + (publish_down != null ? publish_down.hashCode() : 0);
        result = 31 * result + checked_out;
        result = 31 * result + (checked_out_time != null ? checked_out_time.hashCode() : 0);
        result = 31 * result + (actualiza != null ? actualiza.hashCode() : 0);
        result = 31 * result + (kcaldia != +0.0f ? Float.floatToIntBits(kcaldia) : 0);
        result = 31 * result + (cg != +0.0f ? Float.floatToIntBits(cg) : 0);
        return result;
    }
}