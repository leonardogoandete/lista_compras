package br.com.ifrs.listadecompras.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Produto implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nome;
    private int quantidade;

    private String marca;

    private Double valor;

    public Produto() {}

    public Produto(String nome, int quantidade, String marca, Double valor) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.marca = marca;
        this.valor = valor;
    }

    protected Produto(Parcel in) {
        nome = in.readString();
        quantidade = in.readInt();
        marca = in.readString();
        if (in.readByte() == 0) {
            valor = null;
        } else {
            valor = in.readDouble();
        }
    }

    public static final Creator<Produto> CREATOR = new Creator<Produto>() {
        @Override
        public Produto createFromParcel(Parcel in) {
            return new Produto(in);
        }

        @Override
        public Produto[] newArray(int size) {
            return new Produto[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getMarca() {
        return marca;
    }

    public Double getValor() {
        return valor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int i) {
        dest.writeString(nome);
        dest.writeInt(quantidade);
        dest.writeString(marca);
        if (valor == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(valor);
        }
    }
}
