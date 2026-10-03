# DO Loop Implementation - Developer Guide

## Overview

This document provides a technical guide for maintaining and extending the DO...LOOP functionality in GD-FORTH.

The DO loop implementation is structured to be maintainable by junior developers. All code includes extensive comments and follows simple Java patterns without complex language features.

## Architecture

### Core Components

1. **LoopContext** (`runtimeManager/LoopContext.java`)
   - Stores loop state: current index, limit, step size
   - Methods: `increment()`, `isComplete()`, getters
   - Immutable after creation (step size can be changed for +LOOP)

2. **ReturnStack** (enhanced, `runtimeManager/ReturnStack.java`)
   - Manages loop stack (separate from return addresses)
   - All static methods for global access
   - Push/pop loop contexts for nesting support

3. **DoStatement** (`statements/controlFlow/DoStatement.java`)
   - Implements the core loop execution
   - Pops limit and index from data stack
   - Manages loop lifecycle (create, iterate, cleanup)

4. **Index Access Statements**
   - `CurrentLoopIndexStatement` (I): pushes current index
   - `OuterLoopIndexStatement` (J): pushes outer index
   - Simple statements that read from loop context

5. **Loop Control Statements**
   - `LoopStatement` (LOOP): increments by 1
   - `PlusLoopStatement` (+LOOP): increments by TOS value
   - `UnloopStatement` (UNLOOP): exits loop, cleans up context

## Execution Flow

### Simple Loop Example: `0 5 DO I LOOP`

```
1. Parser: Creates DoStatement with [I, LOOP] as loop body
2. Runtime: DoStatement.execute()
   a. Pop 0 (index) and 5 (limit) from stack
   b. Create LoopContext(limit=5, index=0, step=1)
   c. Push LoopContext to ReturnStack.loopStack
   d. Loop:
      - Is index (0) >= limit (5)? No, continue
      - Execute body: I statement pushes 0 to stack
      - Execute body: LOOP statement calls increment() → index=1
      - Is index (1) >= limit (5)? No, continue
      - ... repeat until index >= 5
   e. Pop LoopContext from loop stack
```

### Nested Loop Example

Nested loops work through the loop stack:
- Outer loop pushes LoopContext(outer)
- Inner loop pushes LoopContext(inner) on top
- I reads from LoopContext(inner) using peekLoop()
- J reads from LoopContext(outer) using peekLoop(1)
- When inner loop exits, LoopContext(inner) is popped
- When outer loop exits, LoopContext(outer) is popped

## Key Design Decisions

### 1. Structured DO Statement (Not Interpreted)
**Decision**: Parse complete DO...LOOP block at parse time
**Rationale**: 
- Early syntax error detection
- Matches existing IfStatement pattern
- Simplifies execution logic
**Alternative**: Could interpret LOOP/UNLOOP at runtime, but less reliable

### 2. Dedicated Loop Stack
**Decision**: Separate Stack<LoopContext> in ReturnStack
**Rationale**:
- Clean separation: loop control ≠ data stack
- Easy nesting support
- Prevents stack corruption
**Alternative**: Could use data stack, but pollutes design

### 3. Long Indices
**Decision**: Use `long` for all index/limit values
**Rationale**:
- FORTH standard allows arbitrary precision
- Future-proof for large arrays
- Matches Java's typical long usage
**Alternative**: Could use `int`, but unnecessarily limits

### 4. No Automatic LEAVE Implementation
**Decision**: UNLOOP is separate; LEAVE would be built in FORTH
**Rationale**:
- Keeps core simple
- LEAVE is conditional (requires IF)
- Example FORTH implementation:
  ```forth
  : LEAVE UNLOOP ; (simplified; real version handles THEN)
  ```

## Modification Guide

### Adding a Feature

#### Example: Add LOOP-COUNTER (leaves count on stack)

1. **Create Statement** (`statements/controlFlow/LoopCounterStatement.java`)
   ```java
   public class LoopCounterStatement implements Statement {
       @Override
       public void execute() throws Exception {
           if (!ReturnStack.isLoopActive()) {
               throw new InvalidLoopIndexException("LOOP-COUNTER outside loop");
           }
           LoopContext ctx = ReturnStack.peekLoop();
           // Calculate iterations completed = currentIndex - startIndex
           Stack stack = new Stack();
           stack.push(new IntegerValue(ctx.getCurrentIndex()));
       }
       // ... other methods
   }
   ```

2. **Add Parser Case** (`parser/ForthParser.java`)
   ```java
   case LOOP_COUNTER:
       _iPosition++;
       return new LoopCounterStatement(getToken(-1), _iPosition - 1);
   ```

3. **Add Token Type** (if not already defined)
   - Edit `tokenizer/ForthTokenType.java`
   - Add to `ForthReservedWords.java`

4. **Write Tests** (`test/java/.../LoopCounterStatementTest.java`)
   ```java
   @Test
   void testLoopCounter() throws Exception {
       LoopContext loop = new LoopContext(5, 3, 1);
       ReturnStack.pushLoop(loop);
       
       new LoopCounterStatement(...).execute();
       
       assertEquals(3, stack.pop().toInteger()); // currentIndex = 3
   }
   ```

5. **Test with FORTH** (`test/forth/test-loop-counter.fs`)
   ```forth
   0 5 DO LOOP-COUNTER . LOOP
   (* Should print: 0 1 2 3 4 *)
   ```

### Debugging Tips

1. **Check Loop Stack State**
   ```java
   int size = ReturnStack.getLoopStackSize();
   System.out.println("Active loops: " + size);
   ```

2. **Inspect LoopContext**
   ```java
   LoopContext ctx = ReturnStack.peekLoop();
   System.out.println("Index: " + ctx.getCurrentIndex());
   System.out.println("Limit: " + ctx.getLimit());
   System.out.println("Complete: " + ctx.isComplete());
   ```

3. **Verify Cleanup**
   ```java
   assertFalse(ReturnStack.isLoopActive(), "Loop context not cleaned up");
   ```

## Testing Strategy

### Unit Tests (Most Important)
- Test LoopContext math (increment, completion)
- Test each statement in isolation
- Test error conditions

### Integration Tests
- Test parser creates correct DoStatement
- Test full loop execution with I/J
- Test nested loops

### FORTH Tests
- Test actual FORTH programs
- Verify output matches expectations
- Catch runtime edge cases

## Common Errors and Solutions

### Error: "I used outside DO...LOOP"
**Cause**: CurrentLoopIndexStatement.execute() called when `!ReturnStack.isLoopActive()`
**Fix**: Ensure I/J/LOOP/+LOOP are only used inside a DO block
**Prevention**: Parser should validate syntax

### Error: Stack underflow on "DO"
**Cause**: Not enough values on data stack when DO executes
**Fix**: DoStatement tries to pop two values; provide both
**Example**: `0 5 DO I LOOP` — requires 0 and 5 on stack before DO

### Error: Loop never exits
**Cause**: isComplete() never returns true (infinite loop)
**Possible causes**:
1. Step size is 0 → fix: use valid step size
2. Negative step with wrong logic → fix: check isComplete() logic
3. Integer overflow (index keeps growing) → fix: use smaller numbers

### Error: "J outside nested loop"
**Cause**: OuterLoopIndexStatement tries to access depth 1 when only one loop active
**Fix**: J requires at least two nested loops
**Prevention**: Parser cannot catch this (runtime-dependent), throw error at execution

## Performance Notes

1. **Loop Stack vs Data Stack**
   - Loop stack operations are O(1)
   - No performance impact on data stack operations

2. **Nested Loop Overhead**
   - Each nesting level adds one LoopContext object
   - Memory: ~40 bytes per loop level (three longs)

3. **No Optimization**
   - Current implementation does not optimize
   - Simple "execute body until complete" loop
   - Could be optimized with bytecode compilation (future)

## Future Enhancements

1. **LEAVE Statement**
   - Add flag to DoStatement to signal early exit
   - UNLOOP + flag-setting statement

2. **+LOOP Optimization**
   - Pre-calculate iteration count
   - Skip execution if index already >= limit

3. **Debugging Support**
   - Add trace output for loop iterations
   - Show index values during execution

4. **Error Messages**
   - Improve error messages with line numbers
   - Show loop nesting level in errors

## Related Classes

- `Stack` (data stack)
- `ReturnStack` (manages loop and return stacks)
- `IfStatement` (similar structured control flow)
- `ForthParser` (parses DO blocks)
- All Statement subclasses (execute within loops)

## Testing Checklist

- [ ] LoopContextTest passes all tests
- [ ] DoStatementTest passes all tests
- [ ] CurrentLoopIndexStatementTest passes
- [ ] OuterLoopIndexStatementTest passes
- [ ] ForthParser correctly parses DO...LOOP
- [ ] FORTH test suite passes
- [ ] Nested loops work correctly
- [ ] +LOOP increments properly
- [ ] Error cases throw correct exceptions
- [ ] Loop context cleaned up after execution
- [ ] Memory not leaked (no dangling LoopContexts)
