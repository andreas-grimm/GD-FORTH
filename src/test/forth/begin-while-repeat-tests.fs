\ BEGIN WHILE REPEAT - Indefinite Loop Tests
\ Tests for the BEGIN...WHILE...REPEAT control structure

\ Test 1: Simple countdown loop
: TEST-COUNTDOWN
  CR
  ." Test 1: Simple Countdown (5 to 1)" CR
  5 BEGIN DUP . 1 - DUP 0> WHILE REPEAT DROP CR
;

\ Test 2: Loop with condition check
: TEST-CONDITION-LOOP
  CR
  ." Test 2: Loop with Condition" CR
  0 BEGIN
    1 + DUP . DUP 5 < WHILE
  REPEAT DROP CR
;

\ Test 3: Using a flag variable
: TEST-FLAG-VARIABLE
  CR
  ." Test 3: Flag Variable Loop" CR
  VARIABLE flag
  0 flag !
  BEGIN
    flag @ 1 + DUP flag ! DUP . DUP 3 < WHILE
  REPEAT DROP CR
;

\ Test 4: Nested indefinite loops
: TEST-NESTED-INDEFINITE
  CR
  ." Test 4: Nested Indefinite Loops" CR
  2 BEGIN
    DUP . ." : " 3 BEGIN 1 - DUP . DUP 0> WHILE REPEAT DROP CR
    1 - DUP 0> WHILE
  REPEAT DROP CR
;

\ Test 5: Empty loop (condition immediately false)
: TEST-EMPTY-LOOP
  CR
  ." Test 5: Empty Loop (no iterations)" CR
  0 BEGIN ." Body runs" WHILE REPEAT
  ." Loop exited without running body" CR
;

\ Execute all tests
CR ." ============================================" CR
." BEGIN...WHILE...REPEAT Test Suite" CR
." ============================================" CR

TEST-COUNTDOWN
TEST-CONDITION-LOOP
TEST-FLAG-VARIABLE
TEST-NESTED-INDEFINITE
TEST-EMPTY-LOOP

CR ." ============================================" CR
." All tests completed" CR
." ============================================" CR
