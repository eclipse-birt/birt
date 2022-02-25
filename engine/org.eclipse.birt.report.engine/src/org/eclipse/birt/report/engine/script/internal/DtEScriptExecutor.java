/*******************************************************************************
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
 *******************************************************************************/
package org.eclipse.birt.report.engine.script.internal;

import java.util.HashMap;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataScriptEngine;
import org.eclipse.birt.data.engine.script.JSMethodRunner;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.mozilla.javascript.Scriptable;

public abstract class DtEScriptExecutor extends ScriptExecutor {

	protected static final String DATA_SET = "DataSet";

	protected static final String DATA_SOURCE = "DataSource";

	protected static final String BEFORE_OPEN = "beforeOpen";

	protected static final String BEFORE_CLOSE = "beforeClose";

	protected static final String AFTER_OPEN = "afterOpen";

	protected static final String AFTER_CLOSE = "afterClose";

	protected ExecutionContext context;

	protected Scriptable scope;

	protected IReportContext reportContext;

	private HashMap<Scriptable, JSMethodRunner> runner = new HashMap<>();

	public DtEScriptExecutor(ExecutionContext context) throws BirtException {
		this.context = context;
		if (context != null) {
			this.reportContext = context.getReportContext();
			this.scope = ((IDataScriptEngine) context.getScriptContext().getScriptEngine(IDataScriptEngine.ENGINE_NAME))
					.getJSScope(context.getScriptContext());
		} else {
			this.reportContext = null;
		}
	}

	protected JSMethodRunner getRunner(Scriptable scope, String type, String name) {
		if (scope == null) {
			return new JSMethodRunner(context.getScriptContext(), scope, name);
		}
		JSMethodRunner result = runner.get(scope);
		if (result != null) {
			return result;
		}

		String scopeName = type + "[" + name + "]";

		result = new JSMethodRunner(context.getScriptContext(), scope, scopeName);
		runner.put(scope, result);
		return result;
	}

	protected ScriptStatus handleJS(Scriptable scope, String type, String name, String method, String script,
			String id) {
		if (script == null || type == null || name == null || method == null) {
			return ScriptStatus.NO_RUN;
		}
		if (!(DATA_SET.equals(type) || DATA_SOURCE.equals(type))) {
			return ScriptStatus.NO_RUN;
		}
		Object result = null;
		try {
			JSMethodRunner jsr = getRunner(scope, type, name);
			result = jsr.runScript(method, script, id);
		} catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
			if (context != null) {
				context.addException(new EngineException(MessageConstants.SCRIPT_EVALUATION_ERROR,
						new Object[] { script, e.getMessage() }, e));
			}
		}
		return new ScriptStatus(true, result);
	}

	protected abstract ScriptStatus handleJS(Scriptable scope, String name, String method, String script, String id);

}
