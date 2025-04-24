package Menu;
/*
there two method to create a pdf file using itextpdf library u can add the dependency in the pom.xml file or
download the jar file from the official website and add it to the project
 */
/*
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

public class Frame extends JFrame {
    private JButton valideButton;
    private JButton quitButton;
    private JTextField nameField;
    private JComboBox<String> comboBoxNationality;
    private JRadioButton maleButton;
    private JRadioButton femaleButton;
    private JRadioButton otherButton;
    private ButtonGroup genderGroup;
    //private

    Frame(String name) {
        super(name);
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
        JLabel label = new JLabel("Hello World");
        label.setForeground(Color.RED);
        label.setFont(new Font("Serif", Font.PLAIN, 30));
        label.setOpaque(true);
        label.setBackground(Color.YELLOW);
        this.add(label);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setPreferredSize(new Dimension(700, 100));
        JLabel label2 = new JLabel("Enter your name: ");
        this.add(label2);
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(700, 100));
        this.add(nameField);
        // combobox
        JLabel label3 = new JLabel("Select your natioanility: ");
        this.add(label3);

        comboBoxNationality = new JComboBox<>(new String[]{"Tunisian", "French", "English"});
        comboBoxNationality.setPreferredSize(new Dimension(700, 100));
        this.add(comboBoxNationality);

        JLabel label4 = new JLabel("Select your gender: ");
        this.add(label4);
        maleButton = new JRadioButton("Male");
        femaleButton = new JRadioButton("Female");
        otherButton = new JRadioButton("Other");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        genderGroup.add(otherButton);
        this.add(maleButton);
        this.add(femaleButton);
        this.add(otherButton);


        valideButton = new JButton("Valider");
        this.add(valideButton);
        quitButton = new JButton("Quitter");
        this.add(quitButton);
        this.setVisible(true);

        valideButton.addActionListener(new Ecouteur());
        quitButton.addActionListener(new Ecouteur());

    }

    class Ecouteur implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == valideButton) {
                File name = new File("cv.html");
                try {
                    FileWriter fw = new FileWriter(name, false);

                    String gender = "";
                    if (maleButton.isSelected()) {
                        gender = "Male";
                    } else if (femaleButton.isSelected()) {
                        gender = "Female";
                    } else if (otherButton.isSelected()) {
                        gender = "Other";
                    }

                    fw.write("<html><head><title>CV</title>" +
                            "<h1>Nom et prenom: " + nameField.getText() + "</h1>" +
                            "<h1> Nationlity : " + comboBoxNationality.getSelectedItem().toString() + "</h1>"+
                            "<h1> Gender: "+gender+"</h1>"+

                            "</head><body>");
                    // write the content of the file into pdf file
                    fw.write("</body></html>");

                    fw.close();

                    Desktop.getDesktop().open(name);
                    /*
                    File file = new File("cv.pdf");
                try {
                    PdfWriter writer = new PdfWriter(file);
                    PdfDocument pdfDoc = new PdfDocument(writer);
                    Document document = new Document(pdfDoc);
                    document.add(new Paragraph("Nom et prenom: " + nameField.getText()));
                    document.close();

                    Desktop.getDesktop().open(file);
                } catch (Exception ex) {
                    System.out.println("Error: " + ex);
                }
            }
                     */
                } catch (Exception ex) {
                    System.out.println("Error: " + ex);
                }
            }
            if (e.getSource() == quitButton) {
                System.exit(0);
            }
        }
    }
}