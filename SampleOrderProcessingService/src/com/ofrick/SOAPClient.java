package com.ofrick;

import javax.xml.soap.*;

public class SOAPClient {


	private static String escalationScheme;
	private static String alertDefName;
	private static String resourceName;
	private static String alertId;
	private static String timeStamp;
	private static String message;

	public static void main(String args[]) throws Exception {


		String debugArgs = "TEST___Test___blaaa___12345___201405161307___This is a message";
		// split command line arguments
		//String[] params = args[0].split("___");
		String[] params = debugArgs.split("___");
		escalationScheme = params[0];
		alertDefName = params[1];
		resourceName = params[2];
		alertId = params[3];
		timeStamp = params[4];
		message = alertDefName + "," + resourceName;

		// Create SOAP Connection
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		// Send SOAP Message to SOAP Server
		//String url = "http://h0345.swi.srse.net/SOAPListener/I3SOAPISAPIU.DLL/#alarm";
		String url = "http://localhost:8080/axis2/services/Telalert/setTelalert";
		SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(), url);

		// print SOAP Response
		System.out.print("Response SOAP Message:");
		soapResponse.writeTo(System.out);

		soapConnection.close();


	}
	private static SOAPMessage createSOAPRequest() throws Exception {
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();

		//String targetNamespace = "http://h0345.swi.srse.net/SOAPListener/I3SOAPISAPIU.DLL#alarm";
		//String serverURI = "http://h0345.swi.srse.net/SOAPListener/I3SOAPISAPIU.DLL#alarm";
		String targetNamespace = "http://ofrick.com";
		String serverURI = "http://ofrick.com";

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration("tam", targetNamespace);


		// SOAP Body
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapGetTelalertRequest = soapBody.addChildElement("setTelalertRequest", "tam");
		SOAPElement soapGetTelalert = soapGetTelalertRequest.addChildElement("telalert", "tam");
		SOAPElement soapBodyElem8 = soapGetTelalert.addChildElement("alertDefName", "tam");
		soapBodyElem8.addTextNode(alertDefName);
		SOAPElement soapBodyElem5 = soapGetTelalert.addChildElement("alertId", "tam");
		soapBodyElem5.addTextNode(alertId);
		SOAPElement soapBodyElem3 = soapGetTelalert.addChildElement("client", "tam");
		soapBodyElem3.addTextNode("fred");
		SOAPElement soapBodyElem = soapGetTelalert.addChildElement("escalationScheme", "tam");
		soapBodyElem.addTextNode(escalationScheme);
		SOAPElement soapBodyElem1 = soapGetTelalert.addChildElement("message", "tam");
		soapBodyElem1.addTextNode(message);
		SOAPElement soapBodyElem7 = soapGetTelalert.addChildElement("resourceName", "tam");
		soapBodyElem7.addTextNode("Unknown");
		SOAPElement soapBodyElem6 = soapGetTelalert.addChildElement("system", "tam");
		soapBodyElem6.addTextNode("h0345");
		SOAPElement soapBodyElem4 = soapGetTelalert.addChildElement("timeStamp", "tam");
		soapBodyElem4.addTextNode(timeStamp);
		SOAPElement soapBodyElem2 = soapGetTelalert.addChildElement("tts", "tam");
		soapBodyElem2.addTextNode(message);


		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", serverURI);
		
		soapMessage.saveChanges();

		/* Print the request message */
		System.out.print("Request SOAP Message:");
		soapMessage.writeTo(System.out);
		System.out.println();

		return soapMessage;
	}

}

