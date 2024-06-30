/*
 *************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *
 *************************************************************************
 */
package org.eclipse.birt.report.data.adapter.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.IDataSetInterceptorContext;

public class DataSetInterceptorContext implements IDataSetInterceptorContext {
	Map<String, DataRequestSession> sessionMap = new HashMap<>();

	@Override
	public DataRequestSession getRequestSession(String dataSource) {
		return sessionMap.get(dataSource);
	}

	@Override
	public void registDataRequestSession(String dataSource, DataRequestSession session) {
		sessionMap.put(dataSource, session);
	}

	@Override
	public void close() {
		if (sessionMap.size() > 0) {
			for (DataRequestSession session : sessionMap.values()) {
				session.shutdown();
			}
			sessionMap.clear();
		}
	}
}
