/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.reportitem.AbstractChartBaseQueryGenerator;
import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.GroupedQueryResultSetEvaluatorAdapter;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.metadata.PredefinedStyle;

import com.ibm.icu.text.Collator;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;

/**
 * Data service provider of chart builder for BIRT integration.
 */

public class ReportDataServiceProvider implements IDataServiceProvider
{

	private ExtendedItemHandle itemHandle;

	private static final String OPTION_NONE = Messages.getString( "ReportDataServiceProvider.Option.None" ); //$NON-NLS-1$

	/**
	 * This flag indicates whether the error is found when fetching data. This
	 * is to help reduce invalid query.
	 */
	private boolean isErrorFound = false;

	public ReportDataServiceProvider( ExtendedItemHandle itemHandle )
	{
		super( );
		this.itemHandle = itemHandle;
	}

	private ModuleHandle getReportDesignHandle( )
	{
		return itemHandle.getModuleHandle( );
	}

	public String[] getAllDataSets( )
	{
		List list = getReportDesignHandle( ).getVisibleDataSets( );
		String[] names = new String[list.size( ) + 1];
		names[0] = OPTION_NONE;
		for ( int i = 0; i < list.size( ); i++ )
		{
			names[i + 1] = ( (DataSetHandle) list.get( i ) ).getQualifiedName( );
		}
		return names;
	}

	public final String[] getPreviewHeader( ) throws ChartException
	{
		Iterator iterator = ChartReportItemUtil.getColumnDataBindings( itemHandle );
		ArrayList list = new ArrayList( );
		while ( iterator.hasNext( ) )
		{
			list.add( ( (ComputedColumnHandle) iterator.next( ) ).getName( ) );
		}
		return (String[]) list.toArray( new String[list.size( )] );
	}

	public final List getPreviewData( ) throws ChartException
	{
		return getPreviewRowData( getPreviewHeader( true ), -1, true );
	}

	private String[] getPreviewHeader( boolean isExpression )
			throws ChartException
	{
		String[] exps = getPreviewHeader( );
		if ( isExpression )
		{
			for ( int i = 0; i < exps.length; i++ )
			{
				exps[i] = ChartUIUtil.getExpressionString( exps[i] );
			}
		}
		return exps;
	}

	protected final List getPreviewRowData( String[] columnExpression,
			int rowCount, boolean isStringType ) throws ChartException
	{
		ArrayList dataList = new ArrayList( );

		// Set thread context class loader so Rhino can find POJOs in workspace
		// projects
		ClassLoader oldContextLoader = Thread.currentThread( )
				.getContextClassLoader( );
		ClassLoader parentLoader = oldContextLoader;
		if ( parentLoader == null )
			parentLoader = this.getClass( ).getClassLoader( );
		ClassLoader newContextLoader = getCustomScriptClassLoader( parentLoader );
		Thread.currentThread( ).setContextClassLoader( newContextLoader );

		try
		{
			QueryDefinition queryDefn = new QueryDefinition( );
			int maxRow = getMaxRow( );
			queryDefn.setMaxRows( maxRow );
			queryDefn.setDataSetName( getDataSetFromHandle( ).getQualifiedName( ) );

			DataRequestSession session = prepareDataRequestSession( maxRow );
			for ( int i = 0; i < columnExpression.length; i++ )
			{
				queryDefn.addResultSetExpression( columnExpression[i],
						new ScriptExpression( columnExpression[i] ) );
			}

			// Iterate parameter bindings to check if its expression is a
			// explicit
			// value, otherwise use default value of parameter as its
			// expression.
			resetParametersForDataPreview( getDataSetFromHandle( ), queryDefn );

			IQueryResults actualResultSet = session.executeQuery( queryDefn,
					null,
					itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP )
							.iterator( ),
							ChartReportItemUtil.getColumnDataBindings( itemHandle ) );
			if ( actualResultSet != null )
			{
				String[] expressions = columnExpression;
				int columnCount = expressions.length;
				IResultIterator iter = actualResultSet.getResultIterator( );
				while ( iter.next( ) )
				{
					if ( isStringType )
					{
						String[] record = new String[columnCount];
						for ( int n = 0; n < columnCount; n++ )
						{
							// Bugzilla#190229, to get string with localized
							// format
							record[n] = DataTypeUtil.toString( iter.getValue( expressions[n] ) );
						}
						dataList.add( record );
					}
					else
					{
						Object[] record = new Object[columnCount];
						for ( int n = 0; n < columnCount; n++ )
						{
							record[n] = iter.getValue( expressions[n] );
						}
						dataList.add( record );
					}
				}

				actualResultSet.close( );
			}
		}
		catch ( BirtException e )
		{
			throw new ChartException( ChartReportItemUIActivator.ID,
					ChartException.DATA_BINDING,
					e );
		}
		finally
		{
			// Restore old thread context class loader
			Thread.currentThread( ).setContextClassLoader( oldContextLoader );
		}
		return dataList;
	}

	/**
	 * Check if current parameters is explicit value in chart builder, otherwise
	 * default value of parameters will be used to get preview data.
	 * 
	 * @param dataSetHandle
	 *            current dataset handle.
	 * @param queryDefn
	 *            query definition.
	 * @return query definition.
	 */
	private QueryDefinition resetParametersForDataPreview(
			DataSetHandle dataSetHandle, QueryDefinition queryDefn )
	{
		// 1. Get parameters description from dataset.
		Iterator iterParams = dataSetHandle.parametersIterator( );
		Map dsphMap = new LinkedHashMap( );
		for ( ; iterParams.hasNext( ); )
		{
			DataSetParameterHandle dsph = (DataSetParameterHandle) iterParams.next( );
			dsphMap.put( dsph.getName( ), dsph );
		}

		// 2. Get parameters setting from current handle.
		iterParams = itemHandle.getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP )
				.iterator( );
		Map pbhMap = new LinkedHashMap( );
		for ( ; iterParams.hasNext( ); )
		{
			ParamBindingHandle paramBindingHandle = (ParamBindingHandle) iterParams.next( );
			pbhMap.put( paramBindingHandle.getParamName( ), paramBindingHandle );
		}

		// 3. Check and reset parameters expression.
		Object[] names = pbhMap.keySet( ).toArray( );
		for ( int i = 0; i < names.length; i++ )
		{
			DataSetParameterHandle dsph = (DataSetParameterHandle) dsphMap.get( names[i] );
			ScriptExpression se = null;
			String expr = ( (ParamBindingHandle) pbhMap.get( names[i] ) ).getExpression( );
			// The parameters must be a explicit value, the 'row[]' and
			// 'row.' expressions only be converted to a explicit value
			// under runtime case. So use default value of parameter to get
			// data in Chart Builder.
			if ( dsph.getDefaultValue( ) != null
					&& ( expr == null || expr.indexOf( "row[" ) >= 0 || expr.indexOf( "row." ) >= 0 ) ) //$NON-NLS-1$ //$NON-NLS-2$
			{
				se = new ScriptExpression( dsph.getDefaultValue( ) );
			}
			else
			{
				se = new ScriptExpression( expr );
			}

			InputParameterBinding ipb = new InputParameterBinding( (String) names[i],
					se );
			queryDefn.addInputParamBinding( ipb );
		}

		return queryDefn;
	}

	public String getBoundDataSet( )
	{
		if ( itemHandle.getDataSet( ) == null )
		{
			return null;
		}
		return itemHandle.getDataSet( ).getQualifiedName( );
	}

	public String getReportDataSet( )
	{
		List list = DEUtil.getDataSetList( itemHandle.getContainer( ) );
		if ( list.size( ) > 0 )
		{
			return ( (DataSetHandle) list.get( 0 ) ).getQualifiedName( );
		}
		return null;
	}

	public void setContext( Object context )
	{
		itemHandle = (ExtendedItemHandle) context;
	}

	public void setDataSet( String datasetName )
	{
		boolean needClean = false;
		boolean needAddBinding = false;
		try
		{
			// Clean references if it's set
			if ( itemHandle.getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			{
				itemHandle.setDataBindingReference( null );
			}

			if ( datasetName == null )
			{
				if ( getBoundDataSet( ) != null )
				{
					// Clean old bindings and use container's binding
					needClean = true;
				}
				itemHandle.setDataSet( null );
			}
			else
			{
				DataSetHandle dataset = getReportDesignHandle( ).findDataSet( datasetName );
				if ( itemHandle.getDataSet( ) != dataset )
				{
					itemHandle.setDataSet( dataset );

					// Clean old bindings and add new bindings
					needClean = true;
					needAddBinding = true;
				}
			}
			if ( needClean )
			{
				// Clear parameters and filters, binding if dataset changed
				clearBindings( );
			}

			if ( needAddBinding )
			{
				// Add binding from current dataset
				generateBindings( );
			}
		}
		catch ( SemanticException e )
		{
			ChartWizard.showException( e.getLocalizedMessage( ) );
		}
	}

	private void clearBindings( ) throws SemanticException
	{
		itemHandle.getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP )
				.clearValue( );
		itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP )
				.clearValue( );
		itemHandle.getColumnBindings( ).clearValue( );
	}

	private void generateBindings( ) throws SemanticException
	{
		List columnList = generateComputedColumns( itemHandle );
		if ( columnList.size( ) > 0 )
		{
			for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
			{
				DEUtil.addColumn( itemHandle,
						(ComputedColumn) iter.next( ),
						false );
			}
		}
	}

	/**
	 * Generate computed columns for the given report item with the closest data
	 * set available.
	 * 
	 * @param handle
	 *            the handle of the report item. No aggregation created.
	 * 
	 * @return true if succeed,or fail if no column generated.
	 * @see DataUtil#generateComputedColumns(ReportItemHandle)
	 * 
	 */
	private List generateComputedColumns( ReportItemHandle handle )
			throws SemanticException
	{
		DataSetHandle dataSetHandle = handle.getDataSet( );
		if ( dataSetHandle == null )
		{
			dataSetHandle = DEUtil.getBindingHolder( handle ).getDataSet( );
		}
		if ( dataSetHandle != null )
		{
			List resultSetColumnList = DataUtil.getColumnList( dataSetHandle );
			ArrayList columnList = new ArrayList( );
			for ( Iterator iter = resultSetColumnList.iterator( ); iter.hasNext( ); )
			{
				ResultSetColumnHandle resultSetColumn = (ResultSetColumnHandle) iter.next( );
				ComputedColumn column = StructureFactory.newComputedColumn( handle,
						resultSetColumn.getColumnName( ) );
				column.setDataType( resultSetColumn.getDataType( ) );
				column.setExpression( DEUtil.getExpression( resultSetColumn ) );
				columnList.add( column );
			}
			return columnList;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * Gets dataset from ReportItemHandle at first. If null, get dataset from
	 * its container.
	 * 
	 * @return direct dataset
	 */
	protected DataSetHandle getDataSetFromHandle( )
	{
		if ( itemHandle.getDataSet( ) != null )
		{
			return itemHandle.getDataSet( );
		}
		List datasetList = DEUtil.getDataSetList( itemHandle.getContainer( ) );
		if ( datasetList.size( ) > 0 )
		{
			return (DataSetHandle) datasetList.get( 0 );
		}
		return null;
	}

	private StyleHandle[] getAllStyleHandles( )
	{
		List sLst = getReportDesignHandle( ).getAllStyles( );
		StyleHandle[] list = (StyleHandle[]) sLst.toArray( new StyleHandle[sLst.size( )] );
		Arrays.sort( list, new Comparator( ) {

			Collator collator = Collator.getInstance( ULocale.getDefault( ) );

			public int compare( Object o1, Object o2 )
			{
				StyleHandle s1 = (StyleHandle) o1;
				StyleHandle s2 = (StyleHandle) o2;

				return collator.compare( s1.getDisplayLabel( ),
						s2.getDisplayLabel( ) );
			}

		} );
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#getAllStyles()
	 */
	public String[] getAllStyles( )
	{
		StyleHandle[] handles = getAllStyleHandles( );
		String[] names = new String[handles.length];
		for ( int i = 0; i < names.length; i++ )
		{
			names[i] = handles[i].getQualifiedName( );
		}

		// Filter predefined styles to make its logic same with report design
		// side.
		names = filterPreStyles( names );
		return names;
	}

	/**
	 * Filter predefined styles.
	 * 
	 * @param items
	 *            all available styles
	 * @return filtered styles.
	 */
	private String[] filterPreStyles( String items[] )
	{
		String[] newItems = items;
		if ( items == null )
		{
			newItems = new String[]{};
		}

		List preStyles = new DesignEngine( new DesignConfig( ) ).getMetaData( )
				.getPredefinedStyles( );
		List preStyleNames = new ArrayList( );

		for ( int i = 0; i < preStyles.size( ); i++ )
		{
			preStyleNames.add( ( (PredefinedStyle) preStyles.get( i ) ).getName( ) );
		}

		List sytleNames = new ArrayList( );
		for ( int i = 0; i < newItems.length; i++ )
		{
			if ( preStyleNames.indexOf( newItems[i] ) == -1 )
			{
				sytleNames.add( newItems[i] );
			}
		}

		return (String[]) ( sytleNames.toArray( new String[]{} ) );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#getAllStyleDisplayNames()
	 */
	public String[] getAllStyleDisplayNames( )
	{
		List styles = Arrays.asList( getAllStyles( ) );
		StyleHandle[] handles = getAllStyleHandles( );
		String[] names = new String[styles.size( )];
		for ( int i = 0, j = 0; i < handles.length; i++ )
		{
			// Remove predefined styles to make its logic same with report
			// design side.
			if ( styles.contains( handles[i].getQualifiedName( ) ) )
			{
				names[j++] = handles[i].getDisplayLabel( );
			}
		}
		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#getCurrentStyle()
	 */
	public String getCurrentStyle( )
	{
		if ( itemHandle.getStyle( ) == null )
		{
			return null;
		}
		return itemHandle.getStyle( ).getQualifiedName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#setStyle(java.lang.String)
	 */
	public void setStyle( String styleName )
	{
		try
		{
			if ( styleName == null )
			{
				itemHandle.setStyle( null );
			}
			else
			{
				itemHandle.setStyle( getStyle( styleName ) );
			}
		}
		catch ( SemanticException e )
		{
			ChartWizard.showException( e.getLocalizedMessage( ) );
		}
	}

	private SharedStyleHandle getStyle( String styleName )
	{
		return getReportDesignHandle( ).findStyle( styleName );
	}

	public final Object[] getDataForColumns( String[] sExpressions,
			int iMaxRecords, boolean byRow ) throws ChartException
	{
		List rowData = getPreviewRowData( sExpressions, iMaxRecords, false );
		if ( byRow )
		{
			return rowData.toArray( );
		}
		List columnData = new ArrayList( );
		for ( int i = 0; i < sExpressions.length; i++ )
		{
			Object[] columnArray = new Object[rowData.size( )];
			for ( int j = 0; j < rowData.size( ); j++ )
			{
				columnArray[j] = ( (Object[]) rowData.get( j ) )[i];
			}
			columnData.add( columnArray );
		}
		return columnData.toArray( );
	}

	public final Object getDAtaForColumns( String[] sExpressions,
			int iMaxRecords, boolean byRow ) throws ChartException
	{
		
		return null;
	}
	
	public void dispose( )
	{
		// TODO DataEngine should be disposed when closing report design, rather
		// than when closing chart wizard. Currently, do nothing at this time.

		// ChartDataSetManager.getCurrentInstance( ).dispose( );
	}

	private int getMaxRow( )
	{
		return ChartReportItemUIActivator.getDefault( )
				.getPluginPreferences( )
				.getInt( ChartReportItemUIActivator.PREFERENCE_MAX_ROW );
	}

	public boolean isLivePreviewEnabled( )
	{
		return !isErrorFound
				&& ChartReportItemUIActivator.getDefault( )
						.getPluginPreferences( )
						.getBoolean( ChartReportItemUIActivator.PREFERENCE_ENALBE_LIVE )
				&& ( getReportDataSet( ) != null || getBoundDataSet( ) != null );
	}

	boolean isInvokingSupported( )
	{
		// If report item reference is set, all operations, including filters,
		// parameter bindings and column bindings are not supported
		if ( itemHandle.getDataBindingReference( ) != null )
		{
			return false;
		}
		ReportItemHandle container = DEUtil.getBindingHolder( itemHandle );
		if ( container != null )
		{
			// To check container's report item reference
			return container.getDataBindingReference( ) == null;
		}
		return true;
	}

	private ClassLoader getCustomScriptClassLoader( ClassLoader parent )
	{
		// For Bugzilla 106580: in order for Data Set Preview to locate POJO, we
		// need to set current thread's context class loader to a custom loader
		// which has the following path:
		// All workspace Java project's class path (this class path is already
		// has already calculated byorg.eclipse.birt.report.debug.ui plugin, and
		// set as system property "workspace.projectclasspath"
		String classPath = System.getProperty( "workspace.projectclasspath" ); //$NON-NLS-1$
		if ( classPath == null || classPath.length( ) == 0 )
			return parent;

		String[] classPathArray = classPath.split( File.pathSeparator, -1 );
		int count = classPathArray.length;
		URL[] urls = new URL[count];
		for ( int i = 0; i < count; i++ )
		{
			File file = new File( classPathArray[i] );
			try
			{
				urls[i] = file.toURL( );
			}
			catch ( MalformedURLException e )
			{
				urls[i] = null;
			}
		}

		return new URLClassLoader( urls, parent );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#getDataType(java.lang.String)
	 */
	public DataType getDataType( String expression )
	{
		if ( expression == null || expression.trim( ).length( ) == 0 )
		{
			return null;
		}

		// Find data types from self column bindings first
		Object[] returnObj = findDataType( expression, itemHandle );
		if ( ( (Boolean) returnObj[0] ).booleanValue( ) )
		{
			return (DataType) returnObj[1];
		}

		// Find data types from its container column bindings.
		ReportItemHandle parentHandle = DEUtil.getBindingHolder( itemHandle );
		if ( parentHandle != null )
		{
			returnObj = findDataType( expression, parentHandle );
			if ( ( (Boolean) returnObj[0] ).booleanValue( ) )
			{
				return (DataType) returnObj[1];
			}
		}

		// Try to parse with number format
		try
		{
			NumberFormat.getInstance( ).parse( expression );
			return DataType.NUMERIC_LITERAL;
		}
		catch ( ParseException e )
		{
		}

		// Try to parse with date format
		try
		{
			DateFormat.getInstance( ).parse( expression );
			return DataType.DATE_TIME_LITERAL;
		}
		catch ( ParseException e )
		{
		}

		// Return null for unknown data type.
		return null;
	}

	/**
	 * Find data type of expression from specified item handle.
	 * 
	 * @param expression
	 *            expression.
	 * @param itemHandle
	 *            specified item handle.
	 * @return an object array, size is two, the first element is a boolean
	 *         object, if its value is <code>true</code> then means the data
	 *         type is found and the second element of array stores the data
	 *         type; if its value is <code>false</code> then means that data
	 *         type is not found.
	 */
	private Object[] findDataType( String expression,
			ReportItemHandle itemHandle )
	{
		Object[] returnObj = new Object[2];
		returnObj[0] = new Boolean( false );

		Iterator iterator = itemHandle.columnBindingsIterator( );
		while ( iterator.hasNext( ) )
		{
			ComputedColumnHandle cc = (ComputedColumnHandle) iterator.next( );
			if ( expression.toUpperCase( )
					.indexOf( cc.getName( ).toUpperCase( ) ) >= 0 )
			{
				String dataType = cc.getDataType( );
				if ( dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_STRING ) )
				{
					returnObj[0] = new Boolean( true );
					returnObj[1] = DataType.TEXT_LITERAL;
					break;
				}
				else if ( dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL )
						|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT )
						|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER )
						|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN ) )
				{
					returnObj[0] = new Boolean( true );
					returnObj[1] = DataType.NUMERIC_LITERAL;
					break;
				}
				else if ( dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME )
						|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATE )
						|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_TIME ) )
				{
					returnObj[0] = new Boolean( true );
					returnObj[1] = DataType.DATE_TIME_LITERAL;
					break;
				}
				else if ( dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_ANY ) )
				{
					returnObj[0] = new Boolean( true );
					returnObj[1] = null;
					break;
				}
			}
		}
		return returnObj;
	}

	public String[] getAllReportItemReferences( )
	{
		List referenceList = itemHandle.getAvailableDataBindingReferenceList( );
		String[] references = new String[referenceList.size( ) + 1];
		references[0] = OPTION_NONE;
		for ( int i = 0; i < referenceList.size( ); i++ )
		{
			references[i + 1] = ( (ReportItemHandle) referenceList.get( i ) ).getQualifiedName( );
		}
		return references;
	}

	public String getReportItemReference( )
	{
		ReportItemHandle ref = itemHandle.getDataBindingReference( );
		if ( ref == null )
		{
			return null;
		}
		return ref.getQualifiedName( );
	}

	public void setReportItemReference( String referenceName )
	{
		try
		{
			if ( referenceName == null )
			{
				itemHandle.setDataBindingReference( null );
				// Select the corresponding data set, no need to reset bindings
			}
			else
			{
				if ( !referenceName.equals( getReportItemReference( ) ) )
				{
					// Change reference and reset all bindings
					itemHandle.setDataBindingReference( (ReportItemHandle) getReportDesignHandle( ).findElement( referenceName ) );
					// //Model will reset bindings
					// clearBindings( );
					// generateBindings( );
				}
			}
		}
		catch ( SemanticException e )
		{
			ChartWizard.showException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#prepareRowExpressionEvaluator(org.eclipse.birt.chart.model.Chart,
	 *      java.lang.String[], int, boolean)
	 */
	public IDataRowExpressionEvaluator prepareRowExpressionEvaluator( Chart cm,
			List columnExpression, int rowCount, boolean isStringType )
			throws ChartException
	{
		// Set thread context class loader so Rhino can find POJOs in workspace
		// projects
		ClassLoader oldContextLoader = Thread.currentThread( )
				.getContextClassLoader( );
		ClassLoader parentLoader = oldContextLoader;
		if ( parentLoader == null )
			parentLoader = this.getClass( ).getClassLoader( );
		ClassLoader newContextLoader = getCustomScriptClassLoader( parentLoader );
		Thread.currentThread( ).setContextClassLoader( newContextLoader );

		try
		{
			DataRequestSession session = prepareDataRequestSession( rowCount );

			BaseQueryHelper cbqh = new BaseQueryHelper( itemHandle, cm );
			QueryDefinition queryDefn = (QueryDefinition) cbqh.createBaseQuery( columnExpression );

			// Iterate parameter bindings to check if its expression is a
			// explicit
			// value, otherwise use default value of parameter as its
			// expression.
			resetParametersForDataPreview( getDataSetFromHandle( ), queryDefn );

			IQueryResults actualResultSet = session.executeQuery( queryDefn,
					null,
					itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP )
							.iterator( ),
					ChartReportItemUtil.getColumnDataBindings( itemHandle ) );
			if ( actualResultSet != null )
			{
				return new GroupedQueryResultSetEvaluatorAdapter( actualResultSet.getResultIterator( ),
						ChartReportItemUtil.hasAggregation( cm ) );
			}
		}
		catch ( BirtException e )
		{
			throw new ChartException( ChartReportItemUIActivator.ID,
					ChartException.DATA_BINDING,
					e );
		}
		finally
		{
			// Restore old thread context class loader
			Thread.currentThread( ).setContextClassLoader( oldContextLoader );
		}

		return null;
	}

	/**
	 * @param maxRow
	 * @return
	 * @throws BirtException
	 */
	private DataRequestSession prepareDataRequestSession( int maxRow )
			throws BirtException
	{
		DataSessionContext dsc = new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
				getReportDesignHandle( ) );

		Map appContext = new HashMap( );
		appContext.put( DataEngine.DATA_SET_CACHE_ROW_LIMIT,
				new Integer( maxRow ) );
		dsc.setAppContext( appContext );

		DataRequestSession session = DataRequestSession.newSession( dsc );
		return session;
	}
	
	/**
	 * The class is responsible to create query definition.
	 * 
	 * @since BIRT 2.3
	 */
	class BaseQueryHelper extends AbstractChartBaseQueryGenerator
	{
		/**
		 * Constructor of the class.
		 * 
		 * @param chart
		 * @param handle
		 * @param query
		 */
		public BaseQueryHelper( ExtendedItemHandle handle, Chart chart )
		{
			super( handle, chart );
		}

		/**
		 * Create query definition.
		 * 
		 * @param columnExpression
		 * @return
		 * @throws DataException
		 */
		public IDataQueryDefinition createBaseQuery( List columnExpression )
				throws DataException
		{
			QueryDefinition queryDefn = new QueryDefinition( );
			int maxRow = getMaxRow( );
			queryDefn.setMaxRows( maxRow );
			queryDefn.setDataSetName( getDataSetFromHandle( ).getQualifiedName( ) );

			for ( int i = 0; i < columnExpression.size( ); i++ )
			{
				String expr =  (String) columnExpression.get( i );
				Binding colBinding = new Binding( expr );
				colBinding.setExpression( new ScriptExpression( expr ) );
				queryDefn.addBinding( colBinding );
				fNameSet.add( expr );
			}

			generateGroupBindings( queryDefn );

			return queryDefn;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.birt.chart.reportitem.AbstractChartBaseQueryGenerator#createBaseQuery(org.eclipse.birt.data.engine.api.IDataQueryDefinition)
		 */
		public IDataQueryDefinition createBaseQuery( IDataQueryDefinition parent )
		{
			throw new UnsupportedOperationException( "Don't be implemented in the class." ); //$NON-NLS-1$
		}

	} // End of class BaseQueryHelper.
}