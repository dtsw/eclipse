package com.ofrick;

import javax.xml.soap.*;

public class SOAPClientGet {

	public static void main(String args[]) throws Exception {


		//String debugArgs = "EAI___Test___blaaa___12345___201405161307___This is a message";
		//split command line arguments
		//String[] params = args[0].split("___");

		// Create SOAP Connection
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		// Send SOAP Message to SOAP Server
		//String url = "http://h0345.swi.srse.net/SOAPListener/I3SOAPISAPIU.DLL/#alarm";
		String url = "http://localhost:8080/axis2/services/Telalert/getTelalertByEscalation";
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

		//String serverURI = "http://h0345.swi.srse.net/SOAPListener/I3SOAPISAPIU.DLL#alarm";
		String serverURI = "http://ofrick.com";

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration("tam", serverURI);


		// SOAP Body
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapGetTelalert = soapBody.addChildElement("telalert", "tam");
		SOAPElement soapBodyElem = soapGetTelalert.addChildElement("escalation", "tam");
		soapBodyElem.addTextNode("TEST");

		MimeHeaders headers = soapMessage.getMimeHeaders();
		//headers.addHeader("SOAPAction", serverURI);
		headers.addHeader("SOAPAction", "http://localhost:8080/axis2/services/Telalert/getTelalertByEscalation");
		soapMessage.saveChanges();

		/* Print the request message */
		System.out.print("Request SOAP Message:");
		soapMessage.writeTo(System.out);
		System.out.println();

		return soapMessage;
	}

}

