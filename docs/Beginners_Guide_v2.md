# Bashforth: A Beginner's Guide to Forth - Version 2

## What is Forth? (Expanded)

Forth is a **stack-based** programming language where you communicate with the computer using an implicit stack instead of function calls.

### Traditional Programming (C, Python, Java):
```python
result = add(multiply(2, 3), 4)  # Function calls with arguments
```

### Forth:
```forth
2 3 * 4 +    ( Push 2, push 3, multiply, push 4, add )
```

**The Stack is Your Interface:** Think of it as a plate dispenser at a cafeteria. You remove (pop) from the top and add (push) to the top.

### Why Forth is Revolutionary

```
BEFORE (Traditional):        AFTER (Forth):
Separate function names      One operation model
Hidden state                 Visible stack
Complex syntax              Simple, uniform syntax
Compilation step            Interactive execution
```

## Interactive Forth Session - Full Example

Here's a complete first interaction:

```
$ ./bashforth
BashForth v0.63a

words <enter>       shows a list of available words
doc word  <enter>   gives description of word

ok _
```

### Your First Calculations:

```forth
ok 2 3 +
ok .
5 ok
```

**What Happened:**
1. `2` - Pushed 2 onto stack
2. `3` - Pushed 3 onto stack
3. `+` - Popped both, computed 2+3=5, pushed result
4. `.` - Popped top (5), printed it

```forth
ok 10 20 30 .s
30 20 10 ok

ok drop drop drop
ok .s
ok _
```

## The Stack Visualization Tool

Always use `.s` to see what's on the stack:

```forth
ok 10
ok 20
ok 30
ok .s
30 20 10 ok
```

**Reading: `30 20 10`** means:
- Top (rightmost): 30
- Middle: 20  
- Bottom (leftmost): 10

## Understanding Stack Direction

```
Stack grows downward (like cafeteria stack):

After "10 20 30":
    ┌─────┐
    │  30 │  <- Top (next to pop)
    ├─────┤
    │  20 │
    ├─────┤
    │  10 │  <- Bottom
    └─────┘

Forth prints:  30 20 10  (top, middle, bottom)
```

## Core Stack Words (Essential)

### The Big Five:

| Word | What it does | Example |
|------|-------------|---------|
| `dup` | Copy top | `5 dup` → stack: [5 5] |
| `drop` | Remove top | `5 3 drop` → stack: [5] |
| `swap` | Exchange top two | `5 3 swap` → stack: [3 5] |
| `.s` | Show stack | Shows all values |
| `.` | Print and discard | Outputs top value |

### Practice Session 1: Stack Manipulation

```forth
ok 1 2 3
ok .s
3 2 1 ok

ok dup
ok .s
3 3 2 1 ok

ok swap
ok .s
3 3 1 2 ok

ok drop drop drop drop
ok .s
ok _
```

## Arithmetic - The Natural Way

### Left-to-Right Evaluation

```forth
Traditional:  result = ((5 + 3) * 2) - 1

Forth:        5 3 + 2 * 1 -
              
Stack trace:
  5      → [5]
  3      → [5, 3]
  +      → [8]
  2      → [8, 2]
  *      → [16]
  1      → [16, 1]
  -      → [15]

Result: 15
```

**Key insight:** Forth evaluates left-to-right with no parentheses needed.

### Arithmetic Words

```forth
+ - * / mod      Basic operations
abs negate       Unary operations
min max          Comparisons
2* 2/            Bit shifts (fast multiply/divide by 2)
** abs negate    Power, absolute value, negate
```

### Calculation Examples:

```forth
ok 10 3 +
ok .
13 ok

ok 100 7 /
ok .
14 ok

ok 17 5 mod
ok .
2 ok

ok -5 abs
ok .
5 ok
```

## Defining Your Own Words (The Magic!)

### Your First Definition:

```forth
ok : triple 3 * ;
ok
```

Breaking this down:
- `:` - Start defining a word
- `triple` - Name of new word
- `3 *` - The definition (multiply by 3)
- `;` - End definition

Now `triple` works like a built-in word:

```forth
ok 7 triple
ok .
21 ok
```

### More Definitions:

```forth
ok : square dup * ;
ok
ok : cube dup dup * * ;
ok
ok 5 square .
25 ok

ok 3 cube .
27 ok
```

**Pattern:** `: name body ;`

### Multi-Step Definitions:

```forth
ok : quadruple 2 * 2 * ;
ok

ok : avg-three 3 / + + ;
ok ( adds three numbers and divides by 3 )

ok 5 10 15 avg-three .
10 ok
```

## Decision Making (if...then)

### Basic Conditional:

```forth
ok : is-positive
  0< if
    ." Yes, positive"
  then
;
ok

ok 5 is-positive
Yes, positive ok

ok -3 is-positive
ok _
```

**How it works:**
- `0<` checks if top of stack is less than zero
- If true (-1), execute code until `then`
- If false (0), skip to `then`

### if...then...else

```forth
ok : sign-message
  dup 0< if
    ." negative"
  else
    dup 0= if
      ." zero"
    else
      ." positive"
    then
  then
;
ok

ok -5 sign-message
negative ok

ok 0 sign-message  
zero ok

ok 10 sign-message
positive ok
```

## Loops - Repeating Actions

### Counting Loops (do...loop)

```forth
ok : countdown 5 0 do i . loop ;
ok countdown
0 1 2 3 4 ok
```

**How it works:**
- `5 0 do` - Loop 5 times, counter goes 0→4
- `i` - Get the loop counter
- `.` - Print it
- `loop` - Increment counter and repeat

### Practical Loop Example:

```forth
ok : multiplication-table
  10 0 do
    i .
    " x 7 = " type
    i 7 * .
    cr
  loop
;
ok

ok multiplication-table
0 x 7 = 0
1 x 7 = 7
2 x 7 = 14
...
9 x 7 = 63
ok
```

## Variables - Remember Values

### Creating a Variable:

```forth
ok variable counter
ok
```

Now `counter` is a named memory location.

### Storing and Fetching:

```forth
ok 0 counter !     ( Store 0 in counter )
ok
ok counter @       ( Get value from counter )
ok .
0 ok

ok 42 counter !
ok counter @
ok .
42 ok
```

**Operators:**
- `!` (store) - Put value into memory
- `@` (fetch) - Get value from memory

### Using Variables in Loops:

```forth
ok variable x
ok variable sum

ok : sum-to-n ( n -- )
  0 sum !
  swap 1 + 0 do
    i sum @ + sum !
  loop
  sum @
;
ok

ok 10 sum-to-n .
55 ok ( sum of 1..10 = 55 )
```

## Common Patterns & Idioms

### Pattern 1: Accumulation

```forth
ok : sum-all ( n1 n2 n3 -- sum )
  0 >r         ( Save 0 to return stack )
  + r> +       ( Get it back, add all )
;
```

### Pattern 2: Conditional Stack Manipulation

```forth
ok : max ( a b -- max )
  2dup > if nip else drop then
;
ok

ok 5 10 max .
10 ok
```

### Pattern 3: Loop with Accumulator

```forth
ok : factorial ( n -- n! )
  1 swap       ( Accumulator: 1 )
  dup 1 > if   ( If n > 1 )
    swap 0 do
      i 1+ *   ( Multiply by (i+1) )
    loop
  then
;
ok

ok 5 factorial .
120 ok
```

## String Processing

### String Constants:

```forth
ok ." Hello, World!"
Hello, World!ok

ok s" Stored String" .s
Stored String 
ok
```

### String Operations:

```forth
ok s" Hello" push$
ok s" World" push$
ok append$
ok type$
HelloWorld ok
```

**Explanation:**
- `s" string"` - Create string constant
- `push$` - Push onto string stack
- `append$` - Concatenate top two strings
- `type$` - Print top string

## Debugging Techniques

### Technique 1: Print Stack State

```forth
ok 1 2 3 .s
3 2 1 ok
```

### Technique 2: Insert Debug Prints

```forth
ok : debug-multiply
  dup . ." * " dup . ." = "
  * .
;
ok

ok 5 6 debug-multiply
5 * 6 = 30 ok
```

### Technique 3: Check Word Exists

```forth
ok words
... dup drop swap over nip tuck rot -rot pick ...
ok
```

### Technique 4: See Source Code

```forth
ok see double
: double 2 * ;
ok
```

## Complete Programs

### Program 1: Number Guessing Game

```forth
ok variable answer
ok 42 answer !

ok : guess-game
  begin
    ." Guess the number: "
    key key key key drop
    ." Too low!" cr
  until
;
```

### Program 2: String Processing

```forth
ok : uppercase
  push$
  ." Original: " dup$ type$ cr
  ." Length: " dup$ depth$ . cr
  pop$ 2drop
;
```

### Program 3: Recursive Calculation

```forth
ok : fib ( n -- fib_n )
  dup 2 < if exit then
  dup 1- recursive fib
  swap 2 - recursive fib +
;
```

## The Stack Model - Deep Dive

### Why Stack-Based?

1. **Simple:** No hidden state, just the stack
2. **Fast:** No variable lookup needed
3. **Efficient:** Minimal instruction set
4. **Visible:** All values on display

### Stack Contracts (Stack Effects)

```
( input -- output )   <- This notation describes stack changes

Examples:
  dup     ( x -- x x )      Input: one value, output: two copies
  +       ( n1 n2 -- n3 )   Input: two numbers, output: sum
  .       ( n -- )          Input: one number, output: none
  .s      ( -- )            Input: nothing, output: nothing (prints)
```

### Using Stack Effects to Understand Words

```forth
: my-word ( a b -- c )
  + 2 *    ( Take a,b; compute c = (a+b)*2 )
;

Stack trace for: 3 4 my-word
  3 4       <- Push inputs
  +         <- Pop 4,3; push 7
  2 *       <- Pop 2,7; push 14
  Result: 14
```

## Error Messages and Recovery

### Common Errors:

```
Error: stack underflow
Cause: Tried to pop from empty stack
Fix:   Check stack depth with .s

Error: Word not found: unknowns
Cause: Typo in word name
Fix:   Use 'words' to check available words

Error: unstructured
Cause: Mismatched if/then or do/loop
Fix:   Balance all control structures
```

### Recovery:

```forth
ok 1 2 + + +
Error: stack underflow
ok .s
ok _            <- Ready for next input
```

## The Forth Philosophy

Forth rewards:

1. **Clarity** - Write what you mean
2. **Simplicity** - One operation at a time
3. **Reusability** - Build from simple parts
4. **Interactivity** - Test immediately
5. **Introspection** - Examine the system

## Learning Path (Recommended)

**Day 1:**
- Start Bashforth
- Learn stack operations: dup, drop, swap
- Do arithmetic: +, -, *, /
- View stack with .s

**Day 2:**
- Define simple words with :
- Learn if/then for conditionals
- Practice with comparison words

**Day 3:**
- Use do/loop for counting loops
- Understand loop index i
- Build repeated-action programs

**Day 4:**
- Create variables with variable
- Learn @ and ! for memory
- Build programs that remember state

**Day 5:**
- Study advanced words: pick, roll, >r, r>
- Understand return stack manipulation
- Build complex data structures

**Day 6:**
- Learn string operations
- Explore string stack
- Build string-processing programs

**Week 2:**
- Read implementation details
- Study the word set
- Build complete applications
- Optimize for performance

## Quick Reference Card

```
STACK OPS:    dup drop swap over rot nip tuck pick roll
ARITHMETIC:   + - * / mod abs min max 2* 2/
COMPARE:      = < > 0= 0< <>
MEMORY:       @ ! c@ c! +! 2@ 2!
I/O:          . .s .c emit cr space type key
LOOPS:        do loop i j +loop leave
CONDITIONS:   if then else begin until while repeat
DEFINITION:   : ; variable constant immediate
STRING:       s" ." .( char [char]
STACK INFO:   .s depth
DICT INFO:    words doc see '
```

## Next Steps After This Guide

1. **Read Forth_Implementation_Specification.md** - Understand all words
2. **Try Examples from Comparison_To_Standard_Forth.md** - See differences
3. **Study Detailed_Low_Level_Description.md** - How it works inside
4. **Experiment** - Define your own words and programs
5. **Optimize** - Use see to study efficient words

## Conclusion

Forth is a language that rewards curiosity. Start with simple stack operations, graduate to defining words, then explore conditionals and loops. The immediate feedback and ability to examine any word makes Forth uniquely suited for learning programming concepts from the ground up.

The stack model, while different from traditional languages, becomes natural with practice. Once comfortable, you'll appreciate Forth's elegance and power.

Happy Forth-ing! 🚀

## Version 2 Improvements

- 25+ complete code examples
- Full session transcripts
- Stack visualization diagrams
- Better organization of topics
- More complete programs
- Error recovery examples
- Learning path timeline
- Pattern library
- Quick reference card
- Better explanation of concepts
