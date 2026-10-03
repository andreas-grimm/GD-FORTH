# GriCom Diminutive FORTH Interpreter (GD-FORTH)

&copy; 2020 - 2026 Andreas Grimm | Use according to the included licence file ([LICENSE.md](LICENSE.md))

**Version:** 0.0.2  
**Status:** IF/ELSE/THEN Control Flow Implemented  
**Last Updated:** 2026-10-03

---

## 📚 Project Overview

**GD-FORTH** is a Java 21+ implementation of a Forth interpreter. It can execute `.fs` programs and supports interactive line editing.

### Key Features

- ✅ **Interactive Line Editor** — Build and test programs without file setup
- ✅ **IF/ELSE/THEN Control Flow** — Full conditional execution with arbitrary nesting
  - Simple IF...THEN conditionals
  - IF...ELSE...THEN branching
  - Nested IF statements (unlimited depth)
  - Compatible with comparison operators (>, <, =, etc.)
- ✅ **Arithmetic Operations** — +, -, *, /, MOD, and more
- ✅ **Stack Operations** — DUP, DROP, SWAP, OVER, ROT, NIP, TUCK, PICK, ROLL, DEPTH
- ✅ **Comparison Operations** — =, <>, <, >, <=, >=, 0=, 0<>, 0>, 0<
- ✅ **Variable Support** — VARIABLE definition and access
- ✅ **Memory Operations** — FETCH (@), STORE (!)
- ✅ **I/O Operations** — PRINT (.), CARRIAGE_RETURN (CR), QUESTION (?)

Objectives for full implementation:
- ✅ **Comprehensive Forth Support** (In Progress - IF/ELSE/THEN complete)
- ✅ **Type Safety** (Integer, Long, Real, Boolean, String types)
- ✅ **Advanced Features** (Control flow, word definitions)
- ✅ **Production Ready** (1809 tests passing)
- ✅ **Well Documented** (API docs, user guides, test examples)

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

# Download the JAR
# File: FORTH-0.0.1-jar-with-dependencies.jar
```

### Run Interactive Mode (No File Required)

```bash
java -jar FORTH-0.0.1-jar-with-dependencies.jar
>2 2 + .
ok
4
>
```

### Load and Execute a Program

```bash
java -jar FORTH-0.0.1-jar-with-dependencies.jar existing.fs
>: HELLO .\" Hello World\" CR ;
>HELLO
Hello World
>5 10 + .
15
>
```

### Direct Execution (No Editor)

```bash
java -jar FORTH-0.0.1-jar-with-dependencies.jar -r -q program.fs
```

---

## 📖 Documentation

---

## Quick Navigation

| Topic | File |
|-------|------|
| IF Implementation Details | [docs/IF_IMPLEMENTATION_SUMMARY.md](docs/IF_IMPLEMENTATION_SUMMARY.md) |
| IF Test Examples | [docs/FORTH_IF_TEST_EXAMPLES.md](docs/FORTH_IF_TEST_EXAMPLES.md) |
| IF Execution Demo | [docs/IF_EXECUTION_DEMO.md](docs/IF_EXECUTION_DEMO.md) |
| Version History | [CHANGELOG.md](CHANGELOG.md) |
| License | [LICENSE.md](LICENSE.md) |

---

*For detailed information on any topic, see the documentation links above.*