
import java.awt.Font;
import java.io.*;
import java.net.*;
import java.util.*;

public class AppChatServidor {

    private static final int PORT = 12345;
    private static ServerSocket serverSocket;
    private static Map<String, PrintWriter> clients = new HashMap<>();

    public static void main(String[] args) throws Exception {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor aguardando conexões...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void broadcastUserList() {
        synchronized (clients) {
            String userList = "USERS " + String.join(",", clients.keySet());
            for (PrintWriter writer : clients.values()) {
                writer.println(userList);
            }
        }
    }

    private static class ClientHandler implements Runnable {

        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String clientName;
        

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                clientName = in.readLine();
                System.out.println("Nome recebido pelo servidor: " + clientName);
                synchronized (clients) {
                    clients.put(clientName, out);
                }
                System.out.println(clientName + " entrou no chat.");
                broadcastUserList();

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("/send")) {
                        String[] parts = message.split(" ", 3);
                        if (parts.length == 3) {
                            sendMessageToClient(parts[1], parts[2]);
                        }
                    } else {
                        System.out.println(clientName + ": " + message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                synchronized (clients) {
                    clients.remove(clientName);
                }
                broadcastUserList();
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendMessageToClient(String target, String message) {
            PrintWriter targetOut = clients.get(target);
            if (targetOut != null) {
                targetOut.println(clientName + " diz: " + message);
            } else {
                out.println("Usuário " + target + " não encontrado.");
            }
        }
    }
}
