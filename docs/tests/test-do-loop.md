# Test 6.3: DO/LOOP Counter Loops

## Overview

The **test-do-loop.fs** file validates the DO/LOOP control structure, which provides counting loops with automatic index management. Tests cover basic loops, nested loops, loop index access with I and J, and loop control.

## Test Cases

1. **Simple counting loops**: FROM TO DO ... LOOP
2. **Loop index access (I)**: Reading the loop counter
3. **Nested loops**: Multiple DO/LOOP blocks
4. **Inner loop index (J)**: Accessing outer loop's index
5. **Accumulation in loops**: Using loop indices in computations
6. **Loop bounds**: Various start/end values
7. **Single iteration loops**: Start and end differ by 1
8. **Stack preservation**: Items on stack are preserved through loops

## What Is Being Tested

- **Loop initialization**: Correct setup of loop parameters on return stack
- **Index access (I)**: Reading current loop index without consuming it
- **Outer index access (J)**: Reading parent loop's index in nested loops
- **Loop termination**: Loop ends when index reaches limit
- **Return stack usage**: Loop variables use return stack correctly
- **Nested loop isolation**: Inner loop doesn't interfere with outer loop
- **Non-destructive indexing**: I and J don't consume values
- **Correct iteration count**: FROM TO DO ... LOOP executes exactly (TO - FROM) times

## Structure of Test Cases

Each loop test follows:

```forth
: <word-name>
   <start> <limit> DO
      <body-using-I>
   LOOP
;
T{ <word-name> -> <expected-results> }T
```

### Loop Semantics

```
DO:   Initialize loop parameters
      Parameters: start limit
      Push (limit, index) onto return stack
      
I:    ( -- index )
      Read current loop index (non-destructive)
      Top of return stack
      
LOOP: Increment index
      If index < limit: continue loop
      Else: pop parameters, exit loop
```

### Iteration Behavior

Loop executes from `start` to `limit-1`:
- `0 4 DO ... LOOP` executes with I = 0, 1, 2, 3 (4 iterations)
- `5 8 DO ... LOOP` executes with I = 5, 6, 7 (3 iterations)
- `3 3 DO ... LOOP` executes 0 times (start = limit)

## Code Documentation

### Basic Loop from 0 to N

```forth
T{ : TEST1 0 4 DO I LOOP ; TEST1 -> 0 1 2 3 }T
```

**Execution**:
1. Push 0 (start index)
2. Push 4 (loop limit)
3. DO: Initialize loop (push 4, 0 to return stack)
4. Iteration 1: I reads 0, push to stack → stack: 0
5. LOOP: Increment index (0→1), 1 < 4, continue
6. Iteration 2: I reads 1, push to stack → stack: 0 1
7. LOOP: Increment index (1→2), 2 < 4, continue
8. Iteration 3: I reads 2, push to stack → stack: 0 1 2
9. LOOP: Increment index (2→3), 3 < 4, continue
10. Iteration 4: I reads 3, push to stack → stack: 0 1 2 3
11. LOOP: Increment index (3→4), 4 not < 4, exit loop
12. Return: stack has 0 1 2 3

**Expected**: 0 1 2 3 (4 values)

### Loop with Different Bounds

```forth
T{ : TEST2 1 6 DO I LOOP ; TEST2 -> 1 2 3 4 5 }T
```

Loop from 1 to 5 (limit is 6, so 6-1=5 is last index).

```forth
T{ : TEST3 0 1 DO I LOOP ; TEST3 -> 0 }T
```

Single iteration: start=0, limit=1, so I=0 once.

### Loop Index Access (I)

```forth
T{ : TEST5 0 3 DO I LOOP ; TEST5 -> 0 1 2 }T
```

I reads the current loop index at each iteration.

### Accumulation in Loops

```forth
T{ : TEST6 0 0 4 DO I + LOOP ; TEST6 -> 6 }T
```

**Execution**:
1. Push 0 (accumulator)
2. DO: 0 4 (loop from 0 to 3)
3. Iteration 1: I=0, + → 0 + 0 = 0
4. Iteration 2: I=1, + → 0 + 1 = 1
5. Iteration 3: I=2, + → 1 + 2 = 3
6. Iteration 4: I=3, + → 3 + 3 = 6
7. Return: 6

Sums 0+1+2+3 = 6.

### Nested DO/LOOP

```forth
T{ : TEST7 0 2 DO 0 2 DO I J + LOOP LOOP ; TEST7 -> 0 1 1 2 }T
```

**Execution**:
1. Outer DO: 0 2 (J will be 0, 1)
2. Outer iteration 1 (J=0):
   - Inner DO: 0 2 (I will be 0, 1)
   - Inner iteration 1 (I=0): I J + → 0 0 + = 0 → stack: 0
   - Inner iteration 2 (I=1): I J + → 1 0 + = 1 → stack: 0 1
   - Inner LOOP exits
3. Outer iteration 2 (J=1):
   - Inner DO: 0 2 (I reset to 0, 1)
   - Inner iteration 1 (I=0): I J + → 0 1 + = 1 → stack: 0 1 1
   - Inner iteration 2 (I=1): I J + → 1 1 + = 2 → stack: 0 1 1 2
   - Inner LOOP exits
4. Outer LOOP exits
5. Return: 0 1 1 2

**J access**: J gives the outer loop's current index. In nested loops:
- I: innermost loop index
- J: outer loop index (one level up)
- K: outer-outer loop index (if supported)

### Loop with Stack Preservation

```forth
T{ : TEST8 100 0 3 DO I LOOP ; TEST8 -> 100 0 1 2 }T
```

1. Push 100
2. DO: 0 3
3. Loop executes, I values: 0, 1, 2
4. Return: 100 on bottom, 0 1 2 on top

The stack below the loop (100) is preserved.

### Return Stack Access (R@)

```forth
T{ : TEST12 0 3 DO R@ LOOP ; TEST12 -> 0 1 2 }T
```

R@ reads from return stack. In a loop, the top of return stack is the loop index.
This is equivalent to I.

### Large Loop Bounds

```forth
T{ : TEST10 0 100 DO I 99 = IF LEAVE THEN LOOP ; TEST10 -> 0 1 2 ... 99 }T
```

This tests that DO/LOOP can handle larger loop counts. The LEAVE word exits early if I=99.

## Expected Outcome

### Successful Run

```
=== Test 6.3: DO/LOOP Counter Loops ===
Total: 13
Passed: 13
Failed: 0
All tests passed!
```

Each test passes when:
1. Loop executes the correct number of times
2. I returns the correct index at each iteration
3. J returns the outer loop's index in nested loops
4. Accumulation in loops produces correct sums
5. Stack items outside the loop are preserved
6. Nested loops execute in correct order

### Failure Scenarios

**Loop Doesn't Execute:**
```
T{ 0 4 DO I LOOP -> }T  (expects 0 1 2 3, got nothing)
```
DO/LOOP not implemented or not triggering loop execution.

**Wrong Iteration Count:**
```
T{ 0 4 DO I LOOP -> 0 1 2 3 4 }T  (expects 0 1 2 3, got 5 values)
```
Loop continues one iteration too long (off-by-one error).

**I Not Working:**
```
T{ 0 3 DO I LOOP -> }T  (expects 0 1 2, got nothing)
```
I operator not reading loop index or not working at all.

**Nested Loop Failure:**
```
T{ 0 2 DO 0 2 DO I J + LOOP LOOP -> 0 1 1 2 }T
(expects 4 values, but J is not accessible in inner loop)
```
J not implemented or nested loops have isolation issues.

## Dependency Chain

This test depends on:
- **test-numbers.fs** — Loop bounds are numbers
- **test-stack-basics.fs** — Stack operations around loops
- **test-arithmetic-basic.fs** — Accumulation uses arithmetic

This test must pass before:
- **test-do-plusloop.fs** — +LOOP is variant of LOOP
- **test-nested-loops.fs** — Advanced nesting with LEAVE
- **test-word-definition.fs** — Word definitions often use loops

## Notes

- **Loop variable: return stack**: DO/LOOP stores index and limit on return stack, not data stack
- **I is non-destructive**: I reads index without popping it
- **Automatic increment**: LOOP increments the index automatically
- **Exit condition**: Loop exits when index >= limit
- **Nested loops**: Inner loop completely restores return stack, allowing outer loop to continue
- **J, K access**: Different Forth systems support different numbers of nesting levels

## Loop Execution Model

```
Initial:  start limit DO
            │     │
            │     └─→ Pushed to return stack (limit)
            └────────→ Pushed to return stack (index)

Per iteration:
  I ────→ Read index from return stack (non-destructive)
  Body executes
  
LOOP:     Increment index
          if (index < limit)
            continue
          else
            pop return stack
            exit
```

## Common Loop Patterns

**Count up**:
```forth
0 10 DO
  I    \ Use counter
LOOP
```

**Sum from 0 to N-1**:
```forth
0 0 N DO
  I +
LOOP
```

**Sum of squares**:
```forth
0 0 N DO
  I I * +
LOOP
```

---

**Test File**: test-do-loop.fs  
**Category**: Control Flow - Loops  
**Test Count**: 13  
**Priority**: HIGH (essential for iterative algorithms)
