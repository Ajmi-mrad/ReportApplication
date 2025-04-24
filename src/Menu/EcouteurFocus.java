package Menu;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class EcouteurFocus implements FocusListener {

    GestionProfil gestionProfil;
    public EcouteurFocus(GestionProfil gestionProfil) {
        this.gestionProfil = gestionProfil;
    }

    @Override
    public void focusGained(FocusEvent e) {
    if (e.getSource() == gestionProfil.getTf_first_name()) {
            gestionProfil.getTf_first_name().setText("");
        }
        if (e.getSource() == gestionProfil.getTf_last_name()) {
            gestionProfil.getTf_last_name().setText("");
        }
        if (e.getSource() == gestionProfil.getTf_nickname()) {
            gestionProfil.getTf_nickname().setText("");
        }
    }
    @Override
    public void focusLost(FocusEvent e) {
        if(gestionProfil.getTf_first_name().getText().equals("")){
            gestionProfil.getTf_first_name().setText("Entrer votre nom");
        }
        if(gestionProfil.getTf_last_name().getText().equals("")){
            gestionProfil.getTf_last_name().setText("Entrer votre prenom");
        }
        if(gestionProfil.getTf_nickname().getText().equals("")){
            gestionProfil.getTf_nickname().setText("Entrer votre pseudo");
        }

    }
}