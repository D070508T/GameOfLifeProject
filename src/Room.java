public class Room extends Card{
    private String name;
    private int purchasePrice;
    private int num;

    public Room(String _name, int _purchasePrice, int _num) {
        super("Room");
        name = _name;
        purchasePrice = _purchasePrice;
        num = _num;
    }

    //pre: takes in nothing
    //post: returns a String
    //Returns the name of the room
    public String getName() {
        return name;
    }

    //pre: takes in nothing
    //post: returns an int
    //Returns the purchase price of the room
    public int getPurchasePrice() {
        return purchasePrice;
    }

    //pre: takes in nothing
    //post: returns an int
    //Returns the boundry of the price
    public int getNum() {
        return num;
    }

    //pre: takes in nothing
    //post: returns a string
    //returns the info of the room in an organized way
    public String display() {
        return
                name + "\n" +
                        "Cost: $" + purchasePrice + "\n" +
                        "High sell cost: $" + (purchasePrice + num) + "\n" +
                        "Low sell cost: $" + (purchasePrice - num);
    }

    //pre: takes in nothing
    //post: returns a string
    //returns the info of the room
    public String toString() {
        return name + ",purchase price: " + purchasePrice + ",boundry: " + num;
    }
}