package dk.nqueens;

import java.util.*;

class PointGrouper {

    static boolean groupPointsByLineExceedThreshold(List<Integer> points, int threshold) {
        int size = points.size();

        for(int y1 = 0; y1 < size - 1; y1++) {
            for(int y2 = y1 + 1; y2 < size; y2++) {
                int x1 = points.get(y1);
                int x2 = points.get(y2);

                int pointsOnLine = 0;

                for(int y3 : points) {
                    int x3 = points.get(y3);
                    if(isColinear(x1, y1, x2, y2, x3, y3)) {
                        pointsOnLine++;
                    }
                }

                if(pointsOnLine >= threshold) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean isColinear(int x1, int y1, int x2, int y2, int x3, int y3) {
        return ((x1 - x2) * (y2 - y3)) - ((x2 - x3) * (y1 - y2)) == 0;
    }

    static Map<String, Set<String>> groupPointsByLine(List<Integer> points) {
        /*
        * Assumptions:
        *       NO points have the same X
        *       X >= 0
        *       No points have the same Y by design
        * */
        Map<String, Set<String>> result = new HashMap<>();

        for (int y1 = 0; y1 < -1 + points.size(); y1++) {
            for (int y2 = y1 + 1; y2 < points.size(); y2++) {
                int x1 = points.get(y1);
                String p1 = String.format("%d,%d", x1, y1);

                int x2 = points.get(y2);
                String p2 = String.format("%d,%d", x2, y2);

                // FIXME NOTE: dont want to mess with rounding and precision
                String key = String.format("%d/%d * X + %d/%d", (y2 - y1), (x2 - x1), (x2 * y1 - y2 * x1), (x2 - x1));

                if (!result.containsKey(key)) {
                    Set<String> newEntries = new HashSet<>();
                    newEntries.add(p1);
                    newEntries.add(p2);
                    result.put(key, newEntries);
                } else {
                    Set<String> prev = result.get(key);
                    prev.add(p1);
                    prev.add(p2);
                }
            }
        }

        return result;
    };
}
