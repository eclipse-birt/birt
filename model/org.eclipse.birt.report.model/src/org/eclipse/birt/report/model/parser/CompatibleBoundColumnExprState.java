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

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.BoundDataColumnUtil;
import org.xml.sax.SAXException;

/**
 * Parses expression values in boundDataColumns from BIRT 2.1 to BIRT 2.1.1.
 * This is for rows[] expressions. Includes these steps:
 * <ul>
 * <li>creates the bound data columns on the outer data container.
 * <li>convert rows[] to row._outer.
 * </ul>
 * <p>
 * This is a part of backward compatibility work from BIRT 2.1 to BIRT 2.1.1.
 */

public class CompatibleBoundColumnExprState extends CompatibleMiscExpressionState {

	/**
	 * Constructs a compatible state.
	 * 
	 * @param theHandler the handler to parse the design file.
	 * @param element    the data item
	 * @param propDefn
	 * @param struct
	 */

	CompatibleBoundColumnExprState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
			IStructure struct) {
		super(theHandler, element, propDefn, struct);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		String value = text.toString();

		if (value == null)
			return;

		DesignElement target = BoundDataColumnUtil.findTargetOfBoundColumns(element, handler.module);

		// not to create bound data columns locally.

		if (target != null)
			setupBoundDataColumns(target, value, false);

		// keep the expression as same.

		doEnd(value);
	}
}
