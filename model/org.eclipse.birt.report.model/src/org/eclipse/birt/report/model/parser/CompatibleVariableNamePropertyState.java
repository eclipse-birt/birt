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

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.xml.sax.SAXException;

/**
 * The compatible parser for parsing variableName property value of the
 * <VariableElement> and converts it to the name value.
 * 
 * 
 */
public class CompatibleVariableNamePropertyState extends CompatiblePropertyState {

	/**
	 * The constructor.
	 * 
	 * @param theHandler the parser handler.
	 * @param element    the design element.
	 */
	public CompatibleVariableNamePropertyState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.PropertyState#end()
	 */
	public void end() throws SAXException {
		String value = text.toString();
		if (!StringUtil.isBlank(value)) {
			element.setName(value);
		}
	}
}
