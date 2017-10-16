/**********************************************************************
Copyright (c) 2003 Andy Jefferson and others. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Contributors:
    ...
**********************************************************************/
package org.datanucleus.samples.jdo.tutorial;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Extent;
import javax.jdo.Query;
import javax.jdo.JDOHelper;
import javax.jdo.Transaction;

/**
 * Controlling application for the DataNucleus Tutorial using JDO.
 * Relies on the user defining a file "datanucleus.properties" to be in the CLASSPATH
 * and to include the JDO properties for the DataNucleus PersistenceManager.
 */
public class Main
{
    public static void main(String args[])
    {
        // Create a PersistenceManagerFactory for this datastore
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("Tutorial");

        System.out.println("DataNucleus AccessPlatform with JDO");
        System.out.println("===================================");

        // Persistence of a Product and a Book.
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        Object groupId = null;
        Object groupId2 = null;
        try
        {
            tx.begin();
            System.out.println("Persisting Groups, Persons and Devices");
 
            Group group = new Group("WINDOWS");
            Person p1 = new Person("John","Doe","ABABC");
            Device d1 = new Device("TEXT_PHONE","41795555555");
            Device d2 = new Device("EMAIL","john.doe@ofrick.com");
            p1.getDevices().add(d1);
            p1.getDevices().add(d2);
            group.getSubscribers().add(p1);
            group.getSubscribers().add(d2);
            pm.makePersistent(group);
            
            Group g2 = new Group("UNIX");
            Device d3 = new Device("EMAIL","systems.management@ofrick.com");
            g2.getSubscribers().add(d3);
            pm.makePersistent(g2);
            
            tx.commit();
            groupId = pm.getObjectId(group);
            groupId2 = pm.getObjectId(g2);
            System.out.println("Objects have been persisted");
        }
        catch (Exception e)
        {
            System.out.println("Exception persisting data : " + e.getMessage());
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
        System.out.println("");

        // Basic Extent of all Products
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try
        {
            tx.begin();
            System.out.println("Retrieving Extent for Subscribers");
            Extent<Subscriber> e2 = pm.getExtent(Subscriber.class, true);
            Iterator<Subscriber> iter2 = e2.iterator();
            while (iter2.hasNext())
            {
                Object obj = iter2.next();
                System.out.println(">  " + obj);
                if ( obj instanceof Person ) { 
                	System.out.println("is a person");
                	Person pr = (Person) obj;
                	for ( Device dev : pr.getDevices() ) {
                		System.out.println(dev.getType() + ":" + dev.getAddress());
                	}
                }
                if ( obj instanceof Device ) System.out.println("is a device");
            }    
            tx.commit();
        }
        catch (Exception e)
        {
            System.out.println("Exception thrown during retrieval of Extent : " + e.getMessage());
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
        System.out.println("");
        
        try {
			Thread.sleep(30*1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        // Clean out the database
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try
        {
            tx.begin();

            System.out.println("Retrieving Group using its id");
            Group inv = (Group)pm.getObjectById(groupId);

            System.out.println("Clearing out Subscribers");
            for ( Subscriber s : inv.getSubscribers() ) {
            	if ( s instanceof Person ) {
                    System.out.println("Clearing out Devices");
            		((Person) s).getDevices().clear();
            	}
            }
            inv.getSubscribers().clear();

            System.out.println("Deleting Group");
            pm.deletePersistent(inv);
            
            System.out.println("Deleting all persons from persistence");
            Query q = pm.newQuery(Person.class);
            long numberInstancesDeleted = q.deletePersistentAll();
            System.out.println("Deleted " + numberInstancesDeleted + " persons");
            
            System.out.println("Deleting all groups from persistence");
            q = pm.newQuery(Group.class);
            numberInstancesDeleted = q.deletePersistentAll();
            System.out.println("Deleted " + numberInstancesDeleted + " groups");
            
            System.out.println("Deleting all devices from persistence");
            q = pm.newQuery(Device.class);
            numberInstancesDeleted = q.deletePersistentAll();
            System.out.println("Deleted " + numberInstancesDeleted + " devices");
            
            tx.commit();
        }
        catch (Exception e)
        {
            System.out.println("Exception cleaning out the database : " + e.getMessage());
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }

        System.out.println("");
        System.out.println("End of Tutorial");
        pmf.close();
    }
}
