# GD-FORTH Documentation Index
## Complete Reference and Guide

**Version**: 0.0.1  
**Last Updated**: 2026-07-30  
**Project**: FORTH Interpreter in Java

---

## Quick Navigation

### For FORTH Users

Start here if you want to **write FORTH programs**:

1. **[01_LANGUAGE_GUIDE.md](01_LANGUAGE_GUIDE.md)** ⭐ START HERE
   - Introduction to FORTH concepts
   - Stack-based programming
   - Arithmetic and control flow
   - Word definitions
   - 20 worked examples

2. **[03_STANDARD_WORDS.md](03_STANDARD_WORDS.md)** 📚 REFERENCE
   - Complete word reference
   - Stack effects for all words
   - Examples for each operation
   - Performance notes

3. **[04_EXAMPLES.md](04_EXAMPLES.md)** 💡 TUTORIALS
   - Basic calculations
   - Stack manipulation patterns
   - Complex programs
   - Real-world applications
   - 20+ working examples

### For Java Developers

Start here if you want to **extend or understand the implementation**:

1. **[02_IMPLEMENTATION_GUIDE.md](02_IMPLEMENTATION_GUIDE.md)** ⭐ START HERE
   - Architecture overview
   - Core components explanation
   - Execution flow
   - Module structure
   - Error handling
   - Testing strategy

2. **[05_API_REFERENCE.md](05_API_REFERENCE.md)** 📚 REFERENCE
   - Complete API documentation
   - All interfaces and classes
   - Exception hierarchy
   - Package structure
   - Extension points
   - Maven configuration

---

## Document Map

```
00_DOCUMENTATION_INDEX.md
  ↓
  ├─→ 01_LANGUAGE_GUIDE.md (User Tutorial)
  │   ├─ Core Concepts
  │   ├─ Stack Operations
  │   ├─ Arithmetic
  │   ├─ Data Types
  │   ├─ Control Structures
  │   └─ Word Definitions
  │
  ├─→ 02_IMPLEMENTATION_GUIDE.md (Developer Reference)
  │   ├─ Architecture Overview
  │   ├─ Core Components
  │   ├─ Execution Flow
  │   ├─ Module Structure
  │   ├─ Data Structures
  │   └─ Error Handling
  │
  ├─→ 03_STANDARD_WORDS.md (Complete Reference)
  │   ├─ Arithmetic Operations
  │   ├─ Stack Operations
  │   ├─ Comparison Operations
  │   ├─ I/O Operations
  │   ├─ Control Flow
  │   └─ Word Definition
  │
  ├─→ 04_EXAMPLES.md (Tutorials & Patterns)
  │   ├─ Basic Calculations
  │   ├─ Stack Manipulation
  │   ├─ Word Definitions
  │   ├─ Control Flow
  │   ├─ Complex Programs
  │   ├─ Real-World Applications
  │   └─ Common Patterns
  │
  └─→ 05_API_REFERENCE.md (Java API)
      ├─ Core Interfaces
      ├─ Main Classes
      ├─ Exception Hierarchy
      ├─ Package Structure
      ├─ Extension Points
      └─ Building & Testing
```

---

## Document Descriptions

### 01_LANGUAGE_GUIDE.md

**Purpose**: Teach FORTH programming to new users  
**Audience**: FORTH programmers, beginners  
**Content**:
- What is FORTH and why use it?
- Core concepts (stack, words, execution model)
- Complete overview of all operations
- Data types and ranges
- Control structures
- Word definitions and composition
- Error handling
- Best practices and limitations

**Time to Read**: 30-45 minutes  
**Hands-On**: Pair with REPL session

---

### 02_IMPLEMENTATION_GUIDE.md

**Purpose**: Explain how the interpreter works  
**Audience**: Java developers, system designers  
**Content**:
- High-level architecture (3-stage pipeline)
- Design principles
- Component breakdown (5 major components)
- Token processing
- Parsing to AST
- Runtime execution
- Stack management
- Memory organization
- Complete execution flow diagram
- Performance characteristics
- Extension mechanisms
- Debugging guide

**Time to Read**: 45-60 minutes  
**Best Paired With**: Source code review

---

### 03_STANDARD_WORDS.md

**Purpose**: Complete reference for all FORTH words  
**Audience**: FORTH programmers looking up operations  
**Content**:
- All arithmetic operations (+, -, *, /, MOD)
- Stack operations (DUP, DROP, SWAP, OVER, ROT)
- Comparison operations (>, <, =, etc.)
- Logical operations (AND, OR, NOT)
- I/O operations (., .S, CR, PRINT)
- Control flow (IF...THEN, DO...LOOP, EXIT)
- Word definition (: and ;)
- Advanced operations (bit shifts, power, etc.)
- Word classification (primitive vs defined)
- Error conditions for each word
- Performance notes
- Alphabetical index

**Time to Read**: 20-30 minutes  
**Usage**: Quick lookup while programming

---

### 04_EXAMPLES.md

**Purpose**: Learn by example  
**Audience**: FORTH learners, practitioners  
**Content**:
- 20+ complete working examples
- Basic calculations and arithmetic
- Stack manipulation techniques
- Defining reusable words
- Control flow patterns
- Complex algorithms (Fibonacci, primes, GCD)
- Real-world applications (temperature conversion, distance formula, interest calculations, unit conversions)
- Interactive session examples
- Common programming patterns
- Debugging techniques
- Performance examples
- Exercises with solutions
- Tips and tricks

**Time to Read**: 60+ minutes (reference + hands-on)  
**Best Used**: Interactive exploration with REPL

---

### 05_API_REFERENCE.md

**Purpose**: Complete Java API documentation  
**Audience**: Java developers extending GD-FORTH  
**Content**:
- Architecture overview
- Core interfaces (Lexer, Parser, Statement, Expression, Value)
- Main classes (Stack, Program, Directory, Execute, Token)
- Exception hierarchy and handling
- Complete package structure with file organization
- Extension points for:
  - Adding new arithmetic operations
  - Adding new value types
  - Adding new statement types
- Maven build configuration
- Testing strategy
- Best practices for developers
- Performance optimization tips

**Time to Read**: 40-50 minutes  
**Prerequisites**: Java knowledge, source code familiarity

---

## Quick Reference

### Essential Concepts

| Concept | Document | Section |
|---------|----------|---------|
| What is FORTH? | 01_LANGUAGE_GUIDE | Introduction |
| Stack basics | 01_LANGUAGE_GUIDE | Core Concepts |
| Arithmetic | 01_LANGUAGE_GUIDE | Arithmetic Operations |
| Word definition | 01_LANGUAGE_GUIDE | Word Definitions |
| All operations | 03_STANDARD_WORDS | Complete |
| Implementation | 02_IMPLEMENTATION_GUIDE | Architecture |
| Java API | 05_API_REFERENCE | Core Interfaces |

### By Task

| Task | Start With |
|------|-----------|
| Learn FORTH | 01_LANGUAGE_GUIDE → 04_EXAMPLES |
| Look up a word | 03_STANDARD_WORDS |
| See an example | 04_EXAMPLES |
| Understand architecture | 02_IMPLEMENTATION_GUIDE |
| Extend the interpreter | 05_API_REFERENCE |
| Debug a program | 04_EXAMPLES (Debugging Techniques) |
| Optimize performance | 02_IMPLEMENTATION_GUIDE (Performance) |

### Quick Lookup Tables

**Operations by Type**:
- Arithmetic: `03_STANDARD_WORDS.md#arithmetic-operations`
- Stack: `03_STANDARD_WORDS.md#stack-operations`
- Comparison: `03_STANDARD_WORDS.md#comparison-operations`
- I/O: `03_STANDARD_WORDS.md#io-operations`

**Learning Path**:
1. Read `01_LANGUAGE_GUIDE.md` (30 min)
2. Try examples from `04_EXAMPLES.md` (30 min)
3. Keep `03_STANDARD_WORDS.md` handy for reference
4. Build your own programs

---

## Getting Started Guide

### For FORTH Users (First Time)

**Day 1** (1-2 hours):
1. Read: Introduction to "01_LANGUAGE_GUIDE.md"
2. Read: Core Concepts section
3. Try: Examples 1-5 from "04_EXAMPLES.md"
4. Practice: Enter commands in REPL

**Day 2** (1-2 hours):
1. Read: Arithmetic Operations in "01_LANGUAGE_GUIDE.md"
2. Read: Stack Operations in "01_LANGUAGE_GUIDE.md"
3. Try: Examples 6-12 from "04_EXAMPLES.md"
4. Practice: Simple word definitions

**Week 1+**:
1. Read: Word Definitions section
2. Try: Complex examples from "04_EXAMPLES.md"
3. Reference: `03_STANDARD_WORDS.md` as needed
4. Create: Your own FORTH programs

### For Java Developers (First Time)

**Phase 1** (2-3 hours):
1. Read: "01_LANGUAGE_GUIDE.md" to understand FORTH
2. Try: Examples from "04_EXAMPLES.md"
3. Read: "02_IMPLEMENTATION_GUIDE.md" sections 1-3

**Phase 2** (2-3 hours):
1. Read: Rest of "02_IMPLEMENTATION_GUIDE.md"
2. Read: "05_API_REFERENCE.md" sections 1-4
3. Review: Source code with documentation

**Phase 3** (3+ hours):
1. Study: Package structure ("05_API_REFERENCE.md")
2. Review: Exception handling patterns
3. Plan: Extension point you want to implement
4. Build: Your modification/extension

---

## FAQ - Which Document?

**Q: I want to learn FORTH**  
A: Start with `01_LANGUAGE_GUIDE.md`, then `04_EXAMPLES.md`

**Q: How do I use a specific word?**  
A: Check `03_STANDARD_WORDS.md` for the word

**Q: How does the interpreter work?**  
A: Read `02_IMPLEMENTATION_GUIDE.md`

**Q: I want to add a new feature**  
A: Read `05_API_REFERENCE.md#extension-points`

**Q: I need code examples**  
A: Check `04_EXAMPLES.md` or `03_STANDARD_WORDS.md`

**Q: What's the complete word reference?**  
A: See `03_STANDARD_WORDS.md`

**Q: How do I build/test?**  
A: Check `05_API_REFERENCE.md#building-and-compiling`

---

## Document Maintenance

| Document | Last Updated | Status | Notes |
|----------|--------------|--------|-------|
| 00_DOCUMENTATION_INDEX.md | 2026-07-30 | Current | Index and navigation |
| 01_LANGUAGE_GUIDE.md | 2026-07-30 | Current | Complete language reference |
| 02_IMPLEMENTATION_GUIDE.md | 2026-07-30 | Current | Full architecture docs |
| 03_STANDARD_WORDS.md | 2026-07-30 | Current | Complete word reference |
| 04_EXAMPLES.md | 2026-07-30 | Current | 20+ working examples |
| 05_API_REFERENCE.md | 2026-07-30 | Current | Complete Java API |

---

## Documentation Standards

All documentation follows these standards:

1. **Clarity**: Written for target audience (users or developers)
2. **Completeness**: Covers the topic thoroughly
3. **Examples**: Practical code examples for every major concept
4. **Structure**: Clear hierarchy with table of contents
5. **Cross-References**: Links between related documents
6. **Formatting**: Markdown with consistent style

---

## Related Files

### In Repository Root

- `README.md` — Project overview
- `CHANGELOG.md` — Version history
- `LICENSE.md` — License information
- `pom.xml` — Maven configuration

### In /docs Directory

All documentation files are in this directory with clear naming:
- `00_*` — Index and overview
- `01_*` — FORTH language
- `02_*` — Implementation
- `03_*` — Reference
- `04_*` — Examples
- `05_*` — API

### Test Documentation

Test reports are generated by:
- `mvn test` — Run all tests
- `mvn test jacoco:report` — Generate coverage report

---

## Feedback and Corrections

Found an error or want to improve the documentation?

1. Check all documents for consistency
2. Verify examples actually work
3. Ensure clarity for target audience
4. Submit corrections via issue tracker

---

## Version Information

**Documentation Version**: 0.0.1  
**FORTH Implementation**: 0.0.1  
**Java Target**: Java 21+  
**Test Coverage**: 471 tests, 100% passing

---

## Document Index by Audience

### Users/Learners
1. Start: `01_LANGUAGE_GUIDE.md`
2. Learn: `04_EXAMPLES.md`
3. Reference: `03_STANDARD_WORDS.md`

### Developers
1. Understand: `01_LANGUAGE_GUIDE.md` (10 min)
2. Learn: `02_IMPLEMENTATION_GUIDE.md`
3. Reference: `05_API_REFERENCE.md`
4. Code: Source + tests

### Maintainers
1. All documents
2. Source code
3. Test suite
4. Build configuration

---

## Quick Start Commands

```bash
# Read documentation
cat docs/01_LANGUAGE_GUIDE.md

# View example programs
cat docs/04_EXAMPLES.md

# Check available words
grep "^### " docs/03_STANDARD_WORDS.md

# Build the project
mvn clean package

# Run tests
mvn test

# Start interactive REPL
java -jar target/FORTH-0.0.1-jar-with-dependencies.jar
```

---

## Summary

This comprehensive documentation covers:

✅ **User Guide** — Learn and use FORTH  
✅ **Language Reference** — All operations documented  
✅ **Tutorials** — 20+ working examples  
✅ **Implementation Guide** — How it works internally  
✅ **API Reference** — Complete Java documentation  
✅ **Quick Navigation** — Find what you need fast  

**Total Documentation**: 50,000+ words  
**Examples**: 50+ working code samples  
**Diagrams**: Complete architecture overview  

Happy programming! 🚀
