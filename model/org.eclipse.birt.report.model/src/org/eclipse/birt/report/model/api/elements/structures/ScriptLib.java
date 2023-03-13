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

package org.eclipse.birt.report.model.api.elements.structures;

import org.eclipse.birt.report.model.api.ScriptLibHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.Structure;

/**
 * Represents a jar file used for script handle event.
 *
 * Each jar file has the following properties:
 *
 * <p>
 * <dl>
 * <dt><strong>name</strong></dt>
 * <dd>name of jar file.</dd>
 * </dl>
 * <p>
 *
 *
 */

public class ScriptLib extends Structure {
	/**
	 * Name of the structure.
	 */

	public static final String STRUCTURE_NAME = "ScriptLib"; //$NON-NLS-1$

	/**
	 * Name of jar file.
	 */

	public static final String SCRIPTLIB_NAME_MEMBER = "name"; //$NON-NLS-1$

	/**
	 * value of jar file's name.
	 */

	protected String name = null;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	@Override
	public String getStructName() {
		return STRUCTURE_NAME;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.
	 * String)
	 */

	@Override
	protected Object getIntrinsicProperty(String propName) {
		if (SCRIPTLIB_NAME_MEMBER.equals(propName)) {
			return name;
		}

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java.lang.
	 * String, java.lang.Object)
	 */

	@Override
	protected void setIntrinsicProperty(String propName, Object value) {
		if (SCRIPTLIB_NAME_MEMBER.equals(propName)) {
			name = (String) value;
		} else {
			assert false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.
	 * model.api.SimpleValueHandle, int)
	 */

	@Override
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new ScriptLibHandle(valueHandle, index);
	}

	/**
	 * Sets the jar file name value.
	 *
	 * @param name the jar file name value to set
	 * @throws SemanticException
	 */

	public void setName(String name) {
		setProperty(ScriptLib.SCRIPTLIB_NAME_MEMBER, name);
	}

	/**
	 * Returns jar file name value.
	 *
	 * @return the jar file name value
	 */

	public String getName() {
		return (String) getProperty(null, ScriptLib.SCRIPTLIB_NAME_MEMBER);
	}

}
