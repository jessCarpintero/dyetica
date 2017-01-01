package com.dyetica.app.model;

/**
 * Created by Jess on 28/12/2016.
 */

public class ExtensionsProfile {

    private long _id;
    private float formulas_gr_sem_kilo;
    private float formulas_cg_base ;
    private float formulas_cg_varible1;
    private float formulas_cg_varible2;
    private float formulas_cg_min;
    private float formulas_cg_max;
    private float manana_desayuno_fuerte;
    private float manana_almuerzo_ligero;
    private float manana_desayuno_mediano;
    private float manana_almuerzo_mediano;
    private float manana_desayuno_ligero;
    private float manana_almuerzo_fuerte;
    private float mediodia_priomeroligero;
    private float mediodia_segundofuerte;
    private float mediodia_primeromedio;
    private float mediodia_segundomedio;
    private float mediodia_postre;
    private float merienda;
    private float noche_priomeroligero;
    private float noche_segundofuerte;
    private float noche_primeromedio;
    private float noche_segundomedio;
    private float noche_postre;
    private float menu24_perder;
    private float menu24_aumentar;
    private String link_perfil;
    private String link_menu24h;
    private ExtensionsProfile extensionsProfile;

    public static synchronized ExtensionsProfile getInstance(ExtensionsProfile extensionsProfile){
        if (null == extensionsProfile){
            extensionsProfile = new ExtensionsProfile(extensionsProfile);
        }
        return extensionsProfile;
    }

    private ExtensionsProfile(ExtensionsProfile extensionsProfile){
        this.extensionsProfile = extensionsProfile;
    }

    public ExtensionsProfile(long _id, float formulas_gr_sem_kilo, float formulas_cg_base, float formulas_cg_varible1, float formulas_cg_varible2, float formulas_cg_min, float formulas_cg_max, float manana_desayuno_fuerte, float manana_almuerzo_ligero, float manana_desayuno_mediano, float manana_almuerzo_mediano, float manana_desayuno_ligero, float manana_almuerzo_fuerte, float mediodia_priomeroligero, float mediodia_segundofuerte, float mediodia_primeromedio, float mediodia_segundomedio, float mediodia_postre, float merienda, float noche_priomeroligero, float noche_segundofuerte, float noche_primeromedio, float noche_segundomedio, float noche_postre, float menu24_perder, float menu24_aumentar, String link_perfil, String link_menu24h) {
        this._id = _id;
        this.formulas_gr_sem_kilo = formulas_gr_sem_kilo;
        this.formulas_cg_base = formulas_cg_base;
        this.formulas_cg_varible1 = formulas_cg_varible1;
        this.formulas_cg_varible2 = formulas_cg_varible2;
        this.formulas_cg_min = formulas_cg_min;
        this.formulas_cg_max = formulas_cg_max;
        this.manana_desayuno_fuerte = manana_desayuno_fuerte;
        this.manana_almuerzo_ligero = manana_almuerzo_ligero;
        this.manana_desayuno_mediano = manana_desayuno_mediano;
        this.manana_almuerzo_mediano = manana_almuerzo_mediano;
        this.manana_desayuno_ligero = manana_desayuno_ligero;
        this.manana_almuerzo_fuerte = manana_almuerzo_fuerte;
        this.mediodia_priomeroligero = mediodia_priomeroligero;
        this.mediodia_segundofuerte = mediodia_segundofuerte;
        this.mediodia_primeromedio = mediodia_primeromedio;
        this.mediodia_segundomedio = mediodia_segundomedio;
        this.mediodia_postre = mediodia_postre;
        this.merienda = merienda;
        this.noche_priomeroligero = noche_priomeroligero;
        this.noche_segundofuerte = noche_segundofuerte;
        this.noche_primeromedio = noche_primeromedio;
        this.noche_segundomedio = noche_segundomedio;
        this.noche_postre = noche_postre;
        this.menu24_perder = menu24_perder;
        this.menu24_aumentar = menu24_aumentar;
        this.link_perfil = link_perfil;
        this.link_menu24h = link_menu24h;
    }

    public float getFormulas_gr_sem_kilo() {
        return formulas_gr_sem_kilo;
    }

    public float getFormulas_cg_base() {
        return formulas_cg_base;
    }

    public float getFormulas_cg_varible1() {
        return formulas_cg_varible1;
    }

    public float getFormulas_cg_varible2() {
        return formulas_cg_varible2;
    }

    public float getFormulas_cg_min() {
        return formulas_cg_min;
    }

    public float getFormulas_cg_max() {
        return formulas_cg_max;
    }

    public float getManana_desayuno_fuerte() {
        return manana_desayuno_fuerte;
    }

    public float getManana_almuerzo_ligero() {
        return manana_almuerzo_ligero;
    }

    public float getManana_desayuno_mediano() {
        return manana_desayuno_mediano;
    }

    public float getManana_almuerzo_mediano() {
        return manana_almuerzo_mediano;
    }

    public float getManana_desayuno_ligero() {
        return manana_desayuno_ligero;
    }

    public float getManana_almuerzo_fuerte() {
        return manana_almuerzo_fuerte;
    }

    public float getMediodia_priomeroligero() {
        return mediodia_priomeroligero;
    }

    public float getMediodia_segundofuerte() {
        return mediodia_segundofuerte;
    }

    public float getMediodia_primeromedio() {
        return mediodia_primeromedio;
    }

    public float getMediodia_segundomedio() {
        return mediodia_segundomedio;
    }

    public float getMediodia_postre() {
        return mediodia_postre;
    }

    public float getMerienda() {
        return merienda;
    }

    public float getNoche_priomeroligero() {
        return noche_priomeroligero;
    }

    public float getNoche_segundofuerte() {
        return noche_segundofuerte;
    }

    public float getNoche_primeromedio() {
        return noche_primeromedio;
    }

    public float getNoche_segundomedio() {
        return noche_segundomedio;
    }

    public float getNoche_postre() {
        return noche_postre;
    }

    public float getMenu24_perder() {
        return menu24_perder;
    }

    public float getMenu24_aumentar() {
        return menu24_aumentar;
    }

    public String getLink_perfil() {
        return link_perfil;
    }

    public String getLink_menu24h() {
        return link_menu24h;
    }

    public void setFormulas_gr_sem_kilo(float formulas_gr_sem_kilo) {
        this.formulas_gr_sem_kilo = formulas_gr_sem_kilo;
    }

    public void setFormulas_cg_base(float formulas_cg_base) {
        this.formulas_cg_base = formulas_cg_base;
    }

    public void setFormulas_cg_varible1(float formulas_cg_varible1) {
        this.formulas_cg_varible1 = formulas_cg_varible1;
    }

    public void setFormulas_cg_varible2(float formulas_cg_varible2) {
        this.formulas_cg_varible2 = formulas_cg_varible2;
    }

    public void setFormulas_cg_min(float formulas_cg_min) {
        this.formulas_cg_min = formulas_cg_min;
    }

    public void setFormulas_cg_max(float formulas_cg_max) {
        this.formulas_cg_max = formulas_cg_max;
    }

    public void setManana_desayuno_fuerte(float manana_desayuno_fuerte) {
        this.manana_desayuno_fuerte = manana_desayuno_fuerte;
    }

    public void setManana_almuerzo_ligero(float manana_almuerzo_ligero) {
        this.manana_almuerzo_ligero = manana_almuerzo_ligero;
    }

    public void setManana_desayuno_mediano(float manana_desayuno_mediano) {
        this.manana_desayuno_mediano = manana_desayuno_mediano;
    }

    public void setManana_almuerzo_mediano(float manana_almuerzo_mediano) {
        this.manana_almuerzo_mediano = manana_almuerzo_mediano;
    }

    public void setManana_desayuno_ligero(float manana_desayuno_ligero) {
        this.manana_desayuno_ligero = manana_desayuno_ligero;
    }

    public void setManana_almuerzo_fuerte(float manana_almuerzo_fuerte) {
        this.manana_almuerzo_fuerte = manana_almuerzo_fuerte;
    }

    public void setMediodia_priomeroligero(float mediodia_priomeroligero) {
        this.mediodia_priomeroligero = mediodia_priomeroligero;
    }

    public void setMediodia_segundofuerte(float mediodia_segundofuerte) {
        this.mediodia_segundofuerte = mediodia_segundofuerte;
    }

    public void setMediodia_primeromedio(float mediodia_primeromedio) {
        this.mediodia_primeromedio = mediodia_primeromedio;
    }

    public void setMediodia_segundomedio(float mediodia_segundomedio) {
        this.mediodia_segundomedio = mediodia_segundomedio;
    }

    public void setMediodia_postre(float mediodia_postre) {
        this.mediodia_postre = mediodia_postre;
    }

    public void setMerienda(float merienda) {
        this.merienda = merienda;
    }

    public void setNoche_priomeroligero(float noche_priomeroligero) {
        this.noche_priomeroligero = noche_priomeroligero;
    }

    public void setNoche_segundofuerte(float noche_segundofuerte) {
        this.noche_segundofuerte = noche_segundofuerte;
    }

    public void setNoche_primeromedio(float noche_primeromedio) {
        this.noche_primeromedio = noche_primeromedio;
    }

    public void setNoche_segundomedio(float noche_segundomedio) {
        this.noche_segundomedio = noche_segundomedio;
    }

    public void setNoche_postre(float noche_postre) {
        this.noche_postre = noche_postre;
    }

    public void setMenu24_perder(float menu24_perder) {
        this.menu24_perder = menu24_perder;
    }

    public void setMenu24_aumentar(float menu24_aumentar) {
        this.menu24_aumentar = menu24_aumentar;
    }

    public void setLink_perfil(String link_perfil) {
        this.link_perfil = link_perfil;
    }

    public void setLink_menu24h(String link_menu24h) {
        this.link_menu24h = link_menu24h;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "ExtensionsProfile{" +
                "_id=" + _id +
                ", formulas_gr_sem_kilo=" + formulas_gr_sem_kilo +
                ", formulas_cg_base=" + formulas_cg_base +
                ", formulas_cg_varible1=" + formulas_cg_varible1 +
                ", formulas_cg_varible2=" + formulas_cg_varible2 +
                ", formulas_cg_min=" + formulas_cg_min +
                ", formulas_cg_max=" + formulas_cg_max +
                ", manana_desayuno_fuerte=" + manana_desayuno_fuerte +
                ", manana_almuerzo_ligero=" + manana_almuerzo_ligero +
                ", manana_desayuno_mediano=" + manana_desayuno_mediano +
                ", manana_almuerzo_mediano=" + manana_almuerzo_mediano +
                ", manana_desayuno_ligero=" + manana_desayuno_ligero +
                ", manana_almuerzo_fuerte=" + manana_almuerzo_fuerte +
                ", mediodia_priomeroligero=" + mediodia_priomeroligero +
                ", mediodia_segundofuerte=" + mediodia_segundofuerte +
                ", mediodia_primeromedio=" + mediodia_primeromedio +
                ", mediodia_segundomedio=" + mediodia_segundomedio +
                ", mediodia_postre=" + mediodia_postre +
                ", merienda=" + merienda +
                ", noche_priomeroligero=" + noche_priomeroligero +
                ", noche_segundofuerte=" + noche_segundofuerte +
                ", noche_primeromedio=" + noche_primeromedio +
                ", noche_segundomedio=" + noche_segundomedio +
                ", noche_postre=" + noche_postre +
                ", menu24_perder=" + menu24_perder +
                ", menu24_aumentar=" + menu24_aumentar +
                ", link_perfil='" + link_perfil + '\'' +
                ", link_menu24h='" + link_menu24h + '\'' +
                '}';
    }
}
