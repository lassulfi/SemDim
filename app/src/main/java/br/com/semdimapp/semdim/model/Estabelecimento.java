package br.com.semdimapp.semdim.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;

public class Estabelecimento implements Parcelable {

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

    public Estabelecimento(String id, String nome, String email, String endereco,
                           double latitude, double longitude, ArrayList<Promocao> promocoes){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
        this.promocoes = promocoes;
    }

    public Estabelecimento(Parcel parcel){
        this.id = parcel.readString();
        this.nome = parcel.readString();
        this.email = parcel.readString();
        this.endereco = parcel.readString();
        this.latitude = parcel.readDouble();
        this.longitude = parcel.readDouble();

        if(this.promocoes == null){
            promocoes = new ArrayList<>();
        }

        parcel.readTypedList(promocoes, Promocao.CREATOR);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.nome);
        dest.writeString(this.email);
        dest.writeString(this.endereco);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeTypedList(this.promocoes);
    }

    public static final Creator<Estabelecimento> CREATOR = new Creator<Estabelecimento>() {
        @Override
        public Estabelecimento createFromParcel(Parcel source) {
            return new Estabelecimento(source);
        }

        @Override
        public Estabelecimento[] newArray(int size) {
            return new Estabelecimento[size];
        }
    };
}
