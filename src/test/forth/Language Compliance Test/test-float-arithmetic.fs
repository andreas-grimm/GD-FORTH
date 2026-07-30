\ Test 3.5: Floating-point arithmetic
\ Verifies F+, F-, F*, F/, FABS, and other float operations

INCLUDE test-harness.fs

CR ." === Test 3.5: Floating-Point Arithmetic ==="

\ Note: These tests assume an ANS Forth implementation with floating point support
\ If not available, these will gracefully fail

SET-NEAR 0.0001

\ Floating point addition
T{ 1.5 2.5 F+ -> 4.0 }T
T{ 0.0 5.5 F+ -> 5.5 }T
T{ -2.5 2.5 F+ -> 0.0 }T
T{ 1.1 2.2 F+ -> 3.3 }T

\ Floating point subtraction
T{ 5.5 2.5 F- -> 3.0 }T
T{ 2.5 5.5 F- -> -3.0 }T
T{ 0.0 1.5 F- -> -1.5 }T
T{ 5.0 5.0 F- -> 0.0 }T

\ Floating point multiplication
T{ 2.5 2.0 F* -> 5.0 }T
T{ 3.0 3.0 F* -> 9.0 }T
T{ 0.0 100.0 F* -> 0.0 }T
T{ -2.0 3.0 F* -> -6.0 }T
T{ 1.5 2.0 F* -> 3.0 }T

\ Floating point division
T{ 10.0 2.0 F/ -> 5.0 }T
T{ 9.0 3.0 F/ -> 3.0 }T
T{ 1.0 2.0 F/ -> 0.5 }T
T{ 100.0 10.0 F/ -> 10.0 }T

\ Absolute value (FABS)
T{ 5.5 FABS -> 5.5 }T
T{ -5.5 FABS -> 5.5 }T
T{ 0.0 FABS -> 0.0 }T
T{ -1.1 FABS -> 1.1 }T

\ FNEGATE
T{ 5.5 FNEGATE -> -5.5 }T
T{ -5.5 FNEGATE -> 5.5 }T
T{ 0.0 FNEGATE -> 0.0 }T

\ Floating point comparisons
T{ 5.0 5.0 F= -> -1 }T
T{ 5.0 3.0 F= -> 0 }T
T{ 3.0 5.0 F< -> -1 }T
T{ 5.0 3.0 F< -> 0 }T
T{ 5.0 3.0 F> -> -1 }T
T{ 3.0 5.0 F> -> 0 }T

\ INT and FINT conversion
T{ 3.7 FINT F>D D>F -> 3.0 }T
T{ 5.2 INT -> 5 }T

\ FSQRT (square root)
T{ 4.0 FSQRT -> 2.0 }T
T{ 9.0 FSQRT -> 3.0 }T
T{ 1.0 FSQRT -> 1.0 }T

\ CEIL and FLOOR
T{ 3.2 CEIL -> 4.0 }T
T{ 3.8 FLOOR -> 3.0 }T
T{ 3.0 CEIL -> 3.0 }T

TEST-SUMMARY
