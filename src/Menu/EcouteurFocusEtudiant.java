// src/Menu/EcouteurFocusEtudiant.java
package Menu;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class EcouteurFocusEtudiant implements FocusListener {

    private GestionEtudiant gestionEtudiant;

    public EcouteurFocusEtudiant(GestionEtudiant gestionEtudiant) {
        this.gestionEtudiant = gestionEtudiant;
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() == gestionEtudiant.tf_nom && gestionEtudiant.tf_nom.getText().equals("Entrer votre nom")) {
            gestionEtudiant.tf_nom.setText("");
        }
        if (e.getSource() == gestionEtudiant.tf_prenom && gestionEtudiant.tf_prenom.getText().equals("Entrer votre prenom")) {
            gestionEtudiant.tf_prenom.setText("");
        }
        if (e.getSource() == gestionEtudiant.tf_cin && gestionEtudiant.tf_cin.getText().equals("Entrer votre cin")) {
            gestionEtudiant.tf_cin.setText("");
        }
        if (e.getSource() == gestionEtudiant.tf_moyenne && gestionEtudiant.tf_moyenne.getText().equals("Entrer votre moyenne")) {
            gestionEtudiant.tf_moyenne.setText("");
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() == gestionEtudiant.tf_nom && gestionEtudiant.tf_nom.getText().isEmpty()) {
            gestionEtudiant.tf_nom.setText("Entrer votre nom");
        }
        if (e.getSource() == gestionEtudiant.tf_prenom && gestionEtudiant.tf_prenom.getText().isEmpty()) {
            gestionEtudiant.tf_prenom.setText("Entrer votre prenom");
        }
        if (e.getSource() == gestionEtudiant.tf_cin && gestionEtudiant.tf_cin.getText().isEmpty()) {
            gestionEtudiant.tf_cin.setText("Entrer votre cin");
        }
        if (e.getSource() == gestionEtudiant.tf_moyenne && gestionEtudiant.tf_moyenne.getText().isEmpty()) {
            gestionEtudiant.tf_moyenne.setText("Entrer votre moyenne");
        }
    }
}