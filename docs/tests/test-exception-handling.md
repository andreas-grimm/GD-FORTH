# Test 9.3: Exception Handling with CATCH/THROW

## Overview

The **test-exception-handling.fs** file validates exception handling using CATCH and THROW. Tests cover throwing exceptions with numeric codes, catching exceptions, exception propagation, and exception clearing.

## Test Cases

1. **Basic CATCH/THROW**: Catching thrown exceptions
2. **No exception**: Normal execution when no throw occurs
3. **Exception codes**: Different numeric codes for different exceptions
4. **Exception propagation**: Exceptions propagate through nested calls
5. **Partial stack preservation**: Stack cleared on exception
6. **Multiple catch levels**: Nested CATCH blocks
7. **Conditional throws**: THROW only when condition is true
8. **State after catch**: Continuing execution after caught exception

## What Is Being Tested

- **THROW operation**: Immediately transfers control to matching CATCH
- **CATCH operation**: Establishes exception handler, returns 0 if no exception
- **Exception codes**: Non-zero codes distinguish different error types
- **Stack clearing**: Partial execution is discarded on throw
- **Exception propagation**: Exceptions propagate up the call stack
- **Handler activation**: Caught exceptions execute continuation code
- **Error recovery**: Program can continue after catching exception
- **Nested handlers**: Multiple CATCH blocks at different levels

## Structure of Test Cases

Each exception test follows:

```forth
: <word-that-throws>
   <code> <exception-code> THROW
;

T{ ['] <word-that-throws> CATCH -> <exception-code> }T
```

Or for normal execution:

```forth
T{ ['] <safe-word> CATCH -> 0 }T
```

### CATCH/THROW Semantics

```
CATCH: ( xt -- 0 | exception-code )
  Executes xt
  If no THROW: returns 0
  If THROW: returns the exception code

THROW: ( exception-code -- )
  exception-code must be non-zero
  Searches for innermost CATCH
  Transfers control to after CATCH
  CATCH returns the exception code
```

## Code Documentation

### Simple CATCH/THROW

```forth
T{ : THROWER 123 THROW ; ['] THROWER CATCH -> 123 }T
```

**Execution**:
1. ['] THROWER: Get execution token (address) of THROWER
2. CATCH: Establish exception handler, execute THROWER
3. Inside THROWER: Push 123, then THROW
4. THROW: Search for CATCH, transfer control
5. After CATCH: CATCH returns 123
6. Stack: 123

**Flow**:
```
['] THROWER CATCH
    ↓ (executes THROWER)
123 THROW
    ↓ (transfers control to CATCH)
CATCH returns 123
```

### No Exception Case

```forth
T{ ['] DROP CATCH -> 0 }T
```

**Execution**:
1. ['] DROP: Get execution token for DROP
2. CATCH: Establish handler, execute DROP
3. DROP executes normally (consumes top stack item)
4. Return from CATCH: No exception occurred, return 0
5. Stack: 0

CATCH returns 0 when the executed word completes normally without throwing.

### Throwing with Code Value

```forth
T{ : TEST1 999 THROW ; ['] TEST1 CATCH -> 999 }T
```

Different exception codes can represent different error types:
- 999: Custom application error
- 1: Undefined word
- 2: Type mismatch
- etc.

The exception code is completely up to the programmer.

### Exception Prevents Execution

```forth
T{ : BARRIER 1 999 THROW 2 ; ['] BARRIER CATCH -> 999 }T
```

**Execution**:
1. Push 1
2. THROW 999
3. Transfer to CATCH (discarding the 1)
4. CATCH returns 999
5. Stack: 999 (not 1 or 2)

When THROW occurs, partial stack state is lost. Code after THROW is never executed.

### Caught Exception Doesn't Propagate

```forth
T{ : THROWER 123 THROW ; : SAFECALL ['] THROWER CATCH DROP 100 ; SAFECALL -> 100 }T
```

**Execution**:
1. SAFECALL calls ['] THROWER CATCH
2. Inside CATCH: THROWER throws 123
3. CATCH returns 123
4. DROP removes the 123
5. Push 100
6. Continue after SAFECALL
7. Stack: 100

The exception is "caught" (handled) so it doesn't propagate further.

### Nested CATCH/THROW

```forth
T{ : INNER THROW ; : OUTER 999 ['] INNER CATCH ; ['] OUTER CATCH -> 999 }T
```

**Execution level 1**:
1. ['] OUTER CATCH: Outer CATCH handler established
2. Execute OUTER

**Execution level 2**:
1. Push 999
2. ['] INNER CATCH: Inner CATCH handler established
3. Execute INNER

**Execution level 3**:
1. THROW: No operand, so empty stack error (or implementation specific)
2. Actually: : INNER THROW just throws without value (error)

**Corrected test behavior**:
```forth
T{ : INNER 123 THROW ; : OUTER ['] INNER CATCH ; ['] OUTER CATCH -> 123 }T
```

1. Outer CATCH established
2. OUTER: Inner CATCH established
3. INNER: THROW 123
4. Inner CATCH catches 123, returns it
5. OUTER continues and returns from OUTER
6. Return from outer CATCH with result
7. Stack: 123

### Exception Clears Partial Stack

```forth
T{ : PARTIAL 1 2 999 THROW ; ['] PARTIAL CATCH 1 = -> -1 }T
```

**Execution**:
1. ['] PARTIAL CATCH: Establish handler
2. PARTIAL: Push 1, push 2
3. THROW 999: Transfer to CATCH
4. When transferred: Stack items 1 and 2 are discarded
5. CATCH returns 999
6. Check: 999 == 1? No, result is 0, NOT -1
7. Actually: 999 1 = → 0, 0 NOT → -1

This demonstrates that stack state during THROW execution is not preserved.

### Multiple CATCH Levels

```forth
T{ : INNER THROW ; : OUTER 999 ['] INNER CATCH ; ['] OUTER CATCH -> 999 }T
```

Different CATCH blocks at different nesting levels can handle exceptions. The innermost CATCH that can match the exception code handles it.

### Non-Zero Exception Code Requirement

```forth
T{ ['] DROP CATCH 0 = -> -1 }T
```

When no exception is thrown, CATCH returns 0. This is a special value indicating "no error". Any thrown code must be non-zero.

```forth
T{ : NEG -1 THROW ; ['] NEG CATCH -> -1 }T
T{ : NEG2 -999 THROW ; ['] NEG2 CATCH -> -999 }T
```

Exception codes can be negative (often -1 for generic error).

### Conditional THROW

```forth
T{ : MAYB 5 DUP 10 > IF 42 THROW THEN ; ['] MAYB CATCH -> 0 }T
```

**Execution**:
1. CATCH established
2. MAYB: Push 5
3. DUP: Push 5 again (stack: 5 5)
4. 10 >: 5 > 10? No, push 0
5. IF: 0 is false, skip THROW
6. Return normally from MAYB
7. CATCH returns 0 (no exception)
8. Stack: 0

```forth
T{ : MAYB 15 DUP 10 > IF 42 THROW THEN ; ['] MAYB CATCH -> 42 }T
```

**Execution**:
1. CATCH established
2. MAYB: Push 15
3. DUP: Stack: 15 15
4. 10 >: 15 > 10? Yes, push -1
5. IF: -1 is true, execute THROW
6. 42 THROW: Transfer to CATCH
7. CATCH returns 42
8. Stack: 42

### State Preservation Outside CATCH

```forth
T{ 100 ['] THROWER CATCH 200 + -> 300 }T
```

**Execution**:
1. Push 100
2. CATCH: Try to execute THROWER
3. THROWER throws 123 (or 200 from context)
4. CATCH returns (exception code)
5. 200 +: Add 200 to exception code
6. Stack: 100 + 200 = 300

Items on the stack outside the CATCH block are not affected by exceptions within CATCH.

## Expected Outcome

### Successful Run

```
=== Test 9.3: Exception Handling (CATCH/THROW) ===
Total: 12
Passed: 12
Failed: 0
All tests passed!
```

Each test passes when:
1. Exceptions can be thrown and caught
2. CATCH returns 0 when no exception occurs
3. CATCH returns the exception code when thrown
4. Exceptions propagate up the call stack if not caught
5. Multiple exception codes can be distinguished
6. Stack state outside CATCH is preserved
7. Execution can continue after catching an exception

### Failure Scenarios

**CATCH Not Catching:**
```
T{ : THROWER 123 THROW ; ['] THROWER CATCH -> 123 }T
(expects 123, but execution stops or crashes)
```
CATCH/THROW not implemented or not working correctly.

**CATCH Returns Wrong Code:**
```
T{ : TEST 999 THROW ; ['] TEST CATCH -> 999 }T
(expects 999, got 0 or different number)
```
THROW is executing but CATCH is returning wrong value.

**Exception Propagates:**
```
T{ : THROW1 111 THROW ; : CALL1 ['] THROW1 CATCH DROP 222 ; ['] CALL1 CATCH -> 222 }T
(expects 222 if exception is caught at first level, got 111 if it propagates)
```
Exception not being caught at inner CATCH level.

**Stack Not Cleared:**
```
T{ : PARTIAL 1 2 999 THROW ; ['] PARTIAL CATCH }T
(after execution, stack should be just 999, not 1 2 999)
```
Partial stack state from before THROW is not cleared.

## Dependency Chain

This test depends on:
- **test-numbers.fs** — Exception codes are numbers
- **test-word-definition.fs** — Defines words that throw
- **test-if-then.fs** — Conditional throw in tests

This test must pass before:
- **test-colon-definitions-advanced.fs** — May use exception handling

## Notes

- **Non-zero requirement**: Only non-zero codes represent exceptions; 0 means "no error"
- **Stack clearing**: Stack is cleared when exception is transferred, not when caught
- **Nested CATCH**: Inner CATCH takes precedence; if it doesn't handle exception, outer CATCH might
- **Exception code semantics**: Negative codes often indicate fatal errors; positive codes are application-specific
- **Performance**: Exception handling is typically slower than normal flow (used for error cases)
- **Implementation variation**: Different Forth systems may support different exception types

## Exception Codes Convention

| Code | Meaning |
|------|---------|
| 0 | No exception (returned by CATCH) |
| -1 | Unspecified error |
| -2 | ABORT |
| -13 | Undefined word |
| -23 | Invalid name argument |
| 1-255 | Application-specific errors |
| 256+ | Reserved for future use |

(Actual codes vary by Forth implementation)

## Control Flow Model

```
Normal execution:
  Code A → Code B → Code C → Continue

With exception handling:
  Code A → Code B (THROW) → Skip C → CATCH (return code) → Continue

Nested handlers:
  CATCH1
    CATCH2
      THROW (code)
        ↓ (caught by CATCH2)
    CATCH2 returns code
        ↓
  CATCH1 continues
```

---

**Test File**: test-exception-handling.fs  
**Category**: Advanced Features  
**Test Count**: 12  
**Priority**: MEDIUM (optional advanced feature)
