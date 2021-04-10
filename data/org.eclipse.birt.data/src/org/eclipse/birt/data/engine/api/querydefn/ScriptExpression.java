/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
 * Default implementation of the
 * {@link org.eclipse.birt.data.engine.api.IScriptExpression} interface
 */
public class ScriptExpression extends BaseExpression implements IScriptExpression {
	protected String exprText;

	/**
	 * Constructs a script expression, it could be a Constant expression or
	 * javascript expression with tag of BaseExpression.javaScriptId or
	 * BaseExpression.constantId.
	 * 
	 * @param text expression text
	 */
	public ScriptExpression(String text) {
		this.exprText = text;
	}

	/**
	 * Constructs a script expression, it could be a Constant expression or
	 * javascript expression with tag of BaseExpression.javaScriptId or
	 * BaseExpression.constantId.
	 * 
	 * @param text     expression text
	 * @param dataType Return data type of the expression
	 */
	public ScriptExpression(String text, int dataType) {
		super(dataType);
		this.exprText = text;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IScriptExpression#getText()
	 */
	public String getText() {
		return exprText;
	}

	/**
	 * Sets the expression text
	 */
	public void setText(String text) {
		exprText = text;
	}
}
