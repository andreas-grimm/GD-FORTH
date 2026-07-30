\ Test 7.3: Recursive word definitions
\ Verifies correct recursion handling and tail recursion

INCLUDE test-harness.fs

CR ." === Test 7.3: Recursion ==="

\ Simple countdown recursion
T{ : COUNTDOWN DUP 0 = IF ELSE 1- COUNTDOWN THEN ; 3 COUNTDOWN -> }T

\ Factorial (simple)
T{ : FACT DUP 1 = IF DROP 1 ELSE DUP 1- FACT * THEN ; 5 FACT -> 120 }T

\ Countdown with accumulation
T{ : SUMCOUNT DUP 0 = IF ELSE 1- SUMCOUNT + THEN ; 5 SUMCOUNT -> 15 }T

\ Tail recursion pattern
T{ : TAILCOUNT DUP 0 = IF ELSE 1- TAILCOUNT THEN ; 100 TAILCOUNT -> }T

\ Mutual recursion (A calls B, B calls A)
\ T{ : A DUP 0 = IF DROP 1 ELSE 1- B THEN ;
\    : B DUP 0 = IF DROP 2 ELSE 1- A THEN ;
\    5 A -> ... }T

\ Fibonacci (recursive)
T{ : FIB DUP 2 < IF ELSE DUP 1- FIB SWAP 2- FIB + THEN ; 7 FIB -> 13 }T

\ Power recursion
T{ : POWER DUP 0 = IF DROP 1 ELSE DUP 1- POWER SWAP * THEN ; 2 3 POWER -> 8 }T

\ String recursion (simulated with numbers)
T{ : COUNTDOWN2 DUP 0 = IF DROP 0 ELSE 1- COUNTDOWN2 1+ THEN ; 5 COUNTDOWN2 -> 5 }T

\ Palindrome check simulation
T{ : RECUR DUP 0 = IF ELSE 1- RECUR THEN ; 10 RECUR -> }T

\ GCD (Euclidean algorithm)
T{ : GCD DUP 0 = IF ELSE TUCK MOD GCD THEN ; 48 18 GCD -> 6 }T

\ Ackermann-like function
T{ : ACK DUP 0 = IF DROP 1 ELSE 1- ACK 2 * THEN ; 3 ACK -> 16 }T

\ Recursive multiplication
T{ : MULT DUP 0 = IF DROP 0 ELSE DUP 1- MULT ROT + THEN ; 3 4 MULT -> 12 }T

\ Binary search simulation (simplified)
T{ : BINSIM DUP 0 = IF ELSE 1- BINSIM THEN ; 7 BINSIM -> }T

TEST-SUMMARY
