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

/**
 * Manage the operation related with query result id.
 */
public class QueryResultIDUtil
{
	private static int qursStart = 0;

	private final static String QURE_ID_PREFIX = "QuRs";
	private final static String QURE_ID_SEPARATOR = "_";
	private final static String STREAM_ID_SEPARATOR = "/";

	/**
	 * No instance
	 */
	private QueryResultIDUtil( )
	{
	}

	/**
	 * @return
	 */
	public synchronized static String nextID( )
	{
		return QURE_ID_PREFIX + ( qursStart++ );
	}

	/**
	 * @param _1partQueryResultID
	 * @param _2partQueryResultID
	 * @return
	 */
	public static String buildID( String _1partQueryResultID,
			String _2partQueryResultID )
	{
		String newID = _1partQueryResultID;
		if ( newID != null )
			newID += QURE_ID_SEPARATOR + _2partQueryResultID;
		else
			newID = _2partQueryResultID;

		return newID;
	}

	/**
	 * @param queryResultID
	 * @return
	 */
	public static String get1PartID( String queryResultID )
	{
		if ( queryResultID == null )
			return null;

		int slashIndex = queryResultID.indexOf( QURE_ID_SEPARATOR );
		if ( slashIndex < 0 )
			return null;
		else
		{
			String nextStr = queryResultID.substring( slashIndex + 1 );
			if ( nextStr.startsWith( QURE_ID_PREFIX ) )
				return queryResultID.substring( 0, slashIndex );
			else
				return null;
		}
	}

	/**
	 * @param queryResultID
	 * @return
	 */
	public static String get2PartID( String queryResultID )
	{
		if ( queryResultID == null )
			return null;

		int slashIndex = queryResultID.indexOf( QURE_ID_SEPARATOR );
		if ( slashIndex < 0 )
			return null;
		else
		{
			String nextStr = queryResultID.substring( slashIndex + 1 );
			if ( nextStr.startsWith( QURE_ID_PREFIX ) )
				return queryResultID.substring( slashIndex + 1 );
			else
				return queryResultID;
		}
	}
	
	/**
	 * @param _1partQueryResultID
	 * @param _2partQueryResultID
	 * @return
	 */
	public static String getRealStreamID( String _1partQueryResultID,
			String _2partQueryResultID )
	{
		if ( _1partQueryResultID == null )
			return _2partQueryResultID;
		else
			return _1partQueryResultID
					+ STREAM_ID_SEPARATOR + _2partQueryResultID;
	}

}
