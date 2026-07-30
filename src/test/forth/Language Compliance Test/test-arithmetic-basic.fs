\ Test 3.1: Basic arithmetic operations
\ Verifies +, -, *, / with positive/negative numbers

INCLUDE test-harness.fs

CR ." === Test 3.1: Basic Arithmetic ==="

\ Addition tests
T{ 5 3 + -> 8 }T
T{ 0 0 + -> 0 }T
T{ -5 3 + -> -2 }T
T{ 5 -3 + -> 2 }T
T{ -5 -3 + -> -8 }T
T{ 10 20 + -> 30 }T
T{ 100 200 + -> 300 }T

\ Subtraction tests
T{ 5 3 - -> 2 }T
T{ 3 5 - -> -2 }T
T{ 0 0 - -> 0 }T
T{ 5 0 - -> 5 }T
T{ 0 5 - -> -5 }T
T{ -5 3 - -> -8 }T
T{ -5 -3 - -> -2 }T
T{ 10 7 - -> 3 }T

\ Multiplication tests
T{ 5 3 * -> 15 }T
T{ 0 5 * -> 0 }T
T{ 5 0 * -> 0 }T
T{ 1 7 * -> 7 }T
T{ 7 1 * -> 7 }T
T{ -5 3 * -> -15 }T
T{ 5 -3 * -> -15 }T
T{ -5 -3 * -> 15 }T
T{ 10 10 * -> 100 }T
T{ 12 12 * -> 144 }T

\ Division tests
T{ 20 4 / -> 5 }T
T{ 20 5 / -> 4 }T
T{ 15 3 / -> 5 }T
T{ 7 2 / -> 3 }T
T{ -20 4 / -> -5 }T
T{ 20 -4 / -> -5 }T
T{ -20 -4 / -> 5 }T
T{ 100 10 / -> 10 }T

\ Chained operations
T{ 10 5 + 3 * -> 45 }T
T{ 20 4 / 2 + -> 7 }T
T{ 5 3 * 2 - -> 13 }T
T{ 100 2 / 5 / -> 10 }T

\ 1+ and 1- (increment/decrement)
T{ 5 1+ -> 6 }T
T{ 0 1+ -> 1 }T
T{ -1 1+ -> 0 }T
T{ 5 1- -> 4 }T
T{ 0 1- -> -1 }T

TEST-SUMMARY
