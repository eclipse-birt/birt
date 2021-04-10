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

import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.CascadingParameterGroup;
import org.eclipse.birt.report.model.elements.interfaces.ICascadingParameterGroupModel;

/**
 * Represents the group of cascading parameters. Cascading parameters are
 * created under the group.
 */

public class CascadingParameterGroupHandle extends CascadingParameterGroupHandleImpl
		implements ICascadingParameterGroupModel {
	/**
	 * Constructs the handle for a group of cascading parameters with the given
	 * design and element.
	 * 
	 * @param module  the module
	 * @param element the cascading parameter group element instance.
	 */
	public CascadingParameterGroupHandle(Module module, CascadingParameterGroup element) {
		super(module, element);
	}
}
