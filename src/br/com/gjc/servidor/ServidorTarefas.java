package br.com.gjc.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServidorTarefas {

    private AtomicBoolean estaRodando;
    private ServerSocket servidor;
    private ExecutorService threadPool;

    public static void main(String[] args) throws Exception {
        ServidorTarefas servidor = new ServidorTarefas();
        servidor.rodar();

    }

    public ServidorTarefas() throws IOException {
        System.out.println("<--- Iniciando servidor --->");
        this.servidor = new ServerSocket(4343);
        ExecutorService threadPool = Executors.newCachedThreadPool();
        this.estaRodando = new AtomicBoolean(true);
    }

    private void rodar() throws IOException {

        while (this.estaRodando.get()) {
            try {
                Socket socket = servidor.accept();
                System.out.println("Aceitando novo cliente na porta: " + socket.getPort());

                DistribuirTarefas distribuirTarefas = new DistribuirTarefas(socket, this);
                this.threadPool.execute(distribuirTarefas);
            } catch (IOException e) {
                System.out.println("SocketException, está rodando? " + this.estaRodando);
            }
        }
    }

    public void parar() throws IOException {
        this.estaRodando.set(false);
        this.threadPool.shutdown();
        this.servidor.close();
    }
}