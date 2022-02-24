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

import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Included css style sheet structure
 * 
 */

public class IncludedCssStyleSheet extends Structure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String INCLUDED_CSS_STRUCT = "IncludedCssStyleSheet"; //$NON-NLS-1$

	/**
	 * Name of the file name member. This member is required for the structure.
	 */

	public static final String FILE_NAME_MEMBER = "fileName"; //$NON-NLS-1$

	/**
	 * Name of the external css file member.
	 */
	public static final String EXTERNAL_CSS_URI_MEMBER = "externalCssURI";//$NON-NLS-1$

	/**
	 * Name of the external css file member.
	 */
	public static final String USE_EXTERNAL_CSS = "useExternalCss";//$NON-NLS-1$

	/**
	 * The file name of the included library.
	 */

	protected String fileName;

	/**
	 * The URI of the external css.
	 */
	protected String externalCssURI;

	protected boolean useExternalCss;

	public String getStructName() {
		return INCLUDED_CSS_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.
	 * String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (FILE_NAME_MEMBER.equals(propName))
			return fileName;
		if (EXTERNAL_CSS_URI_MEMBER.equals(propName))
			return externalCssURI;
		if (USE_EXTERNAL_CSS.equals(propName))
			return useExternalCss;

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
		else if (EXTERNAL_CSS_URI_MEMBER.equals(propName))
			externalCssURI = (String) value;
		else if (USE_EXTERNAL_CSS.equals(propName))
			useExternalCss = ((Boolean) value).booleanValue();
		else
			assert false;
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

		PropertyDefn memberDefn = (PropertyDefn) getDefn().getMember(FILE_NAME_MEMBER);
		String fileName = (String) getProperty(module, memberDefn);
		if (StringUtil.isBlank(fileName)) {
			PropertyDefn defn = (PropertyDefn) getDefn().getMember(EXTERNAL_CSS_URI_MEMBER);
			String externalCssURI = (String) getProperty(module, defn);
			if (externalCssURI == null && !useExternalCss) {
				list.add(new PropertyValueException(element, getDefn().getMember(FILE_NAME_MEMBER), fileName,
						PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
			}
		}
		return list;
	}

	/**
	 * Gets the file name of the include css.
	 * 
	 * @return the file name of the include css
	 */

	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the file name of the include css.
	 * 
	 * @param theFileName the new file name to set
	 */

	public void setFileName(String theFileName) {
		fileName = theFileName;
	}

	/**
	 * Gets the URI of the external CSS.
	 * 
	 * @return the URI of the external CSS
	 */
	public String getExternalCssURI() {
		return externalCssURI;
	}

	/**
	 * Sets the URI of the external CSS.
	 * 
	 * @param externalCssURI the URI of the external CSS
	 */
	public void setExternalCssURI(String externalCssURI) {
		this.externalCssURI = externalCssURI;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public boolean isUseExternalCss() {
		return useExternalCss;
	}

	public void setUseExternalCss(boolean useExternalCss) {
		this.useExternalCss = useExternalCss;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.
	 * model.api.SimpleValueHandle, int)
	 */
	protected StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new IncludedCssStyleSheetHandle(valueHandle, index);
	}
}
