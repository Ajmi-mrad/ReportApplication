// src/Menu/EcouteurPopupMenuEtudiant.java
package Menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class EcouteurPopupMenuEtudiant extends MouseAdapter implements ActionListener {
    private GestionEtudiant gestionEtudiant;

    public EcouteurPopupMenuEtudiant(GestionEtudiant gestionEtudiant) {
        this.gestionEtudiant = gestionEtudiant;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Editer")) {
            gestionEtudiant.editEtudiant();
        } else if (e.getActionCommand().equals("Supprimer")) {
            gestionEtudiant.deleteEtudiant();
        }
    }
}