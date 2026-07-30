\ Test 8.3: Dot notation and number printing
\ Verifies . and U. for number output

INCLUDE test-harness.fs

CR ." === Test 8.3: Number Printing (. and U.) ==="

\ Print integers
T{ 42 . -> }T

\ Print zero
T{ 0 . -> }T

\ Print negative
T{ -5 . -> }T

\ Print large numbers
T{ 999999 . -> }T

\ Print from word
T{ : PRINTFIVE 5 . ; PRINTFIVE -> }T

\ Multiple numbers
T{ 1 . 2 . 3 . -> }T

\ Unsigned print (U.)
T{ 42 U. -> }T

\ Unsigned with large value
T{ -1 U. -> }T  \ Should print as large unsigned

\ Unsigned zero
T{ 0 U. -> }T

\ Signed vs unsigned
T{ -100 . -> }T
T{ -100 U. -> }T  \ Different output

\ Stack depth and printing
T{ 1 2 3 DEPTH . -> }T

\ Print in conditionals
T{ TRUE IF 100 . THEN -> }T

\ Print in loops
T{ : PRINTCOUNT 3 0 DO I . LOOP ; PRINTCOUNT -> }T

\ Print with spaces (. adds space)
T{ 10 . 20 . -> }T

\ Nested output
T{ : NESTED ." Value: " 77 . ; NESTED -> }T

\ Print from arithmetic
T{ 5 3 + . -> }T

TEST-SUMMARY
