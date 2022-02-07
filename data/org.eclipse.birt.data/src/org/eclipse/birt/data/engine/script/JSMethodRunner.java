/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.script;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.mozilla.javascript.Scriptable;

/**
 * Utility class to execute Javascript code as methods of a scope object. As per
 * ROM Scripting spec, data set/data source event handler scripts are executed
 * as if they are the bodies of methods defined on the data set /data source
 * objects. This means that (1) "return" keyword is used to return a value (if
 * the event handler has a return value. (2) "this" keyword in the script refers
 * to the data set/data source objects.
 * 
 * To achieve this, this class defines functions in the data set/ data source
 * scope with the script code, and then executes those functions.
 */
public class JSMethodRunner {
	private Scriptable scope;
	private ScriptContext cx;
	private static final String METHOD_NAME_PREFIX = "__bm_";

	/**
	 * Constructor
	 * 
	 * @param scope     Javascript scope within which to execute JS code. This
	 *                  should be the Javascript object implementing the data source
	 *                  or data set
	 * @param scopeName A descriptive name describing the scope. This name is used
	 *                  for error reporting purposes. Example,
	 *                  "DataSet(my_sql_dataset)".
	 */
	public JSMethodRunner(ScriptContext cx, Scriptable scope, String scopeName) {
		this.scope = scope;
		this.cx = cx;
	}

	/**
	 * Executes a method script. Each script should be identified with a unique name
	 * within the scope (such as "afterOpen", "onFetch" etc.). This class assumes
	 * that the content of a named method script is immutable, therefore it defines
	 * each named script only once.
	 * 
	 * @param methodName Identification of the script
	 * @param script     Script text
	 * @return Return value from the script
	 */
	public Object runScript(String methodName, String script) throws BirtException {
		// Add a prefix to the method name so it has less chance of conflict with
		// regular functions
		methodName = METHOD_NAME_PREFIX + methodName;

		try {
			// Check if method already defined in scope
			if (!scope.has(methodName, scope)) {

				// Define the method for the first time
				String scriptText = "function " + methodName + "() {\n" + script + "\n} ";
				ScriptEvalUtil.evaluateJSAsExpr(cx, scope, scriptText, ScriptExpression.defaultID, 1);

			}

			// Call pre-defined method
			String callScriptText = methodName + "()";
			Object result = ScriptEvalUtil.evaluateJSAsExpr(cx, scope, callScriptText, ScriptExpression.defaultID, 1);
			return result;
		} catch (DataException e) {
			throw new DataException(ResourceConstants.SCIRPT_FUNCTION_EXECUTION_FAIL, e,
					new Object[] { methodName, script });
		}
	}

	/**
	 * Executes a method script. Each script should be identified with a unique name
	 * within the scope (such as "afterOpen", "onFetch" etc.). This class assumes
	 * that the content of a named method script is immutable, therefore it defines
	 * each named script only once.
	 * 
	 * @param methodName Identification of the script
	 * @param script     Script text
	 * @param id         script id using in debug mode
	 * @return Return value from the script
	 */
	public Object runScript(String methodName, String script, String id) throws BirtException {
		// Add a prefix to the method name so it has less chance of conflict with
		// regular functions
		methodName = METHOD_NAME_PREFIX + methodName;

		try {
			// Check if method already defined in scope
			if (!scope.has(methodName, scope)) {

				// Define the method for the first time
				String scriptText = "function " + methodName + "() {\n" + script + "\n} ";
				ScriptEvalUtil.evaluateJSAsExpr(cx, scope, scriptText, id, 1);

			}

			// Call pre-defined method
			String callScriptText = methodName + "()";
			Object result = ScriptEvalUtil.evaluateJSAsExpr(cx, scope, callScriptText, id, 1);
			return result;
		} catch (DataException e) {
			throw new DataException(ResourceConstants.SCIRPT_FUNCTION_EXECUTION_FAIL, e,
					new Object[] { methodName, script });
		}
	}

}
