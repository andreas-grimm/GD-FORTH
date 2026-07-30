# API Reference and Architecture
## Java Developer Documentation for GD-FORTH

**Version**: 0.0.1  
**Last Updated**: 2026-07-30  
**Target Audience**: Java Developers extending GD-FORTH

---

## Table of Contents

1. [Overview](#overview)
2. [Core Interfaces](#core-interfaces)
3. [Main Classes](#main-classes)
4. [Exception Hierarchy](#exception-hierarchy)
5. [Package Structure](#package-structure)
6. [Extension Points](#extension-points)
7. [Building and Compiling](#building-and-compiling)

---

## Overview

### Architecture Layers

```
┌─────────────────────────────────────────┐
│         USER (FORTH Program)            │
├─────────────────────────────────────────┤
│  Tokenizer │ Parser │ Runtime Engine    │
├─────────────────────────────────────────┤
│ Stack │ Program │ Directory │ Types     │
├─────────────────────────────────────────┤
│         Error Handling & Logging        │
└─────────────────────────────────────────┘
```

### Key Components

| Component | Package | Purpose |
|-----------|---------|---------|
| Lexer | `tokenizer` | Break source into tokens |
| Parser | `parser` | Convert tokens to statements |
| Executor | `runtimeManager` | Run statements |
| Stack | `memoryManager` | Data storage |
| Values | `variableTypes` | Typed values |

---

## Core Interfaces

### 1. Lexer Interface

```java
package eu.gricom.forth.tokenizer;

public interface Lexer {
  /**
   * Tokenize source code into tokens
   * @param strSource FORTH source code
   * @return List of tokens
   * @throws SyntaxErrorException for invalid syntax
   */
  List<Token> tokenize(String strSource) 
    throws SyntaxErrorException;
}
```

**Implementations**:
- `ForthLexer` — Full FORTH lexer implementation

---

### 2. Parser Interface

```java
package eu.gricom.forth.parser;

public interface Parser {
  /**
   * Parse tokens into executable statements
   * @return List of Statement objects
   * @throws SyntaxErrorException for syntax errors
   */
  List<Statement> parse() 
    throws SyntaxErrorException;
}
```

**Implementations**:
- `ForthParser` — Full FORTH parser

---

### 3. Statement Interface

```java
package eu.gricom.forth.statements;

public interface Statement {
  /**
   * Get the token number for this statement
   * @return token position
   */
  int getTokenNumber();

  /**
   * Execute this statement
   * @throws Exception for runtime errors
   */
  void execute() throws Exception;

  /**
   * Get human-readable content
   * @return statement description
   * @throws Exception for errors
   */
  String content() throws Exception;

  /**
   * Get structured format
   * @return JSON representation
   * @throws Exception for errors
   */
  String structure() throws Exception;
}
```

**Implementations**:
- `NumberStatement` — Push number to stack
- `ArithmeticStatement` — Perform arithmetic
- `PrintStatement` — Output to console
- `EmptyStatement` — End marker

---

### 4. Expression Interface

```java
package eu.gricom.forth.statements;

public interface Expression {
  /**
   * Evaluate this expression
   * @return computed value
   * @throws Exception for evaluation errors
   */
  Value evaluate() throws Exception;

  /**
   * Get readable form
   * @return content string
   */
  String content();

  /**
   * Get structure
   * @return JSON representation
   * @throws Exception for errors
   */
  String structure() throws Exception;
}
```

---

### 5. Value Interface

```java
package eu.gricom.forth.variableTypes;

public interface Value extends Expression {
  // Type conversions
  String toString();
  double toReal();
  int toInteger();

  // Arithmetic operations
  Value plus(Value other) throws SyntaxErrorException;
  Value minus(Value other) throws SyntaxErrorException;
  Value multiply(Value other) throws SyntaxErrorException;
  Value divide(Value other) 
    throws DivideByZeroException, SyntaxErrorException;
  Value modulo(Value other) 
    throws DivideByZeroException, SyntaxErrorException;

  // Comparison operations
  Value equals(Value other) throws SyntaxErrorException;
  Value notEqual(Value other) throws SyntaxErrorException;
  Value smallerThan(Value other) throws SyntaxErrorException;
  Value smallerEqualThan(Value other) 
    throws SyntaxErrorException;
  Value largerThan(Value other) throws SyntaxErrorException;
  Value largerEqualThan(Value other) throws SyntaxErrorException;

  // Bitwise operations
  Value shiftLeft(Value other) throws SyntaxErrorException;
  Value shiftRight(Value other) 
    throws DivideByZeroException, SyntaxErrorException;

  // Logical operations
  Value and(Value other) 
    throws DivideByZeroException, SyntaxErrorException;
  Value or(Value other) 
    throws DivideByZeroException, SyntaxErrorException;

  // Power
  Value power(Value other) throws SyntaxErrorException;
}
```

**Implementations**:
- `IntegerValue` — 32-bit signed integer
- `RealValue` — Floating-point number
- `StringValue` — Text string
- `BooleanValue` — true/false

---

## Main Classes

### Stack (Singleton)

```java
package eu.gricom.forth.memoryManager;

public class Stack {
  /**
   * Push value onto stack
   * @param oValue value to push
   */
  public void push(Value oValue);

  /**
   * Pop value from stack
   * @return top value
   * @throws EmptyStackException if stack empty
   */
  public Value pop() throws EmptyStackException;

  /**
   * Peek at top without removing
   * @return top value
   * @throws EmptyStackException if stack empty
   */
  public Value peek() throws EmptyStackException;

  /**
   * Reset stack to empty
   */
  public void reset();

  /**
   * Get readable representation
   * @return stack contents as string
   */
  public String retrieveContent();
}
```

**Key Feature**: Singleton pattern - all instances share same stack

```java
// These all access the same stack
Stack s1 = new Stack();
Stack s2 = new Stack();
// s1.push() and s2.pop() work on the same data
```

---

### Program

```java
package eu.gricom.forth.memoryManager;

public class Program {
  /**
   * Get current statements
   * @return parsed statements to execute
   */
  public List<Statement> getStatements();

  /**
   * Get all statements (historical)
   * @return all statements ever added
   */
  public List<Statement> getAllStatements();
}
```

---

### Directory (Word Dictionary)

```java
package eu.gricom.forth.memoryManager;

public class Directory {
  /**
   * Store a word definition
   * @param strWord word name
   * @param strFunction word definition
   * @throws SyntaxErrorException if definition invalid
   */
  public void storeWord(String strWord, String strFunction) 
    throws SyntaxErrorException;

  /**
   * Retrieve word definition
   * @param strWord word name
   * @return definition string or null
   */
  public String getWord(String strWord);

  /**
   * Get tokenized definition
   * @param strWord word name
   * @return token list or null
   */
  public List<Token> getToken(String strWord);

  /**
   * List all defined words
   * @return array of "WORD -> definition" strings
   */
  public String[] listWords();
}
```

---

### Execute (Runtime)

```java
package eu.gricom.forth.runtimeManager;

public class Execute {
  /**
   * Create executor for a program
   * @param oProgram the program to execute
   */
  public Execute(Program oProgram);

  /**
   * Load environment and initialize
   */
  public void loadEnvironment();

  /**
   * Execute all statements
   */
  public void runNewStatements();

  /**
   * Get last executed statement
   * @return final statement or null
   */
  public Statement getFinalStatement();
}
```

---

### Token

```java
package eu.gricom.forth.tokenizer;

public final class Token {
  /**
   * Create a token
   * @param strText token text
   * @param oType token type
   * @param iLineNumber source line
   */
  public Token(String strText, ForthTokenType oType, 
               int iLineNumber);

  /**
   * Create a token with sequence number
   * @param strText token text
   * @param oType token type
   * @param iLineNumber source line
   * @param iCommandSequenceNumber command position
   */
  public Token(String strText, ForthTokenType oType, 
               int iLineNumber, int iCommandSequenceNumber);

  // Getters
  public String getText();
  public ForthTokenType getType();
  public int getLine();
  public int getCommandSequence();

  // Setter
  public String setText(String strText);

  // Representation
  public String structure();
  public boolean equals(Token oCompareToken);
}
```

---

## Exception Hierarchy

### Exception Base Class

```java
package eu.gricom.forth.error;

public class ForthException extends Exception {
  public ForthException(String message);
  public ForthException(String message, Throwable cause);
}
```

### Specific Exceptions

#### EmptyStackException
```java
public class EmptyStackException extends ForthException {
  // Thrown when stack operation requires values but stack empty
}
```

#### DivideByZeroException
```java
public class DivideByZeroException extends ForthException {
  // Thrown for division or modulo by zero
}
```

#### SyntaxErrorException
```java
public class SyntaxErrorException extends ForthException {
  // Thrown for syntax errors during parsing/tokenization
}
```

#### FileNotFoundException
```java
public class FileNotFoundException extends ForthException {
  // Thrown when file cannot be read
}
```

#### EmptyProgramException
```java
public class EmptyProgramException extends ForthException {
  // Thrown when program is empty
}
```

### Exception Handling Example

```java
try {
  ForthLexer lexer = new ForthLexer();
  List<Token> tokens = lexer.tokenize(source);
  ForthParser parser = new ForthParser(tokens);
  List<Statement> statements = parser.parse();
  
  Program program = new Program();
  Execute executor = new Execute(program);
  executor.runNewStatements();
} 
catch (SyntaxErrorException e) {
  System.err.println("Syntax error: " + e.getMessage());
} 
catch (EmptyStackException e) {
  System.err.println("Stack error: " + e.getMessage());
} 
catch (Exception e) {
  System.err.println("Runtime error: " + e.getMessage());
}
```

---

## Package Structure

```
eu.gricom.forth
├── Forth.java                     -- Main entry point
│
├── tokenizer/
│   ├── Lexer.java                -- Interface
│   ├── ForthLexer.java           -- Implementation
│   ├── Token.java                -- Token representation
│   ├── ForthTokenType.java       -- Token enumeration
│   ├── Normalizer.java           -- String normalization
│   └── ForthReservedWords.java   -- Reserved word list
│
├── parser/
│   ├── Parser.java               -- Interface
│   ├── ForthParser.java          -- Implementation
│   └── package-info.java
│
├── statements/
│   ├── Statement.java            -- Interface
│   ├── Expression.java           -- Interface
│   ├── NumberStatement.java
│   ├── ArithmeticStatement.java
│   ├── PrintStatement.java
│   ├── EmptyStatement.java
│   └── ...
│
├── variableTypes/
│   ├── Value.java                -- Interface
│   ├── IntegerValue.java
│   ├── RealValue.java
│   ├── StringValue.java
│   └── BooleanValue.java
│
├── memoryManager/
│   ├── Stack.java                -- Singleton stack
│   ├── Program.java
│   ├── Directory.java            -- Word dictionary
│   └── LineNumberXRef.java
│
├── runtimeManager/
│   └── Execute.java              -- Execution engine
│
├── helper/
│   ├── Logger.java
│   ├── Printer.java
│   ├── FileHandler.java
│   ├── EnvParam.java
│   ├── ConsoleColors.java
│   ├── Time.java
│   └── Trace.java
│
└── error/
    ├── ForthException.java       -- Base exception
    ├── EmptyStackException.java
    ├── DivideByZeroException.java
    ├── SyntaxErrorException.java
    ├── FileNotFoundException.java
    ├── EmptyProgramException.java
    ├── FileAlreadyExistsException.java
    └── RuntimeException.java
```

---

## Extension Points

### Adding a New Arithmetic Operation

**Step 1**: Add token type
```java
// In ForthTokenType.java
POWER,  // Add new token type
```

**Step 2**: Update lexer
```java
// In ForthLexer.java
case "**":
  return new Token("**", ForthTokenType.POWER, lineNum);
```

**Step 3**: Update parser
```java
// In ForthParser.java
case POWER:
  aoStatements.add(parseArithmeticStatement());
  _iPosition++;
  break;
```

**Step 4**: Implement in statement
```java
// In ArithmeticStatement.java
case POWER:
  iResult = (int) Math.pow(iInteger_2, iInteger_1);
  break;
```

---

### Adding a New Value Type

**Step 1**: Create value class
```java
public class ComplexValue implements Value {
  private double real, imaginary;
  
  @Override
  public Value plus(Value other) throws SyntaxErrorException {
    // Implementation
  }
  // ... other methods
}
```

**Step 2**: Update type conversions
```java
// Update all Value implementations to handle ComplexValue
```

**Step 3**: Add parsing support
```java
// Update parser to recognize complex number format
```

---

### Adding a New Statement Type

**Step 1**: Create statement class
```java
public class LoopStatement implements Statement {
  private List<Statement> body;
  
  @Override
  public void execute() throws Exception {
    // Implementation
  }
}
```

**Step 2**: Update parser
```java
// Add case to parser's switch statement
```

**Step 3**: Add tests
```java
public class LoopStatementTest {
  @Test
  void testLoop() throws Exception {
    // Test implementation
  }
}
```

---

## Building and Compiling

### Maven Build

```bash
# Compile
mvn clean compile

# Run tests
mvn test

# Build JAR
mvn package

# Full build with all checks
mvn clean verify
```

### Project Structure

```
GD-FORTH/
├── pom.xml                      -- Maven configuration
├── src/
│   ├── main/java/eu/gricom/forth/
│   │   └── ...source files...
│   ├── main/resources/
│   └── test/
│       ├── java/eu/gricom/forth/
│       │   └── ...test files...
│       └── resources/
├── docs/                        -- Documentation
├── target/                      -- Build output
├── README.md
├── CHANGELOG.md
└── LICENSE.md
```

### Maven Configuration (pom.xml)

```xml
<project>
  <groupId>eu.gricom.forth</groupId>
  <artifactId>FORTH</artifactId>
  <version>0.0.1</version>
  <packaging>jar</packaging>
  
  <properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>
  
  <dependencies>
    <!-- JUnit 5 for testing -->
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
```

---

## Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ArithmeticStatementTest

# Run with coverage
mvn test jacoco:report
```

### Test Structure

```java
// Example test
public class MyStatementTest {
  @BeforeEach
  void setUp() {
    stack = new Stack();
    stack.reset();
  }
  
  @Test
  void testBasicOperation() throws Exception {
    stack.push(new IntegerValue(5));
    MyStatement stmt = new MyStatement();
    stmt.execute();
    
    assertEquals(10, ((IntegerValue)stack.pop()).toInteger());
  }
}
```

---

## Best Practices

1. **Use Interfaces**: Program to interfaces, not implementations
2. **Handle Exceptions**: Always catch checked exceptions
3. **Document Stack Effects**: Every word should document (input -- output)
4. **Test Edge Cases**: Especially boundaries and errors
5. **Keep It Simple**: FORTH values simplicity and minimalism
6. **Reuse Stack**: Minimize instance variables
7. **Avoid Globals**: Stack is already global (singleton)

---

## Performance Tips

1. **Stack is O(1)**: All push/pop operations are constant time
2. **Cache Words**: Dictionary lookups should be fast
3. **Lazy Evaluation**: Don't parse unexecuted code
4. **Minimize Allocation**: Reuse Value objects when possible

---

## See Also

- `01_LANGUAGE_GUIDE.md` — User guide
- `02_IMPLEMENTATION_GUIDE.md` — Architecture details
- `03_STANDARD_WORDS.md` — Word reference
- `04_EXAMPLES.md` — Code examples
