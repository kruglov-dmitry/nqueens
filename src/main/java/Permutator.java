import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Permutator {
    private int N;
    private BigInteger totalPermutations;

    private List<Integer> positions;
    private Set<List<Integer>> permutations = new HashSet<>();

    Permutator(int size) {

        if (size < 4)
            throw new IllegalArgumentException("Board size is less than 4: " + size);

        N = size;

        // FIXME should take into account lack of dublicate within rows & columns!
        totalPermutations = new BigInteger("1");
        for (int i = 1; i <= N; i++) {
            totalPermutations = totalPermutations.multiply(new BigInteger(i + ""));
        }

        positions = IntStream.rangeClosed(0, N-1).boxed().collect(Collectors.toList());
        resetPositions();

        // 3, 1, 2, 0
        // [2, 3, 1, 0]
        /*positions = new ArrayList<>();
        positions.add(3);
        positions.add(1);
        positions.add(2);
        positions.add(0);*/

        /*positions = new ArrayList<>();
        // 7, 0, 4, 8, 3, 9, 2, 6, 1, 5
        positions.add(7);
        positions.add(0);
        positions.add(4);
        positions.add(8);
        positions.add(3);
        positions.add(9);
        positions.add(2);
        positions.add(6);
        positions.add(1);
        positions.add(5); */
    };

    BigInteger getTotalPermutations() {
        return totalPermutations;
    }

    void resetPositions() {
        Collections.shuffle(positions);
        System.out.println("Initial positions: " + positions);
    }

    void setPositions(int[] initPositions) {
        positions.clear();
        for (int i = 0; i < N; i++)
            positions.add(initPositions[i]);
        //positions = Arrays.asList(initPositions);

    }

    private void permute() {
        if (N < 2)
            return;

        int idx = N - 2;
        while (idx >= 0 && positions.get(idx) >= positions.get(idx + 1))
            idx--;

        // System.out.println("idx: " + idx);

        if (idx == -1) {
            Collections.sort(positions);
            return;
        }

        int i = N - 1;

        while (i > idx) {
            if (positions.get(i) > positions.get(idx)) {

                Collections.swap(positions, idx, i);

                break;
            }
            i--;
        }

        // System.out.println("Before: " + positions);

        // reversing tail of array
        for (i = N - 1; i > idx; i--, idx++) {
            Collections.swap(positions, idx + 1, i);
        }

        // System.out.println("After: " + positions);
    }

    boolean nextPermutationExist() {
        permute();

        if (permutations.contains(positions)) {
            return false;
        }

        permutations.add(positions);
        return true;
    }

    List<Integer> getPermutations() {
        return positions;
    }
}
