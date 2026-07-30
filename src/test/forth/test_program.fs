\ This is a comment line in Forth

: IS-EVEN? ( n -- )
    2 MOD 0 = IF
        CR . ." is an even number!" CR
    ELSE
        CR . ." is an odd number!" CR
    THEN ;

: RUN-DEMO ( -- )
    CR ." --- Forth Demo Loaded ---" CR
    10 IS-EVEN?
    7 IS-EVEN? ;
