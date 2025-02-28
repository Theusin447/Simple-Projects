
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import javax.swing.*;

public class LerArquivos extends SwingWorker<Void, String> {

    private final String filePath;
    private final JTextArea textArea;
    private final JTextField texto;
    private final int tempo;
    private final int totalLinhas;
    private final JProgressBar progressBar;
    private int linesRead;
    int progresso;

    public static volatile boolean isStopped = false; //Flag criada para fim das leituras

    public LerArquivos(String filePath, JTextArea textArea, JTextField texto, int tempo, JProgressBar progressBar) {
        this.filePath = filePath;
        this.textArea = textArea;
        this.texto = texto;
        this.tempo = tempo;
        this.totalLinhas = contaTotalLinhas(filePath);
        this.progressBar = progressBar;
    }

    @Override
    protected Void doInBackground() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            linesRead = 0;
            while ((line = reader.readLine()) != null && !isCancelled()) {

                if (isStopped) { //Para o processo das threads
                    break;
                }

                linesRead++;
                publish(line); // Envia a linha lida para o process()
                progresso = (int) ((double) linesRead / totalLinhas * 100);
                setProgress(progresso);
                Thread.sleep(tempo);
            }
        } catch (IOException e) {
            mostrarErro("Erro ao ler o arquivo: " + e.getMessage());
        } catch (InterruptedException e) {
            mostrarErro("Interrompido com sucesso!");
        }
        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        for (String line : chunks) {
            synchronized (textArea) {
                textArea.append(line + "\n");
            }
            texto.setText(line);
            progressBar.setValue(getProgress());
        }
    }

    private int contaTotalLinhas(String filePath) {
        int lineCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while ((reader.readLine()) != null) {
                lineCount++;
            }
        } catch (IOException e) {
            mostrarErro("Erro ao contar as linhas do arquivo: " + e.getMessage());
        }
        return lineCount;
    }

    private void mostrarErro(final String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(textArea.getParent(), msg, "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
