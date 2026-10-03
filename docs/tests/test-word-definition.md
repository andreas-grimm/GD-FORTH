# Test 7.1: Basic Word Definitions

## Overview

The **test-word-definition.fs** file validates the fundamental word definition mechanism using colon definitions (`:` and `;`). Tests cover simple constant definitions, definitions with computations, definitions using other words, and recursive definitions.

## Test Cases

1. **Constant words**: Definitions that push a fixed value
2. **Computation words**: Definitions that perform arithmetic
3. **Words using other words**: Composition and reuse
4. **Conditional words**: Definitions with IF/THEN
5. **Memory access**: Definitions that read/write variables
6. **Arithmetic chains**: Definitions using multiple operations
7. **Composition**: Definitions that call other definitions
8. **Chaining**: Calling multiple self-defined words in sequence

## What Is Being Tested

- **Word definition syntax**: `: name ... ;` creates new word
- **Word execution**: Calling a defined word executes its body
- **Word composition**: Defined words can call other defined words
- **Stack behavior**: Words correctly manage the stack
- **Parameter passing**: Stack-based parameters work correctly
- **Return from word**: Execution returns to caller after `;`
- **Variable scope**: Definitions can access variables
- **Word reusability**: Defined word can be called multiple times

## Structure of Test Cases

Each word definition test follows:

```forth
: <word-name>
   <body-code>
;

T{ <word-name> -> <expected-stack> }T
```

Or with parameters:

```forth
: <word-name>
   <code-using-stack-parameters>
;

T{ <param1> <param2> <word-name> -> <expected-stack> }T
```

### Colon Definition Syntax

```forth
: <name>       \ Start definition
   ...         \ Word body
;              \ End definition
```

After `;`, the word is added to the dictionary and can be called like built-in words.

## Code Documentation

### Constant Words

```forth
T{ : FIVE 5 ; FIVE -> 5 }T
```

**Definition creation**:
1. `:` starts a new definition named FIVE
2. Body: `5` pushes the number 5
3. `;` ends the definition

**Execution of FIVE**:
1. FIVE is looked up in dictionary
2. Execute its body: push 5
3. Return to caller
4. Stack: 5

### Words with Computation

```forth
T{ : DOUBLE 2 * ; 3 DOUBLE -> 6 }T
```

**Definition**:
- DOUBLE multiplies the top stack item by 2

**Execution**:
1. Push 3
2. Call DOUBLE
3. DOUBLE: 2 * (multiply top by 2)
4. 3 * 2 = 6
5. Return
6. Stack: 6

```forth
T{ : TRIPLE DUP DOUBLE + ; 4 TRIPLE -> 12 }T
```

**Definition**:
- TRIPLE duplicates the value, doubles it, and adds the original
- Equivalent to: n → n + (n * 2) = n * 3

**Execution**:
1. Push 4
2. Call TRIPLE:
   - DUP: stack is 4 4
   - DOUBLE: 4 2 * = 8, stack is 4 8
   - +: 4 + 8 = 12
3. Stack: 12

### Words Using Other Words

```forth
T{ : A 1 ; : B A 1 + ; B -> 2 }T
```

**Definition**:
- A: push 1
- B: call A (push 1), then add 1

**Execution**:
1. Call B
2. B calls A: push 1 → stack: 1
3. B continues: 1 + → stack: 2
4. Return
5. Stack: 2

### Words with Conditionals

```forth
T{ : ABS DUP 0 < IF NEGATE THEN ; -5 ABS -> 5 }T
```

**Definition**:
- ABS: if top is negative, negate it

**Execution**:
1. Push -5
2. Call ABS:
   - DUP: stack is -5 -5
   - 0 <: compare -5 < 0 → TRUE → stack is -5 -1
   - IF: condition is TRUE, execute NEGATE
   - NEGATE: -(-5) = 5
3. Stack: 5

### Words Accessing Memory

```forth
T{ VARIABLE X : SETX X ! ; : GETX X @ ; 42 SETX GETX -> 42 }T
```

**Definition**:
- SETX: store top-of-stack into variable X
- GETX: fetch value from variable X

**Execution**:
1. Push 42
2. Call SETX:
   - X !: store 42 into X
3. Stack: (empty)
4. Call GETX:
   - X @: fetch from X, push 42
5. Stack: 42

### Words with Arithmetic

```forth
T{ : ADDTEN 10 + ; 7 ADDTEN -> 17 }T
T{ : ADDSQUARE DUP * + ; 3 4 ADDSQUARE -> 19 }T
```

ADDSQUARE computes: a b → a + b²
1. 3 4 on stack
2. DUP: 3 4 4
3. *: 3 16 (4 * 4)
4. +: 19 (3 + 16)

### Word Composition

```forth
T{ : NEGATE 0 SWAP - ; 5 NEGATE -> -5 }T
T{ : SQUARE DUP * ; 5 SQUARE -> 25 }T
T{ : QUAD SQUARE SQUARE ; 2 QUAD -> 16 }T
```

**QUAD definition**:
- SQUARE: 2 * 2 = 4
- SQUARE again: 4 * 4 = 16

This shows that definitions can call other definitions.

### Word Reuse

```forth
T{ : SWAP2 >R SWAP R> ; 1 2 3 SWAP2 -> 1 3 2 }T
```

SWAP2 shows that you can reuse definitions multiple times. Each call creates a new execution context.

### Chained Word Definitions

```forth
T{ : A 1 ; : B A 1 + ; B -> 2 }T
T{ : A 10 ; : B A 5 + ; B -> 15 }T
T{ : ADDONE 1 + ; : ADDFIVE ADDONE ADDONE ADDONE ADDONE ADDONE ; 0 ADDFIVE -> 5 }T
```

Definitions can be built on top of each other, creating layers of abstraction.

## Expected Outcome

### Successful Run

```
=== Test 7.1: Word Definitions ===
Total: 15
Passed: 15
Failed: 0
All tests passed!
```

Each test passes when:
1. Word is created without errors
2. Word executes without errors
3. Word produces correct stack result
4. Word can call other words
5. Word can be reused multiple times
6. Word composition works correctly

### Failure Scenarios

**Word Not Found:**
```
T{ : TEST 5 ; TEST -> 5 }T  (expects 5, but TEST is not defined)
```
Colon definition failed or word was not added to dictionary.

**Syntax Error:**
```
T{ : TEST 5 TEST -> 5 }T  (missing semicolon)
```
Definition did not end properly with `;`.

**Wrong Stack Result:**
```
T{ : DOUBLE 2 * ; 3 DOUBLE -> 7 }T  (expects 6, got 7)
```
The word definition is incorrect or an operator is wrong.

**Recursive Call Fails:**
```
T{ : REC DUP 0 = IF DROP ELSE 1- REC THEN ; 3 REC -> }T
```
Word cannot call itself recursively (if recursion is expected).

**Word Shadowing Issue:**
```
T{ : A 1 ; : B A ; : A 2 ; B -> 2 }T  (expects 2 if latest definition is used)
```
Redefining A changes what B calls (standard behavior).

## Dependency Chain

This test depends on:
- **test-numbers.fs** — Needs number literals
- **test-stack-basics.fs** — Needs stack operations
- **test-arithmetic-basic.fs** — Uses arithmetic in definitions
- **test-if-then.fs** — Uses conditionals in definitions

This test must pass before:
- **test-word-parameters.fs** — Advanced parameter passing
- **test-recursion.fs** — Recursive definitions
- **test-word-shadowing.fs** — Redefining words
- **test-defer.fs** — Dynamic word execution

## Notes

- **Dictionary management**: Words are added to dictionary at compile-time
- **Execution**: Word calls happen at runtime
- **Stack semantics**: Stack is the only parameter passing mechanism
- **Scope**: All words in dictionary are globally accessible
- **Recursive calls**: A word can call itself (if implementation supports it)
- **No type checking**: Forth has no static type system; errors appear at runtime

## Word Definition Model

```
Definition Phase (Compile-time):
  : <name>        ← Start compiling
    <body>        ← Collect tokens
  ;               ← Add to dictionary

Execution Phase (Runtime):
  <name>          ← Look up in dictionary
                  ← Execute the compiled body
                  ← Return to caller
```

## Best Practices

1. **Simple names**: Keep word names clear and short
2. **Stack comments**: Document what word expects and produces (optional)
3. **Composition over complexity**: Build complex operations from simple ones
4. **Reuse**: Define once, use multiple times
5. **Layered abstraction**: Build high-level operations from low-level ones

---

**Test File**: test-word-definition.fs  
**Category**: Word Definitions & Dictionary  
**Test Count**: 15  
**Priority**: HIGH (essential for Forth programming)
