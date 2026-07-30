\ Test 4.2: Character-level memory access
\ Verifies C@, C!, byte-level operations

INCLUDE test-harness.fs

CR ." === Test 4.2: Character Memory Access ==="

\ Character variable storage
CREATE CHAR-BUFFER 10 ALLOT

\ C! and C@ (store/fetch character)
T{ 65 CHAR-BUFFER C! CHAR-BUFFER C@ -> 65 }T
T{ 90 CHAR-BUFFER C! CHAR-BUFFER C@ -> 90 }T
T{ 0 CHAR-BUFFER C! CHAR-BUFFER C@ -> 0 }T
T{ 255 CHAR-BUFFER C! CHAR-BUFFER C@ -> 255 }T

\ Multiple character operations
T{ 65 CHAR-BUFFER C! 66 CHAR-BUFFER 1+ C! CHAR-BUFFER C@ CHAR-BUFFER 1+ C@ -> 65 66 }T

\ ASCII values
T{ 48 CHAR-BUFFER C! CHAR-BUFFER C@ -> 48 }T
T{ 122 CHAR-BUFFER C! CHAR-BUFFER C@ -> 122 }T

\ Zero in char memory
T{ 0 CHAR-BUFFER C! 0 CHAR-BUFFER 1+ C! CHAR-BUFFER C@ -> 0 }T

\ Byte sequence storage
T{ 72 CHAR-BUFFER C!
   105 CHAR-BUFFER 1+ C!
   CHAR-BUFFER C@ CHAR-BUFFER 1+ C@ -> 72 105 }T

\ C+! (increment character)
T{ 97 CHAR-BUFFER C! CHAR-BUFFER C@ -> 97 }T
T{ 1 CHAR-BUFFER C+! CHAR-BUFFER C@ -> 98 }T
T{ 2 CHAR-BUFFER C+! CHAR-BUFFER C@ -> 100 }T

\ High byte values
T{ 128 CHAR-BUFFER C! CHAR-BUFFER C@ -> 128 }T
T{ 200 CHAR-BUFFER C! CHAR-BUFFER C@ -> 200 }T

TEST-SUMMARY
