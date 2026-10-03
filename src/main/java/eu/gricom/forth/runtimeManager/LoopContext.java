package eu.gricom.forth.runtimeManager;

/**
 * LoopContext.java
 *
 * Represents the state of a single DO...LOOP execution.
 * Stores the current index, limit, and step size for loop control.
 *
 * Stack notation: Each DO loop pushes one LoopContext onto the loop stack.
 * Nested loops maintain separate LoopContext objects, allowing I (current) and
 * J (outer) to access the appropriate index values.
 *
 * Example: 0 5 DO I LOOP
 *   - Initial: limit=5, currentIndex=0, stepSize=1
 *   - Loop iteration 1: currentIndex=0
 *   - Loop iteration 2: currentIndex=1
 *   - ...continues until currentIndex >= limit
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class LoopContext {
    private long _lCurrentIndex;
    private long _lLimit;
    private long _lStepSize;

    /**
     * Constructor for creating a new loop context.
     *
     * @param lLimit      the upper bound (loop exits when currentIndex >= limit)
     * @param lIndex      the starting index (initial currentIndex value)
     * @param lStepSize   the increment per iteration (typically 1, or custom for +LOOP)
     */
    public LoopContext(long lLimit, long lIndex, long lStepSize) {
        _lLimit = lLimit;
        _lCurrentIndex = lIndex;
        _lStepSize = lStepSize;
    }

    /**
     * Get the current loop index.
     *
     * @return the current iteration count / loop index
     */
    public long getCurrentIndex() {
        return _lCurrentIndex;
    }

    /**
     * Increment the loop index by the step size.
     * This is called by LOOP or +LOOP statements.
     */
    public void increment() {
        _lCurrentIndex += _lStepSize;
    }

    /**
     * Set the step size for this loop.
     * Used by +LOOP to specify custom increment values.
     *
     * @param lStepSize the new step size
     */
    public void setStepSize(long lStepSize) {
        _lStepSize = lStepSize;
    }

    /**
     * Check if the loop should terminate.
     * Returns true when the loop has completed all iterations.
     *
     * Standard FORTH semantics:
     * - For positive step: loop exits when currentIndex >= limit
     * - For negative step: loop exits when currentIndex <= limit
     *
     * @return true if loop iteration is complete, false if loop should continue
     */
    public boolean isComplete() {
        // Positive step: exit when currentIndex >= limit
        if (_lStepSize > 0) {
            return _lCurrentIndex >= _lLimit;
        }
        // Negative step: exit when currentIndex <= limit
        else if (_lStepSize < 0) {
            return _lCurrentIndex <= _lLimit;
        }
        // Step size of 0 would create infinite loop - considered complete
        else {
            return true;
        }
    }

    /**
     * Get the loop limit.
     *
     * @return the limit value
     */
    public long getLimit() {
        return _lLimit;
    }

    /**
     * Get the step size.
     *
     * @return the step size
     */
    public long getStepSize() {
        return _lStepSize;
    }
}
