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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ScriptLib;

/**
 * Represents a handle of jar file used for script handle event.
 *
 * Each jar file has following properties:
 *
 * <p>
 * <dl>
 * <dt><strong>name</strong></dt>
 * <dd>name of jar file.</dd>
 * </dl>
 * <p>
 *
 */

public class ScriptLibHandle extends StructureHandle {
	/**
	 * Constructs the handle of jar file.
	 *
	 * @param valueHandle the value handle for jar file list of one property
	 * @param index       the position of this jar file in the list
	 */

	public ScriptLibHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Sets the jar file name value.
	 *
	 * @param name the jar file name value to set
	 * @throws SemanticException
	 */

	public void setName(String name) throws SemanticException {
		setProperty(ScriptLib.SCRIPTLIB_NAME_MEMBER, name);
	}

	/**
	 * Returns jar file name value.
	 *
	 * @return the jar file name value
	 */

	public String getName() {
		return getStringProperty(ScriptLib.SCRIPTLIB_NAME_MEMBER);
	}

}
