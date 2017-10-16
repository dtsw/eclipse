package org.datanucleus.samples.jdo.tutorial;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.*;

@PersistenceCapable
public class Group
{
	@PrimaryKey
	String name = null;
    Set<Subscriber> subscribers = new HashSet<>();

    public Group(String name)
    {
        this.name = name;
    }

    public Set<Subscriber> getSubscribers() {return subscribers;}
}
