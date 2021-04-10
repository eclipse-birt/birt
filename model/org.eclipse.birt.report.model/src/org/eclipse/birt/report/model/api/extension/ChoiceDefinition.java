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

package org.eclipse.birt.report.model.api.extension;

/**
 * Adapter class for the IChoiceDefinition class.
 */

abstract public class ChoiceDefinition implements IChoiceDefinition {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.IChoiceDefinition#getDisplayNameID()
	 */

	abstract public String getDisplayNameID();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IChoiceDefinition#getName()
	 */

	abstract public String getName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IChoiceDefinition#getValue()
	 */

	public Object getValue() {
		return null;
	}

}
