# Test 1.2: Boolean Logic Operations

## Overview

The **test-boolean.fs** file validates boolean operations and flag representations. In Forth, TRUE is represented as -1 and FALSE as 0. Tests cover NOT, AND, OR, and XOR operations.

## Test Cases

1. **Flag constants**: TRUE, FALSE representation
2. **NOT operations**: Inverting boolean flags
3. **AND operations**: Logical conjunction
4. **OR operations**: Logical disjunction
5. **XOR operations**: Exclusive OR
6. **Complex boolean expressions**: Combinations of multiple operators

## What Is Being Tested

- **Flag representation**: TRUE = -1, FALSE = 0
- **NOT operation**: Negation of boolean values
- **AND operation**: Logical AND with all combinations
- **OR operation**: Logical OR with all combinations
- **XOR operation**: Exclusive OR behavior
- **Flag consistency**: Intermediate results are proper flags

## Structure of Test Cases

Each boolean test follows the pattern:

```forth
T{ <operand1> <operand2> <operator> -> <expected-result> }T
```

### Truth Table Format

For binary operators (AND, OR, XOR), tests follow these patterns:

**AND Truth Table:**
- TRUE AND TRUE → TRUE (-1)
- TRUE AND FALSE → FALSE (0)
- FALSE AND TRUE → FALSE (0)
- FALSE AND FALSE → FALSE (0)

**OR Truth Table:**
- TRUE OR TRUE → TRUE (-1)
- TRUE OR FALSE → TRUE (-1)
- FALSE OR TRUE → TRUE (-1)
- FALSE OR FALSE → FALSE (0)

**XOR Truth Table:**
- TRUE XOR TRUE → FALSE (0)
- TRUE XOR FALSE → TRUE (-1)
- FALSE XOR TRUE → TRUE (-1)
- FALSE XOR FALSE → FALSE (0)

## Code Documentation

### Flag Representation

```forth
T{ TRUE -> -1 }T
T{ FALSE -> 0 }T
```
Validates the Forth standard representation: TRUE is all bits set (-1 in two's complement), FALSE is zero.

### NOT Operations

```forth
T{ TRUE NOT -> 0 }T
T{ FALSE NOT -> -1 }T
T{ 0 NOT -> -1 }T
T{ -1 NOT -> 0 }T
T{ 1 NOT -> -1 }T
```
Tests that NOT inverts the flag:
- NOT TRUE → FALSE
- NOT FALSE → TRUE
- NOT 0 → -1 (any zero is false)
- NOT -1 → 0 (any non-zero is true)
- NOT 1 → -1 (1 is truthy, so NOT 1 is -1)

### AND Operations

```forth
T{ TRUE TRUE AND -> -1 }T
T{ TRUE FALSE AND -> 0 }T
T{ FALSE TRUE AND -> 0 }T
T{ FALSE FALSE AND -> 0 }T
T{ -1 -1 AND -> -1 }T
T{ 0 0 AND -> 0 }T
```
AND requires both operands to be true. Result uses proper flag representation.

### OR Operations

```forth
T{ TRUE TRUE OR -> -1 }T
T{ TRUE FALSE OR -> -1 }T
T{ FALSE TRUE OR -> -1 }T
T{ FALSE FALSE OR -> 0 }T
T{ -1 -1 OR -> -1 }T
T{ 0 0 OR -> 0 }T
T{ -1 0 OR -> -1 }T
```
OR requires at least one operand to be true. Note: -1 OR 0 = -1 (because -1 has all bits set).

### XOR Operations

```forth
T{ TRUE TRUE XOR -> 0 }T
T{ TRUE FALSE XOR -> -1 }T
T{ FALSE TRUE XOR -> -1 }T
T{ FALSE FALSE XOR -> 0 }T
T{ -1 -1 XOR -> 0 }T
T{ 0 0 XOR -> 0 }T
T{ -1 0 XOR -> -1 }T
```
XOR is true when operands differ. Same values → 0, different values → -1 (in proper flag form).

### Complex Boolean Expressions

```forth
T{ 1 2 AND 3 4 OR AND -> 0 }T
```
This tests operator precedence and chaining:
1. 1 2 AND → 0 (because AND treats both as truthy, but 2 AND 1 = 0 bitwise)
2. 3 4 OR → -1 (both truthy)
3. 0 -1 AND → 0 (anything AND 0 = 0)

```forth
T{ 1 2 OR 3 4 AND OR -> -1 }T
```
1. 1 2 OR → -1 (either truthy)
2. 3 4 AND → 0 (bitwise AND of 3 and 4)
3. -1 0 OR → -1 (anything OR -1 = -1)

## Expected Outcome

### Successful Run

```
=== Test 1.2: Boolean Logic ==="
Total: 44
Passed: 44
Failed: 0
All tests passed!
```

Each test passes when:
1. Flag constants are correctly represented
2. NOT inverts the flag properly
3. AND, OR, XOR operations follow truth tables
4. Complex expressions evaluate correctly
5. Intermediate results are proper flags (-1 or 0)

### Failure Scenarios

**Flag Representation Error:**
```
T{ TRUE -> -1 }T  (expects -1, gets 1)
```
Interpreter uses 1 for TRUE instead of -1. This breaks all conditional logic.

**Operator Error:**
```
T{ TRUE FALSE AND -> 0 }T  (expects 0, gets -1)
```
AND operator is not working correctly.

**Type Inconsistency:**
```
T{ 1 NOT -> -1 }T  (expects -1, gets 0)
```
NOT doesn't handle non-flag truthy values correctly.

## Dependency Chain

This test must pass before:
- **test-comparisons.fs** — Comparison operators produce boolean flags
- **test-if-then.fs** — Conditionals use boolean flags
- **test-arithmetic-basic.fs** — Uses bitwise operations

This test depends on:
- **test-numbers.fs** — Must correctly parse 0, 1, -1

## Notes

- **Flag representation**: Forth uses -1 for TRUE (all bits set) to support bitwise operations
- **Non-boolean values**: Any zero is false; any non-zero is true for conditionals
- **Result format**: Boolean operations should return proper flags (-1 or 0), not arbitrary truthy values
- **Bitwise vs. boolean**: AND, OR, XOR are bitwise operations that produce flags as a byproduct

---

**Test File**: test-boolean.fs  
**Category**: Core Numbers & Boolean Operations  
**Test Count**: 44  
**Priority**: HIGH (essential for conditionals)
