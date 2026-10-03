# DO Loop User Guide

## Introduction

The DO loop is a fundamental control structure in FORTH for repeating a block of code a specific number of times. It's similar to a "for" loop in other programming languages.

This guide explains how to use DO loops in GD-FORTH with practical examples.

## Basic Syntax

```forth
start limit DO ... LOOP
```

Where:
- **start**: The initial loop index (pushed to stack first)
- **limit**: The loop limit (pushed to stack second)
- **...**: Statements to repeat (the loop body)
- **LOOP**: Increment the index by 1 and continue

## Simple Examples

### Example 1: Count from 0 to 4

```forth
0 5 DO I LOOP
```

Output:
```
0 1 2 3 4
```

**Explanation**:
- `0 5 DO` — Start with index 0, loop while index < 5
- `I` — Print the current index
- `LOOP` — Increment index by 1, continue if index < 5

### Example 2: Print "Hello" 3 times

```forth
0 3 DO ." Hello" CR LOOP
```

Output:
```
Hello
Hello
Hello
```

**Explanation**:
- `0 3 DO` — Loop 3 times (index 0, 1, 2)
- `." Hello"` — Print the text "Hello"
- `CR` — Print a newline (carriage return)
- `LOOP` — Increment and continue

### Example 3: Sum numbers from 1 to 5

```forth
0 0 6 DO I + LOOP .
```

Output:
```
15
```

**Explanation**:
- `0` — Start with sum = 0
- `0 6 DO` — Loop with index 0 to 5
- `I +` — Add current index to sum
- `LOOP` — Increment and continue
- `.` — Print the final sum (0+0+1+2+3+4+5 = 15)

## Understanding the Loop Index with I

The **I** command pushes the current loop index onto the stack.

```forth
1 5 DO I . LOOP
```

Output:
```
1 2 3 4
```

This prints the index values from 1 to 4 (the loop stops before reaching the limit of 5).

## Nested Loops

You can put DO loops inside other DO loops. Use **I** for the inner loop index and **J** for the outer loop index.

### Example: Multiplication Table

```forth
1 10 DO
  1 10 DO
    I J * .  SPACE
  LOOP
  CR
LOOP
```

This creates a 9×9 multiplication table.

**Explanation**:
- Outer loop: `J` ranges from 1 to 9
- Inner loop: `I` ranges from 1 to 9
- `I J *` — Multiply inner index by outer index
- `SPACE` — Print a space between numbers
- `CR` — Print newline after each row

## Variable Increment with +LOOP

By default, `LOOP` increments the index by 1. If you want a different increment, use `+LOOP` and provide the increment value.

### Example: Count by 2s

```forth
0 10 DO I . 2 +LOOP
```

Output:
```
0 2 4 6 8
```

**Explanation**:
- `0 10 DO` — Start at 0, limit is 10
- `I .` — Print the current index
- `2 +LOOP` — Increment by 2 and continue

### Example: Count backwards

```forth
10 0 DO I . -1 +LOOP
```

Output:
```
10 9 8 7 6 5 4 3 2 1
```

**Explanation**:
- `10 0 DO` — Start at 10, limit is 0
- `-1 +LOOP` — Decrement by 1 (add -1)
- Loop continues while index > 0

## Common Patterns

### Pattern: Store results in variables

```forth
VARIABLE sum
0 sum !  ( Initialize sum to 0 )
0 0 11 DO
  sum @ I + sum !
LOOP
sum @ .  ( Print the sum: 0+1+2+...+10 = 55 )
```

### Pattern: Conditional inside loop

```forth
0 10 DO
  I 5 = IF
    ." Found 5!" CR
  THEN
LOOP
```

### Pattern: Loop through array elements

```forth
VARIABLE arr
VARIABLE idx

( Assume arr contains 10 integers )
0 10 DO
  arr I 4 * + @  ( Get element at index I, assuming 4 bytes per int )
  .
LOOP
```

## Important Rules

1. **Order of Parameters**: The stack order is `start limit DO`
   - `0 5 DO` means start=0, limit=5
   - NOT `5 0 DO`

2. **Loop Condition**: Loop executes while `index < limit`
   - `0 5 DO` executes with index values: 0, 1, 2, 3, 4 (not 5)
   - `5 5 DO` doesn't execute at all (index >= limit immediately)

3. **Empty Loops**: If start >= limit, the loop doesn't execute
   - `5 3 DO I LOOP` — Nothing happens
   - `5 5 DO I LOOP` — Nothing happens

4. **I and J**: Only use inside DO...LOOP blocks
   - `I` — Current loop index (always available)
   - `J` — Outer loop index (only in nested loops)

## Error Messages

### Error: "I used outside DO...LOOP"
**Cause**: You tried to use `I` when not inside a loop
**Solution**: Make sure `I` is between `DO` and `LOOP`

```forth
(* WRONG *)
I .  ( Error! Not in a loop )

(* CORRECT *)
0 5 DO I . LOOP
```

### Error: "J used outside nested loop"
**Cause**: You tried to use `J` in a single-level loop
**Solution**: Nest the loops properly, or remove `J`

```forth
(* WRONG *)
0 5 DO J . LOOP  ( Error! No outer loop for J )

(* CORRECT *)
0 5 DO 0 3 DO I J . LOOP LOOP  ( Now J works )
```

## Advanced Examples

### Example: Factorial (5! = 120)

```forth
: factorial  ( n -- n! )
  1           ( Start with result = 1 )
  1 SWAP 1 + DO
    I *       ( Multiply result by loop index )
  LOOP
;

5 factorial .  ( Prints: 120 )
```

### Example: Sum of squares

```forth
0                ( Sum starts at 0 )
0 6 DO
  I I * +       ( Add I² to sum )
LOOP
.               ( Print result: 0²+1²+2²+3²+4²+5² = 55 )
```

### Example: Print checkerboard pattern

```forth
0 8 DO
  0 8 DO
    I J + 2 MOD IF
      ." # "
    ELSE
      ." . "
    THEN
  LOOP
  CR
LOOP
```

## Tips and Tricks

1. **Create a loop variable**: Use a word to hide the loop structure
   ```forth
   : REPEAT  ( n -- )
     0 SWAP DO ... LOOP
   ;
   ```

2. **Loop a specific number of times**: Start at 0, limit at count
   ```forth
   5 0 DO ... LOOP  ( Repeat 5 times )
   ```

3. **Access loop index in calculations**:
   ```forth
   0 10 DO
     I I * .  ( Print I² )
   LOOP
   ```

## Next Steps

- Learn about nested loops in the nested loops section
- Learn about variables to store results
- Combine loops with IF...THEN for conditional execution
- Create reusable loop patterns with colon definitions

## See Also

- `IF...THEN` — Conditional execution
- `VARIABLE` — Store values
- `@` and `!` — Read and write memory
- `SPACE` and `CR` — Formatting output
