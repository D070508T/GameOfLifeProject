import java.io.*;
import java.util.Random;

public class FileHandler {
    private String[] data;
    private File file;

    public FileHandler(File f) throws IOException {
        file = f;
        data = makeData();
    }

    //pre: takes in nothing
    //post: returns a string 2d array
    //returns a 2d array with the logign information for all accounts
    public String[][] logins() {
        int counter = 0;
        for (String str : data) { // count how many accounts there are
            if (str.startsWith("Username:")) {
                counter++;
            }
        }

        String[][] logins = new String[counter][2];

        int index = 0;
        for (String str : data) { // add logins to the logins 2d array
            if (str.startsWith("Username")) {
                logins[index][0] = str.substring(10);
            } else if (str.startsWith("Password")) {
                logins[index][1] = str.substring(10);
                index++;
            }
        }

        return logins;
    }

    //pre: takes in nothing
    //post: returns a string of the entire data
    //This method returns a string array with all the data currency in the file
    public String[] makeData() throws IOException {
        create();
        String[] data = new String[arraySize()];
        read(data, 0, new BufferedReader(new FileReader(file)));
        return data;
    }

    //pre: Takes in a string array, the index, and a BufferedReader
    //post: returns nothing
    //This method loads the file into the data array recursively
    public void read(String[] array, int index, BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line != null) {
            array[index] = line;
            read(array, index + 1, reader);
        }
    }

    //pre: takes in nothing
    //post: returns an int
    //Returns the size of the file (how many lines)
    public int arraySize() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int i = 0;
        while (reader.readLine() != null) {
            i++;
        }
        return i;
    }

    //pre: takes in the username
    //post: returns an array of Monsters (players)
    //Loads in all the information from the file for the monsters and returns the Monster objects
    public Monster[] loadGame(String user) {
        Random random = new Random();
        String[] newData = newData(user);
        Monster PLR = null;
        Monster _AI1 = null;
        Monster _AI2 = null;

        Monster monster = null;

        for (int i = 0; i < 18; i++) { // go through the data to load all the information into the players
            if (newData[i] != null) {
                if (newData[i].startsWith("Selected Monster: ")) {
                    if (newData[i].endsWith("Skeleton")) {
                        monster = new Skeleton(random);
                    } else if (newData[i].endsWith("Spider")) {
                        monster = new Spider(random);
                    } else if (newData[i].endsWith("Ghost")) {
                        monster = new Ghost(random);
                    } else if (newData[i].endsWith("Zombie")){
                        monster = new Zombie(random);
                    }

                    if (newData[i].charAt(19) == 'D') {
                        monster.setDiploma("DIPLOMA");
                    } else {
                        monster.setDiploma("NO DIPLOMA");
                    }
                } else if (newData[i].startsWith("Currency: ")) {
                    monster.setCurrency(getInt(newData[i].substring(10)));
                } else if (newData[i].startsWith("Friends: ")) {
                    String friends = newData[i].substring(9);
                    loadData(friends, monster, true);
                } else if (newData[i].startsWith("Room: ")) {
                    if (!newData[i].endsWith("null")) {
                        String name = newData[i].substring(6, newData[i].indexOf(','));
                        int price = getInt(newData[i].substring(newData[i].indexOf("purchase price: ") + 17, newData[i].indexOf(",boundry: ")));
                        int boundry = getInt(newData[i].substring(newData[i].indexOf("boundry: ") + 9));
                        monster.setRoom(new Room(name, price, boundry));
                    } else {
                        monster.setRoom(null);
                    }
                } else if (newData[i].startsWith("Captured Humans: ")) {
                    String humans = newData[i].substring(17);
                    loadData(humans, monster, false);
                } else if (!newData[i].startsWith("Username") && !newData[i].startsWith("Password")){
                    if (newData[i].equals("Player Done")) {
                        PLR = monster;
                    } else if (newData[i].equals("AI1 Done")) {
                        _AI1 = monster;
                    } else if (newData[i].equals("AI2 Done")){
                        _AI2 = monster;
                    }
                }
            }
        }

        return new Monster[]{PLR, _AI1, _AI2};
    }

    //pre: takes in a numerical string
    //post: returns an int
    //Converts string to int
    public int getInt(String str) {
        int num = 0;

        for (int i = 0; i < str.length(); i++) { // go through each character in the numerical string
            num = num * 10 + (str.charAt(i) - '0');
        }

        return num;
    }

    //pre: takes in a string, a Monster object, and a boolean
    //post: returns nothing
    //Loads data into a player object, either a friend to the stack or a human to the linked list
    public void loadData(String str, Monster monster, boolean friends) {
        if (str.length() > 0 && str.indexOf(',') >= 0) {
            if (friends) {
                monster.addFriend(str.substring(0, str.indexOf(',')));
            } else {
                monster.captureHuman(str.substring(0, str.indexOf(',')));
            }
            loadData(str.substring(str.indexOf(',') + 1), monster, friends);
        }
    }

    //pre: takes in the username
    //post: returns a string array
    //Returns a string array with the data that is currently in the file that corresponds to the specific username
    public String[] newData(String user) {
        String[] newData = new String[18];
        for (int i = 0; i < data.length; i++) {
            if (data[i].endsWith(user)) {
                for (int j = 0; j < 18; j++) {
                    newData[j] = data[j + i + 2];
                }

                i = data.length;
            }
        }
        return newData;
    }

    //pre: takes in the array of players
    //post: returns a string array
    //Returns the data that corresponds to the current state of the game
    public String[] current(Monster[] players) {
        return new String[]{
                "Selected Monster: (" + players[0].getDiploma() + ") " + players[0].getName(),
                "Currency: " + players[0].getCurrency(),
                "Friends: " + players[0].getFriends(),
                "Room: " + players[0].getRoom(),
                "Captured Humans: " + players[0].getCapturedHumans(),
                "Player Done",
                "Selected Monster: (" + players[1].getDiploma() + ") " + players[1].getName(),
                "Currency: " + players[1].getCurrency(),
                "Friends: " + players[1].getFriends(),
                "Room: " + players[1].getRoom(),
                "Captured Humans: " + players[1].getCapturedHumans(),
                "AI1 Done",
                "Selected Monster: (" + players[1].getDiploma() + ") " + players[2].getName(),
                "Currency: " + players[2].getCurrency(),
                "Friends: " + players[2].getFriends(),
                "Room: " + players[2].getRoom(),
                "Captured Humans: " + players[2].getCapturedHumans(),
                "AI2 Done"
        };
    }

    //pre: takes in the username, and the array of players
    //post: returns nothing
    //Saves the game onto the file
    public void saveGame(String user, Monster[] players) throws IOException {
        String[] newData = current(players);

        for (int i = 0; i < data.length; i++) { // go through each line in data
            if (data[i].endsWith(user)) {
                for (int j = 0; j < 18; j++) { // go through each of the 18 important lines with the data needed to load
                    data[i + j + 2] = newData[j];
                }
                i = data.length;
            }
        }

        rewriteFile(data);
    }

    //pre: takes in a string array
    //post: returns nothing
    //Rewrites the file by clearing it and writing each line in the _data array
    public void rewriteFile(String[] _data) throws IOException {
        PrintWriter pw = new PrintWriter(file);
        pw.close();

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < _data.length; i++) { // go through each line in data and write it to the file
            writer.write(data[i] + "\n");
        }
        writer.close();
    }

    //pre: takes in the username, the password, and array of players
    //post: returns nothing
    //Creates the save log in the file
    public void createGame(String username, String password, Monster[] players) throws IOException {
        String[] newData = new String[data.length + 20];
        for (int i = 0; i < data.length; i++) { // go through each line in data and copy it to newData
            newData[i] = data[i];
        }

        newData[newData.length - 20] = "Username: " + username;
        newData[newData.length - 19] = "Password: " + password;
        data = newData;
        saveGame(username, players);
    }

    //pre: takes in nothing
    //post: returns nothing
    //Checks if the file is already on the computer, if not, it creates it
    public void create() {
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println(file.getName() + " file already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }
}