# Standard Words Reference
## Complete Dictionary of GD-FORTH Words

**Version**: 0.0.1  
**Last Updated**: 2026-07-30

---

## Table of Contents

1. [Arithmetic Operations](#arithmetic-operations)
2. [Stack Operations](#stack-operations)
3. [Comparison Operations](#comparison-operations)
4. [Logical Operations](#logical-operations)
5. [I/O Operations](#io-operations)
6. [Control Flow](#control-flow)
7. [Word Definition](#word-definition)
8. [Advanced Operations](#advanced-operations)

---

## Arithmetic Operations

### Addition: `+`

**Stack Effect**: `( n1 n2 -- sum )`  
**Description**: Pops two numbers, adds them, pushes result

**Examples**:
```forth
5 3 +      -- 8
10 -3 +    -- 7
0 42 +     -- 42
```

**Implementation**:
```java
// Pops: n1 (top), n2 (below)
// Computes: n2 + n1
// Pushes result
```

---

### Subtraction: `-`

**Stack Effect**: `( n1 n2 -- difference )`  
**Description**: Pops n1, then n2; computes n2 - n1; pushes result

**Examples**:
```forth
20 5 -     -- 15 (20 - 5)
5 20 -     -- -15 (5 - 20)
-10 5 -    -- -15 (-10 - 5)
```

**Order is Important**:
```forth
A B -  ⟹  A - B   (NOT B - A)
```

---

### Multiplication: `*`

**Stack Effect**: `( n1 n2 -- product )`  
**Description**: Pops two numbers, multiplies them, pushes result

**Examples**:
```forth
6 7 *      -- 42
-5 3 *     -- -15
0 999 *    -- 0
```

**Special Cases**:
```forth
N 0 *      -- Always 0
N 1 *      -- Always N
N -1 *     -- Negates N
```

---

### Division: `/`

**Stack Effect**: `( n1 n2 -- quotient )`  
**Description**: Integer division. n2 / n1, result truncated

**Examples**:
```forth
20 4 /     -- 5 (20 / 4)
10 3 /     -- 3 (10 / 3, truncated)
-20 4 /    -- -5 (-20 / 4)
```

**Errors**:
```forth
10 0 /     -- ERROR: / by zero
```

**Truncation**:
- `10 3 /` → 3 (not 3.33...)
- `7 2 /` → 3 (not 3.5)

---

### Modulo/Remainder: `MOD`

**Stack Effect**: `( n1 n2 -- remainder )`  
**Description**: Integer modulo operation. n2 MOD n1

**Examples**:
```forth
17 5 MOD   -- 2 (17 mod 5)
-17 5 MOD  -- -2 (sign follows dividend)
10 5 MOD   -- 0 (exact division)
```

**Errors**:
```forth
10 0 MOD   -- ERROR: / by zero
```

**Properties**:
- Result sign matches dividend (n2)
- Useful for finding remainders and cycles

---

## Stack Operations

### Duplicate: `DUP`

**Stack Effect**: `( n -- n n )`  
**Description**: Duplicates the top stack value

**Examples**:
```forth
5 DUP      -- Stack: [5 5]
0 DUP      -- Stack: [0 0]
```

**Use Cases**:
- Save a value for multiple operations
- Check condition without consuming value

---

### Drop: `DROP`

**Stack Effect**: `( n -- )`  
**Description**: Removes and discards the top stack value

**Examples**:
```forth
5 3 DROP   -- Stack: [5]
10 20 30 DROP -- Stack: [10 20]
```

**Use Cases**:
- Discard intermediate results
- Clean up stack

---

### Swap: `SWAP`

**Stack Effect**: `( n1 n2 -- n2 n1 )`  
**Description**: Exchanges the top two stack values

**Examples**:
```forth
5 3 SWAP   -- Stack: [3 5]
```

**Before/After**:
```
Before: [ ... 5 3 ]
After:  [ ... 3 5 ]
```

---

### Over: `OVER`

**Stack Effect**: `( n1 n2 -- n1 n2 n1 )`  
**Description**: Copies the second stack value to the top

**Examples**:
```forth
5 3 OVER   -- Stack: [5 3 5]
```

**Visualization**:
```
Before: [ ... 5 3 ]
After:  [ ... 5 3 5 ]
```

---

### Rotate: `ROT`

**Stack Effect**: `( n1 n2 n3 -- n2 n3 n1 )`  
**Description**: Rotates the top three stack values

**Examples**:
```forth
5 3 2 ROT  -- Stack: [3 2 5]
```

**Visualization**:
```
Before: [ ... 5 3 2 ]
After:  [ ... 3 2 5 ]
```

---

## Comparison Operations

### Greater Than: `>`

**Stack Effect**: `( n1 n2 -- flag )`  
**Description**: Compares n2 > n1; pushes true/false

**Examples**:
```forth
5 3 > .    -- true (5 > 3)
3 5 > .    -- false (3 > 5)
5 5 > .    -- false (5 not > 5)
```

---

### Less Than: `<`

**Stack Effect**: `( n1 n2 -- flag )`  
**Description**: Compares n2 < n1; pushes true/false

**Examples**:
```forth
3 5 < .    -- true (3 < 5)
5 3 < .    -- false (5 < 3)
5 5 < .    -- false (5 not < 5)
```

---

### Equal: `=`

**Stack Effect**: `( n1 n2 -- flag )`  
**Description**: Checks if n2 = n1

**Examples**:
```forth
5 5 = .    -- true
5 3 = .    -- false
-1 -1 = .  -- true
```

---

### Not Equal: `<>`

**Stack Effect**: `( n1 n2 -- flag )`  
**Description**: Checks if n2 ≠ n1

**Examples**:
```forth
5 3 <> .   -- true
5 5 <> .   -- false
```

---

### Less or Equal: `<=`

**Stack Effect**: `( n1 n2 -- flag )`  
**Description**: Compares n2 ≤ n1

**Examples**:
```forth
5 5 <= .   -- true
3 5 <= .   -- true
5 3 <= .   -- false
```

---

### Greater or Equal: `>=`

**Stack Effect**: `( n1 n2 -- flag )`  
**Description**: Compares n2 ≥ n1

**Examples**:
```forth
5 5 >= .   -- true
5 3 >= .   -- true
3 5 >= .   -- false
```

---

## Logical Operations

### AND: `AND`

**Stack Effect**: `( flag1 flag2 -- result )`  
**Description**: Logical AND of two boolean values

**Examples**:
```forth
1 1 AND .  -- true
1 0 AND .  -- false
0 0 AND .  -- false
```

---

### OR: `OR`

**Stack Effect**: `( flag1 flag2 -- result )`  
**Description**: Logical OR of two boolean values

**Examples**:
```forth
1 0 OR .   -- true
0 0 OR .   -- false
1 1 OR .   -- true
```

---

### NOT: `NOT`

**Stack Effect**: `( flag -- inverted )`  
**Description**: Logical NOT - inverts boolean value

**Examples**:
```forth
0 NOT .    -- true (0 is false)
1 NOT .    -- false (1 is true)
```

---

## I/O Operations

### Print: `.`

**Stack Effect**: `( n -- )`  
**Description**: Print the top stack value and remove it

**Examples**:
```forth
42 .       -- Output: 42
5 3 + .    -- Output: 8
```

**Note**: The value is **consumed** (removed from stack)

---

### Print and Keep: `.S`

**Stack Effect**: `( ... -- ... )`  
**Description**: Print entire stack without modifying it

**Examples**:
```forth
5 3 2 .S   -- Output: [5 3 2]
```

**Non-destructive**: Stack remains unchanged

---

### Carriage Return: `CR`

**Stack Effect**: `( -- )`  
**Description**: Print a newline

**Examples**:
```forth
10 . CR 20 .    -- Output: 10\n20
```

---

### Print String: `PRINT`

**Stack Effect**: `( -- )`  
**Description**: Print to console

**Examples**:
```forth
PRINT "Hello World"
```

---

## Control Flow

### Conditional: `IF...THEN`

**Stack Effect**: `( flag -- )`  
**Syntax**:
```forth
condition IF
  ... true branch ...
THEN
```

**Description**: Execute code only if condition is true

**Examples**:
```forth
5 3 > IF 100 . THEN    -- Prints 100
3 5 > IF 100 . THEN    -- Prints nothing
```

---

### Loop: `DO...LOOP`

**Stack Effect**: `( end start -- )`  
**Syntax**:
```forth
end start DO
  ... loop body (use I for counter) ...
LOOP
```

**Description**: Loop from start to end

**Examples**:
```forth
10 0 DO I . LOOP       -- Prints: 0 1 2 3 4 5 6 7 8 9
5 1 DO I . LOOP        -- Prints: 1 2 3 4
```

---

### Exit: `EXIT`

**Stack Effect**: `( -- )`  
**Description**: Exit current word immediately

**Examples**:
```forth
: BOUNDED-SQRT ( n -- result )
  DUP 0 < IF 0 EXIT THEN
  SQRT
;
```

---

## Word Definition

### Define: `:`

**Syntax**:
```forth
: word-name ( stack-effects )
  ... word body ...
;
```

**Description**: Define a new word

**Examples**:
```forth
: SQUARE ( n -- n² )
  DUP * ;

: TRIPLE ( n -- n*3 )
  3 * ;

: AVERAGE ( a b -- avg )
  + 2 / ;
```

---

### End Definition: `;`

**Syntax**: Closes a word definition (see `:` above)

---

## Advanced Operations

### Shift Left: `LSHIFT`

**Stack Effect**: `( n bits -- result )`  
**Description**: Bit shift left (multiply by 2^bits)

**Examples**:
```forth
5 2 LSHIFT .   -- 20 (5 * 2²)
1 3 LSHIFT .   -- 8  (1 * 2³)
```

---

### Shift Right: `RSHIFT`

**Stack Effect**: `( n bits -- result )`  
**Description**: Bit shift right (divide by 2^bits)

**Examples**:
```forth
20 2 RSHIFT .  -- 5  (20 / 2²)
16 3 RSHIFT .  -- 2  (16 / 2³)
```

---

### Power: `**` or `^`

**Stack Effect**: `( base exponent -- result )`  
**Description**: Exponentiation

**Examples**:
```forth
2 3 ** .       -- 8  (2³)
10 2 ** .      -- 100 (10²)
```

---

### Absolute Value: `ABS`

**Stack Effect**: `( n -- |n| )`  
**Description**: Absolute value

**Examples**:
```forth
-5 ABS .       -- 5
42 ABS .       -- 42
0 ABS .        -- 0
```

---

### Negate: `NEGATE`

**Stack Effect**: `( n -- -n )`  
**Description**: Negate a number (multiply by -1)

**Examples**:
```forth
5 NEGATE .     -- -5
-3 NEGATE .    -- 3
```

---

## Word Classification

### Primitive Words (Built-in)

These cannot be defined in FORTH - they're implemented in Java:
- Arithmetic: `+`, `-`, `*`, `/`, `MOD`
- Stack: `DUP`, `DROP`, `SWAP`, `OVER`, `ROT`
- Comparison: `>`, `<`, `=`, `<=`, `>=`, `<>`
- Logic: `AND`, `OR`, `NOT`
- I/O: `.`, `.S`, `CR`, `PRINT`

### Defined Words

User or library-defined words built from primitives:
```forth
: SQUARE DUP * ;
: DOUBLE 2 * ;
: NEGATE 0 SWAP - ;
```

---

## Word Lookup and Scope

### Scope Rules

1. **Local Scope**: Words defined in REPL are available immediately
2. **Global Scope**: All defined words are globally visible
3. **No Shadowing**: Redefining a word overwrites the old definition

### Example

```forth
: TEST 10 . ;    -- Define TEST
TEST             -- Works: prints 10
: TEST 20 . ;    -- Redefine TEST
TEST             -- Works: prints 20
```

---

## Error Handling by Word

| Word | Error Conditions | Exception |
|------|------------------|-----------|
| `+`, `-`, `*` | Stack underflow | EmptyStackException |
| `/` | Divide by zero | ArithmeticException |
| `MOD` | Modulo by zero | ArithmeticException |
| `DUP`, `DROP`, `SWAP` | Stack underflow | EmptyStackException |
| `.` | Stack empty | EmptyStackException |
| Comparison | Wrong types | ClassCastException |

---

## Performance Notes

| Word | Time | Space |
|------|------|-------|
| Arithmetic | O(1) | O(1) |
| Stack Ops | O(1) | O(1) |
| Comparison | O(1) | O(1) |
| Word Lookup | O(log n) | O(1) |
| Definition | O(n) parse | O(n) store |

---

## Standard Words Summary

```
Arithmetic:     +  -  *  /  MOD  ABS  NEGATE  **
Stack:          DUP  DROP  SWAP  OVER  ROT
Comparison:     >  <  =  <=  >=  <>
Logic:          AND  OR  NOT
I/O:            .  .S  CR  PRINT
Control:        IF  THEN  DO  LOOP  EXIT
Definition:     :  ;
Bit ops:        LSHIFT  RSHIFT
```

---

## See Also

- `01_LANGUAGE_GUIDE.md` — Language tutorial
- `02_IMPLEMENTATION_GUIDE.md` — Technical details
- `04_EXAMPLES.md` — Usage examples
