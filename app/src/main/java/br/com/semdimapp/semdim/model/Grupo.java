package br.com.semdimapp.semdim.model;

import java.util.ArrayList;

/**
 * Classe que armazena as informações do grupo definido pelo usuario
 */
public class Grupo {

    //Atributos
    private String nome;
    private float valor = 0.0f;

    private Usuario criador; //Usuario que criou o grupo
    private ArrayList<Contato> contatos; //ArrayList de Contatos

    //Construtor padrao
    public Grupo(){
        //Construtor padrao para chamar DataSnapshot.getValue(Grupo.class)
    }

    /**
     * Construtor da classe Grupo
     * @param criador objeto da classe Usuario responsálvel pela criacao do grupo
     */
    public Grupo(Usuario criador){
        this.criador = criador;
        contatos = new ArrayList<>();
        valor += criador.getValor();
    }

    /**
     * Adiciona um novo contato ao ArrayList de contatos
     * @param contato objeto da Classe Contato
     */
    public void addContato(Contato contato){
        contatos.add(contato);
        //Contabiliza o valor do contato ao grupo
        valor += contato.getValor();
    }

    /**
     * Retorna a lista de todos os contatos cadastrados no grupo
     * @return ArrayList contendo todos os contatos do grupo
     */
    public ArrayList<Contato> getContatos() {
        return contatos;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return nome;
    }
}
