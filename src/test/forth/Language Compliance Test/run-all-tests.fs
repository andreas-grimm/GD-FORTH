\ Master Test Runner for Forth Interpreter
\ Executes all test programs and generates summary report

CR
CR ." ========================================"
CR ." FORTH INTERPRETER TEST SUITE RUNNER"
CR ." ========================================"
CR

\ Track overall results
VARIABLE TOTAL-PASSED
VARIABLE TOTAL-FAILED
0 TOTAL-PASSED !
0 TOTAL-FAILED !

\ Run each test category

CR ." [1] Core Numbers and Boolean Operations"
CR ." =========================================="
INCLUDE test-numbers.fs
INCLUDE test-boolean.fs
INCLUDE test-bitwise.fs
INCLUDE test-comparisons.fs
INCLUDE test-radix.fs

CR ." [2] Stack Manipulation"
CR ." ========================"
INCLUDE test-stack-basics.fs
INCLUDE test-stack-advanced.fs
INCLUDE test-stack-depth.fs
INCLUDE test-stack-edge-cases.fs
INCLUDE test-return-stack.fs
INCLUDE test-stack-load-patterns.fs

CR ." [3] Arithmetic Operations"
CR ." ==========================="
INCLUDE test-arithmetic-basic.fs
INCLUDE test-arithmetic-division.fs
INCLUDE test-arithmetic-wide.fs
INCLUDE test-arithmetic-edge.fs
INCLUDE test-float-arithmetic.fs

CR ." [4] Memory Access"
CR ." ==================="
INCLUDE test-memory-cells.fs
INCLUDE test-memory-chars.fs
INCLUDE test-memory-alignment.fs
INCLUDE test-memory-move.fs
INCLUDE test-memory-fill.fs

CR ." [5] Control Flow - Conditionals"
CR ." =================================="
INCLUDE test-if-then.fs
INCLUDE test-if-else.fs
INCLUDE test-nested-conditionals.fs
INCLUDE test-conditional-edge.fs
INCLUDE test-case-of.fs

CR ." [6] Control Flow - Loops"
CR ." ============================"
INCLUDE test-begin-until.fs
INCLUDE test-begin-while-repeat.fs
INCLUDE test-do-loop.fs
INCLUDE test-do-plusloop.fs
INCLUDE test-nested-loops.fs
INCLUDE test-unloop.fs

CR ." [7] Word Definitions"
CR ." ======================="
INCLUDE test-word-definition.fs
INCLUDE test-word-parameters.fs
INCLUDE test-recursion.fs
INCLUDE test-word-shadowing.fs
INCLUDE test-defer.fs

CR ." [8] I/O Operations"
CR ." ====================="
INCLUDE test-emit.fs
INCLUDE test-string-output.fs
INCLUDE test-dot-notation.fs
INCLUDE test-io-buffering.fs

CR ." [9] Advanced Features"
CR ." ==============================="
INCLUDE test-evaluate.fs
INCLUDE test-create-does.fs
INCLUDE test-exception-handling.fs
INCLUDE test-colon-definitions-advanced.fs

CR CR ." ========================================"
CR ." OVERALL TEST SUMMARY"
CR ." ========================================"
CR ." Run: gforth run-all-tests.fs"
CR ." Or:  forth < run-all-tests.fs"
CR

BYE
