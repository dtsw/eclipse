package company.eventprocessor;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.*;

/**
 * @author Oliver Frick
 *
 */
public class MessagePropertiesHelper {

	/**
	 * returns a HashMap for the JMSMessage properties 
	 */
	public static HashMap<String, Object> getMessageProperties(Message msg) throws JMSException 
	{
	   HashMap<String, Object> properties = new HashMap<String, Object> ();
	   Enumeration<?> srcProperties = msg.getPropertyNames();
	   while (srcProperties.hasMoreElements()) {
	       String propertyName = (String)srcProperties.nextElement ();
	       properties.put(propertyName, msg.getObjectProperty (propertyName));
	   }
	   return properties;
	}
	
	
	/**
	 * sets JMSMessage properties from a HashMap
	 */
	public static void setMessageProperties(Message msg, HashMap<String, Object> properties) throws JMSException {
	    if (properties == null) {
	        return;
	    }
	    for (Map.Entry<String, Object> entry : properties.entrySet()) {
	        String propertyName = entry.getKey ();
	        Object value = entry.getValue ();
	        msg.setObjectProperty(propertyName, value);
	    }
	}

}
