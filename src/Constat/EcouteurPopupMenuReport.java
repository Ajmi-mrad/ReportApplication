package Constat;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class EcouteurPopupMenuReport extends MouseAdapter implements ActionListener {
    private GestionReport gestionReport;

    public EcouteurPopupMenuReport(GestionReport gestionReport) {
        this.gestionReport = gestionReport;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Editer")) {
            gestionReport.editReport();
        } else if (e.getActionCommand().equals("Supprimer")) {
            gestionReport.deleteReport();
        }
    }
}