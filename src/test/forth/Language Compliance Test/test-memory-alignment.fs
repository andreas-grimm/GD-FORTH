\ Test 4.3: Memory alignment operations
\ Verifies ALIGNED, ALIGN, proper alignment handling

INCLUDE test-harness.fs

CR ." === Test 4.3: Memory Alignment ==="

\ ALIGNED (align address to cell boundary)
T{ 1 ALIGNED 4 = -> -1 }T
T{ 2 ALIGNED 4 = -> -1 }T
T{ 3 ALIGNED 4 = -> -1 }T
T{ 4 ALIGNED 4 = -> -1 }T
T{ 5 ALIGNED 8 = -> -1 }T

\ ALIGN (advance HERE to aligned boundary)
T{ HERE ALIGNED DUP HERE = -> -1 }T

\ Cell alignment checks
T{ 0 ALIGNED 0 = -> -1 }T
T{ 4 ALIGNED 4 = -> -1 }T
T{ 8 ALIGNED 8 = -> -1 }T

\ Char alignment (most systems allow any address)
CREATE CHAR-TEST 1 ALLOT
T{ CHAR-TEST C@ CHAR-TEST C@ = -> -1 }T

\ Mixed char/cell access with alignment
CREATE MIXED-BUFFER 8 ALLOT
T{ 65 MIXED-BUFFER C! 66 MIXED-BUFFER 1+ C! MIXED-BUFFER C@ -> 65 }T

\ Aligned cell storage in buffer
T{ 12345 MIXED-BUFFER ! MIXED-BUFFER @ -> 12345 }T

\ Alignment of HERE
T{ HERE 4 MOD 0 = -> -1 }T

\ CELL (size of a cell)
T{ 1 CELLS 4 = -> -1 }T
T{ 10 CELLS CELLS 1 = -> -1 }T

\ CHARS (size of a character - usually 1)
T{ 1 CHARS 1 = -> -1 }T
T{ 4 CHARS 4 = -> -1 }T

TEST-SUMMARY
