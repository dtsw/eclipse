package company.eventprocessor;

import java.util.Properties;
import javax.jdo.PersistenceManagerFactory;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class EventQConsumer implements Runnable, ExceptionListener {

	private boolean doStop = false;

	public synchronized void doStop() {
		this.doStop = true;
	}

	private synchronized boolean shouldStop() {
		return this.doStop == true;
	}

	private PersistenceManagerFactory pmf;

	public EventQConsumer(PersistenceManagerFactory pmf) {
		this.pmf = pmf;
	}

	public void run() {
		try {

			Properties props = Configuration.getInstance().getProperties();

			// Create a ConnectionFactory
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(props.getProperty("consumer_uri"));

			// Create a Connection
			Connection connection = connectionFactory.createConnection();
			connection.start();

			// Create a Session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// Create the destination (Topic or Queue)
			Destination destination = session.createQueue(props.getProperty("event_queue"));

			// Create a consumer
			MessageConsumer consumer = session.createConsumer(destination);

			// Create and set the Job Queue Listener
			EventQListener listener = new EventQListener(pmf);
			consumer.setMessageListener(listener);

			while (!shouldStop()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			
			// ask Job Queue Listener to stop
			listener.doStop();
			
			// Do Cleanup
			consumer.close();
			session.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onException(JMSException exception) {
	}

}
