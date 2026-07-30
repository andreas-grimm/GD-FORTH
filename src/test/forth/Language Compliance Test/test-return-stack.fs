\ Test 2.5: Return stack operations
\ Verifies correct data movement between stacks

INCLUDE test-harness.fs

CR ." === Test 2.5: Return Stack Operations ==="

\ >R (push to return stack)
T{ 5 >R R> -> 5 }T
T{ 10 >R 20 >R R> R> -> 20 10 }T

\ R@ (read from return stack, non-destructive)
T{ 7 >R R@ R> -> 7 7 }T
T{ 3 >R 4 >R R@ R@ R> R> -> 4 3 4 3 }T

\ RDROP (drop from return stack)
T{ 5 >R RDROP -> }T
T{ 1 >R 2 >R RDROP R> -> 1 }T

\ 2>R (push 2 items to return stack)
T{ 5 6 2>R 2R> -> 5 6 }T

\ Return stack isolation
T{ 10 >R 20 DUP R> + -> 20 30 }T
T{ 5 >R 6 7 R> + -> 6 7 12 }T

\ Nested return stack usage
T{ 1 >R 2 >R 3 >R R> R> R> -> 3 2 1 }T

\ Return stack with complex operations
T{ 100 >R 200 300 + R> -> 200 500 }T

\ >R with arithmetic
T{ 10 5 >R + R> -> 15 5 }T
T{ 20 >R 30 >R R@ R@ * R> R> -> 900 30 20 }T

TEST-SUMMARY
