# GD-FORTH Implementation TO-DO List

**Last Updated:** 2026-07-31  
**Status:** Initial documentation of unimplemented FORTH words

---

## Overview

This document maintains a comprehensive list of all FORTH reserved words that have not yet received implementation in the GD-FORTH interpreter. The interpreter currently implements **20 words** out of **420+ reserved words**.

### Currently Implemented Words (20)
- Arithmetic: `+`, `-`, `*`, `/`, `MOD`
- Comparison: `=`, `<`, `>`, `<=`, `>=`, `<>`
- Zero Comparison: `0=`, `0<`, `0>`, `0<>`
- Stack/Output: `.`, (carriage return)
- Utility: NUMBER token

---

## Unimplemented FORTH Words by Category

### Core Stack Operations (50+ words)

| Word | Purpose |
|------|---------|
| `!` | Store value in memory at address |
| `@` | Fetch value from memory at address |
| `2!` | Store two values (cell pair) in memory |
| `2@` | Fetch two values (cell pair) from memory |
| `C!` | Store byte value in memory |
| `C@` | Fetch byte value from memory |
| `?` | Fetch and display value from memory address |
| `DUP` | Duplicate top stack value |
| `?DUP` | Duplicate top value if non-zero |
| `DROP` | Remove top stack value |
| `2DROP` | Remove top two stack values |
| `SWAP` | Exchange top two stack values |
| `2SWAP` | Exchange top two pairs of values |
| `OVER` | Copy second stack value to top |
| `2OVER` | Copy second pair to top |
| `ROT` | Rotate top three stack values |
| `2ROT` | Rotate top three pairs |
| `-ROT` | Reverse rotate top three values |
| `NIP` | Remove second stack value |
| `TUCK` | Copy top value and place below second |
| `PICK` | Copy nth stack value to top |
| `ROLL` | Move nth stack value to top |
| `DEPTH` | Push current stack depth |

### Arithmetic Operations (30+ words)

| Word | Purpose |
|------|---------|
| `1+` | Add 1 to top stack value |
| `1-` | Subtract 1 from top stack value |
| `2*` | Multiply top value by 2 (shift left) |
| `2/` | Divide top value by 2 (shift right) |
| `ABS` | Replace with absolute value |
| `NEGATE` | Negate top stack value |
| `MAX` | Replace top two values with maximum |
| `MIN` | Replace top two values with minimum |
| `M*` | Multiply, return 64-bit result |
| `M*/` | Multiply then divide, maintaining precision |
| `M+` | Multiply and add |
| `UM*` | Unsigned multiply |
| `UM/MOD` | Unsigned divide and remainder |
| `SM/REM` | Signed divide with remainder |
| `FM/MOD` | Floor divide with modulo |
| `D+` | Add two 64-bit values |
| `D-` | Subtract two 64-bit values |
| `D2*` | Double precision multiply by 2 |
| `D2/` | Double precision divide by 2 |
| `DABS` | Double precision absolute value |
| `DNEGATE` | Double precision negate |
| `S>D` | Convert signed to double |
| `D>S` | Convert double to signed |

### Logical and Bitwise Operations (15+ words)

| Word | Purpose |
|------|---------|
| `AND` | Bitwise AND |
| `OR` | Bitwise OR |
| `XOR` | Bitwise XOR |
| `INVERT` | Bitwise NOT (one's complement) |
| `LSHIFT` | Logical left shift |
| `RSHIFT` | Logical right shift |
| `TRUE` | Push true flag (-1) |
| `FALSE` | Push false flag (0) |
| `WITHIN` | Check if value is within range |

### Control Flow (40+ words)

| Word | Purpose |
|------|---------|
| `IF` | Start conditional block |
| `THEN` | End conditional block |
| `ELSE` | Else clause in conditional |
| `?DO` | Conditional loop start |
| `DO` | Start counted loop |
| `LOOP` | End loop, increment counter |
| `+LOOP` | End loop with variable increment |
| `UNLOOP` | Exit loop early |
| `LEAVE` | Exit current loop |
| `BEGIN` | Start indefinite loop |
| `UNTIL` | End loop with exit condition |
| `WHILE` | Loop while condition true |
| `REPEAT` | Unconditional loop jump |
| `AGAIN` | Unconditional loop repeat |
| `AHEAD` | Skip forward unconditionally |
| `EXIT` | Exit current word/function |
| `CASE` | Start case statement |
| `ENDCASE` | End case statement |
| `OF` | Case label |
| `ENDOF` | End case label |
| `EXECUTE` | Execute word at address |
| `RECURSE` | Call current word recursively |
| `:NONAME` | Define unnamed word |
| `EVALUATE` | Parse and execute string |

### Word Definition (20+ words)

| Word | Purpose |
|------|---------|
| `:` | Start word definition |
| `;` | End word definition |
| `CONSTANT` | Define named constant |
| `VARIABLE` | Define named variable |
| `VALUE` | Define value (assignable constant) |
| `2CONSTANT` | Define 64-bit constant |
| `2VALUE` | Define 64-bit assignable value |
| `2VARIABLE` | Define 64-bit variable |
| `FCONSTANT` | Define floating-point constant |
| `FVALUE` | Define floating-point assignable value |
| `FVARIABLE` | Define floating-point variable |
| `DEFER` | Define deferred word |
| `DEFER!` | Set deferred word behavior |
| `DEFER@` | Get deferred word address |
| `ACTION-OF` | Get xt of deferred word |
| `IS` | Assign value to deferred word |
| `TO` | Assign value to VALUE/FVALUE |
| `CREATE` | Create named memory location |
| `DOES>` | Define action for created word |
| `,` | Comma: compile value into word |
| `C,` | Comma byte: compile byte into word |

### Memory Operations (20+ words)

| Word | Purpose |
|------|---------|
| `HERE` | Get current data space pointer |
| `ALLOT` | Allocate memory bytes |
| `CELLS` | Convert cell count to bytes |
| `CELL+` | Add one cell size to address |
| `CHAR+` | Add one byte to address |
| `CHARS` | Convert byte count to address |
| `FILL` | Fill memory with value |
| `ERASE` | Fill memory with zeros |
| `MOVE` | Copy memory region |
| `CMOVE` | Copy bytes forward |
| `CMOVE>` | Copy bytes backward |
| `ALLOCATE` | Allocate dynamic memory |
| `FREE` | Free dynamic memory |
| `RESIZE` | Resize dynamic memory |
| `DFALIGN` | Align for double-float access |
| `DFALIGNED` | Check double-float alignment |
| `SFALIGN` | Align for single-float access |
| `SFALIGNED` | Check single-float alignment |

### I/O Operations (30+ words)

| Word | Purpose |
|------|---------|
| `EMIT` | Output single character |
| `EMIT?` | Check if character can be emitted |
| `KEY` | Read single character from input |
| `KEY?` | Check if character available |
| `EKEY` | Read extended key |
| `EKEY?` | Check if extended key available |
| `EKEY>CHAR` | Convert extended key to character |
| `XKEY` | Read extended character |
| `XKEY?` | Check if extended character available |
| `TYPE` | Output string |
| `CR` | Output carriage return (newline) |
| `.R` | Output integer in field |
| `U.` | Output unsigned integer |
| `U.R` | Output unsigned integer in field |
| `D.` | Output double-precision integer |
| `D.R` | Output double-precision in field |
| `.S` | Output stack contents |
| `SPACE` | Output single space |
| `SPACES` | Output multiple spaces |
| `."` | Output string literal |
| `.(` | Output comment (no output) |
| `PAGE` | Clear screen |
| `AT-XY` | Move cursor to position |
| `ACCEPT` | Read line from input |
| `REFILL` | Refill input buffer |
| `FLUSH` | Flush output |
| `BL` | Push ASCII space character |
| `COUNT` | Get string length and address |
| `WORD` | Parse next word from input |
| `PARSE` | Parse input until delimiter |
| `PARSE-NAME` | Parse name from input |

### Floating-Point Operations (60+ words)

| Word | Purpose |
|------|---------|
| `F!` | Store float value |
| `F@` | Fetch float value |
| `SF!` | Store single-precision float |
| `SF@` | Fetch single-precision float |
| `DF!` | Store double-precision float |
| `DF@` | Fetch double-precision float |
| `F+` | Float addition |
| `F-` | Float subtraction |
| `F*` | Float multiplication |
| `F/` | Float division |
| `F**` | Float exponentiation |
| `FNEGATE` | Float negation |
| `FABS` | Float absolute value |
| `FMAX` | Float maximum |
| `FMIN` | Float minimum |
| `FLOOR` | Float floor function |
| `FROUND` | Float rounding |
| `FTRUNC` | Float truncation |
| `FSQRT` | Float square root |
| `FEXP` | Float exponential (e^x) |
| `FLN` | Float natural logarithm |
| `FLOG` | Float base-10 logarithm |
| `FSIN` | Float sine |
| `FCOS` | Float cosine |
| `FTAN` | Float tangent |
| `FASIN` | Float arcsine |
| `FACOS` | Float arccosine |
| `FATAN` | Float arctangent |
| `FATAN2` | Float two-argument arctangent |
| `FSINH` | Float hyperbolic sine |
| `FCOSH` | Float hyperbolic cosine |
| `FTANH` | Float hyperbolic tangent |
| `FASINH` | Float hyperbolic arcsine |
| `FACOSH` | Float hyperbolic arccosine |
| `FATANH` | Float hyperbolic arctangent |
| `F0<` | Float less than zero |
| `F0=` | Float equals zero |
| `F<` | Float less than |
| `F~` | Float approximate equality |
| `FDROP` | Remove float from stack |
| `FDUP` | Duplicate float stack value |
| `FSWAP` | Exchange float stack values |
| `FOVER` | Copy second float to top |
| `FROT` | Rotate float stack values |
| `FDEPTH` | Float stack depth |
| `FCONSTANT` | Define float constant |
| `FVALUE` | Define float assignable value |
| `FVARIABLE` | Define float variable |
| `FLITERAL` | Embed float literal |
| `F.` | Output float |
| `FE.` | Output float in scientific notation |
| `FS.` | Output float stack-based |
| `FLOAT+` | Add float cell size |
| `FLOATS` | Convert float count to bytes |
| `SFLOAT+` | Add single-float size |
| `SFLOATS` | Convert single-float count |
| `DFLOAT+` | Add double-float size |
| `DFLOATS` | Convert double-float count |
| `F>D` | Convert float to double |
| `F>S` | Convert float to single |
| `D>F` | Convert double to float |
| `S>F` | Convert single to float |

### String Operations (20+ words)

| Word | Purpose |
|------|---------|
| `S"` | Parse string literal |
| `C"` | Parse counted string |
| `S\"` | Parse escaped string |
| `."` | Output string literal |
| `S>D` | String to double conversion |
| `>NUMBER` | Parse number from string |
| `COMPARE` | Compare two strings |
| `/STRING` | Remove leading characters from string |
| `-TRAILING` | Remove trailing whitespace |
| `+X/STRING` | Complex string operation |
| `SUBSTITUTE` | Substitute in string |
| `UNESCAPE` | Unescape string |
| `X\STRING-` | Complex string operation |

### File I/O (20+ words)

| Word | Purpose |
|------|---------|
| `CREATE-FILE` | Create new file |
| `OPEN_FILE` | Open existing file |
| `CLOSE-FILE` | Close file handle |
| `READ-FILE` | Read from file |
| `READ-LINE` | Read line from file |
| `WRITE-FILE` | Write to file |
| `WRITE-LINE` | Write line to file |
| `FILE-POSITION` | Get file position |
| `REPOSITION-FILE` | Set file position |
| `FILE-SIZE` | Get file size |
| `FILE-STATUS` | Get file status |
| `RENAME-FILE` | Rename file |
| `DELETE-FILE` | Delete file |
| `RESIZE-FILE` | Resize file |
| `FLUSH-FILE` | Flush file buffers |
| `BIN` | Binary file mode |
| `R/O` | Read-only file mode |
| `R/W` | Read-write file mode |
| `W/O` | Write-only file mode |

### Dictionary and Word Search (20+ words)

| Word | Purpose |
|------|---------|
| `FIND` | Find word in dictionary |
| `SEARCH-WORDLIST` | Search specific wordlist |
| `WORDS` | List all defined words |
| `SEE` | Display word definition |
| `FORGET` | Delete word from dictionary |
| `NAME>STRING` | Get word name string |
| `NAME>INTERPRET` | Get interpretation semantics |
| `NAME>COMPILE` | Get compilation semantics |
| `IMMEDIATE` | Mark word as immediate |
| `DEFINITIONS` | Set definitions wordlist |
| `GET-CURRENT` | Get current wordlist |
| `GET_ORDER` | Get wordlist search order |
| `SET-CURRENT` | Set current wordlist |
| `SET-ORDER` | Set wordlist search order |
| `WORDLIST` | Create new wordlist |
| `ALSO` | Add wordlist to order |
| `ONLY` | Set wordlist to standard |
| `PREVIOUS` | Remove wordlist from order |
| `FORTH` | Standard wordlist |
| `FORTH-WORDLIST` | Get standard wordlist |
| `SEARCH` | UNKNOWN |
| `TRAVERSE-WORDLIST` | UNKNOWN |

### Compilation and Interpretation (25+ words)

| Word | Purpose |
|------|---------|
| `[` | Enter interpret mode during compilation |
| `]` | Enter compile mode |
| `[COMPILE]` | Force compilation of immediate word |
| `[IF]` | Conditional compilation: if true |
| `[ELSE]` | Conditional compilation: else clause |
| `[THEN]` | End conditional compilation |
| `[DEFINED]` | Check if word is defined |
| `[UNDEFINED]` | Check if word is not defined |
| `COMPILE,` | Compile execution token |
| `POSTPONE` | Postpone compilation of word |
| `LITERAL` | Compile literal value |
| `2LITERAL` | Compile 64-bit literal |
| `SLITERAL` | Compile string literal |
| `FLITERAL` | Compile float literal |
| `>IN` | Get input buffer index |
| `SOURCE` | Get current input source |
| `SOURCE-ID` | Get source identifier |
| `:NONAME` | Define anonymous word |
| `;CODE` | End assembly language definition |
| `ASSEMBLER` | Assembly language wordlist |
| `CODE` | Begin assembly code |
| `STATE` | Get compilation state |
| `EVALUATE` | Parse and execute string |
| `INCLUDE` | Include file |
| `REQUIRE` | Load file (once) |

### Control and System (30+ words)

| Word | Purpose |
|------|---------|
| `HALT` | UNKNOWN |
| `ABORT` | Abort execution |
| `ABORT"` | Abort with message |
| `QUIT` | Quit to command interpreter |
| `BYE` | Exit program |
| `CATCH` | Catch exception |
| `THROW` | Throw exception |
| `LOAD` | Load block from storage |
| `THRU` | Load range of blocks |
| `MARKER` | Create restore point |
| `MS` | Wait milliseconds |
| `TIME&DATE` | Get current time/date |
| `BASE` | Current number base |
| `HEX` | Set hexadecimal base |
| `DECIMAL` | Set decimal base |
| `PRECISION` | Floating-point precision |
| `SET-PRECISION` | Set FP precision |
| `ENVIRON?` | Check environment variable |
| `ENVIRONMENT?` | Check environment |
| `EDITOR` | Launch editor |
| `BLOCK` | Get block buffer |
| `BUFFER` | Get buffer |
| `BUFFER:` | Define buffer |
| `EMPTY-BUFFERS` | Clear all buffers |
| `SAVE-BUFFERS` | Save buffers to disk |
| `UPDATE` | Mark block updated |

### Advanced Structure (15+ words)

| Word | Purpose |
|------|---------|
| `BEGIN-STRUCTURE` | Start structure definition |
| `END-STRUCTURE` | End structure definition |
| `+FIELD` | Define structure field |
| `FIELD:` | Define structure field with offset |
| `CFIELD:` | Define character field |
| `SFFIELD:` | Define single-float field |
| `DFFIELD:` | Define double-float field |
| `STRUCT` | UNKNOWN |
| `ALIGN` | Align data space |
| `ALIGNED` | Check alignment |

### Miscellaneous (50+ words)

| Word | Purpose |
|------|---------|
| `DUMP` | Display memory contents |
| `SIGN` | Sign of number |
| `HOLD` | Hold string character |
| `HOLDS` | Hold string characters |
| `XHOLD` | Hold extended character |
| `>BODY` | Get word body address |
| `>FLOAT` | Convert to float |
| `'` | Get execution token of word |
| `[']` | Get execution token during compilation |
| `[CHAR]` | Get character code during compilation |
| `CHAR` | Get character code |
| `PAD` | Get temporary buffer |
| `SCR` | Current screen block number |
| `BLK` | Current block number |
| `LIST` | List screen block |
| `BLANK` | Fill with spaces |
| `REPLACES` | UNKNOWN |
| `REPRESENT` | Represent floating point |
| `UNLOOP` | Exit loop early |
| `UNUSED` | Free memory |
| `REQUIRE` | Load file once |
| `REQUIRED` | Check if required |
| `RESTORE-INPUT` | Restore input state |
| `SAVE-INPUT` | Save input state |
| `HELP` | UNKNOWN |
| `ORDER` | Display wordlist order |

### Keyboard and Extended Keys (30 words)

| Word | Purpose |
|------|---------|
| `K-ALT-MASK` | Alt key modifier |
| `K-CTRL-MASK` | Control key modifier |
| `K-SHIFT-MASK` | Shift key modifier |
| `K-UP` | Up arrow key |
| `K-DOWN` | Down arrow key |
| `K-LEFT` | Left arrow key |
| `K-RIGHT` | Right arrow key |
| `K-HOME` | Home key |
| `K-END` | End key |
| `K-PRIOR` | Page up key |
| `K-NEXT` | Page down key |
| `K-INSERT` | Insert key |
| `K-DELETE` | Delete key |
| `K-F1` | Function key F1 |
| `K-F2` | Function key F2 |
| `K-F3` | Function key F3 |
| `K-F4` | Function key F4 |
| `K-F5` | Function key F5 |
| `K-F6` | Function key F6 |
| `K-F7` | Function key F7 |
| `K-F8` | Function key F8 |
| `K-F9` | Function key F9 |
| `K-F10` | Function key F10 |
| `K-F11` | Function key F11 |
| `K-F12` | Function key F12 |
| `EKEY>FKEY` | Convert extended key to function key |
| `EKEY>XCHAR` | Convert extended key to character |
| `XC!+` | Store extended character and advance |
| `XC!+?` | Store extended character with limit |
| `XC-SIZE` | Extended character size |
| `XC-WIDTH` | Extended character width |
| `XC@+` | Fetch extended character and advance |
| `XCHAR+` | Advance by extended character |
| `XCHAR-` | Go back by extended character |
| `XEMIT` | Output extended character |

### Locals and Advanced Features (10+ words)

| Word | Purpose |
|------|---------|
| `LOCALS\|` | Define local variables |
| `(LOCAL)` | Local variable marker |
| `SYNONYM` | Create word synonym |
| `{:` | Start extension block |
| `INCLUDE-FILE` | Include specific file |
| `INCLUDED` | Include file (different version) |

---

## Summary Statistics

- **Total Reserved Words:** 420+
- **Implemented Words:** 20
- **Unimplemented Words:** 400+
- **Unknown Descriptions:** ~15

---

## Implementation Priority

### Phase 1 (Core - Essential for any program)
- [ ] All stack operations (DUP, DROP, SWAP, OVER, ROT, etc.)
- [ ] Basic I/O (EMIT, KEY, TYPE, CR)
- [ ] Memory access (@, !, C@, C!)
- [ ] All arithmetic (1+, 1-, ABS, NEGATE, etc.)

### Phase 2 (Control Flow - Enables complex programs)
- [ ] IF/THEN/ELSE
- [ ] DO/LOOP/+LOOP
- [ ] BEGIN/UNTIL/WHILE
- [ ] Conditionals (?DUP, etc.)

### Phase 3 (Word Definition - Enables code reuse)
- [ ] CONSTANT, VARIABLE, VALUE
- [ ] CREATE/DOES>
- [ ] Word compilation modes ([ ], POSTPONE, etc.)

### Phase 4 (Advanced - Extended capabilities)
- [ ] Floating-point operations
- [ ] File I/O
- [ ] Advanced strings
- [ ] Exception handling (CATCH, THROW)

### Phase 5 (Specialized - Less common features)
- [ ] Extended keyboard support
- [ ] Advanced structure definitions
- [ ] Environment queries
- [ ] Miscellaneous utilities

---

## Notes

- Words marked with "UNKNOWN" require research into standard FORTH specifications
- Some words may have equivalent implementations through other mechanisms
- Floating-point support is not yet implemented
- File I/O would require platform-specific implementations
- Dynamic memory operations (ALLOCATE, FREE) require heap management
- Extended keyboard handling requires terminal/platform support

---

## References

For detailed FORTH word definitions, refer to:
- ISO/IEC 14514 FORTH Standard
- Forth 2012 Standard Documentation
- `docs/03_STANDARD_WORDS.md` in this project

