
/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

/**
 * This class is used to wrap value and type from an script expression in one object
 */

package org.eclipse.birt.chart.model.data;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;

/*
 * This type represents expression
 */
public class ScriptExpression {

	private String type;
	private String value;

	public ScriptExpression() {
		this(ExpressionCodec.JAVASCRIPT);
	}

	public ScriptExpression(String type) {
		this(type, IConstants.EMPTY_STRING);
	}

	public ScriptExpression(String type, String value) {
		this.type = type;
		this.value = value;
	}

	/*
	 * Get expression type
	 *
	 * @return type the type of the expression.
	 */
	public String getType() {
		return type;
	}

	/*
	 * Set expression type
	 *
	 * @param type the type of the expression.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/*
	 * Get expression value
	 *
	 * @return value value of the expression.
	 */
	public String getValue() {
		return value;
	}

	/*
	 * Set expression value
	 *
	 * @param value value of the expression.
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
