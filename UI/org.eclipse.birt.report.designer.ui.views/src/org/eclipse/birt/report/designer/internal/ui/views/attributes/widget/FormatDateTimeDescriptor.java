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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatDateTimeLayoutPeer;
import org.eclipse.birt.report.designer.ui.dialogs.FormatBuilder;

/**
 * Format date time page for formatting date and time.
 */

public class FormatDateTimeDescriptor extends FormatDescriptor {

	/**
	 * Constructs a page for formatting date time, default aligns the page
	 * virtically.
	 * 
	 * @param parent The container
	 * @param style  The style of the page
	 */

	public FormatDateTimeDescriptor() {
		this(PAGE_ALIGN_VIRTICAL, true);
	}

	/**
	 * Constructs a page for formatting date time.
	 * 
	 * @param parent        The container
	 * @param style         The style of the page
	 * @param pageAlignment Aligns the page vertically(PAGE_ALIGN_VIRTICAL) or
	 *                      horizontally(PAGE_ALIGN_HORIZONTAL).
	 */

	public FormatDateTimeDescriptor(int pageAlignment, boolean isFormStyle) {
		this(pageAlignment, isFormStyle, true);
	}

	/**
	 * Constructs a page for formatting date time.
	 * 
	 * @param parent        The container
	 * @param style         The style of the page
	 * @param pageAlignment Aligns the page vertically(PAGE_ALIGN_VIRTICAL) or
	 *                      horizontally(PAGE_ALIGN_HORIZONTAL).
	 * @param showLocale    whether the UI to set locale will be shown
	 * 
	 */
	public FormatDateTimeDescriptor(int pageAlignment, boolean isFormStyle, boolean showLocale) {
		setFormStyle(isFormStyle);

		layoutPeer = new FormatDateTimeLayoutPeer(FormatBuilder.DATETIME, pageAlignment, isFormStyle, showLocale);
	}

}