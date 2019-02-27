import java.math.BigInteger;
import java.util.*;

public class NQueens {

    /*
    *   Board state:
    *
    *       1 0 0 0
    *       0 0 1 0
    *       0 1 0 0
    *       0 0 0 1
    *
    *   Permutation representation:
    *       (0, 2, 1, 3)
    *
    *   Collisions count by diagonals:
    *       \, L->R, bottom-up: (0, 0, 1, 2, 1, 0, 0)
    *       /, L->R, top-down: (1, 0, 0, 2, 0, 0, 1)
    *
    * */

    private class NQueenProblem {

        int boardSize;
        int numOfDiags;

        int[] diagLeftBottomUp;
        int[] diagLeftTopDown;

        Permutator t;

        NQueenProblem(int _boardSize) {

            boardSize = _boardSize;
            numOfDiags = 2 * boardSize - 1;

            diagLeftBottomUp = new int[numOfDiags];
            diagLeftTopDown = new int[numOfDiags];

            t = new Permutator(boardSize);
        };

        int computeSumBottomUp(List<Integer> queensPositions, int idx) {

            int currSum = 0;

            if (idx <= boardSize - 1) {
                int x = 0;
                int y = boardSize - idx - 1;
                while (x < idx + 1 && x < boardSize && y < boardSize) {
                    if (queensPositions.get(y++) == x++)
                        currSum += 1;
                }
            } else {
                int x = idx - boardSize + 1;
                int y = 0;
                while (x < boardSize && y < numOfDiags - idx) {
                    if (queensPositions.get(y++) == x++)
                        currSum += 1;
                }
            }
            return currSum;
        }

        int computeSumTopDown(List<Integer> queensPositions, int idx) {
            int currSum = 0;

            if (idx <= boardSize - 1) {
                int x = 0;
                int y = idx;

                while (x < Math.min(idx + 1, boardSize) && y >= 0)
                    if (queensPositions.get(y--) == x++)
                        currSum += 1;
            } else {
                int x = idx - boardSize + 1;
                int y = boardSize - 1;

                while (x < boardSize && y >= idx - boardSize + 1)
                    if (queensPositions.get(y--) == x++)
                        currSum += 1;
            }

            return currSum;
        }

        void fillCollisions(List<Integer> queensPositions) {
            for (int idx = 0; idx < numOfDiags; idx++) {
                diagLeftBottomUp[idx] = computeSumBottomUp(queensPositions, idx);
                diagLeftTopDown[idx] = computeSumTopDown(queensPositions, idx);
            }
        }

        int getDiagNumBottomUp(int x, int y) {
            if (x < y)
                return boardSize - 1 - (y - x);
            else if (x > y)
                return boardSize - 1 + (x - y);
            else
                return boardSize - 1;
        }

        int getDiagNumTopDown(int x, int y) {
            return x + y;
        }

        void updateBeforeSwap(List<Integer> queensPositions, int idx) {
            int x = queensPositions.get(idx);
            int y = idx;

            int diagNum = getDiagNumBottomUp(x, y);

            diagLeftBottomUp[diagNum]--;

            diagNum = getDiagNumTopDown(x, y);

            diagLeftTopDown[diagNum]--;
        }

        void updateBefore(List<Integer> queensPositions, int i, int j) {
            updateBeforeSwap(queensPositions, i);
            updateBeforeSwap(queensPositions, j);
        }

        void updateAfterSwap(List<Integer> queensPositions, int idx) {
            int x = queensPositions.get(idx);
            int y = idx;

            int diagNum = getDiagNumBottomUp(x, y);

            diagLeftBottomUp[diagNum]++;

            diagNum = getDiagNumTopDown(x, y);

            diagLeftTopDown[diagNum]++;
        }

        void updateAfter(List<Integer> queensPositions, int i, int j) {
            updateAfterSwap(queensPositions, i);
            updateAfterSwap(queensPositions, j);
        }

        int getNumberOfCollisions() {
            int collisionsNum = 0;

            for (int idx = 0; idx < numOfDiags; idx++ ) {
                if (diagLeftBottomUp[idx] > 1)
                    collisionsNum += diagLeftBottomUp[idx] - 1;
                if (diagLeftTopDown[idx] > 1)
                    collisionsNum += diagLeftTopDown[idx] - 1;
            }

            return collisionsNum;
        }

        boolean queenAttacked(List<Integer> queensPositions, int idx) {

            int x = queensPositions.get(idx);
            int y = idx;

            int diagNumBU = getDiagNumBottomUp(x, y);
            int diagNumTD = getDiagNumTopDown(x, y);

            return diagLeftBottomUp[diagNumBU] > 1 || diagLeftTopDown[diagNumTD] > 1;
        }

        void showSolutionNoCache() {

            while(true) {

                    do {
                        List<Integer> queensPositions = t.getPermutations();

                        fillCollisions(queensPositions);

                        int numberOfCollisions = getNumberOfCollisions();

                        if (numberOfCollisions == 0) {
                            showBoard(queensPositions);
                            return;
                        }

                        for (int i = 0; i < boardSize; i++) {
                            for (int j = i + 1; j < boardSize; j++) {
                                if (queenAttacked(queensPositions, i) || queenAttacked(queensPositions, j)) {

                                    numberOfCollisions = getNumberOfCollisions();

                                    updateBefore(queensPositions, i, j);
                                    Collections.swap(queensPositions, i, j);
                                    updateAfter(queensPositions, i, j);

                                    int newNumberOfCollisions = getNumberOfCollisions();

                                    if (newNumberOfCollisions > numberOfCollisions) {
                                        updateBefore(queensPositions, i, j);
                                        Collections.swap(queensPositions, i, j);
                                        updateAfter(queensPositions, i, j);
                                    }

                                    if (newNumberOfCollisions == 0) {
                                        showBoard(queensPositions);
                                        return;
                                    }
                                }
                            }
                        }
                    } while (t.nextPermutationExist());
                    t.resetPositions();
                }
        };

        void checkReset() {
            Set<List<Integer>> cached = new HashSet<>();

            while(BigInteger.valueOf(cached.size()).compareTo(t.getTotalPermutations()) < 0) {
                if (!cached.contains(t.getPermutations())) {
                    cached.add(t.getPermutations());
                    System.out.println(Arrays.toString((t.getPermutations().toArray())));
                }
                t.resetPositions();
            }
        }

    };

    void showBoard(List<Integer> queensPositions) {
        for (int i = 0; i < queensPositions.size(); i++) {
            for (int j = 0; j < queensPositions.size(); j++)
                if (queensPositions.get(i) != j)
                    System.out.print(" 0 ");
                else
                    System.out.print(" 1 ");
            System.out.println("");
        }
    }

    void setQueens(int num) {

        NQueenProblem Queen = new NQueenProblem(num);

        Queen.showSolutionNoCache();
    }

    void run2(int num) {
        NQueenProblem Queen = new NQueenProblem(num);

        // Queen.showPermutations();
        Queen.checkReset();
    }

    public static void main(String[] args) {
        int num = -1;
        try {
            num = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException nfe) {
            System.out.println("You should specify board size!");
            System.exit(1);
        }

        NQueens u = new NQueens();
        u.setQueens(num);

    }
}
