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
