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
 * Parses the Vertical Align choiceset tag for compatibility.
 * <p>
 * Provide back-compatibility for original "verticalAlign" choice set.Now only
 * support 'top' , 'bottom' , 'middle' .
 * <p>
 * The compatible version is less than 3.2.2
 *
 */

public class CompatibleVerticalAlignState extends CompatiblePropertyState {

	CompatibleVerticalAlignState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);

	}

	@Override
	public void end() throws SAXException {
		/**
		 * Now Engine just supports 'top', 'middle','buttom' three choice types. So if
		 * the old file has other choice types, should clean those choice types.
		 */

		String value = text.toString();
		if (DesignChoiceConstants.VERTICAL_ALIGN_TOP.equals(value)
				|| DesignChoiceConstants.VERTICAL_ALIGN_MIDDLE.equals(value)
				|| DesignChoiceConstants.VERTICAL_ALIGN_BOTTOM.equals(value)) {
			super.end();
		}

	}
}
