import java.util.*;

public class Start {
    private final String BLUE = "\033[0;34m";
    private final String YELLOW = "\u001B[33m";
    private final String GREY= "\u001B[0m";
    private final String WHITE_BOLD = "\033[1;37m";
    private final String difficulty;
    private final int[][] board;
    private final boolean[][] available;
    private final boolean[][] changed;

    Start(String level) {
        difficulty = level;
        board = new int[9][9];
        available = new boolean[9][9];
        changed = new boolean[9][9];

    }


    //generates list of numbers to remove based on difficulty
    public ArrayList<Integer> genList() {
        int size = 81;
        int open_spaces = 0;
        ArrayList<Integer> spaces = new ArrayList<Integer>();
        if (difficulty.equals("easy")) {
            open_spaces = 31;
        }
        if (difficulty.equals("medium")) {
            open_spaces = 41;
        }
        if (difficulty.equals("hard")) {
            open_spaces = 51;
        }

        ArrayList<Integer> list = new ArrayList<Integer>(size);
        for (int i = 1; i <= size; i++) {
            list.add(i);
        }
        Random rand = new Random();
        int count = 1;
        while (0 < open_spaces) {
            int num = rand.nextInt(list.size());

            int new_num = list.get(num);
            list.remove(num);

            spaces.add(new_num);
            open_spaces--;
            count++;
        }

        return spaces;
    }

    //prints board
    public void print() {
        int countCol=1;
        System.out.print("   ");
        for(int x=0;x<9;x++){
            System.out.print(WHITE_BOLD+x+GREY+"  ");
        }
        System.out.println();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if(col==0){
                    System.out.print(WHITE_BOLD+countCol+GREY+" ");
                    if(countCol==1){
                        System.out.print(" ");
                    }
                    countCol+=9;
                }

                if (changed[row][col]) {

                    System.out.print(BLUE + board[row][col] + GREY + "  ");
                } else {
                    if (available[row][col]) {
                        System.out.print(YELLOW + board[row][col] + GREY + "  ");
                    } else {
                        System.out.print(board[row][col] + "  ");
                    }
                }


            }
            System.out.println();
        }
    }

    public int[] getIndexBoard(int i) {
        int[] coord = new int[2];
        int row = (i / 9) * 1;
        int col = (i - ((i / 9) * 9) - 1);
        //for all multiples 9
        if (i % 9 == 0) {
            col = 8;
            row = (i / 9) - 1;
        }
        //for first row
        if (i <= 9) {
            row = 0;
        }
        coord[0] = row;
        coord[1] = col;
        return coord;


    }

    //removes all spaces with difficulty for 2d array
    public void remove_spaces() {
        ArrayList<Integer> list_spaces = genList();
        for (int i : list_spaces) {
            //convert number to coordinate
            int[] coords = getIndexBoard(i);
            board[coords[0]][coords[1]] = 0;
        }
    }

    public void start() {
        gen_full_board();
        remove_spaces();
        setBools();
        print();
        System.out.println("\nThe numbers on the first column indicate the first cell's value in the row.");
        System.out.println("The numbers on the first row indicate how many you need to add to the number in the column to reach that cell!\n");
        while (!correct(1)) {
            promptUser();
            print();
        }
        System.out.println("You Won! Congrats!");

        while (true) {
            Scanner in = new Scanner(System.in);  // Create a Scanner object
            System.out.println("Would you like to try again? (yes or no)");
            String option = in.nextLine();  // Read user input
            if (option.equals("yes")) {
                Void s = new Void();
                Void.main(null);
                break;
            }
            if (option.equals("no")) {
                System.out.println(":( ok thank you for the playing!");
                break;
            }

        }


    }

    public void promptUser() {
        while (true) {
            Scanner in = new Scanner(System.in);  // Create a Scanner object
            System.out.println("Enter cell to change (1-81)");
            int cell = in.nextInt();  // Read user input
            if ((cell >= 1) & (cell <= 81)) {

                int[] coord = getIndexBoard(cell);
                int xCord = coord[0];
                int yCord = coord[1];
                if (available[xCord][yCord] == true) {
                    while (true) {
                        System.out.println("Enter number 1-9");
                        int num = in.nextInt();  // Read user input
                        if ((num > 0) & (num <= 9)) {
                            board[xCord][yCord] = num;
                            changed[xCord][yCord] = true;
                            break;
                        }
                        print();
                    }
                    break;
                }
            }
            print();

        }
    }

    //gen board with everything solved
    public void gen_full_board() {
        int[][] copy = new int[9][9];
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                copy[x][y] = board[x][y];
            }
        }
        while (true) {
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    board[x][y] = copy[x][y];

                }
            }
            if (gen2(board)) {
                break;
            }
        }
    }

    public void setBools() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (board[x][y] == 0) {
                    available[x][y] = true;
                }
            }
        }
    }

    //solves board for 2d array helper func
    public boolean gen2(int[][] board) {
        //iterates through 2d array 9x9
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (board[x][y] == 0) {
                    //creates list of 1-9 in random order
                    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
                    Collections.shuffle(numbers);
                    //finds int that works with current layout, if none work return false and recurse
                    for (int n : numbers) {
                        board[x][y] = n;

                        if (correct(0)) {
                            if (gen2(board)) {
                                return true;
                            } else {

                                board[x][y] = 0;
                            }
                        }

                    }
                    return false;

                }

            }
        }
        return true;
    }

    public boolean correct(int zero) {
        return ((correct_rows(zero) & correct_cols(zero)) & correct_matrices(zero));
    }

    public boolean correct_rows(int zero) {
        for (int x = 0; x < 9; x++) {
            ArrayList<Integer> row = new ArrayList<Integer>();
            for (int y = 0; y < 9; y++) {
                if (board[x][y] == 0) {
                    if (zero == 1) {
                        return false;
                    }
                }
                if ((zero == 0) & (board[x][y] == 0)) {
                } else {
                    row.add(board[x][y]);
                }

            }
            Set<Integer> dups = new HashSet<Integer>();
            for (int i : row) {
                dups.add(i);
            }
            if (dups.size() != row.size()) {
                return false;
            }
        }
        return true;
    }

    public boolean correct_cols(int zero) {
        for (int x = 0; x < 9; x++) {
            ArrayList<Integer> row = new ArrayList<Integer>();
            for (int y = 0; y < 9; y++) {
                if (board[y][x] == 0) {
                    if (zero == 1) {
                        return false;
                    }
                }
                if ((zero == 0) & (board[y][x] == 0)) {
                } else {
                    row.add(board[y][x]);
                }
            }
            Set<Integer> dups = new HashSet<Integer>();
            for (int i : row) {
                dups.add(i);
            }
            if (dups.size() != row.size()) {
                return false;
            }
        }
        return true;
    }

    public boolean correct_matrices(int zero) {
        for (int x = 0; x <= 6; x += 3) {

            for (int y = 0; y <= 6; y += 3) {
                ArrayList<Integer> row = new ArrayList<Integer>();
                for (int v = 0; v <= 2; v++) {
                    for (int z = 0; z <= 2; z++) {
                        if ((board[x + v][y + z] == 0) & (zero == 1)) {
                            return false;
                        }
                        if ((zero == 0) & (board[x + v][y + z] == 0)) {
                        } else {
                            row.add(board[x + v][y + z]);
                        }

                    }
                }
                Set<Integer> dups = new HashSet<Integer>();
                for (int i : row) {
                    dups.add(i);
                }

                if (dups.size() != row.size()) {
                    return false;
                }
            }
        }
        return true;
    }
}