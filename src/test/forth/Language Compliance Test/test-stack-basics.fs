\ Test 2.1: Basic stack manipulation
\ Verifies DUP, DROP, OVER, SWAP, ROT

INCLUDE test-harness.fs

CR ." === Test 2.1: Basic Stack Manipulation ==="

\ DUP tests
T{ 5 DUP -> 5 5 }T
T{ 0 DUP -> 0 0 }T
T{ -3 DUP -> -3 -3 }T
T{ 1 2 DUP -> 1 2 2 }T

\ DROP tests
T{ 5 DROP -> }T
T{ 1 2 DROP -> 1 }T
T{ 1 2 3 DROP -> 1 2 }T

\ SWAP tests
T{ 1 2 SWAP -> 2 1 }T
T{ 3 4 SWAP -> 4 3 }T
T{ 0 -1 SWAP -> -1 0 }T
T{ 1 2 3 SWAP -> 1 3 2 }T

\ OVER tests
T{ 1 2 OVER -> 1 2 1 }T
T{ 3 4 OVER -> 3 4 3 }T
T{ 0 1 OVER -> 0 1 0 }T
T{ 1 2 3 OVER -> 1 2 3 2 }T

\ ROT tests (rotate top 3 items)
T{ 1 2 3 ROT -> 2 3 1 }T
T{ 4 5 6 ROT -> 5 6 4 }T
T{ 0 1 2 ROT -> 1 2 0 }T

\ Combined operations
T{ 5 DUP DUP -> 5 5 5 }T
T{ 1 2 SWAP DUP -> 2 1 1 }T
T{ 3 4 OVER OVER -> 3 4 3 4 }T
T{ 1 2 3 ROT ROT -> 1 2 3 }T

\ Stack preservation through operations
T{ 10 20 SWAP DROP -> 20 }T
T{ 5 6 7 ROT DROP -> 6 7 }T

TEST-SUMMARY
