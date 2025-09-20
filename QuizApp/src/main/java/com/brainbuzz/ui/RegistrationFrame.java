package main.java.com.brainbuzz.ui;

/**
 * RegistrationFrame - Signup with validation
 */
public class RegistrationFrame extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(RegistrationFrame.class.getName());

    public RegistrationFrame() {
        initComponents();
        setResizable(false);
        setSize(1070, 700);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        // ...existing code from your old RegistrationFrame.java's initComponents...
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new RegistrationFrame().setVisible(true));
    }
    // ...existing variable declarations...
}
