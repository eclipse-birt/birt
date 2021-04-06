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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.xml.sax.SAXException;

/**
 * Parsed the single included Resource file to the list of included Resources.
 * <p>
 * The conversion should be done to the file that was created before 3.2.15
 * (included .15).
 */

class CompatibleIncludeResourceState extends CompatiblePropertyState {

	/**
	 * Default constructor.
	 * 
	 * @param theHandler the parser handler
	 * @param element    the element to be parsed
	 */

	public CompatibleIncludeResourceState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.PropertyState#end()
	 */

	public void end() throws SAXException {
		String value = text.toString();

		if (StringUtil.isEmpty(value))
			return;

		List<Object> resoureLit = new ArrayList<Object>();
		resoureLit.add(value);
		element.setProperty(IModuleModel.INCLUDE_RESOURCE_PROP, resoureLit);
	}

}
