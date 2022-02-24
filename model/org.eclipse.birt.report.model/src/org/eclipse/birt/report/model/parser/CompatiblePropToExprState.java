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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.xml.sax.SAXException;

/**
 *
 */

class CompatiblePropToExprState extends CompatiblePropertyState {

	private static final int DEFAULT_VALUE_PROP = ScalarParameter.DEFAULT_VALUE_PROP.toLowerCase().hashCode();

	/**
	 * Constructs a compatible state.
	 * 
	 * @param theHandler the handler to parse the design file.
	 * @param element    the data item
	 */

	CompatiblePropToExprState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.PropertyState#end()
	 */

	public void end() throws SAXException {
		handleDefaultValueList(handler.module, element, propDefn, nameValue, handler.versionNumber, text.toString());
	}

	/**
	 * Handles the compatibility case for specified properties.
	 * 
	 * @param value the value
	 * @return the value has been compromised
	 */

	private static String doCompatibility(int versionNumber, String value, DesignElement element, int propCode) {
		if (versionNumber < VersionUtil.VERSION_3_2_4 && (element instanceof ScalarParameter)
				&& DEFAULT_VALUE_PROP == propCode) {

			return StringUtil.trimQuotes(value);
		}

		return value;
	}

	public static void handleDefaultValueList(Module module, DesignElement element, PropertyDefn propDefn, int propCode,
			int versionNumber, String input) {
		String value = doCompatibility(versionNumber, input, element, propCode);

		int tmpType = propDefn.getTypeCode();

		Object newValue = null;
		if (tmpType == IPropertyType.LIST_TYPE) {
			List<Expression> newList = new ArrayList<Expression>();
			newList.add(new Expression(value, ExpressionType.CONSTANT));
			newValue = newList;
		} else {
			newValue = new Expression(value, ExpressionType.CONSTANT);
		}

		try {
			newValue = propDefn.getType().validateValue(module, element, propDefn, newValue);
		} catch (PropertyValueException e) {
			// ignore this exception. must be ROM error.
		}

		if (newValue == null)
			return;

		element.setProperty(propDefn, newValue);
	}
}
