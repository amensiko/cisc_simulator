public class Queue {
    private int[] elements;
    public static final int DEFAULT_CAPACITY =16;//默认长度16
    private int size = 0;

    public Queue() {
        elements = new int[DEFAULT_CAPACITY];
    }

    public void enqueue(int v) {

        elements[size++] = v;

    }
    public int get_buttom(){
        if(size==0){
            return 0;
        }
        return elements[size];
    }

    public int dequeue() {// 先进先出
        if (empty()) {
            throw new RuntimeException("异常");
        }

        int x = elements[0];// 先把第一个元素保存出来

        // 左移一位
        // int[] temp = new int[elements.length];
        // System.arraycopy(elements,1, temp, 0, elements.length-1);
        // elements = temp;

        // 左移一位
        for (int i = 0; i < elements.length - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[elements.length - 1] = 0;// 外面一般访问不了elements 后面的元素可以不用归零,但是归零了感觉舒服点
        size--;
        return x;
    }

    public boolean empty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }
}
