\ Test 5.3: Nested IF/ELSE/THEN structures
\ Verifies proper nesting and branch isolation

INCLUDE test-harness.fs

CR ." === Test 5.3: Nested Conditionals ==="

\ Two-level nesting (true-true)
T{ : TEST1 TRUE IF TRUE IF 111 THEN THEN ; TEST1 -> 111 }T

\ Two-level nesting (true-false)
T{ : TEST2 TRUE IF FALSE IF 111 THEN 222 THEN ; TEST2 -> 222 }T

\ Two-level nesting (false-true)
T{ : TEST3 FALSE IF TRUE IF 111 THEN ELSE 222 THEN ; TEST3 -> 222 }T

\ Two-level nesting with ELSE at both levels
T{ : TEST4 TRUE IF 1 IF 100 ELSE 200 THEN ELSE 300 THEN ; TEST4 -> 100 }T
T{ : TEST5 TRUE IF 0 IF 100 ELSE 200 THEN ELSE 300 THEN ; TEST5 -> 200 }T
T{ : TEST6 FALSE IF 1 IF 100 ELSE 200 THEN ELSE 300 THEN ; TEST6 -> 300 }T

\ Three-level nesting
T{ : TEST7 1 IF 1 IF 1 IF 7 THEN THEN THEN ; TEST7 -> 7 }T
T{ : TEST8 1 IF 1 IF 0 IF 7 ELSE 8 THEN THEN THEN ; TEST8 -> 8 }T
T{ : TEST9 1 IF 0 IF 1 IF 7 ELSE 8 THEN THEN 9 THEN ; TEST9 -> 9 }T

\ Cross-level condition effects
T{ : TEST10 0 IF 1 ELSE 1 IF 2 ELSE 3 THEN THEN ; TEST10 -> 2 }T
T{ : TEST11 1 IF 0 IF 1 ELSE 2 THEN ELSE 3 THEN ; TEST11 -> 2 }T

\ Stack preservation through nested structures
T{ : TEST12 10 20 TRUE IF TRUE IF 30 THEN THEN ; TEST12 -> 10 20 30 }T
T{ : TEST13 10 20 FALSE IF 30 ELSE TRUE IF 40 THEN THEN ; TEST13 -> 10 20 40 }T

\ Complex nesting with arithmetic
T{ : TEST14 5 3 > IF 10 DUP 10 > IF 100 ELSE 200 THEN ELSE 300 THEN ; TEST14 -> 200 }T
T{ : TEST15 10 5 > IF 20 DUP 30 > IF 111 ELSE 222 THEN ELSE 333 THEN ; TEST15 -> 222 }T

\ Four-level nesting
T{ : TEST16 1 IF 1 IF 1 IF 1 IF 16 THEN THEN THEN THEN ; TEST16 -> 16 }T
T{ : TEST17 1 IF 1 IF 1 IF 0 IF 17 ELSE 18 THEN THEN THEN THEN ; TEST17 -> 18 }T

\ Interleaved ELSE at different levels
T{ : TEST18 0 IF 10 ELSE 1 IF 20 ELSE 30 THEN THEN ; TEST18 -> 20 }T
T{ : TEST19 1 IF 0 IF 10 ELSE 20 THEN ELSE 30 THEN ; TEST19 -> 20 }T

TEST-SUMMARY
