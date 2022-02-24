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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructRefValue;

/**
 * Records a change to the back reference of a structure.
 * 
 * @see org.eclipse.birt.report.model.core.ReferencableStructure
 */

public class StructBackRefRecord extends BackRefRecord {

	/**
	 * The structure is referred by <code>reference</code>.
	 */

	protected ReferencableStructure referred = null;

	/**
	 * Constructor.
	 * 
	 * @param module    the module
	 * @param referred  the structure to change.
	 * @param reference the element that refers to a structure.
	 * @param propName  the property name. The type of the property must be
	 *                  <code>STRUCT_REF_TYPE</code>.
	 */

	public StructBackRefRecord(Module module, ReferencableStructure referred, DesignElement reference,
			String propName) {
		super(module, reference, propName);
		this.referred = referred;

		assert referred != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.SimpleRecord#perform(boolean)
	 */

	protected void perform(boolean undo) {
		DesignElement tmpElement = (DesignElement) reference;
		if (undo) {
			ElementPropertyDefn propDefn = tmpElement.getPropertyDefn(propName);

			// To add client is done in resolving structure reference.

			tmpElement.resolveStructReference(module, propDefn);
		} else {
			StructRefValue value = (StructRefValue) tmpElement.getLocalProperty(module, propName);
			value.unresolved(value.getName());

			referred.dropClient(tmpElement);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget() {
		return module;
	}

}
