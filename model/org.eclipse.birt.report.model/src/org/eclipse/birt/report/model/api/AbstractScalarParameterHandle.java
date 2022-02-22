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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractScalarParameterModel;

/**
 *
 * Represents the abstract scalar parameter types.
 *
 * @see org.eclipse.birt.report.model.elements.AbstractScalarParameter
 */

public abstract class AbstractScalarParameterHandle extends AbstractScalarParameterHandleImpl
		implements IAbstractScalarParameterModel {

	/**
	 * Constructor.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public AbstractScalarParameterHandle(Module module, DesignElement element) {
		super(module, element);
	}

}
