package com.mycompany.playlistmusica.ed;

import java.time.LocalDate;

public class Musica {
    
    // vai funcionar como o nó da lista duplamente encadeada
    
    private String titulo;
    private String artista;
    private LocalDate dataInclusao;
    private int duracao;
    private String caminhoArquivo;
    private Musica proximo;
    private Musica anterior;

    //getters
    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public LocalDate getDataInclusao() {
        return dataInclusao;
    }

    public int getDuracao() {
        return duracao;
    }

    public Musica getProximo() {
        return proximo;
    }
    
    public Musica getAnterior() {
        return anterior;
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    //setters
    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }
    
    public void setProximo(Musica proximo) {
        this.proximo = proximo;
    }

    public void setAnterior(Musica anterior) {
        this.anterior = anterior;
    }
    
    //construtor
    public Musica(String titulo, String artista, int duracao) {
        this.titulo = titulo;
        this.artista = artista;
        this.duracao = duracao;
        this.dataInclusao = LocalDate.now();
        this.proximo = null;
        this.anterior = null;
    }
    
    //métodos de correção
    public void corrigirTitulo() {
        
    }
    
    public void corrigirArtista() {
        
    }
    
    public void corrigirDuracao() {
        
    }
    
}
