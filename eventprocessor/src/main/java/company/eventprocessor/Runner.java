package company.eventprocessor;

import java.util.Properties;
import java.util.logging.Logger;

public class Runner implements Runnable {

	static {
		System.setProperty("java.util.logging.config.file", "logging.properties");
	}
	private static final Logger LOGGER = Logger.getLogger(Runner.class.getName());
	
	@Override
	public void run() {
		Properties props = Configuration.getInstance().getProperties();
		System.out.println("Hello");
        LOGGER.info("START");
        LOGGER.info("Mode=" + props.getProperty("stufe"));
    }
	
	public static void main(String[] args) {
		Thread t = new Thread(new Runner());
		System.out.print("starting");
		t.start();
		t.setName("xyz");
		System.out.print("started");
	}

}
