\ Test 2.3: Stack depth checking and manipulation
\ Verifies DEPTH and stack state at various points

INCLUDE test-harness.fs

CR ." === Test 2.3: Stack Depth Checking ==="

\ DEPTH returns number of items on stack
T{ DEPTH -> 0 }T
T{ 1 DEPTH -> 1 1 }T
T{ 1 2 DEPTH -> 1 2 2 }T
T{ 1 2 3 DEPTH -> 1 2 3 3 }T

\ DEPTH after DROP
T{ 1 2 DROP DEPTH -> 1 1 }T
T{ 1 2 3 DROP DEPTH -> 1 2 2 }T

\ Multiple depth checks
T{ DEPTH >R DEPTH R> = -> -1 }T
T{ 1 DEPTH >R DEPTH R> = -> 1 -1 }T

\ Complex depth scenarios
T{ 10 20 30 DEPTH 3 = -> 10 20 30 -1 }T
T{ 5 6 7 8 9 DEPTH 5 = -> 5 6 7 8 9 -1 }T

\ DEPTH with duplicates
T{ 1 DUP DUP DEPTH -> 1 1 1 3 }T

\ DEPTH in sequences
T{ 1 DEPTH 2 DEPTH + -> 1 3 }T

TEST-SUMMARY
