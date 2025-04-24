// src/Menu/GestionEtudiant.java
package Menu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class GestionEtudiant extends JFrame {
    JLabel lb_nom, lb_prenom, lb_cin, lb_moyenne;
    JTextField tf_nom, tf_prenom, tf_cin, tf_moyenne;
    JButton btn_add;
    JPanel panel_nord;
    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem editItem = new JMenuItem("Editer");
    JMenuItem deleteItem = new JMenuItem("Supprimer");
    MyTableModel myTableModel;
    JTable table;

    private void validateTextField(JTextField textField, String placeholder) {
        if (textField.getText().isEmpty() || textField.getText().equals(placeholder)) {
            textField.setBackground(Color.RED);
        } else {
            textField.setBackground(Color.WHITE);
        }
    }

    public GestionEtudiant() {
        this.setTitle("Gestion Etudiant");
        this.setSize(500, 500);

        getContentPane().setBackground(new Color(230, 230, 250));

        lb_nom = new JLabel("Nom : ");
        lb_prenom = new JLabel("Prenom : ");
        lb_cin = new JLabel("Cin : ");
        lb_moyenne = new JLabel("Moyenne : ");

        tf_nom = new JTextField(10);
        tf_nom.setText("Entrer votre nom");
        tf_prenom = new JTextField(10);
        tf_prenom.setText("Entrer votre prenom");
        tf_cin = new JTextField(10);
        tf_cin.setText("Entrer votre cin");
        tf_moyenne = new JTextField(10);
        tf_moyenne.setText("Entrer votre moyenne");
/*
        tf_nom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (tf_nom.getText().equals("Entrer votre nom")) {
                    tf_nom.setText("");
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateTextField(tf_nom, "Entrer votre nom");
            }
        });

        tf_prenom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (tf_prenom.getText().equals("Entrer votre prenom")) {
                    tf_prenom.setText("");
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateTextField(tf_prenom, "Entrer votre prenom");
            }
        });

        tf_cin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (tf_cin.getText().equals("Entrer votre cin")) {
                    tf_cin.setText("");
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateTextField(tf_cin, "Entrer votre cin");
            }
        });

        tf_moyenne.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (tf_moyenne.getText().equals("Entrer votre moyenne")) {
                    tf_moyenne.setText("");
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateTextField(tf_moyenne, "Entrer votre moyenne");
            }
        });

 */
        // appel to EcouteurFocudEtudiant
        tf_nom.addFocusListener(new EcouteurFocusEtudiant(this));
        tf_prenom.addFocusListener(new EcouteurFocusEtudiant(this));
        tf_cin.addFocusListener(new EcouteurFocusEtudiant(this));
        tf_moyenne.addFocusListener(new EcouteurFocusEtudiant(this));

        btn_add = new JButton("Ajouter");

        panel_nord = new JPanel();
        panel_nord.setLayout(new GridLayout(4, 2, 5, 5));
        panel_nord.add(lb_nom);
        panel_nord.add(tf_nom);
        panel_nord.add(lb_prenom);
        panel_nord.add(tf_prenom);
        panel_nord.add(lb_cin);
        panel_nord.add(tf_cin);
        panel_nord.add(lb_moyenne);
        panel_nord.add(tf_moyenne);

        JPanel panel_bouton = new JPanel();
        panel_bouton.add(btn_add);

        JPanel panel_principal = new JPanel();
        panel_principal.setLayout(new BoxLayout(panel_principal, BoxLayout.Y_AXIS));
        panel_principal.add(panel_nord);
        panel_principal.add(panel_bouton);
        this.add(panel_principal, BorderLayout.NORTH);

        EtudiantManager etudiantManager = new EtudiantManager();
        etudiantManager.createTable();
        ResultSet rs = etudiantManager.selectAllEtudiants();

        myTableModel = new MyTableModel(rs, etudiantManager);
        table = new JTable(myTableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        this.add(scrollPane, BorderLayout.CENTER);
        table.setFillsViewportHeight(true);

        btn_add.addActionListener(e -> {
            String nom = tf_nom.getText();
            String prenom = tf_prenom.getText();
            String cin = tf_cin.getText();
            String moyenne = tf_moyenne.getText();

            if (nom.isEmpty() || prenom.isEmpty() || cin.isEmpty() || moyenne.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Entrer tous les champs");
                return;
            }
            if (nom.equals("Entrer votre nom") || prenom.equals("Entrer votre prenom") || cin.equals("Entrer votre cin") || moyenne.equals("Entrer votre moyenne")) {
                JOptionPane.showMessageDialog(this, "Entrer tous les champs");
                return;
            }
            try {
                float moyenneFloat = Float.parseFloat(moyenne);
                if (moyenneFloat < 0 || moyenneFloat > 20) {
                    JOptionPane.showMessageDialog(this, "La moyenne doit être entre 0 et 20");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La moyenne doit être un nombre");
                return;
            }

            if (myTableModel.etudiantExist(cin)) {
                JOptionPane.showMessageDialog(this, "Etudiant déjà existant");
            } else {
                int result = myTableModel.ajoutEtudiant(nom, prenom, cin, moyenne);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Etudiant ajouté avec succès");
                }
            }
            tf_nom.setText("");
            tf_prenom.setText("");
            tf_cin.setText("");
            tf_moyenne.setText("");
        });

        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
        table.setComponentPopupMenu(popupMenu);

        editItem.addActionListener(new EcouteurPopupMenuEtudiant(this));
        deleteItem.addActionListener(new EcouteurPopupMenuEtudiant(this));
    }

    public void editEtudiant() {
        int row = table.getSelectedRow();
        if (row != -1) {
            String cin = table.getValueAt(row, 0).toString();
            String currentNom = table.getValueAt(row, 1).toString();
            String currentPrenom = table.getValueAt(row, 2).toString();
            String currentMoyenne = table.getValueAt(row, 3).toString();

            JTextField nomField = new JTextField(currentNom);
            JTextField prenomField = new JTextField(currentPrenom);
            JTextField moyenneField = new JTextField(currentMoyenne);

            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Nom:"));
            panel.add(nomField);
            panel.add(new JLabel("Prenom:"));
            panel.add(prenomField);
            panel.add(new JLabel("Moyenne:"));
            panel.add(moyenneField);

            int result = JOptionPane.showConfirmDialog(null, panel,
                    "Modifier étudiant", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String moyenne = moyenneField.getText();

                int updateResult = myTableModel.modifierEtudiant(cin, nom, prenom, moyenne);
                if (updateResult > 0) {
                    JOptionPane.showMessageDialog(this, "Étudiant modifié avec succès");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la modification");
                }
            } else if (result == JOptionPane.CANCEL_OPTION) {
                JOptionPane.showMessageDialog(this, "Aucune modification");
            }
        }
    }

    public void deleteEtudiant() {
        int row = table.getSelectedRow();
        if (row != -1) {
            String cin = table.getValueAt(row, 0).toString();
            System.out.println("Trying to delete CIN: " + cin);
            JPanel panelConfirmation = new JPanel(new GridLayout(3, 2));
            panelConfirmation.add(new JLabel("Êtes-vous sûr de vouloir supprimer cet étudiant ?"));
            int result = JOptionPane.showConfirmDialog(null, panelConfirmation,
                    "Supprimer étudiant", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                int deleteResult = myTableModel.suppEudiant(cin);
                if (deleteResult > 0) {
                    JOptionPane.showMessageDialog(this, "Étudiant supprimé avec succès");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'étudiant");
                }
            } else if (result == JOptionPane.CANCEL_OPTION) {
                JOptionPane.showMessageDialog(this, "Aucune suppression");
            }
        }
    }
}