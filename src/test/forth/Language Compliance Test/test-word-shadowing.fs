\ Test 7.4: Word shadowing and redefinition
\ Verifies redefining words, shadowing, scope behavior

INCLUDE test-harness.fs

CR ." === Test 7.4: Word Shadowing and Redefinition ==="

\ Simple redefinition
T{ : TEST 5 ; TEST -> 5 }T
T{ : TEST 10 ; TEST -> 10 }T

\ Redefine to different behavior
T{ : DOUBLE 2 * ; 4 DOUBLE -> 8 }T
T{ : DOUBLE 3 * ; 4 DOUBLE -> 12 }T

\ Shadowing built-in-like words
T{ : MYDUP DUP ; 5 MYDUP -> 5 5 }T
T{ : MYDUP DROP ; 5 MYDUP -> }T

\ Using old definition before shadow
T{ : A 10 ; : B A 5 + ; B -> 15 }T
T{ : A 20 ; B -> 25 }T

\ Nested definitions with shadowing
T{ : X 1 ; : Y X 2 + ; Y -> 3 }T
T{ : X 100 ; Y -> 102 }T

\ Multiple redefinitions
T{ : Z 7 ; Z -> 7 }T
T{ : Z 8 ; Z -> 8 }T
T{ : Z 9 ; Z -> 9 }T

\ Shadowing affects new uses only
T{ : ORIG 42 ; : CALLER ORIG ; CALLER -> 42 }T
T{ : ORIG 100 ; CALLER -> 100 }T

\ Word using another before shadow
T{ : GETVAL 5 ; : COMPUTE GETVAL 10 * ; COMPUTE -> 50 }T
T{ : GETVAL 2 ; COMPUTE -> 20 }T

\ Complex shadowing chain
T{ : A 1 ; : B A ; : C B ; C -> 1 }T
T{ : A 2 ; C -> 2 }T
T{ : B 100 ; C -> 100 }T

\ Shadowing with recursion
T{ : REC DUP 0 = IF DROP 0 ELSE 1- REC 1+ THEN ; 3 REC -> 3 }T
T{ : REC DUP 0 = IF DROP 1 ELSE 1- REC 2 * THEN ; 3 REC -> 8 }T

\ Variable shadowing (namespace)
T{ VARIABLE VAR : SETVAR VAR ! ; : GETVAR VAR @ ; 99 SETVAR GETVAR -> 99 }T

\ Shadowing in same word family
T{ : MODIFY DUP * ; 3 MODIFY -> 9 }T
T{ : MODIFY 100 ; 3 MODIFY -> 100 }T

TEST-SUMMARY
