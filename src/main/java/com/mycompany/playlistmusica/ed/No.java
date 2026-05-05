package com.mycompany.playlistmusica.ed;

import java.time.LocalDate;

public class No {

    // Dados da música
    public String titulo;
    public String artista;
    public String caminhoOuUrl;
    public LocalDate dataInclusao;
    public int duracao; // em segundos

    // Ponteiros da lista duplamente encadeada
    public No proximo;
    public No anterior;

    // Construtor
  
    public No(String titulo, String artista, String caminhoOuUrl) {
        this.titulo       = titulo;
        this.artista      = artista;
        this.caminhoOuUrl = caminhoOuUrl;
        this.dataInclusao = LocalDate.now();
        this.duracao      = 0;
        this.proximo      = null;
        this.anterior     = null;
    }
    
    // Construtor 2
  
    public No(String titulo, String artista, String caminhoOuUrl, int duracao) {
        this(titulo, artista, caminhoOuUrl);
        this.duracao = duracao;
    }

    // Getters
    public String getTitulo()        { return titulo; }
    public String getArtista()       { return artista; }
    public String getCaminhoOuUrl()  { return caminhoOuUrl; }
    public LocalDate getDataInclusao() { return dataInclusao; }
    public int getDuracao()          { return duracao; }

    // Setters (para correção dos dados)
    public void setTitulo(String titulo) 
  { 
    this.titulo       = titulo; 
  }
    public void setArtista(String artista)           
  { 
    this.artista      = artista;
  }
    public void setCaminhoOuUrl(String caminhoOuUrl) 
  { 
    this.caminhoOuUrl = caminhoOuUrl; 
  }
    public void setDuracao(int duracao)              
  { 
    this.duracao      = duracao;
  }

    @Override
    public String toString() 
  {
        return String.format("'%s' - %s [%s] (%s)",
                titulo, artista, formatarDuracao(), dataInclusao);
    }

    private String formatarDuracao() {
        int min = duracao / 60;
        int seg = duracao % 60;
        return String.format("%d:%02d", min, seg);
    }
    
    //métodos de correção
    public void corrigirTitulo() {
        
    }
    
    public void corrigirArtista() {
        
    }
    
    public void corrigirDuracao() {
        
    }
    
}
