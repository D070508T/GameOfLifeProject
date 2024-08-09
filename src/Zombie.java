import java.util.Random;
import java.util.Scanner;

public class Zombie extends Monster{
    public Zombie (Random random) {
        super();
        name = "Zombie";
        salary = random.nextInt(24, 35) * 1000;
        chanceOfFinding = 50;
        chanceOfEscaping = 30;
        requiresDiploma = "DIPLOMA NOT REQUIRED";
    }

    //pre: takes in string array, scanner object, string array, random object, and a boolean
    //post: returns nothing
    //activates player's attribute: gain $800
    public void attribute(String[] cards, Scanner scanner, String[] bugs, Random random, boolean choose) {
        System.out.println("Found a new bug!");
        friends.push(bugs[random.nextInt(0, 5)]);
    }

    //pre: takes in nothing
    //post: returns a string
    //displays the attribute of the monster
    public String showAttribute() {
        return "FIND A NEW BUG FRIEND!";
    }
}