import java.util.Random;
import java.util.Scanner;

public abstract class Monster extends Card{
    protected LinkedList capturedHumans;
    protected int chanceOfFinding;
    protected int chanceOfEscaping;
    protected int currency;
    protected Room room;
    protected Stack friends;
    protected int salary;
    protected String name;
    protected String diploma;
    protected String requiresDiploma;

    public Monster() {
        super("Monster");
        capturedHumans = new LinkedList();
        friends = new Stack(5);
        diploma = "NO DIPLOMA";
    }

    //pre: takes in a string, a string array, a Random object, Scanner object, and a string array with the cards
    //post: returns nothing
    //This method goes through the process of choosing an action card for the player
    public void actionCard(String s, String[] bugs, Random random, Scanner scanner, String[] cards) {
        if (s.startsWith("You found a baby!")) {
            currency += 500;
        } else if (s.startsWith("You woke")) {
            currency -= 500;
            if (currency < 0) {
                currency = 0;
            }
        } else if (s.startsWith("You found an insect")) {
            String bug = bugs[random.nextInt(0, 5)];
            addFriend(bug);
            System.out.println("It's a " + bug);
        } else if (s.startsWith("You lost an insect")) {
            removeFriend();
        } else {
            attribute(cards, scanner, bugs, random, true);
        }
    }

    //pre: takes in nothing
    //post: returns int
    //returns salary
    public int getSalary() {
        return salary;
    }

    //pre: takes in nothing
    //post: returns String
    //returns whether the monster requires a diploma
    public String getRequiresDiploma() {
        return requiresDiploma;
    }

    //pre: takes in nothing
    //post: returns String
    //returns whether the player has a diploma
    public String getDiploma() {
        return diploma;
    }

    //pre: takes in a string
    //post: returns nothing
    //sets whether the player has a diploma or not
    public void setDiploma(String s) {
        diploma = s;
    }

    //pre: takes in nothing
    //post: returns a Stack
    //returns the stack of friends
    public Stack getFriends() {
        return friends;
    }

    //pre: takes in a Stack
    //post: returns nothing
    //sets the stack of friends
    public void setFriends(Stack f) {
        friends = f;
    }

    //pre: takes in nothing
    //post: returns a String
    //returns the name/type of the monster
    public String getName() {
        return name;
    }

    //pre: takes in a string
    //post: returns nothing
    //adds a human to the linked list capturedHumans and increases the currency
    public void captureHuman(String name) {
        currency += salary;
        capturedHumans.addToFront(name);
    }

    //pre: takes in nothing
    //post: returns nothing
    //removes a human from linkedlist capturedHumans
    public void loseHuman() {
        if (capturedHumans.getHead() != null) {
            currency -= salary;
            if (currency < 0) {
                currency = 0;
            }
            capturedHumans.removeFront();
        } else {
            System.out.println("NO HUMANS ESCAPED (Close call)");
        }
    }

    //pre: takes in a string
    //post: returns nothing
    //adds a bug friend to the stack
    public void addFriend(String s) {
        friends.push(s);
    }

    //pre: takes in nothing
    //post: returns nothing
    //removes a bug friend from stack
    public void removeFriend() {
        friends.pop();
    }

    //pre: takes in nothing
    //post: returns int
    //returns the currency amount
    public int getCurrency() {
        return currency;
    }

    //pre: takes in an int
    //post: returns nothing
    //sets the currency to something
    public void setCurrency(int c) {
        currency = c;
    }

    //pre: takes in nothing
    //post: returns a Room
    //returns the player's room
    public Room getRoom() {
        return room;
    }

    //pre: takes in a Room
    //post: returns nothing
    //sets the player's room
    public void setRoom(Room _room) {
        if (_room != null) {
            currency -= _room.getPurchasePrice();
            System.out.println(_room.getName() + " purchased for $" + _room.getPurchasePrice());
        }
        room = _room;
    }

    //pre: takes in nothing
    //post: returns a LinkedList
    //returns the captured humans
    public LinkedList getCapturedHumans() {
        return capturedHumans;
    }

    //pre: takes in a LinkedList
    //post: returns nothing
    //sets the capturedHumans
    public void setCapturedHumans(LinkedList l) {
        capturedHumans = l;
    }

    //pre: takes in nothing
    //post: returns int
    //returns chance of finding a human
    public int getChanceOfFinding() {
        return chanceOfFinding;
    }

    //pre: takes in nothing
    //post: returns int
    //returns chance of human escaping
    public int getChanceOfEscaping() {
        return chanceOfEscaping;
    }

    //pre: takes in nothing
    //post: returns a String
    //returns the information of the player
    public String toString() {
        return name + "  [" + capturedHumans + "] " + chanceOfFinding + " " + chanceOfEscaping + " " + room + " " + currency + " " + friends + " " + salary;
    }

    //pre: takes in nothing
    //post: returns a String
    //returns the information of the player in an organized way
    public String display() {
        return name + " (" + requiresDiploma + ")\n" +
                "Salary: " + salary + "\n" +
                "Chance of finding a human: " + chanceOfFinding + "%\n" +
                "Chance of human escaping: " + chanceOfEscaping + "%\n" +
                "Attribute card: " + showAttribute();
    }

    //pre: takes in nothing
    //post: returns String
    //returns player info (more info than display())
    public String showPlayerInfo() {
        String r = "";
        if (room != null) {
            r = room.display();
        }
        return display() + "\n" +
                "Captured humans: " + capturedHumans + "\n" +
                "Room: \n" + r + "\n" +
                "Bugs: " + friends;
    }

    //pre: takes in cards array, scanner object, string array, random object, and a boolean
    //post: returns nothing
    //activates the player's attribute
    public abstract void attribute(String[] cards, Scanner scanner, String[] bugs, Random random, boolean choose);

    //pre: takes in nothing
    //post: returns a String
    //displays the player attribute
    public abstract String showAttribute();
}