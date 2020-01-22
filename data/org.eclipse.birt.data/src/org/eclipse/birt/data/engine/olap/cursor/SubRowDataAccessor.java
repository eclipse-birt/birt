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

import javax.olap.OLAPException;

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
	 * @throws IOException 
	 */
	public SubRowDataAccessor( RowDataAccessorService service, IRowDataAccessor parentNavigator, int startingLevel ) throws IOException
	{
		super( service, parentNavigator.getAggregationResultSet( ) );
		this.parentNavigator = (RowDataAccessor) parentNavigator;
		this.startingLevel = startingLevel;
		this.service = service;
		this.rs = parentNavigator.getAggregationResultSet( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.RowDataAccessor#populateEdgeInfo(boolean)
	 */
	public void initialize( boolean isPage ) throws IOException
	{
		if ( startingLevel < 0 )
		{
			edgeStart = 0;
			edgeEnd = this.parentNavigator.edgeDimensRelation.traverseLength - 1;
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
		dimTraverse.first();
		edgeTraverse = new EdgeTraverse( this.parentNavigator.edgeDimensRelation,
				edgeStart,
				edgeEnd );
	}
	
	public boolean edge_relative( int arg0 ) throws OLAPException
	{
		if ( arg0 == 0 )
			return true;
		int position = this.edgeTraverse.currentPosition + arg0;
		if ( position >= this.edgeDimensRelation.traverseLength )
		{
			this.edge_afterLast( );
			return false;
		}
		else if ( position < 0 )
		{
			this.dimTraverse.beforeFirst( );
			this.edgeTraverse.currentPosition = -1;
			return false;
		}
		else
		{
			for ( int i = 0; i < Math.abs( arg0 ); i++ )
			{
				if ( arg0 > 0 )
					this.edge_next( );
				else
					this.edge_previous( );
			}
			return true;
		}
	}
}