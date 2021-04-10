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

import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatStringLayoutPeer;

/**
 * Format string page for formatting a string.
 */

public class FormatStringDescriptor extends FormatDescriptor {

	/**
	 * Constructs a new instance of format string page, default aligns the page
	 * vertically.
	 * 
	 * @param parent The parent container of the page.
	 * @param style  style of the page
	 */

	public FormatStringDescriptor() {
		this(PAGE_ALIGN_VIRTICAL, true);
	}

	/**
	 * Constructs a new instance of format string page.
	 * 
	 * @param parent        The parent container of the page.
	 * @param style         style of the page
	 * @param pageAlignment Aligns the page vertically(PAGE_ALIGN_VIRTICAL) or
	 *                      horizontally(PAGE_ALIGN_HORIZONTAL).
	 */

	public FormatStringDescriptor(int pageAlignment, boolean isFormStyle) {
		this(pageAlignment, isFormStyle, true);
	}

	/**
	 * Constructs a new instance of format string page.
	 * 
	 * @param parent        The parent container of the page.
	 * @param style         style of the page
	 * @param pageAlignment Aligns the page vertically(PAGE_ALIGN_VIRTICAL) or
	 *                      horizontally(PAGE_ALIGN_HORIZONTAL).
	 * @param showLocale    whether the UI to set locale will be shown
	 */

	public FormatStringDescriptor(int pageAlignment, boolean isFormStyle, boolean showLocale) {
		setFormStyle(isFormStyle);

		layoutPeer = new FormatStringLayoutPeer(pageAlignment, isFormStyle, showLocale);
	}

}