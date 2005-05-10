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
	 * get parameter definition by name
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
	 * get default value by name
	 * @param param
	 * @return
	 */
	public Object getDefaultValue(IParameterDefnBase param);
}
