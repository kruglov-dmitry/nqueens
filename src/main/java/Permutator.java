import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Permutator {
    private int N;
    private int totalPermutations;
    private List<Integer> positions;
    private Set<List<Integer>> permutations = new HashSet<>();

    Permutator(int size) {

        if (size < 4)
            throw new IllegalArgumentException("Board size is less than 4: " + size);

        N = size;

        totalPermutations = 1;
        for (int i = 1; i<N+1; i++)
            totalPermutations *= i;

        positions = IntStream.rangeClosed(0, N-1).boxed().collect(Collectors.toList());
        resetPositions();

        // 3, 1, 2, 0
        // [2, 3, 1, 0]
        /*positions = new ArrayList<>();
        positions.add(3);
        positions.add(1);
        positions.add(2);
        positions.add(0);*/

        System.out.println("Initial positions: " + positions);

    };

    int getTotalPermutations() {
        return totalPermutations;
    }

    void resetPositions() {
        Collections.shuffle(positions);
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
