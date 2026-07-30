\ Test 6.4: +LOOP variable step loops
\ Verifies variable loop stepping and negative steps

INCLUDE test-harness.fs

CR ." === Test 6.4: +LOOP Variable Stepping ==="

\ Step by 1 (equivalent to LOOP)
T{ : TEST1 0 5 DO I 1 +LOOP ; TEST1 -> 0 1 2 3 4 }T

\ Step by 2
T{ : TEST2 0 10 DO I 2 +LOOP ; TEST2 -> 0 2 4 6 8 }T

\ Step by 3
T{ : TEST3 0 12 DO I 3 +LOOP ; TEST3 -> 0 3 6 9 }T

\ Step by 5
T{ : TEST4 0 20 DO I 5 +LOOP ; TEST4 -> 0 5 10 15 }T

\ Large step
T{ : TEST5 0 100 DO I 10 +LOOP ; TEST5 -> 0 10 20 30 40 50 60 70 80 90 }T

\ Negative step (count down)
T{ : TEST6 10 0 DO I -1 +LOOP ; TEST6 -> 10 9 8 7 6 5 4 3 2 1 }T

\ Negative step by 2
T{ : TEST7 20 0 DO I -2 +LOOP ; TEST7 -> 20 18 16 14 12 10 8 6 4 2 }T

\ Mixed positive in LOOP then negative in nest
T{ : TEST8 0 5 DO 0 2 DO I J 1 +LOOP LOOP ; TEST8 -> 0 0 1 0 0 1 0 0 1 0 0 1 0 0 1 }T

\ Step of 0 (implementation behavior varies)
\ T{ : TEST9 0 5 DO I 0 +LOOP ; TEST9 -> infinite }T

\ Variable step from stack
T{ : TEST10 0 15 2 SWAP DO I ROT SWAP 3 +LOOP ; TEST10 -> 0 3 6 9 12 }T

\ Accumulate with variable step
T{ : TEST11 0 0 10 DO I 2 +LOOP 10 DO I 3 +LOOP ; TEST11 -> 0 2 4 6 8 }T

\ Single iteration with +LOOP
T{ : TEST12 0 1 DO I 1 +LOOP ; TEST12 -> 0 }T

\ Skip to end with large step
T{ : TEST13 0 5 DO I 100 +LOOP ; TEST13 -> 0 }T

TEST-SUMMARY
