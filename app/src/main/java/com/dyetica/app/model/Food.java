package com.dyetica.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.dyetica.app.utils.MethodsUtil;

/**
 * Created by Jess on 31/12/2016.
 */

public class Food implements Parcelable {

    private long id;
    private String descripcion_alimento;
    private String grupo_alimentos;
    private float hidratos_carbono;
    private float proteinas;
    private float grasas;
    private int gramos;
    private int calorias;
    private int state;
    private int tipo_alimento;
    private float valorQr;
    private float valorQp;

    //Campos extra
    private float fibras;
    private float hierro;
    private float calcio;
    private float vitamina_a;
    private float vitamina_b1;
    private float vitamina_c;
    private float rivoflavina;
    private float niacina;
    private float vitamina_e;

    public Food() {
    }

    public Food(long id, String descripcion_alimento, String grupo_alimentos, float hidratos_carbono, float proteinas, float grasas, int gramos, int calorias, int state, int tipo_alimento, float valorQr, float valorQp, float fibras, float hierro, float calcio, float vitamina_a, float vitamina_b1, float vitamina_c, float rivoflavina, float niacina, float vitamina_e) {
        this.id = id;
        this.descripcion_alimento = descripcion_alimento;
        this.grupo_alimentos = grupo_alimentos;
        this.hidratos_carbono = hidratos_carbono;
        this.proteinas = proteinas;
        this.grasas = grasas;
        this.calorias = calorias;
        this.state = state;
        this.tipo_alimento = tipo_alimento;
        this.gramos = 0;
        this.valorQp = 0;
        this.valorQr = 0;
        this.fibras = fibras;
        this.hierro = hierro;
        this.calcio = calcio;
        this.vitamina_a = vitamina_a;
        this.vitamina_b1 = vitamina_b1;
        this.vitamina_c = vitamina_c;
        this.rivoflavina = rivoflavina;
        this.niacina = niacina;
        this.vitamina_e = vitamina_e;
    }

    protected Food(Parcel in) {
        id = in.readLong();
        descripcion_alimento = in.readString();
        grupo_alimentos = in.readString();
        hidratos_carbono = in.readFloat();
        proteinas = in.readFloat();
        grasas = in.readFloat();
        gramos = in.readInt();
        calorias = in.readInt();
        state = in.readInt();
        tipo_alimento = in.readInt();
        valorQr = in.readFloat();
        valorQp = in.readFloat();
        fibras = in.readFloat();
        hierro = in.readFloat();
        calcio = in.readFloat();
        vitamina_a = in.readFloat();
        vitamina_b1 = in.readFloat();
        vitamina_c = in.readFloat();
        rivoflavina = in.readFloat();
        niacina = in.readFloat();
        vitamina_e = in.readFloat();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long _id) {
        this.id = _id;
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

    public int getGramos() {
        return gramos;
    }

    public void setGramos(int gramos) {
        this.gramos = gramos;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getTipo_alimento() {
        return tipo_alimento;
    }

    public void setTipo_alimento(int tipo_alimento) {
        this.tipo_alimento = tipo_alimento;
    }

    public float getFibras() {
        return fibras;
    }

    public void setFibras(float fibras) {
        this.fibras = fibras;
    }

    public float getHierro() {
        return hierro;
    }

    public void setHierro(float hierro) {
        this.hierro = hierro;
    }

    public float getCalcio() {
        return calcio;
    }

    public void setCalcio(float calcio) {
        this.calcio = calcio;
    }

    public float getVitamina_a() {
        return vitamina_a;
    }

    public void setVitamina_a(float vitamina_a) {
        this.vitamina_a = vitamina_a;
    }

    public float getVitamina_b1() {
        return vitamina_b1;
    }

    public void setVitamina_b1(float vitamina_b1) {
        this.vitamina_b1 = vitamina_b1;
    }

    public float getVitamina_c() {
        return vitamina_c;
    }

    public void setVitamina_c(float vitamina_c) {
        this.vitamina_c = vitamina_c;
    }

    public float getRivoflavina() {
        return rivoflavina;
    }

    public void setRivoflavina(float rivoflavina) {
        this.rivoflavina = rivoflavina;
    }

    public float getNiacina() {
        return niacina;
    }

    public void setNiacina(float niacina) {
        this.niacina = niacina;
    }

    public float getVitamina_e() {
        return vitamina_e;
    }

    public void setVitamina_e(float vitamina_e) {
        this.vitamina_e = vitamina_e;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food)) return false;

        Food food = (Food) o;

        if (getId() != food.getId()) return false;
        if (getDescripcion_alimento() != null ? !getDescripcion_alimento().equals(food.getDescripcion_alimento()) : food.getDescripcion_alimento() != null)
            return false;
        return getGrupo_alimentos() != null ? getGrupo_alimentos().equals(food.getGrupo_alimentos()) : food.getGrupo_alimentos() == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getDescripcion_alimento() != null ? getDescripcion_alimento().hashCode() : 0);
        result = 31 * result + (getGrupo_alimentos() != null ? getGrupo_alimentos().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String foodAux = "";
        if ("No hay alimentos".equals(getDescripcion_alimento())){
            foodAux = getDescripcion_alimento();
        } else {
            foodAux += getDescripcion_alimento();
            foodAux +=  "\n (Qp: " + getValorQp() + " / Qg: ";
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
        dest.writeString(descripcion_alimento);
        dest.writeString(grupo_alimentos);
        dest.writeFloat(hidratos_carbono);
        dest.writeFloat(proteinas);
        dest.writeFloat(grasas);
        dest.writeInt(gramos);
        dest.writeInt(calorias);
        dest.writeInt(state);
        dest.writeInt(tipo_alimento);
        dest.writeFloat(valorQr);
        dest.writeFloat(valorQp);
        dest.writeFloat(fibras);
        dest.writeFloat(hierro);
        dest.writeFloat(calcio);
        dest.writeFloat(vitamina_a);
        dest.writeFloat(vitamina_b1);
        dest.writeFloat(vitamina_c);
        dest.writeFloat(rivoflavina);
        dest.writeFloat(niacina);
        dest.writeFloat(vitamina_e);
    }
}
