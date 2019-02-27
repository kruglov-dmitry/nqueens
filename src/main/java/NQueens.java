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

        void setPositions(int[] initPositions) {
            t.setPositions(initPositions);
        }

        int computeSumBottomUp(List<Integer> queensPositions, int idx) {

            int currSum = 0;

            if (idx <= boardSize - 1) {
                int x = 0;
                int y = boardSize - idx - 1;
                while (x < idx + 1 && x < boardSize && y < boardSize) {
                    if (t.getPermutations().get(y++) == x++)
                        currSum += 1;
                }
            } else {
                int x = idx - boardSize + 1;
                int y = 0;
                while (x < boardSize && y < numOfDiags - idx) {
                    if (t.getPermutations().get(y++) == x++)
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

            if (diagNum == 9)
                System.out.println("BottomUP BEFORE UPDATE MINUS " + diagLeftBottomUp[diagNum]);
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

            if (diagNum == 9)
                System.out.println("BottomUP BEFORE UPDATE" + diagLeftBottomUp[diagNum]);

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

        void showDiags(List<Integer> queensPositions, int idx) {
            int x = queensPositions.get(idx);
            int y = idx;

            int diagNumBU = getDiagNumBottomUp(x, y);
            int diagNumTD = getDiagNumTopDown(x, y);

            System.out.println("Diags elements for column: " + idx + " Bottom up: " + diagLeftBottomUp[diagNumBU] +
                    " Top-Down: " + diagLeftTopDown[diagNumTD]);
        }

        void showDebugInfo(List<Integer> queensPositions, int i, int j) {

            System.out.println("Problematic diagonals:");
            System.out.println("BottomUP:");
            for (int idx = 0; idx < numOfDiags; idx++)
                if (diagLeftBottomUp[idx] != 0)
                    System.out.println("index - " + idx + " value " + diagLeftBottomUp[idx]);

            System.out.println("TopDown:");
            for (int idx = 0; idx < numOfDiags; idx++)
                if (diagLeftTopDown[idx] != 0)
                    System.out.println("index - " + idx + " value " + diagLeftTopDown[idx]);

            /*int prev =  diagLeftBottomUp[9];

            System.out.println("WTF - " + diagLeftBottomUp[9]);

            fillCollisions(queensPositions);

            int newv =  diagLeftBottomUp[9];

            System.out.println("After filling collisions - " + diagLeftBottomUp[9]);

            if (prev != newv) {
                int k = 1 / 0;
            } */

        }

        /*void showSolution() {
            Set<List<Integer>> cached = new HashSet<>();

            while(BigInteger.valueOf(cached.size()).compareTo(t.getTotalPermutations()) < 0) {
                if (!cached.contains(t.getPermutations())) {
                    // FIXME TODO: after we have more than Integer.MAX_VALUE elements within set time to get anxious
                    // System.out.println(Arrays.toString((t.getPermutations().toArray())));

                    do {
                        List<Integer> queensPositions = t.getPermutations();

                        if (!cached.contains(queensPositions)) {
                            cached.add(queensPositions);

                            // System.out.println("Positions:" + queensPositions);

                            fillCollisions(queensPositions);

                            int numberOfCollisions = getNumberOfCollisions();

                            if (numberOfCollisions == 0) {
                                showBoard(queensPositions);
                                return;
                            }

                            boolean cycle_exit = false;

                            for (int i = 0; i < boardSize; i++) {
                                for (int j = i + 1; j < boardSize; j++) {
                                    if (queenAttacked(queensPositions, i) || queenAttacked(queensPositions, j)) {

                                        updateBefore(queensPositions, i, j);
                                        Collections.swap(queensPositions, i, j);
                                        updateAfter(queensPositions, i, j);

                                        // cache current permutation
                                        if (cached.contains(queensPositions)) {
                                            cycle_exit = true;
                                            break;
                                        }
                                        cached.add(queensPositions);

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

                                if (cycle_exit)
                                    break;

                            }
                        }
                    } while (t.nextPermutationExist());
                }

                t.resetPositions();
            }

            // iterate over pairs - classic approach - ???
            // iterate over triplets - special conditions - ???

            //            String u = t.getPermutations().stream().map(Object::toString)
            //                    .collect(Collectors.joining(", "));

        }; */

        void showSolutionNoCache() {

            int cnt = 0;

            while(true) {

                // System.out.println("Initial position:" + t.getPermutations());

                    do {
                        List<Integer> queensPositions = t.getPermutations();

                        System.out.println("Investigate position: " + t.getPermutations());

                        fillCollisions(queensPositions);

                        int numberOfCollisions = getNumberOfCollisions();

                        if (numberOfCollisions == 0) {
                            showBoard(queensPositions);
                            return;
                        }

                        /*
                        if (cnt < 50) {
                            System.out.println("Positions:" + queensPositions);
                            System.out.println("Initial positions:");
                            showBoard(queensPositions);
                            System.out.println("Number of collisions:" + numberOfCollisions);
                            System.out.println("Problematic diagonals:");
                            System.out.println("BottomUP:");
                            for (int idx = 0; idx < numOfDiags; idx++)
                                if (diagLeftBottomUp[idx] > 1)
                                    System.out.println(idx);

                            System.out.println("TopDown:");
                            for (int idx = 0; idx < numOfDiags; idx++)
                                if (diagLeftTopDown[idx] > 1)
                                    System.out.println(idx);
                            cnt++;
                        } else
                            return;*/
//                        if(true)
//                            return;

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
                                        // showBoard(queensPositions);

                                        System.out.println("Positions:" + queensPositions);

                                        System.out.println("Initial positions:");
                                        showBoard(queensPositions);
                                        System.out.println("Number of collisions:" + newNumberOfCollisions);
                                        System.out.println("Problematic diagonals:");
                                        System.out.println("BottomUP:");
                                        for (int idx = 0; idx < numOfDiags; idx++)
                                            if (diagLeftBottomUp[idx] > 1)
                                                System.out.println(idx);

                                        System.out.println("TopDown:");
                                        for (int idx = 0; idx < numOfDiags; idx++)
                                            if (diagLeftTopDown[idx] > 1)
                                                System.out.println(idx);

                                        fillCollisions(queensPositions);

                                        Collections.swap(queensPositions, i, j);
                                        System.out.println("Prev positions:" + queensPositions);

                                        return;
                                    }

                                    System.out.println("Update positions: " + queensPositions + " changed elements: " + i + " - " + j);
                                }
                            }
                        }
                    } while (t.nextPermutationExist());
                    t.resetPositions();
                }

            // iterate over pairs - classic approach - ???
            // iterate over triplets - special conditions - ???

            //            String u = t.getPermutations().stream().map(Object::toString)
            //                    .collect(Collectors.joining(", "));

        };

        void showPermutations() {
            do {
                System.out.println(Arrays.toString((t.getPermutations().toArray())));
            } while (t.nextPermutationExist());
        };

        void checkReset() {
            Set<List<Integer>> cached = new HashSet<>();

            // System.out.println(t.getTotalPermutations());

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

    public void run(int num) {

        NQueenProblem Queen = new NQueenProblem(num);

        Queen.showSolutionNoCache();
    }

    void run2(int num) {
        NQueenProblem Queen = new NQueenProblem(num);

        // Queen.showPermutations();
        Queen.checkReset();
    }

    void run3(int num) {
        /*
        * Investigate position: [6, 2, 0, 3, 7, 4, 8, 9, 1, 5]
Update positions: [2, 6, 0, 3, 7, 4, 8, 9, 1, 5] changed elements: 0 - 1
Update positions: [0, 6, 2, 3, 7, 4, 8, 9, 1, 5] changed elements: 0 - 2
Update positions: [0, 6, 2, 3, 7, 4, 8, 9, 1, 5] changed elements: 0 - 3
Update positions: [0, 6, 2, 3, 7, 4, 8, 9, 1, 5] changed elements: 0 - 4
Update positions: [4, 6, 2, 3, 7, 0, 8, 9, 1, 5] changed elements: 0 - 5
        * */

        NQueenProblem Queen = new NQueenProblem(num);
        int testArray2[] = {6, 2, 0, 3, 7, 4, 8, 9, 1, 5};

        Queen.setPositions(testArray2);

        while(true) {
        do {
            List<Integer> queensPositions = Queen.t.getPermutations();

            System.out.println("Investigate position: " + queensPositions);

            Queen.fillCollisions(queensPositions);

            int numberOfCollisions1 = Queen.getNumberOfCollisions();

            if (numberOfCollisions1 == 0) {
                showBoard(queensPositions);
                return;
            }

            for (int i = 0; i < Queen.boardSize; i++) {
                for (int j = i + 1; j < Queen.boardSize; j++) {
                    if (Queen.queenAttacked(queensPositions, i) || Queen.queenAttacked(queensPositions, j)) {

                        System.out.println("Processing elements: " + i + " " + j);

                        int numberOfCollisions = Queen.getNumberOfCollisions();

                        if (Queen.queenAttacked(queensPositions, i)) {
                            System.out.println("Queen under attack: " + i + " column " + queensPositions.get(i));
                        } else if (Queen.queenAttacked(queensPositions, j)) {
                            System.out.println("Queen under attack: " + j + " column " + queensPositions.get(j));
                        }

                        System.out.println("Initial positions:" + queensPositions);
                        showBoard(queensPositions);

                        System.out.println("Diags element before swap:");
                        Queen.showDiags(queensPositions, i);
                        Queen.showDiags(queensPositions, j);

                        Queen.updateBefore(queensPositions, i, j);
                        Collections.swap(queensPositions, i, j);
                        Queen.updateAfter(queensPositions, i, j);

                        System.out.println("After swap positions:" + queensPositions);
                        showBoard(queensPositions);

                        System.out.println("Diags element after swap:");
                        Queen.showDiags(queensPositions, i);
                        Queen.showDiags(queensPositions, j);

                        int newNumberOfCollisions = Queen.getNumberOfCollisions();

                        System.out.println("Number of collisions before: " + numberOfCollisions + " VS " + newNumberOfCollisions);

                        if (newNumberOfCollisions >= numberOfCollisions) {
                            Queen.updateBefore(queensPositions, i, j);
                            Collections.swap(queensPositions, i, j);
                            Queen.updateAfter(queensPositions, i, j);
                        }

                        Queen.showDebugInfo(queensPositions, i, j);

                        if (newNumberOfCollisions == 0) {
                            Queen.fillCollisions(queensPositions);
                            int uuu = Queen.getNumberOfCollisions();
                            System.out.println(" after recomputation collision num: " + uuu);
                            showBoard(queensPositions);
                            return;
                        }
                    }
                }
            }
        } while (Queen.t.nextPermutationExist());
            Queen.t.resetPositions();
    }
    }

    void run4(int num) {
        NQueenProblem Queen = new NQueenProblem(num);
        int testArray2[] = {4, 1, 5, 2, 9, 7, 0, 8, 3, 6};
        Queen.setPositions(testArray2);

        List<Integer> queensPositions = Queen.t.getPermutations();

        System.out.println("Investigate position: " + queensPositions);

        Queen.fillCollisions(queensPositions);

        int numberOfCollisions1 = Queen.getNumberOfCollisions();

        System.out.println("Number of collisions before: " + numberOfCollisions1);
        int i = 7;
        int j = 8;

        System.out.println("Initial positions:" + queensPositions);
        showBoard(queensPositions);

        System.out.println("Diags element before swap:");
        Queen.showDiags(queensPositions, i);
        Queen.showDiags(queensPositions, j);

        Queen.updateBefore(queensPositions, i, j);
        Collections.swap(queensPositions, i, j);
        Queen.updateAfter(queensPositions, i, j);

        System.out.println("After swap positions:" + queensPositions);
        showBoard(queensPositions);

        System.out.println("Diags element after swap:");
        Queen.showDiags(queensPositions, i);
        Queen.showDiags(queensPositions, j);

        int newNumberOfCollisions = Queen.getNumberOfCollisions();

        System.out.println("Number of collisions before: " + numberOfCollisions1 + " VS " + newNumberOfCollisions);

        /*if (newNumberOfCollisions >= numberOfCollisions) {
            Queen.updateBefore(queensPositions, i, j);
            Collections.swap(queensPositions, i, j);
            Queen.updateAfter(queensPositions, i, j);
        }*/

        Queen.showDebugInfo(queensPositions, i, j);

        if (newNumberOfCollisions == 0) {
            Queen.fillCollisions(queensPositions);
            int uuu = Queen.getNumberOfCollisions();
            System.out.println(" after recomputation collision num: " + uuu);
            showBoard(queensPositions);
            // return;
        }
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
        u.run(num);

    }
}
