package com.mycompany.playlistmusica.ed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/*
 * Lista duplamente encadeada de músicas.
 * Cada nó  representa uma música na playlist.
 */
public class Playlist {
  
    private No cabeca; //primeiro nó
    private No cauda;  //ultimo nó
    private int tamanho;      
    private String nome;
    private int duracaoTotal; // duração acumulada em segundos

    // Construtor
    public Playlist() {
        this.cabeca = null;
        this.cauda = null;
        this.tamanho = 0;
        this.duracaoTotal = 0;
        this.nome = "Minha Playlist";
    }

    // Getters
    public No getCabeca() {
        return cabeca;
    }

    public No getCauda() {
        return cauda;
    }

    public int getTamanho() {
        return tamanho;
    }

    public String getNome() {
        return nome;
    }

    public int getDuracaoTotal() {
        return duracaoTotal;
    }

    //Adiciona uma música
    public No adicionar(String titulo, String artista, String caminhoOuUrl, int duracao) {
        No novo = new No(titulo, artista, caminhoOuUrl, duracao);
        if (this.cauda == null) {
            this.cabeca = this.cauda = novo;
        } else {
            novo.setAnterior(cauda);
            this.cauda.setProximo(novo);
            this.cauda = novo;
        }
        duracaoTotal += duracao;
        this.tamanho++;
        return novo;
    }

    /* Sem necessidade, se vai informar a duração toral todas as músicas precisam ter uma duração
    //Adiciona uma música ao final com duração informada
    public No adicionar(String titulo, String artista, String caminhoOuUrl, int duracao) 
    {
        No novo = adicionar(titulo, artista, caminhoOuUrl);
        novo.setDuracao(duracao);
        duracaoTotal += duracao;
        return novo;
    }
    */
  
    // Remove um nó da lista. 
    // Mudança de No no -> No atual
    
    public No buscarTitulo(String tituloMusica) {
        No atual = this.cabeca;
        while(atual != null) {
            if(atual.getTitulo().equalsIgnoreCase(tituloMusica)) {
                return atual;
            } else {
                atual = atual.getProximo();
            }
        }
        return null;
    }
    
    public String remover(No atual) {
        if (atual == null) {
            return "Música não encontrada, remoção incompleta :(";
        } else {
            String nomeMusica = atual.getTitulo();
            this.duracaoTotal -= atual.getDuracao();
            
            if (atual.getAnterior() != null) {
                atual.getAnterior().setProximo(atual.getProximo());
            } else {
                this.cabeca = atual.getProximo();
            }

            if (atual.getProximo() != null) {
                atual.getProximo().setAnterior(atual.getAnterior());
            } else {
                this.cauda = atual.getAnterior();
            }
            
            atual.setAnterior(null);
            atual.setProximo(null);
            tamanho--;
            
            return String.format("Música %s removida com sucesso :)", nomeMusica);
            
        }
    }
    
    //basta utilizar remover(buscarTitulo("Exemplo")
    
    public void embaralharPlaylist() {
        
    }
    
    public void ordenarTitulo(){
        
    }
    
    public void ordenarArtista() {
        
    }
    
    public String formatarDuracao(No musica){
        int totalSegundos = musica.getDuracao();
        int segundos = totalSegundos % 60;
        int minutos = (totalSegundos % 3600) / 60;
        int horas = totalSegundos / 3600;
        
        if (horas == 0 && minutos == 0) {
            return String.format("%dseg", segundos);
        } else if (horas == 0) {
            return String.format("%dmin %dseg", minutos, segundos);
        } else {
            return String.format("%dh %dmin %dseg", horas, minutos, segundos);
        }
        
    }

    public String corrigirNomePlaylist(String nomeNovo) {
        if (nomeNovo == null || nomeNovo.isEmpty()) {
            return "Nome inválido!";
        } else {
            this.nome = nomeNovo;
            return "Alteração concluida :)";
        }
    }

}
   