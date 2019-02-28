### Requirements
- Java 8 or later
- gradle 4.10 or later

### How to build
```bash
gradle build
```
It will create jar `nqueens-1.0-SNAPSHOT.jar` file under folder `build/libs/`

### How to run
```bash
sh start.sh <board_size> [<mode>] [<force_print>]
```
Where:
 * `board_size`     -   is integer within 8-1000000 range.
 * `mode`           -   boolean flag to specify whether to apply `three in line` restriction or not, if not specified - will apply
 * `force_print`    -   boolean flag to specify whether to print resulting board in cases when board_size is big
First found board will be printed in case board size less then 1000.

NOTE: for classical n-queen problem (no `three in line` restriction) you can specify board size 4-1000000  
NOTE: In case restriction is active, acceptable board size: 4, [8, 30+]
Few timings for orientation:
```bash
time sh start.sh 100000 false

real	9m45.001s
user	9m20.165s
sys	0m3.579s

time sh start.sh 30
real	1m38.018s
user	1m44.359s
sys	0m0.730s  
```
NOTE: If `force_print` flag is set and size of input is more than 1000 size - output may be time consuming.  

### Approach overview
Initial idea based on `iterative repair` algorithm i.e. set all queens on the board and iteratively minimise 
penalty factor: i.e. number of queens on the same line of board.
Detailed description can be found at following paper: [A Polynomial Time Algorithm for the N-Queens Problem](http://citeseerx.ist.psu.edu/viewdoc/download;jsessionid=4DC9292839FE7B1AFABA1EDB8183242C?doi=10.1.1.57.4685&rep=rep1&type=pdf)
As soon as we find permutation that do not have any queens clash - we apply point grouping or counting 
to group lines based on line equation `y = Ax + B`, grouping `A` and `B` to keys for accumulating points related to that line. 