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

/**
 * IFormatPage for format number, string, dateTime
 */

public interface IFormatPage
{

	/**
	 * source type for style builder.
	 */
	public static int SOURCE_TYPE_STYLE = 0;

	/**
	 * source type for paramet builder.
	 */
	public static int SOURCE_TYPE_PARAMETER = 1;

	/**
	 * Sets input for the page.
	 * 
	 * @param formatString
	 *            The formatString.
	 */
	public void setInput( String formatString );

	/**
	 * Sets preview text for default use.
	 * 
	 * @param text
	 *            The preview text to be setted.
	 */
	public void setPreviewText( String text );

	/**
	 * Returns the format string from the page.
	 * 
	 * @return The format string.
	 */
	public String getFormatString( );

	/**
	 * Determines the format string of the page is modified or not.
	 * 
	 * @return True if the format string is modified.
	 */
	public boolean isFormatStrModified( );
}