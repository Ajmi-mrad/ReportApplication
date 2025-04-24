package Menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EcouteurPopupMenu extends MouseAdapter implements ActionListener{
    private GestionProfil gestionProfil;

    public EcouteurPopupMenu(GestionProfil gestionProfil) {
        this.gestionProfil = gestionProfil;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==gestionProfil.menuItemSup){
            gestionProfil.supprimerProfil();
        }

        if(e.getSource()==gestionProfil.menuItemSupTous){
            gestionProfil.supprimerTous();
        }
        if(e.getSource()==gestionProfil.menuItemEdit){
            gestionProfil.editerProfil();
        }


    }
}
