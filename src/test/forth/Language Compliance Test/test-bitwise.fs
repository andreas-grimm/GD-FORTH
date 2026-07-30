\ Test 1.3: Bitwise operations
\ Verifies correct bit manipulation results

INCLUDE test-harness.fs

CR ." === Test 1.3: Bitwise Operations ==="

\ Left shift operations
T{ 1 1 LSHIFT -> 2 }T
T{ 1 2 LSHIFT -> 4 }T
T{ 1 3 LSHIFT -> 8 }T
T{ 1 4 LSHIFT -> 16 }T
T{ 1 8 LSHIFT -> 256 }T
T{ 2 3 LSHIFT -> 16 }T
T{ 3 2 LSHIFT -> 12 }T

\ Right shift operations
T{ 8 1 RSHIFT -> 4 }T
T{ 16 2 RSHIFT -> 4 }T
T{ 256 4 RSHIFT -> 16 }T
T{ 4 1 RSHIFT -> 2 }T
T{ 2 1 RSHIFT -> 1 }T

\ Bitwise AND
T{ 15 7 AND -> 7 }T
T{ 12 10 AND -> 8 }T
T{ 255 15 AND -> 15 }T
T{ 0 -1 AND -> 0 }T
T{ -1 -1 AND -> -1 }T

\ Bitwise OR
T{ 8 4 OR -> 12 }T
T{ 1 2 OR -> 3 }T
T{ 4 2 OR -> 6 }T
T{ 0 5 OR -> 5 }T

\ Bitwise XOR
T{ 15 7 XOR -> 8 }T
T{ 12 10 XOR -> 6 }T
T{ 255 0 XOR -> 255 }T
T{ -1 0 XOR -> -1 }T

\ Combined bit operations
T{ 5 3 LSHIFT 2 RSHIFT -> 10 }T
T{ 255 8 LSHIFT 8 RSHIFT -> 255 }T

TEST-SUMMARY
