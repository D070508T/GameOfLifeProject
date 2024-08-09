public class LinkedList {
    private Node head;

    public LinkedList() {
        head = null;
    }

    public Node getHead() {
        return head;
    }

    public void addToFront(String n) {
        Node node = new Node(n);
        node.setNext(head);
        head = node;
    }

    public void removeFront() {
        Node temp = head.getNext();
        head.setNext(null);
        head = temp;
    }

    public String toString() {
        String info = "";
        for (Node node = head; node != null; node = node.getNext()) {
            info += node.getData() + ", ";
        }

        return info;
    }

    public int size() {
        int i = 0;
        for (Node node = head; node != null; node = node.getNext()) {
            i++;
        }

        return i;
    }

    public void makeEmpty() {
        head = null;
    }

    private class Node {
        private String data;
        private Node next;
        private Node (String a) {
            data = a;
            next = null;
        }

        private String getData () {
            return data;
        }

        private Node getNext () {
            return next;
        }

        private void setNext (Node n) {
            next = n;
        }

        private void setData (String str) {
            data = str;
        }

        public String toString() {
            return data;
        }
    }
}