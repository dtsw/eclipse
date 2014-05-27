package com.ofrick;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.ServletContext;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.ServiceLifeCycle;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.description.Parameter;

public class TelalertInit implements ServiceLifeCycle {

	public void startUp( ConfigurationContext ignore, AxisService service ) {
		System.out.println("Telalert backend is starting.");

		//Parameter test = service.getParameter("jobQueue");
		//test.getParameterElement();
		
	      //ConcurrentLinkedQueue<TelalertEntry> jobQueue = new ConcurrentLinkedQueue<TelalertEntry>();
	      //getServletContext().setAttribute("jobQueue", jobQueue);
	      
	}

	@Override
	public void shutDown(ConfigurationContext arg0, AxisService arg1) {
		// TODO Auto-generated method stub
		System.out.println("Telalert backend is stopping.");
	      //getServletContext().removeAttribute("jobQueue");

	}
	
	private ServletContext getServletContext()
	{
		org.apache.axis2.context.MessageContext mc = org.apache.axis2.context.MessageContext.getCurrentMessageContext(); 
		return (ServletContext) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETCONTEXT);
	}
}
