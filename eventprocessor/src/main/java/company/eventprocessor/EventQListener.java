package company.eventprocessor;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.spaceprogram.kittycache.KittyCache;

import company.eventprocessor.entity.Event;

public class EventQListener implements MessageListener {

    private PersistenceManagerFactory pmf;
    protected long counter = 0L;
    
    static {
		System.setProperty("java.util.logging.config.file", "logging.properties");
	}
    private static final Logger LOGGER = Logger.getLogger(EventQListener.class.getName());

    protected KittyCache<String, Object> cache = null;
    protected KittyCache<String, Object> bucket = null;
    protected int cache_ttl_seconds = 900;
    protected int cache_size = 1000;
    protected synchronized Object cache_get(String key) {
        return cache.get(key);
    }
    protected synchronized Object bucket_get(String key) {
        return bucket.get(key);
    }
    protected String getCacheKey(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        return new BigInteger(1, bytes).toString(36);
    }

    public static void main(String[] args) {
        LOGGER.info(EventQListener.class.getName());
    }

    public EventQListener() {
        configureFromProperies();
    }
    
//    private NotificationProducer notificationSMSProducer = new NotificationProducer("sms_queue");
//    private NotificationProducer notificationEmailProducer = new NotificationProducer("mail_queue");
//    private NotificationProducer notificationFixnetProducer = new NotificationProducer("fixnet_queue");

    public EventQListener(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
        configureFromProperies();
    }
    
    private void configureFromProperies() {
        Properties props = Configuration.getInstance().getProperties();
        cache_ttl_seconds = Integer.parseInt(props.getProperty("cache_ttl_seconds"));
        cache_size = Integer.parseInt(props.getProperty("cache_size"));
        cache = new KittyCache<String, Object>(this.cache_size);
        bucket = new KittyCache<String, Object>(1000);
    }

    @Override
    public void onMessage(Message message) {
    	PersistenceManager pm = pmf.getPersistenceManager();
        
        try {
            counter++;
        	System.out.println(String.format("EVENT: %s", message.getStringProperty("CLASS")));
        	System.out.println(String.format("severity: %s", message.getStringProperty("severity")));
        	System.out.println(String.format("status: %s", message.getStringProperty("status")));
        	System.out.println(String.format("msg: %s", message.getStringProperty("msg")));
        	System.out.println(String.format("mc_host: %s", message.getStringProperty("mc_host")));
        	System.out.println(String.format("mc_host_address: %s", message.getStringProperty("mc_host_address")));
        	System.out.println(String.format("mc_object: %s", message.getStringProperty("mc_object")));
        	System.out.println(String.format("date_reception: %s", message.getStringProperty("date_reception")));
        	System.out.println(String.format("mc_ueid: %s", message.getStringProperty("mc_ueid")));
        	
        	long num = Long.parseLong(message.getStringProperty("date_reception"));
			Date date = new Date(num*1000);
	        DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
	        String formatted = format.format(date);
	        System.out.println(formatted);
	        format.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
	        formatted = format.format(date);
	        System.out.println(formatted);
			
	        pm.currentTransaction().begin();
	        Event event = new Event(message);
	        pm.makePersistent(event);
	        pm.currentTransaction().commit();
	        
			System.out.println("----------------------------------------------------------------------------");
            int jobId = message.getIntProperty("JobID");
            LOGGER.info("EventQListener processing job: " + jobId);

        } catch (Exception e) {
            LOGGER.severe("EventQListener caught an Exception");
            LOGGER.fine(e.getMessage());
        } finally {
            if (pm.currentTransaction().isActive()) {
            	LOGGER.fine("Rolling back transaction");
                pm.currentTransaction().rollback();
            }
            LOGGER.fine("closing pm");
            pm.close();
            //pm.getPersistenceManagerFactory().close();
        }
    }

	public void doStop() {
		LOGGER.info(this.getClass().getName() + " doStop");
		System.out.println( String.format("Processed %s events", counter) );
//		notificationEmailProducer.doStop();
//		notificationSMSProducer.doStop();
//		notificationFixnetProducer.doStop();
	}

}
