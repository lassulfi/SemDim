package br.com.semdimapp.semdim.model;

import java.util.ArrayList;
import java.util.Collections;

public class Estabelecimento {

    //Atributos
    private String id;
    private String nome;
    private String email;
    private String endereco;
    private double latitude;
    private double longitude;

    private ArrayList<Promocao> promocoes;

    public Estabelecimento(){
    }

    /**
     * Define o id do estabelecimento
     * @param id
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Retorna o id so estabelecimento
     * @return
     */
    public String getId(){
        return id;
    }

    /**
     * Define o nome do estabelecimento
     * @param nome
     */
    public void setNome(String nome){
        this.nome = nome;
    }

    /**
     * Retorna o nome o estabelecimento
     * @return
     */
    public String getNome(){
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEndereco(String endereco){
        this.endereco = endereco;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setCoordenadas(double latitude, double longitude){

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    /**
     * Adiciona uma promocao ao estabelecimento
     * @param promocao
     */
    public void addPromocao(Promocao promocao){
        //Verifica se a lista de promocoes ainda não foi instanciada
        if(promocoes == null){
            promocoes = new ArrayList<>();
        }
        promocoes.add(promocao);
    }

    /**
     * Retorna a lista de promocoes
     * @return
     */
    public ArrayList<Promocao> getPromocoes() {
        return promocoes;
    }

    /**
     * Remove uma promocao em um determinado indice
     * @param index
     */
    public void excluirPromocao(int index){
        promocoes.remove(index);
    }

    public void ordenarPromocoes(){
        Collections.sort(promocoes);
    }

    /**
     * Recupera o valor da menor promocao
     * @return
     */
    public float obterMenorValor(){
        return promocoes.get(0).getValor();
    }
}
