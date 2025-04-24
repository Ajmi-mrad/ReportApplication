package Constat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurPopupMenuAccident implements ActionListener {
    private GestionAccident gestionAccident;

    public EcouteurPopupMenuAccident(GestionAccident gestionAccident) {
        this.gestionAccident = gestionAccident;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Editer")) {
            gestionAccident.editAccident();
        } else if (command.equals("Supprimer")) {
            gestionAccident.deleteAccident();
        }
    }
}