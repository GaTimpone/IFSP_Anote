package com.ifsp.app.controller.dto;

public class AnotacaoDTO {
    private String titulo;
    private String corpo;
    private Long usuarioId;
    private Long cadernoId;

    public AnotacaoDTO() {
    }

    public AnotacaoDTO(String titulo, String corpo, Long usuarioId, Long cadernoId) {
        this.titulo = titulo;
        this.corpo = corpo;
        this.usuarioId = usuarioId;
        this.cadernoId = cadernoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getCadernoId() {
        return cadernoId;
    }

    public void setCadernoId(Long cadernoId) {
        this.cadernoId = cadernoId;
    }
}
