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

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.DataTypeConversionUtil;
import org.xml.sax.SAXException;

/**
 * Parsed column data type choices to parameter type choices.
 * <p>
 * The conversion is done from the file version 3.2.5. It is a part of automatic
 * conversion for BIRT 2.1.1.
 */

public class CompatibleColumnDataTypeState extends CompatiblePropertyState {

	/**
	 * Default constructor.
	 *
	 * @param theHandler the parser handler
	 * @param element    the element to parse
	 * @param propDefn   the property definition
	 * @param struct     the structure of OdaDataSetParameter
	 */

	CompatibleColumnDataTypeState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
			IStructure struct) {
		super(theHandler, element, propDefn, struct);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.PropertyState#end()
	 */

	@Override
	public void end() throws SAXException {
		String value = text.toString();

		doEnd(DataTypeConversionUtil.converToParamType(value));
	}
}
