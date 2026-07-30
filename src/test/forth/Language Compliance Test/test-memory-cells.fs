\ Test 4.1: Memory cell operations
\ Verifies @, !, cell-based memory read/write

INCLUDE test-harness.fs

CR ." === Test 4.1: Memory Cell Operations ==="

\ Create temporary variable storage
VARIABLE TEST-VAR
VARIABLE TEST-VAR2
VARIABLE TEST-VAR3

\ @ (fetch) and ! (store) operations
T{ 42 TEST-VAR ! TEST-VAR @ -> 42 }T
T{ 100 TEST-VAR ! TEST-VAR @ -> 100 }T
T{ -5 TEST-VAR ! TEST-VAR @ -> -5 }T
T{ 0 TEST-VAR ! TEST-VAR @ -> 0 }T

\ Multiple variables
T{ 10 TEST-VAR ! 20 TEST-VAR2 ! TEST-VAR @ TEST-VAR2 @ -> 10 20 }T
T{ 99 TEST-VAR ! 88 TEST-VAR2 ! 77 TEST-VAR3 ! TEST-VAR @ TEST-VAR2 @ TEST-VAR3 @ -> 99 88 77 }T

\ Overwriting values
T{ 5 TEST-VAR ! 10 TEST-VAR ! TEST-VAR @ -> 10 }T

\ Large values
T{ 2147483647 TEST-VAR ! TEST-VAR @ -> 2147483647 }T
T{ -2147483648 TEST-VAR ! TEST-VAR @ -> -2147483648 }T

\ Zero values
T{ 0 TEST-VAR ! 0 TEST-VAR2 ! TEST-VAR @ TEST-VAR2 @ + -> 0 }T

\ +! (increment cell)
T{ TEST-VAR @ -> 0 }T
T{ 5 TEST-VAR +! TEST-VAR @ -> 5 }T
T{ 3 TEST-VAR +! TEST-VAR @ -> 8 }T
T{ -2 TEST-VAR +! TEST-VAR @ -> 6 }T

\ ? (shorthand for @ followed by .)
T{ 42 TEST-VAR ! TEST-VAR @ -> 42 }T

\ Direct address operations
T{ HERE -> }T
T{ 123 HERE ! HERE @ -> 123 }T

\ Fetch indirect
T{ TEST-VAR >R R@ @ R> DROP -> 6 }T

TEST-SUMMARY
