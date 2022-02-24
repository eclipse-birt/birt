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
import org.eclipse.birt.report.model.api.RuleHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.core.Structure;

/**
 * This class represents one rule. Each rule has the following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>rule expression </strong></dt>
 * <dd>The expression to define the static value.</dd>
 * 
 * <dt><strong>display expression </strong></dt>
 * <dd>The expression to give the display value for this static value</dd>
 * </dl>
 * 
 */

public class Rule extends Structure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String RULE_STRUCTURE = "Rule"; //$NON-NLS-1$

	/**
	 * Name of the member which defines the static value.
	 */
	public static final String RULE_EXPRE_MEMBER = "ruleExpre"; //$NON-NLS-1$

	/**
	 * Name of the member which gives the display value for the static value.
	 */
	public static final String DISPLAY_EXPRE_MEMBER = "displayExpre"; //$NON-NLS-1$

	/**
	 * Static value.
	 */
	protected Expression ruleExpre = null;

	/**
	 * Display value.
	 */
	protected Expression displayExpre = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return RULE_STRUCTURE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new RuleHandle(valueHandle, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */
	protected Object getIntrinsicProperty(String propName) {
		if (RULE_EXPRE_MEMBER.equals(propName))
			return this.ruleExpre;
		if (DISPLAY_EXPRE_MEMBER.equals(propName))
			return this.displayExpre;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java
	 * .lang.String, java.lang.Object)
	 */
	protected void setIntrinsicProperty(String propName, Object value) {
		if (RULE_EXPRE_MEMBER.equals(propName)) {
			ruleExpre = (Expression) value;
		} else if (DISPLAY_EXPRE_MEMBER.equals(propName)) {
			displayExpre = (Expression) value;
		}
	}
}
