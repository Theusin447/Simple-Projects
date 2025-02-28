
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClienteUDP {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite a mensagem:");
        String mensagem;
        mensagem = scanner.next();

        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress serverAdress = InetAddress.getByName("10.74.241.66"); //Endereço do server
            byte[] sendData = mensagem.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAdress, 9876);
            socket.send(sendPacket); //Envia mensagem para o servidor

            //Recebe a resposta
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String resposta = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Resposta do servidor: " + resposta);
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
