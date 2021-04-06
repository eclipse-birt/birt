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

import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;

/**
 * Represents the handle of the included script. Each script in report design
 * has the following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>File Name </strong></dt>
 * <dd>a include script structure in the report design has a required file name
 * to load the script.</dd>
 * </dl>
 * 
 */

public class IncludeScriptHandle extends StructureHandle {

	/**
	 * Constructs the handle of the included script.
	 * 
	 * @param valueHandle the value handle for the included script list of one
	 *                    property
	 * @param index       the position of this included script in the list
	 */

	public IncludeScriptHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns file name of the script.
	 * 
	 * @return file name of the script
	 */

	public String getFileName() {
		return getStringProperty(IncludeScript.FILE_NAME_MEMBER);
	}

	/**
	 * Sets the file name of the script.
	 * 
	 * @param fileName the file name to set
	 */

	public void setFileName(String fileName) {
		setPropertySilently(IncludeScript.FILE_NAME_MEMBER, fileName);
	}

}