package com.udg.envio_de_fotos.Modelos;

public class NumOrden {
    private String numOrden;
    private String id;
    private String fecha;

    public NumOrden(String numOrden, String id, String fecha) {
        this.numOrden = numOrden;
        this.id = id;
        this.fecha = fecha;
    }

    /**
     * Devuelve numOrden
     * @return
     */
    public String getNumOrden() {
        return numOrden;
    }
    /**
     * Devuelve id
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Devuelve fecha
     * @return
     */
    public String getFecha() {
        return fecha;
    }


}
