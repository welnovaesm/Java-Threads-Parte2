package br.com.gjc.cliente;

import java.net.Socket;

public class ClienteTarefas {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 4343);

         System.out.println("Conexao estabelecida");

         socket.close();
    }
}
