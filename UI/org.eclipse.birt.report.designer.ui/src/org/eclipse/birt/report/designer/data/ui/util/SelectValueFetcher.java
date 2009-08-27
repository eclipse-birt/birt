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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
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
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.datatools.connectivity.oda.IBlob;

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
		try
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

			Collection result = null;
			DataRequestSession session = null;
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
								? null : dataSetHandle.getModuleHandle( ) ) ),
						dataSetHandle.getModuleHandle( ) );

				session = engineTask.getDataSession( );

				engineTask.run( );

				result = session.getColumnValueSet( dataSetHandle,
						dataSetHandle.getPropertyHandle( DataSetHandle.PARAMETERS_PROP )
								.iterator( ),
						null,
						columnName );

				engineTask.close( );
				engine.destroy( );
			}
			else
			{
				session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
						dataSetHandle == null ? null
								: dataSetHandle.getModuleHandle( ) ) );

				result = session.getColumnValueSet( dataSetHandle,
						dataSetHandle.getPropertyHandle( DataSetHandle.PARAMETERS_PROP )
								.iterator( ),
						null,
						columnName );
				session.shutdown( );
			}

			assert result != null;
			if ( result.isEmpty( ) )
				return Collections.EMPTY_LIST;

			Object resultProtoType = result.iterator( ).next( );
			if ( resultProtoType instanceof IBlob
					|| resultProtoType instanceof byte[] )
				return Collections.EMPTY_LIST;

			return new ArrayList( result );
		}
		catch ( Exception e )
		{
			throw new BirtException( e.getMessage( ), e );
		}
	}

	public static List getSelectValueFromBinding( String expression,
			DataSetHandle dataSetHandle, Iterator binding, boolean inclFilter )
			throws BirtException
	{
		try
		{
			boolean startsWithRow = ExpressionUtility.isColumnExpression( expression,
					true );
			boolean startsWithDataSetRow = ExpressionUtility.isColumnExpression( expression,
					false );

			List bindingList = null;
			String columnName = null;
			if ( startsWithDataSetRow )
			{
				columnName = ExpressionUtil.getColumnName( expression );
			}
			else if ( startsWithRow )
			{
				columnName = ExpressionUtil.getColumnBindingName( expression );
			}
			else
			{
				bindingList = new ArrayList( );

				while ( binding.hasNext( ) )
				{
					bindingList.add( binding.next( ) );
				}
				ComputedColumn handle = new ComputedColumn( );
				columnName = "TEMP_" + expression;
				handle.setExpression( expression );
				handle.setName( columnName );
				bindingList.add( handle );
			}

			Collection result = null;
			DataRequestSession session = null;
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
								? null : dataSetHandle.getModuleHandle( ) ) ),
						dataSetHandle.getModuleHandle( ) );

				session = engineTask.getDataSession( );

				engineTask.run( );
				result = session.getColumnValueSet( dataSetHandle,
						dataSetHandle.getPropertyHandle( DataSetHandle.PARAMETERS_PROP )
								.iterator( ),
						bindingList == null ? binding : bindingList.iterator( ),
						columnName );

				engineTask.close( );
				engine.destroy( );
			}
			else
			{
				session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
						dataSetHandle == null ? null
								: dataSetHandle.getModuleHandle( ) ) );

				result = session.getColumnValueSet( dataSetHandle,
						dataSetHandle.getPropertyHandle( DataSetHandle.PARAMETERS_PROP )
								.iterator( ),
						bindingList == null ? binding : bindingList.iterator( ),
						columnName );
				session.shutdown( );
			}

			assert result != null;
			if ( result.isEmpty( ) )
				return Collections.EMPTY_LIST;

			Object resultProtoType = result.iterator( ).next( );
			if ( resultProtoType instanceof IBlob
					|| resultProtoType instanceof byte[] )
				return Collections.EMPTY_LIST;

			return new ArrayList( result );
		}
		catch ( Exception e )
		{
			throw new BirtException( e.getMessage( ), e );
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