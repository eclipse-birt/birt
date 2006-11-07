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

import java.sql.Types;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 *
 */
public class TestParamMetaDataImpl implements IParameterMetaData
{
    public TestParamMetaDataImpl()
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterCount()
     */
    public int getParameterCount() throws OdaException
    {
        return 3;
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterMode(int)
     */
    public int getParameterMode( int param ) throws OdaException
    {
        return ( param <= getParameterCount() ) ?
                IParameterMetaData.parameterModeOut :
                IParameterMetaData.parameterModeUnknown;
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterType(int)
     */
    public int getParameterType( int param ) throws OdaException
    {        
        switch( param )
        {
        	case 1: return Types.VARCHAR;
        	case 2: return Types.DATE;
        	case 3: return Types.BOOLEAN;	// not mapped in plugin.xml
        	default: return Types.NULL;
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterTypeName(int)
     */
    public String getParameterTypeName( int param ) throws OdaException
    {
        switch( param )
        {
        	case 1: return "String";
        	case 2: return "Date";
        	default: return "Unknown type";
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IParameterMetaData#getPrecision(int)
     */
    public int getPrecision( int param ) throws OdaException
    {
        return -1;
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IParameterMetaData#getScale(int)
     */
    public int getScale( int param ) throws OdaException
    {
        return -1;
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IParameterMetaData#isNullable(int)
     */
    public int isNullable( int param ) throws OdaException
    {
        return IParameterMetaData.parameterNullableUnknown;
    }

}
