package br.com.kwave.naRegua.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity // Indica que esta classe é uma entidade JPA e será mapeada para uma tabela no banco
public class Barbearia {

    @Id // Marca o campo 'id' como chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura a geração automática do ID
    private Long id;

    @Column(nullable = false) // Indica que o campo não pode ser nulo
    private String nome;

    @Column(nullable = false, unique = true) // Email único
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String telefone;

    @Column
    private String endereco;
    @Column
    private String urlFotoPerfil;

    public Barbearia() {
    }

    public Barbearia(String nome, String username, String email, String senha, String telefone, String endereco,
                     String horarioFuncionamento, String descricao, String urlFotoPerfil, String urlFotoCapa) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.endereco = endereco;
        this.urlFotoPerfil = urlFotoPerfil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getUrlFotoPerfil() {
        return urlFotoPerfil;
    }

    public void setUrlFotoPerfil(String urlFotoPerfil) {
        this.urlFotoPerfil = urlFotoPerfil;
    }

    @Override
    public String toString() {
        return "Barbearia{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + "[PROTECTED]" + '\'' +
                ", telefone='" + telefone + '\'' +
                ", endereco='" + endereco + '\'' +
                ", urlFotoPerfil='" + urlFotoPerfil + '\'' +
                '}';
    }
}