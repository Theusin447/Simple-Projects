
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteTCP {

    public static void main(String[] args) throws Exception {
        try {
            Socket socket = new Socket("10.74.241.66", 1234);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);   // Conecta ao servidor
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String mensagem = "Hello, servidor TCP! Matheus aqui";
            out.println(mensagem);  // Envia mensagem ao servidor

            String resposta = in.readLine();    //Lê resposta do servidor
            System.out.println("Resposta do servidor: " + resposta);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
