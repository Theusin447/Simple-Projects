import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class BolaMovimentoSwing extends JFrame {
    private int x = 100, y = 100; // Posição inicial da bolinha
    private final int RAIO = 20;
    private int dx = 2, dy = 2; // Direção da bolinha no movimento aleatório
    private int velocidade = 5; // Velocidade da bolinha no movimento manual
    private int contador = 0; // Contador crescente
    private boolean movimentoAleatorio = true; // Indica se o movimento é aleatório ou manual
    private Random random = new Random();
    private JLabel labelContador;
    private JTextField textField;

    public BolaMovimentoSwing() {
        setTitle("Movimentação da Bolinha");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Criação do JTextField
        textField = new JTextField();
        add(textField, BorderLayout.NORTH);

        // Adiciona evento para perder foco ao pressionar ESC
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    textField.transferFocus(); // Sai do JTextField
                    requestFocus(); // Retorna o foco para a JFrame
                }
            }
        });

        // Criação do JLabel para o contador
        labelContador = new JLabel(String.valueOf(contador), SwingConstants.CENTER);
        labelContador.setFont(new Font("Arial", Font.BOLD, 30));
        labelContador.setForeground(Color.BLUE);
        labelContador.setBounds(150, 150, 100, 50); // Posição no centro da tela

        JPanel painelDesenho = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED);
                g.fillOval(x, y, RAIO * 2, RAIO * 2);
            }
        };

        painelDesenho.setLayout(null);
        painelDesenho.setBounds(0, 0, 400, 400);
        painelDesenho.add(labelContador);
        add(painelDesenho, BorderLayout.CENTER);

        // Adiciona listener para capturar o clique do mouse e voltar o foco para a tela
        painelDesenho.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX() - RAIO; // Centraliza a bolinha no clique
                y = e.getY() - RAIO;
                requestFocus(); // Volta o foco para a JFrame
            }
        });

        // Adiciona evento de teclado para mudar o modo e mover manualmente
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    movimentoAleatorio = !movimentoAleatorio; // Alterna o modo
                } else if (!movimentoAleatorio) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            y = Math.max(y - velocidade, 0);
                            break;
                        case KeyEvent.VK_DOWN:
                            y = Math.min(y + velocidade, painelDesenho.getHeight() - RAIO * 2);
                            break;
                        case KeyEvent.VK_LEFT:
                            x = Math.max(x - velocidade, 0);
                            break;
                        case KeyEvent.VK_RIGHT:
                            x = Math.min(x + velocidade, painelDesenho.getWidth() - RAIO * 2);
                            break;
                    }
                }
                painelDesenho.repaint();
            }
        });

        setFocusable(true);
        requestFocusInWindow();

        iniciarMovimentoAleatorio(painelDesenho);
        iniciarContador();
    }

    private void iniciarMovimentoAleatorio(JPanel painel) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                while (true) {
                    if (movimentoAleatorio) {
                        x += dx;
                        y += dy;

                        if (x <= 0 || x >= painel.getWidth() - RAIO * 2) {
                            dx = -dx;
                        }
                        if (y <= 0 || y >= painel.getHeight() - RAIO * 2) {
                            dy = -dy;
                        }
                    }

                    painel.repaint();
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        worker.execute();
    }

    private void iniciarContador() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contador++;
                labelContador.setText(String.valueOf(contador));
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
