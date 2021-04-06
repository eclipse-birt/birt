/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.reportitem.ui.dialogs.widget.FormPage;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IFormProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * The class is responsible to set filter conditions.
 * 
 */
public class ExtendedItemFilterDialog extends BaseDialog {

	/** The report item handle. */
	private ExtendedItemHandle fReportItemHandle;

	/** The field will provide all controls and operations for filter setting. */
	private IFormProvider fFilterHandleProvider = new FilterHandleProvider() {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.
		 * FilterHandleProvider#getColumnWidths()
		 */
		public int[] getColumnWidths() {
			return new int[] { 150, 100, 150, 150
					// Default width of columns.
			};
		}
	};

	/**
	 * Consturctor of the class.
	 * 
	 * @param reportItemHandle
	 */
	public ExtendedItemFilterDialog(ExtendedItemHandle reportItemHandle) {
		super(UIUtil.getDefaultShell());
		setShellStyle(SWT.RESIZE);
		fReportItemHandle = reportItemHandle;
		fFilterHandleProvider.setInput(reportItemHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#setShellStyle(int)
	 */
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.
	 * Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		// Binding default help.
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.DIALOG_DATA_SET_FILTER);
		getShell().setText(Messages.getString("dataset.editor.filters")); //$NON-NLS-1$

		// Create filter page.
		Composite composite = (Composite) super.createDialogArea(parent);

		FormPage filterFormPage = new FormPage(composite, FormPage.FULL_FUNCTION, fFilterHandleProvider, true);
		filterFormPage.setLayoutData(new GridData(GridData.FILL_BOTH));

		List handleList = new ArrayList();
		handleList.add(fReportItemHandle);
		filterFormPage.setInput(handleList);

		return composite;
	}

	/**
	 * Set a specified filter handle provider.
	 * 
	 * @param filterHandleProvider
	 * @since 2.3
	 */
	public void setFilterHandleProvider(IFormProvider filterHandleProvider) {
		fFilterHandleProvider = filterHandleProvider;
	}
}
