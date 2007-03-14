/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.cursor;

import javax.olap.OLAPException;

import org.eclipse.birt.data.engine.olap.driver.EdgeAxis;

/**
 * 
 * @author Administrator
 *
 */
class EdgeNavigator implements INavigator
{

	private EdgeInfoGenerator edgeInfoGenerator;
	
	EdgeNavigator( EdgeAxis axis )
	{
		this.edgeInfoGenerator = axis.getEdgeInfoUtil( );
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#afterLast()
	 */
	public void afterLast( ) throws OLAPException
	{
		edgeInfoGenerator.edge_afterLst( );
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#beforeFirst()
	 */
	public void beforeFirst( ) throws OLAPException
	{
		edgeInfoGenerator.edge_beforeFirst( );
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#close()
	 */
	public void close( )
	{
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#first()
	 */
	public boolean first( ) throws OLAPException
	{
		return edgeInfoGenerator.edge_first( );
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#getCurrentMemeber()
	 */
	public Object getCurrentMemeber( )
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#getExtend()
	 */
	public long getExtend( )
	{
		return 0;
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#getPosition()
	 */
	public long getPosition( ) throws OLAPException
	{
		return edgeInfoGenerator.getEdgePostion( );
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#getType()
	 */
	public int getType( )
	{
		return 0;
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#isAfterLast()
	 */
	public boolean isAfterLast( ) throws OLAPException
	{
		return this.edgeInfoGenerator.edge_isAfterLast( );
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#isBeforeFirst()
	 */
	public boolean isBeforeFirst( )
	{
		return this.edgeInfoGenerator.edge_isBeforeFirst( );
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#isFirst()
	 */
	public boolean isFirst( ) throws OLAPException
	{
		return this.edgeInfoGenerator.edge_isFirst( );
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#isLast()
	 */
	public boolean isLast( ) throws OLAPException
	{
		return this.edgeInfoGenerator.edge_isLast( );
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#last()
	 */
	public boolean last( ) throws OLAPException
	{
		return this.edgeInfoGenerator.edge_last( );
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#next()
	 */
	public boolean next( ) throws OLAPException
	{
		return this.edgeInfoGenerator.edge_next( );
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#previous()
	 */
	public boolean previous( ) throws OLAPException
	{
		return this.edgeInfoGenerator.edge_previous( );
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#relative(int)
	 */
	public boolean relative( int arg0 ) throws OLAPException
	{
		return this.edgeInfoGenerator.edge_relative( arg0 );
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#setPosition(long)
	 */
	public void setPosition( long position ) throws OLAPException
	{
		this.edgeInfoGenerator.edge_setPostion( position );
	}

}
