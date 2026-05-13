package com.mycompany.playlistmusica.ed;

import java.util.ArrayList;
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
    
    // Tocar próxima/anterior

    public No proximaMusica(No atual) {
        No proximo = atual.getProximo();
        if (proximo != null) {
            atual= atual.getProximo(); // mover o ponteiro 
        } else if (proximo == null){
            atual = this.cabeca; // chegou na última, volta para a primeira   
        } else {
            return null; // não ter nenhuma música
        }
        return atual;    // vai retornar o nó atual
    }

    public No musicaAnterior(No atual) {
        No anterior = atual.getAnterior();
        if (anterior!= null) {
            atual= atual.getAnterior(); //mover o ponteiro, se tem anterior, volta
        } else if (anterior == null){
            atual = this.cabeca; //está na primeira, volta ao inicio da primeira
        } else {
            return null; // não ter nenhuma música
        }
        return atual;    // vai retornar o nó atual
    }
          
    //basta utilizar remover(buscarTitulo("Exemplo")
    public No[] criarLista(){
        No criarLista = this.cabeca;
        No[] lista = new No[this.tamanho];
        for(int i=0; i < tamanho; i++) {
            lista[i] = criarLista;
            criarLista = criarLista.getProximo();
        }
        return lista;
    }
    
    public void reconstruirPlaylist(No[] lista){
        for(int i=0; i < tamanho; i++) {
            if(i==0) {
                this.cabeca = lista[i];
                this.cauda = lista[i]; 
            } else {
                No atual = lista[i];
                atual.setAnterior(this.cauda);
                this.cauda.setProximo(atual);
                this.cauda = atual;
            }
        } 
        this.cauda.setProximo(null);
        this.cabeca.setAnterior(null);
    }
    
    public void embaralharPlaylist() {
        //criação lista de nós
        No[] lista = criarLista();
        
        //nós em posições aleatórias
        Random aleatorio = new Random();
        for(int i = this.tamanho-1; i>0; i--) {
            int numAleatorio = aleatorio.nextInt(i+1);
            // lista[i] e lista[numAleatorio] trocam de lugar;
            No salvo = lista[i];
            lista[i] = lista[numAleatorio];
            lista[numAleatorio] = salvo;
        }
        
        //nós com ponteiros para o próximo e anterior
        reconstruirPlaylist(lista);
    }

    public void ordenarTitulo(){

        if (cabeca == null || cabeca.getProximo() == null) {
            return;
        }

        ArrayList<No> musicas = new ArrayList<>();

        No atual = cabeca;

        while (atual != null) {
            musicas.add(atual);
            atual = atual.getProximo();
        }

        musicas.sort((a, b) ->
            a.getTitulo().compareToIgnoreCase(b.getTitulo())
        );  

        reconstruirPlaylist(musicas);
    }

    public void ordenarArtista() {

        if (cabeca == null || cabeca.getProximo() == null) {
            return;
        }

        ArrayList<No> musicas = new ArrayList<>();

        No atual = cabeca;

        while (atual != null) {
            musicas.add(atual);
            atual = atual.getProximo();
        }

        musicas.sort((a, b) ->
            a.getArtista().compareToIgnoreCase(b.getArtista())
        );

        reconstruirPlaylist(musicas);
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
   