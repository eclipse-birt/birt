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

package org.eclipse.birt.report.designer.data.ui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.JointDataSetDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportEngineFactory;
import org.eclipse.birt.report.engine.api.impl.ReportEngineHelper;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * Utility class to fetch all available value for filter use.
 * 
 */
public class SelectValueFetcher
{
	/**
	 * private constructor
	 */
	private SelectValueFetcher( )
	{
	}
	
	public static List getSelectValueList( String expression,
			DataSetHandle dataSetHandle, boolean inclFilter )
			throws BirtException
	{

		boolean startsWithRow = ExpressionUtility.isColumnExpression( expression,
				true );
		boolean startsWithDataSetRow = ExpressionUtility.isColumnExpression( expression,
				false );
		if ( !startsWithRow && !startsWithDataSetRow )
		{
			throw new DataException( Messages.getString( "SelectValueDialog.messages.info.invalidSelectVauleExpression" ) ); //$NON-NLS-1$
		}

		String columnName = null;
		if ( startsWithDataSetRow )
		{
			columnName = ExpressionUtil.getColumnName( expression );
		}
		else
		{
			columnName = ExpressionUtil.getColumnBindingName( expression );
		}

		if ( dataSetHandle.getModuleHandle( ) instanceof ReportDesignHandle )
		{
			EngineConfig config = new EngineConfig( );

			config.setProperty( EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
					DataSetProvider.getCustomScriptClassLoader( Thread.currentThread( )
							.getContextClassLoader( ),
							dataSetHandle.getModuleHandle( ) ) );

			ReportEngine engine = (ReportEngine) new ReportEngineFactory( ).createReportEngine( config );

			DummyEngineTask engineTask = new DummyEngineTask( engine,
					new ReportEngineHelper( engine ).openReportDesign( (ReportDesignHandle) ( dataSetHandle == null
							? null : dataSetHandle.getModuleHandle( ) ) ) );

			DataRequestSession session = engineTask.getDataSession( );

			engineTask.run( );

			List selectValueList = new ArrayList( );

			Collection result = session.getColumnValueSet( dataSetHandle,
					dataSetHandle.getPropertyHandle( DataSetHandle.PARAMETERS_PROP )
							.iterator( ),
					null,
					columnName );

			engineTask.close( );
			engine.destroy( );
			return new ArrayList( result );
		}
		else
		{

			List selectValueList = new ArrayList( );

			DataRequestSession session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
					dataSetHandle == null ? null
							: dataSetHandle.getModuleHandle( ) ) );

			Collection result = session.getColumnValueSet( dataSetHandle,
					dataSetHandle.getPropertyHandle( DataSetHandle.PARAMETERS_PROP )
							.iterator( ),
					null,
					columnName );

			return new ArrayList( result );

		}
	}
	
	/**
	 * A private method that helps to define all the children data sets'
	 * data sources and data sets themselves
	 * 
	 * @param session
	 * @param dataSet
	 * @param dataSetDesign
	 * @param inclFilter
	 * 
	 * @throws BirtException
	 */
	private static void defineSourceAndDataSets( DataRequestSession session,
			DataSetHandle dataSet, BaseDataSetDesign dataSetDesign,
			boolean inclFilter ) throws BirtException
	{
		if ( dataSet.getDataSource( ) != null )
		{
			session.defineDataSource( session.getModelAdaptor( ).adaptDataSource( dataSet.getDataSource( ) ) );
		}
		if ( !inclFilter )
		{
			if ( dataSetDesign.getFilters( ) != null )
			{
				dataSetDesign.getFilters( ).clear( );
			}
		}
		session.defineDataSet( dataSetDesign );
		if ( dataSetDesign instanceof JointDataSetDesign )
		{
			defineChildDSetAndDSource( session,
					(JointDataSetDesign) dataSetDesign,
					dataSet.getModuleHandle( ),
					inclFilter,
					true );

			defineChildDSetAndDSource( session,
					(JointDataSetDesign) dataSetDesign,
					dataSet.getModuleHandle( ),
					inclFilter,
					false );
		}
	}

	/**
	 * To help to define the children data sets of a JointDataSet
	 * 
	 * @param session
	 * @param dataSetDesign
	 * @param modulehandle
	 * @param modelAdapter
	 * @param inclFilter
	 * @param isLeft
	 * 
	 * @throws BirtException
	 */
	private static void defineChildDSetAndDSource( DataRequestSession session,
			JointDataSetDesign dataSetDesign, ModuleHandle modulehandle,
			boolean inclFilter, boolean isLeft ) throws BirtException
	{
		DataSetHandle dataSetHandle;
		if ( isLeft )
		{
			dataSetHandle = modulehandle.findDataSet( dataSetDesign.getLeftDataSetDesignName( ) );
		}
		else
		{
			dataSetHandle = modulehandle.findDataSet( dataSetDesign.getRightDataSetDesignName( ) );
		}
		defineSourceAndDataSets( session, dataSetHandle, session.getModelAdaptor( )
				.adaptDataSet( dataSetHandle ), inclFilter );
	}
	
	/**
	 * A help method to get the referenced column name of the given expression
	 * 
	 * @param expression
	 * @return
	 */
	private static String getReferenceColumnName( String expression, boolean mode )
	{
		try
		{
			List columnValue = null;
			columnValue = ExpressionUtil.extractColumnExpressions( expression,
					mode );
			if ( columnValue == null || columnValue.size( ) == 0 )
			{
				if ( columnValue == null || columnValue.size( ) == 0 )
				{
					return null;
				}
			}
			return ( (IColumnBinding) columnValue.get( 0 ) ).getResultSetColumnName( );
		}
		catch ( BirtException e )
		{
			return null;
		}
	}

	/**
	 * 
	 * @param selectValueExpression
	 * @param dataSetHandle
	 * @return
	 * @throws BirtException
	 */
	public static List getSelectValueList( String expression,
			DataSetHandle dataSetHandle ) throws BirtException
	{
		return getSelectValueList( expression, dataSetHandle, true );
	}
}