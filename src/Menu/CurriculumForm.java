package Menu;

import javax.swing.*;

public class CurriculumForm extends JInternalFrame {
    public CurriculumForm() {
        setTitle("Curriculum Vitae");
        setSize(300, 300);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        // Add components to the internal frame
        JPanel panel = new JPanel();

        panel.add(new JLabel("Email:"));
        panel.add(new JTextField(20));
        panel.add(new JLabel("Phone:"));
        panel.add(new JTextField(15));

        add(panel);
    }
}