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

	@Override
	protected void doSetProperty(PropertyDefn propDefn, Object valueToSet) {
		if (valueToSet == null) {
			return;
		}

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

	@Override
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

	@Override
	protected AbstractParseState versionConditionalJumpTo() {
		return null;
	}

}
