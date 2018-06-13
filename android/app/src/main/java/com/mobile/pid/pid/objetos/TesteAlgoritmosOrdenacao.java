package com.mobile.pid.pid.objetos;

import android.app.AlertDialog;
import android.content.Context;

import com.mobile.pid.pid.home.PidSort;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedTransferQueue;

public class TesteAlgoritmosOrdenacao
{
    private static int[] qtdTurmasTeste = {1, 5, 10, 100, 1000, 10000, 100000};

    public static void testarInsertQuick(Context c)
    {
        Queue<String> resultados = new LinkedList<>();

        for(int i = 0; i < qtdTurmasTeste.length; i++)
        {
            int qtdTurmas = qtdTurmasTeste[i];

            List<Turma> turmasInsert = gerarTurmas(qtdTurmas);
            List<Turma> turmasQuick  = new ArrayList<>(turmasInsert);

            long tempoInsertInicial = System.nanoTime();
            PidSort.insertionSort(turmasInsert, Turma.compararPorNome);
            long tempoInsertFinal   = System.nanoTime();

            resultados.add("Insert Sort: " + (tempoInsertFinal - tempoInsertInicial) + " ns\n");

            long tempoQuickInicial = System.nanoTime();
            PidSort.quicksort(turmasQuick, Turma.compararPorNome);
            long tempoQuickFinal   = System.nanoTime();

            resultados.add("Quick Sort: " + (tempoQuickFinal - tempoQuickFinal) + " ns\n");
        }

        apresentarResultados(c, resultados);
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
            sb.append("Resultados para " + qtdTurmasTeste[i] + "turmas:\n");

            sb.append(resultados.remove());
            sb.append(resultados.remove());
            sb.append("\n");
        }

        new AlertDialog.Builder(c)
            .setTitle("Resultados")
            .setMessage(sb.toString())
            .show();
    }
}
