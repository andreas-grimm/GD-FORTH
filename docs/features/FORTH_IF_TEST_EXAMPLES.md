# FORTH Standard IF/ELSE/THEN Test Examples

## Overview

The GD-FORTH interpreter includes comprehensive test suites following the ANS Forth standard test harness format. These tests verify the IF statement implementation against the official FORTH standard.

## Test Harness Format

### Standard Test Syntax

```forth
T{ <test-definition> -> <expected-stack-result> }T
```

**Components:**
- `T{` - Test harness begin marker
- `<test-definition>` - FORTH code to test (typically a word definition)
- `->` - Stack result separator
- `<expected-stack-result>` - Expected values on stack after execution
- `}T` - Test harness end marker

### Example Format

```forth
T{ : MAX2 2DUP > IF NIP ELSE DROP THEN ; MAX2 5 3 -> 5 }T
```

This defines a word `MAX2` that returns the maximum of two stack values, then tests it with inputs 5 and 3, expecting 5 on the stack.

---

## IF/THEN Tests

### Test File: `test-if-then.fs`

**Location:** `src/test/forth/Language Compliance Test/test-if-then.fs`

#### Test 1: Simple IF with TRUE condition
```forth
T{ : TEST1 TRUE IF 100 THEN ; TEST1 -> 100 }T
```
- Define word `TEST1` that pushes TRUE, then IF condition executes true-branch (100)
- Expected: Stack contains [100]

#### Test 2: Simple IF with FALSE condition
```forth
T{ : TEST2 FALSE IF 100 THEN 200 ; TEST2 -> 200 }T
```
- Define word `TEST2` that pushes FALSE, IF skips true-branch, then pushes 200
- Expected: Stack contains [200]

#### Test 3-5: Non-boolean values
```forth
T{ : TEST3 1 IF 50 THEN ; TEST3 -> 50 }T
T{ : TEST4 0 IF 50 THEN 75 ; TEST4 -> 75 }T
T{ : TEST5 -1 IF 60 THEN ; TEST5 -> 60 }T
```
- Verifies FORTH semantics: 0=FALSE, 1=TRUE, -1=TRUE
- Any non-zero value is treated as true

#### Test 6-7: Nested conditionals
```forth
T{ : TEST6 TRUE IF FALSE IF 100 THEN 200 THEN ; TEST6 -> 200 }T
T{ : TEST7 TRUE IF TRUE IF 300 THEN THEN ; TEST7 -> 300 }T
```
- **TEST6**: Outer IF true → Inner IF false (skipped) → Push 200
- **TEST7**: Outer IF true → Inner IF true (100 pushed, then 300) → Result: 300

#### Test 8-9: IF affecting stack operations
```forth
T{ : TEST8 5 TRUE IF 10 + THEN ; TEST8 -> 15 }T
T{ : TEST9 5 FALSE IF 10 + THEN ; TEST9 -> 5 }T
```
- **TEST8**: Stack [5], TRUE condition, execute `10 +` → Result: 15
- **TEST9**: Stack [5], FALSE condition, skip `10 +` → Result: 5

#### Test 10-11: Multiple stack items
```forth
T{ : TEST10 1 2 3 TRUE IF 4 THEN ; TEST10 -> 1 2 3 4 }T
T{ : TEST11 1 2 3 FALSE IF 4 THEN ; TEST11 -> 1 2 3 }T
```
- Verifies IF only consumes the flag, preserves other stack items
- **TEST10**: Stack [1,2,3] + TRUE + execute → [1,2,3,4]
- **TEST11**: Stack [1,2,3] + FALSE + skip → [1,2,3]

#### Test 12-13: Empty THEN branch
```forth
T{ : TEST12 TRUE IF THEN 99 ; TEST12 -> 99 }T
T{ : TEST13 FALSE IF THEN 88 ; TEST13 -> 88 }T
```
- Verifies empty IF branches work correctly
- If condition false, execution continues normally

#### Test 14-15: Arithmetic conditions
```forth
T{ : TEST14 10 5 > IF 1 ELSE 0 THEN ; TEST14 -> 1 }T
T{ : TEST15 10 5 < IF 1 ELSE 0 THEN ; TEST15 -> 0 }T
```
- Tests IF with comparison operators
- `10 5 >` pushes TRUE (-1), IF executes branch, ELSE not taken

#### Test 16: Deeply nested IF
```forth
T{ : TEST16 TRUE IF TRUE IF TRUE IF 999 THEN THEN THEN ; TEST16 -> 999 }T
```
- Three levels of nested IF statements
- All conditions true, so 999 is pushed

---

## IF/ELSE/THEN Tests

### Test File: `test-if-else.fs`

**Location:** `src/test/forth/Language Compliance Test/test-if-else.fs`

#### Test 1-2: Basic IF/ELSE/THEN
```forth
T{ : TEST1 TRUE IF 100 ELSE 200 THEN ; TEST1 -> 100 }T
T{ : TEST2 FALSE IF 100 ELSE 200 THEN ; TEST2 -> 200 }T
```
- **TEST1**: Condition true → Execute true-branch → 100
- **TEST2**: Condition false → Execute else-branch → 200

#### Test 3-5: Non-zero/zero values
```forth
T{ : TEST3 1 IF 50 ELSE 75 THEN ; TEST3 -> 50 }T
T{ : TEST4 0 IF 50 ELSE 75 THEN ; TEST4 -> 75 }T
T{ : TEST5 -1 IF 60 ELSE 70 THEN ; TEST5 -> 60 }T
```
- Verifies FORTH semantics with ELSE
- 0=FALSE (execute else), any other value=TRUE (execute true)

#### Test 6-8: Arithmetic with ELSE
```forth
T{ : TEST6 10 5 > IF 1 ELSE 0 THEN ; TEST6 -> 1 }T
T{ : TEST7 3 5 > IF 1 ELSE 0 THEN ; TEST7 -> 0 }T
T{ : TEST8 5 5 = IF 99 ELSE 88 THEN ; TEST8 -> 99 }T
```
- Tests comparison operators with both branches
- **TEST6**: 10 > 5 is true → 1
- **TEST7**: 3 > 5 is false → 0
- **TEST8**: 5 = 5 is true → 99

#### Test 9-10: Nested IF/ELSE/THEN
```forth
T{ : TEST9 TRUE IF FALSE IF 10 ELSE 20 THEN ELSE 30 THEN ; TEST9 -> 20 }T
T{ : TEST10 FALSE IF 10 ELSE TRUE IF 40 ELSE 50 THEN THEN ; TEST10 -> 40 }T
```
- **TEST9**: Outer true, inner false → else-branch (20)
- **TEST10**: Outer false, else-branch with inner true → 40

#### Test 11-12: IF/ELSE with stack operations
```forth
T{ : TEST11 5 10 > IF + ELSE * THEN ; TEST11 -> 50 }T
T{ : TEST12 10 5 > IF + ELSE * THEN ; TEST12 -> 15 }T
```
- **TEST11**: Stack [5], 10 > (false) → * → 5*10 = 50
- **TEST12**: Stack [5], 10 5 > (true) → + → 10+5 = 15

#### Test 13-14: Triple nesting
```forth
T{ : TEST13 1 2 > IF 0 ELSE 1 IF 2 ELSE 3 THEN THEN ; TEST13 -> 2 }T
T{ : TEST14 5 5 = IF 1 IF 100 ELSE 200 THEN ELSE 300 THEN ; TEST14 -> 100 }T
```
- Complex nesting scenarios
- **TEST13**: 1>2 false → else-branch, then 1 true → 2
- **TEST14**: 5=5 true, 1 true → 100

#### Test 15-16: Stack manipulation in branches
```forth
T{ : TEST15 1 2 3 TRUE IF DROP 99 ELSE THEN ; TEST15 -> 1 2 99 }T
T{ : TEST16 1 2 3 FALSE IF DROP 99 ELSE SWAP THEN ; TEST16 -> 1 3 2 }T
```
- **TEST15**: Stack [1,2,3], IF true, DROP top (3), push 99 → [1,2,99]
- **TEST16**: Stack [1,2,3], IF false, SWAP top two → [1,3,2]

#### Test 17: Complex condition expression
```forth
T{ : TEST17 7 DUP 3 > SWAP 10 < AND IF 888 ELSE 777 THEN ; TEST17 -> 888 }T
```
- Complex condition: `7 DUP 3 > SWAP 10 < AND`
- 7 > 3 AND 7 < 10 = TRUE → 888

#### Test 18: Sequential IF statements
```forth
T{ : TEST18 TRUE IF 1 ELSE 2 THEN FALSE IF 3 ELSE 4 THEN ; TEST18 -> 1 4 }T
```
- Two sequential IF statements in one definition
- Stack: [1] (from first IF true), [1,4] (from second IF false)

---

## FORTH Semantics Summary

### Boolean Values
| Value | Meaning | Behavior |
|-------|---------|----------|
| 0 | FALSE | Execute else-branch or skip IF |
| -1 | TRUE | Execute true-branch |
| Non-zero | TRUE | Treated as true in IF |

### Stack Behavior

**IF...THEN:** `( flag -- )`
- Pops flag from stack
- Executes true-branch if non-zero
- Preserves other stack items

**IF...ELSE...THEN:** `( flag -- )`
- Pops flag from stack
- Executes true-branch if non-zero
- Executes else-branch if zero
- Preserves other stack items

### Nesting Rules
- IF blocks can nest arbitrarily deep
- Each IF must have matching THEN
- ELSE is optional and pairs with nearest IF
- Inner IFs fully resolve before outer blocks resume

---

## Test Execution

These tests are part of the official FORTH language compliance suite and verify that:

✅ Boolean conditions work correctly  
✅ Stack is properly maintained  
✅ Nested conditions execute properly  
✅ ELSE branches work when present  
✅ Edge cases are handled  
✅ Complex expressions work  

The test suite ensures GD-FORTH IF implementation meets ANS Forth standard requirements.

---

## Quick Reference: Common IF Patterns

### Simple conditional
```forth
: ABS ( n -- |n| )
  DUP 0< IF NEGATE THEN ;
```

### Max of two values
```forth
: MAX ( n1 n2 -- max )
  2DUP < IF SWAP THEN DROP ;
```

### Min of two values
```forth
: MIN ( n1 n2 -- min )
  2DUP > IF SWAP THEN DROP ;
```

### Absolute value with ELSE
```forth
: ABS ( n -- |n| )
  DUP 0< IF NEGATE ELSE THEN ;
```

### Conditional computation
```forth
: LIMIT ( n min max -- limited )
  ROT DUP ROT < IF DROP ELSE
    DUP ROT > IF DROP ELSE NIP THEN
  THEN ;
```

---

## Running the Tests

The test files are located in:
```
src/test/forth/Language Compliance Test/
  - test-if-then.fs
  - test-if-else.fs
  - test-nested-conditionals.fs
  - test-conditional-edge.fs
```

These can be run through the FORTH interpreter's compliance test suite.
