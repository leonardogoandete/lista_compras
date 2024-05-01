package br.com.ifrs.listadecompras.model;


import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

//@Entity
public class Produto implements Serializable {

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

    public static List<Produto> inicializaListaProdutos() {
        List<Produto> produtos = new ArrayList<>();
        produtos.add(new Produto("Arroz", 1, "Tio João", 5.99));
        produtos.add(new Produto("Feijão", 1, "Tio João", 4.99));
        produtos.add(new Produto("Macarrão", 1, "Renata", 3.99));
        produtos.add(new Produto("Açúcar", 1, "União", 2.99));
        produtos.add(new Produto("Café", 1, "Melitta", 7.99));
        produtos.add(new Produto("Leite", 1, "Piracanjuba", 3.99));
        produtos.add(new Produto("Óleo", 1, "Liza", 3.99));
        produtos.add(new Produto("Sal", 1, "Cisne", 1.99));
        produtos.add(new Produto("Farinha", 1, "Dona Benta", 2.99));
        produtos.add(new Produto("Molho de Tomate", 1, "Quero", 2.99));
        return produtos;
    }
}
