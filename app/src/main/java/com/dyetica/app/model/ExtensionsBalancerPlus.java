package com.dyetica.app.model;


/**
 * Created by Jess on 01/01/2017.
 */

public class ExtensionsBalancerPlus {

    private long _id;
    private int activado;
    private int num_alimentos;
    private int raciones;
    private String otros;
    private int multiplica;
    private float limite_hidratos;
    private float limite_proteinas;
    private float valor_qp;
    private float valor_qr;
    private String grasas;
    private String frutas;
    private String verduras;
    private String raices;
    private String carnes;
    private String embutidos;
    private String mariscos;
    private String legumbres;
    private String pescados;
    private String cereales;
    private String dulces;
    private String bebidas;
    private String frutos_secos;
    private String lacteos;
    private String salsas;
    private String huevos;
    private String codigo_personal;
    private int maximo_alimentos;
    private String lacteos_quesos_huevos_compensa2;
    private String pescados_mariscos_compensa2;
    private String carnes_compensa2;
    private String embutidos_compensa2;
    private String nueces_compensa2;
    private String legumbres_semillas_raices_compensa2;
    private String cereales_pastas_compensa2;
    private String frutas_bebidas_compensa2;
    private String dulces_compensa2;
    private String patatas_compensa2;
    private float exceso_hidratos_compensa2;
    private float exceso_proteinas_compensa2;
    private String link;
    private String link_compensa;
    private String link_equilibrador;
    private String link_equilibrador_plus;
    private float desayuno_necesidades;
    private float almuerzo_necesidades;
    private float comida_necesidades;
    private float merienda_necesidades;
    private float cena_necesidades;
    private ExtensionsBalancerPlus extensionsBalancerPlus;

    public static synchronized ExtensionsBalancerPlus getInstance(ExtensionsBalancerPlus extensionsBalancerPlus){
        if (null == extensionsBalancerPlus){
            extensionsBalancerPlus = new ExtensionsBalancerPlus(extensionsBalancerPlus);
        }
        return extensionsBalancerPlus;
    }

    private ExtensionsBalancerPlus(ExtensionsBalancerPlus extensionsBalancerPlus){
        this.extensionsBalancerPlus = extensionsBalancerPlus;
    }

    public ExtensionsBalancerPlus(int cena_necesidades, int activado, int num_alimentos, int raciones, String otros, int multiplica, float limite_hidratos, float limite_proteinas, float valor_qp, float valor_qr, String grasas, String frutas, String verduras, String raices, String carnes, String embutidos, String mariscos, String legumbres, String pescados, String cereales, String dulces, String bebidas, String frutos_secos, String lacteos, String salsas, String huevos, String codigo_personal, int maximo_alimentos, String lacteos_quesos_huevos_compensa2, String pescados_mariscos_compensa2, String carnes_compensa2, String embutidos_compensa2, String nueces_compensa2, String legumbres_semillas_raices_compensa2, String cereales_pastas_compensa2, String frutas_bebidas_compensa2, String dulces_compensa2, String patatas_compensa2, float exceso_hidratos_compensa2, float exceso_proteinas_compensa2, String link, String link_compensa, String link_equilibrador, String link_equilibrador_plus, int desayuno_necesidades, int almuerzo_necesidades, int comida_necesidades, int merienda_necesidades) {
        this.cena_necesidades = cena_necesidades;
        this.activado = activado;
        this.num_alimentos = num_alimentos;
        this.raciones = raciones;
        this.otros = otros;
        this.multiplica = multiplica;
        this.limite_hidratos = limite_hidratos;
        this.limite_proteinas = limite_proteinas;
        this.valor_qp = valor_qp;
        this.valor_qr = valor_qr;
        this.grasas = grasas;
        this.frutas = frutas;
        this.verduras = verduras;
        this.raices = raices;
        this.carnes = carnes;
        this.embutidos = embutidos;
        this.mariscos = mariscos;
        this.legumbres = legumbres;
        this.pescados = pescados;
        this.cereales = cereales;
        this.dulces = dulces;
        this.bebidas = bebidas;
        this.frutos_secos = frutos_secos;
        this.lacteos = lacteos;
        this.salsas = salsas;
        this.huevos = huevos;
        this.codigo_personal = codigo_personal;
        this.maximo_alimentos = maximo_alimentos;
        this.lacteos_quesos_huevos_compensa2 = lacteos_quesos_huevos_compensa2;
        this.pescados_mariscos_compensa2 = pescados_mariscos_compensa2;
        this.carnes_compensa2 = carnes_compensa2;
        this.embutidos_compensa2 = embutidos_compensa2;
        this.nueces_compensa2 = nueces_compensa2;
        this.legumbres_semillas_raices_compensa2 = legumbres_semillas_raices_compensa2;
        this.cereales_pastas_compensa2 = cereales_pastas_compensa2;
        this.frutas_bebidas_compensa2 = frutas_bebidas_compensa2;
        this.dulces_compensa2 = dulces_compensa2;
        this.patatas_compensa2 = patatas_compensa2;
        this.exceso_hidratos_compensa2 = exceso_hidratos_compensa2;
        this.exceso_proteinas_compensa2 = exceso_proteinas_compensa2;
        this.link = link;
        this.link_compensa = link_compensa;
        this.link_equilibrador = link_equilibrador;
        this.link_equilibrador_plus = link_equilibrador_plus;
        this.desayuno_necesidades = desayuno_necesidades;
        this.almuerzo_necesidades = almuerzo_necesidades;
        this.comida_necesidades = comida_necesidades;
        this.merienda_necesidades = merienda_necesidades;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public int getActivado() {
        return activado;
    }

    public void setActivado(int activado) {
        this.activado = activado;
    }

    public int getNum_alimentos() {
        return num_alimentos;
    }

    public void setNum_alimentos(int num_alimentos) {
        this.num_alimentos = num_alimentos;
    }

    public int getRaciones() {
        return raciones;
    }

    public void setRaciones(int raciones) {
        this.raciones = raciones;
    }

    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }

    public int getMultiplica() {
        return multiplica;
    }

    public void setMultiplica(int multiplica) {
        this.multiplica = multiplica;
    }

    public float getLimite_hidratos() {
        return limite_hidratos;
    }

    public void setLimite_hidratos(float limite_hidratos) {
        this.limite_hidratos = limite_hidratos;
    }

    public float getLimite_proteinas() {
        return limite_proteinas;
    }

    public void setLimite_proteinas(float limite_proteinas) {
        this.limite_proteinas = limite_proteinas;
    }

    public float getValor_qp() {
        return valor_qp;
    }

    public void setValor_qp(float valor_qp) {
        this.valor_qp = valor_qp;
    }

    public float getValor_qr() {
        return valor_qr;
    }

    public void setValor_qr(float valor_qr) {
        this.valor_qr = valor_qr;
    }

    public String getGrasas() {
        return grasas;
    }

    public void setGrasas(String grasas) {
        this.grasas = grasas;
    }

    public String getFrutas() {
        return frutas;
    }

    public void setFrutas(String frutas) {
        this.frutas = frutas;
    }

    public String getVerduras() {
        return verduras;
    }

    public void setVerduras(String verduras) {
        this.verduras = verduras;
    }

    public String getRaices() {
        return raices;
    }

    public void setRaices(String raices) {
        this.raices = raices;
    }

    public String getCarnes() {
        return carnes;
    }

    public void setCarnes(String carnes) {
        this.carnes = carnes;
    }

    public String getEmbutidos() {
        return embutidos;
    }

    public void setEmbutidos(String embutidos) {
        this.embutidos = embutidos;
    }

    public String getMariscos() {
        return mariscos;
    }

    public void setMariscos(String mariscos) {
        this.mariscos = mariscos;
    }

    public String getLegumbres() {
        return legumbres;
    }

    public void setLegumbres(String legumbres) {
        this.legumbres = legumbres;
    }

    public String getPescados() {
        return pescados;
    }

    public void setPescados(String pescados) {
        this.pescados = pescados;
    }

    public String getCereales() {
        return cereales;
    }

    public void setCereales(String cereales) {
        this.cereales = cereales;
    }

    public String getDulces() {
        return dulces;
    }

    public void setDulces(String dulces) {
        this.dulces = dulces;
    }

    public String getBebidas() {
        return bebidas;
    }

    public void setBebidas(String bebidas) {
        this.bebidas = bebidas;
    }

    public String getFrutos_secos() {
        return frutos_secos;
    }

    public void setFrutos_secos(String frutos_secos) {
        this.frutos_secos = frutos_secos;
    }

    public String getLacteos() {
        return lacteos;
    }

    public void setLacteos(String lacteos) {
        this.lacteos = lacteos;
    }

    public String getSalsas() {
        return salsas;
    }

    public void setSalsas(String salsas) {
        this.salsas = salsas;
    }

    public String getHuevos() {
        return huevos;
    }

    public void setHuevos(String huevos) {
        this.huevos = huevos;
    }

    public String getCodigo_personal() {
        return codigo_personal;
    }

    public void setCodigo_personal(String codigo_personal) {
        this.codigo_personal = codigo_personal;
    }

    public int getMaximo_alimentos() {
        return maximo_alimentos;
    }

    public void setMaximo_alimentos(int maximo_alimentos) {
        this.maximo_alimentos = maximo_alimentos;
    }

    public String getLacteos_quesos_huevos_compensa2() {
        return lacteos_quesos_huevos_compensa2;
    }

    public void setLacteos_quesos_huevos_compensa2(String lacteos_quesos_huevos_compensa2) {
        this.lacteos_quesos_huevos_compensa2 = lacteos_quesos_huevos_compensa2;
    }

    public String getPescados_mariscos_compensa2() {
        return pescados_mariscos_compensa2;
    }

    public void setPescados_mariscos_compensa2(String pescados_mariscos_compensa2) {
        this.pescados_mariscos_compensa2 = pescados_mariscos_compensa2;
    }

    public String getCarnes_compensa2() {
        return carnes_compensa2;
    }

    public void setCarnes_compensa2(String carnes_compensa2) {
        this.carnes_compensa2 = carnes_compensa2;
    }

    public String getEmbutidos_compensa2() {
        return embutidos_compensa2;
    }

    public void setEmbutidos_compensa2(String embutidos_compensa2) {
        this.embutidos_compensa2 = embutidos_compensa2;
    }

    public String getNueces_compensa2() {
        return nueces_compensa2;
    }

    public void setNueces_compensa2(String nueces_compensa2) {
        this.nueces_compensa2 = nueces_compensa2;
    }

    public String getLegumbres_semillas_raices_compensa2() {
        return legumbres_semillas_raices_compensa2;
    }

    public void setLegumbres_semillas_raices_compensa2(String legumbres_semillas_raices_compensa2) {
        this.legumbres_semillas_raices_compensa2 = legumbres_semillas_raices_compensa2;
    }

    public String getCereales_pastas_compensa2() {
        return cereales_pastas_compensa2;
    }

    public void setCereales_pastas_compensa2(String cereales_pastas_compensa2) {
        this.cereales_pastas_compensa2 = cereales_pastas_compensa2;
    }

    public String getFrutas_bebidas_compensa2() {
        return frutas_bebidas_compensa2;
    }

    public void setFrutas_bebidas_compensa2(String frutas_bebidas_compensa2) {
        this.frutas_bebidas_compensa2 = frutas_bebidas_compensa2;
    }

    public String getDulces_compensa2() {
        return dulces_compensa2;
    }

    public void setDulces_compensa2(String dulces_compensa2) {
        this.dulces_compensa2 = dulces_compensa2;
    }

    public String getPatatas_compensa2() {
        return patatas_compensa2;
    }

    public void setPatatas_compensa2(String patatas_compensa2) {
        this.patatas_compensa2 = patatas_compensa2;
    }

    public float getExceso_hidratos_compensa2() {
        return exceso_hidratos_compensa2;
    }

    public void setExceso_hidratos_compensa2(float exceso_hidratos_compensa2) {
        this.exceso_hidratos_compensa2 = exceso_hidratos_compensa2;
    }

    public float getExceso_proteinas_compensa2() {
        return exceso_proteinas_compensa2;
    }

    public void setExceso_proteinas_compensa2(float exceso_proteinas_compensa2) {
        this.exceso_proteinas_compensa2 = exceso_proteinas_compensa2;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink_compensa() {
        return link_compensa;
    }

    public void setLink_compensa(String link_compensa) {
        this.link_compensa = link_compensa;
    }

    public String getLink_equilibrador() {
        return link_equilibrador;
    }

    public void setLink_equilibrador(String link_equilibrador) {
        this.link_equilibrador = link_equilibrador;
    }

    public String getLink_equilibrador_plus() {
        return link_equilibrador_plus;
    }

    public void setLink_equilibrador_plus(String link_equilibrador_plus) {
        this.link_equilibrador_plus = link_equilibrador_plus;
    }

    public float getDesayuno_necesidades() {
        return desayuno_necesidades;
    }

    public void setDesayuno_necesidades(float desayuno_necesidades) {
        this.desayuno_necesidades = desayuno_necesidades;
    }

    public float getAlmuerzo_necesidades() {
        return almuerzo_necesidades;
    }

    public void setAlmuerzo_necesidades(float almuerzo_necesidades) {
        this.almuerzo_necesidades = almuerzo_necesidades;
    }

    public float getComida_necesidades() {
        return comida_necesidades;
    }

    public void setComida_necesidades(float comida_necesidades) {
        this.comida_necesidades = comida_necesidades;
    }

    public float getMerienda_necesidades() {
        return merienda_necesidades;
    }

    public void setMerienda_necesidades(float merienda_necesidades) {
        this.merienda_necesidades = merienda_necesidades;
    }

    public float getCena_necesidades() {
        return cena_necesidades;
    }

    public void setCena_necesidades(float cena_necesidades) {
        this.cena_necesidades = cena_necesidades;
    }

    @Override
    public String toString() {
        return "ExtensionsBalancerPlus{" +
                "_id=" + _id +
                "activado=" + activado +
                ", num_alimentos=" + num_alimentos +
                ", raciones=" + raciones +
                ", otros='" + otros + '\'' +
                ", multiplica=" + multiplica +
                ", limite_hidratos=" + limite_hidratos +
                ", limite_proteinas=" + limite_proteinas +
                ", valor_qp=" + valor_qp +
                ", valor_qr=" + valor_qr +
                ", grasas='" + grasas + '\'' +
                ", frutas='" + frutas + '\'' +
                ", verduras='" + verduras + '\'' +
                ", raices='" + raices + '\'' +
                ", carnes='" + carnes + '\'' +
                ", embutidos='" + embutidos + '\'' +
                ", mariscos='" + mariscos + '\'' +
                ", legumbres='" + legumbres + '\'' +
                ", pescados='" + pescados + '\'' +
                ", cereales='" + cereales + '\'' +
                ", dulces='" + dulces + '\'' +
                ", bebidas='" + bebidas + '\'' +
                ", frutos_secos='" + frutos_secos + '\'' +
                ", lacteos='" + lacteos + '\'' +
                ", salsas='" + salsas + '\'' +
                ", huevos='" + huevos + '\'' +
                ", codigo_personal='" + codigo_personal + '\'' +
                ", maximo_alimentos=" + maximo_alimentos +
                ", lacteos_quesos_huevos_compensa2='" + lacteos_quesos_huevos_compensa2 + '\'' +
                ", pescados_mariscos_compensa2='" + pescados_mariscos_compensa2 + '\'' +
                ", carnes_compensa2='" + carnes_compensa2 + '\'' +
                ", embutidos_compensa2='" + embutidos_compensa2 + '\'' +
                ", nueces_compensa2='" + nueces_compensa2 + '\'' +
                ", legumbres_semillas_raices_compensa2='" + legumbres_semillas_raices_compensa2 + '\'' +
                ", cereales_pastas_compensa2='" + cereales_pastas_compensa2 + '\'' +
                ", frutas_bebidas_compensa2='" + frutas_bebidas_compensa2 + '\'' +
                ", dulces_compensa2='" + dulces_compensa2 + '\'' +
                ", patatas_compensa2='" + patatas_compensa2 + '\'' +
                ", exceso_hidratos_compensa2=" + exceso_hidratos_compensa2 +
                ", exceso_proteinas_compensa2=" + exceso_proteinas_compensa2 +
                ", link='" + link + '\'' +
                ", link_compensa='" + link_compensa + '\'' +
                ", link_equilibrador='" + link_equilibrador + '\'' +
                ", link_equilibrador_plus='" + link_equilibrador_plus + '\'' +
                ", desayuno_necesidades=" + desayuno_necesidades +
                ", almuerzo_necesidades=" + almuerzo_necesidades +
                ", comida_necesidades=" + comida_necesidades +
                ", merienda_necesidades=" + merienda_necesidades +
                ", cena_necesidades=" + cena_necesidades +
                '}';
    }
}
