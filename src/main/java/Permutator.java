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
        // and use for full
        totalPermutations = new BigInteger("1");
        for (int i = 1; i <= N; i++) {
            totalPermutations = totalPermutations.multiply(new BigInteger(i + ""));
        }

        positions = IntStream.rangeClosed(0, N-1).boxed().collect(Collectors.toList());
        resetPositions();
    };

    BigInteger getTotalPermutations() {
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

        // reversing tail of array
        for (i = N - 1; i > idx; i--, idx++) {
            Collections.swap(positions, idx + 1, i);
        }
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
