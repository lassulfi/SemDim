package br.com.semdimapp.semdim.model;

import android.support.annotation.NonNull;

/**
 * Classe que armazena as promocoes dos estabelecimentos
 */
public class Promocao implements Comparable<Promocao> {

    //Atributos
    private String nome;
    private String descricao;
    private float valor;

    /**
     * Construtor da classe promocao
     * @param nome
     * @param descricao
     * @param valor
     */
    public Promocao(String nome, String descricao, float valor){
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
    }

    /**
     * Retorna o nome da promocao
     * @return
     */
    public String getNome(){
        return nome;
    }

    /**
     * Retorna a descricao da promocao
     * @return
     */
    public String getDescricao(){
        return descricao;
    }

    /**
     * Retorna o valor da promocao
     * @return
     */
    public float getValor(){
        return valor;
    }

    @Override
    public int compareTo(Promocao promocao2) {
        return (int)(getValor() - promocao2.getValor());
    }
}
