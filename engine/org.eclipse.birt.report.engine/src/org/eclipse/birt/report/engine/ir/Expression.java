/*******************************************************************************
 * Copyright (c) 2008,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.ir;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ICompiledScript;
import org.eclipse.birt.data.engine.api.IConditionalExpression;

public abstract class Expression {

	public static final int CONSTANT = 0;
	public static final int SCRIPT = 1;
	public static final int CONDITIONAL = 2;

	public static final String SCRIPT_JAVASCRIPT = "javascript";

	protected String scriptText;

	public abstract int getType();

	public String getScriptText() {
		return this.scriptText;
	}

	public String toString() {
		return scriptText;
	}

	public void setScriptText(String scriptText) {
		this.scriptText = scriptText;
	}

	static public Expression newConstant(int valueType, String expression) {
		return new Constant(valueType, expression);
	}

	static public Expression.Constant newConstant(String expression) {
		return new Constant(expression);
	}

	static public Expression.Script newScript(String expression) {
		return new Script(SCRIPT_JAVASCRIPT, "<inline>", 1, expression);
	}

	static public Expression.Script newScript(String language, String expression) {
		return new Script(language, "<inline>", 1, expression);
	}

	static public Expression.Conditional newConditional(IConditionalExpression condExpr) {
		return new Conditional(condExpr);
	}

	static public Expression.Script newScript(String language, String fileName, int lineNumber, String expression) {
		return new Script(language, fileName, lineNumber, expression);
	}

	static public class Conditional extends Expression {

		IConditionalExpression expr;

		public Conditional(IConditionalExpression expr) {
			this.expr = expr;
		}

		public int getType() {
			return CONDITIONAL;
		}

		public IConditionalExpression getConditionalExpression() {
			return expr;
		}

		public String toString() {
			if (expr != null) {
				return expr.toString();
			}
			return "";
		}
	}

	static public class Constant extends Expression {

		static final Object NOT_EVALUATED = "not evaluated";
		Object value;
		int valueType;

		public Constant(int valueType, String expression) {
			this.valueType = valueType;
			this.scriptText = expression;
			this.value = NOT_EVALUATED;
		}

		public Constant(String expression) {
			this(DataType.UNKNOWN_TYPE, expression);
		}

		public int getType() {
			return CONSTANT;
		}

		public int getValueType() {
			return valueType;
		}

		public Object getValue() {
			if (value == NOT_EVALUATED) {
				try {
					value = DataTypeUtil.convert(scriptText, valueType);
				} catch (BirtException ex) {
					value = null;
				}
			}
			return value;
		}
	}

	static public class Script extends Expression {

		transient ICompiledScript compiledScript;

		String language;
		String fileName;
		int lineNumber;

		public Script(String language, String fileName, int lineNumber, String scriptText) {
			this.language = language;
			this.fileName = fileName;
			this.lineNumber = lineNumber;
			this.scriptText = scriptText;
		}

		public int getType() {
			return SCRIPT;
		}

		public ICompiledScript getScriptExpression() {
			return compiledScript;
		}

		public void setCompiledScript(ICompiledScript compiledScript) {
			this.compiledScript = compiledScript;
		}

		public String getLanguage() {
			return language;
		}

		public String getFileName() {
			return fileName;
		}

		public int getLineNumber() {
			return lineNumber;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
	}
}
