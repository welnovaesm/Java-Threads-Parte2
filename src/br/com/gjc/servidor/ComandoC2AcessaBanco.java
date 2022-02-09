package br.com.gjc.servidor;

import java.io.PrintStream;
import java.util.Random;
import java.util.concurrent.Callable;

public class ComandoC2AcessaBanco implements Callable<String> {


    private PrintStream saida;

    public ComandoC2AcessaBanco(PrintStream saida) {
        this.saida = saida;
    }

    @Override
    public String call() throws Exception {
        System.out.println("Servidor recebeu comando c2 - banco");
        saida.print("processando comando c2 - banco");

        Thread.sleep(25000);
        int numero = new Random().nextInt(100) + 1;
        System.out.println("Servidor finalizou comando c2 - banco");

        return Integer.toString(numero);
    }
}
