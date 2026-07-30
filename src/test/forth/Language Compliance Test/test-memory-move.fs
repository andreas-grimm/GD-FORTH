\ Test 4.4: Memory move and block copy operations
\ Verifies MOVE, CMOVE, block copy, overlapping handling

INCLUDE test-harness.fs

CR ." === Test 4.4: Memory Move Operations ==="

CREATE SRC-BUFFER 20 ALLOT
CREATE DST-BUFFER 20 ALLOT

\ Initialize source with values
T{ 100 SRC-BUFFER ! 200 SRC-BUFFER 4 + ! -> }T

\ MOVE (copy byte-for-byte, handles overlap)
T{ SRC-BUFFER DST-BUFFER 8 MOVE DST-BUFFER @ -> 100 }T
T{ SRC-BUFFER DST-BUFFER 8 MOVE DST-BUFFER 4 + @ -> 200 }T

\ CMOVE (character move, doesn't handle forward overlap)
T{ SRC-BUFFER DST-BUFFER 4 CMOVE DST-BUFFER C@ -> 100 }T

\ CMOVE> (character move backwards for overlapping)
T{ SRC-BUFFER DST-BUFFER 4 CMOVE> DST-BUFFER C@ -> 100 }T

\ Copy single cell
T{ 42 SRC-BUFFER ! SRC-BUFFER DST-BUFFER 4 MOVE DST-BUFFER @ -> 42 }T

\ Copy multiple cells
T{ 1 SRC-BUFFER ! 2 SRC-BUFFER 4 + ! 3 SRC-BUFFER 8 + !
   SRC-BUFFER DST-BUFFER 12 MOVE
   DST-BUFFER @ DST-BUFFER 4 + @ DST-BUFFER 8 + @ -> 1 2 3 }T

\ Copy with offset
T{ 999 SRC-BUFFER !
   SRC-BUFFER DST-BUFFER 4 4 + MOVE
   DST-BUFFER 4 + @ -> 999 }T

\ Zero-length move
T{ SRC-BUFFER DST-BUFFER 0 MOVE DST-BUFFER @ -> 0 }T

\ MOVE with exact size
T{ 77 SRC-BUFFER C! SRC-BUFFER DST-BUFFER 1 MOVE DST-BUFFER C@ -> 77 }T

\ Fill pattern before move
T{ 1 DST-BUFFER 20 FILL 55 SRC-BUFFER ! SRC-BUFFER DST-BUFFER 4 MOVE DST-BUFFER @ -> 55 }T

TEST-SUMMARY
