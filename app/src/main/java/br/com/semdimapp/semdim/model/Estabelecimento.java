package br.com.semdimapp.semdim.model;

public class Estabelecimento {

    //Atributos
    private String id;
    private String nome;
    private String email;

    public Estabelecimento(){}

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return nome;
    }
}
