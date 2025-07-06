package redes;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Servidor {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            
          System.out.print("Digite o endereco do servidor (deixe em branco para 'localhost'): ");
            String address = scanner.nextLine();
            if (address.isEmpty()) address = "localhost";

         System.out.print("Digite a porta do servidor (deixe em branco para '10000'): ");
          String portInput = scanner.nextLine();
            int port = portInput.isEmpty() ? 10000 : Integer.parseInt(portInput);

            ServerSocket serverSocket = new ServerSocket(port, 1, InetAddress.getByName(address));
            System.out.println("Aguardando conexao em " + address + ":" + port);

            Socket clientSocket = serverSocket.accept();
            System.out.println("Conectado ao CLIENTE: " + clientSocket.getInetAddress().getHostAddress());

            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            
          Thread recebendo = new Thread(() -> {
                String msg;
                try {
                   while ((msg = reader.readLine()) != null) {
                       if (msg.equalsIgnoreCase("sair")) {
                            System.out.println("O cliente encerrou a conversa.");
                            System.exit(0);
                        }
                        System.out.println("CLIENTE> " + msg);
                    }
                } catch (IOException e) {
                    System.out.println("Erro ao receber mensagem: " + e.getMessage());
                }
            });

           Thread enviando = new Thread(() -> {
                String msg;
                while (true) {
                    System.out.print("SERVIDOR> ");
                    msg = scanner.nextLine();
                    writer.println(msg);
                    if (msg.equalsIgnoreCase("sair")) {
                        System.out.println("Encerrando a conversa...");
                        System.exit(0);
                    }
                }
            });

            recebendo.start();
            enviando.start();

        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}