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

package org.eclipse.birt.data.engine.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * Describes the metadata of a detail row expected in an IResultIterator.
 * Implements Data Engine API IResultMetaData.
 */
public class ResultMetaData implements IResultMetaData
{
    IResultClass		m_odiResultClass;
	protected static Logger logger = Logger.getLogger( ResultMetaData.class.getName( ) );
   
	/**
	 * @param odiResultClass
	 */
    public ResultMetaData( IResultClass odiResultClass )
    {
        assert odiResultClass != null;
        m_odiResultClass = odiResultClass;
	    logger.logp( Level.FINER,
				QueryResults.class.getName( ),
				"QueryResults",
				"QueryResults starts up" );       
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnCount()
     */
    public int getColumnCount()
    {
	    logger.logp( Level.FINE,
				QueryResults.class.getName( ),
				"getColumnCount","");       
        return m_odiResultClass.getFieldCount();
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnName(int)
     */
    public String getColumnName( int index ) throws DataException
    {
	    logger.logp( Level.FINE,
				QueryResults.class.getName( ),
				"getColumnName",
				"the column name at the specified index",
				new Integer( index ) );       
        return m_odiResultClass.getFieldName( index );
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnAlias(int)
     */
    public String getColumnAlias( int index ) throws DataException
    {
	    logger.logp( Level.FINE,
				QueryResults.class.getName( ),
				"getColumnAlias",
				"the column alias at the specified index",
				new Integer( index ) );       
    	return m_odiResultClass.getFieldAlias( index );
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnType(int)
     */
    public int getColumnType( int index ) throws DataException
    {
	    logger.logp( Level.FINE,
				QueryResults.class.getName( ),
				"getColumnType",
				"the data type of the column at the specified index",
				new Integer( index ) );       
        Class odiDataType = m_odiResultClass.getFieldValueClass( index );
        return DataTypeUtil.toApiDataType( odiDataType );
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnTypeName(int)
     */
    public String getColumnTypeName( int index ) throws DataException
    {
	    logger.logp( Level.FINE,
				QueryResults.class.getName( ),
				"getColumnTypeName",
				"the Data Engine data type name of the column at the specified index",
				new Integer( index ) );       
        return DataType.getName( getColumnType( index ) );
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnNativeTypeName(int)
     */
	public String getColumnNativeTypeName( int index ) throws DataException
	{
	    logger.logp( Level.FINE,
				QueryResults.class.getName( ),
				"getColumnNativeTypeName",
				"the data provider specific data type name of the specified column",
				new Integer( index ) );       
		return m_odiResultClass.getFieldNativeTypeName( index );
	}

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnLabel(int)
     */
    public String getColumnLabel( int index ) throws DataException
    {
	    logger.logp( Level.FINE,
				QueryResults.class.getName( ),
				"getColumnLabel",
				"the label or display name of the column at the specified index",
				new Integer( index ) );    
        return m_odiResultClass.getFieldLabel( index );
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#isComputedColumn(int)
     */
	public boolean isComputedColumn( int index ) throws DataException
	{
	    logger.logp( Level.FINE,
				QueryResults.class.getName( ),
				"isComputedColumn",
				"whether the specified projected column is defined as a computed column",
				new Integer( index ) );       
	    return m_odiResultClass.isCustomField( index );
	}

}
