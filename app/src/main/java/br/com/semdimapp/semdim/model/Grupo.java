package br.com.semdimapp.semdim.model;

import java.util.ArrayList;

/**
 * Classe que armazena as informações do grupo definido pelo usuario
 */
public class Grupo {

    //Atributos
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
    }

    /**
     * Adiciona um novo contato ao ArrayList de contatos
     * @param contato objeto da Classe Contato
     */
    public void addContato(Contato contato){
        contatos.add(contato);
    }

    /**
     * Retorna a lista de todos os contatos cadastrados no grupo
     * @return ArrayList contendo todos os contatos do grupo
     */
    public ArrayList<Contato> getContatos() {
        return contatos;
    }
}
