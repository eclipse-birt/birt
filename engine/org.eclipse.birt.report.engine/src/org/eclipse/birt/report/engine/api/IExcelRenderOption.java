/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api;

public interface IExcelRenderOption extends IRenderOption {

	/**
	 * The option to decide if the text out will be wrapped
	 */
	public static final String WRAPPING_TEXT = "excelRenderOption.wrappingText";

	/**
	 * This is the option to decide in which office version will you output the
	 * excel file
	 */
	public static final String OFFICE_VERSION = "excelRenderOption.officeVersion";

	/**
	 * The option to hide the gridlines in the worksheet
	 */
	public static final String HIDE_GRIDLINES = "excelRenderOption.hideGridlines";

	/**
	 * This is the option to decide in which office version will you output the
	 * excel file
	 */
	public static final String OPTION_MULTIPLE_SHEET = "excelRenderOption.multipleSheet";

	/**
	 * Excel will ignore all image items when this option is on.
	 */
	public static final String IGNORE_IMAGE = "excelRenderOption.ignoreImage";

	/**
	 * 
	 * @param wrappingText
	 */
	public void setWrappingText(boolean wrappingText);

	/**
	 * 
	 * @param officeVersion
	 */
	public void setOfficeVersion(String officeVersion);

	/**
	 * 
	 * @return if the text is wrapped
	 */
	public boolean getWrappingText();

	/**
	 * 
	 * @return officeVersion
	 */
	public String getOfficeVersion();

	public boolean isEnableMultipleSheet();

	public void setEnableMultipleSheet(boolean enableMultipleSheet);
}
