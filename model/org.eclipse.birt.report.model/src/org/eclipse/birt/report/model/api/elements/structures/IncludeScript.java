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

package org.eclipse.birt.report.model.api.elements.structures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.IncludeScriptHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * This class represents a include script. The class gives the file name of the
 * include script. Used when a report design {@link ReportDesign}gives a file
 * name of include script in the report design. Each script in report design has
 * the following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>File Name </strong></dt>
 * <dd>a include script structure in the report design has a required file name
 * to load the script.</dd>
 * </dl>
 * 
 */

public class IncludeScript extends Structure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String INCLUDE_SCRIPT_STRUCT = "IncludeScript"; //$NON-NLS-1$

	/**
	 * Name of the "file-name" property.
	 */

	public static final String FILE_NAME_MEMBER = "fileName"; //$NON-NLS-1$

	/**
	 * Value of the file name property.
	 */

	protected String fileName = null;

	/**
	 * Default constructor.
	 * 
	 */

	public IncludeScript() {
	}

	/**
	 * Constructs the script with the required file name.
	 * 
	 * @param fileName file name of the script
	 */

	public IncludeScript(String fileName) {
		this.fileName = fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return INCLUDE_SCRIPT_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.
	 * String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (FILE_NAME_MEMBER.equalsIgnoreCase(propName))
			return fileName;

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

	protected void setIntrinsicProperty(String propName, Object value) {
		if (FILE_NAME_MEMBER.equals(propName))
			fileName = (String) value;
		else
			assert false;
	}

	/**
	 * Returns file name of the script.
	 * 
	 * @return file name of the script
	 */

	public String getFileName() {
		return (String) getProperty(null, FILE_NAME_MEMBER);
	}

	/**
	 * Sets the file name of the script.
	 * 
	 * @param fileName the file name to set
	 */

	public void setFileName(String fileName) {
		setProperty(FILE_NAME_MEMBER, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#validate(org.eclipse.birt.report
	 * .model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List validate(Module module, DesignElement element) {
		ArrayList list = new ArrayList();

		if (StringUtil.isBlank(fileName)) {
			list.add(new PropertyValueException(element, getDefn().getMember(FILE_NAME_MEMBER), fileName,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.
	 * model.api.SimpleValueHandle, int)
	 */
	protected StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new IncludeScriptHandle(valueHandle, index);
	}
}