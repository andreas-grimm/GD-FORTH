# GD-FORTH Documentation

**Version:** 0.0.2  
**Last Updated:** October 3, 2026

---

## 📚 Documentation Structure

The documentation is organized into clear categories for easy navigation:

### 🚀 Getting Started

Start here if you're new to GD-FORTH:

| Document | Purpose |
|----------|---------|
| [00-GETTING-STARTED.md](guides/00-GETTING-STARTED.md) | Quick start guide to install and run GD-FORTH |
| [02-LANGUAGE-GUIDE.md](guides/02-LANGUAGE-GUIDE.md) | Introduction to the FORTH language basics |

### 📖 Guides & Tutorials

Comprehensive guides for users:

| Document | Purpose |
|----------|---------|
| [DOCUMENTATION-SUMMARY.md](guides/DOCUMENTATION-SUMMARY.md) | Overview of all available documentation |
| [INDEX.md](guides/INDEX.md) | Complete documentation index and structure |

### 🔧 Implementation & API

Technical documentation for developers:

| Document | Purpose |
|----------|---------|
| [03-IMPLEMENTATION-GUIDE.md](api/03-IMPLEMENTATION-GUIDE.md) | Architecture and implementation details |
| [04-STANDARD-WORDS.md](api/04-STANDARD-WORDS.md) | Reference of implemented FORTH words |
| [06-API-REFERENCE.md](api/06-API-REFERENCE.md) | Java API reference for developers |

### ✨ Features

Documentation of specific features:

| Feature | Document |
|---------|----------|
| **IF/ELSE/THEN** | [IF_IMPLEMENTATION_SUMMARY.md](features/IF_IMPLEMENTATION_SUMMARY.md) |
| | [FORTH_IF_TEST_EXAMPLES.md](features/FORTH_IF_TEST_EXAMPLES.md) |
| | [IF_EXECUTION_DEMO.md](features/IF_EXECUTION_DEMO.md) |

### 💡 Code Examples

Practical examples and use cases:

| Document | Purpose |
|----------|---------|
| [05-CODE-EXAMPLES.md](examples/05-CODE-EXAMPLES.md) | FORTH code examples and patterns |

### 🧪 Tests & Test Harness

Test documentation and examples:

| Document | Purpose |
|----------|---------|
| [TEST-HARNESS.md](tests/TEST-HARNESS.md) | FORTH test harness documentation |
| [test-if-then.md](tests/test-if-then.md) | IF/THEN test examples |
| [test-stack-basics.md](tests/test-stack-basics.md) | Stack operation tests |
| [test-arithmetic-basic.md](tests/test-arithmetic-basic.md) | Arithmetic operation tests |
| [test-boolean.md](tests/test-boolean.md) | Boolean operation tests |
| [test-numbers.md](tests/test-numbers.md) | Number handling tests |
| [test-memory-cells.md](tests/test-memory-cells.md) | Memory cell tests |
| [test-word-definition.md](tests/test-word-definition.md) | Word definition tests |
| [test-exception-handling.md](tests/test-exception-handling.md) | Exception handling tests |
| [test-do-loop.md](tests/test-do-loop.md) | DO...LOOP tests |

### 📚 Archive

Documentation from predecessor projects:

- **Bashforth Documentation** - Complete documentation of bashforth v0.63a implementation
  - Located in [archive/bashforth/](archive/bashforth/)
  - 5 document types × 5 versions each
  - ~144,000 lines of technical analysis

---

## 🎯 Quick Navigation by Role

### For New Users
1. Read [00-GETTING-STARTED.md](guides/00-GETTING-STARTED.md)
2. Study [02-LANGUAGE-GUIDE.md](guides/02-LANGUAGE-GUIDE.md)
3. Try [05-CODE-EXAMPLES.md](examples/05-CODE-EXAMPLES.md)

### For Developers
1. Review [03-IMPLEMENTATION-GUIDE.md](api/03-IMPLEMENTATION-GUIDE.md)
2. Check [06-API-REFERENCE.md](api/06-API-REFERENCE.md)
3. Read [04-STANDARD-WORDS.md](api/04-STANDARD-WORDS.md)

### For Feature Developers
1. Check [IF_IMPLEMENTATION_SUMMARY.md](features/IF_IMPLEMENTATION_SUMMARY.md)
2. Review test patterns in [tests/test-if-then.md](tests/test-if-then.md)
3. Study [FORTH_IF_TEST_EXAMPLES.md](features/FORTH_IF_TEST_EXAMPLES.md)

### For QA/Testers
1. Review [TEST-HARNESS.md](tests/TEST-HARNESS.md)
2. Check test examples in [tests/](tests/)
3. Study [IF_EXECUTION_DEMO.md](features/IF_EXECUTION_DEMO.md)

---

## 📋 Directory Structure

```
docs/
├── README.md                          (this file)
├── _INDEX.md                          (master documentation index)
│
├── guides/                            (User guides and tutorials)
│   ├── 00-GETTING-STARTED.md
│   ├── 02-LANGUAGE-GUIDE.md
│   ├── INDEX.md
│   ├── DOCUMENTATION-SUMMARY.md
│   └── README-REFERENCE.md
│
├── api/                               (API and Implementation docs)
│   ├── 03-IMPLEMENTATION-GUIDE.md
│   ├── 04-STANDARD-WORDS.md
│   └── 06-API-REFERENCE.md
│
├── features/                          (Feature-specific documentation)
│   ├── IF_IMPLEMENTATION_SUMMARY.md
│   ├── FORTH_IF_TEST_EXAMPLES.md
│   └── IF_EXECUTION_DEMO.md
│
├── examples/                          (Code examples and patterns)
│   └── 05-CODE-EXAMPLES.md
│
├── tests/                             (Test documentation)
│   ├── TEST-HARNESS.md
│   ├── test-if-then.md
│   ├── test-stack-basics.md
│   ├── test-arithmetic-basic.md
│   ├── test-boolean.md
│   ├── test-numbers.md
│   ├── test-memory-cells.md
│   ├── test-word-definition.md
│   ├── test-exception-handling.md
│   └── test-do-loop.md
│
└── archive/                           (Legacy documentation)
    └── bashforth/                     (Bashforth v0.63a documentation)
        ├── Beginners_Guide_v*.md
        ├── Comparison_To_Standard_Forth_v*.md
        ├── Detailed_Low_Level_Description_v*.md
        ├── Forth_Implementation_Specification_v*.md
        ├── Language_Agnostic_Design_v*.md
        └── DOCUMENTATION-TEMPLATE.md
```

---

## 🔄 Documentation Categories

### User-Facing Documentation
- Guides for installation, setup, and basic usage
- Language reference and tutorials
- Code examples and patterns

### Developer-Facing Documentation
- API reference for Java integration
- Implementation details and architecture
- Standard words reference

### Feature Documentation
- Feature-specific guides and examples
- Verification and testing results
- Architecture decisions

### Test Documentation
- Test framework and harness documentation
- Individual test suites and examples
- Test execution patterns

### Archived Documentation
- Legacy bashforth implementation documentation
- Reference material from predecessor projects
- Historical design decisions

---

## 📝 Latest Updates

### Version 0.0.2 (October 3, 2026)
- **New:** IF/ELSE/THEN control flow implementation
  - Feature documentation complete
  - Test examples for standard FORTH semantics
  - Live execution demonstration
- **Reorganized:** Documentation structure for better navigation
- **Added:** Feature-specific documentation directory

### Version 0.0.1 (July 30, 2026)
- Initial check-in
- Core FORTH interpreter functionality
- Comprehensive test suites

---

## 🎓 Learning Resources

### Starting Points
1. **New to FORTH?** → [02-LANGUAGE-GUIDE.md](guides/02-LANGUAGE-GUIDE.md)
2. **Getting started?** → [00-GETTING-STARTED.md](guides/00-GETTING-STARTED.md)
3. **Want examples?** → [05-CODE-EXAMPLES.md](examples/05-CODE-EXAMPLES.md)

### Deep Dives
1. **Architecture details** → [03-IMPLEMENTATION-GUIDE.md](api/03-IMPLEMENTATION-GUIDE.md)
2. **API reference** → [06-API-REFERENCE.md](api/06-API-REFERENCE.md)
3. **FORTH standard words** → [04-STANDARD-WORDS.md](api/04-STANDARD-WORDS.md)

### Feature Study
1. **IF statement** → [features/](features/)
2. **Test patterns** → [tests/](tests/)
3. **Execution demo** → [features/IF_EXECUTION_DEMO.md](features/IF_EXECUTION_DEMO.md)

---

## 📞 Support

For questions or issues:
1. Check the relevant documentation file
2. Review code examples in [examples/](examples/)
3. Study test cases in [tests/](tests/)
4. Consult API documentation in [api/](api/)

---

## 📄 License

All documentation in this directory is provided under the same license as the GD-FORTH project.
See the LICENSE.md file in the project root for details.

---

**Last Updated:** October 3, 2026  
**Documentation Version:** 0.0.2  
**GD-FORTH Version:** 0.0.2
