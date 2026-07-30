\ Test 6.6: UNLOOP and abnormal exit
\ Verifies stack cleanup on abnormal loop exit

INCLUDE test-harness.fs

CR ." === Test 6.6: UNLOOP and Abnormal Exit ==="

\ Basic UNLOOP usage
T{ : TEST1 0 5 DO I 3 = IF UNLOOP THEN LOOP 99 ; TEST1 -> 0 1 2 99 }T

\ UNLOOP with nested loops
T{ : TEST2 0 3 DO 0 3 DO I 1 = IF UNLOOP THEN I LOOP LOOP 99 ; TEST2 -> 0 0 1 0 0 1 0 0 1 99 }T

\ UNLOOP cleans return stack for abnormal exit
T{ : TEST3 0 5 DO I 2 = IF UNLOOP EXIT THEN I LOOP 88 ; TEST3 -> 0 1 }T

\ UNLOOP in nested allows outer to continue
T{ : TEST4 0 2 DO 0 5 DO I 2 = IF UNLOOP THEN I LOOP I LOOP ; TEST4 -> 0 1 0 1 1 0 1 }T

\ Multiple UNLOOP in nested structure
T{ : TEST5 0 2 DO 0 2 DO I 1 = IF UNLOOP THEN LOOP I LOOP ; TEST5 -> 0 0 1 0 }T

\ UNLOOP then continue
T{ : TEST6 0 10 DO I 5 = IF UNLOOP THEN I 2 +LOOP ; TEST6 -> 0 2 4 }T

\ UNLOOP with explicit return
T{ : TEST7 0 10 DO I 5 = IF UNLOOP 999 EXIT THEN I LOOP ; TEST7 -> 0 1 2 3 4 999 }T

\ Stack preservation through UNLOOP
T{ : TEST8 100 0 5 DO I 3 = IF UNLOOP THEN LOOP ; TEST8 -> 100 0 1 2 }T

\ Nested UNLOOP with different exit points
T{ : TEST9 0 3 DO 0 3 DO I 2 = IF UNLOOP ELSE I THEN LOOP LOOP ; TEST9 -> 0 0 1 0 0 1 0 0 1 }T

\ UNLOOP in middle of sequence
T{ : TEST10 0 10 DO I 4 = IF UNLOOP 777 THEN I LOOP 888 ; TEST10 -> 0 1 2 3 777 888 }T

\ Return stack integrity after UNLOOP
T{ : TEST11 100 >R 0 10 DO I 3 = IF UNLOOP THEN LOOP R> ; TEST11 -> 0 1 2 100 }T

\ UNLOOP prevents stack corruption on exit
T{ : TEST12 10 20 0 5 DO 30 I 3 = IF UNLOOP THEN LOOP ; TEST12 -> 10 20 30 0 1 2 }T

TEST-SUMMARY
