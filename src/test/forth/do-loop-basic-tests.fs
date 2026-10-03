(*
  do-loop-basic-tests.fs

  Basic test suite for DO...LOOP functionality in GD-FORTH.
  These tests verify fundamental loop behavior.

  (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
*)

(* Test 1: Simple loop from 0 to 4 *)
(* Expected output: 0 1 2 3 4 *)
: test-simple-loop
  CR
  .S
  ." Test 1: Simple loop 0 to 4" CR
  0 5 DO I . LOOP
  CR ;

(* Test 2: Loop with custom start and end *)
(* Expected output: 10 11 12 13 14 *)
: test-custom-range
  CR
  ." Test 2: Loop from 10 to 14" CR
  10 15 DO I . LOOP
  CR ;

(* Test 3: Count iterations *)
(* Should count 7 iterations from 0 to 6 *)
: test-iteration-count
  CR
  ." Test 3: Counting 7 iterations (0 to 6)" CR
  0 7 DO 1 LOOP
  CR
  ." Completed 7 iterations" CR ;

(* Test 4: Loop with addition inside *)
(* Each I value is doubled *)
: test-arithmetic-in-loop
  CR
  ." Test 4: Doubling each index (0 to 4)" CR
  0 5 DO I I + . LOOP
  CR ;

(* Test 5: Backwards loop (should not execute) *)
(* Expected: no output *)
: test-backwards-loop
  CR
  ." Test 5: Backwards loop (5 to 3, should be empty)" CR
  5 3 DO I . LOOP
  ." (No iterations)" CR ;

(* Test 6: Same start and end (should not execute) *)
: test-same-bounds
  CR
  ." Test 6: Same start and end (5 to 5, should be empty)" CR
  5 5 DO I . LOOP
  ." (No iterations)" CR ;

(* Test 7: Nested loops *)
(* Outer: 0-2, Inner: 0-1 *)
: test-nested-loops
  CR
  ." Test 7: Nested loops (outer: 0-2, inner: 0-1)" CR
  0 3 DO
    0 2 DO
      ." (" I . ." ," J . ." )" SPACE
    LOOP
  LOOP
  CR ;

(* Test 8: Loop with +LOOP (step by 2) *)
(* Expected: 0 2 4 6 8 *)
: test-plus-loop
  CR
  ." Test 8: Loop with step by 2 (0 to 9, step 2)" CR
  0 10 DO I . 2 +LOOP
  CR ;

(* Test 9: +LOOP with step 3 *)
(* Expected: 0 3 6 9 *)
: test-plus-loop-step3
  CR
  ." Test 9: Loop with step by 3 (0 to 10, step 3)" CR
  0 10 DO I . 3 +LOOP
  CR ;

(* Test 10: Single iteration *)
(* Expected: 0 *)
: test-single-iteration
  CR
  ." Test 10: Single iteration loop (0 to 1)" CR
  0 1 DO I . LOOP
  CR ;

(* Run all tests *)
: run-all-tests
  CR
  ." ========================================" CR
  ." GD-FORTH DO...LOOP Test Suite" CR
  ." ========================================" CR
  test-simple-loop
  test-custom-range
  test-iteration-count
  test-arithmetic-in-loop
  test-backwards-loop
  test-same-bounds
  test-nested-loops
  test-plus-loop
  test-plus-loop-step3
  test-single-iteration
  CR
  ." ========================================" CR
  ." All tests completed" CR
  ." ========================================" CR ;

run-all-tests
