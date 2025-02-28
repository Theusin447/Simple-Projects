import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

public class BolaMovimentoSwing extends JFrame {
    private int x = 100, y = 100; // Posição inicial da bolinha
    private final int RAIO = 20;
    private int dx = 2, dy = 2; // Direção da bolinha
    private int contador = 0; // Contador crescente
    private Random random = new Random();
    private JLabel labelContador;
    private JTextField textField;

    public BolaMovimentoSwing() {
        setTitle("Movimentação Aleatória da Bolinha");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Criação do JTextField
        textField = new JTextField();
        add(textField, BorderLayout.NORTH);

        // Criação do JLabel para o contador
        labelContador = new JLabel(String.valueOf(contador), SwingConstants.CENTER);
        labelContador.setFont(new Font("Arial", Font.BOLD, 30));
        labelContador.setForeground(Color.BLUE);
        labelContador.setBounds(150, 150, 100, 50); // Definindo posição no centro da tela

        JPanel painelDesenho = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED);
                g.fillOval(x, y, RAIO * 2, RAIO * 2);
            }
        };

        painelDesenho.setLayout(null); // Usando layout nulo para controlar a posição dos componentes dentro do painel
        painelDesenho.setBounds(0, 0, 400, 400); // Definindo a posição e o tamanho do painel
        painelDesenho.add(labelContador); // Adiciona o contador ao painel de desenho
        add(painelDesenho, BorderLayout.CENTER);

        // Adiciona o listener para capturar o clique do mouse
        painelDesenho.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Define a nova posição da bolinha onde o mouse foi clicado
                x = e.getX() - RAIO; // Centraliza a bolinha no clique
                y = e.getY() - RAIO;
            }
        });

        iniciarMovimentoAleatorio(painelDesenho);
        iniciarContador();
    }

    private void iniciarMovimentoAleatorio(JPanel painel) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                while (true) {
                    // Atualiza a posição da bolinha
                    x += dx;
                    y += dy;

                    // Verifica se a bolinha ultrapassou os limites e ajusta a direção
                    if (x <= 0) {
                        dx = random.nextInt(5) + 1; // Gera uma nova velocidade aleatória
                    } else if (x >= painel.getWidth() - RAIO * 2) {
                        dx = -random.nextInt(5) - 1; // Gera uma nova velocidade aleatória
                    }

                    if (y <= 0) {
                        dy = random.nextInt(5) + 1; // Gera uma nova velocidade aleatória
                    } else if (y >= painel.getHeight() - RAIO * 2) {
                        dy = -random.nextInt(5) - 1; // Gera uma nova velocidade aleatória
                    }

                    painel.repaint();
                    try {
                        Thread.sleep(20); // Pequeno atraso para suavizar o movimento
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        worker.execute();
    }

    private void iniciarContador() {
        // Atualiza o contador a cada 1 segundo sem piscar
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contador++;
                labelContador.setText(String.valueOf(contador)); // Atualiza o JLabel com o contador
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BolaMovimentoSwing frame = new BolaMovimentoSwing();
            frame.setVisible(true);
        });
    }
}
