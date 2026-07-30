# Bashforth Language-Agnostic Design Specification - Version 1

## Abstract

This document describes the Bashforth Forth interpreter implementation in a way that is independent of any programming language. The design focuses on abstract data structures, algorithms, and state management that could be implemented in any language (Python, Java, C, Rust, etc.).

## Core Design Principles

1. **Stack-Based Computation** - All computation uses implicit stacks
2. **Compiled Interpretation** - Source compiles to bytecode, then executes
3. **Virtual Machine Model** - Register-based VM with multiple stacks
4. **Dictionary-Driven** - All behavior defined in dictionary of named words
5. **Reflection** - System can introspect and modify itself

## Abstract Data Model

### Core Data Structures

```
DataStack = Array[Integer]
  - Fixed size array of integers
  - Elements represent signed integers or addresses
  - Top element cached separately for performance
  
ReturnStack = Array[Integer]
  - Stores return addresses and loop parameters
  - Interleaved: 2 values per loop (limit, start)
  
StringStack = Array[String]
  - Stores variable-length strings
  - Top element cached separately
  
Memory = Array[Word]
  where Word = Integer | String | FunctionReference
  - Unified memory for code and data
  - Can store integers, strings, or references to functions
  
Dictionary = OrderedMap[String, WordDefinition]
  WordDefinition = {
    name: String
    codeAddress: Integer       # Address in Memory where code starts
    flags: BitSet              # immediate, smudge, etc.
    isImmediate: Boolean
    isHidden: Boolean
  }
  
HeaderArray = Array[String]            # Word names indexed by word number
ExecutionTokenArray = Array[Integer]   # Code addresses indexed by word number
FlagsArray = Array[Integer]            # Flags indexed by word number
```

### State Variables

```
MachineState = {
  // Stack Pointers
  dataStackPointer: Integer    # Points to top of data stack
  dataStackBase: Integer       # Origin of data stack
  returnStackPointer: Integer  # Points to top of return stack
  returnStackBase: Integer     # Origin of return stack
  stringStackPointer: Integer  # Points to top of string stack
  stringStackBase: Integer     # Origin of string stack
  
  // Virtual Machine Registers
  instructionPointer: Integer  # Next instruction address
  wordPointer: Integer         # Current word being executed
  topOfStack: Integer          # Cached top data stack value
  
  // Dictionary State
  wordCount: Integer           # Total words defined
  dictionaryPointer: Integer   # Next free memory address
  lastWordAddress: Integer     # Address of last word's CFA
  
  // Compilation State
  compileState: Boolean        # true=compile, false=interpret
  base: Integer                # Numeric base (10, 16, 2, etc)
  
  // Exception Handling
  exceptionFrame: Integer      # Linked list of catch frames
  
  // Configuration
  padDistance: Integer         # Distance between HERE and PAD
  tibSize: Integer             # Size of input buffer
}
```

## Execution Model

### Program Counter & Instruction Fetch

```
Algorithm: ExecuteInstruction
  instruction := Memory[instructionPointer]
  instructionPointer := instructionPointer + 1
  
  if instruction is Function then
    Execute(instruction)
  else if instruction is Integer then
    Push(instruction, DataStack)
  else if instruction is Address then
    Call(instruction)
```

### Call Stack (Return Stack)

```
Algorithm: CallWord(address)
  Push(instructionPointer, ReturnStack)
  instructionPointer := address

Algorithm: ReturnFromWord
  instructionPointer := Pop(ReturnStack)
```

### Stack Operations - Data Stack

```
Push(value):
  dataStack[++sp] = value

Pop():
  return dataStack[sp--]

Peek():
  return dataStack[sp]

Drop():
  sp := sp - 1

Dup():
  dataStack[++sp] = dataStack[sp-1]

Swap():
  temp = dataStack[sp]
  dataStack[sp] = dataStack[sp-1]
  dataStack[sp-1] = temp

Over():
  dataStack[++sp] = dataStack[sp-2]

Depth():
  return sp - stackBase
```

### Loop Parameter Storage

Loops use return stack pairs to store limit and counter:

```
StartLoop(limit, start):
  Push(limit, ReturnStack)        # r[rp-1]
  Push(start, ReturnStack)        # r[rp]
  
GetLoopIndex():
  return Peek(ReturnStack)         # i = r[rp]
  
GetLoopLimit():
  return Peek(ReturnStack, offset=1) # limit = r[rp-1]

LoopIteration():
  counter := Peek(ReturnStack)
  limit := Peek(ReturnStack, offset=1)
  increment counter
  if counter has not reached limit:
    branch back to loop start
  else:
    Pop two values from ReturnStack
    continue after loop
```

## Compilation Model

### State Transition Diagram

```
State: Interpret Mode (compileState = false)
  ReadWord(name)
    if DictionaryContains(name):
      Execute(Dictionary[name].codeAddress)
    else if IsNumber(name):
      Push(number, DataStack)
    else:
      Error: Word not found
      
State: Compile Mode (compileState = true)
  ReadWord(name)
    if DictionaryContains(name):
      if Dictionary[name].isImmediate:
        Execute(Dictionary[name].codeAddress)
      else:
        Compile(Dictionary[name].codeAddress)
    else if IsNumber(name):
      CompileAsLiteral(number)
    else:
      Error: Word not found
```

### Compiling a Word Definition

```
Algorithm: CompileWordDefinition
  input: wordName, sourceTokens
  
  Create new dictionary entry for wordName
  Save current dictionaryPointer as CFA
  
  // Compile function prologue
  Compile(NEST)  // Call other words
  
  // Compile word body
  for each token in sourceTokens:
    if IsWord(token):
      Compile(GetExecutionToken(token))
    else if IsNumber(token):
      Compile(LIT)
      Compile(token)
  
  // Compile function epilogue
  Compile(UNNEST)  // Return from word
  
  Reveal(wordName)  // Make visible
```

### Literal Compilation

```
Algorithm: CompileLiteral(value)
  Compile(LIT)
  Compile(value)
  
// At runtime:
LIT:
  Push(Memory[instructionPointer], DataStack)
  instructionPointer := instructionPointer + 1
```

## Dictionary Management

### Lookup Algorithm

```
Algorithm: FindWord(name)
  for index from wordCount-1 down to 0:
    if HeaderArray[index] == name:
      if not FlagsArray[index].hidden:
        return ExecutionTokenArray[index]
  return NOT_FOUND
```

### Adding Words to Dictionary

```
Algorithm: AddWordToDictionary
  HeaderArray[wordCount] := name
  ExecutionTokenArray[wordCount] := currentMemoryPointer
  FlagsArray[wordCount] := flags
  
  Save codeAddress to memory at currentMemoryPointer
  Increment wordCount
```

### Word Flags

```
Flags = BitSet{
  IMMEDIATE: bit 0  // Execute at compile time
  SMUDGE:    bit 1  // Hide from dictionary search
}

Reveal(word):
  Set bit SMUDGE in FlagsArray[word]
  
Hide(word):
  Clear bit SMUDGE in FlagsArray[word]
  
IsImmediate(word):
  return (FlagsArray[word] & IMMEDIATE) != 0
```

## String Processing

### String Stack

Separate stack for variable-length strings:

```
Algorithm: PushString(memoryAddress, length)
  Convert memory range to string
  Push onto StringStack
  
Algorithm: PopString()
  return Top(StringStack)
  Pop from StringStack

Algorithm: PackMemoryToString(address, length)
  string := ""
  for i from 0 to length-1:
    byteValue := Memory[address + i]
    string := string + chr(byteValue)
  return string

Algorithm: UnpackStringToMemory(string, address)
  for i from 0 to length-1:
    Memory[address + i] := ord(string[i])
```

### String Operations

```
Append(string1, string2):
  return string1 + string2

Substring(string, start, length):
  return string[start:start+length]

Compare(string1, string2):
  if string1 < string2: return -1
  if string1 == string2: return 0
  if string1 > string2: return 1
```

## Control Flow

### Branching

```
Algorithm: BranchUnconditional
  offset := Memory[instructionPointer]
  instructionPointer := instructionPointer + offset

Algorithm: BranchIfZero
  condition := Pop(DataStack)
  offset := Memory[instructionPointer]
  if condition == 0:
    instructionPointer := instructionPointer + offset
  else:
    instructionPointer := instructionPointer + 1
```

### Flow Control Structures

```
// if...then
IF:
  condition := Pop(DataStack)
  if condition == 0:
    jump to THEN
    
// if...else...then
IF:
  condition := Pop(DataStack)
  if condition == 0:
    jump to ELSE
THEN:
  skip past THEN

// begin...until
BEGIN:
  save address
  execute body
UNTIL:
  condition := Pop(DataStack)
  if condition == 0:
    jump back to BEGIN

// do...loop
DO:
  start := Pop(DataStack)
  limit := Pop(DataStack)
  Push(limit, ReturnStack)
  Push(start, ReturnStack)
LOOP:
  Increment(Top(ReturnStack))
  if Top(ReturnStack) != Peek(ReturnStack, 1):
    jump back to DO+1
  else:
    Pop(ReturnStack)  // pop both limit and counter
```

## Exception Handling

### Catch Frame Structure

```
CatchFrame = {
  instructionPointer: Integer     // Saved IP
  stackPointer: Integer           // Saved SP
  previousFrame: CatchFrame       // Linked list of frames
  throwHandler: Address           // Where to jump on throw
}

CurrentCatchFrame: CatchFrame or null
```

### Catch/Throw Mechanism

```
Algorithm: CATCH(wordAddress)
  Save current IP to ReturnStack
  Save current SP to ReturnStack
  Save CurrentCatchFrame to ReturnStack
  CurrentCatchFrame := current frame
  
  Execute word at wordAddress
  
  // If execution completes normally:
  if no exception:
    Restore CurrentCatchFrame from ReturnStack
    Return normally

Algorithm: THROW(exceptionCode)
  if exceptionCode != 0:
    if CurrentCatchFrame != null:
      IP := CurrentCatchFrame.instructionPointer
      SP := CurrentCatchFrame.stackPointer
      CurrentCatchFrame := CurrentCatchFrame.previousFrame
      Push(exceptionCode, DataStack)
    else:
      // Top-level exception handler
      Print error message
      Reset interpreter state
      Return to prompt
  else:
    // 0 throw - successful completion
    Continue normally
```

## Word Categories (Abstract)

```
PRIMITIVE_WORD = {
  type: "primitive"
  code: NativeFunction        // Direct implementation
  immediate: Boolean
}

HIGH_LEVEL_WORD = {
  type: "high_level"
  body: Array[ExecutionToken] // Sequence of other words
  immediate: Boolean
}

VARIABLE = {
  type: "variable"
  address: Integer            // Address in memory
  initialValue: Integer
}

CONSTANT = {
  type: "constant"
  value: Integer
}

DEFERRED_WORD = {
  type: "deferred"
  target: ExecutionToken
  mutable: Boolean            // Can be changed at runtime
}
```

## I/O and Input Parsing

### Input Buffer Management

```
InputStream = {
  buffer: Array[Byte]
  position: Integer
  delimiter: Integer          // ASCII code
}

Algorithm: ReadWord(delimiter)
  // Skip leading delimiters
  while position < length and buffer[position] == delimiter:
    position := position + 1
  
  // Read until delimiter or end
  start := position
  while position < length and buffer[position] != delimiter:
    position := position + 1
  
  return buffer[start:position]
```

### Number Parsing

```
Algorithm: ParseNumber(string, base)
  value := 0
  isNegative := false
  
  if string[0] == '-':
    isNegative := true
    string := string[1:]
  
  for each character in string:
    digit := ConvertCharToDigit(character, base)
    if digit >= base:
      return PARSE_ERROR
    value := value * base + digit
  
  if isNegative:
    value := -value
  
  return value
```

## Numeric Output

### Pictured Numeric Output

```
Algorithm: ConvertNumberToString(number, base)
  digits := []
  isNegative := number < 0
  number := abs(number)
  
  if number == 0:
    digits.append('0')
  
  while number > 0:
    remainder := number mod base
    digit := ConvertDigitToChar(remainder)
    digits.append(digit)
    number := number / base
  
  if isNegative:
    digits.append('-')
  
  return reverse(digits)
```

## Memory Layout and Allocation

```
MemoryLayout = {
  0:                    [Code Space - Compiled Words]
  dictionaryPointer:    [Free Space for new words]
  dictionaryPointer:    [User Data Variables]
  dictionaryPointer + padDistance: [Scratch Pad PAD]
  maxMemory:            [Boundary]
}

Algorithm: Allot(cells)
  dictionaryPointer := dictionaryPointer + cells

Algorithm: HERE()
  return dictionaryPointer
```

## Initialization and Startup

```
Algorithm: ColdStart
  // Reset all state
  InitializeStacks()
  
  // Load system dictionary
  for each primitive word:
    AddToDictionary(primitive)
  
  for each high-level word:
    AddToDictionary(high_level)
  
  // Start interpreter
  compileState := false
  instructionPointer := bootAddress
  ExecuteMainLoop()

Algorithm: WarmStart
  // Partial reset (keep dictionary)
  ResetStacks()
  compileState := false
  instructionPointer := promptAddress
  ExecuteMainLoop()
```

## Performance Considerations

```
OPTIMIZATION: Top of Stack Caching
  Keep topOfStack value in fast register/variable
  Only sync with array on stack overflow

OPTIMIZATION: Instruction Dispatch Unrolling
  Unroll main execution loop N times
  Reduces branch prediction failures
  
OPTIMIZATION: Compiled Words
  Pre-compile high-level words to bytecode
  No interpretation overhead at runtime
  
OPTIMIZATION: Immediate Words
  Execute at compile time when used in definitions
  Reduces runtime overhead for control structures
```

## Summary of Key Algorithms

| Operation | Time | Space |
|-----------|------|-------|
| Push/Pop | O(1) | O(1) |
| FindWord | O(n) | O(1) |
| ExecuteWord | O(1) to O(n) | O(n) |
| CompileWord | O(n) | O(n) |
| BranchOffset | O(1) | O(1) |
| CatchFrame | O(1) | O(1) |

n = number of words in dictionary or length of word body
