\ Test 5.4: Conditional edge cases
\ Verifies proper handling of unusual structures and conditions

INCLUDE test-harness.fs

CR ." === Test 5.4: Conditional Edge Cases ==="

\ Empty THEN branch
T{ : TEST1 TRUE IF THEN 99 ; TEST1 -> 99 }T
T{ : TEST2 FALSE IF THEN 88 ; TEST2 -> 88 }T

\ Empty ELSE branch
T{ : TEST3 TRUE IF 100 ELSE THEN ; TEST3 -> 100 }T
T{ : TEST4 FALSE IF 100 ELSE THEN 200 ; TEST4 -> 200 }T

\ Empty both branches
T{ : TEST5 TRUE IF ELSE THEN 77 ; TEST5 -> 77 }T
T{ : TEST6 FALSE IF ELSE THEN 66 ; TEST6 -> 66 }T

\ Complex boolean conditions
T{ : TEST7 5 3 > 2 1 > AND IF 1 ELSE 0 THEN ; TEST7 -> 1 }T
T{ : TEST8 5 3 < 2 1 > OR IF 1 ELSE 0 THEN ; TEST8 -> 0 }T

\ Condition consumes stack items
T{ : TEST9 10 5 > 100 SWAP DROP SWAP DROP IF + ELSE - THEN ; TEST9 -> 15 }T

\ Flag generation inline
T{ : TEST10 3 DUP 4 > IF 200 ELSE 300 THEN ; TEST10 -> 300 }T
T{ : TEST11 5 DUP 4 > IF 200 ELSE 300 THEN ; TEST11 -> 200 }T

\ Condition from function call
T{ : PRED > ; : TEST12 5 3 PRED IF 44 ELSE 55 THEN ; TEST12 -> 44 }T

\ Multiple flag patterns
T{ : TEST13 0 0= IF 10 ELSE 20 THEN ; TEST13 -> 10 }T
T{ : TEST14 -1 0= IF 10 ELSE 20 THEN ; TEST14 -> 20 }T

\ Deeply nested with all empty branches
T{ : TEST15 1 IF 0 IF ELSE THEN ELSE THEN 42 ; TEST15 -> 42 }T

\ Unusual stack depths
T{ : TEST16 1 2 3 4 5 TRUE IF 6 THEN ; TEST16 -> 1 2 3 4 5 6 }T
T{ : TEST17 1 2 3 4 5 FALSE IF 6 THEN 7 ; TEST17 -> 1 2 3 4 5 7 }T

\ Condition at various positions
T{ : TEST18 100 200 5 3 > IF 300 THEN ; TEST18 -> 100 200 300 }T
T{ : TEST19 100 200 5 3 < IF ELSE 300 THEN ; TEST19 -> 100 200 300 }T

TEST-SUMMARY
