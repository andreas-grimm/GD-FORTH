\ Test 8.1: EMIT character output
\ Verifies character emission and output

INCLUDE test-harness.fs

CR ." === Test 8.1: EMIT Character Output ==="

\ Note: These tests emit characters to stdout
\ Output verification may require manual inspection or output redirection

\ Single character emission
T{ 65 EMIT -> }T  \ Outputs 'A'

\ Multiple characters
T{ 72 EMIT 105 EMIT -> }T  \ Outputs 'Hi'

\ Newline
T{ 10 EMIT -> }T  \ Newline

\ Digits
T{ 48 EMIT 49 EMIT 50 EMIT -> }T  \ Outputs '012'

\ Lowercase
T{ 97 EMIT 98 EMIT 99 EMIT -> }T  \ Outputs 'abc'

\ Special characters
T{ 33 EMIT -> }T  \ Exclamation mark
T{ 63 EMIT -> }T  \ Question mark
T{ 32 EMIT -> }T  \ Space

\ Control characters
T{ 9 EMIT -> }T   \ Tab
T{ 13 EMIT -> }T  \ Carriage return

\ ASCII values
T{ 0 EMIT -> }T   \ NUL (may not display)
T{ 127 EMIT -> }T \ DEL

\ Printing sequence
T{ 70 EMIT 111 EMIT 114 EMIT 116 EMIT 104 EMIT -> }T  \ 'Forth'

\ High ASCII (if supported)
T{ 200 EMIT -> }T
T{ 255 EMIT -> }T

\ Emit in loops
T{ : EMITLOOP 65 EMIT 66 EMIT 67 EMIT ; EMITLOOP -> }T

\ Emit from word
T{ : SAYHELLO 72 EMIT 105 EMIT ; SAYHELLO -> }T

TEST-SUMMARY
