# Implementation Guide - GD-FORTH Interpreter
## Architecture and Design Documentation

**Version**: 0.0.1  
**Last Updated**: 2026-07-30  
**Language**: Java 21+

---

## Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [Core Components](#core-components)
3. [Execution Flow](#execution-flow)
4. [Module Structure](#module-structure)
5. [Data Structures](#data-structures)
6. [Error Handling](#error-handling)
7. [Testing Strategy](#testing-strategy)
8. [Performance](#performance)

---

## Architecture Overview

### High-Level Design

The GD-FORTH interpreter follows a **three-stage pipeline** architecture:

```
Source Code
    ↓
[LEXICAL ANALYSIS - Tokenizer]
    ↓
Tokens
    ↓
[SYNTAX ANALYSIS - Parser]
    ↓
Statements (AST)
    ↓
[EXECUTION - Runtime Engine]
    ↓
Results on Stack
```

### Design Principles

1. **Separation of Concerns**: Each module has a single, well-defined responsibility
2. **Stack-Based Execution**: All operations are stack-based (FORTH semantics)
3. **Singleton Patterns**: Global state (stack, program) managed carefully
4. **Exception Handling**: Clear error propagation and handling
5. **Extensibility**: Easy to add new words/operations

---

## Core Components

### 1. Tokenizer (eu.gricom.forth.tokenizer)

**Responsibility**: Convert raw source code into tokens

#### Key Classes

##### `ForthLexer` implements `Lexer`
- **Method**: `List<Token> tokenize(String source)`
- **Purpose**: Break source into meaningful tokens
- **Tokens Recognized**:
  - Numbers (integers, decimals, negatives)
  - Operators (+, -, *, /, MOD)
  - Print commands (PRINT, .)
  - Control flow (IF, THEN, DO, LOOP)
  - Stack operations (DUP, DROP, SWAP)

##### `Token`
- **Fields**:
  - `String text` — Token text (e.g., "42", "+", "DUP")
  - `ForthTokenType type` — Token classification
  - `int lineNumber` — Source line number (for error reporting)
  - `int commandSequenceNumber` — Position in line

##### `ForthTokenType` enum
```java
NUMBER, PLUS, MINUS, MULTIPLY, DIVIDE, MOD,
PRINT, WORD, EMPTY_LINE, EOP, CARRIAGE_RETURN, ...
```

##### `Normalizer`
- Handles string normalization
- Trims whitespace
- Validates input

#### Example Flow

```
Input: "10 20 +"
       ↓
ForthLexer.tokenize()
       ↓
Tokens:
  - Token("10", NUMBER, 1)
  - Token("20", NUMBER, 1)
  - Token("+", PLUS, 1)
```

---

### 2. Parser (eu.gricom.forth.parser)

**Responsibility**: Convert tokens into executable statements

#### Key Classes

##### `ForthParser` implements `Parser`
- **Method**: `List<Statement> parse()`
- **Purpose**: Build Abstract Syntax Tree (AST) from tokens
- **Process**:
  1. Read tokens sequentially
  2. Classify token type
  3. Create appropriate Statement object
  4. Return list of executable statements

#### Statement Types

```
Statement (interface)
├─ NumberStatement
│  ├─ Pushes integer onto stack
│  └─ Example: "42" → push(42)
│
├─ ArithmeticStatement
│  ├─ Performs arithmetic operation
│  └─ Examples: +, -, *, /, MOD
│
├─ PrintStatement
│  ├─ Output to console
│  └─ Examples: PRINT, .
│
└─ EmptyStatement
   ├─ End of input marker
   └─ No operation
```

#### Parsing Rules

```java
parse() {
  while (not EOP) {
    switch (currentToken.type) {
      case NUMBER:
        return parseNumberStatement();
      case PLUS, MINUS, MULTIPLY, DIVIDE, MOD:
        return parseArithmeticStatement();
      case PRINT, PRINT_KEEP_STACK, CARRIAGE_RETURN:
        return parsePrintStatement();
      default:
        throw SyntaxErrorException();
    }
  }
}
```

---

### 3. Runtime Engine (eu.gricom.forth.runtimeManager)

**Responsibility**: Execute statements and manage program state

#### Key Classes

##### `Execute`
- **Responsibility**: Orchestrate statement execution
- **Methods**:
  - `runNewStatements()` — Execute statement list
  - `loadEnvironment()` — Initialize runtime state
  - `getFinalStatement()` — Query last executed statement

##### Execution Loop

```java
public void runNewStatements() {
  for (Statement statement : statements) {
    statement.execute();  // Each statement modifies stack
  }
}
```

#### Stack Management

The `Stack` class is a **singleton** for global access:

```java
public class Stack {
  private static java.util.Stack _oStack = null;  // Singleton
  
  public void push(Value value)
  public Value pop() throws EmptyStackException
  public Value peek() throws EmptyStackException
  public void reset()
}
```

---

### 4. Memory Management (eu.gricom.forth.memoryManager)

**Responsibility**: Manage data and program storage

#### Key Classes

##### `Stack`
- LIFO (Last In, First Out) data structure
- Stores `Value` objects
- Singleton pattern for global access

##### `Program`
- Stores all parsed statements
- Maintains program state
- Provides statement access

##### `Directory`
- Dictionary of user-defined words
- Maps word names to definitions
- Stores tokenized definitions

##### `LineNumberXRef`
- Cross-reference for line numbers
- Maps tokens to statement positions
- Used for error reporting

---

### 5. Variable Types (eu.gricom.forth.variableTypes)

**Responsibility**: Represent data values with type operations

#### Type Hierarchy

```
Value (interface)
├─ IntegerValue
│  ├─ Stores int values
│  └─ Arithmetic: +, -, *, /, MOD
│
├─ RealValue
│  ├─ Floating-point values
│  └─ More operations: trigonometric, etc.
│
├─ StringValue
│  ├─ Text strings
│  └─ String operations
│
└─ BooleanValue
   ├─ true/false values
   └─ Logical operations
```

#### Value Interface

```java
public interface Value extends Expression {
  String toString();
  double toReal();
  int toInteger();
  
  // Arithmetic operations
  Value plus(Value other);
  Value minus(Value other);
  Value multiply(Value other);
  Value divide(Value other);
  Value modulo(Value other);
  
  // Comparison operations
  Value equals(Value other);
  Value notEqual(Value other);
  Value smallerThan(Value other);
  Value largerThan(Value other);
}
```

---

## Execution Flow

### Complete Execution Sequence

```
1. USER INPUT
   "10 20 +"
   
2. TOKENIZER
   ForthLexer.tokenize()
   ↓
   [Token(10), Token(20), Token(+)]
   
3. PARSER
   ForthParser.parse()
   ↓
   [NumberStatement(10),
    NumberStatement(20),
    ArithmeticStatement(PLUS)]
   
4. EXECUTION ENGINE
   Execute.runNewStatements()
   ↓
   For each statement:
     - NumberStatement(10).execute()
       → Stack: [10]
     
     - NumberStatement(20).execute()
       → Stack: [10, 20]
     
     - ArithmeticStatement(PLUS).execute()
       → Pop 20, pop 10
       → Push 10 + 20
       → Stack: [30]
   
5. OUTPUT
   "30" (or printed via PrintStatement)
```

### Statement Execution

Each statement implements:

```java
public interface Statement {
  int getTokenNumber();
  void execute() throws Exception;
  String content() throws Exception;
  String structure() throws Exception;
}
```

#### NumberStatement Execution

```java
public void execute() throws Exception {
  Stack oStack = new Stack();
  oStack.push(_oNumber);  // Push integer onto stack
}
```

#### ArithmeticStatement Execution

```java
public void execute() throws Exception {
  Stack oStack = new Stack();
  
  // Pop two values (order: first popped is operand2)
  int operand1 = oStack.pop().toInteger();  // Top of stack
  int operand2 = oStack.pop().toInteger();  // Below top
  
  int result = switch(_oTokenType) {
    case PLUS -> operand2 + operand1;
    case MINUS -> operand2 - operand1;
    case MULTIPLY -> operand2 * operand1;
    case DIVIDE -> operand2 / operand1;
    case MOD -> operand2 % operand1;
  };
  
  oStack.push(new IntegerValue(result));
}
```

---

## Module Structure

### Package Organization

```
eu.gricom.forth
├── Forth.java (main entry point)
│
├── tokenizer/
│   ├── Lexer.java (interface)
│   ├── ForthLexer.java (implementation)
│   ├── Token.java
│   ├── ForthTokenType.java (enum)
│   ├── Normalizer.java
│   └── ForthReservedWords.java
│
├── parser/
│   ├── Parser.java (interface)
│   ├── ForthParser.java (implementation)
│   └── package-info.java
│
├── statements/
│   ├── Statement.java (interface)
│   ├── Expression.java (interface)
│   ├── NumberStatement.java
│   ├── ArithmeticStatement.java
│   ├── PrintStatement.java
│   ├── EmptyStatement.java
│   └── ...
│
├── variableTypes/
│   ├── Value.java (interface)
│   ├── IntegerValue.java
│   ├── RealValue.java
│   ├── StringValue.java
│   └── BooleanValue.java
│
├── memoryManager/
│   ├── Stack.java
│   ├── Program.java
│   ├── Directory.java
│   ├── LineNumberXRef.java
│   └── ...
│
├── runtimeManager/
│   └── Execute.java
│
├── helper/
│   ├── Logger.java
│   ├── Printer.java
│   ├── FileHandler.java
│   ├── EnvParam.java
│   └── ...
│
└── error/
    ├── ForthException.java (base)
    ├── EmptyStackException.java
    ├── DivideByZeroException.java
    ├── SyntaxErrorException.java
    └── ...
```

---

## Data Structures

### Stack (Singleton)

```java
public class Stack {
  private static java.util.Stack _oStack = null;
  
  // All instances share the same _oStack
  public Stack() {
    if (_oStack == null) {
      _oStack = new java.util.Stack<>();
    }
  }
}
```

**Why Singleton?**: FORTH requires a single global stack shared across all operations

### Program Storage

```java
public class Program {
  private List<Statement> statements;     // Parsed statements
  private List<Statement> allStatements;  // Historical tracking
}
```

### Directory (Word Definitions)

```java
public class Directory {
  private Map<String, String> _aoDefinedWords;      // name -> definition
  private Map<String, List<Token>> _aoWordByToken;  // name -> tokens
  
  public void storeWord(String name, String definition);
  public String getWord(String name);
  public List<Token> getToken(String name);
  public String[] listWords();
}
```

---

## Error Handling

### Exception Hierarchy

```
Exception
└── ForthException (custom base)
    ├── EmptyStackException
    │   └── Thrown when pop() called on empty stack
    │
    ├── DivideByZeroException
    │   └── Thrown for division/modulo by zero
    │
    ├── SyntaxErrorException
    │   └── Thrown for parsing errors
    │
    ├── FileNotFoundException
    │   └── File access errors
    │
    └── ...
```

### Error Propagation

```
Parser.parse()
  └─ throws SyntaxErrorException
  
ArithmeticStatement.execute()
  └─ catches exceptions
  └─ prints error message
  └─ continues execution (graceful handling)

Statement.execute()
  └─ throws Exception
  └─ Execute catches and logs
```

---

## Testing Strategy

### Test Coverage (471+ tests)

- **Unit Tests**: Individual components
- **Integration Tests**: Component interactions
- **Negative Tests**: Error conditions

### Test Categories

```
ArithmeticStatementTest (44 tests)
├── Positive operations (14 tests)
└── Negative operations (30 tests)
    ├── Error conditions (2)
    ├── Negative numbers (8)
    ├── Zero operations (6)
    ├── Overflow/underflow (4)
    └── Complex scenarios (10)

ForthParserTest (42 tests)
├── Valid inputs (40)
└── Error conditions (2)

DirectoryTest (26 tests)
├── Word storage (5)
├── Retrieval (5)
├── Tokenization (4)
└── Listing (6)

... and 359 more tests
```

### Test Organization

```
src/test/java/eu/gricom/forth/
├── statements/
├── tokenizer/
├── parser/
├── variableTypes/
├── memoryManager/
├── helper/
├── error/
└── runtimeManager/
```

---

## Performance Characteristics

### Time Complexity

| Operation | Complexity | Notes |
|-----------|------------|-------|
| Push/Pop | O(1) | Stack operations are constant time |
| Word Lookup | O(log n) | Depends on Dictionary implementation |
| Token Parse | O(n) | Linear in token count |
| Full Execution | O(n) | Linear in statement count |

### Memory Usage

| Component | Space Complexity |
|-----------|------------------|
| Stack | O(depth) |
| Program | O(# statements) |
| Directory | O(# words defined) |
| Tokenizer | O(# tokens) |

### Optimization Tips

1. **Reuse Instances**: Words are looked up once and reused
2. **Stack Efficiency**: Operations are O(1), no traversal needed
3. **Lazy Evaluation**: Only parse what's executed
4. **Minimal Copying**: Values passed by reference when possible

---

## Extension Points

### Adding New Arithmetic Operations

1. Add token type to `ForthTokenType.java`
2. Update `ForthLexer.java` to recognize token
3. Update `ForthParser.java` to parse it
4. Implement in `ArithmeticStatement.execute()`

### Adding New Data Types

1. Create class implementing `Value` interface
2. Implement all required operations
3. Add conversion methods (`toInteger()`, `toReal()`, etc.)
4. Update statement classes to handle type

### Adding New Words

1. Extend `Directory` to store definitions
2. Implement word resolution in `ForthParser`
3. Create execution mechanism for user-defined words

---

## Best Practices for Developers

1. **Maintain Singleton Pattern**: Stack must be globally accessible
2. **Use Exceptions**: Don't use return codes for errors
3. **Document Stack Effects**: Every statement should document stack changes
4. **Keep Statements Small**: Each statement should do one thing
5. **Test Edge Cases**: Especially boundary values and errors
6. **Write Comments**: Stack operations can be hard to follow

---

## Debugging

### Enable Debug Logging

Set environment variable:
```bash
DEBUG=true java -jar FORTH-0.0.1.jar
```

### Common Issues

| Issue | Cause | Solution |
|-------|-------|----------|
| EmptyStackException | Pop from empty stack | Check input has enough values |
| / by zero | Modulo/division by zero | Add guard conditions |
| Unknown word | Undefined word used | Define word before use |
| Wrong result | Stack operation order | Remember: last-in, first-out |

---

## See Also

- `01_LANGUAGE_GUIDE.md` — Language reference
- `03_STANDARD_WORDS.md` — Available words
- `04_EXAMPLES.md` — Complete examples
