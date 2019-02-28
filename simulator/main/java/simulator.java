import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DecimalFormat;


public class simulator {
    private JPanel rootpanel;
    private JTextField r2_textField;
    private JTextField mfr_textField;
    private JTextField irr_textField;
    private JTextField mem_textField;
    private JTextField mbr_textField;
    private JTextField textField2;
    private JButton clearButton;
    private JButton haltButton;
    private JButton IPLButton;
    private JButton singleStepButton;
    private JButton runButton;
    private JTextField x3_textField;
    private JTextField iar_textField;
    private JTextField r1_textField;
    private JTextField r3_textField;
    private JTextField x2_textField;
    private JTextField x1_textField;
    private JRadioButton r0RadioButton;
    private JRadioButton r1RadioButton;
    private JRadioButton r2RadioButton;
    private JRadioButton r3RadioButton;
    private JTextField data1_textField;
    private JButton enter_register;
    private JTextField pc_textField;
    private JTextField address_textField;
    private JTextArea resultsTextArea;
    private JTextField r0_textField;
    private JTextField ir_textField;
    private JTextField cc_textField;
    private JTextField mar_textField;
    private JTextField data2_textField;
    private JTextField data3_textField;
    private JButton enter_memory;
    private JButton enter_pc;

    private JScrollPane scrollPane;

    public static String[] memory = new String[2048];
    public static int[] r = new int[4];
    public static CacheSim cacheSim = new CacheSim();

    /**
     * check if S is binary string
     * @param s
     */
    public final static boolean isBinary(String s) {
        if (s != null && !"".equals(s.trim()))
            return s.matches("^[0-1]*$");
        else
            return false;
    }
    /**
     * Decode instruction,calculate Effective address,execution
     * @param instruction
     */
    public  void cpu(String instruction){
        if (instruction.length() != 16) {
            machinefault_illegal_instruction();
        }
        else {
            int opcode = Integer.parseInt(instruction.substring(0, 6), 2);
            int ix = Integer.parseInt(instruction.substring(8, 10), 2);
            int r = Integer.parseInt(instruction.substring(6, 8), 2);
            int i = Integer.parseInt(instruction.substring(10, 11), 2);
            int addr = Integer.parseInt(instruction.substring(11, 16), 2);




            calculateEA(i, ix, addr, opcode);

            if (opcode == 1) {
                ldrInstr(r);
            } else if (opcode == 2) {
                strInstr(r);
            } else if (opcode == 3) {
                ldaInstr(r);
            } else if (opcode == 41) {

                ldxInstr(ix);


            } else if (opcode == 42) {
                stxInstr(ix);
            } else {
                machinefault_opcode();//invalid opcode
            }
        }

    }

    /**
     * calculate Effective address and store eff_addr to IAR
     * @param i
     * @param ix
     * @param addr
     * @param opcode
     * @return eff_addr
     */
    public int calculateEA(int i,int ix, int addr,int opcode){
        int eff_addr = 0;
        if(opcode==41||opcode==42){
                if (i == 1) {
                    if(memory[addr]==""){
                        machinefault_empty();
                    }
                    else {
                        eff_addr = Integer.parseInt(memory[addr], 2);//
                    }
                }
                else if (i == 0) {
                    eff_addr = addr;
                }
        }
        else {
            if (i == 1) {
                if (ix == 0) {
                    if(memory[addr]==""){
                        machinefault_empty();
                    }
                    else {
                        eff_addr = Integer.parseInt(memory[addr], 2);//
                    }
                }
                if (ix == 1) {
                    if(x1_textField.getText()==""){
                        machinefault_empty();
                    } else {
                        addr = Integer.parseInt(x1_textField.getText(), 2) + addr;
                        if (addr > 2047) {
                            machinefault_address_beyond();
                        } else if (addr < 6) {
                            machinefault_reserved_locations();
                        } else if(memory[addr]==""){
                            machinefault_empty();
                        }
                        else {
                            eff_addr = Integer.parseInt(memory[addr], 2);
                        }
                    }
                }
                if (ix == 2) {
                    if(x2_textField.getText()==""){
                        machinefault_empty();
                    } else {
                        addr = Integer.parseInt(x2_textField.getText(), 2) + addr;
                        if (addr > 2047) {
                            machinefault_address_beyond();
                        } else if (addr < 6) {
                            machinefault_reserved_locations();
                        } else if(memory[addr]==""){
                            machinefault_empty();
                        } else {
                            eff_addr = Integer.parseInt(memory[addr], 2);
                        }
                    }
                }
                if (ix == 3) {
                    if(x3_textField.getText()==""){
                        machinefault_empty();
                    } else {
                        addr = Integer.parseInt(x3_textField.getText(), 2) + addr;
                        if (addr > 2047) {
                            machinefault_address_beyond();
                        } else if (addr < 6) {
                            machinefault_reserved_locations();
                        } else if(memory[addr]==""){
                            machinefault_empty();
                        }else {
                            eff_addr = Integer.parseInt(memory[addr], 2);
                        }
                    }
                }
            } else if (i == 0) {


                if (ix == 0) {
                    eff_addr = addr;

                }
                if (ix == 1) {
                    if(x1_textField.getText()==""){
                        machinefault_empty();
                    } else {
                        eff_addr = addr + Integer.parseInt(x1_textField.getText(), 2);
                    }
                }
                if (ix == 2) {
                    if (x2_textField.getText() == "") {
                        machinefault_empty();
                    } else {
                        eff_addr = addr + Integer.parseInt(x2_textField.getText(), 2);
                    }
                }
                if (ix == 3) {
                    if(x3_textField.getText()==""){
                        machinefault_empty();
                    } else {
                        eff_addr = addr + Integer.parseInt(x3_textField.getText(), 2);
                    }
                }
            }
        }
        if(eff_addr>2047){
            machinefault_address_beyond();
            return 0;
        }else if(addr<6){
            machinefault_reserved_locations();
            return 0;
        }
        else {
            iar_textField.setText(Integer.toBinaryString(eff_addr));//把EA存入IAR
            return eff_addr;
        }

    }

    /**
     * fetch data by address from CPU
     * @param address
     * @return memory[decimal_address]
     */
    public String fetch(String address){
        mar_textField.setText(address);
        int decimal_address=Integer.parseInt(address,2);
        if (decimal_address>2047){
            machinefault_address_beyond();
            return "0";
        }else if(decimal_address<6){
            machinefault_reserved_locations();
            return "0";
        }else {
            mem_textField.setText(memory[decimal_address]);
            mbr_textField.setText(memory[decimal_address]);
            return memory[decimal_address];
        }

    }

    /**
     * read ROM and store every instruction to memory
     * @param fileName
     */
    public  void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                String address=(tempString.substring(17,22));
                int decimal_address=Integer.parseInt(address,2);
                String instruction=tempString.substring(0,16);
                if(line==1){
                    pc_textField.setText(address);
                }
                memory[decimal_address]=instruction;
                resultsTextArea.append("Store "+instruction+ "to memory["+decimal_address+"]"+"\r\n");
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
    public simulator() {
        /**
         * build button so that radiobutton can be selected only one at the same time
         */
            ButtonGroup group = new ButtonGroup();
            group.add(this.r0RadioButton);
            group.add(this.r1RadioButton);
            group.add(this.r2RadioButton);
            group.add(this.r3RadioButton);

        /**
         * clear all the register
         */
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                r0_textField.setText("");
                r1_textField.setText("");
                r2_textField.setText("");
                r3_textField.setText("");
                x1_textField.setText("");
                x2_textField.setText("");
                x3_textField.setText("");
                pc_textField.setText("");
                ir_textField.setText("");
                mar_textField.setText("");
                mbr_textField.setText("");
                mfr_textField.setText("");
                irr_textField.setText("");
                iar_textField.setText("");
                data1_textField.setText("");
                data2_textField.setText("");
                data3_textField.setText("");
                address_textField.setText("");
                cc_textField.setText("");

            }
        });
        /**
         * the listener of the button of entering data to register
         */
        enter_register.addActionListener(new ActionListener() {//machine fault:can only be binary number
            @Override
            public void actionPerformed(ActionEvent e) {
                java.lang.String input_data=data1_textField.getText();
                if(isBinary(input_data)&&input_data.length()<=16){
                    if(r0RadioButton.isSelected()){
                        r0_textField.setText(input_data);
                        resultsTextArea.append("Enter data " + input_data+" into R0"+"\r\n");//display
                    }
                    else if(r1RadioButton.isSelected()){
                        r1_textField.setText(input_data);
                        resultsTextArea.append("Enter data " + input_data+" into R1"+"\r\n");//display
                    }
                    else if(r2RadioButton.isSelected()){
                        r2_textField.setText(input_data);
                        resultsTextArea.append("Enter data " + input_data+" into R2"+"\r\n");//display
                    }
                    else if(r3RadioButton.isSelected()){
                        r3_textField.setText(input_data);
                        resultsTextArea.append("Enter data " + input_data+" into R3"+"\r\n");//display
                    }
                }
                else if(!isBinary(input_data)){
                    resultsTextArea.append("Error, your input is not binary number!"+"\r\n");
                }
                else{
                    resultsTextArea.append("Error, the length of your input is not legal!"+"\r\n");
                }
            }
        });
        /**
         * the listener of the button of entering data to memory
         */
        enter_memory.addActionListener(new ActionListener() {//machine fault:can only be binary number
            @Override
            public void actionPerformed(ActionEvent e) {
                java.lang.String input_data=data2_textField.getText();
                java.lang.String input_address=address_textField.getText();
                if(input_address==""){
                    resultsTextArea.append("Error, your input is empty!"+"\r\n");
                }
                else {
                    if (!input_address.isEmpty()) {
                        int decimal_address = Integer.parseInt(input_address, 2);
                        if (isBinary(input_data) && isBinary(input_address) && decimal_address <= 2047 && decimal_address >= 6) {//合法
                            resultsTextArea.append("Enter data " + input_data + " into Memory[" + decimal_address + "]" + "\r\n");//display
                            memory[decimal_address] = input_data;

                        } else if (!isBinary(data2_textField.getText()) || !isBinary(address_textField.getText())) {//不是数字
                            resultsTextArea.append("Error, your input is not binary number!" + "\r\n");
                        } else {//长度不合法
                            resultsTextArea.append("Error, the value of the address of you input is not legal!" + "\r\n");

                        }
                    }
                    else{
                        resultsTextArea.append("Error, your input is empty!"+"\r\n");
                    }
                }


            }
        });
        /**
         * the listener of the button of entering data to PC
         */
        enter_pc.addActionListener(new ActionListener() {//machine fault:can only be binary number
            @Override
            public void actionPerformed(ActionEvent e) {
                java.lang.String input_data=data3_textField.getText();
                if(isBinary(input_data)&&Integer.parseInt(input_data,2)<=2047&&Integer.parseInt(input_data,2)>=7) {
                    pc_textField.setText(input_data);
                    resultsTextArea.append("Enter data " +input_data+"into PC"+"\r\n");
                }
                else if(!isBinary(input_data)){
                    resultsTextArea.append("Error, your input is not binary number!"+"\r\n");
                }
                else{
                    resultsTextArea.append("Error, the value of your input is more than 2047 or less than 7!"+"\r\n");

                }

            }
        });
        /**
         * the listener of the button of single step
         */
        singleStepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mar_textField.setText(pc_textField.getText());

                String instruction=memory[Integer.parseInt(mar_textField.getText(),2)];
                mem_textField.setText(instruction);
                mbr_textField.setText(instruction);
                cpu(instruction);


            }
        });
        /**
         * the listener of the memory, if input address to mar, and hit return in keyboard,
         * mem will show the content of that address
         */
        mar_textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                mem_textField.setText(memory[Integer.parseInt(mar_textField.getText(),2)]);//在mar里面随便输入数据，按回车，可以在mem中看到对应地址的内存
            }
        });
        /**
         * IPL store the ROM to memory
         */
        IPLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readFileByLines("ROM.txt");
                x3_textField.setText("000001");
                memory[31]="000001";

            }
        });
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i=8;
                while(i<14){
                    String instruction=memory[i];
                    pc_textField.setText(Integer.toBinaryString(i));
                    mem_textField.setText(instruction);
                    mbr_textField.setText(instruction);
                    cpu(instruction);
                    i++;
                }
            }
        });
    }
   public void ldrInstr(int r){
        irr_textField.setText(fetch(iar_textField.getText()));
        String data=irr_textField.getText();
        int decimal_address=Integer.parseInt(iar_textField.getText(),2);
        if(r==0){
            r0_textField.setText(data);
            resultsTextArea.append("Load R0 from memory["+decimal_address+"]"+"\r\n");
        }else if(r==1){
            r1_textField.setText(data);
            resultsTextArea.append("Load R1 from memory["+decimal_address+"]"+"\r\n");
        }else if(r==2){
            r2_textField.setText(data);
            resultsTextArea.append("Load R2 from memory["+decimal_address+"]"+"\r\n");
        }else if(r==3){
            r3_textField.setText(data);
            resultsTextArea.append("Load R3 from memory["+decimal_address+"]"+"\r\n");
        }

   }
   public void strInstr(int r){
        int mem_address=Integer.parseInt(iar_textField.getText(),2);
        if(r==0){
       memory[mem_address]=r0_textField.getText();
            resultsTextArea.append("Store R0 to memory["+mem_address+"]"+"\r\n");
   }else if(r==1){
            memory[mem_address]=r1_textField.getText();
            resultsTextArea.append("Store R1 to memory["+mem_address+"]"+"\r\n");
    }else if(r==2){
            memory[mem_address]=r2_textField.getText();
            resultsTextArea.append("Store R2 to memory["+mem_address+"]"+"\r\n");
    }else if(r==3){
            memory[mem_address]=r3_textField.getText();
            resultsTextArea.append("Store R3 to memory["+mem_address+"]"+"\r\n");
    }

}

public void ldaInstr(int r){
        String address=iar_textField.getText();
    if(r==0){
        r0_textField.setText(address);
        resultsTextArea.append("Load R0 with "+address+"\r\n");

    }else if(r==1){
        r1_textField.setText(address);
        resultsTextArea.append("Load R1 with "+address+"\r\n");
    }else if(r==2){
        r2_textField.setText(address);
        resultsTextArea.append("Load R2 with "+address+"\r\n");
    }else if(r==3){
        r3_textField.setText(address);
        resultsTextArea.append("Load R3 with "+address+"\r\n");
    }
    }

    public void ldxInstr(int ix){
        irr_textField.setText(fetch(iar_textField.getText()));
        String data=irr_textField.getText();
        int decimal_data=Integer.parseInt(data,2);
        if(ix==0){
            //machine fault  ix不应该是0

        }else if(ix==1){
            x1_textField.setText(data);
            resultsTextArea.append("Load X1 from memory["+decimal_data+"]"+"\r\n");
        }else if(ix==2){
            x2_textField.setText(data);
            resultsTextArea.append("Load X2 from memory["+decimal_data+"]"+"\r\n");
        }else if(ix==3){
            x3_textField.setText(data);
            resultsTextArea.append("Load X3 from memory["+decimal_data+"]"+"\r\n");
        }

    }
    public void stxInstr(int ix){
        int mem_address=Integer.parseInt(iar_textField.getText(),2);
        if(ix==0){
            //machine fault  ix不应该是0
        }else if(ix==1){
            memory[mem_address]=x1_textField.getText();
            resultsTextArea.append("Store X1 to memory["+mem_address+"]"+"\r\n");
        }else if(ix==2){
            memory[mem_address]=x2_textField.getText();
            resultsTextArea.append("Store X2 to memory["+mem_address+"]"+"\r\n");
        }else if(ix==3){
            memory[mem_address]=x3_textField.getText();
            resultsTextArea.append("Store X3 to memory["+mem_address+"]"+"\r\n");
        }

    }
    public void machinefault_opcode(){
        resultsTextArea.append("Machine fault: Illegal Operation Code\r\n");
        resultsTextArea.append("MFR set to 0100\r\n");
        mfr_textField.setText("0100");
        resultsTextArea.append("Store PC for Machine Fault to Memory[4]"+"\r\n");
        memory[4]=pc_textField.getText();

    }
    public void machinefault_address_beyond(){
        resultsTextArea.append("Machine fault: Illegal Memory Address beyond 2048 \r\n");
        resultsTextArea.append("MFR set to 1000\r\n");
        mfr_textField.setText("1000");
        resultsTextArea.append("Store PC for Machine Fault to Memory[4]"+"\r\n");
        memory[4]=pc_textField.getText();

    }
    public void machinefault_reserved_locations(){
        resultsTextArea.append("Machine fault: Illegal Memory Address to Reserved Locations \r\n");
        resultsTextArea.append("MFR set to 0001\r\n");
        mfr_textField.setText("0001");
        resultsTextArea.append("Store PC for Machine Fault to Memory[4]"+"\r\n");
        memory[4]=pc_textField.getText();
    }

    public void machinefault_empty(){
        resultsTextArea.append("Machine fault: can't find the data you load or store \r\n");
        //resultsTextArea.append("MFR set to 0001\r\n");
        //mfr_textField.setText("0001");
       // resultsTextArea.append("Store PC for Machine Fault to Memory[4]"+"\r\n");
       // memory[4]=pc_textField.getText();
    }
    public void machinefault_illegal_instruction(){
        resultsTextArea.append("Machine fault: Instruction is illegal \r\n");
    }

    /**
     * Return 16 bits binary string which value is a
     * @param a
     */
    public String toBinary_lenth16(int a){
        if(a>=0){
            DecimalFormat g1=new DecimalFormat("0000000000000000");
            return g1.format(Integer.valueOf(a));
        }
        else{
            int minus_a =-a;
            String true_form=Integer.toBinaryString(minus_a);
            DecimalFormat g1=new DecimalFormat("0000000000000000");
            String str=g1.format(Integer.valueOf(true_form));
            StringBuilder strBuilder = new StringBuilder(str);
            for(int i=0;i<16;i++) {
                if(str.charAt(i)=='0') {
                    strBuilder.setCharAt(i, '1');
                }
                else{
                    strBuilder.setCharAt(i, '0');
                }
            }
            String complement=strBuilder.toString();
            int value_conmplement=Integer.parseInt(complement,2);
            return Integer.toBinaryString(value_conmplement+1);
        }
    }
    /**
     * Return a's value, no matter a is negative or positive
     * @param a
     */
    public int parseInt_for_all(String a){
        //must 16 bits
        if(a.charAt(15)==0){
            return Integer.parseInt(a,2);
        }
        else{
            int temp=Integer.parseInt(a,2);
            a=Integer.toBinaryString(temp-1);
            StringBuilder strBuilder1 = new StringBuilder(a);
            for(int i=0;i<16;i++) {
                if(a.charAt(i)=='0') {
                    strBuilder1.setCharAt(i, '1');
                }
                else{
                    strBuilder1.setCharAt(i, '0');
                }
            }
            return -Integer.parseInt(strBuilder1.toString(),2);
        }
    }


    /**
     * String=String+1
     */
    public String binary_plus_one(String binary_string){
        if (binary_string == ""||binary_string=="0"||binary_string=="0000000000000000") {
            return "0000000000000001";
        }
        int decimal_data=parseInt_for_all(binary_string);
        decimal_data=decimal_data+1;

        return toBinary_lenth16(decimal_data);
    }
    /**
     * String=String-1
     */
    public String binary_minus_one(String binary_string){
        if (binary_string == ""||binary_string=="0"||binary_string=="0000000000000000") {
            return "1111111111111111";
        }
        int decimal_data=parseInt_for_all(binary_string);
        decimal_data=decimal_data-1;
        return toBinary_lenth16(decimal_data);
    }
    /**
     * return a+b
     */
    public String binary_add(String a, String b) {
        int int_a=parseInt_for_all(a);
        int int_b=parseInt_for_all(b);
        int c=int_a+int_b;
        return toBinary_lenth16(c);

    }
    /**
     * return a+b
     */
    public String binary_minus(String a, String b){
        int int_a=parseInt_for_all(a);
        int int_b=parseInt_for_all(b);

        int c=int_a-int_b;
        return toBinary_lenth16(c);
    }

    public void airInstr(String instruction){
        int r = Integer.parseInt(instruction.substring(6, 8), 2);
        String immed  = instruction.substring(11, 16);
        if(r==0){
            r0_textField.setText(binary_add(immed,r0_textField.getText()));
            resultsTextArea.append("AIR:  R0= R0+ Memory[EA]"+"\r\n");
        }
        else if(r==1){
            r1_textField.setText(binary_add(immed,r1_textField.getText()));
            resultsTextArea.append("AIR:  R1= R1+ Memory[EA]"+"\r\n");

        }
        else if(r==2){
            r2_textField.setText(binary_add(immed,r2_textField.getText()));
            resultsTextArea.append("AIR:  R2= R2+ Memory[EA]"+"\r\n");

        }
        else if(r==3){
            r3_textField.setText(binary_add(immed,r3_textField.getText()));
            resultsTextArea.append("AIR:  R3= R3+ Memory[EA]"+"\r\n");

        }
    }


    //===================================================================================================================

    public void mlt(String instruction) {   // instruction could be: MLT 0 2
        String[] instr = instruction.split(" "); // {"MLT", "0", "2"}
        int rx = Integer.valueOf(instr[1]);  //0
        int ry = Integer.valueOf(instr[2]);  //2

        int rx_bin = Integer.parseInt(instr[1], 2);
        int ry_bin = Integer.parseInt(instr[2], 2);

        String x_str = cacheSim.access_memory(rx, memory);
        String y_str = cacheSim.access_memory(ry, memory);
        int x = Integer.valueOf(x_str);
        int y = Integer.valueOf(y_str);
        //int x = r[rx];//whats in register rx
        //int y = r[ry];
        int res = x * y;
        String result = Integer.toBinaryString(res);
        while (result.length() < 16) {
            result = '0' + result;
        }
        cacheSim.write_to_memory(String.valueOf(result.substring(0, 8)), rx, memory);
        cacheSim.write_to_memory(String.valueOf(result.substring(8, 16)), rx+1, memory);
        resultsTextArea.append("\nThe result of MLT is in registers");
        if (rx == 0) {
            r0_textField.setText(String.valueOf(result.substring(0, 8)));
            r1_textField.setText(String.valueOf(result.substring(8, 16)));
        } if(rx == 1) {
            r1_textField.setText(String.valueOf(result.substring(0, 8)));
            r2_textField.setText(String.valueOf(result.substring(8, 16)));
        } if(rx == 2) {
            r2_textField.setText(String.valueOf(result.substring(0, 8)));
            r3_textField.setText(String.valueOf(result.substring(8, 16)));
        }
    }


    private void dvd(String instruction) {
        int rx = Integer.parseInt(instruction.substring(6, 8),2);
        int ry = Integer.parseInt(instruction.substring(8,10),2);
        String x_str = cacheSim.access_memory(rx, memory);
        String y_str = cacheSim.access_memory(ry, memory);
        int x = Integer.valueOf(x_str);
        int y = Integer.valueOf(y_str);
        String result1 = "", result2 = "";
        if (y == 0) {
            cacheSim.write_to_memory(String.valueOf(1), 3, memory);
            //cc[3]=1;
            cc_textField.setText("DVD ZERO");
        } else {
            int res1 = x/y;
            int res2 = x%y;
            result1 = Integer.toBinaryString(res1);
            while (result1.length() < 16) {
                result1 = '0' + result1;
            }
            result2 = Integer.toBinaryString(res2);
            while (result2.length() < 16) {
                result2 = '0' + result2;
            }
            cacheSim.write_to_memory(result1, rx, memory);
            cacheSim.write_to_memory(result2, rx+1, memory);
        }
        resultsTextArea.append("\nThe result of DVD is in registers");
        if(rx==0) {
            r0_textField.setText(result1);
            r1_textField.setText(result2);
        } if(rx==1) {
            r1_textField.setText(result1);
            r2_textField.setText(result2);
        } if(rx==2) {
            r2_textField.setText(result1);
            r3_textField.setText(result2);
        }
    }

    private void trr(String instruction) {
        String[] instr = instruction.split(" "); // {"TRR", "0", "2"}
        int rx = Integer.valueOf(instr[1]);
        int ry = Integer.valueOf(instr[2]);
        String x_str = cacheSim.access_memory(rx, memory);
        String y_str = cacheSim.access_memory(ry, memory);
        int x = Integer.valueOf(x_str);
        int y = Integer.valueOf(y_str);
        resultsTextArea.append("\nTesting equality between R" + rx + " and R" + ry);
        if (x == y) {
            //cc[3]=1; ????????
            cacheSim.write_to_memory(Integer.toString(1), 3, memory);
            cc_textField.setText("EQUAL");
            resultsTextArea.append("\nRegisters are equal");
        } else {
            //cc[3]=0; ?????????
            cacheSim.write_to_memory(Integer.toString(0), 3, memory);
            cc_textField.setText("NOT EQUAL");
            resultsTextArea.append("\nRegisters are not equal");
        }
    }

    private void and(String instruction) {
        int rx = Integer.parseInt(instruction.substring(6, 8),2);
        int ry = Integer.parseInt(instruction.substring(8,10),2);
        resultsTextArea.append("\nAND operation between R" + rx + " and R" + ry);
        resultsTextArea.append("\nResult is in R" + rx);
        String x_str = cacheSim.access_memory(rx, memory);
        String y_str = cacheSim.access_memory(ry, memory);
        int x = Integer.valueOf(x_str);
        int y = Integer.valueOf(y_str);
        cacheSim.write_to_memory(Integer.toString(x & y), rx, memory);
        if (rx == 0) {
            r0_textField.setText(Integer.toString(x & y));
        } if(rx==1) {
            r1_textField.setText(Integer.toString(x & y));
        } if(rx==2) {
            r2_textField.setText(Integer.toString(x & y));
        } if(rx==3) {
            r3_textField.setText(Integer.toString(x & y));
        }
    }

    private void orr(String instruction) {
        int rx = Integer.parseInt(instruction.substring(6, 8),2);
        int ry = Integer.parseInt(instruction.substring(8,10),2);
        resultsTextArea.append("\nOR operation between R" + rx + " and R" + ry);
        resultsTextArea.append("\nResult is in R"+rx);
        String x_str = cacheSim.access_memory(rx, memory);
        String y_str = cacheSim.access_memory(ry, memory);
        int x = Integer.valueOf(x_str);
        int y = Integer.valueOf(y_str);
        cacheSim.write_to_memory(Integer.toString(x | y), rx, memory);
        if (rx == 0) {
            r0_textField.setText(Integer.toString(x | y));
        } if (rx == 1) {
            r1_textField.setText(Integer.toString(x | y));
        } if (rx == 2) {
            r2_textField.setText(Integer.toString(x | y));
        } if (rx == 3) {
            r3_textField.setText(Integer.toString(x | y));
        }
    }

    private void not(String instruction) {
        int rx = Integer.parseInt(instruction.substring(6, 8),2);
        resultsTextArea.append("\nNOT operation of R" + rx);
        resultsTextArea.append("\nResult is in R" + rx);
        String x_str = cacheSim.access_memory(rx, memory);
        int x = Integer.valueOf(x_str);
        cacheSim.write_to_memory(Integer.toString(~x), rx, memory);
        if (rx == 0) {
            r0_textField.setText(Integer.toString(~x));
        } if (rx == 1) {
            r1_textField.setText(Integer.toString(~x));
        } if (rx == 2) {
            r2_textField.setText(Integer.toString(~x));
        } if (rx == 3) {
            r3_textField.setText(Integer.toString(~x));
        }
    }

    //===================================================================================================================


    public static void main(String[] args) {
        JFrame frame = new JFrame("simulator");
        frame.setContentPane(new simulator().rootpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }
}

