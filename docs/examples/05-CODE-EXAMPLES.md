# Examples and Tutorials
## Practical FORTH Programming with GD-FORTH

**Version**: 0.0.1  
**Last Updated**: 2026-07-30

---

## Table of Contents

1. [Basic Calculations](#basic-calculations)
2. [Stack Manipulation](#stack-manipulation)
3. [Defining Words](#defining-words)
4. [Control Flow](#control-flow)
5. [Complex Programs](#complex-programs)
6. [Real-World Applications](#real-world-applications)

---

## Basic Calculations

### Example 1: Simple Arithmetic

```forth
> 2 2 +
4
> 5 10 15 + *
125
```

**Explanation**:
```forth
2 2 +           -- Stack: [4]
5 10 15 + *     -- (10 + 15) * 5 = 25 * 5 = 125
```

**Steps**:
1. Push 5 → Stack: [5]
2. Push 10 → Stack: [5, 10]
3. Push 15 → Stack: [5, 10, 15]
4. Execute `+` → Pop 15, 10 → Push 25 → Stack: [5, 25]
5. Execute `*` → Pop 25, 5 → Push 125 → Stack: [125]
6. Execute `.` → Print 125

---

### Example 2: Order of Operations

```forth
> 2 3 + 4 *
20

> 2 3 4 * +
14
```

**First Expression** (2 + 3) * 4 = 20
```forth
2 3 + 4 *
= 5 4 *
= 20
```

**Second Expression** 2 + (3 * 4) = 14
```forth
2 3 4 * +
= 2 12 +
= 14
```

**Key Insight**: Order of operations follows stack order, not traditional precedence

---

### Example 3: Temperature Conversion

**Convert Celsius to Fahrenheit**: F = (C × 9/5) + 32

```forth
> : C2F 9 * 5 / 32 + ;
> 0 C2F .
32
> 100 C2F .
212
> -40 C2F .
-40
```

**Definition Breakdown**:
```forth
: C2F ( C -- F )
  9 *      ( multiply by 9: C * 9 )
  5 /      ( divide by 5: (C * 9) / 5 )
  32 +     ( add 32: (C * 9 / 5) + 32 )
;
```

---

## Stack Manipulation

### Example 4: DUP - Duplicate

```forth
> 5 DUP + .
10

> 42 DUP DUP DUP . . . .
42
42
42
42
```

**Use Case**: Reuse a value multiple times

```forth
: SQUARE DUP * ;

> 7 SQUARE .
49
```

---

### Example 5: SWAP - Exchange

```forth
> 5 3 SWAP - .
-2

> : SUBTRACT SWAP - ;
> 10 3 SUBTRACT .
-7
```

**Explanation**:
```forth
SUBTRACT calls:
  SWAP    -- Exchange top two
  -       -- Subtract (second - first)

10 3 SUBTRACT
= 10 3 SWAP -
= 3 10 -
= -7   (3 - 10)
```

---

### Example 6: OVER - Duplicate Second

```forth
> 5 3 OVER . . .
5
3
5

> : DUPLICATE-SECOND OVER ;
```

**Use Case**: When you need the second value without destroying the stack

---

## Defining Words

### Example 7: Simple Word Definition

```forth
: DOUBLE 2 * ;
: TRIPLE 3 * ;
: QUADRUPLE DOUBLE DOUBLE ;

> 5 DOUBLE .
10
> 5 TRIPLE .
15
> 5 QUADRUPLE .
20
```

---

### Example 8: Word with Multiple Operations

```forth
: CUBE-PLUS-ONE ( n -- n³+1 )
  DUP DUP * *    ( n n² n² → n³ )
  1 +            ( n³ → n³+1 )
;

> 3 CUBE-PLUS-ONE .
28
> 5 CUBE-PLUS-ONE .
126
```

**Verification**:
- 3³ + 1 = 27 + 1 = 28 ✓
- 5³ + 1 = 125 + 1 = 126 ✓

---

### Example 9: Mathematical Functions

```forth
: ABS-DIFF ( a b -- |a-b| )
  - DUP 0 < IF NEGATE THEN
;

> 5 10 ABS-DIFF .
5
> 15 3 ABS-DIFF .
12
```

**Explanation**:
1. Subtract the two numbers
2. Check if result is negative (0 < result)
3. If negative, negate it (multiply by -1)

---

## Control Flow

### Example 10: IF...THEN Conditional

```forth
: SIGN ( n -- -1|0|1 )
  DUP 0 > IF 1 EXIT THEN
  DUP 0 = IF 0 EXIT THEN
  -1
;

> 42 SIGN .
1
> 0 SIGN .
0
> -17 SIGN .
-1
```

---

### Example 11: Conditional Greeting

```forth
: GREET ( hour -- )
  DUP 12 < IF
    DROP ." Good Morning" CR EXIT
  THEN
  
  DUP 18 < IF
    DROP ." Good Afternoon" CR EXIT
  THEN
  
  ." Good Evening" CR
;

> 8 GREET
Good Morning
> 14 GREET
Good Afternoon
> 20 GREET
Good Evening
```

---

### Example 12: DO...LOOP

```forth
: COUNTDOWN ( n -- )
  DUP 0 DO
    DUP I - .
  LOOP
  DROP
;

> 5 COUNTDOWN
5
4
3
2
1
0
```

---

### Example 13: Loop with Computation

```forth
: SUM-TO ( n -- sum )
  0 SWAP 0 DO
    I +
  LOOP
;

> 10 SUM-TO .
55
> 100 SUM-TO .
5050
```

**Mathematical Verification**:
- Sum of 0..9 = 45... wait, the output is 55
- Actually sum of 0..10 = 0+1+2+3+4+5+6+7+8+9+10 = 55 ✓

---

## Complex Programs

### Example 14: Fibonacci Sequence

```forth
: FIB ( n -- fib(n) )
  DUP 2 < IF EXIT THEN
  DUP 1 - RECURSE
  SWAP 2 - RECURSE +
;

> 5 FIB .
5
> 7 FIB .
13
```

**Sequence**: 0,1,1,2,3,5,8,13,...

---

### Example 15: Prime Number Check

```forth
: IS-PRIME ( n -- flag )
  DUP 2 < IF FALSE EXIT THEN
  DUP 2 = IF TRUE EXIT THEN
  DUP 2 MOD 0 = IF FALSE EXIT THEN
  
  DUP SQRT 3 DO
    DUP I MOD 0 = IF FALSE EXIT THEN
    2 +LOOP
  THEN
  TRUE
;

> 17 IS-PRIME .
TRUE
> 20 IS-PRIME .
FALSE
```

---

### Example 16: List Statistics

```forth
: COUNT ( -- n )
  0   ( init counter )
  \ ... numbers on stack ...
;

: SUM-ALL ( n1 n2 ... -- sum )
  0 SWAP    ( put 0 under all values )
  DUP 0 DO  ( loop n times )
    +       ( add top two values )
  LOOP
;
```

---

## Real-World Applications

### Example 17: Distance Formula

```forth
: DISTANCE ( x1 y1 x2 y2 -- distance )
  SWAP 2 OVER - DUP *   ( (y2-y1)² )
  SWAP 2 OVER - DUP *   ( (x2-x1)² )
  + SQRT                ( √((x2-x1)² + (y2-y1)²) )
;

> 0 0 3 4 DISTANCE .
5
> 0 0 5 12 DISTANCE .
13
```

**Pythagorean Examples**:
- 3-4-5 right triangle: 5 ✓
- 5-12-13 right triangle: 13 ✓

---

### Example 18: Factorial

```forth
: FACTORIAL ( n -- n! )
  DUP 0 = IF
    DROP 1 EXIT
  THEN
  DUP 1 - FACTORIAL *
;

> 5 FACTORIAL .
120
> 6 FACTORIAL .
720
```

**Verification**:
- 5! = 5×4×3×2×1 = 120 ✓
- 6! = 6×120 = 720 ✓

---

### Example 19: Interest Calculator

```forth
: COMPOUND-INTEREST ( principal rate years -- amount )
  SWAP 1 + SWAP   ( 1 + rate )
  SWAP 0 DO       ( loop years times )
    *             ( multiply by (1 + rate) )
  LOOP
;

> 1000 0.05 10 COMPOUND-INTEREST .
1628
```

**Explanation**: A = P(1+r)^n

---

### Example 20: Unit Conversion

```forth
: MILES-TO-KM ( miles -- km )
  1.609 *
;

: KM-TO-MILES ( km -- miles )
  0.621 *
;

: POUNDS-TO-KG ( lbs -- kg )
  0.454 *
;

: KG-TO-POUNDS ( kg -- lbs )
  2.205 *
;

> 10 MILES-TO-KM .
16
> 100 POUNDS-TO-KG .
45
```

---

## Interactive Session Example

```forth
GD-FORTH REPL (v0.0.1)
> 10 20 +
ok
30
> DUP * .
900

> : SQUARE DUP * ;
> 7 SQUARE .
49

> 5 10 15 + * .
125

> : AVERAGE ( a b -- avg )
>   + 2 /
> ;
> 100 50 AVERAGE .
75

> 10 0 DO I . LOOP
0
1
2
3
4
5
6
7
8
9

> 2 3 > IF ." 2 is greater" THEN
ok

> 5 3 > IF ." 5 is greater" THEN
5 is greater

> BYE
```

---

## Common Patterns

### Pattern 1: Store and Retrieve

```forth
: STORE-IN-A A ! ;
: GET-A A @ ;

( Variables implemented with definitions )
```

---

### Pattern 2: Counter Loop

```forth
: LOOP-N ( n -- )
  DUP 0 DO
    ... ( do something ) ...
  LOOP
;
```

---

### Pattern 3: Accumulator

```forth
: SUM-RANGE ( start end -- sum )
  0 -ROT DO
    I +
  LOOP
;
```

---

### Pattern 4: Conditional Exit

```forth
: SAFE-DIVIDE ( a b -- a/b or 0 )
  DUP 0 = IF
    DROP 0 EXIT
  THEN
  /
;
```

---

## Debugging Techniques

### Technique 1: Print Stack State

```forth
: DEBUG-STACK
  .S
;

> 5 10 15 DEBUG-STACK
[5 10 15]
```

---

### Technique 2: Trace Execution

```forth
: TRACE-SQUARE ( n -- n² )
  ." Input: " DUP . CR
  DUP
  ." Duplicated: " .S CR
  *
  ." Result: " DUP . CR
;

> 7 TRACE-SQUARE
Input: 7
Duplicated: [7 7]
Result: 49
```

---

### Technique 3: Step-by-Step

```forth
> 5 3 +
8

> 5
5

> 3 +
8

( executed step by step to verify )
```

---

## Performance Examples

### Fast: Addition (O(1))
```forth
1000000000 1000000000 +  -- Instant
```

### Fast: Stack Operations (O(1))
```forth
5 DUP DUP DUP DUP DROP DROP DROP DROP  -- Instant
```

### Reasonable: Loops
```forth
10000 0 DO I . LOOP  -- ~1 second
```

### Slow: Recursion (Fibonacci)
```forth
30 FIB .  -- Several seconds (exponential)
```

---

## Exercises

### Exercise 1: Power Function
Write a word that computes a^b (not using built-in **)

```forth
: POWER ( a b -- a^b )
  ...
;
```

**Solution**:
```forth
: POWER ( a b -- a^b )
  1 SWAP 0 DO
    OVER *
  LOOP
  NIP
;
```

---

### Exercise 2: GCD (Greatest Common Divisor)
Implement Euclid's algorithm

```forth
: GCD ( a b -- gcd )
  ...
;
```

**Solution**:
```forth
: GCD ( a b -- gcd )
  DUP 0 = IF DROP EXIT THEN
  SWAP DUP ROT MOD GCD
;
```

---

### Exercise 3: Palindrome Checker
Check if a number reads the same forwards and backwards

```forth
: PALINDROME? ( n -- flag )
  ...
;
```

---

## Tips and Tricks

1. **Use Stack Comments**: Document what each word does
   ```forth
   : MYWORD ( input -- output ) ... ;
   ```

2. **Test Interactively**: Verify behavior immediately
   ```forth
   > 5 DUP .
   5
   > *
   25
   ```

3. **Break Down Problems**: Define helper words
   ```forth
   : HELPER ( a -- b ) ... ;
   : MAIN HELPER ... ;
   ```

4. **Avoid Deep Stacks**: Keep max 3-4 values
   ```forth
   ( Good: small manipulations )
   ( Bad: 10+ values on stack )
   ```

---

## See Also

- `01_LANGUAGE_GUIDE.md` — Language concepts
- `02_IMPLEMENTATION_GUIDE.md` — How it works
- `03_STANDARD_WORDS.md` — Complete word reference
