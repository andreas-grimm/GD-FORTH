\ Test 4.5: Memory fill and initialization operations
\ Verifies FILL, ERASE, memory initialization patterns

INCLUDE test-harness.fs

CR ." === Test 4.5: Memory Fill Operations ==="

CREATE FILL-BUFFER 20 ALLOT

\ FILL (fill memory with byte value)
T{ FILL-BUFFER 4 0 FILL FILL-BUFFER @ 0 = -> -1 }T
T{ FILL-BUFFER 4 255 FILL FILL-BUFFER C@ 255 = -> -1 }T
T{ FILL-BUFFER 4 65 FILL FILL-BUFFER C@ 65 = -> -1 }T

\ Fill with non-zero value
T{ FILL-BUFFER 8 42 FILL FILL-BUFFER C@ 42 = -> -1 }T
T{ FILL-BUFFER 8 42 FILL FILL-BUFFER 1+ C@ 42 = -> -1 }T

\ ERASE (fill with zeros - ANS Forth)
T{ FILL-BUFFER 8 ERASE FILL-BUFFER @ 0 = -> -1 }T
T{ FILL-BUFFER 8 ERASE FILL-BUFFER 4 + @ 0 = -> -1 }T

\ Fill partial buffer
T{ FILL-BUFFER 4 99 FILL FILL-BUFFER 8 + 4 99 FILL
   FILL-BUFFER C@ 99 = FILL-BUFFER 8 + C@ 99 = AND -> -1 }T

\ Zero fill patterns
T{ FILL-BUFFER 10 0 FILL
   FILL-BUFFER @ FILL-BUFFER 4 + @ + 0 = -> -1 }T

\ Single byte fill
T{ FILL-BUFFER 1 77 FILL FILL-BUFFER C@ -> 77 }T

\ Large fill
T{ FILL-BUFFER 20 200 FILL FILL-BUFFER C@ -> 200 }T

\ Fill creates uniform pattern
T{ FILL-BUFFER 4 123 FILL FILL-BUFFER C@ FILL-BUFFER 1+ C@ = -> -1 }T

\ Post-fill verification
T{ FILL-BUFFER 3 88 FILL FILL-BUFFER C@ 88 = FILL-BUFFER 1+ C@ 88 = AND -> -1 }T

\ Mixed fill operations
T{ FILL-BUFFER 4 0 FILL FILL-BUFFER 4 + 4 255 FILL
   FILL-BUFFER @ 0 = FILL-BUFFER 4 + @ 255 <> AND -> 0 }T

TEST-SUMMARY
