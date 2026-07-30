\ Test 1.5: Number base conversions and radix operations
\ Verifies correct number interpretation in different bases

INCLUDE test-harness.fs

CR ." === Test 1.5: Number Base and Radix ==="

\ Decimal base (default)
T{ DECIMAL 10 -> 10 }T
T{ DECIMAL 255 -> 255 }T
T{ DECIMAL 16 -> 16 }T

\ Hexadecimal representation (pre-parsed)
T{ HEX FF -> 255 }T
T{ HEX 10 -> 16 }T
T{ HEX 100 -> 256 }T
T{ HEX DEADBEEF -> -559038737 }T

\ Back to decimal
T{ DECIMAL 255 -> 255 }T
T{ DECIMAL 256 -> 256 }T

\ Binary representation
T{ BIN 1010 -> 10 }T
T{ BIN 1111 -> 15 }T
T{ BIN 10000000 -> 128 }T

\ Return to decimal (default)
T{ DECIMAL 99 -> 99 }T

\ BASE variable checks
T{ BASE @ 10 = -> -1 }T

\ Octal representation
T{ OCT 10 -> 8 }T
T{ OCT 77 -> 63 }T
T{ OCT 100 -> 64 }T

\ Base 36 (alphanumeric)
T{ BASE ! 36 BASE @ 36 = -> -1 }T

TEST-SUMMARY
