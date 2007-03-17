/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer.testdriver;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.eclipse.datatools.connectivity.oda.IAdvancedQuery;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IParameterRowSet;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;

/**
 * A tester ODA driver to test the behavior of odaconsumer, calling
 * on an ODA driver's IQuery implementation. 
 * Behavior being tested include:
 * 	setAppContext
 */
public class TestAdvQueryImpl implements IAdvancedQuery
{
    private Object m_appContext;
    private boolean m_isPrepareCalled = false;
    private IParameterMetaData m_paramMetaData;

    public TestAdvQueryImpl()
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#prepare(java.lang.String)
     */
    public void prepare( String queryText ) throws OdaException
    {
        m_isPrepareCalled = true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setAppContext(java.lang.Object)
     */
    public void setAppContext( Object context ) throws OdaException
    {
        // a new this instance should be created each time by odaconsumer,
        // when it opens a connection;
        // so the state should be initialized properly each time
        if( m_isPrepareCalled )
            throw new OdaException( "Error: setAppContext should have been called *before* IQuery.prepare." );
        m_appContext = context;
    }
    
    public Object getAppContext()
    {
        return m_appContext;
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setProperty(java.lang.String, java.lang.String)
     */
    public void setProperty( String name, String value ) throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#close()
     */
    public void close() throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setMaxRows(int)
     */
    public void setMaxRows( int max ) throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#getMaxRows()
     */
    public int getMaxRows() throws OdaException
    {
        return 0;
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#getMetaData()
     */
    public IResultSetMetaData getMetaData() throws OdaException
    {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#executeQuery()
     */
    public IResultSet executeQuery() throws OdaException
    {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#clearInParameters()
     */
    public void clearInParameters() throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(java.lang.String, int)
     */
    public void setInt( String parameterName, int value ) throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(int, int)
     */
    public void setInt( int parameterId, int value ) throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(java.lang.String, double)
     */
    public void setDouble( String parameterName, double value )
            throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(int, double)
     */
    public void setDouble( int parameterId, double value ) throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(java.lang.String, java.math.BigDecimal)
     */
    public void setBigDecimal( String parameterName, BigDecimal value )
            throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(int, java.math.BigDecimal)
     */
    public void setBigDecimal( int parameterId, BigDecimal value )
            throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(java.lang.String, java.lang.String)
     */
    public void setString( String parameterName, String value )
            throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(int, java.lang.String)
     */
    public void setString( int parameterId, String value ) throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(java.lang.String, java.sql.Date)
     */
    public void setDate( String parameterName, Date value ) throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(int, java.sql.Date)
     */
    public void setDate( int parameterId, Date value ) throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(java.lang.String, java.sql.Time)
     */
    public void setTime( String parameterName, Time value ) throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(int, java.sql.Time)
     */
    public void setTime( int parameterId, Time value ) throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(java.lang.String, java.sql.Timestamp)
     */
    public void setTimestamp( String parameterName, Timestamp value )
            throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(int, java.sql.Timestamp)
     */
    public void setTimestamp( int parameterId, Timestamp value )
            throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(java.lang.String, boolean)
     */
    public void setBoolean( String parameterName, boolean value )
            throws OdaException
    {        
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(int, boolean)
     */
    public void setBoolean( int parameterId, boolean value )
            throws OdaException
    {        
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(java.lang.String)
     */
    public void setNull( String parameterName ) throws OdaException
    {        
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(int)
     */
    public void setNull( int parameterId ) throws OdaException
    {        
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#findInParameter(java.lang.String)
     */
    public int findInParameter( String parameterName ) throws OdaException
    {
        // test driver does not handle input parameters
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#getParameterMetaData()
     */
    public IParameterMetaData getParameterMetaData() throws OdaException
    {
        if( m_paramMetaData == null )
            m_paramMetaData = new TestParamMetaDataImpl();
        return m_paramMetaData;
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setSortSpec(org.eclipse.datatools.connectivity.oda.SortSpec)
     */
    public void setSortSpec( SortSpec sortBy ) throws OdaException
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#getSortSpec()
     */
    public SortSpec getSortSpec() throws OdaException
    {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#execute()
     */
    public boolean execute() throws OdaException
    {
        return true;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#findOutParameter(java.lang.String)
     */
    public int findOutParameter( String parameterName ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBoolean(int)
     */
    public boolean getBoolean( int parameterId ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBoolean(java.lang.String)
     */
    public boolean getBoolean( String parameterName ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBigDecimal(int)
     */
    public BigDecimal getBigDecimal( int parameterId ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBigDecimal(java.lang.String)
     */
    public BigDecimal getBigDecimal( String parameterName ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBlob(int)
     */
    public IBlob getBlob( int parameterId ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBlob(java.lang.String)
     */
    public IBlob getBlob( String parameterName ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getClob(int)
     */
    public IClob getClob( int parameterId ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getClob(java.lang.String)
     */
    public IClob getClob( String parameterName ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getDate(int)
     */
    public Date getDate( int parameterId ) throws OdaException
    {
        validateOutputParamId( parameterId );
        
        if( parameterId == 2 )
            return Date.valueOf( "2005-11-13" );
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getDate(java.lang.String)
     */
    public Date getDate( String parameterName ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getDouble(int)
     */
    public double getDouble( int parameterId ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getDouble(java.lang.String)
     */
    public double getDouble( String parameterName ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getInt(int)
     */
    public int getInt( int parameterId ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getInt(java.lang.String)
     */
    public int getInt( String parameterName ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getMetaDataOf(java.lang.String)
     */
    public IResultSetMetaData getMetaDataOf( String resultSetName )
            throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getMoreResults()
     */
    public boolean getMoreResults() throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getResultSet()
     */
    public IResultSet getResultSet() throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getResultSet(java.lang.String)
     */
    public IResultSet getResultSet( String resultSetName ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getResultSetNames()
     */
    public String[] getResultSetNames() throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getRow(int)
     */
    public IParameterRowSet getRow( int parameterId ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getRow(java.lang.String)
     */
    public IParameterRowSet getRow( String parameterName ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getSortSpec(java.lang.String)
     */
    public SortSpec getSortSpec( String resultSetName ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getString(int)
     */
    public String getString( int parameterId ) throws OdaException
    {
        validateOutputParamId( parameterId );
        
        // return appContext object for parameter 1
        if( parameterId == 1 && getAppContext() != null )
            return getAppContext().toString();
        if( parameterId == 3 )
            return "parameter 3 value as a String";
        return null;
    }
    
    /**
     * @param parameterId
     * @throws OdaException
     */
    private void validateOutputParamId( int parameterId ) throws OdaException
    {
        IParameterMetaData paramMD = getParameterMetaData();
        if( paramMD == null )
            throw new OdaException( "Problem with getting query's paramter meta-data." );
        if( parameterId > paramMD.getParameterCount() )
            throw new OdaException( "Given paramter id does not match parameter meta-data." );
        if( paramMD.getParameterMode( parameterId ) == IParameterMetaData.parameterModeIn )
            throw new OdaException( "Given paramter id is not an output parameter." );
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getString(java.lang.String)
     */
    public String getString( String parameterName ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getTime(int)
     */
    public Time getTime( int parameterId ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getTime(java.lang.String)
     */
    public Time getTime( String parameterName ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getTimestamp(int)
     */
    public Timestamp getTimestamp( int parameterId ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getTimestamp(java.lang.String)
     */
    public Timestamp getTimestamp( String parameterName ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setNewRow(int)
     */
    public IParameterRowSet setNewRow( int parameterId ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setNewRow(java.lang.String)
     */
    public IParameterRowSet setNewRow( String parameterName )
            throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setNewRowSet(int)
     */
    public IParameterRowSet setNewRowSet( int parameterId ) throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setNewRowSet(java.lang.String)
     */
    public IParameterRowSet setNewRowSet( String parameterName )
            throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setSortSpec(java.lang.String, org.eclipse.datatools.connectivity.oda.SortSpec)
     */
    public void setSortSpec( String resultSetName, SortSpec sortBy )
            throws OdaException
    {
        // test driver does not support this
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#wasNull()
     */
    public boolean wasNull() throws OdaException
    {
        // use whatever value was obtained, which could be null
        return false;	
    }
}
