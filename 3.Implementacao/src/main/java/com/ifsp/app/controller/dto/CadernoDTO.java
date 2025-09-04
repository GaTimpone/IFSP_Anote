package com.ifsp.app.controller.dto;

public class CadernoDTO {
    private String titulo;
    private Long usuarioId;

    public CadernoDTO() {
    }

    public CadernoDTO(String titulo, Long usuarioId) {
        this.titulo = titulo;
        this.usuarioId = usuarioId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
