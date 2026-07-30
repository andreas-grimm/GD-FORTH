\ Test 3.4: Arithmetic edge cases and error handling
\ Verifies proper handling of division by zero, overflow, etc.

INCLUDE test-harness.fs

CR ." === Test 3.4: Arithmetic Edge Cases ==="

\ Zero multiplication edge cases
T{ 0 0 * -> 0 }T
T{ 0 -1 * -> 0 }T
T{ -1 0 * -> 0 }T
T{ 0 2147483647 * -> 0 }T

\ Zero addition/subtraction
T{ 0 0 + -> 0 }T
T{ 0 5 + -> 5 }T
T{ 5 0 + -> 5 }T
T{ 0 0 - -> 0 }T
T{ 0 5 - -> -5 }T

\ Negative zero (if applicable)
T{ 0 -1 * -> 0 }T

\ Divide by 1
T{ 5 1 / -> 5 }T
T{ -5 1 / -> -5 }T
T{ 0 1 / -> 0 }T

\ Divide into 1
T{ 1 1 / -> 1 }T
T{ 1 2 / -> 0 }T

\ Self operations
T{ 5 5 - -> 0 }T
T{ 5 5 / -> 1 }T
T{ 5 5 MOD -> 0 }T
T{ 5 5 /MOD -> 0 0 }T

\ Identity operations
T{ 10 0 + -> 10 }T
T{ 10 1 * -> 10 }T
T{ 10 10 / -> 1 }T

\ Max/Min integer values
T{ 2147483647 1 + -> -2147483648 }T
T{ -2147483648 1 - -> 2147483647 }T

\ Commutative operations
T{ 5 3 + 3 5 + = -> -1 }T
T{ 10 2 * 2 10 * = -> -1 }T

\ Non-commutative operations
T{ 5 3 - 3 5 - + -> 0 }T
T{ 10 5 / 5 10 / <> -> -1 }T

\ Operator precedence verification
T{ 2 3 + 5 * -> 25 }T
T{ 2 3 * 6 + -> 12 }T

TEST-SUMMARY
