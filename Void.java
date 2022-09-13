import java.util.Scanner;

public class Void {
    public static void main(String[] args) {
        //loops until valid input
        while (true) {
            Scanner in = new Scanner(System.in);  // Create a Scanner object
            System.out.println("Welcome to Sudoko, made by Bradley. Enjoy!\n");
            System.out.println("Enter difficulty (easy, medium, hard)");

            String difficulty = in.nextLine();  // Read user input
            if (difficulty.equals("easy") || difficulty.equals("medium") || difficulty.equals("hard")) {

                Start s = new Start(difficulty);
                s.start();
                break;
            }

        }

    }
}
