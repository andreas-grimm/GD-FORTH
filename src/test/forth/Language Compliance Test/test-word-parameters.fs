\ Test 7.2: Word parameters and parameter passing
\ Verifies parameter passing, local variables if supported

INCLUDE test-harness.fs

CR ." === Test 7.2: Word Parameters ==="

\ Single parameter
T{ : ADDONE 1 + ; 5 ADDONE -> 6 }T

\ Two parameters
T{ : ADD2 + ; 3 4 ADD2 -> 7 }T

\ Three parameters
T{ : ADDTHREE + + ; 1 2 3 ADDTHREE -> 6 }T

\ Parameters with computation
T{ : TIMES3 3 * ; 7 TIMES3 -> 21 }T

\ Multiple independent operations
T{ : COMBO DUP + SWAP - ; 10 3 COMBO -> 17 }T

\ Parameter consumption and production
T{ : DOUBLE2 DUP ROT ROT * ; 2 3 DOUBLE2 -> 2 6 }T

\ Mixed parameter usage
T{ : MIXPARAM OVER 2 * ROT + ; 1 2 3 MIXPARAM -> 1 2 7 }T

\ Recursive parameter handling
T{ : ADDTO0 DUP 0 = IF DROP ELSE 1- + THEN ; 5 ADDTO0 -> 4 }T

\ Parameters in conditionals
T{ : IFPOS DUP 0 > IF DROP 1 ELSE DROP 0 THEN ; 5 IFPOS -> 1 }T

\ Parameter stack manipulation
T{ : SWAP2 >R SWAP R> ; 1 2 3 SWAP2 -> 1 3 2 }T

\ Variable parameters
T{ : STOREGET VARIABLE X X ! X @ ; 99 STOREGET -> 99 }T

\ Local variable simulation with return stack
T{ : SAVEVAL >R DUP + R> + ; 5 10 20 SAVEVAL -> 35 }T

\ Multiple word composition with parameters
T{ : ADDSQ DUP * + ; 3 4 ADDSQ -> 19 }T

\ Parameter passing through loops
T{ : SUMTO 0 SWAP 0 SWAP DO I + LOOP ; SUMTO 5 -> 10 }T

TEST-SUMMARY
