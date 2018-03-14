package com.mobile.pid.pid.home.turmas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by junio on 07/03/2018.
 */

// TODO: Completar classe Turma
public class Turma
{
    private String capaUrl;
    private String nome;
    private String pin;

    private Map<String, Integer> diasDaSemana;

    private List<InfoUsuario> professores;
    private List<InfoUsuario> alunos;

    public Turma() {}

    public Turma(String nome, String pin, String capaUrl, String imagemProf, String nomeProf, Map<String, Integer> diasDaSemana)
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

    public List<InfoUsuario> getProfessores() {
        return professores;
    }

    public List<InfoUsuario> getAlunos() {
        return this.alunos;
    }

    public Map<String, Integer> getDiasDaSemana() {
        return this.diasDaSemana;
    }
}