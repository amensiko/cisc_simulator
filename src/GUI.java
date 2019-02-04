import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JButton buttonStart;
    private JPanel panelMain;
    private JTextField r0_textField;
    private JLabel r0_label;
    private JLabel r1_label;
    private JTextField x2_textField;
    private JTextField r3_textField;
    private JTextField r1_textField;
    private JTextField r2_textField;
    private JTextField x1_textField;
    private JTextField x3_textField;
    private JTextField pc_textField;
    private JTextField cc_textField;
    private JTextField opcode_textField;
    private JTextField address_textField;
    private JTextField mfr_textField;
    private JTextField mbr_textField;
    private JTextField mar_textField;
    private JTextField ir_textField;
    private JButton clearButton;
    private JButton haltButton;
    private JButton singleStepButton;
    private JButton startButton;
    private JTextArea resultsTextArea;
    private JLabel x3_label;
    private JLabel r2_label;
    private JLabel r3_label;
    private JLabel x1_label;
    private JLabel x2_label;
    private JLabel cc_label;
    private JLabel pc_label;
    private JLabel address_label;
    private JLabel opcode_label;
    private JLabel ir_label;
    private JLabel mar_label;
    private JLabel mbr_label;
    private JLabel mfr_label;
    private JTextField irr_textField;
    private JTextField iar_textField;
    private JLabel irr_label;
    private JLabel iar_label;
    private JTextField ix_textField;
    private JTextField i_textField;
    private JLabel ix_label;
    private JLabel i_label;

    public GUI() {
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        r0_textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        r1_textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
