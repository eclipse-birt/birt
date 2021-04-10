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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import com.ibm.icu.util.ULocale;

/**
 * IFormatPage for format number, string, dateTime
 */

public interface IFormatPage {

	/**
	 * Aligns the page vietically.
	 */
	public static int PAGE_ALIGN_VIRTICAL = 0;

	/**
	 * Aligns the page horizontally.
	 */
	public static int PAGE_ALIGN_HORIZONTAL = 1;

	/**
	 * Sets preview text for default use.
	 * 
	 * @param text The preview text to be set.
	 */
	public void setPreviewText(String text);

	/**
	 * Sets input for the format page.
	 * 
	 * @param category The format category.
	 * @param pattern  The format pattern.
	 */
	public void setInput(String category, String pattern, ULocale formatLocale);

	/**
	 * Sets input for the page.
	 * 
	 * @param formatString The formatString.
	 */
	public void setInput(String formatString);

	/**
	 * Gets format category.
	 * 
	 * @return The format category.
	 */
	public String getCategory();

	/**
	 * Gets format pattern.
	 * 
	 * @return The format pattern.
	 */
	public String getPattern();

	public ULocale getLocale();

	/**
	 * Returns the format string from the page.
	 * 
	 * @return The format string.
	 */
	public String getFormatString();

	/**
	 * Determines the format string of the page is modified or not.
	 * 
	 * @return True if the format string is modified.
	 */
	public boolean isFormatModified();

	/**
	 * Determines the page is dirty.
	 * 
	 * @return True if the page is dirty.
	 */
	public boolean isDirty();

	/**
	 * Adds format change listener to the format page.
	 * 
	 * @param listener The format change listener to be added.
	 */
	public void addFormatChangeListener(IFormatChangeListener listener);
}