
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
package org.eclipse.birt.data.engine.olap.impl.query;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.olap.OLAPException;
import javax.olap.cursor.Blob;
import javax.olap.cursor.Clob;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.Date;
import javax.olap.cursor.RowDataMetaData;
import javax.olap.cursor.Time;
import javax.olap.cursor.Timestamp;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.script.JSCubeBindingObject;
import org.eclipse.birt.data.engine.olap.script.OLAPExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapQueryUtil;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;


/**
 * 
 */

public class CubeCursorImpl implements ICubeCursor
{	
	private CubeCursor cursor;
	private Scriptable scope;
	private ICubeQueryDefinition queryDefn;
	private HashMap bindingMap, dataTypeMap;
	
	public CubeCursorImpl ( CubeCursor cursor, Scriptable scope, ICubeQueryDefinition queryDefn ) throws DataException
	{
		this.cursor = cursor;
		this.scope = scope;
		this.queryDefn = queryDefn;
		
		OlapQueryUtil.validateBinding( queryDefn, false );
		
		this.bindingMap = new HashMap( );
		this.dataTypeMap = new HashMap( );
		for ( int i = 0; i < this.queryDefn.getBindings( ).size( ); i++ )
		{
			IBinding binding = (IBinding) this.queryDefn.getBindings( ).get( i );
			if ( binding.getAggrFunction( ) == null )
			{
				this.bindingMap.put( binding.getBindingName( ),
						binding.getExpression( ) );
				OLAPExpressionCompiler.compile( binding.getExpression( ) );
			}
			dataTypeMap.put( binding.getBindingName( ),
					Integer.valueOf( binding.getDataType( ) ) );
		}
		
		this.scope.put( "data", this.scope, new JSCubeBindingObject( this ));
	}
	
	public List getOrdinateEdge( ) throws OLAPException
	{
		return this.cursor.getOrdinateEdge( );
	}

	public Collection getPageEdge( ) throws OLAPException
	{
		return this.cursor.getPageEdge( );
	}

	public void synchronizePages( ) throws OLAPException
	{
		
	}

	public void close( ) throws OLAPException
	{
		this.cursor.close( );
	}

	public InputStream getAsciiStream( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream getAsciiStream( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getBigDecimal( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getBigDecimal( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream getBinaryStream( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream getBinaryStream( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Blob getBlob( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Blob getBlob( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getBoolean( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getBoolean( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return false;
	}

	public byte getByte( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public byte getByte( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public byte[] getBytes( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] getBytes( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Reader getCharacterStream( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Reader getCharacterStream( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Clob getClob( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Clob getClob( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate( int arg0, Calendar arg1 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate( String arg0, Calendar arg1 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public double getDouble( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public double getDouble( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public float getFloat( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public float getFloat( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public int getInt( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public int getInt( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public long getLong( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public long getLong( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public RowDataMetaData getMetaData( ) throws OLAPException
	{
		return this.cursor.getMetaData( );
	}

	public Object getObject( int arg0 ) throws OLAPException
	{
		return null;
	}

	public Object getObject( String arg0 ) throws OLAPException
	{
		Object result = null;
		
		if ( this.bindingMap.get( arg0 ) == null )
		{
			result = this.cursor.getObject( arg0 );
			
		}
		else
		{
			try
			{
				Context cx = Context.enter( );
				IBaseExpression expr = (IBaseExpression) this.bindingMap.get( arg0 );
				result = ScriptEvalUtil.evalExpr( expr, cx, this.scope, null, 0 );
			}
			catch ( Exception e )
			{
				throw new OLAPException( e.getLocalizedMessage( ) );
			}
			finally
			{
				Context.exit( );
			}
		}

		if ( result instanceof DataException )
		{
			throw new OLAPException( ( (DataException) result ).getLocalizedMessage( ) );
		}
		
		if ( this.dataTypeMap.containsKey( arg0 ) )
		{
			try
			{
				result = DataTypeUtil.convert( JavascriptEvalUtil.convertJavascriptValue( result ),
						( (Integer) this.dataTypeMap.get( arg0 ) ).intValue( ) );
			}
			catch ( BirtException e )
			{
				throw new OLAPException( e.getLocalizedMessage( ) );
			}
		}
		return result;
	}

	public Object getObject( int arg0, Map arg1 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object getObject( String arg0, Map arg1 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public short getShort( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public short getShort( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public String getString( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getString( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Time getTime( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Time getTime( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Time getTime( int arg0, Calendar arg1 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Time getTime( String arg0, Calendar arg1 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getTimestamp( int arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getTimestamp( String arg0 ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getTimestamp( int arg0, Calendar arg1 )
			throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getTimestamp( String arg0, Calendar arg1 )
			throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getId( ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getName( ) throws OLAPException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setId( String value ) throws OLAPException
	{
		// TODO Auto-generated method stub

	}

	public void setName( String value ) throws OLAPException
	{
		// TODO Auto-generated method stub

	}
	
	public Scriptable getScope()
	{
		return this.scope;
	}
	
	public java.lang.Object clone( )
	{
		return cursor;
		
	}
}
