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
package org.eclipse.birt.report.data.oda.jdbc.ui.editors;

import java.sql.Types;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Constants;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ParameterMode;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;

/**
 * The utility class for SQLDataSetEditorPage
 *
 */
public class SQLUtility
{

	/**
	 * Get a unique name for dataset parameter
	 * @param parameters
	 * @return
	 */
	public static final String getUniqueName( DataSetParameters parameters )
	{
		int n = 1;
		String prefix = "param"; //$NON-NLS-1$
		StringBuffer buf = new StringBuffer( );
		while ( buf.length( ) == 0 )
		{
			buf.append( prefix ).append( n++ );
			if ( parameters != null )
			{
				Iterator iter = parameters.getParameterDefinitions( )
						.iterator( );
				if ( iter != null )
				{
					while ( iter.hasNext( ) && buf.length( ) > 0 )
					{
						ParameterDefinition parameter = (ParameterDefinition) iter.next( );
						if ( buf.toString( )
								.equalsIgnoreCase( parameter.getAttributes( )
										.getName( ) ) )
						{
							buf.setLength( 0 );
						}
					}
				}
			}
		}
		return buf.toString( );
	}

	/**
	 * save the dataset design's metadata info
	 * 
	 * @param design
	 */
	public static void saveDataSetDesign( DataSetDesign design )
	{
		// obtain query's result set metadata, and update
		// the dataSetDesign with it
		IConnection conn = null;
		try
		{
			IDriver jdbcDriver = new OdaJdbcDriver( );
			conn = jdbcDriver.getConnection( null );
			java.util.Properties prop = new java.util.Properties( );
			DataSourceDesign dataSourceDesign = design.getDataSourceDesign( );
			if ( dataSourceDesign != null )
			{
				prop.put( Constants.ODADriverClass,
						dataSourceDesign.getPublicProperties( )
								.getProperty( Constants.ODADriverClass ) == null
								? "" : dataSourceDesign.getPublicProperties( )
										.getProperty( Constants.ODADriverClass ) );
				prop.put( Constants.ODAURL,
						dataSourceDesign.getPublicProperties( )
								.getProperty( Constants.ODAURL ) == null ? ""
								: dataSourceDesign.getPublicProperties( )
										.getProperty( Constants.ODAURL ) );
				prop.put( Constants.ODAUser,
						dataSourceDesign.getPublicProperties( )
								.getProperty( Constants.ODAUser ) == null ? ""
								: dataSourceDesign.getPublicProperties( )
										.getProperty( Constants.ODAUser ) );
				prop.put( Constants.ODAPassword,
						dataSourceDesign.getPublicProperties( )
								.getProperty( Constants.ODAPassword ) == null
								? "" : dataSourceDesign.getPublicProperties( )
										.getProperty( Constants.ODAPassword ) );
			}
			conn.open( prop );
			IQuery query = conn.newQuery( design.getOdaExtensionDataSetId( ) );
			query.setMaxRows( 1 );
			query.prepare( design.getQueryText( ) );

			setParameterMetaData( design, query );

			// set resultset metadata
			IResultSetMetaData metadata = query.getMetaData( );
			if ( metadata != null )
				setResultSetMetaData( design, metadata );
			else
			{
				query.executeQuery( );
				metadata = query.getMetaData( );
				setResultSetMetaData( design, metadata );
			}

		}
		catch ( OdaException e )
		{
			// no result set definition available, reset in dataSetDesign
			design.setResultSets( null );
		}
		finally
		{
			if ( conn != null )
				try
				{
					conn.close( );
				}
				catch ( OdaException e )
				{
					e.printStackTrace( );
				};
		}
	}

	/**
	 * Set parameter metadata in dataset design
	 * 
	 * @param design
	 * @param query
	 */
	private static void setParameterMetaData( DataSetDesign dataSetDesign,
			IQuery query )
	{
		try
		{
			// set parameter metadata
			IParameterMetaData paramMetaData = query.getParameterMetaData( );
			mergeParameterMetaData( dataSetDesign, paramMetaData );
		}
		catch ( OdaException e )
		{
			dataSetDesign.setParameters( null );
		}
	}
	
	/**
	 * solve the BIDI line problem
	 * @param lineText
	 * @return
	 */
	public static int[] getBidiLineSegments( String lineText )
	{
		int[] seg = null;
		if ( lineText != null
				&& lineText.length( ) > 0
				&& !new Bidi( lineText, Bidi.DIRECTION_LEFT_TO_RIGHT ).isLeftToRight( ) )
		{
			List list = new ArrayList( );

			// Punctuations will be regarded as delimiter so that different
			// splits could be rendered separately.
			Object[] splits = lineText.split( "\\p{Punct}" );

			// !=, <> etc. leading to "" will be filtered to meet the rule that
			// segments must not have duplicates.
			for ( int i = 0; i < splits.length; i++ )
			{
				if ( !splits[i].equals( "" ) )
					list.add( splits[i] );
			}
			splits = list.toArray( );

			// first segment must be 0
			// last segment does not necessarily equal to line length
			seg = new int[splits.length + 1];
			for ( int i = 0; i < splits.length; i++ )
			{
				seg[i + 1] = lineText.indexOf( (String) splits[i], seg[i] )
						+ ( (String) splits[i] ).length( );
			}
		}

		return seg;
	}
    
	
	/**
	 * Return pre-defined query text pattern with every element in a cell.
	 * 
	 * @return pre-defined query text
	 */
	public static String getQueryPresetTextString( String extensionId )
	{
		String[] lines = getQueryPresetTextArray( extensionId );
		String result = "";
		if ( lines != null && lines.length > 0 )
		{
			for ( int i = 0; i < lines.length; i++ )
			{
				result = result
						+ lines[i] + ( i == lines.length - 1 ? " " : " \n" );
			}
		}
		return result;
	}
	
	
	/**
	 * Return pre-defined query text pattern with every element in a cell in an
	 * Array
	 * 
	 * @return pre-defined query text in an Array
	 */
	public static String[] getQueryPresetTextArray( String extensionId )
	{
		final String[] lines;
		if ( extensionId.equals( "org.eclipse.birt.report.data.oda.jdbc.SPSelectDataSet" ) )
			lines = new String[]{
				"{call procedure-name(arg1,arg2, ...)}"
			};
		else
			lines = new String[]{
					"select", "from"
			};
		return lines;
	}
	
	/**
	 * get the tailored serch text
	 * @param namePattern
	 * @return
	 */
	public static String getTailoredSearchText( String namePattern )
	{
		if ( namePattern != null )
		{
			if ( namePattern.lastIndexOf( '%' ) == -1 )
			{
				namePattern = namePattern + "%";
			}
		}
		else
		{
			namePattern = "%";
		}

		return namePattern;
	}
	
	/**
	 * Constuct the DnD string for the dragged object.
	 * 
	 * @param obj
	 * @return
	 */
	public static Object getDnDString( Object obj, String identifierStr,
			boolean useIdentifier )
	{
		if ( !useIdentifier || !( obj instanceof String ) )
			return obj;

		String identifierQuoteString = identifierStr;
		String dndString = (String) obj;

		if ( !identifierQuoteString.equals( " " ) )
		{
			if ( dndString.indexOf( "." ) == -1 )
				return identifierQuoteString
						+ dndString + identifierQuoteString;

			String[] str = dndString.split( "[.]" );
			dndString = "";

			for ( int i = 0; i < str.length; i++ )
			{
				dndString += identifierQuoteString
						+ str[i] + identifierQuoteString + ".";
			}
			return dndString.substring( 0, dndString.lastIndexOf( "." ) );
		}

		return dndString;
	}
	
    /**
	 * merge paramter meta data between dataParameter and datasetDesign's
	 * parameter.
	 * 
	 * @param dataSetDesign
	 * @param md
	 * @throws OdaException
	 */
	private static void mergeParameterMetaData( DataSetDesign dataSetDesign,
			IParameterMetaData md ) throws OdaException
	{
		if ( md == null || dataSetDesign == null )
			return;
		DataSetParameters dataSetParameter = DesignSessionUtil.toDataSetParametersDesign( md,
				ParameterMode.IN_LITERAL );

		if ( dataSetParameter != null )
		{
			Iterator iter = dataSetParameter.getParameterDefinitions( )
					.iterator( );
			while ( iter.hasNext( ) )
			{
				ParameterDefinition defn = (ParameterDefinition) iter.next( );
				proccessParamDefn( defn, dataSetParameter );
			}
		}
		dataSetDesign.setParameters( dataSetParameter );
	}

	/**
	 * Process the parameter definition for some special case
	 * 
	 * @param defn
	 * @param parameters
	 */
	private static void proccessParamDefn( ParameterDefinition defn,
			DataSetParameters parameters )
	{
		if ( defn.getAttributes( ).getName( ) == null
				|| defn.getAttributes( ).getName( ).trim( ).equals( "" ) )
			defn.getAttributes( )
					.setName( SQLUtility.getUniqueName( parameters ) );
		if ( defn.getAttributes( ).getNativeDataTypeCode( ) == Types.NULL )
		{
			defn.getAttributes( ).setNativeDataTypeCode( Types.CHAR );
		}
	}

	/**
	 * Set the resultset metadata in dataset design
	 * 
	 * @param dataSetDesign
	 * @param md
	 * @throws OdaException
	 */
	private static void setResultSetMetaData( DataSetDesign dataSetDesign,
			IResultSetMetaData md ) throws OdaException
	{

		ResultSetColumns columns = DesignSessionUtil.toResultSetColumnsDesign( md );

		if ( columns != null )
		{
			ResultSetDefinition resultSetDefn = DesignFactory.eINSTANCE.createResultSetDefinition( );
			// jdbc does not support result set name
			resultSetDefn.setResultSetColumns( columns );
			// no exception; go ahead and assign to specified dataSetDesign
			dataSetDesign.setPrimaryResultSet( resultSetDefn );
			dataSetDesign.getResultSets( ).setDerivedMetaData( true );
		}
		else
		{
			dataSetDesign.setResultSets( null );
		}
	}
}
