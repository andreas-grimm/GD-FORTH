\ Test 7.1: Basic word definitions
\ Verifies : word definition, simple word creation and execution

INCLUDE test-harness.fs

CR ." === Test 7.1: Word Definitions ==="

\ Simple constant word
T{ : FIVE 5 ; FIVE -> 5 }T

\ Word with computation
T{ : DOUBLE 2 * ; 3 DOUBLE -> 6 }T

\ Word using other words
T{ : TRIPLE DUP DOUBLE + ; 4 TRIPLE -> 12 }T

\ Word with no inputs
T{ : HUNDRED 100 ; HUNDRED -> 100 }T

\ Word with string operations
T{ : ADDTEN 10 + ; 7 ADDTEN -> 17 }T

\ Word with multiple operations
T{ : ADDSQUARE DUP * + ; 3 4 ADDSQUARE -> 19 }T

\ Predefined word usage
T{ : NEGATE 0 SWAP - ; 5 NEGATE -> -5 }T

\ Chained word definitions
T{ : A 1 ; : B A 1 + ; B -> 2 }T

\ Word redefining stack
T{ : SWAP2 >R SWAP R> ; 1 2 3 SWAP2 -> 1 3 2 }T

\ Word with conditionals
T{ : ABS DUP 0 < IF NEGATE THEN ; -5 ABS -> 5 }T

\ Word using arithmetic
T{ : SQUARE DUP * ; 5 SQUARE -> 25 }T

\ Word accessing memory
T{ VARIABLE X : SETX X ! ; : GETX X @ ; 42 SETX GETX -> 42 }T

\ Forward reference (if supported)
\ T{ : A B ; : B 5 ; A -> 5 }T (may not work - forward refs)

\ Word in word definition
T{ : QUAD SQUARE SQUARE ; 2 QUAD -> 16 }T

TEST-SUMMARY
