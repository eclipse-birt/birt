/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceFilter;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

/**
 * ResourceFilterDialog
 */
public class ResourceFilterDialog extends BaseDialog {

	private List filters = new ArrayList();
	private Text descriptionText;
	private CheckboxTableViewer viewer;

	public ResourceFilterDialog() {
		super(Messages.getString("ResourceFilterDialog.Title"));//$NON-NLS-1$
	}

	public void setInput(List filters) {
		this.filters = filters;
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		Composite container = new Composite(composite, SWT.NONE);
		container.setLayout(new GridLayout());

		createViewerArea(container);
		createHelpArea(container);
		initViewer();

		UIUtil.bindHelp(composite, IHelpContextIds.RESOURCE_FILTER_DIALOG_ID);
		return composite;
	}

	private void initViewer() {
		viewer.setInput(filters);
		for (int i = 0; i < filters.size(); i++) {
			ResourceFilter filter = (ResourceFilter) filters.get(i);
			viewer.setChecked(filter, filter.isEnabled());
		}

	}

	private void createHelpArea(Composite parent) {
		Label descriptionLabel = new Label(parent, SWT.NONE);
		descriptionLabel.setText(Messages.getString("ResourceFilterDialog.Lable.Description")); //$NON-NLS-1$
		descriptionText = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY | SWT.WRAP);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 40;
		descriptionText.setLayoutData(gd);
	}

	private void createViewerArea(Composite parent) {
		Label viewerLabel = new Label(parent, SWT.NONE);
		viewerLabel.setText(Messages.getString("ResourceFilterDialog.Lable.Viewer"));//$NON-NLS-1$

		Table table = new Table(parent, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
		table.setLinesVisible(false);
		table.setHeaderVisible(false);

		TableColumn column = new TableColumn(table, SWT.LEFT);
		column.setWidth(250);

		viewer = new CheckboxTableViewer(table);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 250;
		gd.heightHint = 200;
		viewer.getTable().setLayoutData(gd);

		table.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				StructuredSelection selection = ((StructuredSelection) viewer.getSelection());
				Object obj = null;
				if (selection != null && (obj = selection.getFirstElement()) != null) {
					ResourceFilter filter = (ResourceFilter) obj;
					if (filter.getDescription() != null)
						descriptionText.setText(filter.getDescription());
				}
			}

			public void widgetSelected(SelectionEvent e) {
				StructuredSelection selection = ((StructuredSelection) viewer.getSelection());
				Object obj = null;
				if (selection != null && (obj = selection.getFirstElement()) != null) {
					ResourceFilter filter = (ResourceFilter) obj;
					if (filter.getDescription() != null)
						descriptionText.setText(filter.getDescription());
				}
			}

		});
		FilterProvider provider = new FilterProvider();
		viewer.setContentProvider(provider);
		viewer.setLabelProvider(provider);
		viewer.addCheckStateListener(new ICheckStateListener() {

			public void checkStateChanged(CheckStateChangedEvent event) {

				ResourceFilter filter = (ResourceFilter) event.getElement();
				if (event.getChecked()) {
					filter.setEnabled(true);
				} else {
					filter.setEnabled(false);
				}
			}

		});
		viewer.setInput(filters);
	}

	/**
	 * FilterProvider
	 */
	private static class FilterProvider extends LabelProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List)
				return ((List) inputElement).toArray();
			else
				return new Object[0];
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public String getText(Object element) {
			if (element instanceof ResourceFilter) {
				return ((ResourceFilter) element).getDisplayName();
			} else
				return ""; //$NON-NLS-1$
		}

//		public String getToolTip( Object element )
//		{
//			return getText( element );
//		}

	}

}
