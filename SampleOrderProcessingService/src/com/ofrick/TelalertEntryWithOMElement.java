package com.ofrick;


import org.apache.axiom.om.OMElement;

public class TelalertEntryWithOMElement implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8767742565336958028L;
	private String escalation;
	private OMElement telalert;

	public TelalertEntryWithOMElement() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getEscalation() {
		return escalation;
	}

	public void setEscalation(String escalation) {
		this.escalation = escalation;
	}
	
	public OMElement getTelalert() {
		return telalert;
	}

	public void setTelalert(OMElement telalert) {
		this.telalert = telalert;
	}

}


