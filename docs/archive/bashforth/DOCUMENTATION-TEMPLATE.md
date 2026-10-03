# Test Documentation Template

Use this template to create documentation for a new test file.

---

# Test X.Y: [Test Name]

## Overview

The **test-filename.fs** file validates [what is being tested]. Tests cover [specific aspects].

## Test Cases

1. **[Case 1 name]**: [What it tests]
2. **[Case 2 name]**: [What it tests]
3. **[Case 3 name]**: [What it tests]
4. (continue as needed)

## What Is Being Tested

- **[Aspect 1]**: [Explanation]
- **[Aspect 2]**: [Explanation]
- **[Aspect 3]**: [Explanation]
- (continue as needed)

## Structure of Test Cases

Each [operation name] test follows:

```forth
T{ <input> <operation> -> <expected-output> }T
```

### Pattern Explanation

[Explain how tests are structured, what notation is used, etc.]

**Example**:
```forth
T{ 5 DUP -> 5 5 }T
```
[Explain this specific example]

## Code Documentation

### [Test Case 1 Name]

```forth
T{ <example-code> -> <expected-result> }T
```

**Execution**:
1. [Step 1]
2. [Step 2]
3. [Step 3]

**Expected stack**: [result]

[Additional explanation if needed]

### [Test Case 2 Name]

```forth
T{ <example-code> -> <expected-result> }T
```

**Execution**:
1. [Step 1]
2. [Step 2]

[Continue for other important test cases]

### [More Complex Test Case]

```forth
T{ <complex-example> -> <expected-result> }T
```

**Execution flow**:
1. [Detailed step 1]
2. [Detailed step 2]
3. [Detailed step 3]

**Stack diagram**:
```
Before: [a][b][c]
After:  [x][y][z]
```

[Detailed explanation]

## Expected Outcome

### Successful Run

```
=== Test X.Y: [Test Name] ===
Total: NN
Passed: NN
Failed: 0
All tests passed!
```

Each test passes when:
1. [Condition 1]
2. [Condition 2]
3. [Condition 3]

### Failure Scenarios

**[Failure Type 1]:**
```
T{ <example> -> <expected> }T  (expects <X>, got <Y>)
```
[Explanation of what this failure means]

**[Failure Type 2]:**
```
T{ <example> -> <expected> }T  (expects <X>, got <Y>)
```
[Explanation]

[More failure scenarios as needed]

## Dependency Chain

This test depends on:
- **test-file1.fs** — Must pass first
- **test-file2.fs** — Required functionality

This test must pass before:
- **test-file3.fs** — Depends on this
- **test-file4.fs** — Depends on this

## Notes

- **[Important note 1]**: [Explanation]
- **[Important note 2]**: [Explanation]
- **[Implementation detail]**: [Explanation]

## Common Patterns

[If applicable, show common usage patterns]

```forth
Pattern 1:
  [code example]

Pattern 2:
  [code example]
```

---

**Test File**: test-filename.fs  
**Category**: [Category Name]  
**Test Count**: [Number]  
**Priority**: [CRITICAL/HIGH/MEDIUM/LOW]

---

## Instructions for Using This Template

### Before You Start

1. Run the test file: `gforth test-filename.fs`
2. Read the source file: `cat test-filename.fs`
3. Look at similar test documentation for examples
4. Copy this template to create: `docs/test-filename.md`

### Section-by-Section Guidance

**Overview**: 
- 2-3 sentences describing what the test file validates
- Example: "The test-numbers.fs file validates that the Forth interpreter correctly parses and represents integer numbers..."

**Test Cases**:
- List the name of each test
- Add a brief one-line description
- Copy test names from the test file itself

**What Is Being Tested**:
- Convert "Test Cases" from names to functionality
- Example: "- **DUP correctness**: Duplicates top item without consuming it"
- Include edge cases and special values

**Structure of Test Cases**:
- Explain how tests are written
- Show the syntax/pattern
- Explain notation used (e.g., stack notation)
- Provide 1-2 example test patterns

**Code Documentation**:
- Pick 5-10 important test cases
- For each, show:
  1. The test code
  2. Step-by-step execution
  3. Expected result
- Include visual diagrams for complex operations
- Explain WHY the result is what it is

**Expected Outcome**:
- Copy the actual output format from running the test
- List 3-5 conditions for passing
- Show 3-5 common failure scenarios
- Explain what each failure means

**Dependency Chain**:
- Check [INDEX.md](INDEX.md) for dependencies
- List 2-3 tests this depends on
- List 2-3 tests that depend on this

**Notes**:
- Add any gotchas, edge cases, or special considerations
- Implementation-specific behavior
- Performance notes if relevant

### Writing Tips

1. **Use real examples**: Copy actual test code, don't synthesize it
2. **Show execution**: Use step-by-step execution traces, not just descriptions
3. **Explain why**: Don't just describe what happens; explain why
4. **Be specific**: "Duplicates top item" not just "Stack operation"
5. **Use diagrams**: Visual representations help understanding
6. **Cross-reference**: Link to related tests
7. **Include outcomes**: Show what success and failure look like

### Checking Your Work

- [ ] All test cases from test file are listed
- [ ] Code examples are from actual test file
- [ ] Execution flow is clear and step-by-step
- [ ] Expected outcomes include success and failure
- [ ] Dependencies are accurate and link to real tests
- [ ] Notes include any gotchas or edge cases
- [ ] Formatting matches other test documentation
- [ ] All code examples are properly formatted
- [ ] Links work (if using markdown links)
- [ ] Category and test count are correct

### Common Mistakes to Avoid

1. ❌ Don't copy the code without understanding it
2. ❌ Don't skip failure scenarios
3. ❌ Don't make up dependencies; verify them
4. ❌ Don't use abstract examples; use real test code
5. ❌ Don't forget to include the frontmatter (file name, category, priority)
6. ❌ Don't skip the "Code Documentation" section

### Template Customization

- This template is generic; adjust sections as needed
- Some tests might not have "Common Patterns" — remove if not needed
- Some tests might need additional sections for complex behavior
- Always include the 8 main sections in order

### Examples

Look at these documented files for reference:

- **[test-numbers.md](test-numbers.md)** — Simple arithmetic operations
- **[test-stack-basics.md](test-stack-basics.md)** — Stack operations with diagrams
- **[test-if-then.md](test-if-then.md)** — Conditional structures
- **[test-do-loop.md](test-do-loop.md)** — Loop structures with execution flow

## Quick Reference

### Priority Levels

- **CRITICAL** — All other tests depend on this (usually foundational)
- **HIGH** — Essential functionality needed by many tests
- **MEDIUM** — Important but not all tests depend on it
- **LOW** — Nice-to-have features, often I/O related

### Categories

- Core Numbers & Boolean Operations
- Stack Manipulation
- Arithmetic Operations
- Memory Access & Operations
- Control Flow - Conditionals
- Control Flow - Loops
- Word Definitions & Dictionary
- I/O Operations
- Advanced Features

### Format Checklist

- [ ] Title follows format: `# Test X.Y: [Name]`
- [ ] All main sections included
- [ ] Code examples in `` ` ` ` `` blocks
- [ ] Markdown formatting is clean
- [ ] Links are functional
- [ ] Front matter included at bottom
- [ ] Matches existing documentation style

---

**Template Version**: 1.0  
**Last Updated**: 2026-07-20  
**For Questions**: See README.md or INDEX.md
