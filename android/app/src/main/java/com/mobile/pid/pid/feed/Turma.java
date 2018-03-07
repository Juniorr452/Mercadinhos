package com.mobile.pid.pid.feed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junio on 07/03/2018.
 */

// TODO: Completar classe Turma
public class Turma
{
    private String capaUrl;
    private String nome;
    private String pin;

    private List<Integer> diasDaSemana;

    private List<InfoUsuario> professores;
    private List<InfoUsuario> alunos;

    public Turma() {}

    public Turma(String nome, String pin, String capaUrl, String imagemProf, String nomeProf, List<Integer> diasDaSemana)
    {
        this.capaUrl = capaUrl;
        this.nome = nome;
        this.pin  = pin;
        this.diasDaSemana = diasDaSemana;

        this.professores = new ArrayList<>();
        professores.add(new InfoUsuario(nomeProf, imagemProf));
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPin(){
        return this.pin;
    }

    public void setPin(String pin){
        this.pin = pin;
    }

    public String getCapaUrl() {
        return this.capaUrl;
    }

    public InfoUsuario getProfessores()
    {
        return new InfoUsuario("nomee", "foto u r eleee");
    }

    /*public List<InfoUsuario> getAlunos() {
        return this.alunos;
    }

    public List<Integer> getDiasDaSemana() {
        return this.diasDaSemana;
    }*/

    // Informação do usuário (Nome e Foto) nesse tipo: https://qph.fs.quoracdn.net/main-qimg-c4c82ebf15b46df728e39f2a0149b9ad
    class InfoUsuario
    {
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
}
