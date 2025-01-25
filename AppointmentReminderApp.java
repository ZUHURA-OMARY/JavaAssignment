import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class AppointmentReminderApp {

    private JFrame frame;
    private JPanel homePanel;
    ArrayList<Appointment> appointments;
    DefaultListModel<Appointment> listModel;
    private JLabel noAppointmentsLabel;
    private final String FILE_NAME = "appointments.txt"; 

    public AppointmentReminderApp() {
        frame = new JFrame("Appointment App");
        appointments = new ArrayList<>();

        loadAppointments(); 

        homePanel = createHomeScreen();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(homePanel);
        frame.setVisible(true);
    }

    private JPanel createHomeScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255)); 

        JPanel titleLogoPanel = new JPanel();
        titleLogoPanel.setLayout(new BorderLayout());
        titleLogoPanel.setBackground(new Color(135, 206, 250)); 
        titleLogoPanel.setPreferredSize(new Dimension(600, 100));

        JLabel titleLabel = new JLabel("Appointment App", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 
        titleLogoPanel.add(titleLabel, BorderLayout.WEST);

        JLabel logoLabel = new JLabel(new ImageIcon(
                new ImageIcon("images/appointment.png").getImage().getScaledInstance(180, 80, Image.SCALE_SMOOTH)),
                SwingConstants.RIGHT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
        titleLogoPanel.add(logoLabel, BorderLayout.EAST);

        panel.add(titleLogoPanel, BorderLayout.NORTH);

        noAppointmentsLabel = new JLabel("No Appointments Available", SwingConstants.CENTER);
        noAppointmentsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        noAppointmentsLabel.setForeground(Color.GRAY);

        
        listModel = new DefaultListModel<>();
        JList<Appointment> appointmentList = new JList<>(listModel);
        appointmentList.setCellRenderer(new AppointmentListRenderer(this)); 

        refreshAppointmentList();

        panel.add(noAppointmentsLabel, BorderLayout.CENTER); 
        panel.add(new JScrollPane(appointmentList), BorderLayout.CENTER); 

        panel.add(createBottomButtons(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBottomButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(new Color(240, 248, 255));

        JButton addButton = new JButton("Add Appointment");
        styleButton(addButton, new Color(50, 205, 50), Color.WHITE);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddAppointmentDialog();
            }
        });

        JButton clearButton = new JButton("Clear All Appointments");
        styleButton(clearButton, new Color(255, 69, 0), Color.WHITE); 
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmation = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to clear all appointments?",
                        "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    appointments.clear();
                    saveAppointments(); 
                    refreshAppointmentList();
                }
            }
        });

        panel.add(addButton);
        panel.add(clearButton);

        return panel;
    }

    private void showAddAppointmentDialog() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10)); 

        JTextField dateField = new JTextField();
        JTextField timeField = new JTextField();
        JTextField detailsField = new JTextField();

        inputPanel.add(new JLabel("Date:"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Time:"));
        inputPanel.add(timeField);
        inputPanel.add(new JLabel("Details:"));
        inputPanel.add(detailsField);

        int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Edit Appointment", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String date = dateField.getText();
            String time = timeField.getText();
            String details = detailsField.getText();

            if (!date.isEmpty() && !time.isEmpty() && !details.isEmpty()) {
                appointments.add(new Appointment(date, time, details));
                saveAppointments(); 
                refreshAppointmentList();
            } else {
                JOptionPane.showMessageDialog(frame, "All fields must be filled out", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    

    private void styleButton(JButton button, Color bgColor, Color textColor) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(textColor); 
        button.setBackground(bgColor); 
        button.setOpaque(true);
        button.setBorderPainted(false); 
    }

    public void showEditAppointmentDialog(int index) {
        Appointment appointment = appointments.get(index);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10)); 
        JTextField dateField = new JTextField(appointment.getDate());
        JTextField timeField = new JTextField(appointment.getTime());
        JTextField detailsField = new JTextField(appointment.getDetails());

        inputPanel.add(new JLabel("Date:"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Time:"));
        inputPanel.add(timeField);
        inputPanel.add(new JLabel("Details:"));
        inputPanel.add(detailsField);

        int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Edit Appointment", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String date = dateField.getText();
            String time = timeField.getText();
            String details = detailsField.getText();

            if (!date.isEmpty() && !time.isEmpty() && !details.isEmpty()) {
                appointment.setDate(date);
                appointment.setTime(time);
                appointment.setDetails(details);

                saveAppointments(); 
                refreshAppointmentList(); 
            } else {
                JOptionPane.showMessageDialog(frame, "All fields must be filled out", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void deleteAppointment(int index) {
        int confirmation = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to delete this appointment?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            appointments.remove(index); 
            saveAppointments(); 
            refreshAppointmentList(); 
        }
    }

    private void refreshAppointmentList() {
        listModel.clear();
        if (appointments.isEmpty()) {
            noAppointmentsLabel.setVisible(true); 
        } else {
            noAppointmentsLabel.setVisible(false);
            for (Appointment appointment : appointments) {
                listModel.addElement(appointment);
            }
        }
    }

    private void loadAppointments() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    appointments.add(new Appointment(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
        }
    }

    void saveAppointments() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Appointment appointment : appointments) {
                writer.write(appointment.getDate() + ";" + appointment.getTime() + ";" + appointment.getDetails());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AppointmentReminderApp();
    }
}

class Appointment {
    private String date;
    private String time;
    private String details;

    public Appointment(String date, String time, String details) {
        this.date = date;
        this.time = time;
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDetails() {
        return details;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return date + " " + time + " : " + details;
    }
}


class AppointmentListRenderer extends JPanel implements ListCellRenderer<Appointment> {
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
