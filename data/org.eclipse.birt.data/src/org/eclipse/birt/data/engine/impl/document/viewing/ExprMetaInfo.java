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
package org.eclipse.birt.data.engine.impl.document.viewing;

/**
 * 
 */
public class ExprMetaInfo {
	private String name;
	private int groupLevel;

	private int exprType;
	private int dataType;

	private String jsText;

	public final static int SCRIPT_EXPRESSION = 0;
	public final static int CONDITIONAL_EXPRESSION = 1;
	public final static int COMBINED_EXPRESSION = 2;

	/**
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param id
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public int getGroupLevel() {
		return groupLevel;
	}

	/**
	 * @param groupLevel
	 */
	public void setGroupLevel(int groupLevel) {
		this.groupLevel = groupLevel;
	}

	/**
	 * Two types, IScriptExpression and IConditionalExpression
	 * 
	 * @return
	 */
	public int getType() {
		return this.exprType;
	}

	/**
	 * @param exprType
	 */
	public void setType(int exprType) {
		this.exprType = exprType;
	}

	/**
	 * @return
	 */
	public String getJSText() {
		return jsText;
	}

	/**
	 * @param exprText
	 */
	public void setJSText(String jsText) {
		this.jsText = jsText;
	}

	/**
	 * @return
	 */
	public int getDataType() {
		return this.dataType;
	}

	/**
	 * @param dataType
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

}
