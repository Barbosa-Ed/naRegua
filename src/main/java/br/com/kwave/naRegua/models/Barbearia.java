package br.com.kwave.naRegua.models;

import org.springframework.data.annotation.Id;

public class Barbearia {

    @Id
    private String id;
    private String nome;
    private double nota;
    private String logo;
    private String endereco;

    public Barbearia() {}

    public Barbearia(String nome, double nota, String logo, String endereco) {
        this.nome = nome;
        this.nota = nota;
        this.logo = logo;
        this.endereco = endereco;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getNota() {
        return nota;
    }

    public String getLogo() {
        return logo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
