\ Forth Test Harness - Standard test framework
\ Based on forth-standard.org/standard/testsuite

VARIABLE TESTS-RUN
VARIABLE TESTS-PASSED
VARIABLE TESTS-FAILED
VARIABLE VERBOSE
VARIABLE SET-EXACT
VARIABLE SET-NEAR

0 TESTS-RUN !
0 TESTS-PASSED !
0 TESTS-FAILED !
1 VERBOSE !
0 SET-EXACT !
1 SET-NEAR !

\ Error handling
VARIABLE ERROR-XT
0 ERROR-XT !

: HANDLE-ERROR ( n -- )
   ERROR-XT @ ?DUP IF EXECUTE ELSE THROW THEN ;

\ Test macros
: T{
   DEPTH >R
;

: ->
   DEPTH R@ = IF
      R> DROP
   ELSE
      R> DROP
      CR ." Stack depth mismatch: expected " . ." got " DEPTH .
      1 TESTS-FAILED +!
      ABORT
   THEN
;

: }T
   TESTS-RUN @ 1+ TESTS-RUN !
   TESTS-PASSED @ 1+ TESTS-PASSED !
;

\ Floating-point tolerance support
: SET-EXACT ( -- )
   0 SET-EXACT ! 1 SET-NEAR ! ;

: SET-NEAR ( tol -- )
   1 SET-EXACT ! SET-NEAR ! ;

: F-WITHIN? ( f1 f2 -- flag )
   SET-EXACT @ IF
      F=
   ELSE
      SET-NEAR @ F* FABS FROT FROT F- FABS F< IF TRUE ELSE FALSE THEN
   THEN
;

\ Test reporting
: TEST-SUMMARY ( -- )
   CR CR ." ===== TEST SUMMARY ====="
   CR ." Total: " TESTS-RUN @ .
   CR ." Passed: " TESTS-PASSED @ .
   CR ." Failed: " TESTS-FAILED @ .
   TESTS-FAILED @ 0= IF
      CR ." All tests passed!"
   ELSE
      CR ." Some tests failed!"
   THEN
   CR
;

: ASSERT ( flag -- )
   IF
      1 TESTS-PASSED +!
   ELSE
      CR ." Test failed!"
      1 TESTS-FAILED +!
   THEN
   1 TESTS-RUN +!
;
