# Test 3.1: Basic Arithmetic Operations

## Overview

The **test-arithmetic-basic.fs** file validates fundamental arithmetic operations: addition (+), subtraction (-), multiplication (*), division (/), and increment/decrement (1+, 1-). These operations form the basis of all computational tests.

## Test Cases

1. **Addition (+)**: Sum of two numbers
2. **Subtraction (-)**: Difference of two numbers
3. **Multiplication (*)**: Product of two numbers
4. **Division (/)**: Integer quotient
5. **Increment (1+)**: Add one to a number
6. **Decrement (1-)**: Subtract one from a number
7. **Chained operations**: Multiple operations in sequence
8. **Mixed signs**: Operations with positive and negative numbers

## What Is Being Tested

- **Addition**: Correct summation with positive, negative, and zero operands
- **Subtraction**: Correct difference calculation, handling sign changes
- **Multiplication**: Correct product, including sign rules (neg × pos = neg, neg × neg = pos)
- **Division**: Integer quotient (truncation toward zero or floor, depending on implementation)
- **1+ and 1-**: Efficient increment/decrement operations
- **Operator precedence**: When multiple operations are chained
- **Numeric accuracy**: No silent overflow or precision loss

## Structure of Test Cases

Each arithmetic test follows:

```forth
T{ <operand1> <operand2> <operator> -> <expected-result> }T
```

### Numeric Ranges

Tests cover:
- Zero (special case)
- Single digits: 1-9
- Multi-digit: 10-99, 100+
- Negative values: -1 to -999999
- Boundary values: Near min/max int

## Code Documentation

### Addition Tests

```forth
T{ 5 3 + -> 8 }T
T{ 0 0 + -> 0 }T
T{ -5 3 + -> -2 }T
T{ 5 -3 + -> 2 }T
T{ -5 -3 + -> -8 }T
```

**Addition behavior**:
- Positive + Positive = Larger positive
- Zero preserves the other operand
- Positive + Negative: Subtract absolute values, take sign of larger
- Negative + Negative = More negative

```forth
T{ 10 20 + -> 30 }T
T{ 100 200 + -> 300 }T
```
Large numbers tested to verify no silent overflow.

### Subtraction Tests

```forth
T{ 5 3 - -> 2 }T
T{ 3 5 - -> -2 }T
T{ 0 0 - -> 0 }T
T{ 5 0 - -> 5 }T
T{ 0 5 - -> -5 }T
```

**Subtraction behavior**:
- Order matters: 5 - 3 ≠ 3 - 5
- x - 0 = x (identity)
- 0 - x = -x (negation)
- Subtracting negatives: 5 - (-3) acts like 5 + 3

```forth
T{ -5 3 - -> -8 }T
T{ -5 -3 - -> -2 }T
T{ 10 7 - -> 3 }T
```

### Multiplication Tests

```forth
T{ 5 3 * -> 15 }T
T{ 0 5 * -> 0 }T
T{ 5 0 * -> 0 }T
T{ 1 7 * -> 7 }T
T{ 7 1 * -> 7 }T
```

**Multiplication properties**:
- Zero times anything = Zero (commutative)
- One times anything = That thing (identity)
- Order doesn't matter for correctness (commutative)

```forth
T{ -5 3 * -> -15 }T
T{ 5 -3 * -> -15 }T
T{ -5 -3 * -> 15 }T
```

**Sign rules**:
- Pos × Pos = Pos
- Pos × Neg = Neg
- Neg × Pos = Neg
- Neg × Neg = Pos

```forth
T{ 10 10 * -> 100 }T
T{ 12 12 * -> 144 }T
```
Larger multiplications test the interpreter's handling of larger products.

### Division Tests

```forth
T{ 20 4 / -> 5 }T
T{ 20 5 / -> 4 }T
T{ 15 3 / -> 5 }T
T{ 7 2 / -> 3 }T
```

**Division behavior**:
- Integer division (truncates toward zero, typically)
- 7 ÷ 2 = 3 (not 3.5)
- Order matters: 20 / 4 ≠ 4 / 20

```forth
T{ -20 4 / -> -5 }T
T{ 20 -4 / -> -5 }T
T{ -20 -4 / -> 5 }T
```
Division follows multiplication's sign rules.

```forth
T{ 100 10 / -> 10 }T
```
Exact division (no remainder) test.

### Increment/Decrement

```forth
T{ 5 1+ -> 6 }T
T{ 0 1+ -> 1 }T
T{ -1 1+ -> 0 }T
```

**1+ operation**: Add one efficiently (single operation vs. 1 +)

```forth
T{ 5 1- -> 4 }T
T{ 0 1- -> -1 }T
```

**1- operation**: Subtract one efficiently

### Chained Operations

```forth
T{ 10 5 + 3 * -> 45 }T
```
**Execution order**:
1. 10 5 + → 15
2. 15 3 * → 45

Note: This tests left-to-right evaluation with postfix notation.

```forth
T{ 20 4 / 2 + -> 7 }T
```
1. 20 4 / → 5
2. 5 2 + → 7

```forth
T{ 5 3 * 2 - -> 13 }T
```
1. 5 3 * → 15
2. 15 2 - → 13

```forth
T{ 100 2 / 5 / -> 10 }T
```
1. 100 2 / → 50
2. 50 5 / → 10

Sequential operations evaluate left-to-right, confirming postfix notation.

## Expected Outcome

### Successful Run

```
=== Test 3.1: Basic Arithmetic ===
Total: 45
Passed: 45
Failed: 0
All tests passed!
```

Each test passes when:
1. The arithmetic operation produces the correct numeric result
2. Sign is handled correctly (positive, negative, zero)
3. Division truncates appropriately (toward zero)
4. Large numbers are computed without overflow
5. Chained operations evaluate left-to-right

### Failure Scenarios

**Addition Failure:**
```
T{ 5 3 + -> 8 }T  (expects 8, got 7)
```
Arithmetic operator is incorrect or number parsing failed.

**Division Failure:**
```
T{ 7 2 / -> 3 }T  (expects 3, got 4)
```
Division may be rounding instead of truncating, or operator is incorrect.

**Sign Handling Failure:**
```
T{ -5 3 * -> -15 }T  (expects -15, got 15)
```
Multiplication is not applying sign rules correctly.

**Negative Zero:**
```
T{ 0 -1 * -> 0 }T  (expects 0, got some representation of -0)
```
Interpreter may not handle -0 properly (should equal +0).

## Dependency Chain

This test depends on:
- **test-numbers.fs** — Must parse numbers correctly
- **test-stack-basics.fs** — Must manipulate stack properly

This test must pass before:
- **test-arithmetic-division.fs** — Advanced division modes depend on basic / working
- **test-arithmetic-wide.fs** — Wide number arithmetic depends on basic ops
- **test-comparisons.fs** — Comparisons use arithmetic internally
- All loop tests — Loop counters use increment/decrement

## Notes

- **Integer division**: Tests use integer division (truncation), not floating-point
- **Precedence**: Forth uses postfix notation (Reverse Polish), so operators are applied immediately
- **No overflow checking**: These tests don't verify overflow behavior; that's tested separately
- **Commutative operations**: + and * are commutative; - and / are not
- **Identity elements**: 0 for +/-, 1 for *

## Sign Rules Summary

| Operation | Operands | Result |
|-----------|----------|--------|
| Pos + Pos | 5 + 3 | 8 |
| Pos + Neg | 5 + (-3) | 2 |
| Neg + Neg | (-5) + (-3) | -8 |
| Pos - Pos | 5 - 3 | 2 |
| Pos - Neg | 5 - (-3) | 8 |
| Pos × Pos | 5 × 3 | 15 |
| Pos × Neg | 5 × (-3) | -15 |
| Neg × Neg | (-5) × (-3) | 15 |

---

**Test File**: test-arithmetic-basic.fs  
**Category**: Arithmetic Operations  
**Test Count**: 45  
**Priority**: HIGH (essential for most other tests)
