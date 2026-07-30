\ Test 9.3: Exception handling with CATCH/THROW
\ Verifies exception propagation and error handling

INCLUDE test-harness.fs

CR ." === Test 9.3: Exception Handling (CATCH/THROW) ==="

\ Simple CATCH/THROW
T{ : THROWER 123 THROW ; ['] THROWER CATCH -> 123 }T

\ No exception in CATCH
T{ ['] DROP CATCH -> 0 }T

\ THROW with code value
T{ : TEST1 999 THROW ; ['] TEST1 CATCH -> 999 }T

\ Caught exception doesn't propagate
T{ : SAFECALL ['] THROWER CATCH DROP 100 ; SAFECALL -> 100 }T

\ Nested CATCH/THROW
T{ : OUTER ['] THROWER CATCH ; OUTER -> 123 }T

\ Exception prevents normal execution
T{ : BARRIER 1 999 THROW 2 ; ['] BARRIER CATCH -> 999 }T

\ Non-zero exception code (non-zero = exception)
T{ ['] DROP CATCH 0 = -> -1 }T

\ CATCH with result
T{ : WORKS 42 ; ['] WORKS CATCH 0 = -> -1 }T

\ Multiple catch levels
T{ : INNER THROW ; : OUTER 999 ['] INNER CATCH ; ['] OUTER CATCH -> 999 }T

\ Exception clears partial stack
T{ : PARTIAL 1 2 999 THROW ; ['] PARTIAL CATCH 1 = -> -1 }T

\ THROW codes must be non-zero (negative typically)
T{ : NEG -1 THROW ; ['] NEG CATCH -> -1 }T
T{ : NEG2 -999 THROW ; ['] NEG2 CATCH -> -999 }T

\ Conditional throw
T{ : MAYB 5 DUP 10 > IF 42 THROW THEN ; ['] MAYB CATCH -> 0 }T
T{ : MAYB 15 DUP 10 > IF 42 THROW THEN ; ['] MAYB CATCH -> 42 }T

\ State preservation
T{ 100 ['] THROWER CATCH 200 + -> 300 }T

TEST-SUMMARY
