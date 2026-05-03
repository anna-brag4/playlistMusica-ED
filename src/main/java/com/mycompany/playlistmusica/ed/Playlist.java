package com.mycompany.playlistmusica.ed;

public class Playlist {
    
    // vai funcionar como a lista duplamente encadeada
    
    private int tamanho;
    private int duracao; //segundos
    private Musica primeiro;
    private Musica ultimo;

    //getters
    public int getTamanho() {
        return tamanho;
    }

    public int getDuracao() {
        return duracao;
    }

    public Musica getPrimeiro() {
        return primeiro;
    }

    public Musica getUltimo() {
        return ultimo;
    }
    
    //construtor
    public Playlist() {
        this.tamanho = 0;
        this.duracao = 0;
        this.primeiro = null;
        this.ultimo = null;
    }
    
    //métodos para edição da playlist
    public void adicionarMusica() {
        
    }
    
    public void removerMusica() {
        
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
