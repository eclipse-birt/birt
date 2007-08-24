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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.api.EngineException;
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
import org.eclipse.birt.report.model.api.ParamBindingHandle;
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

	private static final String VALUE_PREFIX = "__VALUE__";

	private static final String LABEL_PREFIX = "__LABEL__";

	// stores all parameter definitions. Each task clones the parameter
	// definition information
	// so that Engine IR (repor runnable) can keep a task-independent of the
	// parameter definitions.
	protected Collection parameterDefns = null;

	protected HashMap dataCache = null;

//	protected HashMap labelMap = null;

//	protected HashMap valueMap = null;

	private List labelColumnBindingNames = null;
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

	/**
	 * get selection choices from the data set.
	 * 
	 * @param dataSetName
	 *            data set name
	 * @param labelStmt
	 *            label statement
	 * @param valueStmt
	 *            value statement
	 * @param dataType
	 *            value type
	 * @return
	 */
	private Collection createDynamicSelectionChoices( String pattern,
			String dataSetName, String labelStmt, String valueStmt,
			String dataType, int limit, boolean fixedOrder, boolean isDistinct,
			String sortDirection, String sortBy )
	{
		boolean sortDirectionValue = "asc".equalsIgnoreCase( sortDirection );
		boolean sortByLabel = "label".equalsIgnoreCase( sortBy );

		ArrayList choices = new ArrayList( );
		ReportDesignHandle report = (ReportDesignHandle) this.runnable
				.getDesignHandle( );

		DataSetHandle dataSet = report.findDataSet( dataSetName );
		if ( dataSet != null )
		{
			try
			{
				IDataEngine dataEngine = executionContext.getDataEngine( );
				DataRequestSession dteSession = getDataSession();
				// Define data source and data set
				dataEngine.defineDataSet(dataSet);
				ScriptExpression labelExpr = null;
				if ( labelStmt != null && labelStmt.length( ) > 0 )
				{
					labelExpr = new ScriptExpression( labelStmt );
				}
				ScriptExpression valueExpr = new ScriptExpression( valueStmt );

				QueryDefinition queryDefn = new QueryDefinition( );
				queryDefn.setDataSetName( dataSetName );
				if( limit > 0)
				{
					queryDefn.setMaxRows( limit );
				}

				// add parameters if have any
				Iterator paramIter = dataSet.paramBindingsIterator( );
				while ( paramIter.hasNext( ) )
				{
					ParamBindingHandle binding = (ParamBindingHandle) paramIter
							.next( );
					String paramName = binding.getParamName( );
					String paramExpr = binding.getExpression( );
					queryDefn.getInputParamBindings( ).add(
							new InputParameterBinding( paramName,
									new ScriptExpression( paramExpr ) ) );
				}
				
				String labelColumnName = LABEL_PREFIX;;
				String valueColumnName = VALUE_PREFIX;;
				
				if ( labelExpr != null )
				{
					IBinding binding = new Binding( labelColumnName, labelExpr );
					queryDefn.addBinding( binding );
				}
				
				IBinding binding = new Binding( valueColumnName, valueExpr );
				queryDefn.addBinding( binding );
				
				queryDefn.setAutoBinding( true );

				IPreparedQuery query = dteSession.prepare( queryDefn,
						getAppContext( ) );

				IQueryResults result = query.execute( executionContext
						.getSharedScope( ) );
				IResultIterator iter = result.getResultIterator( );
				int count = 0;
				Set checkPool = new HashSet( );
				while ( iter.next( ) )
				{
					String label = null;
					if ( labelExpr != null )
					{
						label = iter.getString( labelColumnName );
					}
					Object value = iter.getValue( valueColumnName );
										
					value = convertToType( value, dataType );

					// skip duplicated values.
					if ( isDistinct )
					{
						if ( !checkPool.contains( value ) )
						{
							checkPool.add( value );
							choices.add( new SelectionChoice( label, value ) );
							count++;
						}
					}
					else
					{
						choices.add( new SelectionChoice( label, value ) );
						count++;
					}
					if ( ( limit != 0 ) && ( count >= limit ) )
					{
						break;
					}
				}
			}
			catch ( BirtException ex )
			{
				log.log( Level.WARNING, ex.getMessage( ), ex );
				executionContext.addException( ex );
			}
		}
		if ( !fixedOrder )
			Collections.sort( choices, new SelectionChoiceComparator(
					sortByLabel, pattern, sortDirectionValue, ULocale
							.forLocale( locale ) ) );
		return choices;

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
	 * The first step to work with the cascading parameters. Create the query
	 * definition, prepare and execute the query. Cache the iterator of the
	 * result set and also cache the IBaseExpression used in the prepare.
	 * 
	 * @param parameterGroupName -
	 *            the cascading parameter group name
	 */
	private void evaluateGroupQuery( String parameterGroupName )
	{
		CascadingParameterGroupHandle parameterGroup = getCascadingParameterGroup( parameterGroupName );

		if ( dataCache == null )
			dataCache = new HashMap( );

		if ( parameterGroup == null )
			return;

		//If a IResultIterator with the same name has already existed in the dataCache,
		//this IResultIterator and its IQueryResults should be closed.
		IResultIterator iterOld = (IResultIterator) dataCache.get( parameterGroup.getName( ) );
		if ( iterOld != null )
		{
			dataCache.remove( parameterGroup.getName( ) );
			try
			{
				IQueryResults iresultOld = iterOld.getQueryResults();
				iterOld.close( );
				iresultOld.close( );
			}
			catch ( BirtException ex )
			{
				log.log( Level.WARNING, ex.getMessage( ) );
			}
		}
		
		DataSetHandle dataSet = parameterGroup.getDataSet( );
		if ( dataSet != null )
		{
			try
			{
				// Handle data source and data set
				DataRequestSession dteSession = getDataSession( );
				IDataEngine dataEngine = executionContext.getDataEngine( );
				dataEngine.defineDataSet( dataSet );

				QueryDefinition queryDefn = new QueryDefinition( );
				queryDefn.setDataSetName( dataSet.getQualifiedName( ) );
				SlotHandle parameters = parameterGroup.getParameters( );
				Iterator iter = parameters.iterator( );

/*				if ( labelMap == null )
					labelMap = new HashMap( );
				if ( valueMap == null )
					valueMap = new HashMap( );*/
				
				if ( labelColumnBindingNames == null )
					labelColumnBindingNames = new ArrayList();
				
				while ( iter.hasNext( ) )
				{
					Object param = iter.next( );
					if ( param instanceof ScalarParameterHandle )
					{
						String valueExpString = ( (ScalarParameterHandle) param )
								.getValueExpr( );
						ScriptExpression valueExpObject = new ScriptExpression(
								valueExpString );
						
						String keyValue = VALUE_PREFIX+parameterGroup.getName( ) + "_"
						+ ( (ScalarParameterHandle) param ).getName( );
						
	/*					valueMap.put( keyValue,
								valueExpObject );*/
						IBinding binding = new Binding( keyValue, valueExpObject );
						queryDefn.addBinding( binding );
						//queryDefn.getRowExpressions( ).add( valueExpObject );

						String labelExpString = ( (ScalarParameterHandle) param )
								.getLabelExpr( );

						if ( labelExpString != null
								&& labelExpString.length( ) > 0 )
						{
							ScriptExpression labelExpObject = new ScriptExpression(
									labelExpString );
							
							String keyLabel = LABEL_PREFIX+parameterGroup.getName( ) + "_"
							+ ( (ScalarParameterHandle) param ).getName( );
	/*						labelMap.put( keyLabel, labelExpObject );
							queryDefn.getRowExpressions( ).add( labelExpObject );*/
							labelColumnBindingNames.add( keyLabel );
							IBinding labelBinding = new Binding( keyLabel, labelExpObject );
							queryDefn.addBinding( labelBinding );
						}
					}
				}

				queryDefn.setAutoBinding( true );
				
				IPreparedQuery query = dteSession.prepare( queryDefn,
						getAppContext( ) );
				IQueryResults result = query.execute( executionContext
						.getSharedScope( ) );
				IResultIterator resultIter = result.getResultIterator( );
				dataCache.put( parameterGroup.getName( ), resultIter );
				return;
			}
			catch ( BirtException ex )
			{
				log.log( Level.WARNING, ex.getMessage( ), ex );
				executionContext.addException( ex );
			}
		}

		dataCache.put( parameterGroup.getName( ), null );
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

	private Collection getChoicesFromParameterGroup( ScalarParameterHandle parameter, Object[] groupKeyValues )
	{
		assert isCascadingParameter( parameter );
		String paramDataType = parameter.getDataType( );
		CascadingParameterGroupHandle parameterGroup = getCascadingGroup( parameter );
		String parameterGroupName = parameterGroup.getName( );
		IResultIterator iter = null;
		if ( dataCache != null )
		{
			iter = (IResultIterator) dataCache.get( parameterGroupName );
		}
		if ( iter == null )
		{
			evaluateGroupQuery( parameterGroupName );
			iter = (IResultIterator) dataCache.get( parameterGroupName );
			if ( iter == null )
			{
				return Collections.EMPTY_LIST;
			}
		}
		
		// get the group's value name and data type if the parameter in one or
		// more groups.
		SlotHandle parameterSlots = parameterGroup.getParameters( );
		String[] groupValueNames = new String[groupKeyValues.length];
		String[] groupTypes = new String[groupKeyValues.length];
		for ( int i = 0; i < groupKeyValues.length; i++ )
		{
			ScalarParameterHandle tempParameter = (ScalarParameterHandle) parameterSlots
					.get( i );
			if ( tempParameter == parameter )
			{
				break;
			}
			groupValueNames[i] = VALUE_PREFIX + parameterGroupName + "_"
					+ tempParameter.getName( );
			groupTypes[i] = tempParameter.getDataType( );
		}

		String labelColumnName = LABEL_PREFIX + parameterGroupName + "_" + parameter.getName( );
		String valueColumnName = VALUE_PREFIX + parameterGroupName + "_" + parameter.getName( );
			
		int listLimit = parameter.getListlimit( );
		ArrayList choices = new ArrayList( );
		try
		{
			int count = 0;
			
			Set checkPool = new HashSet( );
			while ( iter.next( ) )
			{
				String label = (  labelColumnBindingNames.contains( labelColumnName )
						? iter.getString( labelColumnName )
						: null );
				Object value = iter.getValue( valueColumnName );
				value = convertToType( value, paramDataType );

				// skip duplicated values.
				if ( !checkPool.contains( value ) )
				{
					boolean isInGroup = checkInGroup( groupKeyValues,
							groupValueNames, groupTypes, iter );
					if ( isInGroup )
					{
						checkPool.add( value );
						choices.add( new SelectionChoice( label, value ) );
						count++;
					}
				}
				if ( ( listLimit != 0 ) && ( count >= listLimit ) )
				{
					break;
				}
			}
		}
		catch ( BirtException e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
			executionContext.addException( e );
		}
		if ( !parameter.isFixedOrder( ) )
			Collections.sort( choices, new SelectionChoiceComparator( true, parameter.getPattern( ), ULocale.forLocale( locale ) ) );
		return choices;
	}
	
	/**
	 * Check if the 
	 * @param groupKeyValues
	 * @param groupValueNames
	 * @param groupTypes
	 * @param iter
	 * @return
	 * @throws BirtException
	 */
	private boolean checkInGroup( Object[] groupKeyValues,
			String[] groupValueNames, String[] groupTypes, IResultIterator iter )
			throws BirtException
	{
		for ( int i = 0; i < groupValueNames.length; i++ )
		{
			Object valueParent = iter.getValue( groupValueNames[i] );
			valueParent = convertToType( valueParent, groupTypes[i] );
			if ( ( valueParent == null && valueParent == groupKeyValues[i] )
					|| ( valueParent != null && !valueParent
							.equals( groupKeyValues[i] ) ) )
			{
				return false;
			}
		}
		return true;
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
	}
	
	public void close( )
	{
		if ( dataCache != null )
		{
			Iterator it = dataCache.entrySet( ).iterator( );
	        while (it.hasNext())
	        {
	            Map.Entry entry = (Map.Entry)it.next();
	            IResultIterator iter = (IResultIterator) entry.getValue();
	            if( null == iter)
	            	continue;
	            try
				{
					IQueryResults iresult = iter.getQueryResults();
					iter.close( );
					iresult.close( );
				}
				catch ( BirtException ex )
				{
					log.log( Level.WARNING, ex.getMessage( ) );
				}
	        }
			dataCache.clear( );
			dataCache = null;
		}
		super.close( );
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
		String dataType = parameter.getDataType( );
		boolean fixedOrder = parameter.isFixedOrder( );
		String dataSetName = parameter.getDataSetName( );
		String valueExpr = parameter.getValueExpr( );
		String labelExpr = parameter.getLabelExpr( );
		boolean isDistinct = parameter.distinct( );
		String sortDirection = parameter.getSortDirection( );
		String sortBy = parameter.getSortBy( );
		int limit = parameter.getListlimit( );
		String pattern = parameter.getPattern( );

		return createDynamicSelectionChoices( pattern, dataSetName, labelExpr,
				valueExpr, dataType, limit, fixedOrder, isDistinct,
				sortDirection, sortBy );

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
