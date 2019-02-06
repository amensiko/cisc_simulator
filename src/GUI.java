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
    public static int[] r = new int[4];
    public CacheSim cacheSim = new CacheSim();

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

    public void load(int i, int gpr, int cacheInd, int memoryInd, int tag, int eff_a) {
        int[] w = (int[])(((cacheSim.line).get(cacheInd)).get("w"));
        if (i == 0) {
            resultsTextArea.append("Indirect addressing: 0\n");
            if (gpr == 00) {
                r[0] = w[memoryInd];
                r0_textField.setText("" + r[0]);
                resultsTextArea.append("Loading word " + r[0]);
            }
            if (gpr == 01) {
                r[1] = w[memoryInd];
                r1_textField.setText("" + r[1]);
                resultsTextArea.append("Loading word " + r[1]);
            }
            if (gpr == 10) {
                r[2] = memory[eff_a];
                int number = ((tag * 8) + cacheInd) * 6;
                r2_textField.setText("" + r[2]);
                resultsTextArea.append("Loading word " + r[2]);
            }
            if (gpr == 11) {
                r[3] = w[memoryInd];
                r3_textField.setText("" + r[3]);
                resultsTextArea.append("Loading word " + r[3] + "\n");
            }
        }
        if (i == 1) {
            resultsTextArea.append("Indirect addressing: 1\n");
            int[] w1 = (int[])(((cacheSim.line).get(cacheInd)).get("w"));
            int wn = w1[memoryInd];
            if (gpr == 00) {
                r[0] = memory[wn];
                r0_textField.setText("" + r[0]);
                resultsTextArea.append("Loading word "+ r[0] + "\n");
            }
            if (gpr == 01) {
                r[1] = memory[wn];
                r1_textField.setText("" + r[1]);
                resultsTextArea.append("Loading word "+ r[1] + "\n");
            }
            if (gpr == 10) {
                r[2]=memory[wn];
                resultsTextArea.append("Loading word "+ r[2] + "\n");
                r2_textField.setText(""+r[2]);
            }
            if (gpr == 11) {
                r[3] = memory[wn];
                resultsTextArea.append("Loading word "+ r[3] + "\n");
                r3_textField.setText("" + r[3]);
            }
        }
    }


    public void ldrInstr(int gpr, int eff_a, int i) {
        String eabinary = Integer.toBinaryString(eff_a);
        int eff_addr = eff_a/6;
        int memoryInd = eff_a%6;
        String ea = "";
        while ((Integer.toBinaryString(eff_addr)).length() < 16) {
            ea = "0" + Integer.toBinaryString(eff_addr);
        }
        int cacheInd = Integer.parseInt(ea.substring(13,16),2);
        int tag = Integer.parseInt(ea.substring(0,13),2);
        if (((cacheSim.line).get(cacheInd)).get("tag").equals(tag)) {
            load(i, gpr, cacheInd, memoryInd, tag, eff_a);
        } else if (!(((cacheSim.line).get(cacheInd)).get("tag")).equals(tag)) {
            if (((cacheSim.line).get(cacheInd)).get("written").equals(1)) {
                int start = ((int)((cacheSim.line).get(cacheInd)).get("tag") * 8 + cacheInd) * 6;
                for (int c = 0; c < 6; c++) {
                    int[] w = (int[]) (((cacheSim.line).get(cacheInd)).get("w"));
                    memory[start + c] = w[c];
                }
                int cp = (tag * 8 + cacheInd) * 6;
                for (int c = 0; c < 6; c++) {
                    int[] w = (int[])(((cacheSim.line).get(cacheInd)).get("w"));
                    w[c] = memory[cp+c];
                }

                ((cacheSim.line).get(cacheInd)).put("tag", tag);
                ((cacheSim.line).get(cacheInd)).put("inline", 1);
                ((cacheSim.line).get(cacheInd)).put("written", 0);
                load(i, gpr, cacheInd, memoryInd, tag, eff_a);
            } else if (((cacheSim.line).get(cacheInd)).get("written").equals(0)) {

                int cp = (tag * 8 + cacheInd) * 6;
                for(int c=0; c < 6; c++) {
                    int[] w = (int[])(((cacheSim.line).get(cacheInd)).get("w"));
                    w[c] = memory[cp + c];
                }
                ((cacheSim.line).get(cacheInd)).put("tag", tag);
                ((cacheSim.line).get(cacheInd)).put("inline", 1);
                ((cacheSim.line).get(cacheInd)).put("written", 0);
                load(i, gpr, cacheInd, memoryInd, tag, eff_a);
            }
        }
    }

    public void strInstr(int gpr, int eff_a, int i) {
        resultsTextArea.append("Address: " + Integer.toBinaryString(eff_a));
        if (i == 0) {
            int eff_addr = eff_a/6;
            int memoryInd = eff_a%6;
            String eaBinary = Integer.toBinaryString(eff_addr);
            while (eaBinary.length() < 16) {
                eaBinary = "0" + eaBinary;
            }
            int cacheInd = Integer.parseInt(eaBinary.substring(13,16),2);
            int tag = Integer.parseInt(eaBinary.substring(0,13),2);
            int[] w = (int[])(((cacheSim.line).get(cacheInd)).get("w"));
            if (((cacheSim.line).get(cacheInd)).get("tag").equals(tag)) {
                resultsTextArea.append("In cache "+ cacheInd);
                ((cacheSim.line).get(cacheInd)).put("inline", 1);
                ((cacheSim.line).get(cacheInd)).put("written", 1);
                if (gpr == 00){
                    w[memoryInd] = r[0];
                }
                if (gpr == 01) {
                    w[memoryInd] = r[1];
                }
                if (gpr == 10) {
                    w[memoryInd] = r[2];
                }
                if (gpr == 11) {
                    w[memoryInd] = r[3];
                }
            }
            if (!(((cacheSim.line).get(cacheInd)).get("tag")).equals(tag)) {
                if (gpr == 00) {
                    memory[eff_a] = r[0];
                    w[memoryInd] = r[0];
                }
                if (gpr == 01) {
                    memory[eff_a] = r[1];
                    w[memoryInd] = r[1];
                }
                if (gpr == 10) {
                    memory[eff_a] = r[2];
                    w[memoryInd] = r[2];
                }
                if (gpr == 11) {
                    memory[eff_a] = r[3];
                    w[memoryInd] = r[3];
                }
            }
        }
        if (i == 1) {
            int eff_a1 = memory[eff_a];
            int eff_addr = eff_a1/6;//effaddr equals to the number of block
            int memoryInd = eff_a1%6; //memIndex is the position of Word in a block
            String eabin = Integer.toBinaryString(eff_addr);
            int cacheInd = Integer.parseInt(eabin.substring(13,16),2);
            int tag = Integer.parseInt(eabin.substring(0,13),2);
            int[] w = (int[])(((cacheSim.line).get(cacheInd)).get("w"));
            if (((cacheSim.line).get(cacheInd)).get("tag").equals(tag)) {
                resultsTextArea.append("In cache "+ cacheInd);
                ((cacheSim.line).get(cacheInd)).put("inline", 1);
                ((cacheSim.line).get(cacheInd)).put("written", 1);
                if (gpr == 00) {
                    w[memoryInd] = r[0];
                }
                if (gpr == 01) {
                    w[memoryInd] = r[1];
                }
                if (gpr == 10) {
                    w[memoryInd] = r[2];
                }
                if (gpr == 11) {
                    w[memoryInd] = r[3];
                }
            }
            if (!(((cacheSim.line).get(cacheInd)).get("tag")).equals(tag)) {
                if (gpr == 00) {
                    memory[eff_a1] = r[0];
                }
                if (gpr == 01) {
                    memory[eff_a1] = r[1];
                }
                if (gpr == 10) {
                    memory[eff_a1] = r[2];
                }
                if (gpr == 11) {
                    memory[eff_a1] = r[3];
                }
            }
        }
    }


    public void ldaInstr(int gpr, int eff_a) {
        String eabinary=Integer.toBinaryString(eff_a);
        if (gpr == 00) {
            r[0] = eff_a;
        }
        if (gpr == 01) {
            r[1] = eff_a;
        }
        if (gpr == 10) {
            r[2]= eff_a;
        }
        if (gpr == 11) {
            r[3]= eff_a;
        }
    }


    public void ldxInstr(int ix,int ea,int ival) {
        //TODO
    }


    public void stxInstr(int ix, int eff_a, int i) {
        String eaBin = Integer.toBinaryString(eff_a);//write back policy
        if (i==0) {
            int eff_addr = eff_a/6;
            int memoryInd = eff_a%6;
            String eaBinary = Integer.toBinaryString(eff_addr);
            while (eaBinary.length() < 16) {
                eaBinary = "0" + eaBinary;
            }
            int cacheInd = Integer.parseInt(eaBinary.substring(13,16),2);
            int tag = Integer.parseInt(eaBinary.substring(0,13),2);
            int[] w = (int[])(((cacheSim.line).get(cacheInd)).get("w"));

            if (((cacheSim.line).get(cacheInd)).get("tag").equals(tag)) {
                resultsTextArea.append("In cache "+ cacheInd);
                ((cacheSim.line).get(cacheInd)).put("inline", 1);
                ((cacheSim.line).get(cacheInd)).put("written", 1);
                if (ix == 01) {
                    w[memoryInd] = ix_vals[0];
                }
                if (ix == 10) {
                    w[memoryInd] = ix_vals[1];
                }
                if (ix == 11) {
                    w[memoryInd] = ix_vals[2];
                }
            }
            if (!(((cacheSim.line).get(cacheInd)).get("tag")).equals(tag)) {
                if (ix == 01) {
                    w[memoryInd] = ix_vals[0];
                }
                if(ix == 10) {
                    w[memoryInd] = ix_vals[1];
                }
                if(ix == 11) {
                    w[memoryInd] = ix_vals[2];
                }
            }
        }
    }

}
