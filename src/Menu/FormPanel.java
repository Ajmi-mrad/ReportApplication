package Menu;

import javax.swing.*;
import java.awt.*;

public class FormPanel extends JPanel {
    JLabel title;
    JButton btn;
    JPanel panel_nord;
    Profil profil;
    public FormPanel(Profil profil){
        this.profil= profil;
        this.setLayout(new BorderLayout());
        //this.setBackground(new Color(230, 230, 250));
        // ajout dans bienvenue le nom de profil et le prenom
        title = new JLabel("Bienvenue "+profil.getFirst_name()+" "+profil.getLast_name());

        this.add(title, BorderLayout.NORTH);
        panel_nord= new JPanel();
        // button on panel
        btn = new JButton("Click me");
        panel_nord.add(btn);
        this.add(panel_nord, BorderLayout.SOUTH);

    }
}
