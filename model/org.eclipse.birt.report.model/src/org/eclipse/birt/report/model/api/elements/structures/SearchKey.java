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

package org.eclipse.birt.report.model.api.elements.structures;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.SearchKeyHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.core.Structure;

/**
 * Structure for optional list of search criteria. It is used when we defined
 * action in some report item, such as label item, image item and so on, and the
 * action type is drillthrough type. It's provided for drillthrough contents. If
 * the document supports searching, the link can include search criteria. Each
 * search key has the following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>Expression </strong></dt>
 * <dd>an expression of the search key for the drillthrough.</dd>
 * </dl>
 * <p>
 * 
 * @see Action
 */

public class SearchKey extends Structure {

	/**
	 * Name of the structure.
	 */

	public static final String SEARCHKEY_STRUCT = "SearchKey"; //$NON-NLS-1$

	/**
	 * Name of the expression property.
	 */

	public static final String EXPRESSION_MEMBER = "expression"; //$NON-NLS-1$

	/**
	 * Value of the expression property.
	 */

	protected Expression expression = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return SEARCHKEY_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (EXPRESSION_MEMBER.equals(propName))
			return expression;

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java
	 * .lang.String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {
		if (EXPRESSION_MEMBER.equals(propName))
			expression = (Expression) value;
		else
			assert false;
	}

	/**
	 * Sets the expression value of this key.
	 * 
	 * @param expression the expression to set
	 */

	public void setExpression(String expression) {
		setProperty(EXPRESSION_MEMBER, expression);
	}

	/**
	 * Returns expression value this key.
	 * 
	 * @return the expression value
	 */

	public String getExpression() {
		return getStringProperty(null, EXPRESSION_MEMBER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new SearchKeyHandle(valueHandle, index);
	}
}
