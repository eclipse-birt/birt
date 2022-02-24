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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.BooleanPropertyType;
import org.xml.sax.SAXException;

/**
 * @author Administrator
 * 
 */

public class CompatibleMustMatchState extends CompatiblePropertyState {

	/**
	 * Constructs a <code>CompatibleMustMatchState</code> to parse an
	 * pagebreakinterval property.
	 * 
	 * @param theHandler the parser handle
	 * @param element    the element that holds the obsolete property
	 * 
	 */

	public CompatibleMustMatchState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		String value = text.toString();
		if (BooleanPropertyType.TRUE.equalsIgnoreCase(value.trim()))
			value = BooleanPropertyType.FALSE;
		else if (BooleanPropertyType.FALSE.equalsIgnoreCase(value.trim()))
			value = BooleanPropertyType.TRUE;

		doEnd(value);
	}
}
