\ Test 8.4: Output buffering and sequencing
\ Verifies output order and FLUSH operations

INCLUDE test-harness.fs

CR ." === Test 8.4: I/O Buffering and Sequencing ==="

\ Output sequence verification
T{ 1 . 2 . 3 . -> }T  \ Should output 1 2 3

\ String and number interleaved
T{ ." Start " 42 . ." End" -> }T

\ Multiple emit calls
T{ 72 EMIT 105 EMIT 33 EMIT -> }T  \ Hi!

\ Output in sequence from word
T{ : SEQ ." A" 1 . ." B" 2 . ." C" ; SEQ -> }T

\ Output order in conditionals
T{ 1 IF ." True" THEN ." After" -> }T
T{ 0 IF ." True" ELSE ." False" THEN -> }T

\ Output in loops maintains order
T{ 0 3 DO ." [" I . ." ]" LOOP -> }T

\ Nested output
T{ ." Outer " : INNER ." Inner" ; INNER ." Done" -> }T

\ FLUSH (if supported)
\ T{ ." Buffered" FLUSH ." After Flush" -> }T

\ Output before error (may not complete)
\ T{ ." Message" 1 0 / -> }T

\ Multiple output types in sequence
T{ ." Text" CR 99 . CR ." More" -> }T

\ Output with return stack operations
T{ 5 >R ." Value: " R> . -> }T

\ Output from arithmetic result
T{ ." Sum: " 10 20 + . -> }T

\ Output stability (same result each time)
T{ ." Stable" -> }T
T{ ." Stable" -> }T

TEST-SUMMARY
