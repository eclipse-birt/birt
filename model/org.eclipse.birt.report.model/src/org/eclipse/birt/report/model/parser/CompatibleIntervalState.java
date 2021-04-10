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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.xml.sax.SAXException;

/**
 * Represents the property state which is used to do backward of interval
 * property in Level element.
 * <p>
 * 
 */

public class CompatibleIntervalState extends CompatiblePropertyState {

	/**
	 * Constructor
	 * 
	 * @param theHandler
	 * @param element
	 */

	public CompatibleIntervalState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/**
	 * Handles the special case to read obsolete operators.
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		String value = text.toString();

		boolean isAllowed = false;
		IChoiceSet intervalChoice = MetaDataDictionary.getInstance()
				.getChoiceSet(DesignChoiceConstants.CHOICE_INTERVAL_TYPE);
		IChoice[] choices = intervalChoice.getChoices();
		for (int i = 0; i < choices.length; ++i) {
			IChoice choice = choices[i];
			String choiceName = choice.getName();
			if (choiceName.equalsIgnoreCase(value)) {
				isAllowed = true;
				break;
			}
		}
		if (!isAllowed) {
			value = DesignChoiceConstants.INTERVAL_TYPE_NONE;
		}
		setProperty(name, value);

	}

}
