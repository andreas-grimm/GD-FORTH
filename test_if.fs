\ IF/ELSE/THEN Test Program
\ Tests the new control flow implementation

CR ." ==== IF/THEN Control Flow Tests ===="
CR

\ Test 1: Simple IF with true condition
." Test 1: IF with TRUE condition" CR
1 IF
  ." Branch taken (expected)" CR
THEN
CR

\ Test 2: Simple IF with false condition
." Test 2: IF with FALSE condition (no output expected)" CR
0 IF
  ." Branch taken (unexpected!)" CR
THEN
." No branch taken (expected)" CR
CR

\ Test 3: IF/ELSE with true condition
." Test 3: IF/ELSE with TRUE" CR
1 IF
  ." True branch" CR
ELSE
  ." False branch" CR
THEN
CR

\ Test 4: IF/ELSE with false condition
." Test 4: IF/ELSE with FALSE" CR
0 IF
  ." True branch" CR
ELSE
  ." False branch" CR
THEN
CR

\ Test 5: Nested IF
." Test 5: Nested IF statements" CR
1 IF
  ." Outer true" CR
  1 IF
    ." Inner true" CR
  THEN
THEN
CR

\ Test 6: Nested IF with false inner
." Test 6: Nested IF with false inner" CR
1 IF
  ." Outer true" CR
  0 IF
    ." Inner true (unexpected)" CR
  ELSE
    ." Inner false" CR
  THEN
THEN
CR

\ Test 7: Comparison with IF
." Test 7: Comparison operators with IF" CR
5 3 > IF
  ." 5 > 3 is true" CR
ELSE
  ." 5 > 3 is false" CR
THEN
CR

\ Test 8: Arithmetic in condition
." Test 8: Arithmetic in condition" CR
10 5 - 3 > IF
  ." 10 - 5 = 5, 5 > 3 is true" CR
ELSE
  ." Condition false" CR
THEN
CR

\ Test 9: Multiple statements in branch
." Test 9: Multiple statements in branch" CR
1 IF
  42 .
  ." (should be 42)" CR
  99 .
  ." (should be 99)" CR
THEN
CR

\ Test 10: Stack operations in branches
." Test 10: Stack operations in branches" CR
5 1 IF
  100 +
  .
  ." (should be 105)" CR
THEN
CR

." ==== IF Tests Complete ====" CR
