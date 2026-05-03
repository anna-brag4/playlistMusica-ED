package com.mycompany.playlistmusica.ed;

import java.time.LocalDate;

public class Musica {
    
    // vai funcionar como o nó da lista duplamente encadeada
    
    private String titulo;
    private String artista;
    private LocalDate dataInclusao;
    private int duracao;
    private Musica proximo;
    private Musica anterior;

    //getters e setters
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

    public void setProximo(Musica proximo) {
        this.proximo = proximo;
    }

    public Musica getAnterior() {
        return anterior;
    }

    public void setAnterior(Musica anterior) {
        this.anterior = anterior;
    }
    
    //outros métodos
    public Musica(String titulo, String artista, int duracao) {
        this.titulo = titulo;
        this.artista = artista;
        this.duracao = duracao;
        this.dataInclusao = LocalDate.now();
        this.proximo = null;
        this.anterior = null;
    }
    
    public void corrigirTitulo() {
        
    }
    
    public void corrigirArtista() {
        
    }
    
    public void corrigirDuracao() {
        
    }
    
}
