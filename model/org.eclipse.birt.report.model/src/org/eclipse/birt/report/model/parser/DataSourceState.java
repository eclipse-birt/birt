/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.DataSource;

/**
 * This class parses the data source element.
 * 
 */

public abstract class DataSourceState extends ReportElementState {

	protected DataSource element;

	/**
	 * Constructs the data source state with the design parser handler, the
	 * container element and the container slot of the data source.
	 * 
	 * @param handler the design file parser handler
	 */

	public DataSourceState(ModuleParserHandler handler, int slot) {
		super(handler, handler.getModule(), slot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement() {
		return element;
	}
}
