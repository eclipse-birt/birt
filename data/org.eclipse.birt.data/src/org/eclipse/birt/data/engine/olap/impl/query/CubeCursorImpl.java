
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.olap.OLAPException;
import javax.olap.cursor.Blob;
import javax.olap.cursor.Clob;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.Date;
import javax.olap.cursor.RowDataMetaData;
import javax.olap.cursor.Time;
import javax.olap.cursor.Timestamp;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.script.JSCubeBindingObject;
import org.eclipse.birt.data.engine.olap.script.OLAPExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
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
	private HashMap bindingMap;
	
	public CubeCursorImpl ( CubeCursor cursor, Scriptable scope, ICubeQueryDefinition queryDefn ) throws DataException
	{
		this.cursor = cursor;
		this.scope = scope;
		this.queryDefn = queryDefn;
		
		this.validateBindings( );
		
		this.bindingMap = new HashMap();
		for( int i = 0; i < this.queryDefn.getBindings( ).size( ); i++ )
		{
			IBinding binding = (IBinding) this.queryDefn.getBindings( ).get(i);
			if ( binding.getAggrFunction( ) == null )
			{
				this.bindingMap.put( binding.getBindingName( ), binding.getExpression( ) );
				OLAPExpressionCompiler.compile( binding.getExpression( ) );
			}
		}
		
		this.scope.put( "data", this.scope, new JSCubeBindingObject( this ));
	}
	
	/**
	 * 
	 * @throws DataException
	 */
	private void validateBindings( ) throws DataException
	{
		Set validMeasures = new HashSet();
		for( int i = 0; i < this.queryDefn.getMeasures( ).size( ); i++ )
		{
			IMeasureDefinition measure = (IMeasureDefinition) this.queryDefn.getMeasures( ).get( i );
			validMeasures.add( measure.getName( ) );
		}
		
		Set validDimLevels = new HashSet();

		populateLevel( validDimLevels, ICubeQueryDefinition.COLUMN_EDGE );
		populateLevel( validDimLevels, ICubeQueryDefinition.ROW_EDGE );
		
		for( int i = 0; i < this.queryDefn.getBindings( ).size( ); i++ )
		{
			IBinding binding = (IBinding)this.queryDefn.getBindings( ).get(i);
			Set levels = OlapExpressionCompiler.getReferencedDimLevel( binding.getExpression( ), this.queryDefn.getBindings( ) );
			if( ! validDimLevels.containsAll( levels ))
				throw new DataException( ResourceConstants.INVALID_BINDING_REFER_TO_INEXIST_DIMENSION,
						binding.getBindingName( ) );
			
			String measureName = OlapExpressionCompiler.getReferencedScriptObject( binding.getExpression( ),
					"measure" );
			if ( measureName != null && !validMeasures.contains( measureName ) )
				throw new DataException( ResourceConstants.INVALID_BINDING_REFER_TO_INEXIST_MEASURE,
						binding.getBindingName( ) );

			if ( binding.getAggregatOns( ).size( ) > 0
					&& binding.getAggrFunction( ) == null )
				throw new DataException( ResourceConstants.INVALID_BINDING_MISSING_AGGR_FUNC,
						binding.getBindingName( ) );
		}
	}

	/**
	 * 
	 * @param validDimLevels
	 * @param edgeType
	 */
	private void populateLevel( Set validDimLevels, int edgeType )
	{
		if ( this.queryDefn.getEdge( edgeType ) == null )
			return;
		for( int i = 0; i < this.queryDefn.getEdge( edgeType ).getDimensions( ).size( ); i++ )
		{
			for( int j = 0; j < getHierarchy( edgeType, i ).getLevels( ).size( );j++)
			{
				ILevelDefinition level = (ILevelDefinition)getHierarchy( edgeType, i ).getLevels( ).get( j );
				validDimLevels.add( new DimLevel( this.getDimension( edgeType, i ).getName( ), level.getName( )) );
			}
		}
	}

	/**
	 * 
	 * @param edgeType
	 * @param i
	 * @return
	 */
	private IHierarchyDefinition getHierarchy( int edgeType, int i )
	{
		return ((IHierarchyDefinition)(getDimension( edgeType, i )).getHierarchy( ).get( 0 ));
	}

	/**
	 * 
	 * @param edgeType
	 * @param i
	 * @return
	 */
	private IDimensionDefinition getDimension( int edgeType, int i )
	{
		return (IDimensionDefinition)this.queryDefn.getEdge( edgeType ).getDimensions( ).get( i );
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
				result = ScriptEvalUtil.evalExpr( (IBaseExpression) this.bindingMap.get( arg0 ),
						cx,
						this.scope,
						null,
						0 );
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
