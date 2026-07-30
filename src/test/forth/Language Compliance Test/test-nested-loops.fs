\ Test 6.5: Nested loops and LEAVE
\ Verifies proper loop nesting, J (outer index), LEAVE for early exit

INCLUDE test-harness.fs

CR ." === Test 6.5: Nested Loops and LEAVE ==="

\ Simple nested loops
T{ : TEST1 0 2 DO 0 2 DO I J LOOP LOOP ; TEST1 -> 0 1 0 1 1 0 1 1 }T

\ Three-level nesting
T{ : TEST2 0 2 DO 0 2 DO 0 2 DO I LOOP LOOP LOOP ; TEST2 -> 0 1 0 1 0 1 0 1 }T

\ Accumulate nested
T{ : TEST3 0 0 3 DO 0 3 DO I + LOOP LOOP ; TEST3 -> 9 }T

\ J (access outer loop index)
T{ : TEST4 0 3 DO J LOOP ; TEST4 -> 0 1 2 }T

\ J in nested loop
T{ : TEST5 0 2 DO 0 2 DO J LOOP LOOP ; TEST5 -> 0 0 1 0 1 }T

\ Multiple J references
T{ : TEST6 0 2 DO J 0 2 DO J LOOP LOOP ; TEST6 -> 0 0 1 1 0 1 }T

\ LEAVE - exit loop early
T{ : TEST7 0 10 DO I 5 = IF LEAVE THEN I LOOP ; TEST7 -> 0 1 2 3 4 }T

\ LEAVE in nested loop (exits inner)
T{ : TEST8 0 3 DO 0 10 DO I 2 = IF LEAVE THEN I LOOP LOOP ; TEST8 -> 0 1 0 1 0 1 }T

\ LEAVE in both levels
T{ : TEST9 0 10 DO I 3 = IF LEAVE THEN 0 10 DO I 2 = IF LEAVE THEN I LOOP LOOP ; TEST9 -> 0 1 0 1 0 1 }T

\ Unroll with variable step and LEAVE
T{ : TEST10 0 20 DO I 3 = IF LEAVE THEN I 2 +LOOP ; TEST10 -> 0 2 }T

\ LEAVE with UNLOOP (explicit cleanup)
T{ : TEST11 0 10 DO I 5 = IF UNLOOP EXIT THEN I LOOP 99 ; TEST11 -> 0 1 2 3 4 }T

\ Nested with separate LEAVE conditions
T{ : TEST12 0 5 DO I 2 = IF LEAVE THEN 0 5 DO I 1 = IF LEAVE THEN I LOOP LOOP ; TEST12 -> 0 }T

\ LEAVE doesn't affect outer continuation
T{ : TEST13 0 3 DO 0 2 DO LEAVE LOOP I LOOP ; TEST13 -> 0 1 2 }T

\ Complex nested with accumulation and LEAVE
T{ : TEST14 0 0 5 DO I 0 3 DO I J + 2 = IF LEAVE THEN LOOP LOOP ; TEST14 -> }T

TEST-SUMMARY
