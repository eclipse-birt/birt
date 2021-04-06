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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;

/**
 * This state is for de-escaping characters in CDATA. The conversion is done
 * from the design version 3.2.16.
 */

class CompatibleCDATATextPropertyState extends TextPropertyState {

	/**
	 * Constructs a <code>CompatibleIgnorePropertyState</code> to parse an removed
	 * property.
	 * 
	 * @param theHandler the parser handle
	 * @param element    the element that holds the obsolete property
	 * 
	 */

	public CompatibleCDATATextPropertyState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#doSetProperty
	 * (org.eclipse.birt.report.model.metadata.PropertyDefn, java.lang.Object)
	 */

	protected void doSetProperty(PropertyDefn propDefn, Object valueToSet) {
		if (valueToSet == null)
			return;

		assert valueToSet instanceof String;

		// Validate the value.

		String newValue = (String) valueToSet;
		newValue = deEscape(newValue);

		super.doSetProperty(propDefn, newValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.PropertyState#generalJumpTo()
	 */

	protected AbstractParseState generalJumpTo() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.parser.ExpressionState#versionConditionalJumpTo
	 * ()
	 */

	protected AbstractParseState versionConditionalJumpTo() {
		return null;
	}

}
