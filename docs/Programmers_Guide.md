# GD-FORTH Programmer's Guide

## Table of Contents

1. [Overview & Architecture](#overview--architecture)
2. [Quick Start for Developers](#quick-start-for-developers)
3. [The FORTH Execution Model](#the-forth-execution-model)
4. [System Architecture](#system-architecture)
5. [Core Components Explained](#core-components-explained)
6. [How to Implement New Keywords](#how-to-implement-new-keywords)
7. [Best Practices & Patterns](#best-practices--patterns)
8. [Testing Your Implementation](#testing-your-implementation)
9. [Common Pitfalls & Solutions](#common-pitfalls--solutions)
10. [Advanced Topics](#advanced-topics)

---

## Overview & Architecture

### What is GD-FORTH?

GD-FORTH is a FORTH language interpreter written in Java. FORTH is a stack-based, dynamically typed language where programs manipulate data through a data stack. Unlike procedural languages (Java, C), FORTH operates on a simple principle:

**Everything is a word** → Words manipulate a **data stack** → Programs compose words to build bigger words

### Key Characteristics

- **Stack-Based**: All data operations use a stack (LIFO - Last In, First Out)
- **Dynamically Typed**: Values can be integers, reals, booleans, or strings
- **Extensible**: Users (and developers) add new words easily
- **Interpreted**: Code executes immediately (no separate compilation step)
- **Memory Model**: Variables stored in shared global memory with index-based access

### Your Role as a Developer

As a developer extending GD-FORTH, you'll primarily:
- Add new **Statement** classes (representing FORTH keywords)
- Update the **ForthParser** to recognize new tokens
- Add **Token Types** for new keywords
- Write comprehensive unit tests
- Document your implementation

---

## Quick Start for Developers

### 5-Minute Overview

GD-FORTH execution works like this:

```
User Input: "5 3 + ."
    ↓
Tokenizer (ForthLexer): Breaks into tokens [5] [3] [+] [.]
    ↓
Parser (ForthParser): Creates Statement objects [NumberStatement(5), 
                                                  NumberStatement(3),
                                                  PlusStatement(),
                                                  PrintStatement()]
    ↓
Execution: Each Statement.execute() runs in order
  - NumberStatement(5).execute(): pushes 5 onto stack
  - NumberStatement(3).execute(): pushes 3 onto stack
  - PlusStatement.execute(): pops 3 and 5, pushes 8
  - PrintStatement.execute(): pops 8, prints "8"
    ↓
Output: "8" printed to console
```

### Project Structure

```
src/main/java/eu/gricom/forth/
├── Forth.java                          ← Main entry point
├── tokenizer/                          ← Lexical analysis
│   ├── ForthLexer.java                ← Tokenizer
│   ├── ForthTokenType.java            ← Token type enums
│   ├── ForthReservedWords.java        ← Keyword definitions
│   └── Token.java                      ← Token representation
├── parser/                             ← Syntax analysis & statement creation
│   ├── ForthParser.java               ← Main parser (recursive descent)
│   └── Parser.java                    ← Parser interface
├── statements/                         ← Statement implementations
│   ├── Statement.java                 ← Base interface
│   ├── arithmetics/                   ← Arithmetic operations
│   ├── comparison/                    ← Comparison operations
│   ├── stack/                         ← Stack operations
│   ├── controlFlow/                   ← Control flow (IF, DO, LOOP)
│   ├── inOut/                         ← I/O operations
│   └── ... other categories
├── memoryManager/                      ← Memory & stack management
│   ├── Stack.java                     ← Data stack
│   ├── ReturnStack.java               ← Return/loop stack
│   ├── Memory.java                    ← RAM simulation
│   ├── Variables.java                 ← Variable storage
│   └── ... other managers
├── variableTypes/                      ← Value types
│   ├── Value.java                     ← Base interface
│   ├── IntegerValue.java              ← Integer type
│   ├── RealValue.java                 ← Floating-point type
│   ├── StringValue.java               ← String type
│   └── BooleanValue.java              ← Boolean type
└── ... other packages
```

### Directory for Tests

```
src/test/java/eu/gricom/forth/
├── statements/
│   ├── arithmetics/
│   ├── comparison/
│   ├── stack/
│   ├── controlFlow/
│   ├── inOut/
│   └── ... mirrors main structure
├── parser/
├── tokenizer/
└── ... other test packages
```

---

## The FORTH Execution Model

### The Data Stack

The **data stack** is the core of FORTH execution. Think of it like a stack of plates:

```
Push 5:    [5]
Push 3:    [5, 3]        ← top (TOS - Top of Stack)
+:         [8]           ← 5 + 3 = 8
```

Every word either:
1. **Pushes** values onto the stack
2. **Pops** values from the stack and processes them
3. **Examines** values without popping (peek)

### Example: The "+" Word

```forth
5 3 +
```

Execution steps:
1. `5` → NumberStatement pushes 5: stack = [5]
2. `3` → NumberStatement pushes 3: stack = [5, 3]
3. `+` → PlusStatement:
   - Pops 3 from stack
   - Pops 5 from stack
   - Calculates 5 + 3 = 8
   - Pushes 8 onto stack
   - Stack = [8]

### Stack Notation in FORTH

Developers use **stack notation** to document words:

```forth
( input -- output )
```

Examples:
```forth
( n -- |n| )           ← ABS takes 1 number, returns absolute value
( n1 n2 -- n1+n2 )     ← + takes 2 numbers, returns sum
( n1 n2 -- n1 n2 n1 ) ← OVER duplicates second item to top
( -- ) (depth)         ← DEPTH examines stack, returns depth
```

This notation helps you understand what a word expects and produces.

---

## System Architecture

### The Big Picture: Data Flow

```
┌─────────────────────────────────────────────────────────┐
│                    User Input                           │
│           "10 20 + DUP . SWAP ."                        │
└────────────────────┬────────────────────────────────────┘
                     ↓
         ┌───────────────────────────┐
         │  TOKENIZER (ForthLexer)   │
         │  Breaks into tokens       │
         └───────────────┬───────────┘
                         ↓
         ┌───────────────────────────┐
         │  PARSER (ForthParser)     │
         │  Creates Statements       │
         └───────────────┬───────────┘
                         ↓
    ┌────────────────────────────────────────┐
    │  STATEMENT OBJECTS:                    │
    │  [Num(10), Num(20), Plus, Dup,        │
    │   Print, Swap, Print]                 │
    └────────────────┬───────────────────────┘
                     ↓
    ┌────────────────────────────────────────┐
    │  EXECUTION ENGINE                      │
    │  For each statement: statement.execute()│
    │  Manages stacks, variables, memory     │
    └────────────────┬───────────────────────┘
                     ↓
         ┌───────────────────────────┐
         │  OUTPUT                   │
         │  30 30                    │
         └───────────────────────────┘
```

### Key Components

| Component | Purpose | Location |
|-----------|---------|----------|
| **ForthLexer** | Converts text to tokens | `tokenizer/ForthLexer.java` |
| **ForthParser** | Creates Statement objects from tokens | `parser/ForthParser.java` |
| **Statement** | Represents a FORTH word (interface) | `statements/Statement.java` |
| **Stack** | Holds data values (LIFO) | `memoryManager/Stack.java` |
| **ReturnStack** | Return addresses & loop context | `runtimeManager/ReturnStack.java` |
| **Variables** | Named variable storage | `memoryManager/Variables.java` |
| **Value** | Wraps Java types (int, float, etc.) | `variableTypes/Value.java` |

---

## Core Components Explained

### 1. The Statement Interface

The **Statement** interface is the foundation. Every FORTH keyword is implemented as a Statement:

```java
public interface Statement {
    // Execute this word's behavior
    void execute() throws Exception;
    
    // Return token position (for error reporting)
    int getTokenNumber();
    
    // Return the word content (for debugging)
    String content() throws Exception;
    
    // Return structure representation
    String structure() throws Exception;
}
```

**Every** keyword you implement must implement this interface.

### 2. The Stack (Data Stack)

```java
public class Stack {
    // Push a value
    public void push(Value value)
    
    // Pop and return top value
    public Value pop()
    
    // Peek at top without removing
    public Value peek()
    
    // Check if empty
    public boolean isEmpty()
    
    // Get current depth
    public int size()
    
    // Clear all values
    public void reset()
}
```

**Important**: Stack is a singleton - `Stack oStack = new Stack()` always returns the same instance.

### 3. The Parser (ForthParser)

The parser reads tokens and creates Statement objects:

```java
public class ForthParser {
    // Main parsing method
    public List<Statement> parse(List<Token> tokens)
    
    // Parse individual statements
    private Statement parseOneStatement(Token token, ...)
    
    // Handles arithmetic, comparison, stack operations
    // Has specific methods for control flow
}
```

### 4. Value Types

Values are wrapper classes for Java primitives:

```java
public interface Value {
    // Convert to different types
    int toInteger()
    long toLong()
    double toReal()
    String toBoolean()
    String asString()
    
    // Stack representation
    void print()
}
```

**Why wrapper classes?** FORTH is dynamically typed. A value might be used as integer or real. Wrappers handle conversions.

### 5. Variables System

Variables are stored with unique indices:

```java
public class Variables {
    // Define a new variable
    static int define(String name)
    
    // Look up variable index by name
    static int index(String name)
    
    // Get variable value
    static Value get(int index)
    
    // Set variable value
    static void put(int index, Value value)
}
```

**Pattern**: 
1. `VARIABLE X` calls `Variables.define("X")` → returns index 5
2. Later, `X` pushes index 5 onto stack
3. `@` pops index, calls `Variables.get(5)`, pushes value

---

## How to Implement New Keywords

### Step-by-Step: Adding the "DUP2" Keyword

DUP2 duplicates the top two stack items. It's like DUP but for 2 items.

```forth
5 3 DUP2 .S     ← Output: 5 3 5 3 (stack shows 4 items)
```

Stack notation: `( n1 n2 -- n1 n2 n1 n2 )`

#### Step 1: Add the Token Type

File: `src/main/java/eu/gricom/forth/tokenizer/ForthTokenType.java`

```java
public enum ForthTokenType {
    // ... existing types ...
    DUP2,              // ← Add this line
    // ... rest of enum ...
}
```

#### Step 2: Register the Keyword

File: `src/main/java/eu/gricom/forth/tokenizer/ForthReservedWords.java`

```java
public class ForthReservedWords {
    private static final Map<String, ForthTokenType> RESERVED_WORDS = new HashMap<>();
    
    static {
        // ... existing keywords ...
        RESERVED_WORDS.put("DUP2", ForthTokenType.DUP2);    // ← Add this
        // ... rest of keywords ...
    }
}
```

#### Step 3: Create the Statement Class

File: `src/main/java/eu/gricom/forth/statements/stack/TwoDupStatement.java`

```java
package eu.gricom.forth.statements.stack;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.Token;

/**
 * TwoDupStatement implements the DUP2 command.
 * 
 * Stack behavior: ( n1 n2 -- n1 n2 n1 n2 )
 * Duplicates the top two stack items.
 * 
 * Example:
 *   5 3 DUP2 .S     → 5 3 5 3 (stack contains all four values)
 * 
 * @author Your Name
 * @version 1.0
 */
public class TwoDupStatement implements Statement {
    private final Token _oToken;
    private final int _iTokenNumber;
    
    /**
     * Constructor for DUP2 statement.
     * 
     * @param oToken the DUP2 token from lexer
     * @param iTokenNumber position in source for error reporting
     */
    public TwoDupStatement(final Token oToken, final int iTokenNumber) {
        _oToken = oToken;
        _iTokenNumber = iTokenNumber;
    }
    
    /**
     * Execute DUP2: duplicate top 2 stack items.
     * 
     * @throws Exception if stack doesn't have at least 2 items
     */
    @Override
    public void execute() throws Exception {
        Stack oStack = new Stack();
        
        // Check if stack has at least 2 items
        if (oStack.size() < 2) {
            throw new Exception(
                "DUP2: Stack underflow (need 2 items, have " + 
                oStack.size() + ") at token [" + _iTokenNumber + "]"
            );
        }
        
        // Get the top 2 items (without removing them)
        var oTop = oStack.peek();           // Get n2 (top)
        oStack.pop();                       // Remove temporarily
        var oSecond = oStack.peek();        // Get n1 (second)
        oStack.push(oTop);                  // Restore n2
        
        // Now push copies of both
        oStack.push(oSecond);               // Push copy of n1
        oStack.push(oTop);                  // Push copy of n2
    }
    
    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }
    
    @Override
    public String content() throws Exception {
        return "";
    }
    
    @Override
    public String structure() throws Exception {
        return "";
    }
}
```

#### Step 4: Update the Parser

File: `src/main/java/eu/gricom/forth/parser/ForthParser.java`

Find the parsing method that handles stack operations (look for existing DUP handling):

```java
// In parseOneStatement() method, find the section with:
case DUP:
    return new DupeStatement(oToken, iTokenNumber);

// Add DUP2 right after it:
case DUP2:                                    // ← Add this case
    return new TwoDupStatement(oToken, iTokenNumber);
case DROP:
    return new DropStatement(oToken, iTokenNumber);
// ... rest of cases
```

#### Step 5: Create Unit Tests

File: `src/test/java/eu/gricom/forth/statements/stack/TwoDupStatementTest.java`

```java
package eu.gricom.forth.statements.stack;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DUP2 (duplicate top 2 items) statement.
 */
@DisplayName("DUP2 Statement Tests")
class TwoDupStatementTest {
    
    private Stack stack;
    
    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }
    
    @Test
    @DisplayName("Should duplicate top 2 stack items")
    void testBasicDup2() throws Exception {
        // Setup: push 5, 3
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(3));
        
        // Execute DUP2
        Token oToken = new Token("DUP2", ForthTokenType.DUP2, 1);
        Statement stmt = new TwoDupStatement(oToken, 0);
        stmt.execute();
        
        // Verify stack has 4 items: 5, 3, 5, 3
        assertEquals(4, stack.size());
        
        // Pop and verify in reverse order (stack is LIFO)
        assertEquals(3, stack.pop().toInteger());
        assertEquals(5, stack.pop().toInteger());
        assertEquals(3, stack.pop().toInteger());
        assertEquals(5, stack.pop().toInteger());
        assertTrue(stack.isEmpty());
    }
    
    @Test
    @DisplayName("Should throw error with empty stack")
    void testEmptyStackError() {
        Token oToken = new Token("DUP2", ForthTokenType.DUP2, 1);
        Statement stmt = new TwoDupStatement(oToken, 0);
        
        assertThrows(Exception.class, stmt::execute);
    }
    
    @Test
    @DisplayName("Should throw error with single item")
    void testSingleItemError() throws Exception {
        stack.push(new IntegerValue(5));
        
        Token oToken = new Token("DUP2", ForthTokenType.DUP2, 1);
        Statement stmt = new TwoDupStatement(oToken, 0);
        
        assertThrows(Exception.class, stmt::execute);
    }
    
    @Test
    @DisplayName("Should work with multiple stack items")
    void testWithMultipleItems() throws Exception {
        // Setup: push 1, 2, 3, 4
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));
        
        // Execute DUP2 - should duplicate top 2 (3, 4)
        Token oToken = new Token("DUP2", ForthTokenType.DUP2, 1);
        Statement stmt = new TwoDupStatement(oToken, 0);
        stmt.execute();
        
        // Stack should be: 1, 2, 3, 4, 3, 4
        assertEquals(6, stack.size());
    }
}
```

#### Step 6: Run Tests

```bash
cd /Users/Andreas/Projects/Sources/Java/GD-FORTH
mvn clean test -Dtest=TwoDupStatementTest
```

Expected output:
```
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

#### Step 7: Build and Verify

```bash
mvn clean package
```

After build, test in the FORTH interpreter:

```forth
5 3 DUP2 .S
```

Expected output:
```
5 3 5 3 ⇐ shows all 4 stack items
```

---

## Best Practices & Patterns

### Pattern 1: Simple Operations (Most Common)

```java
public class MinusStatement implements Statement {
    private final Token _oToken;
    private final int _iTokenNumber;
    
    public MinusStatement(final Token oToken, final int iTokenNumber) {
        _oToken = oToken;
        _iTokenNumber = iTokenNumber;
    }
    
    @Override
    public void execute() throws Exception {
        Stack oStack = new Stack();
        
        // Standard pattern:
        // 1. Check prerequisites
        if (oStack.size() < 2) {
            throw new Exception("Stack underflow at token [" + _iTokenNumber + "]");
        }
        
        // 2. Pop operands (in reverse order!)
        long lSecond = oStack.pop().toLong();   // popped last = n2
        long lFirst = oStack.pop().toLong();    // popped first = n1
        
        // 3. Calculate result
        long lResult = lFirst - lSecond;        // n1 - n2
        
        // 4. Push result
        oStack.push(new LongValue(lResult));
    }
    
    @Override
    public int getTokenNumber() { return _iTokenNumber; }
    
    @Override
    public String content() throws Exception { return ""; }
    
    @Override
    public String structure() throws Exception { return ""; }
}
```

**Key Points:**
- Always check prerequisites (stack size)
- Remember stack is LIFO (pop twice reverses order)
- Wrap results appropriately (IntegerValue, RealValue, etc.)
- Throw meaningful exceptions with token number

### Pattern 2: Stack Manipulation

```java
public class OverStatement implements Statement {
    // Stack: ( n1 n2 -- n1 n2 n1 )
    
    @Override
    public void execute() throws Exception {
        Stack oStack = new Stack();
        
        if (oStack.size() < 2) {
            throw new Exception("OVER: Stack underflow");
        }
        
        // Peek both items without popping
        var oSecond = oStack.peek();    // n2
        oStack.pop();
        var oFirst = oStack.peek();     // n1
        oStack.push(oSecond);           // restore n2
        
        // Push copy of n1
        oStack.push(oFirst);
    }
}
```

### Pattern 3: Configuration Access

```java
public class GetMaxBcdDigitsStatement implements Statement {
    
    @Override
    public void execute() throws Exception {
        Stack oStack = new Stack();
        
        // Use EnvParam to get configuration
        int iMaxDigits = EnvParam.getMaxBcdDigits();
        oStack.push(new IntegerValue(iMaxDigits));
    }
}
```

### Pattern 4: Loop-Related Statements

Loop statements need access to **ReturnStack** (not data Stack):

```java
public class CurrentLoopIndexStatement implements Statement {
    // Pushes the current loop index (I command)
    
    @Override
    public void execute() throws Exception {
        Stack oStack = new Stack();
        
        // Check if in a loop
        if (!ReturnStack.isLoopActive()) {
            throw new InvalidLoopIndexException("I used outside loop");
        }
        
        // Get current loop context
        LoopContext oLoop = ReturnStack.peekLoop();
        long lIndex = oLoop.getCurrentIndex();
        
        // Push to data stack
        oStack.push(new LongValue(lIndex));
    }
}
```

### Pattern 5: Control Flow Statements

Control flow statements often need the parser to handle sub-blocks:

```java
public class IfStatement implements Statement {
    private final Token _oToken;
    private final List<Statement> _aoThenBranch;
    private final List<Statement> _aoElseBranch;
    
    public IfStatement(Token oToken, List<Statement> aoThen, 
                       List<Statement> aoElse) {
        _oToken = oToken;
        _aoThenBranch = aoThen;
        _aoElseBranch = aoElse;
    }
    
    @Override
    public void execute() throws Exception {
        Stack oStack = new Stack();
        
        if (oStack.isEmpty()) {
            throw new Exception("IF: Stack underflow");
        }
        
        // Pop condition flag
        boolean bCondition = oStack.pop().toBoolean();
        
        // Execute appropriate branch
        if (bCondition) {
            for (Statement stmt : _aoThenBranch) {
                stmt.execute();
            }
        } else if (!_aoElseBranch.isEmpty()) {
            for (Statement stmt : _aoElseBranch) {
                stmt.execute();
            }
        }
    }
}
```

### Naming Conventions

Follow existing patterns for consistency:

| Word | Class Name | File Location |
|------|-----------|--------|
| `+` | PlusStatement | `statements/arithmetics/` |
| `ABS` | AbsStatement | `statements/mathematics/` |
| `DUP` | DupeStatement | `statements/stack/` |
| `>` | GreaterThanStatement | `statements/comparison/` |
| `.` | PrintStatement | `statements/inOut/` |
| `IF` | IfStatement | `statements/controlFlow/` |
| `DO` | DoStatement | `statements/controlFlow/` |

**Rules:**
- CamelCase class names
- End with "Statement"
- Place in appropriate package
- File name = Class name + ".java"

---

## Testing Your Implementation

### Test Structure

```java
@DisplayName("MyNewWord Tests")                    // Class display name
class MyNewWordStatementTest {
    
    private Stack stack;
    
    @BeforeEach                                    // Runs before each test
    void setUp() {
        stack = new Stack();
        stack.reset();
    }
    
    @Test
    @DisplayName("Should do the happy path")       // Test display name
    void testHappyPath() throws Exception {
        // Arrange: set up initial state
        stack.push(new IntegerValue(5));
        
        // Act: execute the statement
        Statement stmt = new MyNewWordStatement(...);
        stmt.execute();
        
        // Assert: verify the results
        assertEquals(10, stack.pop().toInteger());
    }
}
```

### Running Tests

```bash
# Run specific test class
mvn test -Dtest=MyNewWordStatementTest

# Run all tests
mvn clean test

# Run with output
mvn test -Dtest=MyNewWordStatementTest -X
```

### Test Coverage Guidelines

For each statement, write tests for:

1. **Happy Path** - Normal operation
2. **Boundary Cases** - Min/max values, empty/full states
3. **Error Cases** - Stack underflow, invalid input
4. **Edge Cases** - Negative numbers, zero, very large numbers

**Example Test Suite (4-6 tests):**

```java
@Test void testHappyPath() { ... }
@Test void testEmptyStack() { ... }
@Test void testSingleItem() { ... }
@Test void testNegativeNumbers() { ... }
@Test void testZero() { ... }
@Test void testLargeNumbers() { ... }
```

---

## Common Pitfalls & Solutions

### Pitfall 1: Forgetting Stack is Singleton

**Wrong:**
```java
Stack oStack = new Stack();
```

This always returns the same stack instance (singleton pattern). All your statements use the same stack.

**Correct:**
```java
Stack oStack = new Stack();  // Still correct - it's a singleton
// The key is understanding ALL statements share this stack
```

### Pitfall 2: Pop Order Confusion

**Wrong:**
```java
// For "a b -" (a minus b), written as "3 2 -"
long lA = oStack.pop();      // Gets 2 (wrong!)
long lB = oStack.pop();      // Gets 3 (wrong!)
long result = lA - lB;       // Calculates 2 - 3 = -1 (wrong!)
```

**Correct:**
```java
// Stack is LIFO: last pushed is first popped
long lB = oStack.pop();      // Gets 2 (second operand)
long lA = oStack.pop();      // Gets 3 (first operand)
long result = lA - lB;       // Calculates 3 - 2 = 1 (correct!)
```

**Remember:** Pop in reverse order!

### Pitfall 3: Type Conversion Errors

**Wrong:**
```java
@Override
public void execute() throws Exception {
    Stack oStack = new Stack();
    long lValue = oStack.pop().toLong();  // May fail!
    oStack.push(new IntegerValue(lValue));
}
```

The popped value might be a RealValue, which doesn't have toLong().

**Correct:**
```java
@Override
public void execute() throws Exception {
    Stack oStack = new Stack();
    Value oValue = oStack.pop();
    
    // Convert to integer (FORTH is dynamically typed)
    int iValue = oValue.toInteger();
    oStack.push(new IntegerValue(iValue));
}
```

### Pitfall 4: Not Checking Stack Underflow

**Wrong:**
```java
@Override
public void execute() throws Exception {
    Stack oStack = new Stack();
    oStack.pop();  // Throws if empty - not user friendly
    oStack.pop();
}
```

**Correct:**
```java
@Override
public void execute() throws Exception {
    Stack oStack = new Stack();
    
    if (oStack.size() < 2) {
        throw new Exception(
            "MYWORD: Stack underflow (need 2 items, have " + 
            oStack.size() + ") at token [" + _iTokenNumber + "]"
        );
    }
    
    oStack.pop();
    oStack.pop();
}
```

### Pitfall 5: Forgetting Documentation

**Wrong:**
```java
public class MyWordStatement implements Statement {
    public void execute() throws Exception {
        // ...
    }
}
```

**Correct:**
```java
/**
 * MyWordStatement implements the MYWORD command.
 * 
 * Stack behavior: ( n1 n2 -- n1+n2 )
 * Adds two numbers from the stack.
 * 
 * Example:
 *   5 3 MYWORD .     → 8 (prints 5 + 3)
 * 
 * @author Your Name
 * @version 1.0
 */
public class MyWordStatement implements Statement {
    /**
     * Execute MYWORD operation.
     * 
     * @throws Exception if stack doesn't have enough items
     */
    @Override
    public void execute() throws Exception {
        // ...
    }
}
```

### Pitfall 6: Forget to Update Parser

If you add a token type and statement class, you MUST update ForthParser!

**Common Error:**
```
Exception: Cannot parse token: MYWORD
```

**Solution:** Add case to ForthParser.parseOneStatement():
```java
case MYWORD:
    return new MyWordStatement(oToken, iTokenNumber);
```

### Pitfall 7: Loop Variables Without Proper Context

**Wrong:**
```java
public class MyLoopStatement implements Statement {
    @Override
    public void execute() throws Exception {
        Stack oStack = new Stack();
        var oLoop = ReturnStack.peekLoop();  // Null if not in loop!
    }
}
```

**Correct:**
```java
@Override
public void execute() throws Exception {
    if (!ReturnStack.isLoopActive()) {
        throw new InvalidLoopIndexException("Not in a loop!");
    }
    
    var oLoop = ReturnStack.peekLoop();
    // Now safe to use oLoop
}
```

---

## Advanced Topics

### Adding Compile-Time Behavior

Some FORTH words have different behavior during compilation vs. execution. For example, `."` (print string) typically executes immediately even during compilation.

```java
public class PrintStringStatement implements Statement {
    private final String _strValue;
    
    public PrintStringStatement(String strValue) {
        _strValue = strValue;
    }
    
    @Override
    public void execute() throws Exception {
        System.out.print(_strValue);
    }
}
```

### Creating Control Flow Blocks

Control flow statements often contain sub-blocks:

```java
public class DoStatement implements Statement {
    private final List<Statement> _aoBody;
    
    public DoStatement(Token oToken, List<Statement> aoBody) {
        _oToken = oToken;
        _aoBody = aoBody;
    }
    
    @Override
    public void execute() throws Exception {
        // Loop through body statements
        for (Statement stmt : _aoBody) {
            stmt.execute();
        }
    }
}
```

The parser handles parsing the sub-block (everything until LOOP), and passes it to DoStatement.

### Working with Variables

Variables are stored globally with indices:

```java
public class FetchStatement implements Statement {
    @Override
    public void execute() throws Exception {
        Stack oStack = new Stack();
        
        // Pop variable index
        int iIndex = oStack.pop().toInteger();
        
        // Get variable value
        Value oValue = Variables.get(iIndex);
        
        // Push onto stack
        oStack.push(oValue);
    }
}
```

### Exception Handling

Use specific exception classes for different error types:

```java
// Use generic Exception for stack errors
throw new Exception("MYWORD: Stack underflow at token [" + 
                    _iTokenNumber + "]");

// Use specific exception classes for domain errors
throw new InvalidLoopIndexException("I used outside loop");
throw new MissingDoException("LOOP without DO");
```

### Performance Optimization

Most FORTH implementations are interpretive and don't need heavy optimization. However:

1. **Avoid unnecessary object creation** - Reuse Value objects when possible
2. **Cache frequently accessed values** - Variable lookups, configuration
3. **Profile before optimizing** - Measure where time is actually spent

### Memory Management

GD-FORTH simulates memory with an array. You can store arbitrary data:

```java
public class StoreStatement implements Statement {
    @Override
    public void execute() throws Exception {
        Stack oStack = new Stack();
        
        // Pop value and address
        Value oValue = oStack.pop();
        int iAddress = oStack.pop().toInteger();
        
        // Store in memory
        Memory.write(iAddress, oValue);
    }
}
```

---

## Debugging Tips

### 1. Stack Inspection

Print stack contents during debugging:

```java
// In your Statement
Stack oStack = new Stack();
System.out.println("Stack size: " + oStack.size());
System.out.println("Stack top: " + oStack.peek());
```

### 2. Test in Isolation

Create small test programs to isolate issues:

```forth
5 DUP .S      ← Should show: 5 5
DROP .S       ← Should show: 5
```

### 3. Add Debug Output

```java
@Override
public void execute() throws Exception {
    Stack oStack = new Stack();
    System.out.println("MYWORD: Before - stack size: " + oStack.size());
    // ... operation ...
    System.out.println("MYWORD: After - stack size: " + oStack.size());
}
```

### 4. Check Token Registration

Verify your token is registered:

```java
// In ForthReservedWords - does your keyword appear?
RESERVED_WORDS.put("MYWORD", ForthTokenType.MYWORD);
```

### 5. Verify Parser Integration

Check ForthParser.parseOneStatement():

```java
case MYWORD:
    return new MyWordStatement(oToken, iTokenNumber);
```

---

## Checklist for Adding a New Word

Use this checklist when implementing a new FORTH keyword:

- [ ] Understand stack behavior (write stack notation)
- [ ] Add ForthTokenType enum value
- [ ] Register in ForthReservedWords.RESERVED_WORDS
- [ ] Create Statement class
  - [ ] Implement all interface methods
  - [ ] Add comprehensive JavaDoc
  - [ ] Check stack prerequisites
  - [ ] Handle errors gracefully
- [ ] Update ForthParser.parseOneStatement()
- [ ] Create unit test class
  - [ ] Test happy path
  - [ ] Test error cases
  - [ ] Test boundary conditions
- [ ] Run unit tests: `mvn test -Dtest=MyWordStatementTest`
- [ ] Run full build: `mvn clean package`
- [ ] Test in FORTH interpreter
- [ ] Update TODO.md if needed
- [ ] Add integration test if needed

---

## Useful Code Snippets

### Getting the Data Stack

```java
Stack oStack = new Stack();  // Singleton - always same instance
```

### Getting the Return/Loop Stack

```java
ReturnStack.pushLoop(oLoopContext);
LoopContext oLoop = ReturnStack.peekLoop();
ReturnStack.popLoop();
boolean bInLoop = ReturnStack.isLoopActive();
```

### Working with Variables

```java
// Define a new variable
int iIndex = Variables.define("X");

// Access variable by name (get its index)
int iIndex = Variables.index("X");

// Get variable value
Value oValue = Variables.get(iIndex);

// Set variable value
Variables.put(iIndex, new IntegerValue(42));
```

### Creating Values

```java
Value oValue = new IntegerValue(42);
Value oValue = new LongValue(9999999999L);
Value oValue = new RealValue("3.14159");
Value oValue = new StringValue("hello");
Value oValue = new BooleanValue(true);
```

### Type Conversions

```java
Value oValue = oStack.pop();
int iInt = oValue.toInteger();
long lLong = oValue.toLong();
double dDouble = oValue.toReal();
String str = oValue.asString();
```

---

## Where to Find Examples

The codebase has 104 Java files with many working examples:

### Simple Operations (Good Models)

- **PlusStatement** - `statements/arithmetics/`
- **DupeStatement** - `statements/stack/`
- **PrintStatement** - `statements/inOut/`
- **GreaterThanStatement** - `statements/comparison/`

### Complex Operations

- **IfStatement** - Control flow with branches
- **DoStatement** - Loop management
- **CurrentLoopIndexStatement** - Loop context access

### Test Examples

- **PlusStatementTest** - Basic arithmetic testing
- **DoStatementTest** - Control flow testing
- **IfStatementTest** - Complex statement testing

---

## Final Tips for Success

1. **Start Small** - Implement simple operations first (single-operand math)
2. **Copy & Adapt** - Use existing similar statements as templates
3. **Test First** - Write tests before the implementation
4. **Read Errors** - Error messages tell you what's missing
5. **Ask Questions** - Code comments are your friend
6. **Document Well** - Future developers (including you!) will appreciate it
7. **Keep it Simple** - Avoid complex Java features; junior devs need to maintain it
8. **Build Often** - Run `mvn clean package` frequently

---

## References

- **FORTH Standard**: ISO/IEC 14514 or Forth 2012
- **This Project**: See docs/ for more guides
- **Code Examples**: Study existing Statement implementations
- **Test Examples**: Study corresponding *Test.java files

---

## Contact & Support

For questions about extending GD-FORTH:
1. Check this guide first
2. Review similar existing implementations
3. Run the test suite to verify changes
4. Build the project to catch errors early

**Remember**: Every FORTH word is just a Statement. Learn how to implement one, and you can implement them all!

---

**Document Version**: 1.0  
**Created**: 2026-10-03  
**For**: GD-FORTH v0.0.3  
**Audience**: Junior Java Developers  
**Maintained By**: Andreas Grimm
