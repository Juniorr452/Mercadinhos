package com.mobile.pid.pid.home.turmas;

/**
 * Created by junio on 07/03/2018.
 */

// Informação do usuário (Nome e Foto) nesse tipo: https://qph.fs.quoracdn.net/main-qimg-c4c82ebf15b46df728e39f2a0149b9ad
public class InfoUsuario {
    private String nome;
    private String fotoUrl;

    public InfoUsuario() {}

    public InfoUsuario(String nome, String fotoUrl)
    {
        this.nome = nome;
        this.fotoUrl = fotoUrl;
    }

    public String getNome() {
        return nome;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }
}
