package br.com.gjc.servidor;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class DistribuirTarefas implements Runnable {

    private Socket socket;

    public DistribuirTarefas(Socket socket) {
        this.socket = socket;
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
                        break;
                    case "c2":
                        saidaCliente.println("Confirmação do comando c2");
                        break;
                    default:
                        saidaCliente.println("Comando não encontrado ");
                }
                System.out.println(comando);
            }
            saidaCliente.close();
            scan.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
