package com.mobile.pid.pid.classes_e_interfaces;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.mobile.pid.pid.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class TesteAlgoritmosOrdenacao
{
    private static int[] qtdTurmasTeste = {2, 5, 10, 50, 100, 500, 1000};
    static long tInicial, tFinal;
    static final int QTD_ALGORITMOS = 4;

    public static void testarOrdenacao(Context c)
    {
        Queue<String> resultados = new LinkedList<>();

        for(int i = 0; i < qtdTurmasTeste.length; i++)
        {
            int qtdTurmas = qtdTurmasTeste[i];

            List<Turma> turmasInsert = gerarTurmas(qtdTurmas);
            List<Turma> turmasQuick  = new ArrayList<>(turmasInsert);
            List<Turma> turmasSelect = new ArrayList<>(turmasInsert);
            List<Turma> turmasMerge  = new ArrayList<>(turmasInsert);

            tInicial = System.nanoTime();
            PidSort.insertionSort(turmasInsert, Turma.compararPorNome);
            tFinal   = System.nanoTime();

            adicionarResultado(resultados, "InsertionSort");

            tInicial = System.nanoTime();
            PidSort.selectionSort(turmasSelect, Turma.compararPorNome);
            tFinal   = System.nanoTime();

            adicionarResultado(resultados, "SelectionSort");

            tInicial = System.nanoTime();
            PidSort.quicksort(turmasQuick, Turma.compararPorNome);
            tFinal   = System.nanoTime();

            adicionarResultado(resultados, "QuickSort");

            tInicial = System.nanoTime();
            PidSort.mergeSort(turmasMerge, Turma.compararPorNome);
            tFinal   = System.nanoTime();

            adicionarResultado(resultados, "MergeSort");
        }

        Log.e("Teste", "asdasd");

        apresentarResultados(c, resultados);
    }

    private static void adicionarResultado(Queue resultados, String nomeAlg) {
        resultados.add(nomeAlg + ": " + (tFinal - tInicial) + " ns\n");
    }

    // Método para gerar uma determinada qtd de turmas
    // com nomes aleatórios.
    private static List<Turma> gerarTurmas(int qtd)
    {
        List<Turma> turmas = new ArrayList<>(qtd);

        for(int i = 0; i < qtd; i++)
        {
            Turma t = new Turma();
            t.setNome(gerarStringAleatoria(20));

            turmas.add(t);
        }

        return turmas;
    }

    // https://stackoverflow.com/questions/20536566/creating-a-random-string-with-a-z-and-0-9-in-java
    private static String gerarStringAleatoria(int n)
    {
        String charsPermitidos = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz 0123456789";

        Random random = new Random();
        StringBuilder aleatoria = new StringBuilder();

        for(int i = 0; i < n; i++)
        {
            char aleatorio = charsPermitidos.charAt(random.nextInt(charsPermitidos.length()));
            aleatoria.append(aleatorio);
        }

        return aleatoria.toString();
    }

    private static void apresentarResultados(Context c, Queue resultados)
    {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < qtdTurmasTeste.length; i++)
        {
            sb.append("Resultados para " + qtdTurmasTeste[i] + " turmas:\n");

            for(int j = 0; j < QTD_ALGORITMOS; j++)
                sb.append(resultados.remove());

            sb.append("\n");
        }

        Log.w("Teste da ordenação", sb.toString());

        new AlertDialog.Builder(c)
            .setTitle("Resultados")
            .setMessage(sb.toString())
            .setPositiveButton(R.string.Ok, null)
            .create()
            .show();
    }
}
