package com.ofrick;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import sample.servicelifecycle.bean.Book;

public class TelalertList extends ConcurrentLinkedQueue<TelalertEntry> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5497325971924111761L;
	private String listName;
	
	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public TelalertList() {
		super();
		// TODO Auto-generated constructor stub
	}

    public TelalertList(String listName) {
        this.listName = listName;
    }

	public void removeTelalertEntry(TelalertEntry telalert) {
		this.poll();
	}

	public TelalertEntry getTelalertEntry( String escalation ) {
		return (TelalertEntry) this.peek();
	}
	
}
