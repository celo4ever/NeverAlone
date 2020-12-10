package com.example.neveralone.Activity.Chat;


public class MessageRecibir extends Message {
    private Long hora;

    public MessageRecibir() {
    }

    public MessageRecibir(Long hora) {
        this.hora = hora;
    }

    public MessageRecibir(String mensaje, String nombre, String fotoPerfil, String type_mensaje, String idPeticion, Long hora) {
        super(mensaje, nombre, fotoPerfil, type_mensaje, idPeticion);
        this.hora = hora;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }

}
