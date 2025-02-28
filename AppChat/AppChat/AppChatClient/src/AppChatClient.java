
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class AppChatClient extends JFrame {

    private JTextArea taChat;
    private JList<String> userList; // Seleção de usuários.
    private DefaultListModel<String> listModel; // Atualiza a lista.
    private JTextField tfMessage;
    private JTextField tfRecipient;
    private JButton btnSend;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String serverAddress = "192.168.1.15";
    private int port = 12345;

    public AppChatClient() {
        setTitle("Chat Client");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Font Arial = new Font("Arial", Font.BOLD, 18);

        taChat = new JTextArea();
        taChat.setEditable(false);
        taChat = new JTextArea();
        taChat.setEditable(false);
        taChat.setLineWrap(true);   // Quebra de linha automática ativada
        taChat.setWrapStyleWord(true);  // Respeita as palavras
        add(new JScrollPane(taChat), BorderLayout.CENTER);
        taChat.setBackground(Color.LIGHT_GRAY);
        taChat.setForeground(Color.BLACK);

        listModel = new DefaultListModel<>();
        userList = new JList<>(listModel); // Usuários online.
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setBackground(Color.DARK_GRAY);
        userList.setForeground(Color.WHITE);
        userList.addMouseListener(new MouseAdapter() { // Clique no nome para enviar mensagem
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedUser = userList.getSelectedValue();
                if (selectedUser != null) {
                    tfRecipient.setText(selectedUser);
                }
            }
        });

        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(120, 0));
        add(userScrollPane, BorderLayout.EAST);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel recipientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        recipientPanel.setFont(Arial);
        recipientPanel.add(new JLabel("Destinatário:"));
        tfRecipient = new JTextField(15);
        recipientPanel.add(tfRecipient);
        panel.add(recipientPanel, BorderLayout.NORTH);
        tfRecipient.setBackground(Color.WHITE);

        JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        messagePanel.setFont(Arial);
        messagePanel.add(new JLabel("Mensagem:"));
        tfMessage = new JTextField(20);
        messagePanel.add(tfMessage);
        panel.add(messagePanel, BorderLayout.CENTER);
        tfMessage.setBackground(Color.WHITE);

        btnSend = new JButton("Enviar");
        btnSend.addActionListener(e -> sendMessage());
        panel.add(btnSend, BorderLayout.EAST);

        add(panel, BorderLayout.SOUTH);

        connectToServer();
        new Thread(new MessageReceiver()).start();
    }

    private void connectToServer() {
        try {
            String iphost = JOptionPane.showInputDialog("Digite o ip do servidor:");
            serverAddress = iphost;
            socket = new Socket(serverAddress, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String name = JOptionPane.showInputDialog("Digite seu nome:");
            out.println(name);
            setTitle("Chat Client - " + name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String recipient = tfRecipient.getText();
        String message = tfMessage.getText();

        if (!message.isEmpty() && !recipient.isEmpty()) {
            out.println("/send " + recipient + " " + message);
            taChat.append("Você (para " + recipient + "): " + message + "\n");
            tfMessage.setText("");
        }
    }

    private class MessageReceiver implements Runnable {
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("USERS ")) {
                        String[] users = message.substring(6).split(",");
                        SwingUtilities.invokeLater(() -> {
                            listModel.clear();
                            for (String user : users) {
                                listModel.addElement(user.trim());
                            }
                        });
                    } else {
                        taChat.append(message + "\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppChatClient().setVisible(true));
    }
}