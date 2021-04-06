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

public class CompatibleRenameListPropertyState extends CompatibleListPropertyState {

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

	public CompatibleRenameListPropertyState(ModuleParserHandler theHandler, DesignElement element,
			String obsoletePropName) {
		super(theHandler, element);
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
