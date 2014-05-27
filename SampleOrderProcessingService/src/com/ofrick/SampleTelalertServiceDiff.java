package com.ofrick;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.*;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMText;
import org.apache.axis2.transport.http.HTTPConstants;

public class SampleTelalertServiceDiff {

	private HashMap<String, Double> orders = new HashMap<String, Double>(); 

	public static final String OMnamespace = "http://h0345.swi.srse.net/SOAPListener/I3SOAPISAPIU.DLL#alarm";

	OMFactory factory = OMAbstractFactory.getOMFactory();
	OMNamespace OMNamespaceObj = factory.createOMNamespace(OMnamespace, "ns");

	public void AddOrder(OMElement element) throws XMLStreamException{
		element.build();
		element.detach();

		OMElement symbol = element.getFirstChildWithName(new QName(OMnamespace, "symbol"));
		OMElement price = element.getFirstChildWithName(new QName(OMnamespace, "price"));

		orders.put(symbol.getText(), new Double(price.getText()));

		System.out.println("Order Added. [Symbol=" + symbol.getText() 
				+ ", Price=" + price.getText() +"]");
	}

	
	/*<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:telalert="http://ofrick.com"><SOAP-ENV:Header/><SOAP-ENV:Body><telalert:telalert><telalert:escalation>EAI</telalert:escalation><telalert:message>Test,blaaa</telalert:message><telalert:tts>Test,blaaa</telalert:tts><telalert:client>fred</telalert:client><telalert:timestamp>201405161307</telalert:timestamp><telalert:alert_id>12345</telalert:alert_id><telalert:system>h0345</telalert:system><telalert:resource>Unknown</telalert:resource></telalert:telalert></SOAP-ENV:Body></SOAP-ENV:Envelope>*/

	public OMElement AddTelalert(OMElement element) throws XMLStreamException{
		element.build();
		element.detach();

		OMElement escalation = element.getFirstChildWithName(new QName(OMnamespace, "escalation"));
		
		//ConcurrentLinkedQueue<TelalertEntry> list2 = (ConcurrentLinkedQueue) getServletContext().getAttribute("jobQueue");	
		//List<TelalertEntry> list = new ArrayList<TelalertEntry>();
		//Collection<TelalertEntry> syncList = Collections.synchronizedList(list);
		List<TelalertEntryWithOMElement> list = (ArrayList<TelalertEntryWithOMElement>) getServletContext().getAttribute("jobQueue");
		// create Job Queue, if none exists
			if ( list == null ) {
					list = new ArrayList<TelalertEntryWithOMElement>();
					getServletContext().setAttribute("jobQueue", list);
				}
		
		Collection<TelalertEntryWithOMElement> list2 = Collections.synchronizedList(list);
		
		// create Job Queue, if none exists
		//if ( list2 == null ) {
		//	list2 = new ConcurrentLinkedQueue<TelalertEntry>();
		//	getServletContext().setAttribute("jobQueue", list2);
		//}
		TelalertEntryWithOMElement entry = new TelalertEntryWithOMElement();
		entry.setEscalation(escalation.getText());
		entry.setTelalert(element);
		list2.add(entry);

		System.out.println("Telalert Added. [Escalation=" + entry.getEscalation() +"]");
		
		OMElement root = factory.createOMElement("OrderQueryResult", OMNamespaceObj);
		return root;
	}

	public OMElement GetTelalertByEscalation(OMElement element) throws XMLStreamException{
		element.build();
		element.detach();

		System.out.println("Telalert..." + element.getNamespaceURI());

		OMElement escalationParam = element.getFirstChildWithName(new QName(OMnamespace, "escalation"));

		String escalationString = new String("");
		escalationString.concat(escalationParam.getText());
		System.out.println( escalationString );

		//ConcurrentLinkedQueue<TelalertEntry> list2 = (ConcurrentLinkedQueue) getServletContext().getAttribute("jobQueue");
		List<TelalertEntryWithOMElement> list = (ArrayList<TelalertEntryWithOMElement>) getServletContext().getAttribute("jobQueue");
		Collection<TelalertEntryWithOMElement> list2 = Collections.synchronizedList(list);
		
		synchronized (list2) {
		      Iterator i = list2.iterator(); // Must be in the synchronized block
		      TelalertEntryWithOMElement tel = new TelalertEntryWithOMElement();
			while (i.hasNext())
		    	 tel = (TelalertEntryWithOMElement) i.next();
		         System.out.println(tel);
		  }
		
		if ( list2 != null && escalationString != "" ) {
			
			System.out.println("List size: " + list2.size() );
			
			Iterator<TelalertEntryWithOMElement> itr= list2.iterator();
			while(itr.hasNext()){
				System.out.println( itr.next().getClass().getName() );
				TelalertEntryWithOMElement entry = (TelalertEntryWithOMElement) itr.next();
				if ( entry.getEscalation().equals("EAI")) {
					list2.remove(entry);
					OMElement root = factory.createOMElement("OrderQueryResult", OMNamespaceObj);
					OMElement telalert = entry.getTelalert();
					root.addChild(telalert);
					System.out.println("Telalert Retrived. [Escalation=" + entry.getEscalation() +"]");

					return root;
				}
				else {
					System.out.println("was not EAI: " + entry.getEscalation() );
				}
			}
		}
		return null; 
	}
	
	private ServletContext getServletContext()
	{
		org.apache.axis2.context.MessageContext mc = org.apache.axis2.context.MessageContext.getCurrentMessageContext(); 
		return (ServletContext) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETCONTEXT);
	}


}