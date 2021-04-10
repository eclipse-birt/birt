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
import org.eclipse.birt.report.model.core.DesignElement;
import org.xml.sax.SAXException;

/**
 * This state is for handling the compatibility problem for pagebreaks used to
 * set on table row. Now if the container of table row is group, then set these
 * page breaks properties to group, otherwise, ignore these properties.
 */

public class CompatiblePageBreakPropState extends CompatiblePropertyState {

	/**
	 * Constructs a <code>CompatiblePageBreakPropState</code> to parse an pagebreak
	 * property.
	 * 
	 * @param theHandler the parser handle
	 * @param element    the element that holds the obsolete property
	 * 
	 */

	public CompatiblePageBreakPropState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		String value = text.toString();
		if (value.equalsIgnoreCase("left") //$NON-NLS-1$
				|| value.equalsIgnoreCase("right")) //$NON-NLS-1$
		{
			value = DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS;

			doEnd(value);
		} else {
			super.end();
		}
	}
}
