\ Test 1.4: Comparison operators
\ Verifies correct boolean results from comparisons

INCLUDE test-harness.fs

CR ." === Test 1.4: Comparison Operators ==="

\ Equality tests
T{ 5 5 = -> -1 }T
T{ 5 3 = -> 0 }T
T{ 0 0 = -> -1 }T
T{ -1 -1 = -> -1 }T
T{ -5 5 = -> 0 }T

\ Less than tests
T{ 3 5 < -> -1 }T
T{ 5 3 < -> 0 }T
T{ 5 5 < -> 0 }T
T{ -5 0 < -> -1 }T
T{ 0 -5 < -> 0 }T
T{ -10 -5 < -> -1 }T

\ Greater than tests
T{ 5 3 > -> -1 }T
T{ 3 5 > -> 0 }T
T{ 5 5 > -> 0 }T
T{ 0 -5 > -> -1 }T
T{ -5 0 > -> 0 }T

\ Less than or equal
T{ 3 5 <= -> -1 }T
T{ 5 3 <= -> 0 }T
T{ 5 5 <= -> -1 }T
T{ -10 -5 <= -> -1 }T
T{ 0 0 <= -> -1 }T

\ Greater than or equal
T{ 5 3 >= -> -1 }T
T{ 3 5 >= -> 0 }T
T{ 5 5 >= -> -1 }T
T{ 0 0 >= -> -1 }T

\ Zero comparisons
T{ 0 0= -> -1 }T
T{ 5 0= -> 0 }T
T{ -5 0= -> 0 }T

T{ -5 0< -> -1 }T
T{ 0 0< -> 0 }T
T{ 5 0< -> 0 }T

T{ 5 0> -> -1 }T
T{ 0 0> -> 0 }T
T{ -5 0> -> 0 }T

\ Unsigned comparisons
T{ 5 3 U< -> 0 }T
T{ 3 5 U< -> -1 }T
T{ 0 -1 U< -> -1 }T

TEST-SUMMARY
