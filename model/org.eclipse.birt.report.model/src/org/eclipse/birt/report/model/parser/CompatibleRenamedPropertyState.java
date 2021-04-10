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

/**
 * Represents the state to rename the design file. In version 1, it renames two
 * properties:
 * 
 * <ul>
 * <li>TextDataItem: contentTypeExpr to contentType</li>
 * <li>ListGroup: groupStart to intervalBase</li>
 * </ul>
 */

class CompatibleRenamedPropertyState extends CompatiblePropertyState {

	/**
	 * The obsolete property name.
	 */

	private String obsoletePropName;

	/**
	 * Constructs a <code>CompatibleRenamedPropertyState</code> to parse an obsolete
	 * property.
	 * 
	 * @param theHandler       the parser handle
	 * @param element          the element that holds the obsolete property
	 * @param obsoletePropName the name of the obsolete property.
	 */

	public CompatibleRenamedPropertyState(ModuleParserHandler theHandler, DesignElement element,
			String obsoletePropName) {
		super(theHandler, element);
		this.obsoletePropName = obsoletePropName;
	}

	/**
	 * Constructs a <code>CompatibleRenamedPropertyState</code> to parse an obsolete
	 * property.
	 * 
	 * @param theHandler       the parser handle
	 * @param element          the element that holds the obsolete property
	 * @param propDefn         the property defnition
	 * @param struct           the strucutre
	 * @param obsoletePropName the name of the obsolete property.
	 */

	CompatibleRenamedPropertyState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
			IStructure struct, String obsoletePropName) {
		super(theHandler, element);

		this.propDefn = propDefn;
		this.struct = struct;
		this.obsoletePropName = obsoletePropName;
	}

	/**
	 * Returns the name of the obsolete property.
	 * 
	 * @return the name of the obsolete property
	 */

	protected String getObsoletePropName() {
		return obsoletePropName;
	}

}
