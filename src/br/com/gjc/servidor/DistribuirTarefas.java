package br.com.gjc.servidor;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DistribuirTarefas implements Runnable {

    private final ExecutorService threadPool;
    private final BlockingQueue<String> filaComandos;
    private Socket socket;
    private ServidorTarefas servidor;

    public DistribuirTarefas(ExecutorService threadPool, BlockingQueue<String> filaComandos, Socket socket, ServidorTarefas servidor) {
        this.threadPool = threadPool;
        this.filaComandos = filaComandos;
        this.socket = socket;
        this.servidor = servidor;
    }

    @Override
    public void run() {
        try {
            System.out.println("Distribuindo tarefas para " + socket);
            Scanner scan = new Scanner(socket.getInputStream());
            PrintStream saidaCliente = new PrintStream(socket.getOutputStream());

            while (scan.hasNextLine()) {
                String comando = scan.nextLine();
                System.out.println("Comando recebido " + comando);

                switch (comando) {
                    case "c1":
                        saidaCliente.println("Confirmação do comando c1");
                        ComandoC1 c1 = new ComandoC1(saidaCliente);
                        this.threadPool.execute(c1);
                        break;
                    case "c2":
                        saidaCliente.println("Confirmação do comando c2");
                        ComandoC2ChamaWS c2WS = new ComandoC2ChamaWS(saidaCliente);
                        ComandoC2AcessaBanco c2Banco = new ComandoC2AcessaBanco(saidaCliente);
                        Future<String> futureWS = this.threadPool.submit(c2WS);
                        Future<String> futureBanco = this.threadPool.submit(c2Banco);

                        this.threadPool.submit(new JuntaResultadosFutureWSFutureBanco(futureWS, futureBanco, saidaCliente));

                        String resultadoWS = futureWS.get();
                        break;
                    case "c3":
                        this.filaComandos.put(comando); //bloqueia
                        saidaCliente.println("Comando c3 adicionado na fila");
                        break;
                    case "fim":
                        saidaCliente.println("Desligando servidor");
                        servidor.parar();
                        return;
                    default:
                        saidaCliente.println("Comando não encontrado ");
                }
               // System.out.println(comando);
            }
            saidaCliente.close();
            scan.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
