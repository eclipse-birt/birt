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

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Format string page for formatting a string.
 */
public class FormatStringPage extends FormatPage {

	/**
	 * Constructs a new instance of format string page, default aligns the page
	 * virtically.
	 * 
	 * @param parent The parent container of the page.
	 * @param style  style of the page
	 */
	public FormatStringPage(Composite parent, int style) {
		this(parent, style, PAGE_ALIGN_VIRTICAL, true);
	}

	/**
	 * Constructs a new instance of format string page.
	 * 
	 * @param parent        The parent container of the page.
	 * @param style         style of the page
	 * @param pageAlignment Aligns the page virtically(PAGE_ALIGN_VIRTICAL) or
	 *                      horizontally(PAGE_ALIGN_HORIZONTAL).
	 */
	public FormatStringPage(Composite parent, int style, int pageAlignment, boolean showLocale) {
		super(parent, style);

		this.setLayout(new FillLayout());

		layoutPeer = new FormatStringLayoutPeer(pageAlignment, false, showLocale);

		layoutPeer.createLayout(this);
	}

}