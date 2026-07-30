\ Test 1.1: Number parsing and representation
\ Verifies correct stack values after parsing numbers

INCLUDE test-harness.fs

CR ." === Test 1.1: Number Parsing and Representation ==="

T{ 0 -> 0 }T
T{ 1 -> 1 }T
T{ -1 -> -1 }T
T{ 42 -> 42 }T
T{ -42 -> -42 }T
T{ 999999 -> 999999 }T
T{ -999999 -> -999999 }T
T{ 2147483647 -> 2147483647 }T
T{ -2147483648 -> -2147483648 }T

\ Mixed operations with parsed numbers
T{ 10 20 + -> 30 }T
T{ 100 5 - -> 95 }T
T{ 7 8 * -> 56 }T
T{ 20 4 / -> 5 }T

\ Zero edge cases
T{ 0 0 + -> 0 }T
T{ 0 5 + -> 5 }T
T{ 5 0 + -> 5 }T
T{ 0 0 * -> 0 }T
T{ 0 5 * -> 0 }T

\ Negative number operations
T{ -5 3 + -> -2 }T
T{ -10 -5 + -> -15 }T
T{ -3 -4 * -> 12 }T

\ Large number representation
T{ 65536 -> 65536 }T
T{ -65536 -> -65536 }T
T{ 16777216 -> 16777216 }T

TEST-SUMMARY
