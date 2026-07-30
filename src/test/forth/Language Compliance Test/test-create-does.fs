\ Test 9.2: CREATE/DOES> data structures
\ Verifies compile-time/runtime splitting for data structures

INCLUDE test-harness.fs

CR ." === Test 9.2: CREATE/DOES> Data Structures ==="

\ Simple CREATE with DOES>
T{ CREATE CONST 42 , DOES> @ ; CONST -> 42 }T

\ CREATE multiple data values
T{ CREATE PAIR 10 , 20 , DOES> 2@ + ; PAIR -> 30 }T

\ CREATE with offset
T{ CREATE DATA 100 , 200 , 300 , DOES> @ ; DATA -> 100 }T
T{ CREATE DATA2 100 , 200 , 300 , DOES> CELL + @ ; DATA2 -> 200 }T

\ CREATE with computation in DOES>
T{ CREATE DOUBLE 5 , DOES> @ 2 * ; DOUBLE -> 10 }T

\ CREATE multiple instances
T{ CREATE NUM1 1 , DOES> @ ; CREATE NUM2 2 , DOES> @ ; NUM1 NUM2 + -> 3 }T

\ CREATE with array-like access
T{ CREATE ARRAY 10 , 20 , 30 , DOES> 2 CELLS + @ ; ARRAY -> 30 }T

\ CREATE used as field definition
T{ CREATE POINT: 0 , DOES> @ ; 99 POINT: -> 99 }T

\ CREATE with transformation
T{ CREATE NEGATE 7 , DOES> @ 0 SWAP - ; NEGATE -> -7 }T

\ CREATE with complex DOES> logic
T{ CREATE COMPUTE 5 , 3 , DOES> DUP @ SWAP CELL + @ + ; COMPUTE -> 8 }T

\ Multiple CREATEs with shared behavior
T{ CREATE VAL1 50 , CREATE VAL2 75 ,
   : FETCH>10 @ 10 - ; VAL1 FETCH>10 VAL2 FETCH>10 + -> 100 }T

\ CREATE in word definition
T{ : MAKECONST CREATE , DOES> @ ; 999 MAKECONST MC MC -> 999 }T

\ CREATE with conditional DOES>
T{ CREATE SMART 15 , DOES> @ DUP 10 > IF 1 ELSE 0 THEN ; SMART -> 1 }T

TEST-SUMMARY
