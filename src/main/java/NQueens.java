import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NQueens {

    private class NQueenProblem {
        int boardSize;
        int[][] board;

        public NQueenProblem(int _boardSize) {
            boardSize = _boardSize;
            board = new int[boardSize][boardSize];
        };

        private List<Integer> randomInitBoard() {
            // TODO
            List a1 = new ArrayList<Integer>();
            return a1;
        };

        public boolean getNextSolution() {

            List u = randomInitBoard();
            // iterate over pairs - classic approach - ???
            // iterate over triplets - special conditions - ???

            return false;
        };

        public int [][] getBoard() {
            return board;
        };

        public int getBoardSize() {
            return boardSize;
        }

    };

    public void showBoard(int[][] board, int N) {
        for (int i = 0; N > i; i++) {
            for (int j = 0; j < N; j++)
                System.out.print(" " + board[i][j] + " ");
            System.out.println();
        }
    }

    public static void main(String[] args)
    {
        int num = -1;
        try {
            num = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException nfe) {
            System.out.println("You should specify board size!");
            System.exit(1);
        }

        NQueenProblem Queen = new NQueenProblem(num);

        System.out.println("Press any key for next solution (if any) or ESC to exit");

        Scanner input = new Scanner(System.in);
        while(input.nextInt() != 27) {
            if (Queen.getNextSolution())
                showBoard(Queen.getBoard(), Queen.getBoardSize());
            else {
                System.out.println("Can't find next solution");
                break;
            }
        }
    }
}
