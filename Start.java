import sun.security.rsa.RSAUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Start extends JPanel implements ActionListener {
    private String difficulty;
    private int[][] board;
    Start(String level){
        difficulty=level;
        board = new int[9][9];
    }
    public void heading(){
        System.out.println("TEST");
    }

    //generates list of numbers to remove based on difficulty
    public ArrayList<Integer> genList(){
        int size = 81;
        int open_spaces=0;
        ArrayList<Integer> spaces = new ArrayList<Integer>();
        if(difficulty.equals("easy")) {
            open_spaces = 31;
        }
        if(difficulty.equals("medium")){
                open_spaces=41;
        }
        if(difficulty.equals("hard")){
            open_spaces=51;
        }

        ArrayList<Integer> list = new ArrayList<Integer>(size);
        for(int i = 1; i <= size; i++) {
            list.add(i);
        }
        Random rand = new Random();
        int count=1;
        while(0<open_spaces) {
            int num = rand.nextInt(list.size());

            int new_num= list.get(num);
            list.remove(num);

            spaces.add(new_num);
            open_spaces--;
            count++;
        }

        return spaces;
    }

    public void gen_game(){


    }
    //prints board
    public void print(){
        for (int row = 0; row < 9; row++)
        {
            for (int ind = 0; ind < 9; ind++) {
                System.out.print(board[row][ind] + " ");
            }
            System.out.println();
        }
    }
    //removes all spaces with difficulty for 2d array
    public void remove_spaces(){
        ArrayList<Integer> list_spaces=genList();
        for(int i: list_spaces){
            //convert number to coordinate
            int row=(i/9)*1;
            int col = (i-((i/9)*9)-1);
            //for all multiples 9
            if(i%9==0){
                col=8;
                row=(i/9)-1;
            }
            //for first row
            if(i<=9){
                row=0;
            }

            board[row][col]=0;
        }
    }

    public void startt(){
        gen_full_board();
        remove_spaces();

        print();

        //solve
        gen_full_board();

        print();

    }

    //gen board with everything solved
    public void gen_full_board(){
        int[][] copy = new int[9][9];
        for(int x=0;x<9;x++){
            for(int y=0;y<9;y++){
                copy[x][y]=board[x][y];
            }
        }
        while(true) {
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

    //solves board for 2d array helper func
    public boolean gen2(int[][] board){
        //iterates through 2d array 9x9
        for(int x=0;x<9;x++) {
            for (int y = 0; y < 9; y++) {
                if (board[x][y] == 0) {
                    //creates list of 1-9 in random order
                    List<Integer> numbers = Arrays.asList(1,2,3,4,5,6,7,8,9);
                    Collections.shuffle((List<?>) numbers);
                    //finds int that works with current layout, if none work return false and recurse
                    for(int n: numbers) {
                        board[x][y] = n;
                        if (correct(0)) {
                            if (gen2(board)) {
                                return true;
                            } else {

                                board[x][y] = 0;
                            }
                        }

                    }/*return false;*/
                    return false;

                }

            }
        }
        return true;
    }

    public boolean correct(int zero){
        return ((correct_rows(zero) & correct_cols(zero)) & correct_matrices(zero));
    }

    public boolean correct_rows(int zero){
        for(int x=0;x<9;x++) {
            ArrayList<Integer> row = new ArrayList<Integer>();
            for(int y=0;y<9;y++) {
                if(board[x][y]==0){
                    if(zero==1){
                        return false;
                    }
                }
                if((zero==0)&(board[x][y]==0)){
                } else {
                    row.add(board[x][y]);
                }

            }
            Set<Integer> dups = new HashSet<Integer>();
            for(int i: row) {
                dups.add(i);
            }
            if(dups.size()!=row.size()) {
                return false;
            }
        }
        return true;
    }

    public boolean correct_cols(int zero){
        for(int x=0;x<9;x++) {
            ArrayList<Integer> row = new ArrayList<Integer>();
            for(int y=0;y<9;y++) {
                if(board[y][x]==0){
                    if(zero==1){
                        return false;
                    }
                }
                if((zero==0)&(board[y][x]==0)){
                } else {
                    row.add(board[y][x]);
                }
            }
            Set<Integer> dups = new HashSet<Integer>();
            for(int i: row) {
                dups.add(i);
            }
            if(dups.size()!=row.size()) {
                return false;
            }
        }
        return true;
    }

    public boolean correct_matrices(int zero){
        for(int x=0;x<=6;x+=3){

            for(int y=0;y<=6;y+=3){
                ArrayList<Integer> row = new ArrayList<Integer>();
                for(int v=0;v<=2;v++){
                    for(int z=0;z<=2;z++){
                        if((board[x+v][y+z]==0)&(zero==1)){
                            return false;
                        }
                        if((zero==0)&(board[x+v][y+z]==0)){
                        } else {
                            row.add(board[x+v][y+z]);
                        }

                    }
                }
                Set<Integer> dups = new HashSet<Integer>();
                for(int i: row) {
                    dups.add(i);
                }

                if(dups.size()!=row.size()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }


    /*public void set(){
        JPanel Board = new JPanel(new GridLayout(9, 9));
        JLabel[][] boardd = new JLabel[9][9];
         for(int i = 0;i<9;i++) {
            for (int j = 0; j < 9; j++) {

                boardd[i][j] = new JLabel();

                boardd[i][j].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));


                Font font = new Font("Arial", Font.PLAIN, 20);

                boardd[i][j].setFont(font);

                boardd[i][j].setForeground(Color.WHITE);

                boardd[i][j].setBackground(Color.WHITE);


                boardd[i][j].setOpaque(true);

                boardd[i][j].setHorizontalAlignment(JTextField.CENTER);

                Board.add(boardd[i][j]);

            }
        }
    }*/




}
