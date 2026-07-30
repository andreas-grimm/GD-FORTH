\ Test 6.2: BEGIN/WHILE/REPEAT loops
\ Verifies pre-test loop iteration and exit

INCLUDE test-harness.fs

CR ." === Test 6.2: BEGIN/WHILE/REPEAT Loops ==="

\ Simple WHILE loop
T{ : TEST1 5 BEGIN DUP 0 > WHILE 1- REPEAT DROP ; TEST1 -> }T

\ Count up
T{ : TEST2 0 BEGIN DUP 3 < WHILE 1+ REPEAT ; TEST2 -> 3 }T

\ Count with condition
T{ : TEST3 1 BEGIN DUP 5 < WHILE DUP 2 * REPEAT DROP ; TEST3 -> 4 }T

\ WHILE false on first iteration
T{ : TEST4 0 BEGIN DUP 1 > WHILE 1+ REPEAT 99 ; TEST4 -> 0 99 }T

\ Accumulation loop
T{ : TEST5 0 0 5 BEGIN OVER 0 > WHILE ROT + ROT 1- REPEAT SWAP DROP DROP ; TEST5 -> }T

\ Nested condition in WHILE
T{ : TEST6 10 BEGIN DUP 0 > DUP 5 < AND WHILE 1- REPEAT DROP ; TEST6 -> }T

\ WHILE with multiple stack items
T{ : TEST7 1 100 BEGIN OVER 0 > WHILE 1+ SWAP 1- SWAP REPEAT NIP ; TEST7 -> 100 }T

\ Exit immediately
T{ : TEST8 0 BEGIN FALSE WHILE 100 REPEAT 50 ; TEST8 -> 50 }T

\ Loop with arithmetic in condition
T{ : TEST9 1 BEGIN DUP DUP * 100 < WHILE 1+ REPEAT ; TEST9 -> 10 }T

\ Preserve outer stack
T{ : TEST10 100 200 5 BEGIN DUP 0 > WHILE 1- REPEAT DROP ; TEST10 -> 100 200 }T

\ Loop controlling flow
T{ : TEST11 3 BEGIN DUP 0 > WHILE DUP 2 = IF DROP 0 THEN 1- REPEAT ; TEST11 -> }T

\ Complex exit condition
T{ : TEST12 0 BEGIN DUP 100 < DUP 50 > AND WHILE 1+ REPEAT ; TEST12 -> 100 }T

TEST-SUMMARY
