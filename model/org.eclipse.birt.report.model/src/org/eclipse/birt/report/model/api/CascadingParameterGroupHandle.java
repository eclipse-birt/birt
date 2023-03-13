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
