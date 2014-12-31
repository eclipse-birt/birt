/*
 *************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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


public class DataSetInterceptorContext implements IDataSetInterceptorContext
{
	Map<String, DataRequestSession> sessionMap = new HashMap<String, DataRequestSession>( );

	public DataRequestSession getRequestSession( String dataSource )
	{
		return sessionMap.get( dataSource );
	}

	public void registDataRequestSession( String dataSource,
			DataRequestSession session )
	{
		sessionMap.put( dataSource, session );
	}

	public void close( )
	{
		if( sessionMap.size( ) > 0 )
		{
			for( DataRequestSession session: sessionMap.values( ) )
			{
				session.shutdown( );
			}
			sessionMap.clear( );
		}
	}
}
