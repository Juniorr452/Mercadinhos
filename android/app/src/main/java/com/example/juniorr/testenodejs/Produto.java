package com.example.juniorr.testenodejs;

/**
 * Created by junio on 11/02/2018.
 */

class Produto
{
  /**
   * nome : Tênis
   * PRECO : 69.99
   */

  private String nome;
  private double preço;

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public double getPreço() {
    return preço;
  }

  public void setPreço(double preço) {
    this.preço = preço;
  }
}
