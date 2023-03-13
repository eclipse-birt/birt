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

package org.eclipse.birt.report.designer.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatDateTimePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatNumberPage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatStringPage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatPage;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ibm.icu.util.ULocale;

/**
 * The builder used to generate a format string
 * <dt><b>Styles: </b></dt>
 * <dd>STRING</dd>
 * <dd>NUMBER</dd>
 * <dd>DATETIME</dd>
 */

public class FormatBuilder extends BaseDialog {

	/** Style Constants */
	/** String format constant */
	public static final int STRING = 1;
	/** Number format constant */
	public static final int NUMBER = 2;
	/** DateTime format constant */
	public static final int DATETIME = 3;
	/** DateTime format constant */
	public static final int DATE = 4;
	/** DateTime format constant */
	public static final int TIME = 5;

	private static final String DLG_TITLE = Messages.getString("FormatBuilder.Title"); //$NON-NLS-1$
	private IFormatPage page;
	private String formatCategory = null;
	private String formatPattern = null;
	private ULocale formatLocale = null;
	private String previewText = null;

	private int type;

	/**
	 * Constructs a new instance of the format builder
	 *
	 * @param style the style of the format builder
	 *
	 */
	public FormatBuilder(int type) {
		super(DLG_TITLE);
		assert type == STRING || type == NUMBER || type == DATETIME || type == DATE || type == TIME;
		this.type = type;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		ScrolledComposite scrollContent = new ScrolledComposite(composite, SWT.H_SCROLL | SWT.V_SCROLL);
		scrollContent.setAlwaysShowScrollBars(false);
		scrollContent.setExpandHorizontal(true);
		scrollContent.setLayout(new FillLayout());
		scrollContent.setLayoutData(new GridData(GridData.FILL_BOTH));
		switch (type) {
		case STRING:
			page = new FormatStringPage(scrollContent, SWT.NONE, IFormatPage.PAGE_ALIGN_VIRTICAL, false);
			break;
		case NUMBER:
			page = new FormatNumberPage(scrollContent, SWT.NONE, IFormatPage.PAGE_ALIGN_VIRTICAL, false);
			break;
		case DATETIME:
		case DATE:
		case TIME:
			page = new FormatDateTimePage(scrollContent, type, SWT.NONE, IFormatPage.PAGE_ALIGN_VIRTICAL, false);
			break;
		}
		Point size = ((Composite) page).computeSize(SWT.DEFAULT, SWT.DEFAULT);
		((Composite) page).setSize(size);
		scrollContent.setContent((Composite) page);
		UIUtil.bindHelp(composite, IHelpContextIds.FORMAT_BUILDER_ID);
		return composite;
	}

	/*
	 * Set preview text
	 */
	public void setPreviewText(String previewText) {
		this.previewText = previewText;
	}

	/*
	 * Set format categrory and patten
	 */
	public void setInputFormat(String formatCategroy, String formatPattern, ULocale formatLocale) {
		assert !StringUtil.isBlank(formatCategroy);
		this.formatCategory = formatCategroy;
		this.formatPattern = formatPattern;
		this.formatLocale = formatLocale;
	}

	@Override
	protected boolean initDialog() {
		page.setInput(formatCategory, formatPattern, formatLocale);
		page.setPreviewText(previewText);
		return true;
	}

	@Override
	protected void okPressed() {
		if (page.isFormatModified()) {

			setResult(new Object[] { page.getCategory(), page.getPattern(), page.getLocale() });
			super.okPressed();
		} else {
			cancelPressed();
		}
	}
}
