
import javax.swing.*;
import java.awt.*;

public class SwingWorkerExemplo extends JFrame {

    private JProgressBar progressBar;
    private JTextArea textArea;
    private JButton startButton;

    public SwingWorkerExemplo() {
        super("Exemplo completo de uso do SwingWorker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        startButton = new JButton("Iniciar Tarefa");
        startButton.addActionListener(e -> {    //Evento "e" chamando a  sequencia de eventos
            startButton.setEnabled(false);
            startButton.setText("Tarefa em andamento");
            MySwingWorker worker = new MySwingWorker(progressBar, textArea, startButton);
            worker.addPropertyChangeListener(evt -> {
                if ("progress".equals(evt.getPropertyName())) {
                    progressBar.setValue((Integer) evt.getNewValue());
                }
            });
            worker.execute();
        });
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(progressBar, BorderLayout.NORTH);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.SOUTH);
        add(panel);
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SwingWorkerExemplo example = new SwingWorkerExemplo();
                example.setVisible(true);
            }
        });
    }
}
