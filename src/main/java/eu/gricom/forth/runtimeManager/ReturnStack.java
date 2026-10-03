package eu.gricom.forth.runtimeManager;

import java.util.Stack;

/**
 * ReturnStack.java
 *
 * Manages return addresses and loop contexts for FORTH execution.
 * Originally designed for return addresses, now also manages the loop stack
 * for nested DO...LOOP control structures.
 *
 * Static architecture: All methods are static, maintaining a single shared
 * return stack and loop stack across the entire FORTH program execution.
 *
 * Loop Stack: A separate stack of LoopContext objects for managing nested loops.
 * When DO executes, it pushes a LoopContext. When LOOP completes, it pops.
 * This allows proper nesting of DO...LOOP blocks.
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class ReturnStack {
    private static Stack _oStack = new Stack<>();
    private static Stack<LoopContext> _loopStack = new Stack<>();

    /**
     * Push a loop context onto the loop stack.
     * Called by DoStatement when entering a DO...LOOP block.
     *
     * @param oLoopContext the loop context to push
     */
    public static void pushLoop(LoopContext oLoopContext) {
        _loopStack.push(oLoopContext);
    }

    /**
     * Pop a loop context from the loop stack.
     * Called by DoStatement or UnloopStatement when exiting a DO...LOOP block.
     *
     * @return the popped LoopContext
     * @throws java.util.EmptyStackException if loop stack is empty
     */
    public static LoopContext popLoop() {
        return _loopStack.pop();
    }

    /**
     * Peek at the current (innermost) loop context without removing it.
     * Called by I statement and LOOP statement.
     *
     * @return the current LoopContext
     * @throws java.util.EmptyStackException if loop stack is empty
     */
    public static LoopContext peekLoop() {
        return _loopStack.peek();
    }

    /**
     * Peek at a loop context at a specific depth in the loop stack.
     * Used by J statement to access outer loop indices.
     *
     * Depth 0: current loop (same as peekLoop())
     * Depth 1: outer loop (for J)
     * Depth 2: outer-outer loop (for nested J)
     *
     * @param iDepth the depth from the top of the stack
     * @return the LoopContext at that depth
     * @throws java.util.EmptyStackException if depth exceeds stack size
     */
    public static LoopContext peekLoop(int iDepth) {
        // The stack is 0-indexed from the top
        // peekLoop(0) = peek() = current loop
        // peekLoop(1) = one below current = outer loop
        int iIndex = _loopStack.size() - 1 - iDepth;
        if (iIndex < 0) {
            throw new java.util.EmptyStackException();
        }
        return _loopStack.elementAt(iIndex);
    }

    /**
     * Check if we are currently inside a loop.
     *
     * @return true if the loop stack is not empty (we're in a loop)
     */
    public static boolean isLoopActive() {
        return !_loopStack.isEmpty();
    }

    /**
     * Get the current size of the loop stack.
     * Useful for debugging and validation.
     *
     * @return the number of active loops
     */
    public static int getLoopStackSize() {
        return _loopStack.size();
    }

    /**
     * Reset the loop stack.
     * Called when reinitializing the interpreter or clearing state.
     */
    public static void resetLoopStack() {
        _loopStack.clear();
    }

    /**
     * Reset all stacks (return stack and loop stack).
     * Called when reinitializing the interpreter.
     */
    public static void reset() {
        _oStack.clear();
        _loopStack.clear();
    }
}
