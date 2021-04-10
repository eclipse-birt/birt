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

/**
 * This state is for handling the obsolete "onRow" methods on list/table. The
 * "onRow" property value of table/list should be set to rows in detail slot.
 */

public class CompatibleOnRowPropertyState extends CompatiblePropertyState {

	/**
	 * Constructs a <code>CompatibleIgnorePropertyState</code> to parse an removed
	 * property.
	 * 
	 * @param theHandler the parser handle
	 * @param element    the element that holds the obsolete property
	 * 
	 */

	public CompatibleOnRowPropertyState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() {
		String value = text.toString();
		handler.tempValue.put(element, value);
		return;
	}
}
