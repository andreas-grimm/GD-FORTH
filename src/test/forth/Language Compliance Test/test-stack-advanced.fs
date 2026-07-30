\ Test 2.2: Advanced stack manipulation
\ Verifies -ROT, NIP, TUCK, 2DUP, 2DROP, 2OVER, 2SWAP

INCLUDE test-harness.fs

CR ." === Test 2.2: Advanced Stack Manipulation ==="

\ 2DUP (duplicate top 2 items)
T{ 3 4 2DUP -> 3 4 3 4 }T
T{ 1 2 2DUP DUP -> 1 2 1 2 1 }T

\ 2DROP (drop top 2 items)
T{ 1 2 2DROP -> }T
T{ 1 2 3 4 2DROP -> 1 2 }T

\ 2SWAP (swap top 4 items into 2 pairs)
T{ 1 2 3 4 2SWAP -> 3 4 1 2 }T

\ 2OVER (copy 3rd & 4th items)
T{ 1 2 3 4 2OVER -> 1 2 3 4 1 2 }T

\ -ROT (rotate opposite to ROT)
T{ 1 2 3 -ROT -> 3 1 2 }T
T{ 4 5 6 -ROT -> 6 4 5 }T

\ NIP (drop second item)
T{ 1 2 NIP -> 2 }T
T{ 3 4 NIP -> 4 }T
T{ 5 6 7 NIP -> 5 7 }T

\ TUCK (copy top to below second)
T{ 1 2 TUCK -> 2 1 2 }T
T{ 3 4 TUCK -> 4 3 4 }T

\ ?DUP (dup if not zero)
T{ 0 ?DUP -> 0 }T
T{ 5 ?DUP -> 5 5 }T
T{ -1 ?DUP -> -1 -1 }T

\ PICK (copy nth item from top)
T{ 1 2 3 1 PICK -> 1 2 3 2 }T
T{ 1 2 3 0 PICK -> 1 2 3 3 }T
T{ 1 2 3 2 PICK -> 1 2 3 1 }T

\ ROLL (move nth item to top)
T{ 1 2 3 1 ROLL -> 1 3 2 }T
T{ 1 2 3 2 ROLL -> 2 3 1 }T

\ Complex combinations
T{ 1 2 3 4 2SWAP 2OVER -> 3 4 1 2 3 4 }T
T{ 5 6 TUCK NIP -> 6 5 }T

TEST-SUMMARY
