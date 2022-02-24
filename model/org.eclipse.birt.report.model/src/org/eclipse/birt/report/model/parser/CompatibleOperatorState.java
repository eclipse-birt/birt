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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.StyleRule;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.xml.sax.SAXException;

/**
 * Represents the list property state which is used to reading structure list of
 * filters in the listing elements or datasets.
 * <p>
 * The compatible version is 1. This state translates "null" to "is-null",
 * "not-null" to "is-not-null", "true" to "is-ture" and "false" to "is-false".
 * 
 */

class CompatibleOperatorState extends CompatiblePropertyState {

	private static final String NULL_VALUE = "null"; //$NON-NLS-1$
	private static final String NOT_NULL_VALUE = "not-null"; //$NON-NLS-1$
	private static final String TRUE_VALUE = "true"; //$NON-NLS-1$
	private static final String FALSE_VALUE = "false";//$NON-NLS-1$

	/**
	 * Constructs a <code>CompatibleOperatorState</code>.
	 * 
	 * @param theHandler the design parser handle
	 * @param element    the element
	 * @param propDefn   the property definition
	 * @param struct     the structure
	 */

	public CompatibleOperatorState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
			IStructure struct) {
		super(theHandler, element, propDefn, struct);
	}

	/**
	 * Handles the special case to read obsolete operators.
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		String value = text.toString();

		assert struct != null;

		if (NULL_VALUE.equalsIgnoreCase(value)) {
			if (FilterCondition.OPERATOR_MEMBER.equalsIgnoreCase(propDefn.getName()))
				value = DesignChoiceConstants.FILTER_OPERATOR_NULL;
			else if (StyleRule.OPERATOR_MEMBER.equalsIgnoreCase(propDefn.getName()))
				value = DesignChoiceConstants.MAP_OPERATOR_NULL;
		}

		if (NOT_NULL_VALUE.equalsIgnoreCase(value)) {
			if (FilterCondition.OPERATOR_MEMBER.equalsIgnoreCase(propDefn.getName()))
				value = DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL;
			else if (StyleRule.OPERATOR_MEMBER.equalsIgnoreCase(propDefn.getName()))
				value = DesignChoiceConstants.MAP_OPERATOR_NOT_NULL;
		}

		if (TRUE_VALUE.equalsIgnoreCase(value)) {
			if (FilterCondition.OPERATOR_MEMBER.equalsIgnoreCase(propDefn.getName()))
				value = DesignChoiceConstants.FILTER_OPERATOR_TRUE;
			else if (StyleRule.OPERATOR_MEMBER.equalsIgnoreCase(propDefn.getName()))
				value = DesignChoiceConstants.MAP_OPERATOR_TRUE;
		}

		if (FALSE_VALUE.equalsIgnoreCase(value)) {
			if (FilterCondition.OPERATOR_MEMBER.equalsIgnoreCase(propDefn.getName()))
				value = DesignChoiceConstants.FILTER_OPERATOR_FALSE;
			else if (StyleRule.OPERATOR_MEMBER.equalsIgnoreCase(propDefn.getName()))
				value = DesignChoiceConstants.MAP_OPERATOR_FALSE;
		}

		setMember(struct, propDefn.getName(), name, value);
	}
}
