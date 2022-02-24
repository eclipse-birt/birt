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

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.core.DesignElement;
import org.xml.sax.SAXException;

/**
 * Represents the property state which is used to do backward of
 * dateTimeLevelType property in Level element.
 * 
 */

public class CompatibleDateTimeLevelTypeState extends CompatiblePropertyState {

	/**
	 * Constructor
	 * 
	 * @param theHandler
	 * @param element
	 */

	public CompatibleDateTimeLevelTypeState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/**
	 * Handles the special case to read obsolete operators.
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		String value = text.toString();

		if ("week".equals(value)) //$NON-NLS-1$
		{
			value = DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR;
		} else if ("day".equals(value))//$NON-NLS-1$
		{
			value = DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR;
		}

		setProperty(name, value);
	}
}
