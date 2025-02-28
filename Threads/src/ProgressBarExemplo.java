
import javax.swing.SwingUtilities;

class ProgressBarExemplo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MinhaProgressBar example = new MinhaProgressBar();
                example.setVisible(true);
                example.iniciarTarefa();
            }
        });
    }
}
