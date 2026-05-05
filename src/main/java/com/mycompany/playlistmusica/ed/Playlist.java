package com.mycompany.playlistmusica.ed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Lista duplamente encadeada de músicas.
 * Cada nó  representa uma música na playlist.
 */

public class Playlist 
{

    public No cabeca;
    public No cauda;
    public int tamanho;      
    private String nome;
    private int duracaoTotal; // duração acumulada em segundos

    // Construtor
    public Playlist() 
    {
        this.cabeca       = null;
        this.cauda        = null;
        this.tamanho      = 0;
        this.duracaoTotal = 0;
        this.nome         = "Minha Playlist";
    }

    public Playlist(String nome) 
    {
        this();
        this.nome = nome;
    }

    // Getters
    public int getTamanho()      { return tamanho; }
    public int getDuracaoTotal() { return duracaoTotal; }
    public String getNome()      { return nome; }
    public No getPrimeiro()      { return cabeca; }
    public No getUltimo()        { return cauda; }

    //Adiciona uma música
    
    public No adicionar(String titulo, String artista, String caminhoOuUrl) {
        No novo = new No(titulo, artista, caminhoOuUrl);
        if (cauda == null) {
            cabeca = cauda = novo;
        } else {
            novo.anterior = cauda;
            cauda.proximo = novo;
            cauda         = novo;
        }
        tamanho++;
        return novo;
    }

    //Adiciona uma música ao final com duração informada
    public No adicionar(String titulo, String artista, String caminhoOuUrl, int duracao) 
    {
        No novo = adicionar(titulo, artista, caminhoOuUrl);
        novo.setDuracao(duracao);
        duracaoTotal += duracao;
        return novo;
    }

  
    // Remove um nó da lista. 
    public void remover(No no) {
        if (no == null) return;

        if (no.anterior != null) no.anterior.proximo = no.proximo;
        else                     cabeca               = no.proximo;

        if (no.proximo != null) no.proximo.anterior = no.anterior;
        else                    cauda                = no.anterior;

        duracaoTotal -= no.getDuracao();
        no.anterior   = null;
        no.proximo    = null;
        tamanho--;
    }


    
    public void embaralharPlaylist() {
        
    }
    
    public void ordenarTitulo(){
        
    }
    
    public void ordenarArtista() {
        
    }
    
    public void formatarDuracao(){
        
    }
    
}
