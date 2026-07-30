\ Test 5.1: Simple IF/THEN conditionals
\ Verifies correct conditional execution paths

INCLUDE test-harness.fs

CR ." === Test 5.1: IF/THEN Conditionals ==="

\ Simple IF/THEN with true condition
T{ : TEST1 TRUE IF 100 THEN ; TEST1 -> 100 }T

\ Simple IF/THEN with false condition
T{ : TEST2 FALSE IF 100 THEN 200 ; TEST2 -> 200 }T

\ IF/THEN with non-boolean values (0=false, non-0=true)
T{ : TEST3 1 IF 50 THEN ; TEST3 -> 50 }T
T{ : TEST4 0 IF 50 THEN 75 ; TEST4 -> 75 }T
T{ : TEST5 -1 IF 60 THEN ; TEST5 -> 60 }T

\ Nested conditionals
T{ : TEST6 TRUE IF FALSE IF 100 THEN 200 THEN ; TEST6 -> 200 }T
T{ : TEST7 TRUE IF TRUE IF 300 THEN THEN ; TEST7 -> 300 }T

\ IF/THEN affecting stack
T{ : TEST8 5 TRUE IF 10 + THEN ; TEST8 -> 15 }T
T{ : TEST9 5 FALSE IF 10 + THEN ; TEST9 -> 5 }T

\ IF/THEN with multiple stack items
T{ : TEST10 1 2 3 TRUE IF 4 THEN ; TEST10 -> 1 2 3 4 }T
T{ : TEST11 1 2 3 FALSE IF 4 THEN ; TEST11 -> 1 2 3 }T

\ Empty THEN branch
T{ : TEST12 TRUE IF THEN 99 ; TEST12 -> 99 }T
T{ : TEST13 FALSE IF THEN 88 ; TEST13 -> 88 }T

\ IF with arithmetic condition
T{ : TEST14 10 5 > IF 1 ELSE 0 THEN ; TEST14 -> 1 }T
T{ : TEST15 10 5 < IF 1 ELSE 0 THEN ; TEST15 -> 0 }T

\ Deeply nested IF
T{ : TEST16 TRUE IF TRUE IF TRUE IF 999 THEN THEN THEN ; TEST16 -> 999 }T

TEST-SUMMARY
