package com.mobile.pid.pid.home;

import android.content.Context;
import android.widget.Toast;

import com.mobile.pid.pid.objetos.Turma;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PidSort
{

    /*private Context context;
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
    }*/

    // TODO: Insertion sort
    public static List insertionSort(List lista, Comparator comparator)
    {
        int n = lista.size();
        int i, j;

        for(i = 1; i < n; i++)
        {
            Object tmp = lista.get(i);

            // Se for menor que algum da esquerda
            // Move os outros para a direita até encaixar o objeto tmp no seu lugar ordenado.
            for(j = i; j > 0 && comparator.compare(tmp, lista.get(j - 1)) < 0; j--)
                lista.set(j, lista.get(j - 1));

            lista.set(j, tmp);
        }

        return lista;
    }

    public static List quicksort(List lista, Comparator comparator)
    {
        Collections.sort(lista, comparator);

        return lista;
    }
}
