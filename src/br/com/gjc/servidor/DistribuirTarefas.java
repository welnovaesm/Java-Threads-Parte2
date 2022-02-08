package br.com.gjc.servidor;

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

            while(scan.hasNextLine()){
                String comando = scan.nextLine();
                System.out.println(comando);
            }

            scan.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
