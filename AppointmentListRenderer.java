

import javax.swing.*;
import java.awt.*;

public class AppointmentListRenderer extends JPanel implements ListCellRenderer<Appointment> {
    private JLabel appointmentLabel;
    private JButton editButton;
    private JButton deleteButton;
    private AppointmentReminderApp app;
    private int currentIndex;

    public AppointmentListRenderer(AppointmentReminderApp app) {
        this.app = app;
        setLayout(new BorderLayout(10, 10));
        appointmentLabel = new JLabel();
        add(appointmentLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        editButton = new JButton("Edit");
        styleActionButton(editButton, new Color(100, 149, 237));
        editButton.addActionListener(e -> app.showEditAppointmentDialog(currentIndex));
        buttonPanel.add(editButton);

        deleteButton = new JButton("Delete");
        styleActionButton(deleteButton, new Color(220, 20, 60)); 
        deleteButton.addActionListener(e -> app.deleteAppointment(currentIndex));
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.EAST);
        setFocusable(true);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends Appointment> list,
            Appointment appointment,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        appointmentLabel.setText(appointment.toString());
        currentIndex = index;

        if (isSelected) {
            setBackground(new Color(173, 216, 230)); 
        } else {
            setBackground(Color.WHITE);
        }

        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        return this;
    }

    private void styleActionButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor); 
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
    }
}
