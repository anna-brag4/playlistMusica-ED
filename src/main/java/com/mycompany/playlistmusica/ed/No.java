package com.mycompany.playlistmusica.ed;

import java.time.LocalDate;

public class No {

    // Dados da música
    private String titulo;
    private String artista;
    private String caminhoOuUrl;
    private LocalDate dataInclusao;
    private int duracao; // em segundos

    // Ponteiros da lista duplamente encadeada
    private No proximo;
    private No anterior;

    // Construtor
  
    public No(String titulo, String artista, String caminhoOuUrl, int duracao) {
        this.titulo       = titulo;
        this.artista      = artista;
        this.caminhoOuUrl = caminhoOuUrl;
        this.dataInclusao = LocalDate.now();
        this.duracao      = duracao;
        this.proximo      = null;
        this.anterior     = null;
    }
    
    /*
    // Construtor 2
  
    public No(String titulo, String artista, String caminhoOuUrl, int duracao) {
        this(titulo, artista, caminhoOuUrl);
        this.duracao = duracao;
    }
    */

    // Getters
    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public String getCaminhoOuUrl() {
        return caminhoOuUrl;
    }

    public LocalDate getDataInclusao() {
        return dataInclusao;
    }

    public int getDuracao() {
        return duracao;
    }

    public No getProximo() {
        return proximo;
    }

    public No getAnterior() {
        return anterior;
    }
 
    // Setters
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public void setCaminhoOuUrl(String caminhoOuUrl) {
        this.caminhoOuUrl = caminhoOuUrl;
    }

    public void setDataInclusao(LocalDate dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public void setProximo(No proximo) {
        this.proximo = proximo;
    }

    public void setAnterior(No anterior) {
        this.anterior = anterior;
    }
    
    //métodos de correção
    public String corrigirTitulo(String tituloNovo) {
        if (tituloNovo == null || tituloNovo.isEmpty()) {
            return "Titulo inválido!";
        } else {
            this.titulo = tituloNovo;
            return "Alteração concluida :)";
        }
    }
    
    public String corrigirArtista(String artistaNovo) {
        if (artistaNovo == null || artistaNovo.isEmpty()) {
            return "Artista inválido!";
        } else {
            this.artista = artistaNovo;
            return "Alteração concluida :)";
        }
    }
    
    public String corrigirDuracao(int duracaoNova) {
        if (duracaoNova <= 0) {
            return "Duração inválida!";
        } else {
            this.duracao = duracaoNova;
            return "Alteração concluida :)";
        }
    }
    
}
