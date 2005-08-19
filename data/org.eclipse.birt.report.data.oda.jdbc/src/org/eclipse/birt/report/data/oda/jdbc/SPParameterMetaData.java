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

package org.eclipse.birt.report.data.oda.jdbc;

import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.data.oda.IParameterMetaData;
import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;

/**
 * 
 * This class implements the org.eclipse.birt.data.oda.IParameterMetaData
 * interface.
 *  
 */
 
public class SPParameterMetaData implements IParameterMetaData
{

	/** JDBC ParameterMetaData instance */
	private java.util.List paramMetadataList;

	private static Logger logger = Logger.getLogger( SPParameterMetaData.class.getName( ) );	

	/**
	 * assertNotNull(Object o)
	 * 
	 * @param o
	 *            the object that need to be tested null or not. if null, throw
	 *            exception
	 */
	private void assertNotNull( Object o ) throws OdaException
	{
		if ( o == null )
		{
			throw new JDBCException( ResourceConstants.DRIVER_NO_PARAMETERMETADATA,
					ResourceConstants.ERROR_NO_PARAMETERMETADATA );

		}
	}

	/**
	 * 
	 * Constructor ParameterMetaData(java.sql.ParameterMetaData paraMeta) use
	 * JDBC's ParameterMetaData to construct it.
	 *  
	 */
	public SPParameterMetaData( List paraMetadataList )
			throws OdaException
	{
		this.paramMetadataList = paraMetadataList;
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IParameterMetaData#getParameterCount()
	 */
	public int getParameterCount( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				SPParameterMetaData.class.getName( ),
				"getParameterCount",
				"SPParameterMetaData.getParameterCount( )" );
		assertNotNull( paramMetadataList );
		return paramMetadataList.size();
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IParameterMetaData#getParameterMode(int)
	 */
	public int getParameterMode( int param ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				SPParameterMetaData.class.getName( ),
				"getParameterMode",
				"SPParameterMetaData.getParameterMode( )" );
		assertNotNull( paramMetadataList );
		int result = IParameterMetaData.parameterModeUnknown;
		ParameterDefn paramDefn = (ParameterDefn) paramMetadataList.get( param -1 );
		if ( paramDefn.getParamInOutType( ) == java.sql.ParameterMetaData.parameterModeIn )
			result = IParameterMetaData.parameterModeIn;
		else if ( paramDefn.getParamInOutType( ) == java.sql.ParameterMetaData.parameterModeOut )
			result = IParameterMetaData.parameterModeOut;
		else if ( paramDefn.getParamInOutType( ) == java.sql.ParameterMetaData.parameterModeInOut )
			result = IParameterMetaData.parameterModeInOut;
		return result;
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IParameterMetaData#getParameterType(int)
	 */
	public int getParameterType( int param ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				SPParameterMetaData.class.getName( ),
				"getParameterType",
				"SPParameterMetaData.getParameterType( )" );
		assertNotNull( paramMetadataList );
		ParameterDefn paramDefn = (ParameterDefn) paramMetadataList.get( param -1 );
		return paramDefn.getParamType( );
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IParameterMetaData#getParameterTypeName(int)
	 */
	public String getParameterTypeName( int param ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				SPParameterMetaData.class.getName( ),
				"getParameterTypeName",
				"SPParameterMetaData.getParameterTypeName( )" );
		assertNotNull( paramMetadataList );
		ParameterDefn paramDefn = (ParameterDefn) paramMetadataList.get( param -1 );
		return paramDefn.getParamTypeName();
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IParameterMetaData#getPrecision(int)
	 */
	public int getPrecision( int param ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				SPParameterMetaData.class.getName( ),
				"getPrecision",
				"SPParameterMetaData.getPrecision( )" );
		assertNotNull( paramMetadataList );
		ParameterDefn paramDefn = (ParameterDefn) paramMetadataList.get( param -1 );
		return paramDefn.getPrecision( );
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IParameterMetaData#getScale(int)
	 */
	public int getScale( int param ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				SPParameterMetaData.class.getName( ),
				"getScale",
				"SPParameterMetaData.getScale( )" );
		assertNotNull( paramMetadataList );
		ParameterDefn paramDefn = (ParameterDefn) paramMetadataList.get( param -1);
		return paramDefn.getScale();
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IParameterMetaData#isNullable(int)
	 */
	public int isNullable( int param ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				SPParameterMetaData.class.getName( ),
				"isNullable",
				"SPParameterMetaData.isNullable( )" );
		assertNotNull( paramMetadataList );
		int result = IParameterMetaData.parameterNullableUnknown;
		ParameterDefn paramDefn = (ParameterDefn) paramMetadataList.get( param -1 );

		if ( paramDefn.getIsNullable( ) == java.sql.ParameterMetaData.parameterNullable )
			result = IParameterMetaData.parameterNullable;
		else if ( paramDefn.getIsNullable( ) == java.sql.ParameterMetaData.parameterNoNulls )
			result = IParameterMetaData.parameterNoNulls;
		return result;
	}
}