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

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
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
        return m_odiResultClass.getFieldName( index );
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnAlias(int)
     */
    public String getColumnAlias( int index ) throws DataException
    {
    	return m_odiResultClass.getFieldAlias( index );
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnType(int)
     */
    public int getColumnType( int index ) throws DataException
    {
        Class odiDataType = m_odiResultClass.getFieldValueClass( index );
        return DataTypeUtil.toApiDataType( odiDataType );
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnTypeName(int)
     */
    public String getColumnTypeName( int index ) throws DataException
    {
        return DataType.getName( getColumnType( index ) );
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnNativeTypeName(int)
     */
	public String getColumnNativeTypeName( int index ) throws DataException
	{
		return m_odiResultClass.getFieldNativeTypeName( index );
	}

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnLabel(int)
     */
    public String getColumnLabel( int index ) throws DataException
    {
        return m_odiResultClass.getFieldLabel( index );
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IResultMetaData#isComputedColumn(int)
     */
	public boolean isComputedColumn( int index ) throws DataException
	{
	    return m_odiResultClass.isCustomField( index );
	}

}
