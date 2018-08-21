package br.com.semdimapp.semdim.model;

/**
 * Classe Contato dos usuarios do App
 */
public class Contato {

    //Atributos
    private String id; //ID do contato
    private String nome; //Nome do contato
    private String email; //Email do contato
    private float valor; //Valor que o contato pode gastar

    //Construtor
    public Contato(){
        //Construtor padrao para chamar DataSnapshot.getValue(Contato.class)
    }

    public Contato(String nome, String email){
        this.nome = nome;
        this.email = email;
    }

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

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
}
