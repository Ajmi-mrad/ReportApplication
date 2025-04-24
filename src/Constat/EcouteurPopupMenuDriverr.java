package Constat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class EcouteurPopupMenuDriverr extends MouseAdapter implements ActionListener {
    private GestionDriverr gestionDriverr;

    public EcouteurPopupMenuDriverr(GestionDriverr gestionDriverr) {
        this.gestionDriverr = gestionDriverr;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Editer")) {
            gestionDriverr.editDriver();
        } else if (e.getActionCommand().equals("Supprimer")) {
            gestionDriverr.deleteDriver();
        }
    }
}