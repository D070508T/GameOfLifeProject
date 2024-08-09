import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        File file = new File("File.txt");
        FileHandler fileHandler = new FileHandler(file);

        Monster[] players = new Monster[3];

        String[][] logins = fileHandler.logins();

        Queue[] cards = new Queue[]{new Queue(), new Queue(), new Queue()}; // room, monster, action
        shuffleCards(cards[0], cards[1], cards[2]);

        System.out.println("'WAKE UP!', says the evil spirit" +
                "\n'When I found you in the forest you were completely knocked out.'" +
                "\n'You are mine now. You work for me. You will compete with two other people to serve me and find human spirits for me'" +
                "\n'I'll let you choose how you want to do it.'" +
                "\n'If you want I can offer you training. This will be greatly beneficial for you.'" +
                "\n'I will reward you after every human spirit found. With a diploma, I will reward you more.'" +
                "\n'With the money I pay you, you can buy rooms to stay in. You might want some place to sleep. Unless you want bugs all over you'" +
                "\n'Speaking of bugs, there's lots of them here. Protect them if you can. They're endangered'"
        );

        /*
        This loop runs forever until the player types "QUIT" in the login screen
        which will trigger the System.exit(0); line
        */
        while (true) {
            String user = login(scanner, logins, fileHandler, players, cards);
            System.out.println("Login successful");
            players = fileHandler.loadGame(user);
            System.out.println("Data loaded successfully");
            if (game(scanner, players, cards, fileHandler, user, new Random())) {
                deleteFromFile(fileHandler, user);
            }
            logins = fileHandler.logins();
        }
    }

    //pre: takes in the FileHandler object, and the username to tell which save log we're on
    //post: returns nothing
    //This method removes a game from the file after someone wins.
    public static void deleteFromFile(FileHandler fileHandler, String user) throws IOException {
        String[] data = fileHandler.makeData();
        int userIndex = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i].endsWith(user)) {
                userIndex = i;
                i = data.length;
            }
        }

        Stack newData = new Stack(data.length - 20);

        for (int i = 0; i < data.length; i++) {
            if (i < userIndex || i > userIndex + 20) {
                newData.push(data[i]);
            }
        }

        fileHandler.rewriteFile(newData.getStack());
    }

    //pre: takes in Scanner object, array with players, array with cards, FileHandler object, a Random object, and a String, the username
    //post: returns nothing
    //This method runs the actual game until someone wins, which causes them to go back to login
    public static boolean game(Scanner scanner, Monster[] players, Queue[] cards, FileHandler fileHandler, String user, Random random) throws IOException{
        int end = -1;
        while (end == -1) {
            for (int i = 0; i < 3 && end == -1; i++) { // goes through all the players turns
                end = turn(scanner, players, cards, fileHandler, user, random, i);
                fileHandler.saveGame(user, players);
            }
        }

        if (end == 0) {
            return true;
        }

        return false;
    }

    //pre: takes in Scanner object, players array, cards array, FileHandler object, the username - as a string, a Random object, and an int which tells which player's turn it's on
    //post: returns an int corresponding to the end status of the game (if someone won)
    //This method runs the turn of the player. It checks if it's the player's turn or the AI, which causes it to either get user input or randomly generate choices
    public static int turn(Scanner scanner, Monster[] players, Queue[] cards, FileHandler fileHandler, String user, Random random, int turn) throws IOException {
        if (turn != 0) {
            int spin = spin(random, players[turn]);
            if (spin == 3) { // diploma
                if (players[turn].getCurrency() >= 22000 && players[turn].getDiploma().equals("NO DIPLOMA")) {
                    if (random.nextBoolean()) {
                        giveDiploma(players[turn]);
                        System.out.println("AI " + turn + " has been awarded a diploma!");
                    }
                }
            } else if (spin == 10) {
                System.out.println("AI " + turn + " WINS");
                return 0;
            } else if (spin == 4) { // found human
                players[turn].captureHuman(humanNames()[random.nextInt(0, 8)]);
                System.out.println("AI " + turn + " has found a human and gained $" + players[turn].getSalary());
            } else if (spin == 5) { // lost human
                System.out.println("A HUMAN IS ESCAPING! AI " + turn +  " IS LOSING $" + players[turn].getSalary());
                players[turn].loseHuman();
            } else {
                Card card = cards[spin].front();
                if (spin == 0) { //room card
                    System.out.println("AI " + turn + " HAS LANDED ON A ROOM CARD");
                    if (players[turn].getRoom() == null) {
                        if (random.nextBoolean()) {
                            players[turn].setRoom((Room) card);
                        }
                    } else {
                        if (random.nextBoolean()) {
                            Room temp = players[turn].getRoom();
                            boolean b = random.nextBoolean();
                            sellRoom(players[turn], b);
                            System.out.print("AI " + turn + " has sold " + temp.getName() + " for $");
                            if (b) {
                                System.out.println(temp.getPurchasePrice() - temp.getNum());
                            } else {
                                System.out.println(temp.getPurchasePrice() + temp.getNum());
                            }
                        }
                    }
                } else if (spin == 1) { // monster card
                    System.out.println("\nAI " + turn + " HAS LANDED ON A MONSTER CARD\n");
                    if (random.nextBoolean()) {
                        players[turn] = copyMonster((Monster) card, players[turn]);
                        System.out.println("AI " + turn + " has switched to a " + players[turn].getName());
                    }
                } else if (spin == 2) { // action card
                    System.out.println("\nAI " + turn + " HAS LANDED ON AN ACTION CARD\nIT READS: " + card.display());
                    players[turn].actionCard(cards[2].front().display(), insects(), random, scanner, actionCards());
                }
                cards[spin].enqueue(cards[spin].dequeue());
            }

        } else {
            System.out.print("\nEnter 'QUIT' to go back to login screen\n"+
                    "Enter 'GAME' to see complete game state\n"+
                    "Enter anything else to spin!\n"+
                    " >>> ");
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("QUIT")) {
                return 1;
            } else if (userInput.equalsIgnoreCase("GAME")) {
                System.out.println("GAME STATE:");
                String[] state = fileHandler.current(players);
                for (int i = 0; i < state.length; i++) { // goes through each line in the game state string array
                    System.out.println(state[i]);
                }
                displayLeaderboard(players);
            } else {
                int spin = spin(random, players[0]);
                if (spin == 3) {
                    training(scanner, players[0]);
                } else if (spin == 10) {
                    fileHandler.saveGame(user, players);
                    System.out.println("GAME OVER! YOU HAVE LEFT THE HOUSE");
                    return 0;
                } else if (spin == 4) {
                    System.out.println("YOU FOUND A HUMAN! YOU HAVE BEEN AWARDED $" + players[0].getSalary());
                    players[0].captureHuman(humanNames()[random.nextInt(0, 8)]);
                } else if (spin == 5) {
                    players[0].loseHuman();
                } else {
                    Card card = cards[spin].front();
                    if (spin == 0) {
                        System.out.println("YOU LANDED ON A ROOM CARD");
                        System.out.println(card.display());
                        if (players[0].getRoom() == null) {
                            System.out.print("\n\nYou have $" + players[0].getCurrency() + "\nWould you like to purchase this room (y/n)? >>> ");
                            if (scanner.nextLine().equalsIgnoreCase("Y")) {
                                if (players[0].getCurrency() < ((Room) card).getPurchasePrice()) {
                                    System.out.println("You do not have enough money");
                                } else {
                                    players[0].setRoom((Room) card);
                                }
                            }
                        } else {
                            System.out.print("You already own a room. Would you like to sell" + players[0].getRoom().getName() + "for either $" +
                                    (players[0].getRoom().getPurchasePrice() - players[0].getRoom().getNum()) +
                                    " or for $" +
                                    (players[0].getRoom().getPurchasePrice() + players[0].getRoom().getNum()) +
                                    "? Spin to find out. (y/n) >>> ");
                            if (scanner.nextLine().equalsIgnoreCase("Y")) {
                                sellRoom(players[0], random.nextBoolean());
                            }
                        }
                    } else if (spin == 1) {
                        System.out.println("YOU LANDED ON A MONSTER CARD");
                        System.out.println(card.display());
                        System.out.print("\nHere is your current monster:\n" + players[0].display() + "\n\nWould you like to switch to this monster (y/n) >>> ");
                        if (scanner.nextLine().equalsIgnoreCase("Y")) {
                            players[0] = copyMonster((Monster) card, players[0]);
                        }
                    } else if (spin == 2) {
                        System.out.println("YOU LANDED ON ACTION CARD");
                        System.out.println(card.display());
                        players[0].actionCard(cards[2].front().display(), insects(), random, scanner, actionCards());
                        cards[2].enqueue(cards[2].dequeue());
                    }
                    cards[spin].enqueue(cards[spin].dequeue());
                }
            }
        }
        return -1;
    }

    //pre: takes in the array of players
    //post: returns nothing
    //This method displays the leaderboard, sorted by most to the least cash
    public static void displayLeaderboard(Monster[] players) {
        Monster[] array = new Monster[3];
        for (int i = 0; i < 3; i++) {
            array[i] = players[i];
        }

        int greatestIndex = 0;
        for (int i = 2; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                if (array[j].getCurrency() < array[greatestIndex].getCurrency()) {
                    greatestIndex = j;
                }
            }
            Monster temp = array[i];
            array[i] = array[greatestIndex];
            array[greatestIndex] = temp;
        }

        System.out.println("\nLEADERBOARD:\n1st place:\n" +
                array[0].showPlayerInfo() +
                "\n\n2nd place:\n" + array[1].showPlayerInfo() +
                "\n\n3rd place:\n" + array[2].showPlayerInfo() + "\n");
    }

    //pre: takes in nothing
    //post: returns a string array
    //this method returns all the human names for when you capture a random human
    public static String[] humanNames() {
        return new String[]{
                "Naman",
                "Jatin",
                "Shahzaib",
                "Gustavo",
                "Stella",
                "Hannah",
                "Shane",
                "Kana"
        };
    }

    //pre: takes in nothing
    //post: returns a string array
    //This method returns all the possible action cards for when you land on a random one
    public static String[] actionCards() {
        return new String[]{"You found a baby! You get $500 spirit dollars",
                "You woke the evil spirit up! Lose $500",
                "You found an insect! It's an endangered species! Take good care of it!",
                "You lost an insect.",
                "ATTRIBUTE CARD"};
    }

    //pre: takes in nothing
    //post: returns a String array
    //This method returns all the insects you can randomly find throughout the game
    public static String[] insects() {
        return new String[]{
                "Salt Creek Tiger Beetle",
                "Hine's Emerald",
                "Tiger Beetle",
                "Blackburn's Spinx Moth",
                "Formica Rufibarbis"
        };
    }

    //pre: takes in two monster objects, one will become a copy of the other
    //post: returns a Monster object
    //This method returns a monster that is a different type but has the same stats as the previous one for when you switch monsters
    public static Monster copyMonster(Monster m1, Monster m2) {
        m1.setCapturedHumans(m2.getCapturedHumans());
        m1.setCurrency(m2.getCurrency());
        m1.setRoom(m2.getRoom());
        m1.setFriends(m2.getFriends());
        m1.setDiploma(m2.getDiploma());
        return m1;
    }

    //pre: takes in a player object, and a boolean
    //post: returns nothing
    //this method sells a player's room for either a high value or low value
    public static void sellRoom(Monster player, boolean lowValue) {
        System.out.print(player.getRoom().getName() + " sold for $");
        if (lowValue) {
            player.setCurrency(player.getCurrency() + player.getRoom().getPurchasePrice() - player.getRoom().getNum());
            System.out.println(player.getRoom().getPurchasePrice() - player.getRoom().getNum());
        } else {
            player.setCurrency(player.getCurrency() + player.getRoom().getPurchasePrice() + player.getRoom().getNum());
            System.out.println(player.getRoom().getPurchasePrice() + player.getRoom().getNum());
        }
        player.setRoom(null);
    }

    //pre: takes in scanner object and player
    //post: returns nothing
    //This method asks if the player wants to receive training and if so gives them a diploma
    public static void training(Scanner scanner, Monster player) {
        System.out.print("Would you like to receive training for $23,000? You get access to new monster cards! (y/n) >>> ");
        if (scanner.nextLine().equalsIgnoreCase("Y")) {
            if (player.getDiploma().equals("DIPLOMA")) {
                System.out.println("You already have a diploma.");
            } else {
                if (player.getCurrency() < 22000) {
                    System.out.println("You do not have enough money.");
                } else {
                    giveDiploma(player);
                }
            }
        }
    }

    //pre: takes in a player object
    //post: returns nothing
    //This method gives a specific player a diploma
    public static void giveDiploma(Monster player) {
        player.setCurrency(player.getCurrency() - 22000);
        player.setDiploma("DIPLOMA");
    }

    //pre: takes in a Random object and a Monster object (a player)
    //post: returns an integer corresponding to what you spun
    //This method chooses a random place to land depending on the specific monster's probabilities and returns it
    public static int spin(Random random, Monster monster) {
        int num = random.nextInt(1,  101);
        if (num < monster.getChanceOfFinding()) {
            return 4;
        } else if (num < monster.getChanceOfEscaping() + monster.getChanceOfFinding()) {
            return 5;
        } else if (num < monster.getChanceOfEscaping() + monster.getChanceOfFinding() + 10) {
            return 0;
        } else if (num < monster.getChanceOfFinding() + monster.getChanceOfEscaping() + 20) {
            return 1;
        } else if (num == 99) {
            return 10;
        } else if (num != 100) {
            return 2;
        }
        return 3;
    }

    //pre: takes in a Scanner object, the logins 2d array, the FileHandler object, the players array, and the cards array
    //post: returns a String representing the username
    //This method makes the user log in to an existing account or create a new one and returns the username. It makes sure all input is valid
    public static String login(Scanner scanner, String[][] logins, FileHandler fileHandler, Monster[] players, Queue[] cards) throws IOException {
        boolean validLogin = false;
        String usernameInput = "";
        String passwordInput = "";
        while (!validLogin) {
            if (!usernameInput.equals("")) {
                System.out.println("INVALID. TRY AGAIN.");
            }
            System.out.print("Username (Enter 'CREATE' to create new login, or 'QUIT' to quit): ");
            usernameInput = scanner.nextLine();
            boolean skipLogin = false;
            if (usernameInput.equalsIgnoreCase("CREATE")) {
                logins = createLogin(scanner, logins, fileHandler, players, cards);
                skipLogin = true;
            } else if (usernameInput.equalsIgnoreCase("QUIT")) {
                System.exit(0);
            }
            if (!skipLogin) {
                System.out.print("Password: ");
                passwordInput = scanner.nextLine();
            } else {
                usernameInput = logins[logins.length-1][0];
                passwordInput = logins[logins.length-1][1];
            }
            for (int i = 0; i < logins.length; i++) { // go through all logins to find if this login matches
                if (logins[i][0].equals(usernameInput) && logins[i][1].equals(passwordInput)) {
                    validLogin = true;
                    i = logins.length;
                }
            }
        }

        return usernameInput;
    }

    //pre: takes in Scanner object, logins 2d array, FileHandler object, players array, and array of cards
    //post: returns a 2d array which is the new logins 2d array after a new account is created
    //This method allows the user to create a new VALID login and returns the new updated logins 2d array
    public static String[][] createLogin(Scanner scanner, String[][] logins, FileHandler fileHandler, Monster[] players, Queue[] cards) throws IOException {
        boolean validUsername = false;
        String username = "";
        String password = "";

        while (!validUsername) {
            if (!username.equals("")) {
                System.out.println("INVALID. TRY AGAIN.CREATE");
            }
            validUsername = true;
            System.out.print("New username: ");
            username = scanner.nextLine();
            System.out.print("New password: ");
            password = scanner.nextLine();

            for (int i = 0; i < logins.length; i++) { // go through all logins to check if this login is original
                if (logins[i][0].equals(username)) {
                    validUsername = false;
                }
            }
        }

        players[1] = (Monster) cards[1].dequeue();
        players[2] = (Monster) cards[1].dequeue();
        if (players[1].getRequiresDiploma().equals("REQUIRES DIPLOMA")) {
            players[1].setDiploma("DIPLOMA");
        }
        if (players[2].getRequiresDiploma().equals("REQUIRES DIPLOMA")) {
            players[2].setDiploma("DIPLOMA");
        }
        System.out.println("\nThese are your opponents:\n\nAI1:\n" + players[1].display() + "\n\nAI2:\n" + players[2].display() + "\n\n");

        System.out.print("Would you like to receive training to get a diploma for $22,000? You get access to better monster cards! (y/n) >>> ");
        boolean hasDiploma = false;
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            hasDiploma = true;
        }

        Monster card1 = (Monster) cards[1].dequeue();
        Monster card2 = (Monster) cards[1].dequeue();

        while (!hasDiploma && card1.getRequiresDiploma().equals("REQUIRES DIPLOMA") && card2.getRequiresDiploma().equals("REQUIRES DIPLOMA")) {
            card1 = (Monster) cards[1].dequeue();
            card2 = (Monster) cards[1].dequeue();
        }

        System.out.print("CARD 1:\n" + card1.display() + "\n\nCARD 2:\n" + card2.display() + "\n\nChoose (1/2) >>> ");
        String input = "";
        boolean valid = false;
        while (!valid) {
            input = scanner.nextLine();
            if (input.equals("1")) {
                if (!hasDiploma && card1.getRequiresDiploma().equals("REQUIRES DIPLOMA")) {
                    valid = false;
                } else {
                    players[0] = card1;
                    valid = true;
                }
            } else {
                if (!hasDiploma && card2.getRequiresDiploma().equals("REQUIRES DIPLOMA")) {
                    valid = false;
                } else {
                    players[0] = card2;
                    valid = true;
                }
            }
            if (!valid) {
                System.out.print("INVALID CHOICE. Try again >>> ");
            }
        }

        for (int i = 0; i < 3; i++) { // go through each player to set their starting cash
            players[i].setCurrency(players[i].getSalary());
        }

        if (hasDiploma) {
            players[0].setDiploma("DIPLOMA");
            players[0].setCurrency(players[0].getSalary() - 20000);
        }

        String[][] newLogins = new String[logins.length + 1][2];
        for (int i = 0; i < logins.length; i++) { // copy logins array
            newLogins[i] = logins[i];
        }

        newLogins[newLogins.length - 1] = new String[]{username, password};

        fileHandler.createGame(username, password, players);

        return newLogins;
    }

    //pre: takes in the card deck for the rooms, the card deck for monsters, and card deck for action cards
    //post: returns nothing
    //This method shuffles all the cards in all decks
    public static void shuffleCards(Queue rooms, Queue monsters, Queue action) {
        String[] roomNames = {"Ghostly Grand Ballroom",
                "Phantom Parlor",
                "Wraith's Watch Room",
                "Chilling Chamber"};
        String[] actionCards = actionCards();
        Random random = new Random();
        for (int i = 0; i < 20; i++) { // go through all cards in the decks
            int num = random.nextInt(0, 3);
            rooms.enqueue(new Room(roomNames[num], random.nextInt(30, 50) * 1000, random.nextInt(1, 15) * 1000));
            action.enqueue(new Card(actionCards[random.nextInt(0, 5)]));
            if (num == 0) {
                monsters.enqueue(new Ghost(random));
            } else if (num == 1) {
                monsters.enqueue(new Skeleton(random));
            } else if (num == 2) {
                monsters.enqueue(new Zombie(random));
            } else {
                monsters.enqueue(new Spider(random));
            }
        }
    }
}