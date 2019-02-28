
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