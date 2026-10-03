# Test 1.1: Number Parsing and Representation

## Overview

The **test-numbers.fs** file validates that the Forth interpreter correctly parses and represents integer numbers on the stack. Tests cover positive integers, negative integers, zero, and large values.

## Test Cases

1. **Basic single-digit parsing**: 0, 1, -1
2. **Two-digit numbers**: 42, -42
3. **Large numbers**: 999999, -999999, max/min 32-bit integers
4. **Mixed arithmetic with parsed numbers**: Multi-operand expressions
5. **Zero edge cases**: Zero as operand in various positions
6. **Negative number operations**: Arithmetic with negative values
7. **Large number representation**: 65536, -65536, 16777216

## What Is Being Tested

- **Number parsing**: Does the interpreter correctly convert decimal literals to stack values?
- **Number representation**: Are numbers stored with correct sign and magnitude?
- **Arithmetic with numbers**: Can parsed numbers be used in computations?
- **Boundary values**: Correct handling of zero, maximum, and minimum representable integers
- **Negative number support**: Proper two's complement or equivalent representation

## Structure of Test Cases

Each test follows the standard format:

```forth
T{ <number> -> <expected-stack-value> }T
T{ <number1> <number2> <op> -> <expected-result> }T
```

### Test Structure Breakdown

```forth
T{ 0 -> 0 }T
```
- Parses the literal 0
- Pushes 0 onto stack
- Expects top-of-stack to be 0

```forth
T{ 10 20 + -> 30 }T
```
- Parses and pushes 10
- Parses and pushes 20
- Executes + (addition)
- Expects result 30 on stack

## Code Documentation

### Zero and Single Digits

```forth
T{ 0 -> 0 }T
```
Tests that zero parses correctly. Zero is a special case because 0 is often used as a false flag in Forth.

```forth
T{ 1 -> 1 }T
T{ -1 -> -1 }T
```
Tests basic positive and negative single digits.

### Multi-digit Numbers

```forth
T{ 42 -> 42 }T
T{ -42 -> -42 }T
T{ 999999 -> 999999 }T
T{ -999999 -> -999999 }T
```
Tests that multi-digit numbers parse correctly while preserving magnitude and sign.

### Boundary Values

```forth
T{ 2147483647 -> 2147483647 }T
T{ -2147483648 -> -2147483648 }T
```
Tests the maximum and minimum values for 32-bit signed integers (2^31 - 1 and -2^31).

### Arithmetic Verification

```forth
T{ 10 20 + -> 30 }T
T{ 100 5 - -> 95 }T
T{ 7 8 * -> 56 }T
T{ 20 4 / -> 5 }T
```
Confirms that parsed numbers work correctly in arithmetic operations. This validates both parsing and the arithmetic operations themselves.

### Zero in Arithmetic

```forth
T{ 0 0 + -> 0 }T
T{ 0 5 + -> 5 }T
T{ 5 0 + -> 5 }T
T{ 0 0 * -> 0 }T
T{ 0 5 * -> 0 }T
```
Tests edge cases where zero appears in computations. Multiplication by zero should yield zero regardless of order.

### Negative Number Arithmetic

```forth
T{ -5 3 + -> -2 }T
T{ -10 -5 + -> -15 }T
T{ -3 -4 * -> 12 }T
```
Validates that negative numbers combine correctly in arithmetic. Note: -3 * -4 = 12 (negative times negative = positive).

### Large Number Representation

```forth
T{ 65536 -> 65536 }T
T{ -65536 -> -65536 }T
T{ 16777216 -> 16777216 }T
```
Tests that large numbers (beyond typical byte/word sizes) parse correctly.

## Expected Outcome

### Successful Run

```
=== Test 1.1: Number Parsing and Representation ===
Total: 27
Passed: 27
Failed: 0
All tests passed!
```

Each test case passes when:
1. The number literal is correctly parsed from input
2. The number is placed on the stack with correct value
3. The sign is correctly preserved (positive vs. negative)
4. Arithmetic operations using the parsed numbers produce correct results

### Failure Scenarios

**Parsing Error:**
```
Stack depth mismatch: expected 1 got 0
```
The number failed to parse or wasn't pushed to the stack.

**Arithmetic Error:**
```
T{ 10 20 + -> 30 }T  (expects 30, gets 31)
```
The arithmetic operation produced incorrect results, indicating either the numbers or the operator are wrong.

**Boundary Value Error:**
```
T{ 2147483647 -> 2147483647 }T  (expects 2147483647, gets wrong value)
```
The interpreter may not support the full range of 32-bit integers, or overflow handling differs.

## Dependency Chain

This test must pass before:
- **test-arithmetic-basic.fs** — Requires correct number parsing for arithmetic tests
- **test-comparisons.fs** — Needs correct number representation to compare values
- All other tests that use numeric literals

## Notes

- Tests assume **signed integer** representation (not unsigned)
- Tests use **decimal base** by default (radix tests are separate)
- **2's complement** representation is assumed for negative numbers
- Tests do **not** verify overflow behavior (that's in test-arithmetic-edge.fs)

---

**Test File**: test-numbers.fs  
**Category**: Core Numbers & Boolean Operations  
**Test Count**: 27  
**Priority**: HIGH (foundational)
