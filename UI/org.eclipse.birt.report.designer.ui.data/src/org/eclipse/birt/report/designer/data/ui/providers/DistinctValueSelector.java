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

package org.eclipse.birt.report.designer.data.ui.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.data.ui.dataset.ExternalUIUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataSetProvider;
import org.eclipse.birt.report.designer.data.ui.util.DummyEngineTask;
import org.eclipse.birt.report.designer.data.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportEngineFactory;
import org.eclipse.birt.report.engine.api.impl.ReportEngineHelper;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.datatools.connectivity.oda.IBlob;

/**
 * Utility class to fetch all available value for filter use.
 * 
 */
public class DistinctValueSelector
{
	/**
	 * private constructor
	 */
	private DistinctValueSelector( )
	{
	}

	/**
	 * Used in the filter select value dialog in dataset editor
	 * 
	 * @param expression
	 * @param dataSetHandle
	 * @param binding
	 * @param useDataSetFilter
	 * @return
	 * @throws BirtException
	 */
	public static List getSelectValueList( Expression expression,
			DataSetHandle dataSetHandle, boolean useDataSetFilter )
			throws BirtException
	{
		DataRequestSession session = null;
		ScriptExpression expr = null;
		ReportEngine engine = null;
		DummyEngineTask engineTask = null;
		try
		{

			if ( dataSetHandle.getModuleHandle( ) instanceof ReportDesignHandle )
			{
				ReportDesignHandle copy = (ReportDesignHandle) ( dataSetHandle.getModuleHandle( )
						.copy( ).getHandle( null ) );
				
				EngineConfig config = new EngineConfig( );

				config.setProperty( EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
						DataSetProvider.getCustomScriptClassLoader( Thread.currentThread( )
								.getContextClassLoader( ),
								copy ) );
				engine = (ReportEngine) new ReportEngineFactory( ).createReportEngine( config );

				engineTask = new DummyEngineTask( engine,
						new ReportEngineHelper( engine ).openReportDesign( (ReportDesignHandle) copy ),
						copy );
				session = engineTask.getDataSession( );
				
				engineTask.run( );

				expr = session.getModelAdaptor( ).adaptExpression( expression );
			}
			else
			{
				session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
						dataSetHandle == null ? null
								: dataSetHandle.getModuleHandle( ) ) );
				expr = session.getModelAdaptor( ).adaptExpression( expression );
			}

			boolean startsWithRow = ExpressionUtility.isColumnExpression( expr.getText( ),
					true );
			boolean startsWithDataSetRow = ExpressionUtility.isColumnExpression( expr.getText( ),
					false );
			if ( !startsWithRow && !startsWithDataSetRow )
			{
				throw new DataException( Messages.getString( "SelectValueDialog.messages.info.invalidSelectVauleExpression" ) ); //$NON-NLS-1$
			}

			String dataSetColumnName = null;
			if ( startsWithDataSetRow )
			{
				dataSetColumnName = ExpressionUtil.getColumnName( expr.getText( ) );
			}
			else
			{
				dataSetColumnName = ExpressionUtil.getColumnBindingName( expr.getText( ) );
			}

			List bindingList = new ArrayList( );
			ComputedColumn handle = new ComputedColumn( );
			String columnName = "TEMP_" + expression.getStringExpression( );
			handle.setExpression( ExpressionUtil.createJSDataSetRowExpression( dataSetColumnName ) );
			handle.setName( columnName );
			bindingList.add( handle );
			
			ExternalUIUtil.populateApplicationContext( dataSetHandle, session );

			Collection result = session.getColumnValueSet( dataSetHandle,
					dataSetHandle.getPropertyHandle( DataSetHandle.PARAMETERS_PROP )
							.iterator( ),
					bindingList.iterator( ),
					null,
					columnName,
					useDataSetFilter,
					null );

			assert result != null;
			if ( result.isEmpty( ) )
				return Collections.EMPTY_LIST;

			Object resultProtoType = result.iterator( ).next( );
			if ( resultProtoType instanceof IBlob
					|| resultProtoType instanceof byte[] )
				return Collections.EMPTY_LIST;

			return new ArrayList( result );
		}
		finally
		{
			if ( dataSetHandle.getModuleHandle( ) instanceof ReportDesignHandle )
			{
				if ( engineTask != null )
				{
					engineTask.close( );
				}
				if ( engine != null )
				{
					engine.destroy( );
				}
			}
			else
			{
				if ( session != null )
				{
					session.shutdown( );
				}
			}
		}
	}
	
	/**
	 * Used in filter select value dialog in layout with group definition.
	 * 
	 * @param expression
	 * @param dataSetHandle
	 * @param binding The iterator of ComputedColumnHandle
	 * @param groupIterator The iterator of GroupHandle
	 * @param useDataSetFilter
	 * @return
	 * @throws BirtException
	 */
	public static List getSelectValueFromBinding( Expression expression,
			DataSetHandle dataSetHandle, Iterator binding,
			Iterator groupIterator, boolean useDataSetFilter )
			throws BirtException
	{
		String columnName = null;
		List bindingList = new ArrayList( );

		if ( binding != null && binding.hasNext( ) )
		{
			while ( binding.hasNext( ) )
			{
				bindingList.add( binding.next( ) );
			}
		}
		ComputedColumn handle = new ComputedColumn( );
		columnName = "TEMP_" + expression.getStringExpression( );
		handle.setExpressionProperty( ComputedColumn.EXPRESSION_MEMBER,
				expression );
		handle.setName( columnName );
		bindingList.add( handle );

		Collection result = null;
		DataRequestSession session = null;
		if ( dataSetHandle.getModuleHandle( ) instanceof ReportDesignHandle )
		{
			EngineConfig config = new EngineConfig( );

			ReportDesignHandle copy = (ReportDesignHandle) ( dataSetHandle.getModuleHandle( )
					.copy( ).getHandle( null ) );

			config.setProperty( EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
					DataSetProvider.getCustomScriptClassLoader( Thread.currentThread( )
							.getContextClassLoader( ),
							copy ) );

			ReportEngine engine = (ReportEngine) new ReportEngineFactory( ).createReportEngine( config );

			DummyEngineTask engineTask = new DummyEngineTask( engine,
					new ReportEngineHelper( engine ).openReportDesign( (ReportDesignHandle) copy ),
					copy );

			session = engineTask.getDataSession( );
			ExternalUIUtil.populateApplicationContext( dataSetHandle, session );
			
			engineTask.run( );
			result = session.getColumnValueSet( dataSetHandle,
					dataSetHandle.getPropertyHandle( DataSetHandle.PARAMETERS_PROP )
							.iterator( ),
					bindingList.iterator( ),
					groupIterator,
					columnName,
					useDataSetFilter,
					null );

			engineTask.close( );
			engine.destroy( );
		}
		else
		{
			session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
					dataSetHandle == null ? null
							: dataSetHandle.getModuleHandle( ) ) );
			ExternalUIUtil.populateApplicationContext( dataSetHandle, session );

			result = session.getColumnValueSet( dataSetHandle,
					dataSetHandle.getPropertyHandle( DataSetHandle.PARAMETERS_PROP )
							.iterator( ),
					bindingList.iterator( ),
					null,
					columnName,
					useDataSetFilter,
					null );
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
}