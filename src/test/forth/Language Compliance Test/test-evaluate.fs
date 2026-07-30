\ Test 9.1: EVALUATE runtime interpretation
\ Verifies EVALUATE and parsing strings as code

INCLUDE test-harness.fs

CR ." === Test 9.1: EVALUATE (Runtime Interpretation) ==="

\ Simple numeric evaluation
T{ S" 42" EVALUATE -> 42 }T

\ Arithmetic expression
T{ S" 10 20 +" EVALUATE -> 30 }T

\ Multiple operations
T{ S" 5 3 - 2 *" EVALUATE -> 4 }T

\ Stack manipulation in string
T{ S" 1 2 SWAP" EVALUATE -> 2 1 }T

\ Word execution via string
T{ : TESTWORD 99 ; S" TESTWORD" EVALUATE -> 99 }T

\ Complex expression
T{ S" 100 10 / 5 +" EVALUATE -> 15 }T

\ Nested evaluation (if supported)
\ T{ S" S\" 5 3 +\" EVALUATE EVALUATE" EVALUATE -> 8 }T

\ Evaluation with variables
T{ VARIABLE VAR S" 55" EVALUATE VAR ! S" VAR @" EVALUATE -> 55 }T

\ Conditional via EVALUATE
T{ S" TRUE IF 111 THEN" EVALUATE -> 111 }T
T{ S" FALSE IF 111 ELSE 222 THEN" EVALUATE -> 222 }T

\ Loop simulation (may not work in all implementations)
T{ S" 0 3 DO I LOOP" EVALUATE -> 0 1 2 }T

\ String parsing and interpretation
T{ S" 7 DUP *" EVALUATE -> 49 }T

\ Multiple evaluations in sequence
T{ S" 10" EVALUATE S" 20" EVALUATE + -> 30 }T

\ Error handling (may throw)
\ T{ S" UNDEFINED_WORD" EVALUATE -> ERROR }T

TEST-SUMMARY
