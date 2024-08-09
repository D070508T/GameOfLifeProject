import java.util.Random;
import java.util.Scanner;

public class Ghost extends Monster{
    public Ghost (Random random) {
        super();
        name = "Ghost";
        salary = random.nextInt(35, 45) * 1000;
        chanceOfFinding = 30;
        chanceOfEscaping = 10;
        requiresDiploma = "DIPLOMA NOT REQUIRED";
    }

    //pre: takes in string array, scanner object, string array, random object, and boolean
    //post: returns nothing
    //Activates attribute: player gets to choose an action card
    public void attribute(String[] cards, Scanner scanner, String[] bugs, Random random, boolean choose) {
        System.out.println("Choose an action card!");
        if (choose) {
            for (int i = 0; i < cards.length; i++) {
                System.out.println("CARD " + i + ": " + cards[i]);
            }
            boolean valid = false;
            String input = "";
            while (!valid) {
                if (!input.equals("")) {
                    System.out.println("INVALID");
                }
                System.out.print("Choose >>> ");
                input = scanner.nextLine();
                if ((int) input.charAt(0) >= 48 && (int) input.charAt(0) <= 52) {
                    valid = true;
                    actionCard(cards[(int) input.charAt(0) - 48], bugs, random, scanner, cards);
                } else {
                    valid = false;
                }
            }
        } else {
            actionCard(cards[random.nextInt(0, 5)], bugs, random, scanner, cards);
        }
    }

    //pre: takes in nothing
    //post: returns a string
    //displays the attribute of the monster
    public String showAttribute() {
        return "CHOOSE AN ACTION CARD";
    }
}