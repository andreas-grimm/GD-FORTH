# Test 2.1: Basic Stack Manipulation

## Overview

The **test-stack-basics.fs** file validates the core stack manipulation operations that are fundamental to all Forth programming: DUP, DROP, SWAP, OVER, and ROT. These operations are essential because virtually all other tests depend on them.

## Test Cases

1. **DUP (duplicate)**: Copy top stack item
2. **DROP (discard)**: Remove top stack item
3. **SWAP (exchange)**: Exchange top two items
4. **OVER (copy second)**: Copy second item to top
5. **ROT (rotate)**: Rotate top three items
6. **Combined operations**: Multiple stack ops in sequence

## What Is Being Tested

- **DUP correctness**: Duplicates top item without consuming it
- **DROP correctness**: Removes top item without affecting others
- **SWAP correctness**: Exchanges exactly the top two items
- **OVER correctness**: Copies the second item (not the first)
- **ROT correctness**: Rotates the top three items correctly
- **Stack isolation**: Operations don't affect items below the working set
- **Order preservation**: Items maintain relative order (when appropriate)

## Structure of Test Cases

Each stack operation test uses the pattern:

```forth
T{ <initial-stack> <operation> -> <expected-result> }T
```

### Stack Notation

Stack diagrams use the convention: `( before -- after )`
- Left of `--` shows initial stack (top on right)
- Right of `--` shows final stack (top on right)

## Code Documentation

### DUP Operation

```forth
T{ 5 DUP -> 5 5 }T
```
**Operation**: ( 5 -- 5 5 )
- Takes top item (5)
- Duplicates it
- Leaves two copies on stack
- Original item is not consumed

```forth
T{ 0 DUP -> 0 0 }T
T{ -3 DUP -> -3 -3 }T
T{ 1 2 DUP -> 1 2 2 }T
```
DUP works with:
- Zero (special case)
- Negative numbers
- Multiple items on stack (only top is duplicated)

### DROP Operation

```forth
T{ 5 DROP -> }T
```
**Operation**: ( 5 -- )
- Removes top item from stack
- Nothing remains
- No other items are affected

```forth
T{ 1 2 DROP -> 1 }T
T{ 1 2 3 DROP -> 1 2 }T
```
DROP works with multiple items:
- Only the topmost item is removed
- Lower items remain in order

### SWAP Operation

```forth
T{ 1 2 SWAP -> 2 1 }T
```
**Operation**: ( 1 2 -- 2 1 )
- Exchanges the top two items
- Bottom of stack unchanged
- Order is completely reversed

```forth
T{ 3 4 SWAP -> 4 3 }T
T{ 0 -1 SWAP -> -1 0 }T
T{ 1 2 3 SWAP -> 1 3 2 }T
```
The last example shows that items below the top two are unaffected:
- Initial: 1 2 3 (3 on top)
- After SWAP: 1 3 2 (2 on top, 3 is now second)

### OVER Operation

```forth
T{ 1 2 OVER -> 1 2 1 }T
```
**Operation**: ( 1 2 -- 1 2 1 )
- Copies the **second** item (1, not 2)
- Pushes it on top
- Original position unchanged

```forth
T{ 3 4 OVER -> 3 4 3 }T
T{ 0 1 OVER -> 0 1 0 }T
T{ 1 2 3 OVER -> 1 2 3 2 }T
```
OVER is commonly used to duplicate an item that's not on top:
- ( a b -- a b a )
- Most operations consume their operands, so OVER preserves the second item for reuse

### ROT Operation

```forth
T{ 1 2 3 ROT -> 2 3 1 }T
```
**Operation**: ( 1 2 3 -- 2 3 1 )
- Takes the third item (1)
- Moves it to the top
- Other two items shift down

```forth
T{ 4 5 6 ROT -> 5 6 4 }T
T{ 0 1 2 ROT -> 1 2 0 }T
```

**Visualization**:
```
Before:  [1] [2] [3]     (3 on top)
         ↓   ↓   ↑ ROT
After:   [2] [3] [1]     (1 on top)
```

### Combined Operations

```forth
T{ 5 DUP DUP -> 5 5 5 }T
```
Multiple DUPs in sequence create more copies. Each DUP duplicates the current top.

```forth
T{ 1 2 SWAP DUP -> 2 1 1 }T
```
Chain of operations:
1. Stack: 1 2
2. SWAP: 2 1
3. DUP: 2 1 1

```forth
T{ 3 4 OVER OVER -> 3 4 3 4 }T
```
Two OVERs:
1. Stack: 3 4
2. First OVER: 3 4 3
3. Second OVER: 3 4 3 4

```forth
T{ 1 2 3 ROT ROT -> 1 2 3 }T
```
Two ROTs restore the original order (ROT is its own inverse repeated 3 times).

### Stack Preservation

```forth
T{ 10 20 SWAP DROP -> 20 }T
```
After SWAP and DROP:
1. Initial: 10 20
2. SWAP: 20 10
3. DROP: 20 (remains)

```forth
T{ 5 6 7 ROT DROP -> 6 7 }T
```
After ROT and DROP:
1. Initial: 5 6 7
2. ROT: 6 7 5
3. DROP: 6 7

## Expected Outcome

### Successful Run

```
=== Test 2.1: Basic Stack Manipulation ===
Total: 24
Passed: 24
Failed: 0
All tests passed!
```

Each test passes when:
1. The correct items are on the stack after the operation
2. Items are in the correct order
3. Items below the working set are unaffected
4. The operation produces the exact stack depth expected

### Failure Scenarios

**DUP Failure:**
```
T{ 5 DUP -> 5 5 }T  (expects 2 items, got 1)
```
DUP is not working; it's either not duplicating or consuming the item.

**SWAP Failure:**
```
T{ 1 2 SWAP -> 2 1 }T  (expects stack 2 1, got 1 2)
```
SWAP is not exchanging items properly, or implementation is incorrect.

**DROP Failure:**
```
T{ 1 2 DROP -> 1 }T  (expects 1 item, got 2)
```
DROP is not removing the top item.

**OVER Failure:**
```
T{ 1 2 OVER -> 1 2 1 }T  (expects 1 2 1, got 1 1 2)
```
OVER is copying the wrong item (top instead of second).

## Dependency Chain

This test **MUST** pass before any other tests because:
- All tests use DUP, DROP, SWAP, OVER in test verification
- The test harness itself relies on these operations
- Stack manipulation is the foundation of Forth

## Notes

- **Stack notation**: In this documentation, rightmost value is "top of stack"
- **Non-destructive**: DUP and OVER are non-destructive (don't consume operands)
- **Destructive**: DROP and SWAP are destructive (consume operands to produce result)
- **Order matters**: These operations have specific, well-defined semantics in Forth standard

## Visual Learning

**DUP Diagram**:
```
Before: [A]
After:  [A][A]
```

**SWAP Diagram**:
```
Before: [A][B]
After:  [B][A]
```

**OVER Diagram**:
```
Before: [A][B]
After:  [A][B][A]
```

**ROT Diagram**:
```
Before: [A][B][C]
After:  [B][C][A]
```

---

**Test File**: test-stack-basics.fs  
**Category**: Stack Manipulation  
**Test Count**: 24  
**Priority**: CRITICAL (all other tests depend on this)
