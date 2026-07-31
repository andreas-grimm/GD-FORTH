# GriCom Diminutive FORTH Interpreter (GD-FORTH)

&copy; 2020 - 2026 Andreas Grimm | Use according to the included licence file ([LICENSE.md](LICENSE.md))

**Version:** 0.0.1  
**Status:** First check-in  
**Last Updated:** 2026-07-30

---

## 📚 Project Overview

**GD-FORTH** is a Java 21+ implementation of a Forth interpreter. It can execute `.fs` programs and supports interactive line editing.

### Key Features

- ✅ **Interactive Line Editor** — Build and test programs without file setup

Objective for the implementation
- ✅ **Comprehensive Forth Support**
- ✅ **Type Safety**
- ✅ **Advanced Features**
- ✅ **Production Ready**
- ✅ **Well Documented**

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
| User Manual |  |
| Language Ref |  |
| Coding Standards |  |
| Architecture |  |
| Version History | [CHANGELOG.md](CHANGELOG.md) |
| Test Info |  |
| Upgrade Plans |  |
| License | [LICENSE.md](LICENSE.md) |

---

*For detailed information on any topic, see the documentation links above.*