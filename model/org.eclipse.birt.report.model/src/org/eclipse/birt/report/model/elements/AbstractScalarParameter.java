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
