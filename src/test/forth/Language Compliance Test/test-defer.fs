\ Test 7.5: DEFER and deferred execution
\ Verifies dynamic word execution and IS changes

INCLUDE test-harness.fs

CR ." === Test 7.5: DEFER and Deferred Execution ==="

\ Basic DEFER usage
T{ DEFER TESTWORD : IMPL1 100 ; TESTWORD IS IMPL1 TESTWORD -> 100 }T

\ Change deferred behavior
T{ DEFER DYNAMIC : FIRST 1 ; DYNAMIC IS FIRST DYNAMIC -> 1 }T
T{ : SECOND 2 ; DYNAMIC IS SECOND DYNAMIC -> 2 }T

\ DEFER with different implementation
T{ DEFER ADD2 : ADDONE 1 + ; : ADDTWO 2 + ; 5 TESTWORD -> }T

\ DEFER initially undefined (graceful failure)
\ T{ DEFER UNDEF UNDEF -> ERROR }T

\ Deferred arithmetic
T{ DEFER MATH : MATHPLUS + ; MATH IS MATHPLUS 3 4 MATH -> 7 }T
T{ : MATHTIMES * ; MATH IS MATHTIMES 3 4 MATH -> 12 }T

\ Multiple DEFER words
T{ DEFER OP1 DEFER OP2 : A + ; : B * ; OP1 IS A OP2 IS B 5 3 OP1 3 OP2 -> 8 3 }T

\ DEFER in word definition
T{ DEFER DISPATCH : DISPATCH_CALL DISPATCH ; : IMPL 88 ; DISPATCH IS IMPL DISPATCH_CALL -> 88 }T

\ Switch behaviors
T{ DEFER MODE : MODE_A 1 ; : MODE_B 2 ; : MODE_C 3 ;
   MODE IS MODE_A MODE -> 1 }T
T{ MODE IS MODE_B MODE -> 2 }T
T{ MODE IS MODE_C MODE -> 3 }T

\ DEFER with conditional logic
T{ DEFER CHECK : CHECKA TRUE ; : CHECKB FALSE ; CHECK IS CHECKA CHECK IF 10 THEN -> 10 }T
T{ CHECK IS CHECKB CHECK IF 10 THEN 20 ; -> 20 }T

\ DEFER preserving stack
T{ DEFER TRANSFORM : T1 2 * ; 5 TRANSFORM -> 10 }T
T{ : T2 3 * ; TRANSFORM IS T2 5 TRANSFORM -> 15 }T

\ Recursive via DEFER
T{ DEFER RECURSE : COUNTDOWN DUP 0 = IF ELSE 1- RECURSE THEN ;
   RECURSE IS COUNTDOWN 3 RECURSE -> }T

TEST-SUMMARY
