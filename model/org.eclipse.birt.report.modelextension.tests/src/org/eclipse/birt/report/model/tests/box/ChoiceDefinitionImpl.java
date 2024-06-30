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

package org.eclipse.birt.report.model.tests.box;

import org.eclipse.birt.report.model.api.extension.IChoiceDefinition;

/**
 * Implements <code>IChoiceDefinition</code> for testing
 */

public class ChoiceDefinitionImpl implements IChoiceDefinition {

	String displayNameID = null;
	Object value = null;
	String name = null;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.model.extension.IChoiceDefn#getDisplayName()
	 */
	@Override
	public String getDisplayNameID() {
		return displayNameID;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.model.extension.IChoiceDefn#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.model.extension.IChoiceDefn#getValue()
	 */
	@Override
	public Object getValue() {
		return value;
	}

}
