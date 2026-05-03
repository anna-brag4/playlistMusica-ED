package com.mycompany.playlistmusica.ed;

import java.time.LocalDate;

public class Musica {
    private String titulo;
    private String artista;
    private LocalDate dataInclusao;
    private int duracao;
    private Musica proximo;
    private Musica anterior;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public LocalDate getDataInclusao() {
        return dataInclusao;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
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
    
    
    
}
