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

    public static int[] memory = new int[2048];
    public static int[] ix_vals = new int[3];

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
        singleStepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        haltButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public void ipl(String req) {
        if (req.length() > 16) {
            resultsTextArea.append("Illegal request, machine fault\n");
            mfr_textField.setText("1");
        }

        opcode_textField.setText(req.substring(0, 6));
        ix_textField.setText(req.substring(8, 10));
        i_textField.setText("" + req.charAt(10));
        address_textField.setText(req.substring(11, 16));

        if (Integer.parseInt(req.substring(11, 16), 2) > 32) {
            mfr_textField.setText("3, illegal address");
        } else if (Integer.parseInt(req.substring(0, 6), 2) == 0) {
            resultsTextArea.append("Halting");
        }

        int opcode = Integer.parseInt(req.substring(0, 6), 2);
        int gpr = Integer.valueOf(req.substring(6, 8));  //general purpose register: R0, R1, R2, or R3
        int ix = Integer.valueOf(req.substring(8, 10));
        int ix_decimal = Integer.parseInt(req.substring(8, 10), 2);
        int i = Integer.valueOf(req.substring(10, 11));
        String address_binary = req.substring(11, 16);
        int address = Integer.valueOf(req.substring(11, 16));
        int address_decimal = Integer.parseInt(req.substring(11, 16), 2);

        int eff_addr = calculateEA(ix, i, address_decimal);

        if (opcode == 1) {
            ldrInstr(gpr, eff_addr, i);
        } else if (opcode == 2) {
            strInstr(gpr, eff_addr, i);
        } else if (opcode == 3) {
            ldaInstr(gpr, eff_addr);
        } else if (opcode == 41) {
            ldxInstr(ix, eff_addr, i);
        } else if(opcode==42) {
            stxInstr(ix, eff_addr, i);
        }


    }

    public int calculateEA(int ix, int i, int addr) {
        int eff_addr = 0;
        if (i != 0) {
            if (ix == 0) {
                eff_addr = memory[addr];
            }
            if (ix == 1) {
                eff_addr = memory[ix_vals[0] + addr];
            }
            if (ix == 2) {
                eff_addr = memory[ix_vals[1] + addr];
            }
            if (ix == 3) {
                eff_addr = memory[ix_vals[2] + addr];
            }
        } else if (i == 0) {
            if (ix == 0) {
                eff_addr = addr;
            }
            if (ix == 1) {
                eff_addr = addr + ix_vals[0];
            }
            if (ix == 2) {
                eff_addr = addr + ix_vals[1];
            }
            if (ix == 3) {
                eff_addr = addr + ix_vals[2];
            }
        }
        return eff_addr;
    }


}
