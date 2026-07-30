\ Test 6.1: BEGIN/UNTIL loops
\ Verifies post-test loop iteration and exit

INCLUDE test-harness.fs

CR ." === Test 6.1: BEGIN/UNTIL Loops ==="

\ Simple countdown loop
T{ : TEST1 0 5 BEGIN DUP 0 = UNTIL DROP ; TEST1 -> }T

\ Loop counter
T{ : TEST2 0 : BEGIN 1+ DUP 5 = UNTIL DROP ; 0 BEGIN 1+ DUP 5 = UNTIL -> 5 }T

\ Simple iteration
T{ : TEST3 0 BEGIN 1+ DUP 3 > UNTIL ; TEST3 -> 4 }T

\ Count accumulator
T{ : TEST4 0 1 BEGIN + 1+ DUP 5 = UNTIL DROP ; TEST4 -> }T

\ Loop with condition on stack
T{ : TEST5 1 BEGIN DUP 2 * DUP 10 > UNTIL DROP ; TEST5 -> 16 }T

\ Factorial-like
T{ : TEST6 1 1 BEGIN DUP 4 > UNTIL DROP ; TEST6 -> 1 }T

\ Variable decrement
T{ : TEST7 10 BEGIN 1- DUP 0 = UNTIL DROP ; TEST7 -> }T

\ Loop with accumulation
T{ : TEST8 0 0 5 BEGIN DUP 0 > WHILE + 1- REPEAT DROP ; TEST8 -> 10 }T

\ Simple repetition
T{ : TEST9 5 BEGIN 1- DUP 0 = UNTIL DROP ; TEST9 -> }T

\ Until with comparison
T{ : TEST10 1 BEGIN 1+ DUP 100 > UNTIL ; TEST10 -> 101 }T

\ Nested loop (simple case)
T{ : TEST11 1 BEGIN 1+ DUP 3 = UNTIL DROP ; TEST11 -> }T

\ Loop state preservation
T{ : TEST12 100 BEGIN DUP 1- SWAP DROP 1 = UNTIL ; TEST12 -> 1 }T

TEST-SUMMARY
