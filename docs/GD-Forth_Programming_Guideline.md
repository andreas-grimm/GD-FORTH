# GD-FORTH Programming Guideline
## A Comprehensive Guide for Junior Developers Learning Forth

**Version:** 0.0.3  
**Last Updated:** 2026-10-04  
**Target Audience:** Junior developers new to Forth programming

---

## Table of Contents

1. [Introduction to Forth](#introduction-to-forth)
2. [Getting Started with GD-FORTH](#getting-started-with-gd-forth)
3. [The Stack: Foundation of Forth](#the-stack-foundation-of-forth)
4. [Basic Operations](#basic-operations)
5. [Variables and Memory](#variables-and-memory)
6. [Control Flow](#control-flow)
7. [Word Definitions](#word-definitions)
8. [Common Patterns](#common-patterns)
9. [Best Practices](#best-practices)
10. [Troubleshooting](#troubleshooting)

---

## Introduction to Forth

### What is Forth?

Forth is a stack-based programming language that is:
- **Minimalist**: Simple core with everything else defined in Forth itself
- **Interactive**: Write and test code at the command line
- **Efficient**: Direct memory access and fast execution
- **Flexible**: Extend the language with custom words (functions)

### Why Forth?

Forth is used in:
- Embedded systems (satellites, aerospace)
- Real-time control systems
- Audio and DSP processing
- Educational environments
- Rapid prototyping

### Key Concepts

**Stack-Based Execution**: Instead of function parameters in parentheses, Forth uses a stack:
```forth
3 5 +           ( Pushes 3, then 5, then adds them = 8 on stack )
```

**Reverse Polish Notation (RPN)**: Operations come after their operands:
```
Infix:       3 + 5
Forth (RPN): 3 5 +
```

---

## Getting Started with GD-FORTH

### Installation and Running

GD-FORTH is a Java-based FORTH interpreter. To run it:

```bash
java -jar target/FORTH-0.0.3-jar-with-dependencies.jar
```

### The Interactive Shell

Once started, you'll see a prompt where you can type Forth commands:

```
FORTH> 5 3 +
8
FORTH> 
```

### First Commands

Print to the output:
```forth
." Hello, Forth!"
```

Print a number:
```forth
42 .
```

Print with line break:
```forth
42 . CR
```

---

## The Stack: Foundation of Forth

### Understanding the Stack

The stack is your workspace. Operations push values on and pop values off:

```forth
5           ( Stack: [ 5 ] )
3           ( Stack: [ 5, 3 ] )
+           ( Pop 3 and 5, push 8. Stack: [ 8 ] )
.           ( Pop 8 and print it. Stack: [ ] )
```

### Stack Notation

Forth uses a notation to describe what a word does:

```forth
DUP         ( n -- n n )        Duplicate top of stack
DROP        ( n -- )            Remove top of stack
SWAP        ( n1 n2 -- n2 n1 )  Swap top two items
OVER        ( n1 n2 -- n1 n2 n1 ) Copy second item to top
ROT         ( n1 n2 n3 -- n2 n3 n1 ) Rotate top three
```

### Common Stack Operations

```forth
5 DUP       ( Stack: [ 5, 5 ] )
5 5 +       ( Stack: [ 10 ] )
10 3 -      ( Stack: [ 7 ] )
7 2 *       ( Stack: [ 14 ] )
14 2 /      ( Stack: [ 7 ] )
7 2 MOD     ( Stack: [ 1 ] )  (7 modulo 2)
```

### Viewing the Stack

Print current stack contents:
```forth
.S
```

---

## Basic Operations

### Arithmetic

```forth
3 5 +       = 8    ( Addition )
3 5 -       = -2   ( Subtraction, reverse order! )
3 5 *       = 15   ( Multiplication )
10 3 /      = 3    ( Division, integer )
10 3 MOD    = 1    ( Modulo, remainder )
```

**Important**: Subtraction and division use reverse order:
```forth
10 3 -      ( Computes 3 - 10 = -7, NOT 10 - 3 )
10 3 /      ( Computes 3 / 10 = 0, NOT 10 / 3 )
```

### Comparisons

Comparisons return TRUE (-1) or FALSE (0):

```forth
5 3 >       ( 3 > 5? = 0 (false) )
5 3 <       ( 3 < 5? = -1 (true) )
5 5 =       ( 5 = 5? = -1 (true) )
5 3 <>      ( 5 ≠ 3? = -1 (true) )
```

### Boolean Values

- **TRUE** = -1 (all bits set)
- **FALSE** = 0 (no bits set)

Any non-zero value is considered "true" in conditionals.

---

## Variables and Memory

### Creating Variables

Create a named storage location:

```forth
VARIABLE counter
```

### Setting and Getting Values

Store a value in a variable:
```forth
10 counter !        ( Store 10 in counter )
```

Retrieve a value from a variable:
```forth
counter @           ( Get value from counter, push on stack )
.                   ( Print it: 10 )
```

### Modifying Variables

Increment a variable:
```forth
VARIABLE x
5 x !               ( x = 5 )
x @ 1 + x !         ( x = x + 1 = 6 )
```

### Memory Addresses

Variables store their values at specific memory addresses:

```forth
VARIABLE myvar
myvar               ( Pushes the address of myvar )
@                   ( Fetches the value at that address )
```

---

## Control Flow

### Conditional Execution (IF...THEN)

Execute code only if a condition is true:

```forth
5 3 > IF
  ." 5 is greater than 3" CR
THEN
```

### IF...ELSE...THEN

Execute different code based on a condition:

```forth
5 3 > IF
  ." 5 is greater"
ELSE
  ." 3 is greater"
THEN
```

### Counted Loops (DO...LOOP)

Loop a fixed number of times:

```forth
0 5 DO
  I .         ( I is the loop index: 0, 1, 2, 3, 4 )
LOOP
```

Stack notation: `( limit start -- )`, not `(start limit)`!

### Variable Increment Loops (DO...+LOOP)

Increment by a different amount:

```forth
0 10 DO
  I .         ( Prints: 0, 2, 4, 6, 8 )
  2 +LOOP
```

### Do-While Loops (BEGIN...UNTIL)

Execute body at least once, test condition at end:

```forth
VARIABLE count
0 count !
BEGIN
  count @ 1 + DUP count ! DUP .    ( Increment and print )
  count @ 10 >                      ( Test condition )
UNTIL
```

### Condition-at-Top Loops (BEGIN...WHILE...REPEAT)

Test condition before executing body:

```forth
5 BEGIN
  DUP .                 ( Print current value )
  1 -                   ( Decrement )
  DUP 0>                ( Check if still positive )
WHILE
  REPEAT
```

### Early Loop Exit (LEAVE)

Exit a loop before it normally ends:

```forth
0 100 DO
  I .
  I 50 = IF LEAVE THEN
LOOP
```

---

## Word Definitions

### Creating Your Own Words

Define a new word (custom function):

```forth
: SQUARE       ( n -- n² )
  DUP *
;
```

The colon `:` starts a definition, the word name follows, then the implementation, and `;` ends it.

### Documentation in Word Definitions

Stack notation shows what the word expects and produces:

```forth
: DOUBLE       ( n -- n*2 )
  2 *
;

: MAX2         ( n1 n2 -- max )
  2DUP > IF NIP ELSE DROP THEN
;
```

### Examples of Custom Words

```forth
: GREETING
  ." Hello, Forth!" CR
;

: COUNTED-PRINT   ( n -- )
  0 DO
    I . SPACE
  LOOP
  CR
;

: FACTORIAL       ( n -- n! )
  DUP 0= IF DROP 1 EXIT THEN
  DUP 1 - FACTORIAL *
;
```

---

## Common Patterns

### Sum a Series

```forth
: SUM-TO       ( n -- sum )
  0 SWAP        ( 0 on bottom, n on top )
  0 DO
    I +
  LOOP
;

5 SUM-TO .     ( Output: 10 )
```

### Find Maximum of Two Numbers

```forth
: MAX2         ( n1 n2 -- max )
  2DUP > IF NIP ELSE DROP THEN
;

5 3 MAX2 .     ( Output: 5 )
```

### Conditional Based on Flag

```forth
: DESCRIBE     ( n -- )
  DUP 0< IF
    ." Negative"
  ELSE DUP 0= IF
    ." Zero"
  ELSE
    ." Positive"
  THEN THEN
;

-5 DESCRIBE    ( Output: Negative )
```

### Print a Range

```forth
: PRINT-RANGE  ( start end -- )
  DO
    I . SPACE
  LOOP
  CR
;

1 10 PRINT-RANGE    ( Output: 1 2 3 4 5 6 7 8 9 )
```

---

## Best Practices

### 1. Always Document Stack Effects

Every word should have a stack notation comment:

```forth
: TRIPLE       ( n -- n*3 )
  3 *
;
```

### 2. Keep Words Simple and Focused

Each word should do one thing:

```forth
( Good - simple, single purpose )
: DOUBLE  ( n -- n*2 ) 2 * ;
: TRIPLE  ( n -- n*3 ) 3 * ;

( Less good - does multiple things )
: CALCULATE-AND-PRINT  ( n -- )
  DUP 2 * . ." doubled: " . CR
;
```

### 3. Avoid Deep Stack Usage

Use variables to store intermediate values if stack gets complicated:

```forth
( Good )
VARIABLE temp
: OPERATION   ( a b c -- result )
  temp !      ( Save c )
  +           ( Add a and b )
  temp @ *    ( Multiply by c )
;

( Less good - requires mental stack tracking )
: OPERATION   ( a b c -- result )
  ROT ROT + SWAP *
;
```

### 4. Test Incrementally

Build and test small pieces:

```forth
: SQUARE  ( n -- n² ) DUP * ;
5 SQUARE .        ( Test immediately )

: CUBE    ( n -- n³ ) DUP SQUARE * ;
5 CUBE .          ( Test this new word )
```

### 5. Use Clear Naming

Choose descriptive names:

```forth
( Good names )
: PRINT-HELLO ." Hello" CR ;
: CALCULATE-AREA ( width height -- area ) * ;

( Unclear names )
: FOO ." Bar" CR ;
: CALC * ;
```

---

## Troubleshooting

### "Stack underflow" Error

**Problem**: You tried to use more values than are on the stack.

```forth
5 DUP DUP + +       ( Error! Only one 5 on stack )
```

**Solution**: Ensure you have enough values before operations.

### Unexpected Stack Contents

**Solution**: Use `.S` to see what's on the stack at any time:

```forth
5 3 + .S           ( Check what's there before printing )
```

### Infinite Loops

**Problem**: Loop never exits because condition never becomes true.

**Solution**: Check your condition logic:

```forth
( This loops forever - 1 is always > 0 )
BEGIN
  ." Loop" CR
  1
WHILE REPEAT

( Fixed - decrements and checks )
VARIABLE x
5 x !
BEGIN
  x @ 1 - DUP x ! DUP .
  x @ 0>
WHILE REPEAT
```

### Word Not Found

**Problem**: You misspelled a word or defined it incorrectly.

```forth
DUBBLE 5 .         ( Error - should be DOUBLE )
```

**Solution**: Check spelling and ensure word is defined before use.

---

## Reference Summary

### Core Stack Operations
| Word | Input | Output | Description |
|------|-------|--------|-------------|
| DUP | n | n n | Duplicate |
| DROP | n | — | Remove |
| SWAP | n1 n2 | n2 n1 | Swap |
| OVER | n1 n2 | n1 n2 n1 | Copy second to top |
| ROT | n1 n2 n3 | n2 n3 n1 | Rotate top three |

### Arithmetic
| Word | Input | Output | Description |
|------|-------|--------|-------------|
| + | a b | a+b | Add |
| - | a b | b-a | Subtract |
| * | a b | a*b | Multiply |
| / | a b | b/a | Divide |
| MOD | a b | b mod a | Modulo |

### Comparisons (Output: -1=true, 0=false)
| Word | Description |
|------|-------------|
| = | Equal |
| <> | Not equal |
| < | Less than |
| > | Greater than |
| <= | Less or equal |
| >= | Greater or equal |

### Control Flow
| Word | Syntax | Description |
|------|--------|-------------|
| IF | condition IF ... THEN | Conditional execution |
| ELSE | IF ... ELSE ... THEN | Alternative branch |
| DO...LOOP | start end DO ... LOOP | Counted loop |
| +LOOP | ... increment +LOOP | Variable increment loop |
| BEGIN...UNTIL | BEGIN ... UNTIL condition | Do-while loop |
| BEGIN...WHILE...REPEAT | BEGIN ... WHILE ... REPEAT | Condition-at-top loop |

### I/O
| Word | Description |
|------|-------------|
| . | Print top of stack |
| CR | Carriage return (newline) |
| ." text" | Print literal text |
| .S | Print entire stack |

### Memory
| Word | Stack | Description |
|------|-------|-------------|
| VARIABLE | — | Create variable |
| ! | value addr | Store value at address |
| @ | addr | Fetch value from address |

---

## Learning Path

### Week 1: Stack and Basic Operations
1. Understand the stack metaphor
2. Practice stack operations: DUP, DROP, SWAP
3. Learn arithmetic: +, -, *, /, MOD
4. Print values with `.` and `CR`

### Week 2: Control Flow
1. Master IF...THEN...ELSE
2. Learn DO...LOOP with index access (I)
3. Practice writing simple conditionals
4. Combine arithmetic with conditionals

### Week 3: Variables and Memory
1. Create and modify variables
2. Use variables in loops
3. Understand memory addresses
4. Combine variables with control flow

### Week 4: Word Definitions
1. Define simple words
2. Add stack documentation comments
3. Build complex words from simpler ones
4. Create reusable components

### Week 5+: Projects
1. Simple calculators
2. String manipulation
3. Data structure operations
4. System utilities

---

## Next Steps

### Recommended Resources

1. **Programmers_Guide.md** - Deep dive into GD-FORTH implementation
   - For developers extending the interpreter
   - Architecture and design patterns
   - How to add new FORTH words

2. **Official Test Files**
   - `src/test/forth/` - Example programs
   - See how control flow and patterns are used
   - Learn from tested, working code

3. **GD-FORTH Source Code**
   - `src/main/java/eu/gricom/forth/` - Java implementation
   - Understand how FORTH is interpreted
   - Study the architecture

### Practice Exercises

1. **Fibonacci**: Write a word that calculates the nth Fibonacci number
2. **Prime Check**: Create a word to determine if a number is prime
3. **Reverse**: Write a word to reverse a sequence of numbers
4. **Statistics**: Calculate mean and standard deviation of numbers
5. **Sorting**: Implement a simple sorting algorithm

### Common Mistakes to Avoid

1. **Forgetting order matters** - `3 - 5` is -2, not 2
2. **Not tracking the stack** - Use `.S` frequently
3. **Missing stack documentation** - Always comment stack effects
4. **Deep nesting without variables** - Use temporary storage
5. **Not testing incrementally** - Test each word as you write it

---

## Glossary

- **Stack**: The main memory structure; values are pushed and popped
- **Word**: A defined operation or subroutine in Forth
- **RPN**: Reverse Polish Notation; operator comes after operands
- **Stack notation**: `( input -- output )` describing a word's effect
- **TRUE**: Non-zero value; typically -1
- **FALSE**: Zero value
- **Colon definition**: Using `:` and `;` to define new words
- **Index**: Loop variable (accessed with `I`)

---

## Conclusion

Forth is a powerful and elegant language for direct system control and rapid development. GD-FORTH provides a complete implementation with:

- **Full control flow support** (IF, DO, BEGIN variants)
- **Variable management** (VARIABLE, @, !)
- **Stack operations** (DUP, DROP, SWAP, OVER, ROT)
- **Arithmetic and comparisons** (all standard operations)
- **Word definitions** (create custom operations)

With this guide, you're equipped to start writing Forth programs. Remember:

1. **Think in stacks** - Visualize what's on the stack
2. **Keep it simple** - Simple words combine into complex ones
3. **Test often** - Build incrementally and verify each piece
4. **Document clearly** - Stack notation saves confusion later

Happy Forth programming! 🚀

---

**For More Information:**
- See `docs/Programmers_Guide.md` for implementation details
- Check `src/test/forth/` for example programs
- Review `CHANGELOG.md` for version history and features
