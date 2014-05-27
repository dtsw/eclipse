package com.ofrick;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.*;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMText;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.transport.http.HTTPConstants;

import sample.servicelifecycle.LibraryConstants;
import sample.servicelifecycle.bean.BookList;
import sample.servicelifecycle.bean.UserList;

public class SampleTelalertService {

	public static final String OMnamespace = "http://h0345.swi.srse.net/SOAPListener/I3SOAPISAPIU.DLL#alarm";

	private TelalertList telalertList;
	
	OMFactory factory = OMAbstractFactory.getOMFactory();
	OMNamespace OMNamespaceObj = factory.createOMNamespace(OMnamespace, "ns");	
	
	/*<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" 
	 * xmlns:telalert="http://ofrick.com">
	 * <SOAP-ENV:Header/><SOAP-ENV:Body>
	 * <telalert:telalert>
	 * <telalert:escalation>EAI</telalert:escalation>
	 * <telalert:message>Test,blaaa</telalert:message>
	 * <telalert:tts>Test,blaaa</telalert:tts>
	 * <telalert:client>fred</telalert:client>
	 * <telalert:timestamp>201405161307</telalert:timestamp>
	 * <telalert:alert_id>12345</telalert:alert_id>
	 * <telalert:system>h0345</telalert:system>
	 * <telalert:resource>Unknown</telalert:resource>
	 * </telalert:telalert>
	 * </SOAP-ENV:Body></SOAP-ENV:Envelope>*/

	public OMElement AddTelalert(OMElement element) throws XMLStreamException{
		element.build();
		element.detach();

		OMElement escalation = element.getFirstChildWithName(new QName(OMnamespace, "escalation"));
		OMElement message = element.getFirstChildWithName(new QName(OMnamespace, "message"));
		OMElement tts = element.getFirstChildWithName(new QName(OMnamespace, "tts"));
		OMElement client = element.getFirstChildWithName(new QName(OMnamespace, "client"));
		OMElement timestamp = element.getFirstChildWithName(new QName(OMnamespace, "timestamp"));
		OMElement alertId = element.getFirstChildWithName(new QName(OMnamespace, "alert_id"));
		OMElement system = element.getFirstChildWithName(new QName(OMnamespace, "system"));
		OMElement resource = element.getFirstChildWithName(new QName(OMnamespace, "resource"));
		
		ConcurrentLinkedQueue<TelalertEntry> list2 = (ConcurrentLinkedQueue<TelalertEntry>) getServletContext().getAttribute("jobQueue");	

			if ( list2 == null ) {
					list2 = new ConcurrentLinkedQueue<TelalertEntry>();
					getServletContext().setAttribute("jobQueue", list2);
				}

		TelalertEntry entry = new TelalertEntry();
		entry.setEscalationScheme(escalation.getText());
		entry.setMessage(message.getText());
		entry.setTts(tts.getText());
		entry.setClient(client.getText());
		entry.setTimeStamp(timestamp.getText());
		entry.setAlertId(alertId.getText());
		entry.setSystem(system.getText());
		entry.setResourceName(resource.getText());
		
		//entry.setTelalert(element);
		list2.add(entry);

		System.out.println("Telalert Added. [Escalation=" + entry.getEscalationScheme() +"]");
		
		OMElement root = factory.createOMElement("OrderQueryResult", OMNamespaceObj);
		return root;
	}

	public OMElement GetTelalertByEscalation(OMElement element) throws XMLStreamException{
		element.build();
		element.detach();

		System.out.println("Telalert..." + element.toString());

		OMElement escalationParam = element.getFirstChildWithName(new QName(OMnamespace, "escalation"));

		String escalationString = new String( escalationParam.getText() );
		System.out.println( escalationString );

		ConcurrentLinkedQueue<TelalertEntry> list2 = (ConcurrentLinkedQueue<TelalertEntry>) getServletContext().getAttribute("jobQueue");
		
		if ( list2 != null && escalationString != "" ) {
			
			System.out.println("List size: " + list2.size() );
			
			Iterator<TelalertEntry> itr= list2.iterator();
			TelalertEntry entry = new TelalertEntry(); 

			while(itr.hasNext()){
					entry = itr.next();
					System.out.println(entry);

				if ( entry.getEscalationScheme().equals( escalationString )) {
					list2.remove(entry);
					OMElement root = factory.createOMElement("OrderQueryResult", OMNamespaceObj);
					//OMElement telalert = entry.getTelalert();
					//root.addChild(telalert);
					System.out.println("Telalert Retrieved. [Escalation=" + entry.getEscalationScheme() +"]");
					System.out.println("[message=" + entry.getMessage() +"]");
					System.out.println("[tts=" + entry.getTts() +"]");
					System.out.println("[client=" + entry.getClient() +"]");
					System.out.println("[timestamp=" + entry.getTimeStamp() +"]");
					System.out.println("[alert_id=" + entry.getAlertId() +"]");
					System.out.println("[system=" + entry.getSystem() +"]");
					System.out.println("[resource=" + entry.getResourceName() +"]");

					/*<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" 
					 * xmlns:telalert="http://ofrick.com">
					 * <SOAP-ENV:Header/><SOAP-ENV:Body>
					 * <telalert:telalert>
					 * <telalert:escalation>EAI</telalert:escalation>
					 * <telalert:message>Test,blaaa</telalert:message>
					 * <telalert:tts>Test,blaaa</telalert:tts>
					 * <telalert:client>fred</telalert:client>
					 * <telalert:timestamp>201405161307</telalert:timestamp>
					 * <telalert:alert_id>12345</telalert:alert_id>
					 * <telalert:system>h0345</telalert:system>
					 * <telalert:resource>Unknown</telalert:resource>
					 * </telalert:telalert>
					 * </SOAP-ENV:Body></SOAP-ENV:Envelope>*/
					
					return root;
				}
				else {
					System.out.println( entry.getEscalationScheme() +" was not: "+ escalationString );
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

	/**
     * Session related methods
     */
    public void init(ServiceContext serviceContext) {
        AxisService service = serviceContext.getAxisService();
        this.telalertList = (TelalertList) service.getParameterValue("jobQueue");
        /*this.availableBookList = (BookList) service.getParameterValue(LibraryConstants.AVAILABLE_BOOK);
        this.availableBookList.setListName(LibraryConstants.AVAILABLE_BOOK);
        this.allBookList = (BookList) service.getParameterValue(LibraryConstants.ALL_BOOK);
        this.lendBookList = (BookList) service.getParameterValue(LibraryConstants.LEND_BOOK);
        this.userList = (UserList) service.getParameterValue(LibraryConstants.USER_LIST);*/
        if (this.telalertList == null) {
        	this.telalertList = new TelalertList();
        	try {
        		service.addParameter("jobQueue", this.telalertList);
        	} catch (Exception e) {
        		//
        		System.out.println("could not store jobQueue: " + e.getMessage() );
        	}
        }
        System.out.println("init list size:" + this.telalertList.size() );
    }

    public void destroy(ServiceContext serviceContext) throws AxisFault {
        AxisService service = serviceContext.getAxisService();
        /*service.addParameter(new Parameter(LibraryConstants.AVAILABLE_BOOK, availableBookList));
        service.addParameter(new Parameter(LibraryConstants.ALL_BOOK, allBookList));
        service.addParameter(new Parameter(LibraryConstants.LEND_BOOK, lendBookList));
        service.addParameter(new Parameter(LibraryConstants.USER_LIST, userList));*/
        System.out.println("destroy");
    }

}