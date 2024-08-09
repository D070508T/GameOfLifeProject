import java.util.Random;
import java.util.Scanner;

public class Spider extends Monster{
    public Spider (Random random) {
        super();
        name = "Spider";
        salary = random.nextInt(53, 70) * 1000;
        chanceOfFinding = 60;
        chanceOfEscaping = 25;
        requiresDiploma = "REQUIRES DIPLOMA";
    }

    //pre: takes in string array, scanner object, string array, random object, and a boolean
    //post: returns nothing
    //activates player's attribute: gain $800
    public void attribute(String[] cards, Scanner scanner, String[] bugs, Random random, boolean choose) {
        System.out.println("Gained $800!");
        currency += 800;
    }

    //pre: takes in nothing
    //post: returns a string
    //displays the attribute of the monster
    public String showAttribute() {
        return "GAIN $800";
    }
}