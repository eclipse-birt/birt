
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl;

import java.util.Map;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.PreparedCubeQuery;

/**
 * 
 */

public class QueryPrepareUtil
{
	public static IPreparedQuery prepareQuery( DataEngineImpl dataEngine,
			IQueryDefinition queryDefn, IBaseDataSetDesign dataSetDesign,
			Map appContext, IQueryContextVisitor contextVisitor) throws DataException
	{
		return null;
		
	}
	
	static public IPreparedQuery preparePresentationQuery(
			DataEngineImpl dataEngine, IQueryDefinition queryDefn,
			IBaseDataSetDesign dataSetDesign, Map appContext,
			IQueryContextVisitor contextVisitor ) throws DataException
	{
		return null;
	}
	
	public static IPreparedCubeQuery prepareQuery( Map<String, String> cubeDataSourceMap, Map<String, String> cubeDataObjectMap, DataEngineSession session, DataEngineContext context, ICubeQueryDefinition cubeQuery, Map appContext ) throws DataException
	{
		return new PreparedCubeQuery( cubeQuery,
				session,
				context,
				appContext );
	}
	
	public static void clear( DataEngineSession session )
	{		
	}
}
