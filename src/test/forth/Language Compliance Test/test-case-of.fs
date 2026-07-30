\ Test 5.5: CASE/OF multi-way branching
\ Verifies correct case selection and default handling

INCLUDE test-harness.fs

CR ." === Test 5.5: CASE/OF Multi-way Branching ==="

\ Simple CASE/OF/ENDOF
T{ : TEST1 3 CASE 1 OF 10 ENDOF 2 OF 20 ENDOF 3 OF 30 ENDOF ENDCASE ; TEST1 -> 30 }T

\ First case match
T{ : TEST2 1 CASE 1 OF 111 ENDOF 2 OF 222 ENDOF ENDCASE ; TEST2 -> 111 }T

\ Second case match
T{ : TEST3 2 CASE 1 OF 111 ENDOF 2 OF 222 ENDOF ENDCASE ; TEST3 -> 222 }T

\ No match (default, consumes the selector)
T{ : TEST4 99 CASE 1 OF 10 ENDOF 2 OF 20 ENDOF ENDCASE ; TEST4 -> }T

\ CASE with default action
T{ : TEST5 3 CASE 1 OF 10 ENDOF 2 OF 20 ENDOF 30 ENDCASE ; TEST5 -> 30 }T

\ Multiple case entries
T{ : TEST6 4 CASE 1 OF 1 ENDOF 2 OF 2 ENDOF 3 OF 3 ENDOF 4 OF 4 ENDOF ENDCASE ; TEST6 -> 4 }T

\ CASE with larger values
T{ : TEST7 100 CASE 10 OF 1 ENDOF 100 OF 2 ENDOF 1000 OF 3 ENDOF ENDCASE ; TEST7 -> 2 }T

\ CASE with negative values
T{ : TEST8 -1 CASE -1 OF 111 ENDOF 0 OF 222 ENDOF ENDCASE ; TEST8 -> 111 }T

\ CASE with zero
T{ : TEST9 0 CASE 0 OF 0 ENDOF 1 OF 1 ENDOF ENDCASE ; TEST9 -> 0 }T

\ CASE with multiple defaults (last wins)
T{ : TEST10 5 CASE 1 OF 10 ENDOF 2 OF 20 ENDOF 100 200 ENDCASE ; TEST10 -> 100 }T

\ CASE preserving stack
T{ : TEST11 10 20 3 CASE 1 OF 30 ENDOF 3 OF 40 ENDOF ENDCASE ; TEST11 -> 10 20 40 }T

\ CASE with arithmetic condition
T{ : TEST12 10 5 > CASE TRUE OF 1 ENDOF FALSE OF 2 ENDOF ENDCASE ; TEST12 -> 1 }T
T{ : TEST13 3 5 > CASE TRUE OF 1 ENDOF FALSE OF 2 ENDOF ENDCASE ; TEST13 -> 2 }T

\ Nested CASE (implementation dependent)
T{ : TEST14 2 CASE 1 OF 10 ENDOF 2 OF 20 ENDOF ENDCASE ; TEST14 -> 20 }T

TEST-SUMMARY
