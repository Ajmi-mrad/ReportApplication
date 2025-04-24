package Menu;
import  javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GestionProfil extends JInternalFrame {
    //Declaration des composantes
    JLabel lb_first_name;
    JLabel lb_last_name;
    JLabel lb_nickname;
    JTextField tf_first_name;
    JTextField tf_last_name;
    JTextField tf_nickname;
    JButton btn_save;
    JPanel panel_nord;
    JSplitPane jsp;
    JList gl;
    JTabbedPane jtp;
    DefaultListModel model;
    JLabel lbl_help;
    ArrayList <FormPanel> tabulationList = new ArrayList<>();
    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem menuItemSup = new JMenuItem("Supprimer");
    JMenuItem menuItemEdit = new JMenuItem("Edit");
    JMenuItem menuItemSupTous = new JMenuItem("supprimer tous");
    public JTextField getTf_first_name() {
        return tf_first_name;
    }

    public void setTf_first_name(JTextField tf_first_name) {
        this.tf_first_name = tf_first_name;
    }

    public JTextField getTf_last_name() {
        return tf_last_name;
    }

    public void setTf_last_name(JTextField tf_last_name) {
        this.tf_last_name = tf_last_name;
    }

    public JTextField getTf_nickname() {
        return tf_nickname;
    }

    public void setTf_nickname(JTextField tf_nickname) {
        this.tf_nickname = tf_nickname;
    }

    public GestionProfil() {
        this.setTitle("Gestion Profil");
        this.setSize(500, 500);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setMaximizable(true);
        this.setResizable(true);
        //creation de l'interface graphique
        getContentPane().setBackground(new Color(230, 230, 250));
        lb_first_name = new JLabel("Nom : ");
        lb_last_name = new JLabel("Prenom : ");
        lb_nickname = new JLabel("Pseudo : ");
        tf_first_name = new JTextField(10);
        tf_first_name.setText("Entrer votre nom");
        tf_last_name = new JTextField(10);
        tf_last_name.setText("Entrer votre prenom");
        tf_nickname = new JTextField(10);
        tf_nickname.setText("Entrer votre pseudo");
        btn_save = new JButton("Enregistrer");
        panel_nord = new JPanel();
        panel_nord.add(lb_first_name);
        panel_nord.add(tf_first_name);
        panel_nord.add(lb_last_name);
        panel_nord.add(tf_last_name);
        panel_nord.add(lb_nickname);
        panel_nord.add(tf_nickname);
        panel_nord.add(btn_save);



        this.add(panel_nord, BorderLayout.NORTH);
        gl = new JList();
        gl.setPreferredSize(new Dimension(500, 200));
        model = new DefaultListModel();
        gl.setModel(model);

        jsp = new JSplitPane();
        jtp = new JTabbedPane();
        //jtp.addTab("Tab 1", new JTabbedPane());
        //jtp.addTab("Tab 2", new JTabbedPane());
        jsp.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        jsp.setRightComponent(jtp);
        jsp.setLeftComponent(gl);
        this.add(jsp, BorderLayout.CENTER);
        lbl_help = new JLabel("Help");
        lbl_help.setForeground(Color.BLACK);
        this.add(lbl_help, BorderLayout.SOUTH);



        //Events
        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //enregistrer le profil dans data
                Profil profil = new Profil();
                profil.setFirst_name(tf_first_name.getText());
                profil.setLast_name(tf_last_name.getText());
                profil.setNickname(tf_nickname.getText());

                // appel de la fonction recherche du class profile if profil does not exist show message
                Profil p = Profil.rechercheProfil(profil.getNickname());
                if(p.getNickname() != null){
                    JOptionPane.showMessageDialog(GestionProfil.this, "Pseudo déjà utilisé");
                    return;
                }
                Data.data.add(profil);
                JOptionPane.showMessageDialog(GestionProfil.this, "Profil enregistré avec succès");
                model.addElement(tf_nickname.getText());
            }

        });
        // action sur le bouton save
        btn_save.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btn_save.doClick();
                }
            }
        });

        tf_first_name.addFocusListener(new EcouteurFocus(this));
        tf_last_name.addFocusListener(new EcouteurFocus(this));
        tf_nickname.addFocusListener(new EcouteurFocus(this));

        lb_first_name.addMouseListener(new EcouteurLabel(this));
        lb_last_name.addMouseListener(new EcouteurLabel(this));
        lb_nickname.addMouseListener(new EcouteurLabel(this));
        // action sur le JList
        //gl.addMouseListener(new EcouteurList(this));

        gl.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {
                    // when double click on the list check if exist in the jtp if not add it
                    String selectedPseudo = (String) gl.getSelectedValue();
                    boolean tabExists = false;

                    // Check if the tab already exists
                    for (int i = 0; i < jtp.getTabCount(); i++) {
                        if (jtp.getTitleAt(i).equals(selectedPseudo)) {
                            tabExists = true;
                            break;
                        }
                    }
                    // If the tab does not exist, add it
                    if (tabExists==false) {
                        Profil p = Profil.rechercheProfil(selectedPseudo);
                        FormPanel pn = new FormPanel(p);
                        tabulationList.add(pn);
                        jtp.addTab(selectedPseudo, pn);
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    // button3 click droit

                    popupMenu.add(menuItemSup);
                    popupMenu.add(menuItemEdit);
                    popupMenu.add(menuItemSupTous);
                    // add action listener to menu items

                    menuItemSupTous.addActionListener(new EcouteurPopupMenu(GestionProfil.this));
                    menuItemSup.addActionListener(new EcouteurPopupMenu(GestionProfil.this));
                    menuItemEdit.addActionListener(new EcouteurPopupMenu(GestionProfil.this));
                    // Function that Supprimer profile

                    popupMenu.show(gl,e.getX(),e.getY());
                }
            }
        });

        this.setVisible(true);

    }

    public void supprimerProfil() {
        Profil p = Profil.rechercheProfil(gl.getSelectedValue() + "");
        System.out.println("Profil à supprimer: " + gl.getSelectedValue());

        if (p != null) {
            Data.data.remove(p);
            System.out.println("Profil supprimé: " + p.getNickname());
        } else {
            System.out.println("Profil introuvable !");
        }
        for (int i = 0; i < jtp.getTabCount(); i++) {
            System.out.println("Checking tab: " + jtp.getTitleAt(i));
            if (jtp.getTitleAt(i).equals(gl.getSelectedValue() + "")) {
                System.out.println("Tab to remove found: " + jtp.getTitleAt(i));
                jtp.removeTabAt(i);
                System.out.println("Tab removed: " + gl.getSelectedValue());
                break;
            }
        }
        model.removeElement(gl.getSelectedValue());

    }
    public void editerProfil() {
        String selectedPseudo = (String) gl.getSelectedValue();
        Profil p = Profil.rechercheProfil(selectedPseudo);

        if (p != null) {
            String newPseudo = JOptionPane.showInputDialog(this, "Modifier le pseudo:", p.getNickname());
            Profil pr = Profil.rechercheProfil(newPseudo);
            if(pr.getNickname() != null){
                JOptionPane.showMessageDialog(GestionProfil.this, "Pseudo déjà utilisé");
                return;
            }
            if (newPseudo != null && !newPseudo.trim().isEmpty()){
                // Update the profile in Data.data
                p.setNickname(newPseudo);

                // Update the JList model
                int selectedIndex = gl.getSelectedIndex();
                model.set(selectedIndex, newPseudo);

                // Update the tab title if it exists
                for (int i = 0; i < jtp.getTabCount(); i++) {
                    if (jtp.getTitleAt(i).equals(selectedPseudo)) {
                        jtp.setTitleAt(i, newPseudo);
                        break;
                    }
                }
                JOptionPane.showMessageDialog(this, "Pseudo modifié avec succès");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Profil introuvable !");
        }
    }
    public void supprimerTous() {
        Data.data.clear();
        model.clear();
        jtp.removeAll();
    }
}
