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

package org.eclipse.birt.report.designer.internal.ui.extension;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.ui.IPreferenceConstants;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemBuilderUI;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemPropertyEditUI;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemUI;
import org.eclipse.jface.util.Assert;

/**
 * The object used to cache the UI extension points
 */

public class ExtendedElementUIPoint {

	private String extensionName;

	private IReportItemUI reportItemUI = null;

	private Map attributesMap = new HashMap(5);

	private HashMap classMap = new HashMap(2);

	/**
	 * Construct an new instance with the given extension name. All default
	 * value will be initialized.
	 * 
	 * @param extensionName
	 *            the extension name of the extended element
	 */
	ExtendedElementUIPoint(String extensionName) {
		this.extensionName = extensionName;

		//Default value
		setAttribute(IExtensionConstants.EDITOR_SHOW_IN_DESIGNER, Boolean.TRUE);
		setAttribute(IExtensionConstants.EDITOR_SHOW_IN_MASTERPAGE,
				Boolean.TRUE);
		setAttribute(IExtensionConstants.EDITOR_CAN_RESIZE, Boolean.TRUE);
		setAttribute(IExtensionConstants.PALETTE_CATEGORY,
				IPreferenceConstants.PALETTE_CONTENT);
	}

	/**
	 * Gets the extension name of the element
	 * 
	 * @return Returns the extension name;
	 */
	public String getExtensionName() {
		return extensionName;
	}

	/**
	 * Gets the UI instance of the element
	 * 
	 * @return Returns the UI instance;
	 */
	public IReportItemUI getReportItemUI() {
		return reportItemUI;
	}

	public IReportItemBuilderUI getReportItemBuilderUI() {
		return (IReportItemBuilderUI) classMap.get(IExtensionConstants.BUILDER);
	}

	public IReportItemPropertyEditUI getReportItemPropertyEditUI() {
		return (IReportItemPropertyEditUI) classMap
				.get(IExtensionConstants.PROPERTYEDIT);
	}

	/**
	 * Gets the corresponding attribute of the key of the extended element
	 * 
	 * @param key
	 *            the key of the attribute.It cannot be null
	 * @return Returns the corresponding attribute, or null if the key is
	 *         invalid or the corresponding attribute hasn't been set
	 */
	public Object getAttribute(String key) {
		Assert.isLegal(key != null);
		return attributesMap.get(key);
	}

	/**
	 * Sets the UI instance of the element
	 * 
	 * @param reportItemUI
	 *            the UI instance to set.It cannot be null
	 */
	void setReportItemUI(IReportItemUI reportItemUI) {
		Assert.isLegal(reportItemUI != null);
		this.reportItemUI = reportItemUI;
	}

	/**
	 * Sets the UI instance of the element
	 * 
	 * @param reportItemBuilderUI
	 *            the Builder UI instance to set.It can be null
	 */
	void setReportItemBuilderUI(IReportItemBuilderUI reportItemBuilderUI) {
		classMap.put(IExtensionConstants.BUILDER, reportItemBuilderUI);
	}

	/**
	 * Sets the corresponding attribute of the key of the extended element
	 * 
	 * @param key
	 *            the key of the attribute.It cannot be null
	 */
	void setAttribute(String key, Object value) {
		Assert.isLegal(key != null);
		attributesMap.put(key, value);
	}

	/**
	 * Set the corresponding class instance of the key of the extended element
	 * 
	 * @param className
	 * @param object
	 */
	public void setClass(String key, Object value) {
		classMap.put(key, value);
	}
}