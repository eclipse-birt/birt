/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

public class EXCELRenderOption extends RenderOption implements IExcelRenderOption {

	public EXCELRenderOption() {
		super();
	}

	public EXCELRenderOption(IRenderOption options) {
		super(options);
	}

	/**
	 * Get office version.
	 * 
	 * @return office version
	 */
	public String getOfficeVersion() {

		if (getStringOption(OFFICE_VERSION) == null)
			return "office2003";
		return getStringOption(OFFICE_VERSION);
	}

	/**
	 * Get the flag which indicates if text wrapped.
	 * 
	 * @return text if it is wrapped
	 */
	public boolean getWrappingText() {
		return getBooleanOption(WRAPPING_TEXT, true);
	}

	/**
	 * Set office version.
	 * 
	 * @param officeVersion
	 */
	public void setOfficeVersion(String officeVersion) {
		setOption(OFFICE_VERSION, officeVersion);
	}

	/**
	 * Set wrapping text.
	 * 
	 * @param wrappingText
	 */
	public void setWrappingText(boolean wrappingText) {
		setOption(WRAPPING_TEXT, wrappingText);
	}

	/**
	 * Set hide gridlines.
	 * 
	 * @param hide gridlines
	 */
	public void setHideGridlines(boolean hideGridlines) {
		setOption(HIDE_GRIDLINES, hideGridlines);
	}

	/**
	 * Get hide gridlines.
	 * 
	 * @param hide gridlines
	 */
	public boolean getHideGridlines() {
		return getBooleanOption(HIDE_GRIDLINES, false);
	}

	public boolean isEnableMultipleSheet() {
		return getBooleanOption(OPTION_MULTIPLE_SHEET, true);
	}

	public void setEnableMultipleSheet(boolean enableMultipleSheet) {
		setOption(OPTION_MULTIPLE_SHEET, enableMultipleSheet);
	}

}
