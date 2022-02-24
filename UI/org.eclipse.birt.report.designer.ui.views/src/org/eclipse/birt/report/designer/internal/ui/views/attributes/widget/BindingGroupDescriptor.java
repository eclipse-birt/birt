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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BindingGroupDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BindingGroupDescriptorProvider.BindingInfo;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class BindingGroupDescriptor extends PropertyDescriptor {

	protected Composite container;
	private Button datasetRadio;
	private Button reportItemRadio;
	private ComboViewer datasetCombo;
	private CCombo reportItemCombo;
	private Button bindingButton;

	public BindingGroupDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}

	public Control createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		container.setLayout(layout);
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		datasetRadio = FormWidgetFactory.getInstance().createButton(container, SWT.RADIO, isFormStyle());
		datasetRadio.setText(getProvider().getText(0));
		datasetRadio.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				refreshBinding();
				if (datasetRadio.getSelection() && getProvider().isBindingReference()
						&& (DEUtil.getBindingHolder(getProvider().getReportItemHandle(), true) == null
								|| DEUtil.getBindingHolder(getProvider().getReportItemHandle(), true)
										.getDataBindingType() != ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF))
					saveBinding();
			}

		});
		if (isFormStyle())
			datasetCombo = new ComboViewer(FormWidgetFactory.getInstance().createCCombo(container, true));
		else
			datasetCombo = new ComboViewer(new CCombo(container, SWT.READ_ONLY));
		datasetCombo.setLabelProvider(getProvider().getDataSetLabelProvider());
		datasetCombo.setContentProvider(getProvider().getDataSetContentProvider());
		datasetCombo.getCCombo().addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				saveBinding();
			}

		});
		GridData gd = new GridData();
		gd.widthHint = 300;
		datasetCombo.getCCombo().setLayoutData(gd);
		datasetCombo.getCCombo().setVisibleItemCount(30);
		bindingButton = FormWidgetFactory.getInstance().createButton(container, SWT.PUSH, isFormStyle());
		bindingButton.setText(getProvider().getText(1));
		bindingButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				getProvider().bindingDialog();
			}
		});
		reportItemRadio = FormWidgetFactory.getInstance().createButton(container, SWT.RADIO, isFormStyle());
		reportItemRadio.setText(getProvider().getText(2));
		reportItemRadio.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				refreshBinding();
				if (reportItemRadio.getSelection()
						&& getProvider().getReportItemHandle()
								.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_DATA
						&& (DEUtil.getBindingHolder(getProvider().getReportItemHandle(), true) == null
								|| DEUtil.getBindingHolder(getProvider().getReportItemHandle(), true)
										.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF))
					saveBinding();
			}

		});
		if (isFormStyle())
			reportItemCombo = FormWidgetFactory.getInstance().createCCombo(container, true);
		else
			reportItemCombo = new CCombo(container, SWT.READ_ONLY);
		reportItemCombo.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				saveBinding();
			}
		});
		gd = new GridData();
		gd.widthHint = 300;
		reportItemCombo.setLayoutData(gd);
		reportItemCombo.setVisibleItemCount(30);
		return container;
	}

	public Control getControl() {
		return container;
	}

	private void saveBinding() {
		BindingInfo info = new BindingInfo();
		if (datasetRadio.getSelection()) {
			info = (BindingInfo) ((StructuredSelection) datasetCombo.getSelection()).getFirstElement();
		} else {
			info.setBindingType(ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF);
			info.setBindingValue(reportItemCombo.getText());
		}
		try {
			this.oldInfo = info;
			save(info);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	public void load() {
		if (!provider.isEnable()) {
			datasetRadio.setEnabled(false);
			datasetRadio.setSelection(false);
			datasetCombo.getCCombo().setEnabled(false);
			bindingButton.setEnabled(false);
			reportItemRadio.setSelection(false);
			reportItemRadio.setEnabled(false);
			reportItemCombo.setEnabled(false);
			datasetCombo.getCCombo().deselectAll();
			reportItemCombo.deselectAll();
			return;
		}
		datasetRadio.setEnabled(true);
		reportItemRadio.setEnabled(true);
		BindingInfo info = (BindingInfo) getDescriptorProvider().load();
		if (info != null) {
			refreshBindingInfo(info);
		}
	}

	private void refreshBinding() {
		if (datasetRadio.getSelection()) {
			datasetRadio.setSelection(true);
			datasetCombo.getCCombo().setEnabled(true);
			bindingButton.setEnabled(getProvider().enableBindingButton());
			reportItemRadio.setSelection(false);
			reportItemCombo.setEnabled(false);
			if (datasetCombo.getCCombo().getSelectionIndex() == -1) {
				BindingInfo[] infos = getProvider().getAvailableDatasetItems();
				datasetCombo.setInput(infos);
				datasetCombo.setSelection(new StructuredSelection(infos[0]));
			}
		} else {
			datasetRadio.setSelection(false);
			datasetCombo.getCCombo().setEnabled(false);
			bindingButton.setEnabled(false);
			reportItemRadio.setSelection(true);
			reportItemCombo.setEnabled(true);
			if (reportItemCombo.getSelectionIndex() == -1) {
				reportItemCombo.setItems(getProvider().getReferences());
				reportItemCombo.select(0);
			}
		}
	}

	private BindingInfo oldInfo;

	private void refreshBindingInfo(BindingInfo info) {
		int type = info.getBindingType();
		Object value = info.getBindingValue();
		datasetCombo.setInput(getProvider().getAvailableDatasetItems());
		reportItemCombo.setItems(getProvider().getReferences());
		if (type == ReportItemHandle.DATABINDING_TYPE_NONE) {
			if (DEUtil.getBindingHolder(getProvider().getReportItemHandle(), true) != null
					&& DEUtil.getBindingHolder(getProvider().getReportItemHandle(), true)
							.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF)
				type = ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF;
		}
		switch (type) {
		case ReportItemHandle.DATABINDING_TYPE_NONE:
			if (oldInfo != null) {
				if (oldInfo.getBindingType() == ReportItemHandle.DATABINDING_TYPE_DATA) {
					selectDatasetType(info);
				} else if (oldInfo.getBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF) {
					selectReferenceType(info, value);
				} else {
					selectDatasetType(info);
				}
				break;
			}
		case ReportItemHandle.DATABINDING_TYPE_DATA:
			selectDatasetType(info);
			break;
		case ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF:
			selectReferenceType(info, value);
		}
	}

	private void selectReferenceType(BindingInfo info, Object value) {
		datasetRadio.setSelection(false);
		datasetCombo.getCCombo().setEnabled(false);
		bindingButton.setEnabled(false);

		reportItemRadio.setSelection(true);
		reportItemCombo.setEnabled(true);
		reportItemCombo.setText(value.toString());

		// From 2.3, Multi-view is supported to create related chart
		// view with current table/crosstab, the chart query in
		// multi-view is sharing table/crosstab, so it should be
		// read-only, disable button status for the case.
		if (info.isReadOnly()) {
			datasetRadio.setEnabled(false);
			reportItemRadio.setEnabled(false);
			reportItemCombo.setEnabled(false);
		}
	}

	private void selectDatasetType(BindingInfo value) {
		datasetRadio.setSelection(true);
		datasetCombo.getCCombo().setEnabled(true);
		datasetCombo.setSelection(new StructuredSelection(value));
		bindingButton.setEnabled(getProvider().enableBindingButton());
		reportItemRadio.setSelection(false);
		reportItemCombo.setEnabled(false);
	}

	public void save(Object obj) throws SemanticException {
		getProvider().save(obj);
	}

	private BindingGroupDescriptorProvider provider;

	public void setDescriptorProvider(IDescriptorProvider provider) {
		this.descriptorProvider = (BindingGroupDescriptorProvider) provider;
		if (provider instanceof BindingGroupDescriptorProvider)
			this.provider = (BindingGroupDescriptorProvider) provider;
	}

	public BindingGroupDescriptorProvider getProvider() {
		return provider;
	}

	public void setInput(Object handle) {
		super.setInput(handle);
	}
}
