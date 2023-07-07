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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Format date time page for formatting date and time.
 */

public class FormatDateTimePage extends FormatPage {

	/**
	 * Constructs a page for formatting date time, default aligns the page
	 * vertically.
	 *
	 * @param parent The container
	 * @param type
	 * @param style  The style of the page
	 */
	public FormatDateTimePage(Composite parent, int type, int style) {
		this(parent, type, style, PAGE_ALIGN_VIRTICAL, true);
	}

	/**
	 * Constructs a page for formatting date time.
	 *
	 * @param parent        The container
	 * @param type
	 * @param style         The style of the page
	 * @param pageAlignment Aligns the page vertically(PAGE_ALIGN_VIRTICAL) or
	 *                      horizontally(PAGE_ALIGN_HORIZONTAL).
	 * @param showLocale
	 */
	public FormatDateTimePage(Composite parent, int type, int style, int pageAlignment, boolean showLocale) {
		super(parent, style);

		this.setLayout(new FillLayout());

		layoutPeer = new FormatDateTimeLayoutPeer(type, pageAlignment, false, showLocale);

		layoutPeer.createLayout(this);
	}

}
