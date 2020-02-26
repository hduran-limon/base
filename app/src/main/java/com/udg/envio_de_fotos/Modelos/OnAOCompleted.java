package com.udg.envio_de_fotos.Modelos;

public interface OnAOCompleted {
    /** Se llama cuando termina el metodo alta en ABCNumOrden */
    void OnAltaCompleted(String respuesta);
    /** Se llama cuando termina el metodo obtenerCampo1 en ABCNumOrden*/
    void OnObtenerCampoCompleted(String campo_1);
}
