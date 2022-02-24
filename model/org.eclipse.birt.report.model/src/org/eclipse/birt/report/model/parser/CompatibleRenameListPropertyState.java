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
