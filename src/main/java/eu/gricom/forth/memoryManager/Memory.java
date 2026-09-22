/**
 * Memory.java
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
package eu.gricom.forth.memoryManager;

import eu.gricom.forth.helper.EnvParam;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Manages a dynamic byte array with support for blocked (read-only) memory ranges.
 * Provides peek/poke operations for reading and writing individual bytes with bounds checking.
 * Supports loading ROM images from configuration into blocked memory areas.
 */
public class Memory {
    private static final Logger LOGGER = Logger.getLogger(Memory.class.getName());
    private byte[] _abByte;

    /**
     * Represents a contiguous range of protected memory addresses.
     */
    public static class MemoryRange {
        public int iStart;
        public int iEnd;

        /**
         * Creates a new MemoryRange with the specified boundaries.
         *
         * @param iStart the start address (inclusive)
         * @param iEnd the end address (inclusive)
         */
        public MemoryRange(int iStart, int iEnd) {
            this.iStart = iStart;
            this.iEnd = iEnd;
        }
    }

    private List<MemoryRange> _loBlockedRanges = new ArrayList<>();

    /**
     * Creates a new Memory instance with the specified size.
     *
     * @param iSize the size of the memory in bytes
     */
    public Memory(int iSize) {
        _abByte = new byte[iSize];
    }

    /**
     * Creates a new Memory instance with the specified size and loads ROM configuration.
     * Blocks memory areas and loads ROM images as specified in config.yaml.
     *
     * @param iSize the size of the memory in bytes
     * @param strRomPlatform the ROM platform name from config.yaml (e.g., "standard_16bit", "embedded", "extended")
     * @throws IllegalArgumentException if ROM image file size does not match the blocked range size
     * @throws RuntimeException if a critical ROM configuration error occurs
     */
    public Memory(int iSize, String strRomPlatform) {
        _abByte = new byte[iSize];
        loadRomConfiguration(strRomPlatform);
    }

    /**
     * Loads ROM configuration from config.yaml and initializes blocked memory ranges.
     * For each blocked range, either loads a ROM image file or fills with 0x00.
     *
     * @param strRomPlatform the ROM platform name from config.yaml
     * @throws IllegalArgumentException if ROM image file size does not match the blocked range size
     * @throws RuntimeException if a critical ROM configuration error occurs
     */
    @SuppressWarnings("unchecked")
    private void loadRomConfiguration(String strRomPlatform) {
        Map<String, Object> mRomConfig = EnvParam.getRomConfiguration(strRomPlatform);

        if (mRomConfig == null) {
            LOGGER.warning("ROM platform '" + strRomPlatform + "' not found. No ROM areas will be blocked.");
            return;
        }

        // Extract platform name
        String strPlatformName = (String) mRomConfig.get("name");
        LOGGER.info("Loading ROM configuration for platform: " + strPlatformName);

        // Extract blocked ranges
        Object oBlockedRanges = mRomConfig.get("blocked_ranges");
        if (!(oBlockedRanges instanceof List)) {
            LOGGER.warning("No blocked_ranges found in ROM configuration");
            return;
        }

        List<Map<String, Object>> loRanges = (List<Map<String, Object>>) oBlockedRanges;

        // Process each blocked range
        int iRangeIndex = 0;
        for (Map<String, Object> mRange : loRanges) {
            Integer iStart = (Integer) mRange.get("start");
            Integer iEnd = (Integer) mRange.get("end");
            String strImageName = (String) mRange.get("image_name");

            if (iStart == null || iEnd == null) {
                LOGGER.warning("Invalid blocked range at index " + iRangeIndex + ": start or end is null");
                continue;
            }

            // Validate range
            if (iStart < 0 || iEnd >= _abByte.length || iStart > iEnd) {
                throw new RuntimeException("ROM blocked range [" + iStart + ", " + iEnd + "] is invalid for memory size " + _abByte.length);
            }

            int iRangeSize = iEnd - iStart + 1;
            MemoryRange oRange = new MemoryRange(iStart, iEnd);
            _loBlockedRanges.add(oRange);

            // Load ROM image for this specific block if specified
            byte[] abRomImage = null;
            if (strImageName != null && !strImageName.isEmpty()) {
                abRomImage = loadRomImage(strImageName);
            }

            // Fill the range with ROM data or 0x00
            if (abRomImage != null) {
                // Validate image size
                if (abRomImage.length < iStart + iRangeSize) {
                    throw new IllegalArgumentException("ROM image file '" + strImageName + "' is too small. " +
                            "Expected at least " + (iStart + iRangeSize) + " bytes, got " + abRomImage.length + " bytes.");
                }

                // Copy ROM data into memory
                System.arraycopy(abRomImage, iStart, _abByte, iStart, iRangeSize);
                LOGGER.info("Loaded ROM data into range [" + iStart + ", " + iEnd + "] from file: " + strImageName);
            } else {
                // Fill with 0x00 (already initialized in constructor)
                if (strImageName == null || strImageName.isEmpty()) {
                    LOGGER.info("No ROM image specified. Blocked range [" + iStart + ", " + iEnd + "] initialized with 0x00");
                }
            }

            iRangeIndex++;
        }

        LOGGER.info("ROM configuration loaded successfully. Total blocked ranges: " + _loBlockedRanges.size());
    }

    /**
     * Loads a ROM image file from the resources directory.
     *
     * @param strFileName the name of the ROM image file
     * @return the byte array containing the ROM data, or null if the file is not found
     */
    private byte[] loadRomImage(String strFileName) {
        try {
            ClassLoader oClassLoader = Thread.currentThread().getContextClassLoader();
            InputStream oInputStream = oClassLoader.getResourceAsStream(strFileName);

            if (oInputStream == null) {
                LOGGER.warning("ROM image file not found: " + strFileName + ". Will fill blocked areas with 0x00");
                return null;
            }

            // Read the file into a byte array
            byte[] abData = oInputStream.readAllBytes();
            LOGGER.info("Loaded ROM image file: " + strFileName + " (" + abData.length + " bytes)");
            return abData;

        } catch (Exception e) {
            LOGGER.warning("Error loading ROM image file '" + strFileName + "': " + e.getMessage() + ". Will fill blocked areas with 0x00");
            return null;
        }
    }

    /**
     * Reads a single byte from the memory at the specified index.
     *
     * @param iIndex the memory address to read from
     * @return the byte value at the specified address
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public byte peek(int iIndex) {
        if (iIndex < 0 || iIndex >= _abByte.length) {
            throw new IndexOutOfBoundsException("Index " + iIndex + " out of bounds [0, " + (_abByte.length - 1) + "]");
        }
        return _abByte[iIndex];
    }

    /**
     * Writes a byte value to the memory at the specified index.
     *
     * @param iIndex the memory address to write to
     * @param bValue the byte value to write
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public void poke(int iIndex, byte bValue) {
        if (iIndex < 0 || iIndex >= _abByte.length) {
            throw new IndexOutOfBoundsException("Index " + iIndex + " out of bounds [0, " + (_abByte.length - 1) + "]");
        }
        _abByte[iIndex] = bValue;
    }

    /**
     * Checks if the specified memory address falls within a blocked (read-only) range.
     *
     * @param iIndex the memory address to check
     * @return true if the address is protected, false otherwise
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public boolean isROM(int iIndex) {
        if (iIndex < 0 || iIndex >= _abByte.length) {
            throw new IndexOutOfBoundsException("Index " + iIndex + " out of bounds [0, " + (_abByte.length - 1) + "]");
        }
        for (MemoryRange oRange : _loBlockedRanges) {
            if (iIndex >= oRange.iStart && iIndex <= oRange.iEnd) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the list of blocked memory ranges.
     *
     * @return the list of protected memory ranges
     */
    public List<MemoryRange> getBlockedRanges() {
        return _loBlockedRanges;
    }
}