package com.mobile.pid.pid.home;

import android.content.Context;
import android.widget.Toast;

import com.mobile.pid.pid.objetos.Turma;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PidSort
{
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

    public static List selectionSort(List lista, Comparator comparator)
    {
        int n = lista.size();
        int i, j;

        for(i = 0; i < n - 1; i++)
        {
            int menorIndex = i;

            for(j = i + 1; j < n; j++)
            {
                Object objAtual = lista.get(j);
                Object objMenor = lista.get(i);

                if (comparator.compare(objAtual, objMenor) < 0)
                    menorIndex = j;
            }

            trocar(lista, i, menorIndex);
        }

        return lista;
    }

    public static void quicksort(List lista, Comparator comparator)
    {
        quickSortRecursivo(lista, 0, lista.size() - 1, comparator);
    }

    private static void quickSortRecursivo(List lista, int de, int para, Comparator comparator)
    {
        if(de < para)
        {
            int pivoIndice = particionar(lista, de, para, comparator);
            quickSortRecursivo(lista, de, pivoIndice - 1, comparator);
            quickSortRecursivo(lista, pivoIndice + 1, para, comparator);
        }
    }

    private static int particionar(List lista, int de, int para, Comparator comparator)
    {
        Object pivo = lista.get(para);

        int parede = de;

        for(int i = de; i < para; i++)
        {
            Object selecionado = lista.get(i);

            if(comparator.compare(selecionado, pivo) <= 0)
            {
                trocar(lista, i, para);
                parede++;
            }
        }

        lista.set(para, lista.get(parede));
        lista.set(parede, pivo);

        return parede;
    }

    private static void trocar(List lista, int index1, int index2)
    {
        Object tmp = lista.get(index1);
        lista.set(index1, lista.get(index2));
        lista.set(index2, tmp);
    }
}
