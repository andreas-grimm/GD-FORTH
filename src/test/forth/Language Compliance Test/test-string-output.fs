\ Test 8.2: String output and ." notation
\ Verifies string printing and formatted output

INCLUDE test-harness.fs

CR ." === Test 8.2: String Output ==="

\ Simple string output
T{ ." Hello" -> }T

\ String with spaces
T{ ." Hello World" -> }T

\ Multiple strings
T{ ." Part1" ." Part2" -> }T

\ String with newline embedded
T{ ." Line 1" CR ." Line 2" -> }T

\ Empty string
T{ ." " -> }T

\ String with special characters
T{ ." !@#$%^&*()" -> }T

\ String with numbers
T{ ." Test123" -> }T

\ String in word definition
T{ : GREET ." Hello, Forth!" ; GREET -> }T

\ Conditional string output
T{ : SHOUT TRUE IF ." YES!" THEN ; SHOUT -> }T

\ String in loop
T{ : REPEAT3TIMES 3 0 DO ." Hi " LOOP ; REPEAT3TIMES -> }T

\ String concatenation (output)
T{ ." First " ." Second " ." Third" -> }T

\ String with tab
T{ ." Col1" 9 EMIT ." Col2" -> }T

\ Escape sequences (if supported)
T{ ." Quote: " ." Test" -> }T

\ String length-related (TYPE word)
\ CREATE STR ." Test" 4 TYPE

\ Numbers and strings mixed
T{ 42 . ." is the answer" -> }T

TEST-SUMMARY
