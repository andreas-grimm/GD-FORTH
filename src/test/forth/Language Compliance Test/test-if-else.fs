\ Test 5.2: IF/ELSE/THEN conditional branching
\ Verifies both branches and nested conditions

INCLUDE test-harness.fs

CR ." === Test 5.2: IF/ELSE/THEN Conditionals ==="

\ Simple IF/ELSE/THEN with true condition
T{ : TEST1 TRUE IF 100 ELSE 200 THEN ; TEST1 -> 100 }T

\ Simple IF/ELSE/THEN with false condition
T{ : TEST2 FALSE IF 100 ELSE 200 THEN ; TEST2 -> 200 }T

\ IF/ELSE/THEN with non-zero/zero values
T{ : TEST3 1 IF 50 ELSE 75 THEN ; TEST3 -> 50 }T
T{ : TEST4 0 IF 50 ELSE 75 THEN ; TEST4 -> 75 }T
T{ : TEST5 -1 IF 60 ELSE 70 THEN ; TEST5 -> 60 }T

\ IF/ELSE/THEN with arithmetic
T{ : TEST6 10 5 > IF 1 ELSE 0 THEN ; TEST6 -> 1 }T
T{ : TEST7 3 5 > IF 1 ELSE 0 THEN ; TEST7 -> 0 }T
T{ : TEST8 5 5 = IF 99 ELSE 88 THEN ; TEST8 -> 99 }T

\ Nested IF/ELSE/THEN
T{ : TEST9 TRUE IF FALSE IF 10 ELSE 20 THEN ELSE 30 THEN ; TEST9 -> 20 }T
T{ : TEST10 FALSE IF 10 ELSE TRUE IF 40 ELSE 50 THEN THEN ; TEST10 -> 40 }T

\ IF/ELSE/THEN with stack operations
T{ : TEST11 5 10 > IF + ELSE * THEN ; TEST11 -> 50 }T
T{ : TEST12 10 5 > IF + ELSE * THEN ; TEST12 -> 15 }T

\ Triple nested
T{ : TEST13 1 2 > IF 0 ELSE 1 IF 2 ELSE 3 THEN THEN ; TEST13 -> 2 }T
T{ : TEST14 5 5 = IF 1 IF 100 ELSE 200 THEN ELSE 300 THEN ; TEST14 -> 100 }T

\ Multiple parameters through branches
T{ : TEST15 1 2 3 TRUE IF DROP 99 ELSE THEN ; TEST15 -> 1 2 99 }T
T{ : TEST16 1 2 3 FALSE IF DROP 99 ELSE SWAP THEN ; TEST16 -> 1 3 2 }T

\ Complex condition expression
T{ : TEST17 7 DUP 3 > SWAP 10 < AND IF 888 ELSE 777 THEN ; TEST17 -> 888 }T

\ Alternating branches in sequence
T{ : TEST18 TRUE IF 1 ELSE 2 THEN FALSE IF 3 ELSE 4 THEN ; TEST18 -> 1 4 }T

TEST-SUMMARY
