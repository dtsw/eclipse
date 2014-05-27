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

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;

public class TelalertService {

	//To store Telalerts until the are delivered
	private TelalertList undeliveredTelalertList;

	public void setTelalert( TelalertEntry telalert ) throws AxisFault {

		if (telalert == null) {
			throw new AxisFault("Telalert in Request was null!");
		}
		else {
			this.undeliveredTelalertList.add(telalert);
			System.out.println( "added!" + this.undeliveredTelalertList.size() );
		}

	}

	public TelalertEntry getTelalertByEscalation(String escalation) throws AxisFault {
		TelalertEntry telalert = undeliveredTelalertList.getTelalertEntry(escalation);
		if (telalert == null) {
			throw new AxisFault("No Telalert by that escalation.");
		}
		undeliveredTelalertList.removeTelalertEntry(telalert);
		return telalert;
	}

	/**
	 * Session related methods
	 */
	public void init(ServiceContext serviceContext) {
		AxisService service = serviceContext.getAxisService();
		this.undeliveredTelalertList = (TelalertList) service.getParameterValue("jobQueue");
		this.undeliveredTelalertList.setListName("jobQueue");
		System.out.println("[TelalertService] init done." + this.undeliveredTelalertList.size() );
	}

	public void destroy(ServiceContext serviceContext) throws AxisFault {
		AxisService service = serviceContext.getAxisService();
		service.addParameter(new Parameter("jobQueue", undeliveredTelalertList));
	}
}