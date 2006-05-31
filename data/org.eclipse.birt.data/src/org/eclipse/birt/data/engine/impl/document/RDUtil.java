/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl.document;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Provide the sevice to new instance of RDSave and RDLoad
 */
public final class RDUtil
{
	/**
	 * @param context
	 * @param queryDefn
	 * @param rowCount
	 * @param queryResultInfo
	 * @return
	 * @throws DataException
	 */
	public static IRDSave newSave( DataEngineContext context,
			IBaseQueryDefinition queryDefn, int rowCount,
			QueryResultInfo queryResultInfo ) throws DataException
	{
		QueryResultInfo newQueryResultInfo = getRealQueryResultInfo( queryResultInfo );

		if ( context.getMode( ) == DataEngineContext.MODE_GENERATION )
			return new RDSave( context, queryDefn, rowCount, newQueryResultInfo );
		else if ( context.getMode( ) == DataEngineContext.MODE_UPDATE )
			return new RDSave2( context,
					queryDefn,
					rowCount,
					newQueryResultInfo );
		else
			assert false;

		return null;
	}

	/**
	 * @param queryResultInfo
	 * @return
	 */
	private static QueryResultInfo getRealQueryResultInfo(
			QueryResultInfo queryResultInfo )
	{
		String rootQueryResultID = null;
		String selfQueryResultID = null;

		selfQueryResultID = QueryResultIDUtil.get2PartID( queryResultInfo.getQueryResultID( ) );
		if ( selfQueryResultID == null )
			selfQueryResultID = queryResultInfo.getQueryResultID( );
		else
			rootQueryResultID = QueryResultIDUtil.get1PartID( queryResultInfo.getQueryResultID( ) );

		return new QueryResultInfo( rootQueryResultID,
				null,
				selfQueryResultID,
				queryResultInfo.getSubQueryName( ),
				queryResultInfo.getIndex( ) );
	}
	
	/**
	 * @param context
	 * @param queryResultInfo
	 * @return
	 * @throws DataException
	 */
	public static RDLoad newLoad( DataEngineContext context,
			QueryResultInfo queryResultInfo ) throws DataException
	{
		return new RDLoad( context, queryResultInfo );
	}

}
