package Constat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bureau extends JFrame {
    //Declaration des composantes
    JMenuBar menubar;
    JMenu menu_cars;
    JMenu menu_profil;
    JMenu constat;
    JMenuItem editReport;
    JMenu menu_accident;
    //JMenuItem addCar;
    JMenuItem editCar;
    //JMenuItem deleteCar;
    JMenuItem editProfil;
    JMenuItem addDriver;
    JMenuItem editAccident;
    GestionCar gc;
    JDesktopPane desktop;

    //CurriculumForm cv;
    public Bureau() {
        setTitle("Rapido Report");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //creation de l'interface graphique
        menubar = new JMenuBar();
        menu_cars = new JMenu("Cars");
        menu_profil = new JMenu("Profil");
        menu_accident = new JMenu("Accident");
        constat = new JMenu("Constat");
        editReport = new JMenuItem("Edit Report");
        editProfil = new JMenuItem("Edit Profil");
        //addCar = new JMenuItem("Add");
        editCar = new JMenuItem("Edit");
        //deleteCar = new JMenuItem("Delete");
        editAccident = new JMenuItem("Edit Accident");
       // addDriver = new JMenuItem("Add Driver");
        //menu_cars.add(addCar);
        menu_cars.add(editCar);
       // menu_cars.add(deleteCar);
        //menu_cars.add(tpEtudiant);
        menu_profil.add(editProfil);
        //menu_profil.add(addDriver);
        menu_accident.add(editAccident);
        constat.add(editReport);

        menubar.add(menu_cars);
        menubar.add(menu_profil);
        menubar.add(constat);
        menubar.add(menu_accident);


        setJMenuBar(menubar);
        desktop = new JDesktopPane();
        // add a label bienvenu to the desktop
        JLabel lbl = new JLabel("Bienvenu");
        // color
        lbl.setForeground(java.awt.Color.RED);
        lbl.setBounds(200, 200, 100, 100);
        desktop.add(lbl);
        add(desktop);


        //ajout des ecouteurs
        /*
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

         */

        editCar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GestionCar gestionCar = new GestionCar();
                desktop.add(gestionCar);
                gestionCar.setVisible(true);
            }
        });

        editProfil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GestionDriverr gestionDriverr = new GestionDriverr();
                desktop.add(gestionDriverr);
                gestionDriverr.setVisible(true);
            }
        });
        editAccident.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GestionAccident gestionAccident = new GestionAccident();
                desktop.add(gestionAccident);
                gestionAccident.setVisible(true);
            }
        });
        editReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GestionReport gestionReport = new GestionReport();
                desktop.add(gestionReport);
                gestionReport.setVisible(true);
            }
        });

    }

    public static void main(String[] args) {
        Bureau b = new Bureau();
        b.setVisible(true);
    }
}

