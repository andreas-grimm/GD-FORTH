\ Test 3.3: Double-number and wide arithmetic
\ Verifies 64-bit operations and wide intermediate results

INCLUDE test-harness.fs

CR ." === Test 3.3: Wide Number Arithmetic ==="

\ M* (multiply returning double precision)
T{ 100 200 M* -> 20000 }T
T{ 1000 1000 M* -> 1000000 }T
T{ 2147483647 2 M* -> 4294967294 }T

\ D+ (double precision addition)
T{ 0 0 0 0 D+ -> 0 0 }T
T{ 1 0 1 0 D+ -> 2 0 }T
T{ 0 1 1 0 D+ -> 1 1 }T

\ D- (double precision subtraction)
T{ 2 0 1 0 D- -> 1 0 }T
T{ 1 0 1 0 D- -> 0 0 }T
T{ 0 0 1 0 D- -> -1 -1 }T

\ D* (double precision multiply)
T{ 100 0 200 0 D* -> 20000 0 }T
T{ 1000 0 1000 0 D* -> 1000000 0 }T

\ UM* (unsigned multiply)
T{ 50000 50000 UM* -> 2500000000 }T

\ */ (multiply then divide, preserving intermediate precision)
T{ 10 5 2 */ -> 25 }T
T{ 100 3 4 */ -> 75 }T
T{ 7 8 2 */ -> 28 }T

\ 2* and 2/ (shift operations)
T{ 5 2* -> 10 }T
T{ 20 2/ -> 10 }T
T{ 3 2* -> 6 }T
T{ 7 2/ -> 3 }T
T{ -10 2* -> -20 }T
T{ -10 2/ -> -5 }T

\ Overflow handling (implementation specific)
T{ 2147483647 1+ -> -2147483648 }T

\ ABS (absolute value)
T{ 5 ABS -> 5 }T
T{ -5 ABS -> 5 }T
T{ 0 ABS -> 0 }T
T{ -2147483648 ABS -> -2147483648 }T

TEST-SUMMARY
