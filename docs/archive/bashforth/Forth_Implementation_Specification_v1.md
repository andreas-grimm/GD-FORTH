# Bashforth Forth Implementation Specification - Version 1

## Overview

Bashforth implements a subset of Forth with approximately 150+ words covering primitives, high-level definitions, and immediate words for control structures and compilation. This document details all word categories, their stack effects, semantics, and implementation notes.

## Word Categories

### 1. Primitives (Bash Function Implementations)

Core words implemented directly in bash for performance. These execute directly without further interpretation.

#### Stack Operations

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `dup` | `( x -- x x )` | Duplicate top of stack |
| `drop` | `( x -- )` | Discard top of stack |
| `swap` | `( x1 x2 -- x2 x1 )` | Exchange top two elements |
| `over` | `( x1 x2 -- x1 x2 x1 )` | Copy second element to top |
| `rot` | `( x1 x2 x3 -- x2 x3 x1 )` | Rotate third to top |
| `-rot` | `( x1 x2 x3 -- x3 x1 x2 )` | Rotate top under second |
| `nip` | `( x1 x2 -- x2 )` | Discard second element |
| `tuck` | `( x1 x2 -- x2 x1 x2 )` | Insert copy of top under second |
| `pick` | `( ...xn...x0 n -- xn )` | Copy nth element to top (0-based) |
| `roll` | `( ...xn...x0 n -- ...x0 xn )` | Move nth element to top |
| `?dup` | `( 0 -- 0 )` or `( x -- x x )` | Dup if nonzero |
| `2dup` | `( x1 x2 -- x1 x2 x1 x2 )` | Duplicate two top elements |
| `2drop` | `( x1 x2 -- )` | Drop two top elements |
| `2swap` | `( x1 x2 x3 x4 -- x3 x4 x1 x2 )` | Swap two pairs |
| `2over` | `( x1 x2 x3 x4 -- x1 x2 x3 x4 x1 x2 )` | Over for two pairs |
| `depth` | `( -- n )` | Push number of stack elements |

#### Return Stack Operations

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `>r` | `( x -- )` R: `( -- x )` | Move to return stack |
| `r>` | `( -- x )` R: `( x -- )` | Move from return stack |
| `r@` | `( -- x )` R: `( x -- x )` | Copy from return stack |
| `rdrop` | `( -- )` R: `( x -- )` | Drop return stack top |
| `2>r` | `( x1 x2 -- )` R: `( -- x2 x1 )` | Move two to return stack |
| `2r>` | `( -- x1 x2 )` R: `( x2 x1 -- )` | Move two from return stack |
| `i` | `( -- x )` | Loop index (innermost loop) |
| `j` | `( -- x )` | Outer loop index |

#### Arithmetic

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `+` | `( n1 n2 -- n3 )` | Add |
| `-` | `( n1 n2 -- n3 )` | Subtract (n1-n2) |
| `*` | `( n1 n2 -- n3 )` | Multiply |
| `/` | `( n1 n2 -- n3 )` | Divide (n1/n2) - non-floored |
| `/mod` | `( n1 n2 -- r q )` | Divide with remainder |
| `mod` | `( n1 n2 -- r )` | Modulo |
| `*/mod` | `( n1 n2 n3 -- r q )` | (n1*n2)/n3, remainder and quotient |
| `*/` | `( n1 n2 n3 -- q )` | (n1*n2)/n3, quotient only |
| `**` | `( n1 u -- n2 )` | Power (n1 to power u) |
| `abs` | `( n -- u )` | Absolute value |
| `negate` | `( n1 -- n2 )` | Negate (-n1) |
| `min` | `( n1 n2 -- min )` | Minimum of two |
| `max` | `( n1 n2 -- max )` | Maximum of two |
| `1+` | `( n1 -- n2 )` | Add 1 |
| `1-` | `( n1 -- n2 )` | Subtract 1 |
| `2+` | `( n1 -- n2 )` | Add 2 |
| `2*` | `( n1 -- n2 )` | Multiply by 2 (shift left) |
| `2/` | `( n1 -- n2 )` | Divide by 2 (shift right) |
| `cell+` | `( a -- a )` | Same as `1+` (for address arithmetic) |
| `lshift` | `( n1 n2 -- n3 )` | Logical shift left |
| `rshift` | `( n1 n2 -- n3 )` | Logical shift right |

#### Comparison

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `=` | `( x1 x2 -- f )` | Equal (-1 true, 0 false) |
| `<>` | `( x1 x2 -- f )` | Not equal |
| `<` | `( n1 n2 -- f )` | Less than (n1 < n2) |
| `>` | `( n1 n2 -- f )` | Greater than (n1 > n2) |
| `0=` | `( x -- f )` | Equal to zero |
| `0<` | `( n -- f )` | Less than zero |
| `d=` | `( x1l x1h x2l x2h -- f )` | Double number equal |

#### Bitwise Logical

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `and` | `( x1 x2 -- x3 )` | Bitwise AND |
| `or` | `( x1 x2 -- x3 )` | Bitwise OR |
| `xor` | `( x1 x2 -- x3 )` | Bitwise XOR |
| `invert` | `( x -- x' )` | Bitwise NOT (complement) |

#### Memory Access

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `@` | `( a -- x )` | Fetch cell from address |
| `!` | `( x a -- )` | Store cell at address |
| `c@` | `( a -- c )` | Fetch byte (8-bit) |
| `c!` | `( c a -- )` | Store byte |
| `+!` | `( n a -- )` | Add to memory location |
| `2@` | `( a -- x1 x2 )` | Fetch two cells |
| `2!` | `( x1 x2 a -- )` | Store two cells |
| `count` | `( a -- a+1 c )` | Load byte and advance address |
| `skim` | `( a -- a+1 x )` | Load cell and advance address |
| `exchange` | `( x a -- x' )` | Swap x with memory at a |

#### Memory Operations

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `fill` | `( a n c -- )` | Fill n locations at a with c |
| `move` | `( a1 a2 n -- )` | Move n cells from a1 to a2 |
| `compare` | `( a1 n1 a2 n2 -- -1\|0\|1 )` | Compare two strings |
| `scan` | `( a n1 c -- n2 )` | Scan for character c |
| `skip` | `( a n1 c -- n2 )` | Skip leading occurrences of c |

#### Input/Output

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `emit` | `( c -- )` | Output character with ASCII c |
| `space` | `( -- )` | Output space character |
| `spaces` | `( n -- )` | Output n spaces |
| `cr` | `( -- )` | Output newline |
| `type` | `( a n -- )` | Output string of length n at address a |
| `key` | `( -- c )` | Read character, return ASCII code |
| `key?` | `( -- f )` | Check for key available (0 or -1) |
| `accept` | `( a n -- n' )` | Read up to n characters into buffer at a |

#### Base Conversion

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `hex` | `( -- )` | Set base to 16 |
| `decimal` | `( -- )` | Set base to 10 |
| `binary` | `( -- )` | Set base to 2 |
| `.` | `( n -- )` | Print number respecting current base |
| `..` | `( n -- )` | Print raw value (debug) |

#### Pictured Numeric Output

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `<#` | `( n -- 0 n f )` | Start pictured output |
| `#` | `( n1 n2 f -- c n3 n4 f )` | Convert one digit |
| `#s` | `( n1 n2 f -- ??? n3 n4 f )` | Convert remaining digits |
| `#>` | `( ??? n1 n2 f -- a n3 )` | End pictured output |
| `#>type` | `( n1 -- )` | Output pictured number |
| `sign` | `( n1 n2 f -- n3 n4 f )` | Add sign if negative |
| `hold` | `( n1 n2 f c -- c n3 n4 f )` | Insert character in output |

#### Exception Handling

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `catch` | `( a -- n )` | Execute address, catch exceptions |
| `throw` | `( n -- )` | Throw exception n |
| `abort` | `( -- )` | Throw exception -2 |

#### Dictionary Access

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `@` | `( a -- x )` | Fetch from address |
| `!` | `( x a -- )` | Store to address |
| `here` | `( -- a )` | Get end-of-code address (HERE) |
| `pad` | `( -- a )` | Get scratch pad address |
| `>body` | `( xt -- a )` | Get body address from execution token |
| `body>` | `( a -- xt )` | Get execution token from body |
| `>name` | `( xt -- n )` | Get word number from execution token |
| `name>` | `( n -- xt )` | Get execution token from word number |
| `>in` | `( -- a )` | Get input pointer address |
| `state` | `( -- a )` | Get compilation state address |
| `last` | `( -- a )` | Get address of last word's CFA |
| `base` | `( -- a )` | Get base address |

#### High-Level Compilation

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `,` | `( x -- )` | Compile cell |
| `c,` | `( c -- )` | Compile byte |
| `allot` | `( n -- )` | Reserve n memory locations |
| `[` | `( -- )` | Turn off compilation (immediate) |
| `]` | `( -- )` | Turn on compilation |

### 2. High-Level Words (Compiled Forth)

Words defined in terms of other words, compiled to bytecode sequences.

#### System Words

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `nop` | `( -- )` | No operation |
| `cells` | `( n -- n )` | Convert cells to address offset (identity in bashforth) |
| `chars` | `( n -- n )` | Convert chars to address offset (identity in bashforth) |
| `quit` | `( -- )` | Enter command interpreter loop (deferred) |
| `warm` | `( -- )` | Warm start (deferred) |
| `cold` | `( -- )` | Cold start |
| `hello` | `( -- )` | Display welcome message |
| `license` | `( -- )` | Display GPL license |
| `bye` | `( -- )` | Exit Forth |

#### Control Flow

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `if` | `( f -- )` | Start conditional (immediate) |
| `then` | `( -- )` | End conditional (immediate) |
| `else` | `( -- )` | Alternative branch (immediate) |
| `begin` | `( -- )` | Start loop (immediate) |
| `until` | `( f -- )` | Loop until true (immediate) |
| `while` | `( f -- )` | Loop while true (immediate) |
| `repeat` | `( -- )` | End while loop (immediate) |
| `again` | `( -- )` | Unconditional loop (immediate) |
| `do` | `( limit start -- )` | Start indexed loop (immediate) |
| `?do` | `( limit start -- )` | Conditional loop (immediate) |
| `loop` | `( -- )` | Increment loop counter (immediate) |
| `+loop` | `( n -- )` | Add n to loop counter (immediate) |
| `leave` | `( -- )` | Exit current loop (immediate) |
| `?leave` | `( f -- )` | Exit loop if true (immediate) |
| `for` | `( start -- )` | Countdown loop (immediate) |
| `next` | `( -- )` | End countdown loop (immediate) |

#### Word Definition

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `:` | `( <name> -- )` | Start colon definition (immediate) |
| `;` | `( -- )` | End colon definition (immediate) |
| `:noname` | `( -- a )` | Define unnamed word (immediate) |
| `create` | `( <name> -- )` | Create with runtime action |
| `variable` | `( <name> -- )` | Create a variable |
| `constant` | `( <name> n -- )` | Create a constant |
| `immediate` | `( -- )` | Make most recent word immediate |
| `hide` | `( -- )` | Hide most recent word |
| `reveal` | `( -- )` | Reveal most recent word |

#### Parsing and Compilation

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `'` | `( <name> -- xt )` | Get execution token (immediate) |
| `[']` | `( <name> -- )` | Compile execution token (immediate) |
| `postpone` | `( <name> -- )` | Compile immediate word (immediate) |
| `literal` | `( n -- )` | Compile number (immediate) |
| `compiling` | `( -- f )` | Get compile state |
| `?comp` | `( -- )` | Throw if not compiling |

#### String Literals and Comments

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `"` (s") | `( -- a n )` | String literal (immediate) |
| `."` | `( -- )` | Print string literal (immediate) |
| `(` | `( <comment> )` | Comment (immediate) |
| `\` | `( <comment> )` | Line comment (immediate) |
| `s(` | `( <string> -- a n )` | Interactive string to stack (immediate) |
| `.(` | `( <string> )` | Interactive print string (immediate) |
| `char` | `( <char> -- c )` | Get ASCII of next character (immediate) |
| `[char]` | `( <char> -- )` | Compile ASCII of next char (immediate) |

#### Debugging

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `.s` | `( -- )` | Display stack contents |
| `words` | `( -- )` | List all words |
| `doc` | `( <name> -- )` | Display word documentation |
| `see` | `( <name> -- )` | Show word source |

#### String Stack

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `push$` | `( a n -- )` | Push string to string stack |
| `pop$` | `( -- a n )` | Pop string from string stack |
| `dup$` | `( -- )` | Duplicate top string |
| `drop$` | `( -- )` | Drop top string |
| `swap$` | `( -- )` | Swap two strings |
| `over$` | `( -- )` | Copy second string to top |
| `nip$` | `( -- )` | Drop second string |
| `rot$` | `( -- )` | Rotate strings |
| `2dup$` | `( -- )` | Duplicate two strings |
| `append$` | `( -- )` | Concatenate two strings |
| `sub$` | `( u1 u2 -- )` | Substring (start, length) |
| `left$` | `( u -- )` | Keep leftmost u characters |
| `right$` | `( u -- )` | Keep rightmost u characters |
| `type$` | `( -- )` | Output top string and drop |
| `.s$` | `( -- )` | Display string stack |
| `depth$` | `( -- n )` | Number of strings on stack |

#### Miscellaneous

| Word | Stack Effect | Description |
|------|--------------|-------------|
| `rnd` | `( n -- n' )` | Random 0 to n-1 |
| `secs` | `( n -- )` | Sleep n seconds |
| `ms` | `( n -- )` | Sleep n milliseconds |
| `epoche` | `( -- n )` | Seconds since epoch |
| `nanoseconds` | `( -- n )` | Nanoseconds since epoch |
| `time` | `( xt -- n )` | Measure execution time |
| `time&date` | `( -- s m h d M y )` | Get current time |
| `system` | `( a n -- exit_code )` | Execute shell command |
| `bash` | `( -- )` | Enter bash shell |
| `set` | `( -- )` | Show environment |
| `env` | `( -- )` SS: `( name -- value )` | Get environment variable |

### 3. Immediate Words for Compilation Control

Words that execute at compile time to control code generation.

#### Compilation State Control

| Word | Action |
|------|--------|
| `[` | Turn off compilation (immediate) |
| `]` | Turn on compilation |
| `literal` | Compile top of stack as literal (immediate) |
| `;` | End word definition (immediate) |

#### Control Structure Markers

| Word | Marks | Purpose |
|------|-------|---------|
| `if` | Branch point | Start conditional |
| `then` | Resolution | End conditional |
| `begin` | Loop start | Loop entry point |
| `until` | Branch back | Loop until condition |
| `do` | Loop start | Indexed loop |
| `loop` | Loop end | Increment and branch |

## Memory Organization

### Code Space
Addresses 0 to dp: Contains compiled word bodies

### Variable Space  
Addresses dp to pad: User variables and constants

### Scratch Pad
Addresses pad to pad+TIBSIZE: String buffers, scratch area

### Configuration
- PADAWAY: Distance from HERE to PAD (default 256)
- TIBSIZE: Input buffer size (default 256)

## Compilation Details

### Nested Colon Definitions

```
: add5 5 + ;
```

Compiled as:
```
nest
lit (5)
plus
unnest
```

### Immediate Words in Colon Definitions

```
: test [ 1 2 + ] literal ;
```

The `[ 1 2 + ]` executes at compile time, pushing 3 to stack, then `literal` compiles it.

### Control Structures

```
: abs dup 0< if negate then ;
```

Compiled with branch offsets resolved after compilation.

## Error Codes (Exceptions)

Standard exception codes (negative numbers):

```
-1   Terminated
-2   Aborted  
-4   Stack underflow
-9   Invalid memory address
-10  Division by zero
-13  Word not found
-14  Compile-only word used in interpretation
-21  Unsupported operation
-22  Unstructured (mismatched control structure)
-24  Invalid numeric argument
-26  Loop parameters unavailable
-38  File not found
-65  String stack underflow
```

## Stack Sizes and Limits

- Data stack: Configurable, typically 256+ elements
- Return stack: Configurable, typically 256+ elements  
- String stack: Configurable, typically 256+ strings
- Memory: Limited by available bash array space
- Dictionary: Limited by word count

## Constants Defined

| Constant | Value | Meaning |
|----------|-------|---------|
| `true` | -1 | Boolean true |
| `false` | 0 | Boolean false |
| `0` | 0 | Zero |
| `1` | 1 | One |
| `2` | 2 | Two |
| `3` | 3 | Three |
| ... | ... | ... |
| `6` | 6 | Six |
| `bl` | 32 | Space character |
| `esc` | 27 | Escape character |
| `nlimit` | 2^31-1 | Max signed integer |
| `cell` | 1 | Cell size in "cells" |

## Input/Output Configuration

- `PROMPT`: Prompt string (default "ok")
- `EDITOR`: External editor for edit command
- `sources`: Directory for include files

## Summary

Bashforth implements ~150+ words covering:
- 30+ stack operations
- 40+ arithmetic/comparison/bitwise
- 20+ memory operations  
- 15+ I/O operations
- 20+ control flow constructs
- 15+ dictionary/compilation words
- 15+ string stack operations
- Various system and utility words

All implemented either as bash primitives or high-level Forth definitions.
