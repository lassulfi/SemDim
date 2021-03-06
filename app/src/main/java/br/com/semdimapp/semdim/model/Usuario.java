package br.com.semdimapp.semdim.model;

/**
 * Classe usuario.
 */
public class Usuario {

    //Atributos
    private String id; //ID do usuario
    private String nome; //username do usuario
    private String email; //email
    private String senha; //senha
    public float valor;

    //Construtor
    public Usuario(){
        //Construtor padrao para chamar DataSnapshot.getValue(Usuario.class)
    }

    public Usuario( String nome, String email, String senha){
        this.id = null;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    //Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Usuario: nome: "
                + nome + ", email: " + email;
    }
}
