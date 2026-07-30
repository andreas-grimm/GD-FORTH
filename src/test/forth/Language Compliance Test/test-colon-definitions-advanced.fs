\ Test 9.4: Advanced colon definitions and compilation
\ Verifies immediate words and compile/interpret mode behavior

INCLUDE test-harness.fs

CR ." === Test 9.4: Advanced Colon Definitions ==="

\ Basic colon definition (already tested, but confirm)
T{ : BASIC 42 ; BASIC -> 42 }T

\ IMMEDIATE word (executed during compilation)
T{ : IMMED IMMEDIATE 100 ; -> }T

\ State checking (compilation vs interpretation)
T{ : CHECKSTATE STATE @ ; CHECKSTATE 0 <> -> -1 }T

\ Compile-only words behavior
\ : BRACKET [ ; \ Typical bracket behavior

\ Literal compilation
T{ : WITHLIT 10 [LITERAL] LITERAL ; -> }T

\ Postpone (if available)
\ T{ : DEFERRED POSTPONE DROP ; DEFERRED 99 -> }T

\ Tick compilation
T{ : GETTICK ['] DROP ; GETTICK DROP -> }T

\ Interpretation mode behavior
T{ : MODE1 STATE @ 0 = IF 1 ELSE 0 THEN ; MODE1 -> 1 }T

\ Definition before use (forward reference may not work)
\ T{ : USES FORWARD ; : FORWARD 77 ; USES -> 77 }T

\ Compile behavior
T{ : COMPILER ." Compiling" ; COMPILER -> }T

\ Nest colon definitions
T{ : L1 10 ; : L2 L1 20 + ; L2 -> 30 }T

\ Multiple nesting
T{ : M1 1 ; : M2 M1 2 + ; : M3 M2 3 + ; M3 -> 6 }T

\ Recursion in colon definition (already tested)
T{ : REC DUP 0 = IF DROP ELSE 1- REC THEN ; 5 REC -> }T

\ Conditional word definition (runtime)
T{ 1 IF : MAYBE1 50 THEN ; MAYBE1 -> 50 }T

\ Word analysis
T{ : ANALYZE DUP . ; 7 ANALYZE -> 7 }T

\ Compilation affects later code
T{ : SETS VARIABLE X ; X 42 SWAP ! SWAP @ -> 42 }T

TEST-SUMMARY
