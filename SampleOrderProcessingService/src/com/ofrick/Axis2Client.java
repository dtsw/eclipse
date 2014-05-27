package com.ofrick;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;

public class Axis2Client
{
    //http://localhost:8080/axis2/services/AxisWebService?wsdl

    public static void main(String[] args)
    {
        RPCServiceClient serviceClient = null;;
        try
        {
            serviceClient = new RPCServiceClient();
        }
        catch (AxisFault e1)
        {
            e1.printStackTrace();
        }

        //if the web service to be accessed is authenticated then set the userName and passWord in the below authenticator object
        HttpTransportProperties.Authenticator authenticator = new HttpTransportProperties.Authenticator();
        authenticator.setUsername("<userName>");
        authenticator.setPassword("<passWord>");

        Options options = serviceClient.getOptions();
        options.setProperty(HTTPConstants.AUTHENTICATE, authenticator);

        // Provide the EndPoint Reference available in the wsdl ,<soap12:address> tag.      
        EndpointReference targetEPR = new EndpointReference(
                "http://localhost:8080/axis2/services/Telalert.TelalertHttpSoap12Endpoint/");

        //provide the action name available in the wsdl, under <wsdl:operation > tag.
        options.setAction("urn:getUniqueCharCount");
        options.setTo(targetEPR);
        QName opName = new QName("http://ofrick.com",
                "setTelalert");

        TelalertEntry entry = new TelalertEntry();
		entry.setEscalationScheme("EAI");
		entry.setMessage("this is a test msg");
		entry.setTts("and this is a test tts");
		entry.setClient("cwp1088");
		entry.setTimeStamp("201405202254");
		entry.setAlertId("1234");
		entry.setSystem("h0566");
		entry.setResourceName("Tomcat Service");
		
        //Object[] opSetArgs = new Object[] { "Blackberry" };
		Object[] opSetArgs = new Object[] { entry };
        Class[] returnTypes = new Class[] { String.class };

        try
        {
            Object[] response = serviceClient.invokeBlocking(opName, opSetArgs,
                    returnTypes);
            System.out.println(response[0]);
        }
        catch (AxisFault e)
        {
            e.printStackTrace();
        }
    }
}
