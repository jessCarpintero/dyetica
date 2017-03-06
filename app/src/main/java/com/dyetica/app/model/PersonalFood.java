package com.dyetica.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jess on 31/12/2016.
 */

public class PersonalFood implements Parcelable {

    private long id;
    private long id_user;
    private String descripcion_alimento;
    private int ubicacion;
    private float hidratos_carbono;
    private float proteinas;
    private float grasas;
    private int peso_neto;
    private float valorQr;
    private float valorQp;
    private int calorias;

    public PersonalFood() {
    }

    public PersonalFood(long id, long idUser, String descripcion_alimento, int ubicacion, float hidratos_carbono, float proteinas, float grasas, int peso_neto, float valorQr, float valorQp) {
        this.id = id;
        this.id_user = idUser;
        this.descripcion_alimento = descripcion_alimento;
        this.ubicacion = ubicacion;
        this.hidratos_carbono = hidratos_carbono;
        this.proteinas = proteinas;
        this.grasas = grasas;
        this.peso_neto = peso_neto;
        this.calorias = 0;
        this.valorQr = valorQr;
        this.valorQp = valorQp;
    }

    protected PersonalFood(Parcel in) {
        id = in.readLong();
        id_user = in.readLong();
        descripcion_alimento = in.readString();
        ubicacion = in.readInt();
        hidratos_carbono = in.readFloat();
        proteinas = in.readFloat();
        grasas = in.readFloat();
        peso_neto = in.readInt();
        valorQr = in.readFloat();
        calorias = in.readInt();
        valorQp = in.readFloat();
    }

    public static final Creator<PersonalFood> CREATOR = new Creator<PersonalFood>() {
        @Override
        public PersonalFood createFromParcel(Parcel in) {
            return new PersonalFood(in);
        }

        @Override
        public PersonalFood[] newArray(int size) {
            return new PersonalFood[size];
        }
    };

    public int getPeso_neto() {
        return peso_neto;
    }

    public void setPeso_neto(int peso_neto) {
        this.peso_neto = peso_neto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    public String getDescripcion_alimento() {
        return descripcion_alimento;
    }

    public void setDescripcion_alimento(String descripcion_alimento) {
        this.descripcion_alimento = descripcion_alimento;
    }

    public int getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(int ubicacion) {
        this.ubicacion = ubicacion;
    }

    public float getHidratos_carbono() {
        return hidratos_carbono;
    }

    public void setHidratos_carbono(float hidratos_carbono) {
        this.hidratos_carbono = hidratos_carbono;
    }

    public float getProteinas() {
        return proteinas;
    }

    public void setProteinas(float proteinas) {
        this.proteinas = proteinas;
    }

    public float getGrasas() {
        return grasas;
    }

    public void setGrasas(float grasas) {
        this.grasas = grasas;
    }

    public float getValorQr() {
        return valorQr;
    }

    public void setValorQr(float valorQr) {
        this.valorQr = valorQr;
    }

    public float getValorQp() {
        return valorQp;
    }

    public void setValorQp(float valorQp) {
        this.valorQp = valorQp;
    }

    public int getCalorias() {
        return calorias;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonalFood)) return false;

        PersonalFood that = (PersonalFood) o;

        if (getId() != that.getId()) return false;
        if (getId_user() != that.getId_user()) return false;
        if (getUbicacion() != that.getUbicacion()) return false;
        return getDescripcion_alimento().equals(that.getDescripcion_alimento());

    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (int) (getId_user() ^ (getId_user() >>> 32));
        result = 31 * result + getDescripcion_alimento().hashCode();
        result = 31 * result + getUbicacion();
        return result;
    }

    @Override
    public String toString() {
        String foodAux = "";
        if ("No hay alimentos".equals(getDescripcion_alimento())){
            foodAux = getDescripcion_alimento();
        } else {
            foodAux += getDescripcion_alimento();
            foodAux += "\n (Qp: " + getValorQp() + " / Qg: ";
            foodAux += getValorQr() + ")";
        }
        return foodAux;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(id_user);
        dest.writeString(descripcion_alimento);
        dest.writeInt(ubicacion);
        dest.writeFloat(hidratos_carbono);
        dest.writeFloat(proteinas);
        dest.writeFloat(grasas);
        dest.writeInt(peso_neto);
        dest.writeFloat(valorQr);
        dest.writeInt(calorias);
        dest.writeFloat(valorQp);
    }
}
