package br.com.semdimapp.semdim.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Classe que armazena as promocoes dos estabelecimentos
 */
public class Promocao implements Comparable<Promocao>, Parcelable {

    //Atributos
    private String nome;
    private String descricao;
    private float valor;

    public Promocao(){
        //Construtor padrao para chamar DataSnapshot.getValue(Promocao.class)
    }

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

    public Promocao(Parcel in) {
        nome = in.readString();
        descricao = in.readString();
        valor = in.readFloat();
    }

    public static final Creator<Promocao> CREATOR = new Creator<Promocao>() {
        @Override
        public Promocao createFromParcel(Parcel in) {
            return new Promocao(in);
        }

        @Override
        public Promocao[] newArray(int size) {
            return new Promocao[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nome);
        dest.writeString(this.descricao);
        dest.writeFloat(this.valor);
    }
}
