
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServidorUDP {

    public static void main(String[] args) throws Exception {
        try {
            DatagramSocket socket = new DatagramSocket(9876); //Porta do server
            byte[] receiveData = new byte[1024];

            System.out.println("Servidor UDP esperando por dados...");

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket); //Recebe o pacote

                String mensagem = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Mensagem recebida: " + mensagem);

                //Resposta para o cliente
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                String resposta = "Mensagem recebida!";
                DatagramPacket sendPacket = new DatagramPacket(resposta.getBytes(), resposta.length(), clientAddress, clientPort);
                socket.send(sendPacket); //Envia
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
