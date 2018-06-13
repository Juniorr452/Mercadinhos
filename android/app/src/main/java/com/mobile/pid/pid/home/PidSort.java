package com.mobile.pid.pid.home;

import android.content.Context;
import android.widget.Toast;

import com.mobile.pid.pid.objetos.Turma;

import java.util.List;

public class PidSort {

    private Context context;
    private List<Turma> t;

    public PidSort (Context context, List<Turma> t) {
        this.context = context;
        this.t = t;
    }

    //TODO IMPLEMENTAR ORDENACAO POR DIA
    public List<Turma> ordenarTurmaDia() {
        return t;
    }

    //TODO IMPLEMENTAR ORDENACAO POR ALFABETO
    public List<Turma> ordenarTurmaAlfabeto() {
        return t;
    }
}
