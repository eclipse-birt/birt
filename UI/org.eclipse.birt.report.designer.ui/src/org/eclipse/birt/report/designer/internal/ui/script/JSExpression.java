/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.script;

import java.util.Stack;

import org.eclipse.birt.report.designer.internal.ui.script.JSObjectMetaData.JSField;
import org.eclipse.birt.report.designer.internal.ui.script.JSObjectMetaData.JSMethod;

/**
 * JSExpression use to parse a js expression fragment and determine what type
 * will return for this code fragment.
 */

public class JSExpression {

	private String expr;
	private int isParsed = -1;

	protected Stack codeStack = new Stack();

	private JSSyntaxContext context;

	public JSExpression(JSSyntaxContext context, String expr) {
		this.context = context;
		this.expr = expr;
	}

	public Object getReturnType() {
		return parse();
	}

	/**
	 *
	 */
	private Object parse() {
		if (expr == null) {
			return null;
		}

		if (isParsed < 0) {
			parse(expr);
		}

		if (isParsed == 0) {
			return null;
		}

		JSObjectMetaData objectMeta = null;
		Object fragment;
		String currentCode = null;
		String preCode = ""; //$NON-NLS-1$

		parseLoop: while (!codeStack.isEmpty() && (fragment = codeStack.pop()) != null) {
			currentCode = (String) fragment;
			// is a method.
			if (isMethod(currentCode) && objectMeta != null) {
				String methodName = getMethodName(currentCode);
				JSMethod[] methods = objectMeta.getMethods();
				for (int i = 0; i < methods.length; i++) {
					if (methods[i].getName().equals(methodName)) {
						objectMeta = methods[i].getReturn();
						continue parseLoop;
					}
				}
				objectMeta = null;
			} else if (isField(currentCode))// is a field or package declare
			{
				if (preCode != null && objectMeta == null) {
					try {
						objectMeta = getFieldObjectMeta(currentCode, preCode);
					} catch (ClassNotFoundException e) {
						objectMeta = null;
					}
				} else {
					String fieldName = getFieldName(currentCode);
					JSField[] fields = objectMeta.getFields();
					for (int i = 0; i < fields.length; i++) {
						if (fields[i].getName().equals(fieldName)) {
							objectMeta = fields[i].getType();
							continue parseLoop;
						}
					}
					if (!".".equals(currentCode)) { //$NON-NLS-1$
						objectMeta = null;
					}
				}
			} else if (isArray(currentCode) && objectMeta != null) {
				objectMeta = objectMeta.getComponentType();
			} else if (objectMeta == null && !".".equals(currentCode)) //$NON-NLS-1$
			// root fragment
			{
				objectMeta = context.getVariableMeta(currentCode);
				if (objectMeta == null) {
					objectMeta = JSSyntaxContext.getEnginJSObject(currentCode);
				}
				if (objectMeta == null && codeStack.isEmpty()) {
					return JSSyntaxContext.getAllEnginJSObjects();
				}
			}
			preCode += currentCode;
		}
		return objectMeta;
	}

	protected JSObjectMetaData getFieldObjectMeta(String currentCode, String preCode) throws ClassNotFoundException {
		return JSSyntaxContext.getJavaClassMeta(preCode + currentCode);
	}

	private boolean isArray(String currentCode) {
		return currentCode.startsWith("[") //$NON-NLS-1$
				&& currentCode.endsWith("]"); //$NON-NLS-1$
	}

	private String getFieldName(String fieldCode) {
		return fieldCode.substring(1);
	}

	private boolean isField(String currentCode) {
		return currentCode.indexOf(".") == 0 //$NON-NLS-1$
				&& currentCode.lastIndexOf(")") == -1; //$NON-NLS-1$
	}

	private boolean isMethod(String currentCode) {
		return currentCode.indexOf(".") == 0 //$NON-NLS-1$
				&& currentCode.lastIndexOf(")") == currentCode.length() - 1; //$NON-NLS-1$
	}

	private String getMethodName(String methodCode) {
		return methodCode.substring(1, methodCode.indexOf("(")); //$NON-NLS-1$
	}

	/**
	 * parse the expression, and put code fragment into stack. ignore code in a pair
	 * of bracket.
	 *
	 * @param expression
	 * @return
	 */
	protected void parse(String expression) {
		Stack bracketStack = new Stack();

		int endOffset = expression.length();
		int startOffset = endOffset;
		char currentChar;
		while (startOffset > 0 && isParsed < 1) {
			startOffset--;
			currentChar = expr.charAt(startOffset);
			if (currentChar == ')') {
				bracketStack.push(")"); //$NON-NLS-1$
			} else if (currentChar == ']') {
				bracketStack.push("]"); //$NON-NLS-1$
			} else if (currentChar == '(') {
				if (bracketStack.isEmpty()) {
					codeStack.push(expression.substring(++startOffset, endOffset).trim());
					isParsed = 1;
					return;
				} else if (!bracketStack.pop().equals(")")) //$NON-NLS-1$
				{
					isParsed = 0;
					return;
				}
			} else if (currentChar == '[') {
				// if ( bracketStack.isEmpty( ) )
				// {
				// codeStack.push( expression.substring( ++startOffset,
				// endOffset ) );
				// isParsed = 1;
				// return;
				// }
				// else if ( !bracketStack.pop( ).equals( "]" ) ) //$NON-NLS-1$
				// {
				// isParsed = 0;
				// return;
				// }
				if (bracketStack.size() > 0 && bracketStack.pop().equals("]")) //$NON-NLS-1$
				{
					codeStack.push(expression.substring(startOffset, endOffset).trim());
					endOffset = startOffset;
				} else {
					isParsed = 0;
					return;
				}
			} else if (currentChar == '.' && bracketStack.isEmpty()) {
				codeStack.push(expression.substring(startOffset, endOffset).trim());
				endOffset = startOffset;
			}
		}
		codeStack.push(expression.substring(startOffset, endOffset).trim());
		isParsed = 1;
	}

}
