/*
 *************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.data.engine.api.IScriptExpression;

/**
 * Utility class to create the constant expression and java script expression.
 * 
 */
public class ScriptExpressionUtil {

	private ScriptExpressionUtil() {
	}

	/**
	 * Utility method to create constant expression
	 * 
	 * @param expr
	 * @return
	 */
	public static IScriptExpression createConstantExpression(String constantExpr) {
		ScriptExpression scriptExpression = new ScriptExpression(constantExpr);
		scriptExpression.setScriptId(BaseExpression.constantId);
		scriptExpression.setHandle(constantExpr);
		return scriptExpression;
	}

	/**
	 * Utility method to create java script expression
	 * 
	 * @param expr
	 * @return
	 */
	public static IScriptExpression createJavaScriptExpression(String jsExppression) {
		ScriptExpression scriptExpression = new ScriptExpression(jsExppression);
		scriptExpression.setScriptId(BaseExpression.javaScriptId);
		return scriptExpression;
	}
}
