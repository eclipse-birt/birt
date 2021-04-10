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

import org.eclipse.birt.report.model.elements.ScriptDataSource;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses the script data source element.
 * 
 */

public class ScriptDataSourceState extends DataSourceState {

	/**
	 * Constructs the state for parsing script data source.
	 * 
	 * @param handler the design file parser handler
	 */

	public ScriptDataSourceState(ModuleParserHandler handler, int slot) {
		super(handler, slot);
		element = new ScriptDataSource();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		initElement(attrs, true);
	}
}
