\ Test 2.6: Stack load patterns and complex scenarios
\ Verifies real-world stack manipulation patterns

INCLUDE test-harness.fs

CR ." === Test 2.6: Stack Load Patterns ==="

\ Pattern: push multiple, manipulate
T{ 1 2 3 4 5 DEPTH -> 1 2 3 4 5 5 }T

\ Pattern: accumulate and rearrange
T{ 10 20 30 ROT -> 20 30 10 }T
T{ 10 20 30 40 2ROT -> 10 30 40 20 }T

\ Pattern: save/restore via return stack
T{ 5 10 >R 20 R> + -> 5 30 }T

\ Pattern: nested duplicates
T{ 5 DUP DUP + DUP * -> 5 10 100 }T

\ Pattern: complex shuffle
T{ 1 2 3 4 SWAP ROT OVER DROP -> 1 4 3 2 }T

\ Pattern: double-width stack simulation
T{ 1 2 3 4 2OVER 2SWAP -> 1 2 3 4 1 2 }T

\ Pattern: filtering with conditionals
T{ 5 10 DUP 0> IF ELSE DROP THEN -> 5 10 }T

\ Pattern: loop setup (prepare for DO/LOOP)
T{ 10 0 -> 10 0 }T
T{ 20 1 10 -> 20 1 10 }T

\ Pattern: multiple stack depths
T{ 1 DEPTH SWAP DEPTH SWAP -> 1 1 1 }T

\ Pattern: cleanup (reverse of setup)
T{ 1 2 3 4 5 DROP DROP DROP DROP -> 1 }T

\ Pattern: stack as temporary storage
T{ 100 >R 200 >R 300 R> + R> + -> 600 }T

\ Pattern: parameter passing equivalent
T{ 5 10 DUP ROT + SWAP / -> 10 3 }T

TEST-SUMMARY
