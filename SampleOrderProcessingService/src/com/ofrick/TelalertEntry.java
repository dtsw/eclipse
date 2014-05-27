package com.ofrick;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;

public class TelalertEntry {
	/**
	 * 
	 */
	//private static final long serialVersionUID = 81391465851536942L;
	private String escalationScheme;
	private String alertDefName;
	private String resourceName;
	private String alertId;
	private String timeStamp;
	private String message;
	private String tts;
	private String client;
	private String system;
	
	public String getEscalationScheme() {
		return escalationScheme;
	}
	public void setEscalationScheme(String escalationScheme) {
		this.escalationScheme = escalationScheme;
	}
	public String getAlertDefName() {
		return alertDefName;
	}
	public void setAlertDefName(String alertDefName) {
		this.alertDefName = alertDefName;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getAlertId() {
		return alertId;
	}
	public void setAlertId(String alertId) {
		this.alertId = alertId;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTts() {
		return tts;
	}
	public void setTts(String tts) {
		this.tts = tts;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	
    public OMElement serialize(OMFactory fac) {
        return null;
    }
    
}
