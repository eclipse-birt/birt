/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc;

/**
 * Parameter defination: parameter paraName, parameter type, parameter datatype,
 * parameter inOutType, parameter precision parameter scale parameter nullable
 */

public class ParameterDefn {
	/**
	 * parameter name
	 */
	private String paramName;
	/**
	 * parameter data type
	 */
	private int type;
	/**
	 * parameter in/out type
	 */
	private int inOutType;
	/**
	 * @param paramTypeName
	 */
	private String paramTypeName;
	/**
	 * @param precesion
	 */
	private int precision;
	/**
	 * @param scale
	 */
	private int scale;
	/**
	 * 
	 * @param nullable
	 */
	private int nullable;

	/**
	 * set parameter name
	 * 
	 * @param paramName
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	/**
	 * get parameter name
	 * 
	 * @return
	 */
	public String getParamName() {
		return this.paramName;
	}

	/**
	 * set parameter type name
	 * 
	 * @return
	 */
	public void setParamTypeName(String typeName) {
		this.paramTypeName = typeName;
	}

	/**
	 * get parameter type name
	 * 
	 * @return
	 */
	public String getParamTypeName() {
		return this.paramTypeName;
	}

	/**
	 * @param precision
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}

	/**
	 * get parameter precision
	 * 
	 */
	public int getPrecision() {
		return this.precision;
	}

	/**
	 * @param scale
	 */
	public void setScale(int scale) {
		this.scale = scale;
	}

	/**
	 * get parameter scale
	 */
	public int getScale() {
		return this.scale;
	}

	/**
	 * 
	 * @param nullable
	 */
	public void setIsNullable(int nullable) {
		this.nullable = nullable;
	}

	/**
	 * 
	 * @return nullable
	 */
	public int getIsNullable() {
		return this.nullable;
	}

	/**
	 * set parameter data type
	 * 
	 * @param type
	 */
	public void setParamType(int type) {
		this.type = type;
	}

	/**
	 * get parameter data type
	 * 
	 * @return
	 */
	public int getParamType() {
		return type;
	}

	/**
	 * set parameter type
	 * 
	 * @param inoutType
	 */
	public void setParamInOutType(int inoutType) {
		this.inOutType = inoutType;
	}

	/**
	 * get parameter type
	 * 
	 * @return
	 */
	public int getParamInOutType() {
		return inOutType;
	}
}