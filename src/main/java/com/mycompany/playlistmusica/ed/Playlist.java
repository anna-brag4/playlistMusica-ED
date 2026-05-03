package com.mycompany.playlistmusica.ed;

public class Playlist {
    
    // vai funcionar como a lista duplamente encadeada
    
    private int tamanho;
    private int duracao; //segundos
    private Musica primeiro;
    private Musica ultimo;

    //getters e setters
    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public Musica getPrimeiro() {
        return primeiro;
    }

    public void setPrimeiro(Musica primeiro) {
        this.primeiro = primeiro;
    }

    public Musica getUltimo() {
        return ultimo;
    }

    public void setUltimo(Musica ultimo) {
        this.ultimo = ultimo;
    }
    
    //Outros métodos
    public Playlist() {
        this.tamanho = 0;
        this.duracao = 0;
        //construtor
    }
    
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
