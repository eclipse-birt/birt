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

package org.eclipse.birt.report.item.crosstab.core.re;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

import javax.olap.cursor.CubeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;

/**
 * 
 */

public class DummyCubeResultSet implements ICubeResultSet
{

	public CubeCursor getCubeCursor( )
	{
		DummyDimensionCursor ddc1 = new DummyDimensionCursor( 2 );
		DummyDimensionCursor ddc2 = new DummyDimensionCursor( 2 );
		DummyDimensionCursor ddc3 = new DummyDimensionCursor( 2 );

		DummyEdgeCursor dec = new DummyEdgeCursor( 8 );
		dec.addDimensionCursor( ddc1 );
		dec.addDimensionCursor( ddc2 );
		dec.addDimensionCursor( ddc3 );

		DummyDimensionCursor ddr1 = new DummyDimensionCursor( 2 );
		DummyDimensionCursor ddr2 = new DummyDimensionCursor( 2 );
		DummyDimensionCursor ddr3 = new DummyDimensionCursor( 2 );

		DummyEdgeCursor der = new DummyEdgeCursor( 8 );
		der.addDimensionCursor( ddr1 );
		der.addDimensionCursor( ddr2 );
		der.addDimensionCursor( ddr3 );

		DummyCubeCursor dcc = new DummyCubeCursor( );
		dcc.addOrdinateEdgeCursor( dec );
		dcc.addOrdinateEdgeCursor( der );
//		dcc.addOrdinateEdgeCursor( new SimpleMixedEdgeCursor() );
//		dcc.addOrdinateEdgeCursor( new SimpleMixedEdgeCursor() );

		return dcc;
	}

	public void close( )
	{
		// TODO Auto-generated method stub

	}

	public Object evaluate( String expr )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object evaluate( IBaseExpression expr )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getBigDecimal( String name ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Blob getBlob( String name ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean getBoolean( String name ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] getBytes( String name ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public long getCurrentPosition( )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public Date getDate( String name ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Double getDouble( String name ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public int getEndingGroupLevel( )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public String getGroupId( int groupLevel )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public DataSetID getID( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getInteger( String name ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public IResultMetaData getResultMetaData( ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public int getStartingGroupLevel( )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public String getString( String name ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue( String name ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isEmpty( ) throws BirtException
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean next( )
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean skipTo( long rows )
	{
		// TODO Auto-generated method stub
		return false;
	}

	public String getCellIndex( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void skipTo( String cellIndex )
	{
		// TODO Auto-generated method stub
		
	}

	public IBaseResultSet getParent( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public IBaseQueryResults getQueryResults( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getRawID( ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public int getType( )
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
