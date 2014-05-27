/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.ofrick;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.engine.ServiceLifeCycle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TelalertLifeCycle implements ServiceLifeCycle {
    private static final Log log = LogFactory.getLog(TelalertLifeCycle.class);

    public void startUp(ConfigurationContext configctx,
                        AxisService service) {
        try {
        	System.out.println("[TelalertLifeCycle] Initializing empty Job Queue...");
        	TelalertList jobQueue = new TelalertList("jobQueue");
    		//putTelalert( jobQueue );
    		
        	service.addParameter(new Parameter("jobQueue", jobQueue));
        } catch (Exception exception) {
            log.info(exception);
        }
    }

    public void shutDown(ConfigurationContext configctx,
                         AxisService service) {
    }
    
    private void putTelalert( TelalertList jobQueue) {
    	TelalertEntry entry = new TelalertEntry();
		entry.setEscalationScheme("EAI");
		entry.setMessage("this is a test msg");
		entry.setTts("and this is a test tts");
		entry.setClient("cwp1088");
		entry.setTimeStamp("201405202254");
		entry.setAlertId("1234");
		entry.setSystem("h0566");
		entry.setResourceName("Tomcat Service");
		
		jobQueue.add(entry);
    }

}