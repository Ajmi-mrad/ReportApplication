package Menu;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bureau extends JFrame {
    //Declaration des composantes
    JMenuBar menubar;
    JMenu menu_TP_Swing;
    JMenu menu_TP_BD;
    JMenuItem tp1;
    JMenuItem tp2;
    JMenuItem tpEtudiant;
    GestionProfil gp;
    JDesktopPane desktop;
    CurriculumForm cv;
    public Bureau() {
        setTitle("Java swing project");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //creation de l'interface graphique
        menubar = new JMenuBar();
        menu_TP_Swing = new JMenu("TP Swing");
        menu_TP_BD = new JMenu("TP BD");
        tpEtudiant = new JMenuItem("TP Etudiant");
        tp1 = new JMenuItem("TP 1");
        tp2 = new JMenuItem("TP 2");
        menu_TP_Swing.add(tp1);
        menu_TP_Swing.add(tp2);
        menu_TP_BD.add(tpEtudiant);
        menubar.add(menu_TP_Swing);
        menubar.add(menu_TP_BD);
        setJMenuBar(menubar);
        desktop = new JDesktopPane();
        // add a label bienvenu to the desktop
        JLabel lbl = new JLabel("Bienvenu");
        // color
        lbl.setForeground(java.awt.Color.RED);
        lbl.setBounds(200, 200, 100, 100);
        desktop.add(lbl);
        add(desktop);
        /*
        gp = new GestionProfil();
        cv = new CurriculumForm();
        desktop.add(gp);
        desktop.add(cv);
        this.add(desktop);

         */
        //ajout des ecouteurs
        tp1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //CurriculumForm curriculumForm = new CurriculumForm();
                Frame frame = new Frame("Curriculum From");
                //desktop.add(curriculumForm);
                desktop.add(frame);
                //this.add(desktop);
                //curriculumForm.setVisible(true);
                //frame.setVisible(true);
            }
        });

        tp2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GestionProfil gestionProfil = new GestionProfil();
                desktop.add(gestionProfil);
                gestionProfil.setVisible(true);
            }
        });
        tpEtudiant.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GestionEtudiant gestionEtudiant = new GestionEtudiant();
                //desktop.add(gestionEtudiant);
                gestionEtudiant.setSize(500, 500);
                gestionEtudiant.setVisible(true);
            }
        });
    }


    public static void main(String[] args) {
        Bureau b = new Bureau();
        b.setVisible(true);
    }


}
