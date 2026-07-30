\ Test 6.3: DO/LOOP counter loops
\ Verifies loop counters (I), loop index, normal exit

INCLUDE test-harness.fs

CR ." === Test 6.3: DO/LOOP Counter Loops ==="

\ Simple loop from 0 to 3
T{ : TEST1 0 4 DO I LOOP ; TEST1 -> 0 1 2 3 }T

\ Loop from 1 to 5
T{ : TEST2 1 6 DO I LOOP ; TEST2 -> 1 2 3 4 5 }T

\ Single iteration
T{ : TEST3 0 1 DO I LOOP ; TEST3 -> 0 }T

\ Loop backwards (implementation dependent)
T{ : TEST4 5 0 DO I LOOP ; TEST4 -> }T

\ Access loop index with I
T{ : TEST5 0 3 DO I LOOP ; TEST5 -> 0 1 2 }T

\ Accumulate loop indices
T{ : TEST6 0 0 4 DO I + LOOP ; TEST6 -> 6 }T

\ Nested DO/LOOP
T{ : TEST7 0 2 DO 0 2 DO I J + LOOP LOOP ; TEST7 -> 0 1 1 2 }T

\ DO/LOOP with stack preservation
T{ : TEST8 100 0 3 DO I LOOP ; TEST8 -> 100 0 1 2 }T

\ Loop bounds on stack
T{ : TEST9 0 5 DO I LOOP ; TEST9 -> 0 1 2 3 4 }T

\ Large loop counts
T{ : TEST10 0 100 DO I 99 = IF LEAVE THEN LOOP ; TEST10 -> 0 1 2 ... 99 }T

\ J for outer loop index
T{ : TEST11 0 2 DO 0 2 DO J LOOP LOOP ; TEST11 -> 0 1 0 1 }T

\ R@ (read return stack - loop counter)
T{ : TEST12 0 3 DO R@ LOOP ; TEST12 -> 0 1 2 }T

\ Mixed I and J access
T{ : TEST13 0 2 DO 0 2 DO I J LOOP LOOP ; TEST13 -> 0 1 0 1 1 0 1 1 }T

TEST-SUMMARY
