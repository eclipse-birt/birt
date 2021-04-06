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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.elements.interfaces.IAbstractScalarParameterModel;

/**
 * Abstract class for the various kinds of scalar parameters.
 * 
 */
public abstract class AbstractScalarParameter extends AbstractScalarParameterImpl
		implements IAbstractScalarParameterModel {

	/**
	 * Default constructor.
	 */

	protected AbstractScalarParameter() {
	}

	/**
	 * Constructs the abstract scalar parameter element with a required and unique
	 * name.
	 * 
	 * @param theName the required name
	 */

	protected AbstractScalarParameter(String theName) {
		super(theName);
	}
}
