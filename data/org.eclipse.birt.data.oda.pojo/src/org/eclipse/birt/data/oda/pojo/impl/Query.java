/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.oda.pojo.impl;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.URLClassLoader;
import org.eclipse.birt.data.oda.pojo.Activator;
import org.eclipse.birt.data.oda.pojo.api.IPojoDataSet;
import org.eclipse.birt.data.oda.pojo.api.PojoDataSetFromArray;
import org.eclipse.birt.data.oda.pojo.api.PojoDataSetFromCollection;
import org.eclipse.birt.data.oda.pojo.api.PojoDataSetFromIterator;
import org.eclipse.birt.data.oda.pojo.i18n.Messages;
import org.eclipse.birt.data.oda.pojo.impl.internal.ClassMethodFieldBuffer;
import org.eclipse.birt.data.oda.pojo.impl.internal.PojoDataSetFromCustomClass;
import org.eclipse.birt.data.oda.pojo.querymodel.PojoQuery;
import org.eclipse.birt.data.oda.pojo.util.PojoQueryParser;
import org.eclipse.birt.data.oda.pojo.util.URLParser;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;


/**
 * Implementation class of IQuery for ODA POJO runtime driver.
 */
public class Query implements IQuery
{
	private static Logger logger = Logger.getLogger( Query.class.getName( ) );
	private int maxRows;
	private boolean isClosed;
	
	private String pojoDataSetClassPath; //Class path for the class serving as POJO instance provider 
	
	private PojoQuery pojoQuery;
	
	private Connection connection;
	
	private Map<String, Object> passedInParams = new HashMap<String, Object>( );
	
	@SuppressWarnings("unchecked")
	Map appContext;
	
	public Query( String pojoDataSetClassPath )
	{
		this.pojoDataSetClassPath = pojoDataSetClassPath;
		this.connection = null;
	}
	
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#prepare(java.lang.String)
	 */
	public void prepare( String queryText ) throws OdaException
	{
		testClosed( );
		if ( queryText == null )
		{
			throw new OdaException( Messages.getString( "Query.NoQueryText" ) ); //$NON-NLS-1$
		}
		pojoQuery = PojoQueryParser.parse( queryText );
		pojoQuery.setConnection(connection);
	}
	
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setAppContext(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public void setAppContext( Object context ) throws OdaException
	{
	    testClosed( );
	    if ( context != null && !(context instanceof Map) )
	    {
	    	logger.log( Level.WARNING, "Invalid appContext: " + context ); //$NON-NLS-1$
	    }
	    else
	    {
	    	appContext = (Map)context;
	    }
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#close()
	 */
	public void close() throws OdaException
	{
        isClosed = true;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMetaData()
	 */
	public IResultSetMetaData getMetaData() throws OdaException
	{
		testClosed( );
		return new ResultSetMetaData( pojoQuery.getReferenceGraph( ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#executeQuery()
	 */
	public IResultSet executeQuery() throws OdaException
	{
		testClosed( );
		URLClassLoader pojoClassLoader = getPojoDataSetClassLoader( );
		IPojoDataSet pojoDataSet = getPojoDataSet( pojoClassLoader );
		pojoQuery.prepareParameterValues( passedInParams, pojoClassLoader );
		pojoDataSet.open( appContext, passedInParams );
		IResultSet resultSet = new ResultSet( pojoQuery, pojoDataSet, pojoClassLoader );
		resultSet.setMaxRows( getMaxRows() );
		return resultSet;
	}
	
    @SuppressWarnings("unchecked")
	private IPojoDataSet getPojoDataSet( ClassLoader cl ) throws OdaException
    {
    	
    	//First try to get from appContext
    	IPojoDataSet pojoDataSet = getPojoDataSetFromAppContext( );
    	
    	if ( pojoDataSet == null )
    	{
    		Class pojoDataSetClass = loadPojoDataSetClass( cl );
    		try
			{
    			if ( IPojoDataSet.class.isAssignableFrom( pojoDataSetClass ))
    			{
    				//pojoDataSetClass is a subclass of IPojoDataSet
    				pojoDataSet = (IPojoDataSet)pojoDataSetClass.newInstance( );
    			}
    			else
    			{
    				pojoDataSet = new PojoDataSetFromCustomClass( pojoDataSetClass );
    			}
			}
			catch ( InstantiationException e )
			{
				throw new OdaException( e );
			}
			catch ( IllegalAccessException e )
			{
				throw new OdaException( e );
			}
	
    	}
    	return pojoDataSet;
    }
    
    @SuppressWarnings("unchecked")
	private Class loadPojoDataSetClass( ClassLoader cl ) throws OdaException
    {
		String dataSetClass = pojoQuery.getDataSetClass( );
		
		if ( dataSetClass == null || dataSetClass.length( ) == 0 )
		{
			throw new OdaException( Messages.getString( "ResultSet.MissDataSetClassName" )); //$NON-NLS-1$
		}
		try
		{
			return cl.loadClass( dataSetClass );
		}
		catch ( Throwable e )
		{
			throw new OdaException( e );
		}
    }
    
    private URLClassLoader getPojoDataSetClassLoader( ) throws OdaException
    {
		URLParser up = new URLParser( appContext );
		URL[] urls = up.parse( pojoDataSetClassPath );
		logger.log( Level.INFO, "URLs from data set class path: [" + Arrays.toString( urls ) + "]" ); //$NON-NLS-1$ //$NON-NLS-2$
		return new URLClassLoader( urls, Activator.class.getClassLoader( ) );
    }
    
    @SuppressWarnings("unchecked")
	private IPojoDataSet getPojoDataSetFromAppContext( ) throws OdaException
    {
    	if ( appContext != null 
    			&& pojoQuery.getAppContextKey( ) != null && pojoQuery.getAppContextKey( ).length( ) > 0 )
    	{
    		final Object pojoInstances = appContext.get( pojoQuery.getAppContextKey( ) );
    		if ( pojoInstances != null ) //set by user
    		{
    			if ( pojoInstances instanceof Iterator )
    			{
    				return new PojoDataSetFromIterator( )
    				{
						@Override
						protected Iterator fetchPojos( ) throws OdaException
						{
							return (Iterator) pojoInstances;
						}
    				};
    			} 
    			else if ( pojoInstances instanceof Collection )
    			{
    				return new PojoDataSetFromCollection( )
    				{
						@Override
						protected Collection fetchPojos( ) throws OdaException
						{
							return (Collection) pojoInstances;
						}
    				};
    			} 
    			else if ( pojoInstances instanceof Object[] )
    			{
    				return new PojoDataSetFromArray( )
    				{
						protected Object[] fetchPojos( ) throws OdaException
						{
							return (Object[]) pojoInstances;
						}
    				};
    			}
    			else
    			{
    				throw new OdaException(Messages.getString( "ResultSet.InvalidAppContextValue", pojoQuery.getAppContextKey( ), pojoInstances.getClass( ).getName( ))); //$NON-NLS-1$
    			}
    		}
    	}
    	return null;
    }

	/* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#cancel()
     */
    public void cancel() throws OdaException, UnsupportedOperationException
    {
        throw new UnsupportedOperationException( );
    }

    /*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setProperty(java.lang.String, java.lang.String)
	 */
	public void setProperty( String name, String value ) throws OdaException
	{
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setMaxRows(int)
	 */
	public void setMaxRows( int max ) throws OdaException
	{
		testClosed( );
	    maxRows = max > 0 ? max : 0;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMaxRows()
	 */
	public int getMaxRows() throws OdaException
	{
		testClosed( );
		return maxRows;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#clearInParameters()
	 */
	public void clearInParameters() throws OdaException
	{
		passedInParams.clear( );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(java.lang.String, int)
	 */
	public void setInt( String parameterName, int value ) throws OdaException
	{
		passedInParams.put( parameterName, value );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(int, int)
	 */
	public void setInt( int parameterId, int value ) throws OdaException
	{
		setParamValueByIndex( parameterId, value );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(java.lang.String, double)
	 */
	public void setDouble( String parameterName, double value ) throws OdaException
	{
		passedInParams.put( parameterName, value );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(int, double)
	 */
	public void setDouble( int parameterId, double value ) throws OdaException
	{
		setParamValueByIndex( parameterId, value );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(java.lang.String, java.math.BigDecimal)
	 */
	public void setBigDecimal( String parameterName, BigDecimal value ) throws OdaException
	{
		passedInParams.put( parameterName, value );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(int, java.math.BigDecimal)
	 */
	public void setBigDecimal( int parameterId, BigDecimal value ) throws OdaException
	{
		setParamValueByIndex( parameterId, value );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(java.lang.String, java.lang.String)
	 */
	public void setString( String parameterName, String value ) throws OdaException
	{
		passedInParams.put( parameterName, value );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(int, java.lang.String)
	 */
	public void setString( int parameterId, String value ) throws OdaException
	{
		setParamValueByIndex( parameterId, value );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(java.lang.String, java.sql.Date)
	 */
	public void setDate( String parameterName, Date value ) throws OdaException
	{
		passedInParams.put( parameterName, value );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(int, java.sql.Date)
	 */
	public void setDate( int parameterId, Date value ) throws OdaException
	{
		setParamValueByIndex( parameterId, value );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(java.lang.String, java.sql.Time)
	 */
	public void setTime( String parameterName, Time value ) throws OdaException
	{
		passedInParams.put( parameterName, value );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(int, java.sql.Time)
	 */
	public void setTime( int parameterId, Time value ) throws OdaException
	{
		setParamValueByIndex( parameterId, value );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(java.lang.String, java.sql.Timestamp)
	 */
	public void setTimestamp( String parameterName, Timestamp value ) throws OdaException
	{
		passedInParams.put( parameterName, value );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(int, java.sql.Timestamp)
	 */
	public void setTimestamp( int parameterId, Timestamp value ) throws OdaException
	{
		setParamValueByIndex( parameterId, value );
	}

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(java.lang.String, boolean)
     */
    public void setBoolean( String parameterName, boolean value )
            throws OdaException
    {
    	passedInParams.put( parameterName, value );
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(int, boolean)
     */
    public void setBoolean( int parameterId, boolean value )
            throws OdaException
    {
    	setParamValueByIndex( parameterId, value );
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setObject(java.lang.String, java.lang.Object)
     */
    public void setObject( String parameterName, Object value )
            throws OdaException
    {
    	passedInParams.put( parameterName, value );
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setObject(int, java.lang.Object)
     */
    public void setObject( int parameterId, Object value ) throws OdaException
    {
    	setParamValueByIndex( parameterId, value );
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(java.lang.String)
     */
    public void setNull( String parameterName ) throws OdaException
    {
    	passedInParams.put( parameterName, null );
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(int)
     */
    public void setNull( int parameterId ) throws OdaException
    {
    	setParamValueByIndex( parameterId, null );
    }
    
    private void setParamValueByIndex( int index, Object value ) throws OdaException
    {
    	String name = pojoQuery.getQueryParameters( ).getParamName( index );
    	if ( name != null )
    	{
    		setObject( name, value );
    	}
    }

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#findInParameter(java.lang.String)
	 */
	public int findInParameter( String parameterName ) throws OdaException
	{
		return pojoQuery.getQueryParameters( ).findInParameter( parameterName );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getParameterMetaData()
	 */
	public IParameterMetaData getParameterMetaData() throws OdaException
	{
		return pojoQuery.getQueryParameters( ).getParameterMetaData( );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setSortSpec(org.eclipse.datatools.connectivity.oda.SortSpec)
	 */
	public void setSortSpec( SortSpec sortBy ) throws OdaException
	{
        throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getSortSpec()
	 */
	public SortSpec getSortSpec() throws OdaException
	{
		throw new UnsupportedOperationException();
	}

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setSpecification(org.eclipse.datatools.connectivity.oda.spec.QuerySpecification)
     */
    @SuppressWarnings("restriction")
    public void setSpecification( QuerySpecification querySpec )
            throws OdaException, UnsupportedOperationException
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#getSpecification()
     */
    @SuppressWarnings("restriction")
    public QuerySpecification getSpecification()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#getEffectiveQueryText()
     */
    public String getEffectiveQueryText()
    {
        throw new UnsupportedOperationException( );
    }
    
    public void setConnection( Connection connection )
    {
    	this.connection = connection;
    }
    
	/**
	 * If the result set is closed then throw an OdaException. This method is invoked
	 * before an method defined in IResultSet is called.
	 * 
	 * @throws OdaException
	 */
	private void testClosed() throws OdaException
	{
		if( isClosed )
		{
			throw new OdaException( Messages.getString("Query.Closed")); //$NON-NLS-1$
		}
	}
}
