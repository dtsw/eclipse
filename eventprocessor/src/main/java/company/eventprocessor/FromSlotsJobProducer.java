package company.eventprocessor;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Message;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author Oliver Frick
 */
public class FromSlotsJobProducer {
    
	static {
		System.setProperty("java.util.logging.config.file", "logging.properties");
	}
	private static final Logger LOGGER = Logger.getLogger(FromSlotsJobProducer.class.getName());
    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private Properties props;

    public void before() throws Exception {
        props = Configuration.getInstance().getProperties();
        connectionFactory = new ActiveMQConnectionFactory( props.getProperty("producer_uri") );
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination = session.createQueue( props.getProperty("event_queue") );
    }

    public void after() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    public void run() throws Exception {
        MessageProducer producer = session.createProducer(destination);

        Message message = session.createMessage();
        Map<String, String> parsedArgs = parseEnvVars();
        
        if ( populateWithAttributes(message, parsedArgs ) ) {
            message.setIntProperty("JobID", 1);
            int expiry = Integer.parseInt(props.getProperty("message_ttl_seconds")) * 1000;
            producer.setTimeToLive( expiry );
            producer.send(message);
            LOGGER.info("FromSlotsJobProducer sent Job");
        }
        
        producer.close();
    }

    public static void main(String[] args) {
        FromSlotsJobProducer producer = new FromSlotsJobProducer();
        LOGGER.info("Starting FromSlotsJobProducer now...");
        try {
            producer.before();
            producer.run();
            producer.after();
        } catch (Exception e) {
            LOGGER.severe("Caught an exception inside before/run/after: " + e.getMessage());
            e.printStackTrace();
        }
        LOGGER.info("Finished running the FromSlotsJobProducer.");
    }

    private static Map<String, String> parseEnvVars() {
        Map<String, String> env = System.getenv();
        return env;
    }

    private static boolean populateWithAttributes(Message msg, Map<String, String> env ) {
    	boolean result = true;
    	try {
	    	String slots = env.get("SLOTS");
	    	String[] slotsArray = slots.split(":");
	    	Set<String> mySet = new HashSet<>(Arrays.asList(slotsArray));
	    	Set<String> wantFields = new HashSet<>(Arrays.asList(DataMapping.getMappedAttributes()));
	    	mySet.retainAll(wantFields);
	    	// we also need the Event Class, it is not in SLOTS list
	    	mySet.add("CLASS");

	    	for (String slotName : mySet) {
	            try {
	                msg.setStringProperty(slotName, env.get(slotName));
	            } catch (Exception e) {
	                LOGGER.fine(e.getMessage());
	            }
	        }
    	} catch (Exception e) {
    		result = false;
		}
    	return result;
    }
}
