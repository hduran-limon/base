package com.udg.envio_de_fotos.Modelos;

public interface OnTaskCompleted {
    /**
     * Se llama cuando termina la ejecución del método sendEmail.
     * @param msj
     */
    void onSendCompleted(String msj);


}
