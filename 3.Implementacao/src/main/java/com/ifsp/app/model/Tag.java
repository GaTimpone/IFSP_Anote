package com.ifsp.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ifsp.app.model.enums.Cor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    @JsonIgnore
    private List<Anotacao> anotacoes;

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    @JsonIgnore
    private List<Caderno> cadernos;

    private String nome;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "A cor da tag é obrigatória")
    private Cor cor;

    public Long getId() {
        return id;
    }

    public List<Anotacao> getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(List<Anotacao> anotacoes) {
        this.anotacoes = anotacoes;
    }

    public List<Caderno> getCadernos() {
        return cadernos;
    }

    public void setCadernos(List<Caderno> cadernos) {
        this.cadernos = cadernos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }
}
