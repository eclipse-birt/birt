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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.IncludedLibraryHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;

/**
 * This class represents one include library of report design.
 * <p>
 * This is a managed object, meaning that all changes should be made though the
 * command layer so that they can be undone and redone. Each column hint has the
 * following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>File Name </strong></dt>
 * <dd>File name is required for a include library.</dd>
 * 
 * <dt><strong>Namespace </strong></dt>
 * <dd>Namespace of the library, which is used to identify one library.</dd>
 * </dl>
 * 
 */

public class IncludedLibrary extends Structure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String INCLUDED_LIBRARY_STRUCT = "IncludedLibrary"; //$NON-NLS-1$

	/**
	 * Name of the file name member. This member is required for the structure.
	 */

	public static final String FILE_NAME_MEMBER = "fileName"; //$NON-NLS-1$

	/**
	 * Name of the namespace member.
	 */

	public static final String NAMESPACE_MEMEBR = "namespace"; //$NON-NLS-1$

	/**
	 * The file name of the included library.
	 */

	protected String fileName;

	/**
	 * The namespace of the included library.
	 */

	protected String namespace;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return INCLUDED_LIBRARY_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (FILE_NAME_MEMBER.equals(propName))
			return fileName;
		else if (NAMESPACE_MEMEBR.equals(propName))
			return namespace;

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java
	 * .lang.String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {
		if (FILE_NAME_MEMBER.equals(propName))
			fileName = (String) value;
		else if (NAMESPACE_MEMEBR.equals(propName))
			namespace = (String) value;
		else
			assert false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#validate(org.eclipse.birt
	 * .report.model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List validate(Module module, DesignElement element) {
		ArrayList list = new ArrayList();

		checkStringMember(fileName, FILE_NAME_MEMBER, element, list);
		checkStringMember(namespace, NAMESPACE_MEMEBR, element, list);

		return list;
	}

	/**
	 * Gets the file name of the include library.
	 * 
	 * @return the file name of the include library
	 */

	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the file name of the include library.
	 * 
	 * @param theFileName the new file name to set
	 */

	public void setFileName(String theFileName) {
		fileName = theFileName;
	}

	/**
	 * Returns the namespace of the included library. The namespace identify one
	 * library uniquely in design file.
	 * 
	 * @return the namespace of the included library.
	 */

	public String getNamespace() {
		return namespace;
	}

	/**
	 * Sets the namespace for library.
	 * 
	 * @param namespace the namespace to set.
	 */

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */
	protected StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new IncludedLibraryHandle(valueHandle, index);
	}
}
