/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.eclipse.birt.data.engine.api.DataType;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * Describes the metadata of a detail row expected in an IResultIterator.
 * Implements Data Engine API IResultMetaData.
 */
class ResultMetaData implements IResultMetaData
{
    IResultClass		m_odiResultClass;
    
    ResultMetaData( IResultClass odiResultClass )
    {
        assert odiResultClass != null;
        m_odiResultClass = odiResultClass;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnCount()
     */
    public int getColumnCount()
    {
        return m_odiResultClass.getFieldCount();
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnName(int)
     */
    public String getColumnName( int index ) throws DataException
    {
        try
        {
            return m_odiResultClass.getFieldName( index );
        }
        catch ( DataException e )
        {
            throw e;
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnAlias(int)
     */
    public String getColumnAlias( int index ) throws DataException
    {
        try
        {
            return m_odiResultClass.getFieldAlias( index );
        }
        catch ( DataException e )
        {
            throw e;
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnType(int)
     */
    public int getColumnType( int index ) throws DataException
    {
        Class odiDataType = null;
        try
        {
            odiDataType = m_odiResultClass.getFieldValueClass( index );
        }
        catch ( DataException e )
        {
            throw e;
        }
 
		if( odiDataType == null )
			return DataType.UNKNOWN_TYPE;

		// maps odi data type to Dte api DataType
		if( odiDataType == Integer.class )
		    return DataType.INTEGER_TYPE;
		if( odiDataType == Double.class )
		    return DataType.DOUBLE_TYPE;
		if( odiDataType == String.class )
			return DataType.STRING_TYPE;
		if( odiDataType == BigDecimal.class )
			return DataType.DECIMAL_TYPE;
		if( odiDataType == Date.class ||
			odiDataType == Time.class ||
			odiDataType == Timestamp.class )
			return DataType.DATE_TYPE;
		
		// any other types are not recognized nor supported;
		// BOOLEAN_TYPE and BLOB_TYPE are not supported yet
		assert false;
		return DataType.UNKNOWN_TYPE;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnTypeName(int)
     */
    public String getColumnTypeName( int index ) throws DataException
    {
        return DataType.getName( getColumnType( index ) );
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnLabel(int)
     */
    public String getColumnLabel( int index ) throws DataException
    {
        try
        {
            return m_odiResultClass.getFieldLabel( index );
        }
        catch ( DataException e )
        {
            throw e;
        }
    }

}
