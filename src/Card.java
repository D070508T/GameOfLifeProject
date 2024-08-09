public class Card {
    private String mainContent;

    public Card(String s) {
        mainContent = s;
    }

    //pre: takes in nothing
    //post: returns a string
    //returns the content of the card so it can be displayed
    public String display() {
        return mainContent;
    }

    //pre: takes in nothing
    //post: returns a string
    //returns the content of the string
    public String toString() {
        return mainContent;
    }
}