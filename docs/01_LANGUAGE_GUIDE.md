# FORTH Language Guide
## GD-FORTH Interpreter Documentation

**Version**: 0.0.1  
**Last Updated**: 2026-07-30  
**Author**: Andreas Grimm

---

## Table of Contents

1. [Introduction](#introduction)
2. [Core Concepts](#core-concepts)
3. [Stack Operations](#stack-operations)
4. [Arithmetic Operations](#arithmetic-operations)
5. [Data Types](#data-types)
6. [Control Structures](#control-structures)
7. [Word Definitions](#word-definitions)
8. [Examples](#examples)

---

## Introduction

### What is FORTH?

FORTH is a stack-based, concatenative programming language known for its simplicity, efficiency, and minimalist design philosophy. Unlike traditional procedural languages (like Java or Python), FORTH operates primarily on a stack data structure, where operations consume values from the stack and produce results back onto the stack.

### Key Characteristics

- **Stack-Based**: All operations use a single stack for data manipulation
- **Minimalist**: Small core language with extensibility through word definitions
- **Concatenative**: Programs are built by composing simple words into more complex ones
- **Reflective**: Can inspect and modify its own behavior at runtime
- **Interactive**: REPL (Read-Eval-Print Loop) for immediate feedback

### Typical FORTH Program Structure

```forth
2 3 +
```

This reads as:
1. Push 2 onto the stack
2. Push 3 onto the stack  
3. Execute + (pop 2 values, add them, push result)

Result: Stack contains 5

---

## Core Concepts

### The Stack

The stack is FORTH's primary data structure. All operations are stack-based:

```
Stack grows upward (top is rightmost)

Before: [ ]
Push 5: [ 5 ]
Push 3: [ 5 3 ]
+:      [ 8 ]  (5 + 3)
```

### Words (Functions)

A "word" in FORTH is a named unit of code. FORTH has:
- **Primitive words**: Built-in operations (numbers, +, -, *, /, etc.)
- **Defined words**: User-created sequences of other words

### Execution Model

FORTH uses **immediate execution**:
1. Read a token
2. Look up in dictionary
3. If found: execute
4. If not found: try to parse as number and push to stack
5. Repeat

---

## Stack Operations

### Basic Stack Manipulation

#### DUP (Duplicate)
```forth
5 DUP    -- Stack: [ 5 5 ]
```
Duplicates the top value on the stack.

#### DROP (Remove)
```forth
5 3 DROP  -- Stack: [ 5 ]
```
Removes the top value from the stack.

#### SWAP (Exchange)
```forth
5 3 SWAP  -- Stack: [ 3 5 ]
```
Exchanges the positions of the top two stack values.

#### OVER (Copy Second)
```forth
5 3 OVER  -- Stack: [ 5 3 5 ]
```
Duplicates the second stack value to the top.

#### ROT (Rotate)
```forth
5 3 2 ROT  -- Stack: [ 3 2 5 ]
```
Rotates the top three stack values.

---

## Arithmetic Operations

### Basic Arithmetic

#### Addition: `+`
```forth
10 5 +    -- Result: 15
-3 7 +    -- Result: 4
```

#### Subtraction: `-`
```forth
20 5 -    -- Result: 15
```

**Note**: Stack order matters! The operation is: (second - first)

```forth
5 20 -    -- Result: 15  (20 - 5)
20 5 -    -- Result: -15 (5 - 20)
```

#### Multiplication: `*`
```forth
6 7 *     -- Result: 42
-3 4 *    -- Result: -12
```

#### Division: `/`
```forth
20 4 /    -- Result: 5
10 3 /    -- Result: 3 (integer division, truncated)
```

**Note**: Division by zero causes an error

#### Modulo: `MOD`
```forth
17 5 MOD  -- Result: 2
-17 5 MOD -- Result: -2 (sign follows dividend)
```

### Arithmetic Examples

```forth
( Calculate 2 + 3 * 4 in FORTH style )
2 3 4 * +    -- Stack: [ 14 ] (2 + 12)

( Calculate (5 + 3) * (10 - 2) )
5 3 + 10 2 - *    -- Stack: [ 64 ] (8 * 8)

( Compute average of three numbers )
10 20 30 + + 3 /    -- Stack: [ 20 ]
```

---

## Data Types

### Integers

FORTH operates primarily on integers in this implementation.

```forth
42        -- Integer literal
-17       -- Negative integer
0         -- Zero
```

### Integer Ranges

- **Minimum**: -2,147,483,648 (Integer.MIN_VALUE)
- **Maximum**: 2,147,483,647 (Integer.MAX_VALUE)
- **Overflow Behavior**: Wraps around (Java integer semantics)

### Type Conversion

```forth
3.14      -- Floating point converted to integer: 3
```

---

## Control Structures

### Conditional Execution: `IF...THEN`

```forth
5 3 > IF 10 . THEN    -- Prints 10 (5 is greater than 3)
```

### Loops: `DO...LOOP`

```forth
10 0 DO i . LOOP    -- Prints: 0 1 2 3 4 5 6 7 8 9
```

### Comments

```forth
( This is a comment )
2 3 +    ( add 2 and 3 )    -- Result: 5
```

---

## Word Definitions

### Defining New Words

Create reusable code sequences:

```forth
: SQUARE ( n -- n^2 )
  DUP * ;

5 SQUARE .    -- Prints: 25
```

### Stack Notation (Documentation)

The comment `( n -- n^2 )` is a stack effect comment:
- Input: `n` (one number on stack)
- Output: `n^2` (result on stack)

### Complex Definitions

```forth
: QUADRUPLE ( n -- n*4 )
  2 * 2 * ;

: AVERAGE3 ( n1 n2 n3 -- average )
  + + 3 / ;

10 20 30 AVERAGE3 .    -- Prints: 20
```

---

## Standard Words

### Arithmetic
- `+` — Addition
- `-` — Subtraction
- `*` — Multiplication
- `/` — Division
- `MOD` — Modulo (remainder)

### Comparison
- `>` — Greater than
- `<` — Less than
- `=` — Equal
- `<=` — Less than or equal
- `>=` — Greater than or equal
- `<>` — Not equal

### Stack Operations
- `DUP` — Duplicate top value
- `DROP` — Remove top value
- `SWAP` — Exchange top two values
- `OVER` — Duplicate second value
- `ROT` — Rotate top three values

### I/O Operations
- `.` — Print top stack value (pop it)
- `.S` — Print entire stack (non-destructive)
- `CR` — Print carriage return

### Control Flow
- `IF...THEN` — Conditional execution
- `DO...LOOP` — Loop from start to end
- `:` — Define new word
- `;` — End word definition

---

## Examples

### Example 1: Factorial (using recursion simulation)

```forth
: FACTORIAL ( n -- n! )
  DUP 2 < IF DROP 1 EXIT THEN
  DUP 1 - RECURSE * ;

5 FACTORIAL .    -- Prints: 120
```

### Example 2: Sum of Numbers 1 to N

```forth
: SUM-TO ( n -- sum )
  0 SWAP 0 DO
    OVER I + +
  LOOP
  NIP ;

100 SUM-TO .    -- Prints: 5050
```

### Example 3: Temperature Conversion

```forth
: CELSIUS-TO-FAHRENHEIT ( C -- F )
  9 * 5 / 32 + ;

0 CELSIUS-TO-FAHRENHEIT .     -- Prints: 32
100 CELSIUS-TO-FAHRENHEIT .   -- Prints: 212
```

### Example 4: Interactive Session

```forth
> 2 2 + .
4
> : DOUBLE DUP + ;
> 21 DOUBLE .
42
> 5 10 15 SWAP . . .
10
5
15
```

---

## Error Handling

### Empty Stack Error

When an operation requires values but stack is empty:

```forth
+        -- Error: EmptyStackException
```

### Division by Zero

```forth
10 0 /   -- Error: / by zero
10 0 MOD -- Error: / by zero
```

### Unknown Word

```forth
UNKNOWN   -- Error: Unknown word (if not defined)
```

---

## Performance Considerations

- **Stack Operations**: O(1) - constant time
- **Word Lookup**: O(log n) or O(1) depending on implementation
- **Memory**: Minimal overhead, efficient for resource-constrained environments

---

## Best Practices

1. **Use Stack Comments**: Document stack effects clearly
   ```forth
   : MYWORD ( input1 input2 -- output ) ... ;
   ```

2. **Keep Words Small**: Break complex operations into simple, reusable words

3. **Avoid Deep Stacks**: If you need many values, consider data structures

4. **Test Interactively**: Use the REPL to verify behavior before defining

5. **Comment Complex Logic**: Use `( comment )` for non-obvious operations

---

## Limitations

Current implementation limitations:

- No floating-point numbers (integers only)
- No arrays or complex data structures
- No file I/O in standard words
- No built-in recursion (simulated only)
- Limited string support

---

## Further Reading

- See `02_IMPLEMENTATION_GUIDE.md` for technical details
- See `03_STANDARD_WORDS.md` for complete word reference
- See `04_EXAMPLES.md` for more complex examples
