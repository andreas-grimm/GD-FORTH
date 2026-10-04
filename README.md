# GriCom Diminutive FORTH Interpreter (GD-FORTH)

&copy; 2020 - 2026 Andreas Grimm | Use according to the included licence file ([LICENSE.md](LICENSE.md))

**Version:** 0.0.3  
**Status:** Phase 2 Control Flow Complete (15 loop/conditional keywords)  
**Last Updated:** 2026-10-04

---

## 📚 Project Overview

**GD-FORTH** is a Java 21+ implementation of a Forth interpreter. It can execute `.fs` programs and supports interactive line editing.

### Key Features

- ✅ **Interactive Line Editor** — Build and test programs without file setup
- ✅ **Complete Control Flow** — All major FORTH loop and conditional constructs
  - **Conditionals**: IF...THEN, IF...ELSE...THEN (arbitrary nesting)
  - **Counted loops**: DO...LOOP, DO...+LOOP with I/J index access
  - **Indefinite loops**: BEGIN...WHILE...REPEAT (condition-at-top)
  - **Do-while loops**: BEGIN...UNTIL (condition-at-bottom)
  - **Infinite loops**: BEGIN...AGAIN with LEAVE early exit
  - **15 total control flow keywords implemented**
- ✅ **Arithmetic Operations** — +, -, *, /, MOD, and more
- ✅ **Stack Operations** — DUP, DROP, SWAP, OVER, ROT, NIP, TUCK, PICK, ROLL, DEPTH
- ✅ **Comparison Operations** — =, <>, <, >, <=, >=, 0=, 0<>, 0>, 0<
- ✅ **Variable Support** — VARIABLE definition and access
- ✅ **Memory Operations** — FETCH (@), STORE (!)
- ✅ **I/O Operations** — PRINT (.), CARRIAGE_RETURN (CR), QUESTION (?)

Implementation Status:
- ✅ **Phase 1: Basic Operations** (100% complete)
  - Stack operations, arithmetic, comparisons, variables, memory I/O
- ✅ **Phase 2: Control Flow** (100% complete)
  - IF/THEN/ELSE, DO/LOOP, BEGIN/WHILE/REPEAT, BEGIN/UNTIL, BEGIN/AGAIN, LEAVE
- ⏳ **Phase 3: Advanced Features** (Planned)
- ✅ **Type Safety** (Integer, Long, Real, Boolean, String types)
- ✅ **Production Ready** (1867 tests passing, 0 warnings)
- ✅ **Well Documented** (Comprehensive guides, API docs, test examples)

# Differences to existing Standards:

## No support of the Result Stack

As the modification of the running code by the programming is nowadays seen as an antipattern, this interpreter will not
implement that functionality. Therefore, the following defined reserved words will not be implemented:

| Reserved word | Description                               |
|---------------|-------------------------------------------|
| >R      	    | Move top stack value to return stack      |
| R>	        | Move top return stack value to stack      |
| R@	        | Copy top return stack value to stack      |
| 2>R	        | Move top two values to return stack       |
| 2R>	        | Move top two return stack values to stack |
| 2R@	        | Copy top two return stack values to stack |
| N>R	        | Move n values to return stack             |
| NR>	        | Move n values from return stack           |

---

## 🚀 Quick Start

### Installation

```bash
# Requirements: Java 21+
java -version  # Should show Java 21 or later

# Build from source
mvn clean package
# Generates: FORTH-0.0.3-jar-with-dependencies.jar
```

### Run Interactive Mode (No File Required)

```bash
java -jar target/FORTH-0.0.3-jar-with-dependencies.jar
FORTH> 2 2 + .
4
FORTH> 
```

### Load and Execute a Program

```bash
java -jar target/FORTH-0.0.3-jar-with-dependencies.jar
FORTH> : HELLO ." Hello World" CR ;
FORTH> HELLO
Hello World
FORTH> 5 10 + .
15
FORTH> 
```

### Execute a FORTH File

```bash
java -jar target/FORTH-0.0.3-jar-with-dependencies.jar program.fs
```

---

## 📖 Documentation

---

## Quick Navigation

### 📚 Learning Forth with GD-FORTH

| Topic | File | Audience |
|-------|------|----------|
| **GD-FORTH Programming Guideline** | [docs/GD-Forth_Programming_Guideline.md](docs/GD-Forth_Programming_Guideline.md) | Junior Developers (Start here!) |
| Getting started with Forth | [docs/GD-Forth_Programming_Guideline.md#getting-started-with-gd-forth](docs/GD-Forth_Programming_Guideline.md#getting-started-with-gd-forth) | New to Forth |
| Stack operations and control flow | [docs/GD-Forth_Programming_Guideline.md#the-stack-foundation-of-forth](docs/GD-Forth_Programming_Guideline.md#the-stack-foundation-of-forth) | Fundamentals |
| Word definitions and patterns | [docs/GD-Forth_Programming_Guideline.md#word-definitions](docs/GD-Forth_Programming_Guideline.md#word-definitions) | Intermediate |

### 🛠️ Extending GD-FORTH

| Topic | File | Audience |
|-------|------|----------|
| **Programmers Guide** | [docs/Programmers_Guide.md](docs/Programmers_Guide.md) | Junior Developers (Implementation) |
| Architecture overview | [docs/Programmers_Guide.md](docs/Programmers_Guide.md) | Developers extending interpreter |
| How to add new keywords | [docs/Programmers_Guide.md](docs/Programmers_Guide.md) | Implementation guide |

### 📋 Reference & History

| Topic | File |
|-------|------|
| Version History & Release Notes | [CHANGELOG.md](CHANGELOG.md) |
| Project TODO & Roadmap | [TODO.md](TODO.md) |
| License | [LICENSE.md](LICENSE.md) |

---

## Documentation Overview

### For Forth Programmers (New Users)
Start with **[docs/GD-Forth_Programming_Guideline.md](docs/GD-Forth_Programming_Guideline.md)** for:
- Introduction to Forth concepts
- Step-by-step learning path
- Interactive examples
- Common patterns and best practices
- Troubleshooting guide

### For Java Developers (Extending GD-FORTH)
Review **[docs/Programmers_Guide.md](docs/Programmers_Guide.md)** for:
- GD-FORTH architecture
- How the interpreter works
- How to implement new FORTH words
- Code organization and patterns
- Implementation examples (DUP2, custom operators)

### For Project Management
Check **[CHANGELOG.md](CHANGELOG.md)** for:
- Version history and features
- Release notes with test results
- Implementation progress

---

*Choose a guide above based on your needs: learning Forth or extending the interpreter.*