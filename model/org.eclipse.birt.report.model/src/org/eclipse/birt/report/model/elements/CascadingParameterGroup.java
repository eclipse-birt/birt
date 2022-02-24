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

import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ICascadingParameterGroupModel;

/**
 * Used to group a list of cascading parameters. Each parameter in the group is
 * a scalar parameter, type is "dynamic".
 * 
 */

public class CascadingParameterGroup extends CascadingParameterGroupImpl implements ICascadingParameterGroupModel {

	/**
	 * Default constructor.
	 */

	public CascadingParameterGroup() {

	}

	/**
	 * Constructs the cascading parameter group with an optional name.
	 * 
	 * @param theName the optional name
	 */

	public CascadingParameterGroup(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
		visitor.visitCascadingParameterGroup(this);
	}

	/**
	 * Returns the handle to this element;
	 */

	public ParameterGroupHandle handle(Module module) {
		if (handle == null) {
			handle = new CascadingParameterGroupHandle(module, this);
		}
		return (CascadingParameterGroupHandle) handle;
	}

}
