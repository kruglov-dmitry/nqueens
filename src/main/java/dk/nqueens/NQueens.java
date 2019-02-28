package dk.nqueens;

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

        boolean applyLineConstraint;
        boolean forcePrint;

        Permutator t;

        NQueenProblem(int _boardSize, boolean _applyLineConstraint, boolean _forcePrint) {

            boardSize = _boardSize;
            applyLineConstraint = _applyLineConstraint;

            if (applyLineConstraint && boardSize < 8)
                throw new IllegalArgumentException("Board size is less than 8 for three in line constrain - " + boardSize);

            forcePrint = _forcePrint;

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

        void updateBeforeSwap(List<Integer> queensPositions, int y) {
            int x = queensPositions.get(y);

            int diagNum = getDiagNumBottomUp(x, y);

            diagLeftBottomUp[diagNum]--;

            diagNum = getDiagNumTopDown(x, y);

            diagLeftTopDown[diagNum]--;
        }

        void updateBefore(List<Integer> queensPositions, int i, int j) {
            updateBeforeSwap(queensPositions, i);
            updateBeforeSwap(queensPositions, j);
        }

        void updateAfterSwap(List<Integer> queensPositions, int y) {
            int x = queensPositions.get(y);

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

        boolean queenAttacked(List<Integer> queensPositions, int y) {

            int x = queensPositions.get(y);

            int diagNumBU = getDiagNumBottomUp(x, y);
            int diagNumTD = getDiagNumTopDown(x, y);

            return diagLeftBottomUp[diagNumBU] > 1 || diagLeftTopDown[diagNumTD] > 1;
        }

        boolean checkLines(List<Integer> queensPositions) {
            Map<String, Set<String>> map = PointGrouper.groupPointsByLine(queensPositions);
            for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
                Set<String> queensOnTheSameLine = entry.getValue();
                if (queensOnTheSameLine.size() > 2) {
                    return false;
                }
            }
            return true;
            // return PointGrouper.groupPointsByLineExceedThreshold(queensPositions, 3);
        }

        void showSolutionNoCache() {

            while(true) {

                    do {
                        List<Integer> queensPositions = t.getPermutations();

                        fillCollisions(queensPositions);

                        int numberOfCollisions = getNumberOfCollisions();

                        if (numberOfCollisions == 0) {
                            if (!applyLineConstraint || checkLines(queensPositions)) {
                                showBoard(queensPositions, forcePrint);
                                return;
                            }

                            if (applyLineConstraint)
                                continue;

                             /*if (applyLineConstraint) {
                                 if (checkLines(queensPositions)) {
                                     showBoard(queensPositions, forcePrint);
                                     return;
                                 } else {
                                     continue;
                                 }
                             } else {
                                 showBoard(queensPositions, forcePrint);
                                 return;
                             }*/
                        }

                        boolean skipQueenSet = false;

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
                                        if (!applyLineConstraint || checkLines(queensPositions)) {
                                            showBoard(queensPositions, forcePrint);
                                            return;
                                        }

                                        if (applyLineConstraint) {
                                            skipQueenSet = true;
                                            break;
                                        }

                                        /*if (applyLineConstraint) {
                                            if (checkLines(queensPositions)) {
                                                showBoard(queensPositions, forcePrint);
                                                return;
                                            } else {
                                                skipQueenSet = true;
                                                break;
                                            }
                                        } else {
                                            showBoard(queensPositions, forcePrint);
                                            return;
                                        } */
                                    }
                                }
                            }
                            if (skipQueenSet)
                                break;
                        }
                    } while (t.nextPermutationExist());
                    t.resetPositions();
                }
        };
    };

    private void showBoard(List<Integer> queensPositions, boolean forcePrint) {
        if (queensPositions.size() > 1000 && !forcePrint)
            return;

        for (int i = 0; i < queensPositions.size(); i++) {
            for (int j = 0; j < queensPositions.size(); j++)
                if (queensPositions.get(i) != j)
                    System.out.print(" 0 ");
                else
                    System.out.print(" 1 ");
            System.out.println("");
        }
    }

    private void setQueens(int num, boolean applyLineConstraint, boolean forcePrint) {

        NQueenProblem Queen = new NQueenProblem(num, applyLineConstraint, forcePrint);

        Queen.showSolutionNoCache();
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

        boolean applyLineConstraint = true;
        boolean forcePrint = false;

        if (args.length > 1) {
            try {
                applyLineConstraint = Boolean.parseBoolean(args[1]);
            } catch (NumberFormatException nfe) {
                System.out.println("if you decided to specify Three-in-line constrain flag please note that it can be only true or false as string");
                System.exit(1);
            }
        }

        if (args.length > 2) {
            try {
                forcePrint = Boolean.parseBoolean(args[2]);
            }
            catch (NumberFormatException nfe) {
                System.out.println("if you decided to specify Force-print flag please note that it can be only true or false as string");
                System.exit(1);
            }
        }

        NQueens u = new NQueens();
        u.setQueens(num, applyLineConstraint, forcePrint);
    }
}
