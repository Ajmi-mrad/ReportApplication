package Constat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurPopupMenuCarUser implements ActionListener {
    private GestionCarUser gestionCarUser;

    public EcouteurPopupMenuCarUser(GestionCarUser gestionCarUser) {
        this.gestionCarUser = gestionCarUser;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Editer")) {
            gestionCarUser.editCarUser();
        } else if (e.getActionCommand().equals("Supprimer")) {
            gestionCarUser.deleteCarUser();
        }
    }
}