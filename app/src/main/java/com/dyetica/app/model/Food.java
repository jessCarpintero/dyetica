package com.dyetica.app.model;

/**
 * Created by Jess on 31/12/2016.
 */

public class Food {

    private long _id;
    private String descripcion_alimento;
    private String grupo_alimentos;
    private float hidratos_carbono;
    private float proteinas;
    private float grasas;

    public Food() {
    }


    public Food(long _id, String descripcion_alimento, String grupo_alimentos, float hidratos_carbono, float proteinas, float grasas) {
        this._id = _id;
        this.descripcion_alimento = descripcion_alimento;
        this.grupo_alimentos = grupo_alimentos;
        this.hidratos_carbono = hidratos_carbono;
        this.proteinas = proteinas;
        this.grasas = grasas;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getDescripcion_alimento() {
        return descripcion_alimento;
    }

    public void setDescripcion_alimento(String descripcion_alimento) {
        this.descripcion_alimento = descripcion_alimento;
    }

    public String getGrupo_alimentos() {
        return grupo_alimentos;
    }

    public void setGrupo_alimentos(String grupo_alimentos) {
        this.grupo_alimentos = grupo_alimentos;
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

   @Override
    public String toString() {
        return "Food{" +
                "_id=" + _id +
                ", descripcion_alimento='" + descripcion_alimento + '\'' +
                ", grupo_alimentos='" + grupo_alimentos + '\'' +
                ", hidratos_carbono=" + hidratos_carbono +
                ", proteinas=" + proteinas +
                ", grasas=" + grasas +
                '}';
    }
}
