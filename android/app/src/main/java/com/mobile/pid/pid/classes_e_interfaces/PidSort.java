package com.mobile.pid.pid.classes_e_interfaces;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
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

    private static void quickSortRecursivo(List lista, int inicio, int fim, Comparator comparator)
    {
        if(inicio < fim)
        {
            int pivoIndice = intercalar(lista, inicio, fim, comparator);
            quickSortRecursivo(lista, inicio, pivoIndice - 1, comparator);
            quickSortRecursivo(lista, pivoIndice + 1, fim, comparator);
        }
    }

    // https://www.youtube.com/watch?v=MVZTDkucIro
    private static int intercalar(List lista, int inicio, int fim, Comparator comparator)
    {
        int i = inicio, j = fim;

        while(i < j)
        {
            while(i < fim && comparator.compare(lista.get(i), lista.get(inicio)) <= 0) i++;
            while(j > inicio && comparator.compare(lista.get(j), lista.get(inicio)) >= 0) j--;

            if(i < j)
                trocar(lista, i, j);
        }

        trocar(lista, inicio, j);

        return j;
    }

    /*private static int mediano(List lista, int i, int j, int k, Comparator comparator)
    {
        Object[] array = {lista.get(i), lista.get(j), lista.get(k)};

        selectionSort(lista, comparator);

        if(array[1] == lista)
    }*/

    private static int particionar(List lista, int inicio, int fim, Comparator comparator)
    {
        Object pivo = lista.get(fim);

        int parede = inicio;

        for(int i = inicio; i < fim; i++)
        {
            Object selecionado = lista.get(i);

            if(comparator.compare(selecionado, pivo) <= 0)
            {
                trocar(lista, i, fim);
                parede++;
            }
        }

        lista.set(fim, lista.get(parede));
        lista.set(parede, pivo);

        return parede;
    }

    public static List mergeSort(List l, Comparator comparator)
    {
        Object[] lista = l.toArray();

        Object[] temp = new Object[lista.length];

        mergeSort(lista, temp, 0, lista.length - 1, comparator);

        return Arrays.asList(lista);
    }

    public static void mergeSort(Object[] lista, Object[] temp, int inicio, int fim, Comparator comparator)
    {
        if(inicio < fim)
        {
            int meio = (fim + inicio) / 2;

            mergeSort(lista, temp,         inicio,   meio,      comparator);
            mergeSort(lista, temp, meio + 1,    fim,      comparator);
            merge    (lista, temp,         inicio,   meio, fim, comparator);
        }
    }

    private static void merge(Object[] lista, Object[] temp, int inicio, int meio, int fim, Comparator comparator)
    {
        for(int k = inicio; k <= fim; k++)
            temp[k] = lista[k];

        int i = inicio, j = meio + 1;

        for(int k = inicio; k <= fim; k++)
        {
            if(i > meio)
                lista[k] = temp[j++];
            else if(j > fim)
                lista[k] =  temp[i++];
            else if(comparator.compare(temp[i], temp[j]) < 0)
                lista[k] =  temp[i++];
            else
                lista[k] =  temp[j++];
        }
    }

    /*private static void merge(List lista, Object[] temp, int inicio, int meio, int fim, Comparator comparator)
    {
        int esquerda = inicio;
        int direita  = meio + 1;

        for(int i = inicio; i <= fim; i++)
            temp[i] = lista.get(i);

        while(esquerda <= meio && direita <= fim)
        {
            Object o1 = temp[esquerda];
            Object o2 = temp[direita];

            int lado;

            if(comparator.compare(o1, o2) < 0)
                lado = esquerda++;
            else
                lado = direita++;

            lista.set(inicio++, lado);
        }

        while(esquerda <= meio)
            lista.set(inicio++, temp[esquerda++]);
    }*/

    // TODO: REMOVER, O DE CIMA É MELHOR
    private static void mergeFlavia(List lista, Object[] temp, int inicio, int meio, int fim, Comparator comparator)
    {
        boolean fim1 = false, fim2 = false;
        int i, j, k;

        int tamanho  = fim - inicio + 1;
        int esquerda = inicio;
        int direita  = meio + 1;

        temp = new Object[fim];

        for (i = 0; i < tamanho; i++)
        {
            if (!fim1 && !fim2)
            {
                Object o1 = lista.get(esquerda);
                Object o2 = lista.get(direita);

                if (comparator.compare(o1, o2) < 0)
                    temp[i] =  lista.get(esquerda++);
                else
                    temp[i] = lista.get(direita++);

                if (esquerda > meio) fim1 = true;
                if (direita  > fim)  fim2 = false;
            }
            else
            {
                if (!fim1)
                    temp[i] = lista.get(esquerda++);
                else
                    temp[i] = lista.get(direita++);
            }
        }

        for (j = 0, k = inicio; j < tamanho; j++, k++)
            lista.set(k, temp[j]);
    }

    private static void trocar(List lista, int index1, int index2)
    {
        Object tmp = lista.get(index1);
        lista.set(index1, lista.get(index2));
        lista.set(index2, tmp);
    }
}
