/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
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

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SelectionChoiceHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
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
		super( engine, runnable );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IGetParameterDefinitionTask#getParameterDefns(boolean)
	 */
	public Collection getParameterDefns( boolean includeParameterGroups )
	{
		Collection original = ( (ReportRunnable) runnable )
				.getParameterDefns( includeParameterGroups );
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

		Collection original = ( (ReportRunnable) runnable )
				.getParameterDefns( true );
		Iterator iter = original.iterator( );

		while ( iter.hasNext( ) )
		{
			ParameterDefnBase pBase = (ParameterDefnBase) iter.next( );
			if ( name.equals( pBase.getName( ) ) )
			{
				try
				{
					ret = (IParameterDefnBase) pBase.clone( );
					break;
				}
				catch ( CloneNotSupportedException e ) // This is a Java
				// exception
				{
					log.log( Level.SEVERE, e.getMessage( ), e );
				}
			}
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
		if ( expr == null || expr.length( ) == 0 )
		{
			return null;
		}
		return convertToType( expr, parameter.getDataType( ) );
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
		if ( DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC
				.equals( selectionType ) )
		{
			if ( parameter.getDataSetName( ) != null )
			{
				return getChoicesFromParameterQuery( parameter );
			}
			else if ( isCascadingParameter( parameter ))
			{
				Object[] parameterValuesAhead =  getParameterValuesAhead( parameter );
				return getChoicesFromParameterGroup ( parameter, parameterValuesAhead );
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
				Object value = getStringValue( choice.getValue( ), dataType );
				choices.add( new SelectionChoice( label, value ) );
			}
			if ( !fixedOrder )
				Collections
						.sort( choices, new SelectionChoiceComparator( true, parameter.getPattern( ), ULocale.forLocale( locale ) ) );
			return choices;

		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * convert the string to value.
	 * 
	 * @param value
	 *            value string
	 * @param valueType
	 *            value type
	 * @return object with the specified value
	 */
	private Object getStringValue( String value, String valueType )
	{
		try
		{
			if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( valueType ) )
				return DataTypeUtil.toBoolean( value );
			if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( valueType ) )
				return DataTypeUtil.toDate( value );
			if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( valueType ) )
				return DataTypeUtil.toBigDecimal( value );
			if ( DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( valueType ) )
				return DataTypeUtil.toDouble( value );
		}
		catch ( BirtException e )
		{
			log.log( Level.SEVERE, e.getLocalizedMessage( ), e );
		}
		return value;
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
	private Collection createDynamicSelectionChoices( String pattern, String dataSetName,
			String labelStmt, String valueStmt, String dataType, int limit,
			boolean fixedOrder )
	{
		ArrayList choices = new ArrayList( );
		ReportDesignHandle report = (ReportDesignHandle) this.runnable
				.getDesignHandle( );

		DataSetHandle dataSet = report.findDataSet( dataSetName );
		if ( dataSet != null )
		{
			try
			{
				IDataEngine dataEngine = executionContext.getDataEngine( );
				DataEngine dteDataEngine = getDataEngine();
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
					queryDefn.addResultSetExpression( labelColumnName, labelExpr );
				}
				
				queryDefn.addResultSetExpression( valueColumnName, valueExpr );
				
				// Create a group to skip all of the duplicate values
				GroupDefinition groupDef = new GroupDefinition( );
				groupDef.setKeyColumn( valueColumnName );
				queryDefn.addGroup( groupDef );
				
				queryDefn.setAutoBinding( true );

				IPreparedQuery query = dteDataEngine.prepare( queryDefn, this.appContext );

				IQueryResults result = query.execute( executionContext
						.getSharedScope( ) );
				IResultIterator iter = result.getResultIterator( );
				int count = 0;
				while ( iter.next( ) )
				{
					String label = null;
					if ( labelExpr != null )
					{
						label = iter.getString( labelColumnName );
					}
					Object value = iter.getValue( valueColumnName );
					choices.add( new SelectionChoice( label, convertToType(
							value, dataType ) ) );
					count++;
					if ( ( limit != 0 ) && ( count >= limit ) )
					{
						break;
					}
					iter.skipToEnd( 1 ); // Skip all of the duplicate values
				}
			}
			catch ( BirtException ex )
			{
				ex.printStackTrace( );
			}
		}
		if ( !fixedOrder )
			Collections.sort( choices, new SelectionChoiceComparator( true, pattern, ULocale.forLocale( locale ) ) );
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
				DataEngine dteDataEngine = getDataEngine( );
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
						
						queryDefn.addResultSetExpression( keyValue, valueExpObject );
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
							queryDefn.addResultSetExpression( keyLabel, labelExpObject );
						}

						GroupDefinition groupDef = new GroupDefinition( );
						groupDef.setKeyExpression( valueExpString );
						queryDefn.addGroup( groupDef );
					}
				}

				queryDefn.setAutoBinding( true );
				
				IPreparedQuery query = dteDataEngine.prepare( queryDefn, this.appContext );
				IQueryResults result = query.execute( executionContext
						.getSharedScope( ) );
				IResultIterator resultIter = result.getResultIterator( );
				dataCache.put( parameterGroup.getName( ), resultIter );
				return;
			}
			catch ( BirtException ex )
			{
				ex.printStackTrace( );
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
		CascadingParameterGroupHandle parameterGroup = getCascadingGroup( parameter );
		String parameterGroupName = parameterGroup.getName( );
			evaluateQuery( parameterGroupName );
		IResultIterator iter = (IResultIterator) dataCache.get( parameterGroupName );
		if ( iter == null )
		{
			evaluateQuery( parameterGroupName );
			if ( iter == null )
			{
				return Collections.EMPTY_LIST;
			}
		}

		String labelColumnName = LABEL_PREFIX + parameterGroupName + "_" + parameter.getName( );
		String valueColumnName = VALUE_PREFIX + parameterGroupName + "_" + parameter.getName( );
			
		int listLimit = parameter.getListlimit( );
		ArrayList choices = new ArrayList( );
		int skipLevel = groupKeyValues.length + 1;
		try
		{
			if ( skipLevel > 1 )
				iter.findGroup( groupKeyValues );

			int startGroupLevel = skipLevel - 1;
			int count = 0;
			while ( iter.next( ) )
			{
				// startGroupLevel = iter.getStartingGroupLevel();
				String label = (  labelColumnBindingNames.contains( labelColumnName )
						? iter.getString( labelColumnName )
						: null );
				Object value = iter.getValue( valueColumnName );
				// value = convertToType( value, valueType );
				choices.add( new SelectionChoice( label, value ) );
				count++;
				if ( ( listLimit != 0 ) && ( count >= listLimit ) )
					break;

				iter.skipToEnd( skipLevel );

				int endGroupLevel = iter.getEndingGroupLevel( );
				if ( endGroupLevel <= startGroupLevel )
				{
					break;
				}
			}
		}
		catch ( BirtException e )
		{
			e.printStackTrace( );
		}
		if ( !parameter.isFixedOrder( ) )
			Collections.sort( choices, new SelectionChoiceComparator( true, parameter.getPattern( ), ULocale.forLocale( locale ) ) );
		return choices;
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
		int limit = parameter.getListlimit( );
		String pattern = parameter.getPattern( );

		return createDynamicSelectionChoices( pattern, dataSetName, labelExpr,
				valueExpr, dataType, limit, fixedOrder );
	}
}
