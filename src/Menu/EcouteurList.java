package Menu;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EcouteurList extends MouseAdapter {
    private GestionProfil gestionProfil;

    public EcouteurList(GestionProfil gestionProfil) {
        this.gestionProfil = gestionProfil;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount()==2){

        }

    }
}
