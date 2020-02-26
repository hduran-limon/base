package com.udg.envio_de_fotos.Modelos;

public interface OnABCCompleted {
    /***
     * Se llama cuando termina la ejecucion del metodo baja
     * @param res
     */
    void OnBajaCompleted(String[] res);

    /**
     * Se llama cuando termina la ejecucion del metodo actualiza, en la clase "NumOrdenes" se llama cuando
     * termina la ejecucion del metodo buscaHoy.
     * @param res
     */
    void OnActualizarCompleted(String res);

    /**
     * Se llama cuando termina la ejecucion del metodo buscaDatos
     * @param res
     */
    void OnBuscaDatosCompleted(String[] res);

    /**
     * Se llama cuando termina la ejecucion del metodo terminar, en la clase "NumOrdenes" se llama cuando
     * termina la ejecucion del metodo buscaCualquierDia
     * @param res
     */
    void OnTerminarCompleted(String res);
}
