package Menu;

import Menu.GestionProfil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EcouteurLabel extends MouseAdapter {
    GestionProfil gestionProfil;

    public EcouteurLabel(GestionProfil gestionProfil) {
        this.gestionProfil = gestionProfil;
    }

    //color will be changed once ur mouse is passed
    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() instanceof JLabel) {
            JLabel lbl = (JLabel) e.getSource();
            lbl.setForeground(Color.GREEN);
        }
        //lorsque vous passez la souris sur la zone de saisie afficher un message qui lui correspond


    }
    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() instanceof JLabel) {
            JLabel lbl = (JLabel) e.getSource();
            lbl.setForeground(Color.BLACK);
        }
    }
}
