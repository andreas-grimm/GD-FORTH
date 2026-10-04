\ BEGIN...UNTIL, BEGIN...AGAIN, LEAVE Tests
\ Tests for indefinite loops and early loop exit

\ Test 1: BEGIN...UNTIL
: TEST-UNTIL
  CR
  ." Test 1: BEGIN...UNTIL - do-while loop" CR
  VARIABLE x
  0 x !
  BEGIN x @ 1 + DUP x ! DUP . DUP 3 > UNTIL DROP CR
;

\ Test 2: BEGIN...AGAIN with DO...LOOP LEAVE
: TEST-AGAIN-WITH-LEAVE
  CR
  ." Test 2: BEGIN...AGAIN with LEAVE from DO" CR
  0 10 DO
    BEGIN
      ." >" EMIT
      I 5 = IF LEAVE THEN
    AGAIN
  LOOP CR
;

\ Test 3: LEAVE in nested DO
: TEST-LEAVE-NESTED
  CR
  ." Test 3: LEAVE in nested DO" CR
  0 3 DO
    0 5 DO
      I 2 = IF LEAVE THEN
      I . SPACE
    LOOP
    CR
  LOOP
;

\ Test 4: Multiple conditions with LEAVE
: TEST-LEAVE-MULTIPLE
  CR
  ." Test 4: Multiple LEAVE conditions" CR
  0 100 DO
    I 10 = IF LEAVE THEN
    I 5 = IF CR THEN
    I . SPACE
  LOOP CR
;

\ Execute all tests
CR ." ====================================" CR
." BEGIN...UNTIL, AGAIN, LEAVE Tests" CR
." ====================================" CR

TEST-UNTIL
TEST-AGAIN-WITH-LEAVE
TEST-LEAVE-NESTED
TEST-LEAVE-MULTIPLE

CR ." ====================================" CR
." All tests completed" CR
." ====================================" CR
