\ Test 1.2: Boolean logic operations
\ Verifies flag representations and logical operations

INCLUDE test-harness.fs

CR ." === Test 1.2: Boolean Logic ==="

T{ TRUE -> -1 }T
T{ FALSE -> 0 }T

\ NOT operations
T{ TRUE NOT -> 0 }T
T{ FALSE NOT -> -1 }T
T{ 0 NOT -> -1 }T
T{ -1 NOT -> 0 }T
T{ 1 NOT -> -1 }T

\ AND operations
T{ TRUE TRUE AND -> -1 }T
T{ TRUE FALSE AND -> 0 }T
T{ FALSE TRUE AND -> 0 }T
T{ FALSE FALSE AND -> 0 }T
T{ -1 -1 AND -> -1 }T
T{ 0 0 AND -> 0 }T

\ OR operations
T{ TRUE TRUE OR -> -1 }T
T{ TRUE FALSE OR -> -1 }T
T{ FALSE TRUE OR -> -1 }T
T{ FALSE FALSE OR -> 0 }T
T{ -1 -1 OR -> -1 }T
T{ 0 0 OR -> 0 }T
T{ -1 0 OR -> -1 }T

\ XOR operations
T{ TRUE TRUE XOR -> 0 }T
T{ TRUE FALSE XOR -> -1 }T
T{ FALSE TRUE XOR -> -1 }T
T{ FALSE FALSE XOR -> 0 }T
T{ -1 -1 XOR -> 0 }T
T{ 0 0 XOR -> 0 }T
T{ -1 0 XOR -> -1 }T

\ Complex boolean expressions
T{ 1 2 AND 3 4 OR AND -> 0 }T
T{ 1 2 OR 3 4 AND OR -> -1 }T

TEST-SUMMARY
