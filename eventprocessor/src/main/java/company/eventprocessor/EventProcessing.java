package company.eventprocessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.datastore.DataStoreCache;

import com.spaceprogram.kittycache.KittyCache;

public class EventProcessing {

	static PersistenceManagerFactory pmf;
	static EventQConsumer eventQConsumer = null;
	
	static {
		System.setProperty("java.util.logging.config.file", "logging.properties");
	}
	private static final Logger LOGGER = Logger.getLogger(EventProcessing.class.getName());

	public static void thread(Runnable runnable, boolean daemon, String name) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.setName(name);
        brokerThread.start();
    }

    public static void main(String[] args) {
    	
    	try {
    		
    		KittyCache<String, Object> bucket = new KittyCache<String, Object>(100);
    		bucket.put("123", "Hello World", 1000);
    		
        	pmf = JDOHelper.getPersistenceManagerFactory("events");
        	
    		eventQConsumer = new EventQConsumer(pmf);
    		thread(eventQConsumer, false, "eventConsumer");

    		// create Thread for Shutdown
            Thread monitor = new MonitorThread();
            // this will call run()
            monitor.start();
            LOGGER.info("Main thread has started monitor thread");
    	} catch (Exception e) {
    		e.printStackTrace();
    		pmf.close();
		}
	
//		try {
//			Thread.sleep(30*1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		eventQConsumer.doStop();
//		
		
	}
    
    private static class MonitorThread extends Thread {

        private ServerSocket socket;

        public MonitorThread() {
            setDaemon(true);
            setName("StopMonitor");
            try {
                socket = new ServerSocket(Integer.parseInt(Configuration.getInstance().getProperties().getProperty("shutdown_port")), 1, InetAddress.getByName("127.0.0.1"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void doCommand(Socket socket) {
            LOGGER.info("doCommand");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String inputLine;
                inputLine = reader.readLine();
                if (inputLine.equalsIgnoreCase("flushCache")) {
//                    pm.evictAll();
                    DataStoreCache c = pmf.getDataStoreCache();
                    c.evictAll();
                    LOGGER.info("Objects evicted from pm and pmf Cache");
                    socket.close();
                } else if (inputLine.equalsIgnoreCase("quit")) {
                    LOGGER.info("STOP");

                    // ask the Consumer Threads to stop
                    eventQConsumer.doStop();

                    socket.close();
//                    if (pm.currentTransaction().isActive()) {
//                        pm.currentTransaction().rollback();
//                    }
//                    pm.close();
                    pmf.close();
                    //pm.getPersistenceManagerFactory().close();
                    LOGGER.info("Closed pm and pmf");
                    
                    try {
                    	Thread.sleep(10*1000);
                    } catch (Exception e) {
						e.printStackTrace();
					}
                    System.exit(0);
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            boolean running = true;
            try {
                while ( running ) {
                    doCommand(socket.accept());
                }
            } catch (Exception e) {
                if ( !socket.isClosed() ) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                    }
                }
                throw new RuntimeException(e);
            }
        }

    }
}
