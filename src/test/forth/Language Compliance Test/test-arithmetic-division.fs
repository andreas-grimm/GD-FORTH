\ Test 3.2: Division modes and remainder operations
\ Verifies floored vs symmetric division, MOD, /MOD

INCLUDE test-harness.fs

CR ." === Test 3.2: Division Modes and Remainder ==="

\ MOD operation (remainder after floored division)
T{ 20 3 MOD -> 2 }T
T{ 20 7 MOD -> 6 }T
T{ 7 2 MOD -> 1 }T
T{ 0 5 MOD -> 0 }T
T{ 10 10 MOD -> 0 }T
T{ -20 3 MOD -> 1 }T
T{ 20 -3 MOD -> -1 }T
T{ -20 -3 MOD -> -2 }T

\ /MOD operation (quotient and remainder)
T{ 20 3 /MOD -> 2 6 }T
T{ 20 7 /MOD -> 6 2 }T
T{ 7 2 /MOD -> 1 3 }T
T{ 100 10 /MOD -> 10 0 }T
T{ 15 4 /MOD -> 3 3 }T

\ FM/MOD (floored modulo - Forth standard)
T{ 20 3 FM/MOD -> 2 6 }T
T{ -20 3 FM/MOD -> 1 -20 }T

\ SM/REM (symmetric remainder - alternative)
T{ 20 3 SM/REM -> 2 6 }T
T{ -20 3 SM/REM -> -2 -14 }T

\ M* (double number multiply)
T{ 20 30 M* -> 600 }T
T{ -20 30 M* -> -600 }T

\ U/MOD (unsigned division/modulo)
T{ 20 3 U/MOD -> 2 6 }T
T{ 100 7 U/MOD -> 6 2 }T

\ Division with zero behavior (implementation specific)
\ T{ 20 0 / -> ERROR }T

\ Integer division truncation
T{ 7 2 / -> 3 }T
T{ 9 2 / -> 4 }T
T{ 10 3 / -> 3 }T

\ Remainder sign handling
T{ 17 5 MOD -> 2 }T
T{ -17 5 MOD -> 3 }T
T{ 17 -5 MOD -> -3 }T
T{ -17 -5 MOD -> -2 }T

TEST-SUMMARY
