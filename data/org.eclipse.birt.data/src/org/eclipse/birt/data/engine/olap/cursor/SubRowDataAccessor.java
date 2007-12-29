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
package org.eclipse.birt.data.engine.olap.cursor;

import java.io.IOException;

import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;

/**
 * 
 *
 */
public class SubRowDataAccessor extends RowDataAccessor
{
	private int startingLevel = -1, edgeStart = 0, edgeEnd = 0;
	private RowDataAccessor parentNavigator;
	private IAggregationResultSet rs;
	private RowDataAccessorService service;
	
	/**
	 * 
	 * @param service
	 * @param parentNavigator
	 * @param startingLevel
	 */
	public SubRowDataAccessor( RowDataAccessorService service, RowDataAccessor parentNavigator, int startingLevel )
	{
		super( service );
		this.parentNavigator = parentNavigator;
		this.startingLevel = startingLevel;
		this.service = service;
		this.rs = service.getAggregationResultSet( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.RowDataAccessor#populateEdgeInfo(boolean)
	 */
	public void populateEdgeInfo( boolean isPage ) throws IOException
	{
		if ( startingLevel < 0 )
		{
			edgeStart = 0;
			int fetchSize = this.parentNavigator.getDataAccessorService( )
					.getFetchSize( );
			if ( fetchSize > 0 && this.rs.length( ) > fetchSize )
			{
				edgeEnd = fetchSize - 1;
			}
			else
			{
				edgeEnd = this.parentNavigator.edgeDimensRelation.traverseLength - 1;
			}
		}
		else
		{
			edgeStart = this.parentNavigator.getEdgeStart( startingLevel );
			edgeEnd = this.parentNavigator.getEdgeEnd( startingLevel );
		}
		edgeDimensRelation = this.parentNavigator.edgeDimensRelation;
		dimTraverse = new DimensionTraverse( service.getDimensionAxis( ),
				this.parentNavigator.edgeDimensRelation,
				edgeStart,
				edgeEnd );
		edgeTraverse = new EdgeTraverse( this.parentNavigator.edgeDimensRelation,
				edgeStart,
				edgeEnd );
	}
}