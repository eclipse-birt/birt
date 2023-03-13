/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

class Structure {

	protected StructureHandle structureHandle;

	Structure(StructureHandle structureHandle) {
		this.structureHandle = structureHandle;
	}

	/**
	 * Sets the value of the property.
	 *
	 * @param propName the property name.
	 * @param value    the property value.
	 * @throws SemanticException
	 */
	protected void setProperty(String propName, Object value) throws SemanticException {
		ActivityStack cmdStack = structureHandle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			structureHandle.setProperty(propName, value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();

	}
}
