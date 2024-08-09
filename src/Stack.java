public class Stack {
    private String[] data;
    private int top;

    public Stack(int size) {
        data = new String[size];
        top = 0;
    }

    //pre: takes in nothing
    //post: returns a string array
    //returns the array in the stack
    public String[] getStack() {
        return data;
    }

    //pre: takes in a string
    //post: returns nothing
    //puts something in the stack
    public void push(String s) {
        data[top] = s;
        if (top < 4) {
            top++;
        }
    }

    //pre: takes in nothing
    //post: returns a string
    //returns the string at the top of the stack
    public String pop() {
        if (top > 0) {
            top--;
        }
        return data[top+1];
    }

    //pre: takes in nothing
    //post: returns a string
    //returns the info in the stack
    public String toString() {
        String str = "";
        for (int i = 0; i < top; i++) {
            str += data[i] + ", ";
        }
        return str;
    }
}