package company.eventprocessor;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Configuration Class This Singleton Class is responsible for loading all of
 * the general configuration options for this application
 *
 * @author jkimmell
 */
public class Configuration {

    /**
     * The instance of Configuration that this Class is storing
     */
    private static Configuration instance = null;

    /**
     * FILENAME is the file location of the configuration JSON file
     *
     * @todo Potentially make this configurable
     */
    public static final String FILENAME = "eventprocessor.properties";

	static {
		System.setProperty("java.util.logging.config.file", "logging.properties");
	}
	private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());

    /**
     * Get the Instance of this class There should only ever be one instance of
     * this class and other classes can use this static method to retrieve the
     * instance
     *
     * @return Configuration the stored Instance of this class
     */
    public static Configuration getInstance() {
        if (Configuration.instance == null) {
            Configuration.instance = new Configuration();
        }

        return Configuration.instance;
    }

    /**
     * The loaded config Properties Object
     */
    private Properties config = null;

    /**
     * Constructor This is private so it cannot be instantiated by anyone other
     * than this class Try and load the current Config, if no config was found,
     * try to create a new one
     */
    private Configuration() {
        boolean result = this.loadConfig(FILENAME);

        if (!result) {
            System.exit(0); //Catastrophic
        }
    }

    public Properties getProperties() {
        return this.config;
    }

    /**
     * Load the Configuration specified at fileName
     *
     * @param fileName
     * @return boolean did this load succeed?
     */
    private boolean loadConfig(String fileName) {

        try {
            InputStream input = Configuration.class.getClassLoader().getResourceAsStream(fileName);
            //InputStream input = getClass().getResourceAsStream(fileName);
            if (input == null) {
                LOGGER.severe("Unable to find " + fileName);
                return false;
            }

            this.config = new Properties();
            this.config.load(input);

        } catch (Exception e) {
            LOGGER.severe("Exception in Configuration");
            return false;
        }

        return true;
    }

}
