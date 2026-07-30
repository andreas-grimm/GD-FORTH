# Test 4.1: Memory Cell Operations

## Overview

The **test-memory-cells.fs** file validates fundamental memory access operations using cell-sized storage (typically 32 or 64-bit words). Tests cover reading (`@`), writing (`!`), and increment (`+!`) operations on variables.

## Test Cases

1. **Store and fetch (@, !)**: Write and read cell values
2. **Multiple variables**: Operations on different cells
3. **Overwriting values**: Updating stored values
4. **Large values**: Storing max/min integers
5. **Zero values**: Special case of zero
6. **Increment operations (+!)**: Modifying stored values
7. **Indirect addressing**: Using addresses on stack
8. **Fetch shortcuts (?).**: Alternative notation (if supported)

## What Is Being Tested

- **Store operation (!)**: Write top stack value to address
- **Fetch operation (@)**: Read value from address to stack
- **Variable creation**: VARIABLE creates addressable cells
- **Address preservation**: Variables have consistent addresses
- **Overwrite behavior**: New values replace old values
- **Increment operation (+!)**: Add value to stored content
- **Memory isolation**: Different variables don't interfere
- **Value persistence**: Stored values persist between accesses

## Structure of Test Cases

Each memory test follows:

```forth
VARIABLE <var-name>

T{ <value> <var-name> ! <var-name> @ -> <value> }T
```

### Memory Access Notation

- **`<var> !`** — Store: Pop value from stack, write to variable's address
- **`<var> @`** — Fetch: Read value from variable's address, push to stack
- **`<var> +!`** — Increment: Add value from stack to variable's content
- **`<var>`** — Push address: Leaves the address of the variable on stack

## Code Documentation

### Basic Store and Fetch

```forth
T{ 42 TEST-VAR ! TEST-VAR @ -> 42 }T
```

**Execution**:
1. Push 42
2. TEST-VAR: push address of TEST-VAR
3. !: pop 42, pop address, store 42 at that address
4. TEST-VAR: push address again
5. @: pop address, fetch value (42), push to stack
6. Stack: 42

**What happens in memory**:
```
Before:  TEST-VAR: [??]
After:   TEST-VAR: [42]
Stack:   42
```

### Store Different Values

```forth
T{ 100 TEST-VAR ! TEST-VAR @ -> 100 }T
T{ -5 TEST-VAR ! TEST-VAR @ -> -5 }T
T{ 0 TEST-VAR ! TEST-VAR @ -> 0 }T
```

Any integer can be stored: positive, negative, zero.

### Multiple Variables

```forth
T{ 10 TEST-VAR ! 20 TEST-VAR2 ! TEST-VAR @ TEST-VAR2 @ -> 10 20 }T
```

**Execution**:
1. 10 TEST-VAR ! — Store 10 in TEST-VAR
2. 20 TEST-VAR2 ! — Store 20 in TEST-VAR2
3. TEST-VAR @ — Fetch from TEST-VAR: 10
4. TEST-VAR2 @ — Fetch from TEST-VAR2: 20
5. Stack: 10 20

Each variable maintains its own independent value.

### Store and Use

```forth
T{ 99 TEST-VAR ! 88 TEST-VAR2 ! 77 TEST-VAR3 ! 
   TEST-VAR @ TEST-VAR2 @ TEST-VAR3 @ -> 99 88 77 }T
```

Multiple assignments followed by multiple fetches. Each variable retains its value independently.

### Overwriting Values

```forth
T{ 5 TEST-VAR ! 10 TEST-VAR ! TEST-VAR @ -> 10 }T
```

**Execution**:
1. Store 5 in TEST-VAR: TEST-VAR [5]
2. Store 10 in TEST-VAR: TEST-VAR [10] (overwrites 5)
3. Fetch from TEST-VAR: 10
4. Stack: 10

The new value completely replaces the old value.

### Large Values

```forth
T{ 2147483647 TEST-VAR ! TEST-VAR @ -> 2147483647 }T
T{ -2147483648 TEST-VAR ! TEST-VAR @ -> -2147483648 }T
```

Tests that the maximum and minimum 32-bit signed integers can be stored and retrieved.

### Zero Values

```forth
T{ 0 TEST-VAR ! 0 TEST-VAR2 ! TEST-VAR @ TEST-VAR2 @ + -> 0 }T
```

Zero is a special case (often used in conditionals and arithmetic).

### Increment Operation (+!)

```forth
T{ TEST-VAR @ -> 0 }T
T{ 5 TEST-VAR +! TEST-VAR @ -> 5 }T
T{ 3 TEST-VAR +! TEST-VAR @ -> 8 }T
T{ -2 TEST-VAR +! TEST-VAR @ -> 6 }T
```

**+! semantics**: `value var +!` means var = var + value

**Execution sequence**:
1. Initial TEST-VAR: [0]
2. 5 TEST-VAR +! → TEST-VAR becomes [0+5] = [5]
3. 3 TEST-VAR +! → TEST-VAR becomes [5+3] = [8]
4. -2 TEST-VAR +! → TEST-VAR becomes [8-2] = [6]

### Direct Address Operations

```forth
T{ HERE -> }T
T{ 123 HERE ! HERE @ -> 123 }T
```

HERE is a special address that points to the end of compiled definitions. Can write to it (though not recommended in practice).

### Fetch Using Address from Stack

```forth
T{ TEST-VAR >R R@ @ R> DROP -> 6 }T
```

**Execution**:
1. >R: move TEST-VAR's address to return stack
2. R@: copy address from return stack (non-destructive)
3. @: fetch value at that address
4. R> DROP: clean up return stack
5. Stack: 6 (current value in TEST-VAR)

## Expected Outcome

### Successful Run

```
=== Test 4.1: Memory Cell Operations ===
Total: 21
Passed: 21
Failed: 0
All tests passed!
```

Each test passes when:
1. Values can be stored in variables
2. Stored values can be retrieved exactly
3. Overwriting works correctly
4. Different variables don't interfere
5. Large positive and negative values work
6. The +! operation correctly adds to stored values
7. Addresses can be manipulated on the stack

### Failure Scenarios

**Store Not Working:**
```
T{ 42 TEST-VAR ! TEST-VAR @ -> 42 }T  (expects 42, got 0 or garbage)
```
The ! operator is not storing the value correctly.

**Fetch Not Working:**
```
T{ 42 TEST-VAR ! TEST-VAR @ -> 42 }T  (expects 42, but @ returns 0)
```
The @ operator is not reading the stored value.

**Variable Interference:**
```
T{ 10 TEST-VAR ! 20 TEST-VAR2 ! TEST-VAR @ -> 10 }T  (expects 10, got 20)
```
Different variables are interfering with each other (addressing error).

**+! Not Working:**
```
T{ 5 TEST-VAR +! TEST-VAR @ -> 5 }T  (after initial 0, expects 5, got 0)
```
The +! operator is not incrementing the stored value.

**Overflow Handling:**
```
T{ 2147483647 TEST-VAR ! TEST-VAR @ -> 2147483647 }T  
(expects max int, got overflow result)
```
Overflow behavior differs from expected (varies by implementation).

## Memory Model

### Variable Creation

```forth
VARIABLE TEST-VAR
```

Allocates storage and assigns an address. TEST-VAR is a word that pushes this address.

### Store Operation

```
Stack: [value] [address]
  !
Stack: []
Memory: address ← value
```

### Fetch Operation

```
Stack: [address]
  @
Stack: [value]
```

### Increment Operation

```
Stack: [increment] [address]
  +!
Stack: []
Memory: address ← address + increment
```

## Dependency Chain

This test depends on:
- **test-numbers.fs** — Needs number values to store
- **test-stack-basics.fs** — Uses SWAP, >R, etc. in some tests

This test must pass before:
- **test-memory-chars.fs** — Character access uses similar mechanics
- **test-memory-alignment.fs** — Alignment tests address management
- **test-memory-move.fs** — Block operations require working @ and !
- **test-word-definition.fs** — Word definitions that use variables

## Notes

- **Cell size**: Typically 4 bytes (32-bit) or 8 bytes (64-bit)
- **Variable scope**: All variables are global (no local scope)
- **Address stability**: Variable addresses don't change during execution
- **Direct addressing**: Can use addresses on the stack (advanced technique)
- **Uninitialized values**: Variables may contain garbage until first write
- **Memory safety**: No bounds checking; writing to arbitrary addresses is possible (dangerous)

## Common Patterns

**Counter**:
```forth
VARIABLE COUNT
0 COUNT !
1 COUNT +!
COUNT @ \ Get current count
```

**Temporary storage**:
```forth
VARIABLE TEMP
\ ... computation ...
TEMP !    \ Save intermediate result
\ ... more computation ...
TEMP @    \ Retrieve saved result
```

**State machine**:
```forth
VARIABLE STATE
0 STATE !
\ Set state: number STATE !
\ Check state: STATE @ CASE ... ENDCASE
```

---

**Test File**: test-memory-cells.md  
**Category**: Memory Access & Operations  
**Test Count**: 21  
**Priority**: HIGH (essential for working with state and variables)
