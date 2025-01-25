import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JFrame {
    public SplashScreen() {
        setTitle("Appointment Reminder App");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setUndecorated(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(65, 45, 34));

        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon(new ImageIcon("images/appointment.png").getImage().getScaledInstance(250, 150, Image.SCALE_SMOOTH));
        imageLabel.setIcon(icon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(imageLabel, BorderLayout.NORTH);

        JLabel title = new JLabel("Appointment Reminder App", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.CENTER);

        JLabel loading = new JLabel("Loading...", JLabel.CENTER);
        loading.setFont(new Font("Arial", Font.BOLD, 16));
        loading.setForeground(Color.LIGHT_GRAY);
        panel.add(loading, BorderLayout.SOUTH);

        add(panel);

        new Timer(3000, e -> {
            ((Timer) e.getSource()).stop();
            dispose(); 
            new AppointmentReminderApp(); 
        }).start();

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SplashScreen::new);
    }
}

