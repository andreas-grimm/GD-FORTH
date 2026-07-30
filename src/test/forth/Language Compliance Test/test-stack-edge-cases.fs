\ Test 2.4: Stack edge cases and error handling
\ Verifies graceful handling of edge cases

INCLUDE test-harness.fs

CR ." === Test 2.4: Stack Edge Cases ==="

\ Single item edge cases
T{ 5 DROP -> }T
T{ -1 DUP DROP -> -1 }T
T{ 0 SWAP DROP -> }T

\ Operations on edge values
T{ 0 0 SWAP -> 0 0 }T
T{ -1 -1 OVER -> -1 -1 -1 }T
T{ 1 0 ROT -> 0 1 1 }T

\ Zero handling in stack ops
T{ 0 DUP -> 0 0 }T
T{ 0 0 SWAP -> 0 0 }T
T{ 1 0 SWAP -> 0 1 }T

\ Large values
T{ 2147483647 DUP -> 2147483647 2147483647 }T
T{ -2147483648 SWAP -> -2147483648 -2147483648 }T

\ ?DUP edge cases
T{ 0 ?DUP -> 0 }T
T{ 1 ?DUP -> 1 1 }T
T{ -5 ?DUP DROP -> -5 }T

\ Empty stack marker (if supported)
\ Most implementations don't track empty stack specially

\ Depth limiting scenarios
T{ 1 2 3 4 5 6 7 8 9 10 DEPTH -> 1 2 3 4 5 6 7 8 9 10 10 }T
T{ 1 2 3 4 5 6 7 8 9 10 DROP DROP DROP DEPTH -> 1 2 3 4 5 6 7 7 }T

\ Stack persistence
T{ 42 DUP DUP DUP DEPTH -> 42 42 42 42 4 }T

TEST-SUMMARY
