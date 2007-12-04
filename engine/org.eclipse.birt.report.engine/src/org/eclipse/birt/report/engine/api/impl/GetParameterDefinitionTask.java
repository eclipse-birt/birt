/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.ICascadingParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IParameterGroupDefn;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignVisitor;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SelectionChoiceHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import com.ibm.icu.util.ULocale;

/**
 * Defines an engine task that handles parameter definition retrieval
 */
public class GetParameterDefinitionTask extends EngineTask
		implements
			IGetParameterDefinitionTask
{

	// stores all parameter definitions. Each task clones the parameter
	// definition information
	// so that Engine IR (repor runnable) can keep a task-independent of the
	// parameter definitions.
	protected Collection parameterDefns = null;

	/**
	 * @param engine
	 *            reference to the report engine
	 * @param runnable
	 *            the runnable report design
	 */
	public GetParameterDefinitionTask( IReportEngine engine,
			IReportRunnable runnable )
	{
		super( engine, runnable, IEngineTask.TASK_GETPARAMETERDEFINITION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IGetParameterDefinitionTask#getParameterDefns(boolean)
	 */
	public Collection getParameterDefns( boolean includeParameterGroups )
	{
		DesignElementHandle handle = ( (ReportRunnable) runnable ).getDesignHandle( );
		Collection original = getParameters( (ReportDesignHandle)handle, includeParameterGroups );
		Iterator iter = original.iterator( );

		// Clone parameter definitions, fill in locale and report dsign
		// information
		parameterDefns = new ArrayList( );

		while ( iter.hasNext( ) )
		{
			ParameterDefnBase pBase = (ParameterDefnBase) iter.next( );
			try
			{
				parameterDefns.add( pBase.clone( ) );
			}
			catch ( CloneNotSupportedException e ) // This is a Java
			// exception
			{
				log.log( Level.SEVERE, e.getMessage( ), e );
			}
		}

		if ( parameterDefns != null )
		{
			iter = parameterDefns.iterator( );
			while ( iter.hasNext( ) )
			{
				IParameterDefnBase pBase = (IParameterDefnBase) iter.next( );
				if ( pBase instanceof ScalarParameterDefn )
				{
					( (ScalarParameterDefn) pBase )
							.setReportDesign( (ReportDesignHandle) runnable
									.getDesignHandle( ) );
					( (ScalarParameterDefn) pBase ).setLocale( locale );
					( (ScalarParameterDefn) pBase ).evaluateSelectionList( );
				}
				else if ( pBase instanceof ParameterGroupDefn )
				{
					( (ParameterGroupDefn) pBase )
							.setReportDesign( (ReportDesignHandle) runnable
									.getDesignHandle( ) );
					Iterator iter2 = ( (ParameterGroupDefn) pBase )
							.getContents( ).iterator( );
					while ( iter2.hasNext( ) )
					{
						IParameterDefnBase p = (IParameterDefnBase) iter2
								.next( );
						if ( p instanceof ScalarParameterDefn )
						{
							( (ScalarParameterDefn) p )
									.setReportDesign( (ReportDesignHandle) runnable
											.getDesignHandle( ) );
							( (ScalarParameterDefn) p ).setLocale( locale );
							( (ScalarParameterDefn) p ).evaluateSelectionList( );
						}
					}
				}
			}
		}
		return parameterDefns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IGetParameterDefinitionTask#evaluateDefaults()
	 */
	public void evaluateDefaults( ) throws EngineException
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask#getParameterDefn(java.lang.String)
	 */
	public IParameterDefnBase getParameterDefn( String name )
	{
		IParameterDefnBase ret = null;
		if ( name == null )
		{
			return ret;
		}

		DesignElementHandle handle = ( (ReportRunnable) runnable ).getDesignHandle( );
		Collection original = getParameters( (ReportDesignHandle)handle, true );
		
		Iterator iter = original.iterator( );
		while ( iter.hasNext( ) )
		{
			ret = getParamDefnBaseByName( (ParameterDefnBase) iter.next( ),
					name );
			if ( ret != null )
				break;
		}
		
		if ( ret != null )
		{

			if ( ret instanceof ScalarParameterDefn )
			{
				( (ScalarParameterDefn) ret )
						.setReportDesign( (ReportDesignHandle) runnable
								.getDesignHandle( ) );
				( (ScalarParameterDefn) ret ).setLocale( locale );
				( (ScalarParameterDefn) ret ).evaluateSelectionList( );
			}
			else if ( ret instanceof ParameterGroupDefn )
			{
				( (ParameterGroupDefn) ret )
						.setReportDesign( (ReportDesignHandle) runnable
								.getDesignHandle( ) );
				( (ParameterGroupDefn) ret ).setLocale( locale );
				Iterator iter2 = ( (ParameterGroupDefn) ret ).getContents( )
						.iterator( );
				while ( iter2.hasNext( ) )
				{
					IParameterDefnBase p = (IParameterDefnBase) iter2.next( );
					if ( p instanceof ScalarParameterDefn )
					{
						( (ScalarParameterDefn) p )
								.setReportDesign( (ReportDesignHandle) runnable
										.getDesignHandle( ) );
						( (ScalarParameterDefn) p ).setLocale( locale );
						( (ScalarParameterDefn) p ).evaluateSelectionList( );
					}
				}
			}
		}
		return ret;
	}

	public SlotHandle getParameters( )
	{
		ReportDesignHandle report = (ReportDesignHandle) runnable
				.getDesignHandle( );
		return report.getParameters( );
	}

	public ParameterHandle getParameter( String name )
	{
		ReportDesignHandle report = (ReportDesignHandle) runnable
				.getDesignHandle( );
		return report.findParameter( name );

	}

	public HashMap getDefaultValues( )
	{
		// using current parameter settings to evaluate the default parameters
		usingParameterValues( );

		final HashMap values = new HashMap( );
		// reset the context parameters
		new ParameterVisitor( ) {

			boolean visitScalarParameter( ScalarParameterHandle param,
					Object userData )
			{
				String name = param.getName( );
				String expr = param.getDefaultValue( );
				String type = param.getDataType( );
				Object value = convertToType( expr, type );
				values.put( name, value );
				return true;
			}

			boolean visitParameterGroup( ParameterGroupHandle group,
					Object userData )
			{
				return visitParametersInGroup( group, userData );
			}
		}.visit( (ReportDesignHandle) runnable.getDesignHandle( ) );
		return values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask#getDefaultParameter(java.lang.String)
	 */
	public Object getDefaultValue( IParameterDefnBase param )
	{
		return ( param == null ) ? null : getDefaultValue( param.getName( ) );
	}

	public Object getDefaultValue( String name )
	{
		ReportDesignHandle report = (ReportDesignHandle) runnable
				.getDesignHandle( );
		ScalarParameterHandle parameter = (ScalarParameterHandle) report
				.findParameter( name );
		if ( parameter == null )
		{
			return null;
		}

		usingParameterValues( );

		// using the current setting to evaluate the parameter values.
		String expr = parameter.getDefaultValue( );
		String dataType = parameter.getDataType( );
		if ( expr == null
				|| ( expr.length( ) == 0 && !DesignChoiceConstants.PARAM_TYPE_STRING
						.equals( dataType ) ) )
		{
			return null;
		}
		return convertToType( expr, dataType );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask#getSelectionChoice(java.lang.String)
	 */
	public Collection getSelectionList( String name )
	{
		usingParameterValues( );

		ReportDesignHandle report = (ReportDesignHandle) this.runnable
				.getDesignHandle( );
		ScalarParameterHandle parameter = (ScalarParameterHandle) report
				.findParameter( name );
		if ( parameter == null )
		{
			return Collections.EMPTY_LIST;
		}
		String selectionType = parameter.getValueType( );
		String dataType = parameter.getDataType( );
		boolean fixedOrder = parameter.isFixedOrder( );
		boolean sortByLabel = "label".equalsIgnoreCase( parameter.getSortBy( ) );
		boolean sortDirectionValue = "asc".equalsIgnoreCase( parameter.getSortDirection( ) );
		if ( DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC
				.equals( selectionType ) )
		{
			CascadingParameterGroupHandle group = null;
			if ( isCascadingParameter( parameter ) )
			{
				group = getCascadingGroup( parameter );
			}
			if ( group != null )
			{
				// parameter in group
				if ( DesignChoiceConstants.DATA_SET_MODE_SINGLE.equals( group
						.getDataSetMode( ) ) )
				{
					// single dataSet
					return getCascadingParameterList( parameter );
				}
				else
				{
					// multiple dataSet
					if ( parameter.getDataSetName( ) != null )
					{
						// parameter has dataSet
						return getChoicesFromParameterQuery( parameter );
					}
					// parameter do not has dataSet, so use the group's dataSet
					// we do not support such mix parameters.
					// return empty list
				}
			}
			else
			{
				// parameter not in group
				if ( parameter.getDataSetName( ) != null )
				{
					// parameter has dataSet
					return getChoicesFromParameterQuery( parameter );
				}
				// return empty list
			}
		}
		else if ( DesignChoiceConstants.PARAM_VALUE_TYPE_STATIC
				.equals( selectionType ) )
		{
			Iterator iter = parameter.choiceIterator( );
			ArrayList choices = new ArrayList( );
			while ( iter.hasNext( ) )
			{

				SelectionChoiceHandle choice = (SelectionChoiceHandle) iter
						.next( );

				String label = report
						.getMessage( choice.getLabelKey( ), locale );
				if ( label == null )
				{
					label = choice.getLabel( );
				}
				Object value = convertToType( choice.getValue( ), dataType );
				choices.add( new SelectionChoice( label, value ) );
			}
			if ( !fixedOrder )
				Collections.sort( choices, new SelectionChoiceComparator(
						sortByLabel, parameter.getPattern( ),
						sortDirectionValue, ULocale.forLocale( locale ) ) );
			return choices;

		}
		return Collections.EMPTY_LIST;
	}

	private Collection getCascadingParameterList( ScalarParameterHandle parameter )
	{
		Object[] parameterValuesAhead =  getParameterValuesAhead( parameter );
		return getChoicesFromParameterGroup ( parameter, parameterValuesAhead );
	}

	private Collection populateToList( IResultIterator iterator,
			ScalarParameterHandle parameter, SelectionFilter filter )
	{
		ParameterHelper parameterHelper = new ParameterHelper( parameter, locale );
		Collection choices = parameterHelper.createSelectionCollection( );
		int limit = parameter.getListlimit( );
		try
		{
			while ( iterator.next( )
					&& ( limit <= 0 || choices.size( ) < limit ) )
			{
				// skip duplicated values.
				if ( filter != null && !filter.accept( iterator ) )
				{
					continue;
				}
				String label = parameterHelper.getLabel( iterator );
				Object value = parameterHelper.getValue( iterator );
				choices.add( new SelectionChoice( label, value ) );
			}
		}
		catch ( BirtException ex )
		{
			log.log( Level.WARNING, ex.getMessage( ), ex );
			executionContext.addException( ex );
		}
		return choices;
	}

	private DataRequestSession createDataSession( DataSetHandle dataSet )
	{
		IDataEngine dataEngine = executionContext.getDataEngine( );
		DataRequestSession dteSession = getDataSession();
		// Define data source and data set
		dataEngine.defineDataSet(dataSet);
		return dteSession;
	}
	
	private QueryDefinition createQueryDefinition( DataSetHandle dataSet )
	{
		QueryDefinition queryDefn = new QueryDefinition( );
		queryDefn.setDataSetName( dataSet.getQualifiedName( ) );
		queryDefn.setAutoBinding( true );
		return queryDefn;
	}
	
	private IResultIterator executeQuery( DataRequestSession dteSession,
			QueryDefinition queryDefn ) throws BirtException
	{
		IPreparedQuery query = dteSession.prepare( queryDefn, getAppContext( ) );
		IQueryResults result = query
				.execute( executionContext.getSharedScope( ) );
		return result.getResultIterator( );
	}
	
	/**
	 * The first step to work with the cascading parameters. Create the query
	 * definition, prepare and execute the query. Cache the iterator of the
	 * result set and also cache the IBaseExpression used in the prepare.
	 * 
	 * @param parameterGroupName -
	 *            the cascading parameter group name
	 */
	public void evaluateQuery( String parameterGroupName )
	{
	}
	
	/**
	 * The second step to work with the cascading parameters. Get the selection
	 * choices for a parameter in the cascading group. The parameter to work on
	 * is the parameter on the next level in the parameter cascading hierarchy.
	 * For the "parameter to work on", please see the following example. Assume
	 * we have a cascading parameter group as Country - State - City. If user
	 * specified an empty array in groupKeyValues (meaning user doesn't have any
	 * parameter value), the parameter to work on will be the first level which
	 * is Country in this case. If user specified groupKeyValues as
	 * Object[]{"USA"} (meaning user has set the value of the top level), the
	 * parameter to work on will be the second level which is State in "USA" in
	 * this case. If user specified groupKeyValues as Object[]{"USA", "CA"}
	 * (meaning user has set the values of the top and the second level), the
	 * parameter to work on will be the third level which is City in "USA, CA"
	 * in this case.
	 * 
	 * @param parameterGroupName -
	 *            the cascading parameter group name
	 * @param groupKeyValues -
	 *            the array of known parameter values (see the example above)
	 * @return the selection list of the parameter to work on
	 */
	public Collection getSelectionListForCascadingGroup(
			String parameterGroupName, Object[] groupKeyValues )
	{
		CascadingParameterGroupHandle parameterGroup = getCascadingParameterGroup( parameterGroupName );
		if ( parameterGroup == null )
			return Collections.EMPTY_LIST;

		SlotHandle slotHandle = parameterGroup.getParameters( );
		if ( groupKeyValues.length >= slotHandle.getCount( ) )
		{
			return Collections.EMPTY_LIST;
		}

		for ( int i = 0; i < groupKeyValues.length; i++ )
		{
			String parameterName = (( ScalarParameterHandle ) slotHandle.get( i )).getName( );
			setParameterValue( parameterName, groupKeyValues[ i ] );
		}

		ScalarParameterHandle requestedParam = (ScalarParameterHandle) slotHandle
				.get( groupKeyValues.length ); // The parameters in
		// parameterGroup must be scalar
		// parameters.
		if ( requestedParam == null )
		{
			return Collections.EMPTY_LIST;
		}
		return this.getSelectionList( requestedParam.getName( ) );
	}

	public Collection getSelectionTreeForCascadingGroup(
			String parameterGroupName )
	{
		CascadingParameterGroupHandle parameterGroup = getCascadingParameterGroup( parameterGroupName );
		if ( parameterGroup == null )
			return Collections.EMPTY_LIST;
		SlotHandle parameters = parameterGroup.getParameters( );
		int parameterCount = parameters.getCount( );
		if ( DesignChoiceConstants.DATA_SET_MODE_SINGLE.equals( parameterGroup
				.getDataSetMode( ) ) )
		{
			// single dataSet
			IResultIterator resultIterator = getResultSetOfCascadingGroup( parameterGroup );
			if ( resultIterator == null )
			{
				return Collections.EMPTY_LIST;
			}
			Collection selectionTree = populateToSelectionTree( resultIterator,
					parameterGroup );
			close( resultIterator );
			return selectionTree;
		}
		else
		{
			ParameterHelper[] parameterHelpers = getParameterHelpers( parameterGroup );
			ChoiceListCache cache = new ChoiceListCache( parameterHelpers );
			assert( parameterCount > 0 );
			return getSelectionTree( parameters, parameterHelpers, cache,
					new Object[0] );
		}
	}

	private Collection getSelectionTree( SlotHandle parameters,
			ParameterHelper[] parameterHelpers, ChoiceListCache cache,
			Object[] parameterValueAhead )
	{
		int parameterIndex = parameterValueAhead.length;
		int parameterCount = parameters.getCount( );
		ScalarParameterHandle parameter = (ScalarParameterHandle) parameters
				.get( parameterIndex );
		Collection choices = getChoicesFromParameterQuery( parameter );
		Iterator iterator = choices.iterator( );
		Collection result = null;
		while ( iterator.hasNext( ) )
		{
			Object[] values = new Object[parameterIndex + 1];
			for ( int i = 0; i < parameterValueAhead.length; i++ )
			{
				values[i] = parameterValueAhead[i];
			}
			IParameterSelectionChoice choice = (IParameterSelectionChoice) iterator
					.next( );
			Object value = choice.getValue( );
			values[parameterIndex] = value;
			Collection children = null;
			if ( parameterIndex == parameterCount - 1 )
			{
				children = Collections.EMPTY_LIST;
			}
			else if ( cache.containsChildren( values, parameterIndex ) )
			{
				children = cache.getChildren( values, parameterIndex );
			}
			else
			{
				executionContext.setParameter( parameter.getName( ), value,
						choice.getLabel( ) );
				children = getSelectionTree( parameters, parameterHelpers, cache, values );
			}
			result = cache.getParent( values, parameterIndex );
			CascadingParameterSelectionChoice groupChoice = parameterHelpers[parameterIndex]
					.createCascadingParameterSelectionChoice( choice );
			result.add( groupChoice );
			groupChoice.setChildren( children );
		}
		return result;
	}

	private void close( IResultIterator resultIterator )
	{
		try
		{
			resultIterator.close( );
		}
		catch ( BirtException e )
		{
			e.printStackTrace();
		}
	}

	private Collection populateToSelectionTree( IResultIterator iterator,
			CascadingParameterGroupHandle parameterGroup )
	{
		assert iterator != null;
		ParameterHelper[] parameterHelpers = getParameterHelpers( parameterGroup );
		ChoiceListCache cache = new ChoiceListCache( parameterHelpers );
		int parameterCount = parameterHelpers.length;
		try
		{
			while ( iterator.next( ) )
			{
				Object[] values = new Object[parameterCount];
				for ( int i = 0; i < parameterCount; i++ )
				{
					ParameterHelper parameterHelper = parameterHelpers[i];
					CascadingParameterSelectionChoice choice = parameterHelper
							.createCascadingParameterSelectionChoice( iterator );
					values[i] = choice.getValue( );
					cache.getParent( values, i ).add( choice );
					choice.setChildren( cache.getChildren( values, i ) );
				}
			}
		}
		catch ( BirtException ex )
		{
			log.log( Level.WARNING, ex.getMessage( ), ex );
			executionContext.addException( ex );
		}
		return cache.getRoot( );
	}

	private ParameterHelper[] getParameterHelpers(
			CascadingParameterGroupHandle parameterGroup )
	{
		SlotHandle parameters = parameterGroup.getParameters( );
		int parameterCount = parameters.getCount( );
		ParameterHelper[] parameterHelpers = new ParameterHelper[parameterCount];
		for ( int i = 0; i < parameterCount; i++ )
		{
			ScalarParameterHandle parameter = (ScalarParameterHandle) parameters.get( i );
			parameterHelpers[i] = new ParameterHelper( parameter, locale );
		}
		return parameterHelpers;
	}
	
	private Collection getChoicesFromParameterGroup(
			ScalarParameterHandle parameter, Object[] groupKeyValues )
	{
		assert isCascadingParameter( parameter );
		CascadingParameterGroupHandle parameterGroup = getCascadingGroup( parameter );
		IResultIterator iterator = getResultSetOfCascadingGroup( parameterGroup );
		if ( iterator == null )
		{
			return Collections.EMPTY_LIST;
		}
		return populateToList( iterator, parameter, new ParameterGroupFilter(
				groupKeyValues, parameterGroup ) );
	}

	private interface SelectionFilter
	{
		boolean accept( IResultIterator iterator ) throws BirtException;
	}
	
	private class ParameterGroupFilter implements SelectionFilter
	{
		Object[] keyValues;
		String[] valueColumnNames;
		String[] valueTypes;
		
		public ParameterGroupFilter( Object[] keyValues,
				CascadingParameterGroupHandle parameterGroup )
		{
			this.keyValues = keyValues;
			valueColumnNames = new String[keyValues.length];
			valueTypes = new String[keyValues.length];
			SlotHandle parameterSlots = parameterGroup.getParameters( );
			for ( int i = 0; i < keyValues.length; i++ )
			{
				ScalarParameterHandle tempParameter = (ScalarParameterHandle) parameterSlots
						.get( i );
				valueColumnNames[i] = ParameterHelper.getValueColumnName( tempParameter );
				valueTypes[i] = tempParameter.getDataType( );
			}

		}
		
		public boolean accept( IResultIterator iterator ) throws BirtException
		{
			for ( int i = 0; i < valueColumnNames.length; i++ )
			{
				Object value = iterator.getValue( valueColumnNames[i] );
				value = convertToType( value, valueTypes[i] );
				if ( ( value == null && keyValues[i] != null )
						|| ( value != null && !value.equals( keyValues[i] ) ) )
				{
					return false;
				}
			}
			return true;
		}
	}
	
	private IResultIterator getResultSetOfCascadingGroup(
			CascadingParameterGroupHandle parameterGroup )
	{
		if ( parameterGroup == null )
			return null;
		
		//If a IResultIterator with the same name has already existed in the dataCache,
		//this IResultIterator and its IQueryResults should be closed.
		DataSetHandle dataSet = parameterGroup.getDataSet( );
		if ( dataSet != null )
		{
			try
			{
				// Handle data source and data set
				QueryDefinition queryDefn = createQueryDefinition( dataSet );
		
				Iterator iter = parameterGroup.getParameters( ).iterator( );
				while ( iter.hasNext( ) )
				{
					Object parameter = iter.next( );
					if ( parameter instanceof ScalarParameterHandle )
					{
						ParameterHelper.addParameterBinding( queryDefn,
								(ScalarParameterHandle) parameter );
					}
				}
		
				DataRequestSession dteSession = createDataSession( dataSet );
				return executeQuery( dteSession, queryDefn );
			}
			catch ( BirtException ex )
			{
				log.log( Level.WARNING, ex.getMessage( ), ex );
				executionContext.addException( ex );
			}
		}
		return  null;
	}
	
	private CascadingParameterGroupHandle getCascadingParameterGroup(
			String name )
	{
		ReportDesignHandle report = (ReportDesignHandle) runnable
				.getDesignHandle( );

		return report.findCascadingParameterGroup( name );
	}

	static class SelectionChoice implements IParameterSelectionChoice
	{

		String label;

		Object value;

		SelectionChoice( String label, Object value )
		{
			this.label = label;
			this.value = value;
		}

		public String getLabel( )
		{
			return this.label;
		}

		public Object getValue( )
		{
			return this.value;
		}
		
		public boolean equals( Object obj )
		{
			if ( obj == this )
			{
				return true;
			}
			if ( !(obj instanceof SelectionChoice ) )
			{
				return false;
			}
			SelectionChoice choice = (SelectionChoice)obj;
			if ( value == null )
			{
				return choice.value == null;
			}
			return value.equals( choice.value );
		}
		
		public int hashCode( )
		{
			return value == null? 0 : value.hashCode( );
		}
	}
	
	static class CascadingParameterSelectionChoice extends SelectionChoice
			implements
				ICascadingParameterSelectionChoice
	{
		Collection children;

		public CascadingParameterSelectionChoice( String label, Object value )
		{
			super( label, value );
		}

		public void setChildren( Collection children )
		{
			this.children = children;
		}
		
		public Collection getChildSelectionList( )
		{
			return children;
		}

		public boolean equals( Object obj )
		{
			if ( obj == this )
			{
				return true;
			}
			if ( !(obj instanceof CascadingParameterSelectionChoice ) )
			{
				return false;
			}
			CascadingParameterSelectionChoice choice = (CascadingParameterSelectionChoice)obj;
			if ( value == null )
			{
				return choice.value == null;
			}
			return value.equals( choice.value );
		}
	}
	
	static class ChoiceListCache
	{
		private ParameterHelper[] parameterHelpers;
		private Map[] cachedLists;
		private Collection root;
		private int parameterCount;
		
		public ChoiceListCache( ParameterHelper[] parameterHelpers )
		{
			this.parameterHelpers = parameterHelpers;
			this.parameterCount = parameterHelpers.length;
			cachedLists = new Map[parameterCount - 1];
			for ( int i = 0; i < cachedLists.length; i++ )
			{
				cachedLists[i] = new HashMap( );
			}
			root = parameterHelpers[0].createSelectionCollection( );
		}
		
		public Collection getParent( Object[] values, int parameterIndex )
		{
			if ( parameterIndex == 0 )
			{
				return root;
			}
			int parentIndex = parameterIndex - 1;
			ValueGroup valueGroup = new ValueGroup( values, parentIndex );
			Map cache = cachedLists[parentIndex];
			Collection parent = (Collection)cache.get( valueGroup );
			if ( parent == null )
			{
				parent = parameterHelpers[parameterIndex]
						.createSelectionCollection( );
				cache.put( valueGroup, parent );
			}
			return parent;
		}

		public boolean containsChildren( Object[] values, int parameterIndex )
		{
			if ( parameterIndex == parameterCount - 1 )
			{
				return false;
			}

			ValueGroup valueGroup = new ValueGroup( values, parameterIndex );
			Map cache = cachedLists[parameterIndex];
			return cache.containsKey( valueGroup );
		}
		
		public Collection getChildren( Object[] values, int parameterIndex )
		{
			if ( parameterIndex == parameterCount - 1 )
			{
				return Collections.EMPTY_LIST;
			}

			ValueGroup valueGroup = new ValueGroup( values, parameterIndex );
			Map cache = cachedLists[parameterIndex];
			Collection parent = (Collection)cache.get( valueGroup );
			if ( parent == null )
			{
				parent = parameterHelpers[parameterIndex + 1]
						.createSelectionCollection( );
				cache.put( valueGroup, parent );
			}
			return parent;
		}
		
		public Collection getRoot( )
		{
			return root;
		}
	}
	
	static class ValueGroup
	{
		private Object[] values;
		private int parameterIndex;
		
		public ValueGroup( Object[] values, int parameterIndex )
		{
			this.values = values;
			this.parameterIndex = parameterIndex;
		}
		
		public int hashCode( )
		{
			int hashCode = 0;
			for ( int i = 0; i <= parameterIndex; i++ ){
				hashCode += 13 * values[i].hashCode( );
			}
			return hashCode;
		}
		
		public boolean equals( Object obj )
		{
			if ( obj == this )
			{
				return true;
			}
			if ( !(obj instanceof ValueGroup ) )
			{
				return false;
			}
			ValueGroup valueGroup = (ValueGroup)obj;
			if ( parameterIndex != valueGroup.parameterIndex )
			{
				return false;
			}
			for ( int i = 0; i <= parameterIndex; i++ )
			{
				if ( !(equal(values[i], valueGroup.values[i])))
				{
					return false;
				}
			}
			return true;
		}
		
		private boolean equal(Object obj1, Object obj2 )
		{
			if ( obj1 == null )
			{
				return obj2 == null;
			}
			return obj1.equals( obj2 );
		}
	}
	
	private boolean isCascadingParameter( ScalarParameterHandle parameter )
	{
		return parameter.getContainer( ) instanceof CascadingParameterGroupHandle;
	}

	private Object[] getParameterValuesAhead( ScalarParameterHandle parameter )
	{
		assert isCascadingParameter( parameter );
		CascadingParameterGroupHandle parameterGroup = getCascadingGroup( parameter );
		SlotHandle parameters = parameterGroup.getParameters( );
		List values = new ArrayList( );
		for ( int i = 0; i < parameters.getCount( ); i++ )
		{
			ScalarParameterHandle tempParameter = ( ScalarParameterHandle ) parameters.get( i );
			if ( tempParameter == parameter )
			{
				break;
			}
			values.add( getParameterValue( tempParameter.getName( ) ) );
		}
		return values.toArray( );
	}

	private CascadingParameterGroupHandle getCascadingGroup( ScalarParameterHandle parameter )
	{
		DesignElementHandle handle = parameter.getContainer( );
		assert handle instanceof CascadingParameterGroupHandle;
		CascadingParameterGroupHandle parameterGroup = ( CascadingParameterGroupHandle ) handle;
		return parameterGroup;
	}

	private Collection getChoicesFromParameterQuery( ScalarParameterHandle parameter)
	{
		IResultIterator iter = getResultSetForParameter( parameter );
		if ( iter == null )
		{
			return Collections.EMPTY_LIST;
		}
		return populateToList( iter, parameter, null);
	}

	private IResultIterator getResultSetForParameter(
			ScalarParameterHandle parameter )
	{
		ReportDesignHandle report = (ReportDesignHandle) this.runnable
				.getDesignHandle( );
		DataSetHandle dataSet = report.findDataSet( parameter.getDataSetName( ) );
		IResultIterator iterator = null;
		if ( dataSet != null )
		{
			try
			{
				QueryDefinition queryDefn = createQueryDefinition( dataSet );
				ParameterHelper.addParameterBinding( queryDefn, parameter );
		
				DataRequestSession dteSession = createDataSession( dataSet );
				iterator = executeQuery( dteSession, queryDefn );
			}
			catch ( BirtException ex )
			{
				log.log( Level.WARNING, ex.getMessage( ), ex );
				executionContext.addException( ex );
			}
		}
		return iterator;
	}
	
	private IParameterDefnBase getParamDefnBaseByName( ParameterDefnBase param,
			String name )
	{
		ParameterDefnBase ret = null;
		if ( param instanceof ScalarParameterDefn
				&& name.equals( param.getName( ) ) )
		{
			ret = param;
		}
		else if ( param instanceof ParameterGroupDefn )
		{
			if ( name.equals( param.getName( ) ) )
			{
				ret = param;
			}
			else
			{
				Iterator iter = ( (ParameterGroupDefn) param ).getContents( )
						.iterator( );
				while ( iter.hasNext( ) )
				{
					ParameterDefnBase pBase = (ParameterDefnBase) iter.next( );
					if ( name.equals( pBase.getName( ) ) )
					{
						ret = pBase;
						break;
					}
				}
			}
		}
		if ( ret != null )
		{
			try
			{
				return (IParameterDefnBase) ret.clone( );
			}
			catch ( CloneNotSupportedException e )
			{
				log.log( Level.SEVERE, e.getMessage( ), e );
			}
		}
		return ret;
	}
	
	/**
	 * Gets the parameter list of the report.
	 * 
	 * @param design -
	 *            the handle of the report design
	 * @param includeParameterGroups
	 *            A <code>boolean</code> value specifies whether to include
	 *            parameter groups or not.
	 * @return The collection of top-level report parameters and parameter
	 *         groups if <code>includeParameterGroups</code> is set to
	 *         <code>true</code>; otherwise, returns all the report
	 *         parameters.
	 */
	public ArrayList getParameters( ReportDesignHandle handle,
			boolean includeParameterGroups )
	{
		assert ( handle != null );
		ParameterIRVisitor visitor = new ParameterIRVisitor( handle );
		ArrayList parameters = new ArrayList( );

		SlotHandle paramSlot = handle.getParameters( );
		IParameterDefnBase param;
		for ( int i = 0; i < paramSlot.getCount( ); i++ )
		{
			visitor.apply( paramSlot.get( i ) );
			assert ( visitor.currentElement != null );
			param = (IParameterDefnBase) visitor.currentElement;
			assert ( param.getName( ) != null );
			parameters.add( param );
		}
		
		if ( includeParameterGroups )
			return parameters;
		else
			return flattenParameter( parameters );
	}
	
	/**
	 * Puts all the report parameters including those appear inside parameter
	 * groups to the <code>allParameters</code> object.
	 * 
	 * @param params
	 *            A collection of parameters and parameter groups.
	 */
	protected ArrayList flattenParameter( ArrayList params )
	{
		assert params != null;
		IParameterDefnBase param;
		ArrayList allParameters = new ArrayList( );

		for ( int n = 0; n < params.size( ); n++ )
		{
			param = (IParameterDefnBase) params.get( n );
			if ( param.getParameterType( ) == IParameterDefnBase.PARAMETER_GROUP
					|| param.getParameterType( ) == IParameterDefnBase.CASCADING_PARAMETER_GROUP )
			{
				allParameters
						.addAll( flattenParameter( ( (IParameterGroupDefn) param )
								.getContents( ) ) );
			}
			else
			{
				allParameters.add( param );
			}
		}
		
		return allParameters;
	}
	
	class ParameterBinding
	{
		String labelColumnName;
		String valueColumnName;
		String valueType;
		
		public ParameterBinding( String labelColumnName, String valueColumnName, String valueType )
		{
			this.labelColumnName = labelColumnName;
			this.valueColumnName = valueColumnName;
			this.valueType = valueType;
		}
	}
	
	class ParameterIRVisitor extends DesignVisitor
	{
		/**
		 * report design handle
		 */
		protected ReportDesignHandle handle;
		
		/**
		 * current report element created by visitor
		 */
		protected Object currentElement;
		
		ParameterIRVisitor( ReportDesignHandle handle )
		{
			super( );
			this.handle = handle;
		}
		
		public void visitParameterGroup( ParameterGroupHandle handle )
		{
			ParameterGroupDefn paramGroup = new ParameterGroupDefn( );
			paramGroup.setHandle( handle );
			paramGroup.setParameterType( IParameterDefnBase.PARAMETER_GROUP );
			paramGroup.setName( handle.getName( ) );
			paramGroup.setDisplayName( handle.getDisplayName( ) );
			paramGroup.setDisplayNameKey( handle.getDisplayNameKey( ) );
			paramGroup.setHelpText( handle.getHelpText( ) );
			paramGroup.setHelpTextKey( handle.getHelpTextKey( ) );
			SlotHandle parameters = handle.getParameters( );

			// set custom properties
			List properties = handle.getUserProperties( );
			for ( int i = 0; i < properties.size( ); i++ )
			{
				UserPropertyDefn p = (UserPropertyDefn) properties.get( i );
				paramGroup.addUserProperty( p.getName( ), handle.getProperty( p
						.getName( ) ) );
			}

			int size = parameters.getCount( );
			for ( int n = 0; n < size; n++ )
			{
				apply( parameters.get( n ) );
				if ( currentElement != null )
				{
					paramGroup.addParameter( (IParameterDefnBase) currentElement );
				}
			}

			currentElement = paramGroup;
		}

		public void visitCascadingParameterGroup(
				CascadingParameterGroupHandle handle )
		{
			CascadingParameterGroupDefn paramGroup = new CascadingParameterGroupDefn( );
			paramGroup.setHandle( handle );
			paramGroup
					.setParameterType( IParameterDefnBase.CASCADING_PARAMETER_GROUP );
			paramGroup.setName( handle.getName( ) );
			paramGroup.setDisplayName( handle.getDisplayName( ) );
			paramGroup.setDisplayNameKey( handle.getDisplayNameKey( ) );
			paramGroup.setHelpText( handle.getHelpText( ) );
			paramGroup.setHelpTextKey( handle.getHelpTextKey( ) );
			paramGroup.setPromptText( handle.getPromptText( ) );
			DataSetHandle dset = handle.getDataSet( );
			if ( dset != null )
			{
				paramGroup.setDataSet( dset.getName( ) );
			}
			SlotHandle parameters = handle.getParameters( );

			// set custom properties
			List properties = handle.getUserProperties( );
			for ( int i = 0; i < properties.size( ); i++ )
			{
				UserPropertyDefn p = (UserPropertyDefn) properties.get( i );
				paramGroup.addUserProperty( p.getName( ), handle.getProperty( p
						.getName( ) ) );
			}

			int size = parameters.getCount( );
			for ( int n = 0; n < size; n++ )
			{
				apply( parameters.get( n ) );
				if ( currentElement != null )
				{
					paramGroup.addParameter( (IParameterDefnBase) currentElement );
				}
			}

			currentElement = paramGroup;

		}

		public void visitScalarParameter( ScalarParameterHandle handle )
		{
			assert ( handle.getName( ) != null );
			// Create Parameter
			ScalarParameterDefn scalarParameter = new ScalarParameterDefn( );
			scalarParameter.setHandle( handle );
			scalarParameter.setParameterType( IParameterDefnBase.SCALAR_PARAMETER );
			scalarParameter.setName( handle.getName( ) );

			// set custom properties
			List properties = handle.getUserProperties( );
			for ( int i = 0; i < properties.size( ); i++ )
			{
				UserPropertyDefn p = (UserPropertyDefn) properties.get( i );
				scalarParameter.addUserProperty( p.getName( ), handle
						.getProperty( p.getName( ) ) );
			}
			String align = handle.getAlignment( );
			if ( DesignChoiceConstants.SCALAR_PARAM_ALIGN_CENTER.equals( align ) )
				scalarParameter.setAlignment( IScalarParameterDefn.CENTER );
			else if ( DesignChoiceConstants.SCALAR_PARAM_ALIGN_LEFT.equals( align ) )
				scalarParameter.setAlignment( IScalarParameterDefn.LEFT );
			else if ( DesignChoiceConstants.SCALAR_PARAM_ALIGN_RIGHT.equals( align ) )
				scalarParameter.setAlignment( IScalarParameterDefn.RIGHT );
			else
				scalarParameter.setAlignment( IScalarParameterDefn.AUTO );

			scalarParameter.setAllowBlank( handle.allowBlank( ) );
			scalarParameter.setAllowNull( handle.allowNull( ) );
			scalarParameter.setIsRequired( handle.isRequired( ) );

			String controlType = handle.getControlType( );
			if ( DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX.equals( controlType ) )
				scalarParameter.setControlType( IScalarParameterDefn.CHECK_BOX );
			else if ( DesignChoiceConstants.PARAM_CONTROL_LIST_BOX
					.equals( controlType ) )
				scalarParameter.setControlType( IScalarParameterDefn.LIST_BOX );
			else if ( DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON
					.equals( controlType ) )
				scalarParameter.setControlType( IScalarParameterDefn.RADIO_BUTTON );
			else
				scalarParameter.setControlType( IScalarParameterDefn.TEXT_BOX );

			scalarParameter.setDefaultValue( handle.getDefaultValue( ) );
			scalarParameter.setDisplayName( handle.getDisplayName( ) );
			scalarParameter.setDisplayNameKey( handle.getDisplayNameKey( ) );

			scalarParameter.setFormat( handle.getPattern( ) );
			scalarParameter.setHelpText( handle.getHelpText( ) );
			scalarParameter.setHelpTextKey( handle.getHelpTextKey( ) );
			scalarParameter.setPromptText( handle.getPromptText( ) );
			scalarParameter.setPromptTextKey( handle.getPromptTextID( ) );
			scalarParameter.setIsHidden( handle.isHidden( ) );
			scalarParameter.setName( handle.getName( ) );

			String valueType = handle.getDataType( );
			if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( valueType ) )
				scalarParameter.setDataType( IScalarParameterDefn.TYPE_BOOLEAN );
			else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( valueType ) )
				scalarParameter.setDataType( IScalarParameterDefn.TYPE_DATE_TIME );
			else if ( DesignChoiceConstants.PARAM_TYPE_DATE.equals( valueType ) )
				scalarParameter.setDataType( IScalarParameterDefn.TYPE_DATE );
			else if ( DesignChoiceConstants.PARAM_TYPE_TIME.equals( valueType ) )
				scalarParameter.setDataType( IScalarParameterDefn.TYPE_TIME );
			else if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( valueType ) )
				scalarParameter.setDataType( IScalarParameterDefn.TYPE_DECIMAL );
			else if ( DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( valueType ) )
				scalarParameter.setDataType( IScalarParameterDefn.TYPE_FLOAT );
			else if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( valueType ) )
				scalarParameter.setDataType( IScalarParameterDefn.TYPE_STRING );
			else if ( DesignChoiceConstants.PARAM_TYPE_INTEGER.equals( valueType ) )
				scalarParameter.setDataType( IScalarParameterDefn.TYPE_INTEGER );
			else
				scalarParameter.setDataType( IScalarParameterDefn.TYPE_ANY );

			ArrayList values = new ArrayList( );
			Iterator selectionIter = handle.choiceIterator( );
			while ( selectionIter.hasNext( ) )
			{
				SelectionChoiceHandle selection = (SelectionChoiceHandle) selectionIter
						.next( );
				ParameterSelectionChoice selectionChoice = new ParameterSelectionChoice(
						this.handle );
				selectionChoice.setLabel( selection.getLabelKey( ), selection
						.getLabel( ) );
				selectionChoice.setValue( selection.getValue( ), scalarParameter
						.getDataType( ) );
				values.add( selectionChoice );
			}
			scalarParameter.setSelectionList( values );
			scalarParameter.setAllowNewValues( !handle.isMustMatch( ) );
			scalarParameter.setFixedOrder( handle.isFixedOrder( ) );

			String paramType = handle.getValueType( );
			if ( IScalarParameterDefn.SELECTION_LIST_TYPE_STATIC.equals( paramType )
					&& scalarParameter.getSelectionList( ) != null
					&& scalarParameter.getSelectionList( ).size( ) > 0 )
			{
				scalarParameter
						.setSelectionListType( IScalarParameterDefn.SELECTION_LIST_STATIC );
			}
			else if ( IScalarParameterDefn.SELECTION_LIST_TYPE_DYNAMIC
					.equals( paramType ) )
			{
				scalarParameter
						.setSelectionListType( IScalarParameterDefn.SELECTION_LIST_DYNAMIC );
			}
			else
			{
				scalarParameter
						.setSelectionListType( IScalarParameterDefn.SELECTION_LIST_NONE );
			}
			scalarParameter.setValueConcealed( handle.isConcealValue( ) );
			currentElement = scalarParameter;
		}

	}
}
