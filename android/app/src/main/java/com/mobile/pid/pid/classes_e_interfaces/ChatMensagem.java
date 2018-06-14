package com.mobile.pid.pid.classes_e_interfaces;

public class ChatMensagem
{
    // ID do usuário que enviou.
    private String  uid;
    private String  fotoUrl;
    private String  mensagem;
    private boolean professor;

    public ChatMensagem() { }

    public ChatMensagem(String uid, String fotoUrl, String mensagem, boolean professor) {
        this.uid = uid;
        this.fotoUrl = fotoUrl;
        this.mensagem = mensagem;
        this.professor = professor;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoPerfil) {
        this.fotoUrl = fotoPerfil;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public boolean isProfessor() {
        return professor;
    }

    public void setProfessor(boolean professor) {
        this.professor = professor;
    }
}
