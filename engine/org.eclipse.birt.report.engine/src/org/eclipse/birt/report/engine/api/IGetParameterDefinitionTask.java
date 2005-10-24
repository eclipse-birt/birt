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

package org.eclipse.birt.report.engine.api;

import java.util.Collection;
import java.util.HashMap;

import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;

/**
 * an engine task that retrieves parameter definitions from a report. The task retrieves parameter 
 * definitions, default values and dynamic selection lists. 
 */
public interface IGetParameterDefinitionTask extends IEngineTask {
	
	/**
	 * return report parameter definitions
	 * 
	 * @param includeParameterGroups
	 *            whether returns one level of parameters with parameter groups
	 *            or return a flatten collection of all parameters
	 * @return if includeParameterGroups = true, an ordered collection of report
	 *         parameters. Each item in the colleciton is of type
	 *         IParameterDefnBase if includeParameterGroups = false, a
	 *         collection of parameters. Each item in the collection is of type
	 *         IParameterDefn.
	 */
	public abstract Collection getParameterDefns(boolean includeParameterGroups);

	/**
	 * returns the parameter definition given the parameter name name
	 * 
	 * @param name the parameter name
	 * @return the definition
	 */
	public abstract IParameterDefnBase getParameterDefn(String name);
	
	
	/**
	 * evaluates the default value expressions for defined parameters. this function may cause the runtime environment 
	 * to be set up, and default parameter expressions being evaluated. If getDefaultValue is called on a IScalarParameterDefn 
	 * right after getParameterDefns without calling this function, the default value is null.
	 * 
	 * @throws EngineException throws exception when there is an error evaluating default value expressions
	 * @deprecated use getDefaultParameterValues() directly.
	 */
	public abstract void evaluateDefaults() throws EngineException;
	
	/**
	 * runs the query associated with a parameter with a given name, and fills the dynamic parameter list. 
	 * Before calling this function, other parameter values may have been set. If getSelectionList is called 
	 * on the report parameter before this function is called, the function returns null if the parameter 
	 * has a dynamic selection list; if it is called after this function, getSelectionList returns the 
	 * selection list. 
	 * 
	 * @param parameterName the name of the parameter
	 * @throws EngineException throws exception if the query for dynamic selection list has problem to run 
	 */
	//public abstract void fillDynamicSelectionList(String parameterName) throws EngineException;

	/**
	 * Set one parameter value. If parameter does not exist in report design, do nothing. 
	 * 
	 * @param name the parameter
	 * @param value the value of the parameter
	 */
	public void setValue( String name, Object value );

	/**
	 * get all default values
	 * 
	 * @return the default value map
	 */
	public HashMap getDefaultValues();
	
	/**
	 * get default value by parameter name
	 * 
	 * @param param reference to a parameter 
	 * @return default value for a parameter
	 */
	public Object getDefaultValue(IParameterDefnBase param);
	
	/**
	 * get parameter definitions.
	 * @param name parameter name
	 * @return
	 * @deprecated
	 */
	public ParameterHandle getParameter(String name);
	/**
	 * get all the parameters & parameter groups defined in the 
	 * report design.
	 * @return SlotHandle get from MODEL.
	 * @deprecated
	 */
	public SlotHandle getParameters();
	/**
	 * get the default value of parameter.
	 * @param name parameter name
	 * @return the default value defined in the design. null if not defined.
	 * @deprecated
	 */
	public Object getDefaultParameterValue(String name);
	/**
	 * get all defined default parameter values.
	 * @return hash map contains the parameter names and value pair.
	 * @deprecated
	 */
	public HashMap getDefaultParameterValues();
	/**
	 * get the collections of a selection choices.
	 * if the name is dynamic selection choice, the engine query the data base, return the choice.
	 * @param name parameter name
	 * @return collection of IParameterSelectionChoice
	 * @deprecated
	 */
	public Collection getSelectionChoice(String name);
	public Collection getSelectionList(String name);
	
	/**
	 * The first step to work with the cascading parameters. 
	 * Create the query definition, prepare and execute the query.
	 * Cache the iterator of the result set and also cache the IBaseExpression used in the prepare.
	 * 
	 * @param parameterGroupName - the cascading parameter group name
	 */
	public void evaluateQuery( String parameterGroupName );
	
	/**
	 * The second step to work with the cascading parameters.
	 * Get the selection choices for a parameter in the cascading group.
	 * The parameter to work on is the parameter on the next level in the parameter cascading hierarchy.
	 * For the "parameter to work on", please see the following example. 
	 * Assume we have a cascading parameter group as Country - State - City.
	 * If user specified an empty array in groupKeyValues (meaning user doesn't have any parameter value), 
	 * the parameter to work on will be the first level which is Country in this case.
	 * If user specified groupKeyValues as Object[]{"USA"} (meaning user has set the value of the top level),
	 * the parameter to work on will be the second level which is State in "USA" in this case.
	 * If user specified groupKeyValues as Object[]{"USA", "CA"} (meaning user has set the values of the top and the second level),
	 * the parameter to work on will be the third level which is City in "USA, CA" in this case.
	 * 
	 * @param parameterGroupName - the cascading parameter group name
	 * @param groupKeyValues - the array of known parameter values (see the example above)  
	 * @return the selection list of the parameter to work on
	 * @deprecated 
	 */
	public Collection getSelectionChoicesForCascadingGroup( String parameterGroupName, Object[] groupKeyValues );
	public Collection getSelectionListForCascadingGroup( String parameterGroupName, Object[] groupKeyValues );
	
}
