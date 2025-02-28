import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class MySwingWorker extends SwingWorker<String, Integer> {
    private final JProgressBar progressBar;
    private final JTextArea textArea;
    private final JButton button;

    public MySwingWorker(JProgressBar progressBar, JTextArea textArea, JButton button){
        this.progressBar = progressBar;
        this.textArea = textArea;
        this.button = button;
    }

    @Override
    protected String doInBackground() throws Exception{
        //Simula uma tarefa de longa duração
        for(int i=0; i<=100; i++){
            Thread.sleep(100);  //Dorme por 100 ms
            publish(i); //Publica o processo
            setProgress(i); //Atualiza a barra de processo
        }
        return "Tarefa concluida com sucesso";
    }

    @Override
    protected void process(List<Integer> chunks){
        //Atualiza a interface gráfica
        for(int chunk : chunks){
            progressBar.setValue(chunk);
            textArea.append("Progresso: "+ chunk +"%\n");
        }
    }

    @Override
    protected void done(){
        try {
            String result = get();
            textArea.append(result+"\n");
            button.setEnabled(true);
            button.setText("Iniciar Tarefa");
        } catch (InterruptedException | ExecutionException e) {
            textArea.append("Erro: "+e.getMessage()+"\n");
        }
    }
}
