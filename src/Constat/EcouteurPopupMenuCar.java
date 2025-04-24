package Constat;

import Menu.GestionEtudiant;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class EcouteurPopupMenuCar extends MouseAdapter implements ActionListener {
    private GestionCar gestionCar;

    public EcouteurPopupMenuCar(GestionCar gestionCar) {
        this.gestionCar = gestionCar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Editer")) {
            gestionCar.editCar();
        } else if (e.getActionCommand().equals("Supprimer")) {
            gestionCar.deleteCar();
        }
    }
}
