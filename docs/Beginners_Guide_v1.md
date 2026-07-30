# Bashforth: A Beginner's Guide to Forth - Version 1

## What is Forth?

Forth is one of the most unusual programming languages. Instead of traditional function calls like:
```python
result = add(2, 3)
```

Forth uses a **stack** to pass values and return results:
```forth
2 3 +
```

This simple example:
1. Pushes `2` onto the stack
2. Pushes `3` onto the stack  
3. Executes `+`, which pops 3 and 2, then pushes their sum (5)

Forth is like Reverse Polish Notation (RPN) calculators where you enter the numbers first, then the operation.

## Why Forth is Unique

### 1. Simplicity
No complex syntax rules. Just push values and call words. The language is much smaller than Python or Java.

### 2. Interactivity
You can type words directly and see results immediately. Perfect for exploration.

### 3. Extensibility
You can define new words in Forth itself. No need to recompile or reload.

### 4. Reflection
The language can examine and modify itself. This is powerful for debugging and customization.

### 5. No Compilation Overhead
Words compile to a simple bytecode instantly.

## Getting Started

### Starting Bashforth

```bash
./bashforth
```

You should see:
```
BashForth v0.63a
www: https://github.com/Bushmills/Bashforth

ok
```

### Your First Commands

Let's do some basic math. Type these commands:

```forth
2 3 +
.
```

**Explanation:**
- `2` - Push 2 onto stack
- `3` - Push 3 onto stack
- `+` - Add them (pops both, pushes result)
- `.` - Print the result (5)

### Simple Stack Operations

Try these:
```forth
10 20 30
.s
```

**Output:**
```
10 20 30
```

This shows the stack contains three values. `dup` duplicates the top:

```forth
5 dup
.s
```

**Output:**
```
5 5
```

## The Stack Model Explained

Think of the stack like a plate dispenser:
- You can only add (push) plates from the top
- You can only remove (pop) plates from the top
- You can never access a plate in the middle without removing the ones above

### Basic Stack Words

| Word | Effect | Example |
|------|--------|---------|
| `dup` | Duplicate top | `5 dup` → stack: [5, 5] |
| `drop` | Remove top | `5 3 drop` → stack: [5] |
| `swap` | Exchange top two | `5 3 swap` → stack: [3, 5] |
| `over` | Copy 2nd to top | `5 3 over` → stack: [5, 3, 5] |
| `.s` | Show stack | Display all values |

### Practice Exercise 1

Compute `(5 + 3) * 2` in Forth:
```forth
5 3 + 2 *
.
```

**Result:** 16

No parentheses needed! Forth evaluates left to right following its natural order.

## Defining Your Own Words

This is where Forth becomes powerful. Use `:` to define a word:

```forth
: double 2 * ;
```

Now `double` is a new word:
```forth
5 double
.
```

**Output:** 10

### How Definitions Work

```forth
: add3 3 + ;
```

Breaking this down:
- `:` - Start defining a word
- `add3` - Name of the new word
- `3 +` - The definition (add 3)
- `;` - End definition

Now `add3` works like a built-in word:
```forth
10 add3 .
```

**Output:** 13

### Practice Exercise 2

Define a word to compute square:
```forth
: square dup * ;
```

Test it:
```forth
7 square .
```

**Output:** 49

## Arithmetic Words

| Word | Stack Effect | Example |
|------|--------------|---------|
| `+` | `( n1 n2 -- n3 )` | `5 3 +` → 8 |
| `-` | `( n1 n2 -- n3 )` | `5 3 -` → 2 |
| `*` | `( n1 n2 -- n3 )` | `5 3 *` → 15 |
| `/` | `( n1 n2 -- n3 )` | `15 3 /` → 5 |
| `mod` | `( n1 n2 -- n3 )` | `17 5 mod` → 2 |
| `abs` | `( n -- n )` | `-5 abs` → 5 |
| `negate` | `( n -- n )` | `5 negate` → -5 |

## Conditional Execution (if...then)

Forth has conditionals for decision-making:

```forth
: sign ( n -- )
  dup 0< if
    ." negative" 
  then
;
```

This reads:
- `dup 0<` - Duplicate top and compare with zero (less than?)
- `if` - If true, execute the following
- `." negative"` - Print "negative"
- `then` - End conditional

Test it:
```forth
-5 sign
```

**Output:** negative

### Using else

```forth
: sign-full ( n -- )
  dup 0< if
    ." negative"
  else
    ." non-negative"
  then
;
```

### Comparison Words

| Word | Meaning | Example |
|------|---------|---------|
| `<` | Less than | `3 5 <` → true |
| `>` | Greater than | `5 3 >` → true |
| `=` | Equal | `5 5 =` → true |
| `<>` | Not equal | `5 3 <>` → true |
| `0<` | Less than zero | `-5 0<` → true |
| `0=` | Equal to zero | `0 0=` → true |

Note: `true` = -1, `false` = 0 in Forth

## Loops (do...loop)

Repeat an action a certain number of times:

```forth
: loop-test
  5 0 do
    i .
  loop
;
```

This outputs:
```
0 1 2 3 4
```

**Explanation:**
- `5 0 do` - Loop from 0 to 4 (5 times)
- `i` - Get the current loop index
- `.` - Print it
- `loop` - Increment counter and repeat

### Another Loop Example

```forth
: multiplication-table
  10 0 do
    i . 
    i 2 * .
    cr
  loop
;
```

This prints:
```
0 0
1 2
2 4
3 6
...
```

`cr` outputs a newline.

## Variables

Store values that you can retrieve later:

```forth
variable counter
```

Now `counter` is a named storage location:

```forth
42 counter !
```

Store 42 at counter. Then retrieve it:

```forth
counter @
.
```

**Output:** 42

### Working with Variables

```forth
variable x
100 x !
x @ 50 +
.
```

**Output:** 150

The `!` (store) and `@` (fetch) are the fundamental memory operations.

## Common Patterns

### Pattern 1: Accumulation

```forth
: sum-to ( n -- sum )
  0 swap 0 do
    i +
  loop
;
```

This computes sum from 0 to n-1.

### Pattern 2: Conditional Execution

```forth
: between ( n low high -- f )
  over > over < or not
;
```

Check if n is between low and high.

### Pattern 3: Nested Loops

```forth
: multiplication-table ( n -- )
  dup 0 do
    dup 0 do
      i 1+ j 1+ * .
    loop
    cr
  loop
  drop
;
```

Print n×n multiplication table.

## Working with Strings

Forth has great string support:

```forth
." Hello, World!"
```

The `."` word prints a string.

### String Stack

Store strings separately:

```forth
s" Hello" push$
s" World" push$
append$
type$
```

This concatenates and prints: `HelloWorld`

## Input/Output

### Outputting Values

```forth
5 .
42 . 
```

Prints: `5 42`

### Outputting Text

```forth
." This is text"
```

### Getting Input

```forth
key
```

Read a single character (returns its ASCII code).

### Practice Exercise 3

Create a simple program:

```forth
: greet
  ." What is your name? "
  s" Thanks for the input!" type
;

greet
```

## Stack Notation

Forth documentation uses this notation:

```forth
( input1 input2 -- output )
```

Example:
```forth
+   ( n1 n2 -- n3 )     Add two numbers
dup ( x -- x x )        Duplicate top
```

Read it as: "pops inputs from left, pushes outputs to right"

## Common Mistakes

### Mistake 1: Wrong Order
```forth
5 3 -
```

This computes 3 - 5 = -2, not 5 - 3 = 2. Order matters!

### Mistake 2: Forgetting Stack Depth
```forth
2 3 +
4 +
```

The first `+` consumes 2 and 3. The second `+` tries to pop from an empty stack. Error!

### Mistake 3: Stack Underflow
```forth
drop drop drop
```

If you don't have 3 items, you get an error.

## Debugging Tips

### View the Stack
```forth
.s
```

Always check what's on the stack!

### Print Intermediate Values
```forth
: my-word
  dup . drop
  5 +
  .
;
```

Insert `.` and `dup` to debug values.

### Check Word Availability
```forth
words
```

Lists all defined words.

## Simple Programs

### Example 1: Absolute Value

```forth
: abs ( n -- n )
  dup 0< if
    negate
  then
;

-5 abs .
```

**Output:** 5

### Example 2: Factorial

```forth
: factorial ( n -- n! )
  dup 0= if
    drop 1
  else
    dup 1- factorial *
  then
;

5 factorial .
```

**Output:** 120

### Example 3: Sum of Squares

```forth
: sum-of-squares ( n -- sum )
  0 swap 1 do
    i i * +
  loop
;

5 sum-of-squares .
```

**Output:** 55 (1² + 2² + 3² + 4² + 5²)

## Key Concepts Summary

| Concept | Explanation |
|---------|-------------|
| Stack | LIFO (Last In First Out) storage |
| Push | Add value to top of stack |
| Pop | Remove value from top |
| Word | A named sequence of operations |
| Definition | Creating a new word with `:` |
| Immediate | Execute during definition (control structures) |
| Stack Notation | `( before -- after )` |
| Compilation | Convert definition to bytecode |
| Interpretation | Execute word immediately |

## Forth Philosophy

Forth encourages:
1. **Simplicity** - Do one thing well
2. **Composability** - Combine simple words into complex ones
3. **Interactivity** - Test ideas immediately
4. **Clarity** - Reading code is knowing what it does
5. **Economy** - Minimal overhead, maximum flexibility

## Next Steps

1. **Read the Word List** - See `Forth_Implementation_Specification.md`
2. **Explore Built-in Words** - Type `words` to list all
3. **Define Your Own Words** - Experiment with `:` definitions
4. **Study Examples** - Look at system word definitions with `see`
5. **Understand Control** - Master if/then, do/loop
6. **Read Advanced Topics** - Study the architecture documents

## Quick Reference

### Essential Words
```forth
.        Print top of stack
.s       Show all stack values
dup      Duplicate top
drop     Remove top
swap     Exchange top two
over     Copy second to top
+        Addition
-        Subtraction
*        Multiplication
/        Division
mod      Modulo
=        Equal
<        Less than
>        Greater than
if..then if/else conditional
do..loop counted loop
:        Define word
;        End definition
```

### Common Errors and Solutions

| Error | Cause | Fix |
|-------|-------|-----|
| Stack underflow | Not enough values | Check stack depth with `.s` |
| Word not found | Typo or not defined | Use `words` to check |
| Compilation error | Syntax error | Check `;` is present |
| Infinite loop | Loop doesn't exit | Check loop condition |
| Unexpected result | Wrong operand order | Remember stack order |

## Getting Help

- Type `doc word-name` - Get documentation for a word
- Type `see word-name` - View source code
- Type `words` - List all available words
- Type `.s` - Display stack contents

## Conclusion

Forth is a language that rewards curiosity and exploration. Its interactive nature means you can immediately test ideas. Start with simple calculations, define a few helper words, and gradually build up your knowledge.

The combination of simplicity, power, and interactivity makes Forth uniquely suitable for learning how computers really work and for building elegant, compact programs.

Happy Forth-ing!
