import java.util.Random;
import java.util.Scanner;

public class Skeleton extends Monster{
    public Skeleton (Random random) {
        super();
        name = "Skeleton";
        salary = random.nextInt(50, 67) * 1000;
        chanceOfFinding = 40;
        chanceOfEscaping = 10;
        requiresDiploma = "REQUIRES DIPLOMA";
    }

    //pre: takes in string array, scanner object, string array, random object, and a boolean
    //post: returns nothing
    //activates player's attribute: gain $1000
    public void attribute(String[] cards, Scanner scanner, String[] bugs, Random random, boolean choose) {
        System.out.println("Gained $1000!");
        currency += 1000;
    }

    //pre: takes in nothing
    //post: returns a string
    //displays the attribute of the monster
    public String showAttribute() {
        return "GAIN $1,000";
    }
}