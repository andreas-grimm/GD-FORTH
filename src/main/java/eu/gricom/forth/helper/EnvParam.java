package eu.gricom.forth.helper;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class EnvParam {
    private static final Logger LOGGER = Logger.getLogger(EnvParam.class.getName());
    private static final String CONFIG_FILE_PATH = "config.yaml";
    private static EnvParam _oInstance;
    private static String _strConfigGroup = "application";
    private final Map<String, Object> _mConfig;
    private final Map<String, Object> _mYamlData;

    private EnvParam() {
        _mYamlData = loadYamlData();
        _mConfig = loadConfig();
    }

    public static synchronized EnvParam getInstance() {
        if (_oInstance == null) {
            _oInstance = new EnvParam();
        }
        return _oInstance;
    }

    public static void setConfigGroup(String strGroup) {
        _strConfigGroup = strGroup;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> loadYamlData() {
        Map<String, Object> mResult = new HashMap<>();
        try {
            ClassLoader oClassLoader = Thread.currentThread().getContextClassLoader();
            InputStream oInputStream = oClassLoader.getResourceAsStream(CONFIG_FILE_PATH);

            if (oInputStream != null) {
                Yaml oYaml = new Yaml();
                Map<String, Object> mYamlData = oYaml.load(oInputStream);
                if (mYamlData != null) {
                    mResult.putAll(mYamlData);
                }
            } else {
                LOGGER.warning("Config file not found: " + CONFIG_FILE_PATH);
            }
        } catch (Exception e) {
            LOGGER.warning("Failed to load YAML data: " + e.getMessage());
        }
        return mResult;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> loadConfig() {
        Map<String, Object> mResult = new HashMap<>();
        try {
            ClassLoader oClassLoader = Thread.currentThread().getContextClassLoader();
            InputStream oInputStream = oClassLoader.getResourceAsStream(CONFIG_FILE_PATH);

            if (oInputStream != null) {
                Yaml oYaml = new Yaml();
                Map<String, Object> mYamlData = oYaml.load(oInputStream);
                if (mYamlData != null && mYamlData.containsKey(_strConfigGroup)) {
                    Object oEnvData = mYamlData.get(_strConfigGroup);
                    if (oEnvData instanceof Map) {
                        mResult.putAll((Map<String, Object>) oEnvData);
                    }
                }
            } else {
                LOGGER.warning("Config file not found: " + CONFIG_FILE_PATH);
            }
        } catch (Exception e) {
            LOGGER.warning("Failed to load config file: " + e.getMessage());
        }
        return mResult;
    }

    private String getValueOrEnv(String strKey) {
        String strEnvValue = System.getenv(strKey);
        if (strEnvValue != null && !strEnvValue.trim().isEmpty()) {
            return strEnvValue;
        }

        Object oConfigValue = _mConfig.get(strKey);
        if (oConfigValue != null) {
            return oConfigValue.toString();
        }

        return null;
    }

    public static String getString(String strKey) {
        String strValue = getInstance().getValueOrEnv(strKey);
        if (strValue == null) {
            LOGGER.warning("Configuration key not found: " + strKey);
            return "";
        }
        return strValue;
    }

    public static int getInt(String strKey) {
        String strValue = getInstance().getValueOrEnv(strKey);
        if (strValue == null) {
            LOGGER.warning("Configuration key not found: " + strKey);
            return 0;
        }
        try {
            return Integer.parseInt(strValue);
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid integer value for key '" + strKey + "': " + strValue);
            return 0;
        }
    }

    public static float getFloat(String strKey) {
        String strValue = getInstance().getValueOrEnv(strKey);
        if (strValue == null) {
            LOGGER.warning("Configuration key not found: " + strKey);
            return 0.0f;
        }
        try {
            return Float.parseFloat(strValue);
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid float value for key '" + strKey + "': " + strValue);
            return 0.0f;
        }
    }

    public static boolean getBoolean(String strKey) {
        String strValue = getInstance().getValueOrEnv(strKey);
        if (strValue == null) {
            LOGGER.warning("Configuration key not found: " + strKey);
            return false;
        }
        return Boolean.parseBoolean(strValue);
    }

    public static int getMaxBcdDigits() {
        return getInt("max_bcd_digits");
    }

    @Deprecated(since = "1.0", forRemoval = true)
    public static int getMAX_BCD_DIGITS() {
        return getMaxBcdDigits();
    }

    /**
     * Returns the real type variable length (precision in digits).
     * Used by RealValue class for floating-point precision.
     *
     * @return the maximum number of significant digits for real numbers
     */
    public static int getRealTypeVariableLength() {
        return getInt("realTypeVariableLength");
    }

    /**
     * Returns the application name.
     * Used by Forth class for display and help messages.
     *
     * @return the application name
     */
    public static String getAppName() {
        return getString("app_name");
    }

    /**
     * Returns the application version.
     * Used by Forth class for version display.
     *
     * @return the semantic version string
     */
    public static String getVersion() {
        return getString("version");
    }

    /**
     * Returns the logging level.
     * Used by Forth class for logger configuration.
     *
     * @return the log level string (severe, warning, info, fine, finer, finest)
     */
    public static String getLogLevel() {
        return getString("log_level");
    }

    /**
     * Returns the debug mode flag.
     * Can be used for enabling diagnostic output.
     *
     * @return true if debug mode is enabled, false otherwise
     */
    public static boolean isDebugMode() {
        setConfigGroup("testing");
        boolean bResult = getBoolean("debug_mode");
        setConfigGroup("application");
        return bResult;
    }

    /**
     * Returns the memory size in bytes.
     * Used by Memory class to initialize the byte array size.
     *
     * @return the memory size in bytes
     */
    public static int getMemorySize() {
        setConfigGroup("memory");
        int iResult = getInt("size");
        setConfigGroup("application");
        return iResult;
    }

    /**
     * Returns the ROM configuration for a specific platform.
     * Used by Memory class to load ROM areas and image files.
     *
     * @param strPlatformName the name of the ROM platform (e.g., "standard_16bit", "embedded", "extended")
     * @return a Map containing platform configuration (name, blocked_ranges, image_name), or null if not found
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getRomConfiguration(String strPlatformName) {
        EnvParam oInstance = getInstance();
        if (oInstance._mYamlData == null || !oInstance._mYamlData.containsKey("roms")) {
            LOGGER.warning("ROM configuration not found in config.yaml");
            return null;
        }

        Object oRoms = oInstance._mYamlData.get("roms");
        if (!(oRoms instanceof Map)) {
            LOGGER.warning("Invalid ROM configuration structure");
            return null;
        }

        Map<String, Object> mRoms = (Map<String, Object>) oRoms;
        if (!mRoms.containsKey(strPlatformName)) {
            LOGGER.warning("ROM platform not found: " + strPlatformName);
            return null;
        }

        Object oPlatform = mRoms.get(strPlatformName);
        if (oPlatform instanceof Map) {
            return (Map<String, Object>) oPlatform;
        }

        LOGGER.warning("Invalid platform configuration for: " + strPlatformName);
        return null;
    }

    /**
     * Returns the list of available ROM platforms.
     *
     * @return a list of platform names, or null if ROM configuration is not available
     */
    @SuppressWarnings("unchecked")
    public static java.util.List<String> getAvailableRomPlatforms() {
        EnvParam oInstance = getInstance();
        if (oInstance._mYamlData == null || !oInstance._mYamlData.containsKey("roms")) {
            return null;
        }

        Object oRoms = oInstance._mYamlData.get("roms");
        if (!(oRoms instanceof Map)) {
            return null;
        }

        Map<String, Object> mRoms = (Map<String, Object>) oRoms;
        return new java.util.ArrayList<>(mRoms.keySet());
    }
}
