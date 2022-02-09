package br.com.gjc.cliente;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteTarefas {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 4343);

        System.out.println("Conexao estabelecida");

        Thread threadEnviaComando = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PrintStream saida = new PrintStream(socket.getOutputStream());
                    Scanner scan = new Scanner(System.in);
                    while (scan.hasNextLine()) {
                        String linha = scan.nextLine();

                        if (linha.trim().equals("")) {
                            break;
                        }
                        saida.println(linha);
                    }
                    saida.close();
                    scan.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread threadRecebeResposta = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Recebendo dados do servidor");
                    Scanner respostaServidor = new Scanner(socket.getInputStream());
                    while (respostaServidor.hasNextLine()) {
                        String linha = respostaServidor.nextLine();
                        System.out.println(linha);
                    }
                    respostaServidor.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        threadRecebeResposta.start();
        threadEnviaComando.start();

        threadEnviaComando.join();

        System.out.println("Fechando socket do cliente");
        socket.close();
    }
}
