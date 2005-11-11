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
import org.eclipse.birt.report.engine.adapter.ModelDteApiAdapter;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SelectionChoiceHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

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

	protected HashMap dataCache = null;
	protected HashMap labelMap = null;
	protected HashMap valueMap = null;

	/**
	 * @param engine
	 *            reference to the report engine
	 * @param runnable
	 *            the runnable report design
	 */
	public GetParameterDefinitionTask( ReportEngine engine,
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
			catch ( CloneNotSupportedException e ) // This is a Java exception
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
					( (ScalarParameterDefn) pBase ).setReportDesign( runnable
							.getDesignHandle( ).getDesign( ) );
					( (ScalarParameterDefn) pBase ).setLocale( locale );
					( (ScalarParameterDefn) pBase ).evaluateSelectionList( );
				}
				else if ( pBase instanceof ParameterGroupDefn )
				{
					Iterator iter2 = ( (ParameterGroupDefn) pBase )
							.getContents( ).iterator( );
					while ( iter2.hasNext( ) )
					{
						IParameterDefnBase p = (IParameterDefnBase) iter2
								.next( );
						if ( p instanceof ScalarParameterDefn )
						{
							( (ScalarParameterDefn) p )
									.setReportDesign( runnable
											.getDesignHandle( ).getDesign( ) );
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
				.getParameterDefns( false );
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
				( (ScalarParameterDefn) ret ).setReportDesign( runnable
						.getDesignHandle( ).getDesign( ) );
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
						( (ScalarParameterDefn) p ).setReportDesign( runnable
								.getDesignHandle( ).getDesign( ) );
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
				Object value = evaluate( expr, type );
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
		if (param == null)
			return null;
		else
			return getDefaultValue( param.getName() );
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
		return evaluate( expr, parameter.getDataType( ) );
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
			return null;
		}
		String selectionType = parameter.getValueType( );
		String dataType = parameter.getDataType( );
        boolean fixedOrder = parameter.isFixedOrder();
		if (DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC.equals(selectionType))
		{
			String dataSetName = parameter.getDataSetName();
			String valueExpr = parameter.getValueExpr();
			String labelExpr = parameter.getLabelExpr();
            int limit = parameter.getListlimit();
		
			return createDynamicSelectionChoices(dataSetName, labelExpr, valueExpr, dataType, limit, fixedOrder);
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
				if ( label != null )
				{
					label = choice.getLabel( );
				}
				Object value = getStringValue( choice.getValue( ), dataType );
				choices.add( new SelectionChoice( label, value ) );
			}
            if(!fixedOrder)
                Collections.sort(choices, new SelectionChoiceComparator(true));
			return choices;
            
		}
		return null;
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
	private Collection createDynamicSelectionChoices(String dataSetName, String labelStmt, String valueStmt, String dataType, int limit, boolean fixedOrder)
	{
		ArrayList choices = new ArrayList( );
		ReportDesignHandle report = (ReportDesignHandle) this.runnable
				.getDesignHandle( );
		
		DataSetHandle dataSet = report.findDataSet( dataSetName );
		if ( dataSet != null )
		{
			try
			{
				DataEngine dataEngine = getDataEngine( );

				// Define data source and data set
				DataSourceHandle dataSource = dataSet.getDataSource( );
				try
				{
					dataEngine
							.defineDataSource( ModelDteApiAdapter.getInstance( )
									.createDataSourceDesign( dataSource ) );
					dataEngine.defineDataSet( ModelDteApiAdapter.getInstance( )
							.createDataSetDesign( dataSet ) );
				}
				catch ( BirtException e )
				{
					log.log( Level.SEVERE, e.getMessage());
				}				
                ScriptExpression labelExpr = null;
				if(labelStmt!=null && labelStmt.length()>0)
                {
				    labelExpr = new ScriptExpression(labelStmt);
                }
				ScriptExpression valueExpr = new ScriptExpression(valueStmt);
				
				QueryDefinition queryDefn = new QueryDefinition();
				queryDefn.setDataSetName(dataSetName);
				
				//add parameters if have any
				Iterator paramIter = dataSet.paramBindingsIterator();
				while (paramIter.hasNext())
				{
					ParamBindingHandle binding = (ParamBindingHandle) paramIter
							.next( );
					String paramName = binding.getParamName( );
					String paramExpr = binding.getExpression( );
					queryDefn.getInputParamBindings( ).add(
							new InputParameterBinding( paramName,
									new ScriptExpression( paramExpr ) ) );
				}
                if(labelExpr!=null)
                {
                    queryDefn.getRowExpressions().add(labelExpr);
                }
				queryDefn.getRowExpressions().add(valueExpr);
				
				// Create a group to skip all of the duplicate values
				GroupDefinition groupDef = new GroupDefinition( );
				groupDef.setKeyExpression( valueStmt );
				queryDefn.addGroup( groupDef );

				IPreparedQuery query = dataEngine.prepare(queryDefn);
				
				IQueryResults result = query.execute(executionContext.getSharedScope());
				IResultIterator iter = result.getResultIterator();
                int count = 0;
				while (iter.next())
				{
                    String label = null;
                    if(labelExpr!=null)
                    {
						label = iter.getString(labelExpr);
                    }
					Object value = iter.getValue(valueExpr);
					choices.add(new SelectionChoice(label, convertToType(value, dataType)));
                    count++;
                    if ( (limit != 0) &&
                         (count >= limit) )
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
        if(!fixedOrder)
            Collections.sort(choices, new SelectionChoiceComparator(true));
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

		DataSetHandle dataSet = parameterGroup.getDataSet( );
		if ( dataSet != null )
		{
			try
			{
				// Handle data source and data set
				DataEngine dataEngine = getDataEngine( );
				DataSourceHandle dataSource = dataSet.getDataSource( );
				try
				{
					dataEngine
							.defineDataSource( ModelDteApiAdapter.getInstance( )
									.createDataSourceDesign( dataSource ) );
					dataEngine.defineDataSet( ModelDteApiAdapter.getInstance( )
							.createDataSetDesign( dataSet ) );
				}
				catch ( BirtException e )
				{
					log.log( Level.SEVERE, e.getMessage( ) );
				}

				QueryDefinition queryDefn = new QueryDefinition( );
				queryDefn.setDataSetName( dataSet.getName( ) );
				SlotHandle parameters = parameterGroup.getParameters( );
				Iterator iter = parameters.iterator( );

				if ( labelMap == null )
					labelMap = new HashMap( );
				if ( valueMap == null )
					valueMap = new HashMap( );

				while ( iter.hasNext( ) )
				{
					Object param = iter.next( );
					if ( param instanceof ScalarParameterHandle )
					{
						String valueExpString = ( (ScalarParameterHandle) param )
								.getValueExpr( );
						Object valueExpObject = new ScriptExpression(
								valueExpString );
						valueMap.put( parameterGroup.getName( ) + "_"
								+ ( (ScalarParameterHandle) param ).getName( ),
								valueExpObject );
						queryDefn.getRowExpressions( ).add( valueExpObject );

						String labelExpString = ((ScalarParameterHandle)param).getLabelExpr();
						
						if (labelExpString != null && labelExpString.length() > 0)
						{
    						Object labelExpObject = new ScriptExpression(labelExpString);
    						labelMap.put(parameterGroup.getName() + "_" + ((ScalarParameterHandle)param).getName(), labelExpObject);
    						queryDefn.getRowExpressions().add( labelExpObject );
						}
						
						GroupDefinition groupDef = new GroupDefinition( );
						groupDef.setKeyExpression( valueExpString );
						queryDefn.addGroup( groupDef );
			}
		}
		
				IPreparedQuery query = dataEngine.prepare( queryDefn );
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
			return null;

		IResultIterator iter = (IResultIterator) dataCache.get( parameterGroup
				.getName( ) );
		if ( iter == null )
			return null;

		SlotHandle slotHandle = parameterGroup.getParameters( );
		assert ( groupKeyValues.length < slotHandle.getCount( ) );
		int skipLevel = groupKeyValues.length + 1;
		
		ScalarParameterHandle requestedParam =  (ScalarParameterHandle) slotHandle.get( groupKeyValues.length ); // The parameters in parameterGroup must be scalar parameters.
		int listLimit = requestedParam.getListlimit();
        boolean fixedOrder = requestedParam.isFixedOrder();
		String valueType = requestedParam.getDataType();
		// We need to cache the expression object in function evaluateQuery and 
		// use the cached object here instead of creating a new one because 
		// according to DtE API for IResultIterator.getString, 
		// the expression object must be the same object created at the time of preparation. 
		// Actually, the prepare process will modify the expression object , only after which 
		// the expression object can be used to get the real value from the result set. 
		// If we create a new expression object here, it won't work.
		ScriptExpression labelExpr = (ScriptExpression) labelMap
				.get( parameterGroup.getName( ) + "_"
						+ requestedParam.getName( ) );
		ScriptExpression valueExpr = (ScriptExpression) valueMap
				.get( parameterGroup.getName( ) + "_"
						+ requestedParam.getName( ) );

		ArrayList choices = new ArrayList( );
		try
				{
			if ( skipLevel > 1 )
				iter.findGroup( groupKeyValues );

			int startGroupLevel = skipLevel -1;
			int count = 0;
			while ( iter.next( ) )
			{
				//startGroupLevel = iter.getStartingGroupLevel();
				String label = (labelExpr != null ? iter.getString( labelExpr ) : null);
				Object value = iter.getValue( valueExpr );
				//value = convertToType( value, valueType );
				choices.add( new SelectionChoice( label, value ) );
				count++;
				if ( ( listLimit != 0 ) && ( count >= listLimit ) )
					break;

				iter.skipToEnd( skipLevel );
				
				int endGroupLevel = iter.getEndingGroupLevel();
				if (endGroupLevel <= startGroupLevel)
					{
					break;
					}
				}
			}
		catch ( BirtException e )
		{
			e.printStackTrace( );
		}
        if(!fixedOrder)
            Collections.sort(choices, new SelectionChoiceComparator(true));
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
}
