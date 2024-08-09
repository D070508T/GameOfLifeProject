public class Queue {
    private Card[] data;
    private int rear;

    public Queue() {
        data = new Card[20];
        rear = 0;
    }

    //pre: takes in a Card
    //post: returns nothing
    //Adds a Card to the queue
    public void enqueue (Card n) {
        data[rear] = n;
        rear++;
    }

    //pre: takes in nothing
    //post: returns a card
    //Returns the card at the front of the queue
    public Card front() {
        return data[0];
    }

    //pre: takes in nothing
    //post: returns a Card
    //removes and returns the Card at the top of the queue
    public Card dequeue() {
        Card temp = data[0];
        for (int i = 1; i < rear; i++) {
            data[i-1] = data[i];
        }
        rear--;
        return temp;
    }

    //pre: takes in nothing
    //post: returns a string
    //Returns the information in the queue
    public String toString() {
        String info = "QUEUE: ";
        for (int i = 0; i < rear; i++) {
            info += data[i] + ", ";
        }
        return info;
    }
}