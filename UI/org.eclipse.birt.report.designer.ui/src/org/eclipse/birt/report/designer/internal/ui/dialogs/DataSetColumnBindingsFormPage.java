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

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.DataSetColumnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * Data set binding page.
 */

public class DataSetColumnBindingsFormPage extends FormPage {

	private Button btnAddAggregateOn;
	private Button btnAddMeasureOn;
	private Button btnRefresh;

	// private Button generateAllBindingsButton;
	// Comments this button because of bug 143398.
	// private Button removeUnusedColumnButton;

	public DataSetColumnBindingsFormPage(Composite parent, DataSetColumnBindingsFormHandleProvider provider) {
		super(parent, FormPage.FULL_FUNCTION, provider, true);
		provider.setTableViewer(this.getTableViewer());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage#
	 * createControl()
	 */
	protected void createControl() {
		// createGenerateAllBindingsButton( );
		// Comments this calling because of bug 143398.
		// createRemoveUnusedColumnButton( );
		super.createControl();

		if (((DataSetColumnBindingsFormHandleProvider) provider).canAggregation()) {
			btnAddAggregateOn = new Button(this, SWT.PUSH);
			btnAddAggregateOn.setText(Messages.getString("FormPage.Button.Add.AggregateOn")); //$NON-NLS-1$
			btnAddAggregateOn.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					handleAddAggregateOnSelectEvent();
				}
			});
			btnAddMeasureOn = new Button(this, SWT.PUSH);
			btnAddMeasureOn.setText(Messages.getString("FormPage.Button.Add.MeasureOn")); //$NON-NLS-1$
			btnAddMeasureOn.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					handleAddMeasureOnSelectEvent();
				}
			});
		}
		btnRefresh = new Button(this, SWT.PUSH);
		btnRefresh.setText(Messages.getString("FormPage.Button.Binding.Refresh")); //$NON-NLS-1$
		btnRefresh.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleRefreshSelectEvent();
			}
		});

		fullLayout();
	}

	protected void handleAddAggregateOnSelectEvent() {
		int pos = table.getSelectionIndex();
		try {
			((DataSetColumnBindingsFormHandleProvider) provider).addAggregateOn(pos);
		} catch (Exception e) {
			WidgetUtil.processError(btnAddAggregateOn.getShell(), e);
			return;
		}

		refresh();
		table.setSelection(table.getItemCount() - 1);
	}

	protected void handleAddMeasureOnSelectEvent() {
		int pos = table.getSelectionIndex();
		try {
			((DataSetColumnBindingsFormHandleProvider) provider).addMeasureOn(pos);
		} catch (Exception e) {
			WidgetUtil.processError(btnAddMeasureOn.getShell(), e);
			return;
		}

		refresh();
		table.setSelection(table.getItemCount() - 1);
	}

	protected void handleRefreshSelectEvent() {
		((DataSetColumnBindingsFormHandleProvider) provider).generateAllBindingColumns();
		refresh();
	}

	// Comments this method because of bug 143398.
	// private void createRemoveUnusedColumnButton( )
	// {
	// removeUnusedColumnButton = new Button( this, SWT.BORDER );
	// removeUnusedColumnButton.setText( Messages.getString(
	// "DataSetColumnBindingsFormPage.Button.RemoveUnused" ) );
	// removeUnusedColumnButton.addSelectionListener( new SelectionAdapter( ) {
	//
	// public void widgetSelected( SelectionEvent e )
	// {
	// provider.removedUnusedColumnBindings( input );
	// }
	//
	// } );
	// }

	/*
	 * private void createGenerateAllBindingsButton( ) { generateAllBindingsButton =
	 * new Button( this, SWT.BORDER ); generateAllBindingsButton.setText(
	 * Messages.getString( "DataSetColumnBindingsFormPage.Button.Generate" ) );
	 * //$NON-NLS-1$ generateAllBindingsButton.addSelectionListener( new
	 * SelectionAdapter( ) {
	 * 
	 * public void widgetSelected( SelectionEvent e ) {
	 * provider.generateAllBindingColumns( ); } } ); }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage#
	 * fullLayout()
	 */
	protected void fullLayout() {
		super.fullLayout();
		// put btnDel flow over btnEdit

		// btnEdit.setVisible( false );

		Button button = btnAdd;
		int btnWidth = 60;

		if (btnAddAggregateOn != null) {
			button = btnAddAggregateOn;
			FormData data = new FormData();
			data.top = new FormAttachment(btnAdd, 0, SWT.BOTTOM);
			data.left = new FormAttachment(btnAdd, 0, SWT.LEFT);
			data.width = Math.max(btnWidth, btnAddAggregateOn.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
			btnAddAggregateOn.setLayoutData(data);

			if (this.provider instanceof DataSetColumnBindingsFormHandleProvider) {
				Object adaptableObject = ((DataSetColumnBindingsFormHandleProvider) this.provider).getBindingObject();
				if (adaptableObject != null) {
					IBindingDialogHelper helper = (IBindingDialogHelper) ElementAdapterManager
							.getAdapter(adaptableObject, IBindingDialogHelper.class);
					if (helper != null) {
						IBindingDialogHelper helperHelper = (IBindingDialogHelper) ElementAdapterManager
								.getAdapter(helper, IBindingDialogHelper.class);
						if (helperHelper != null) {
							helper = helperHelper;
						}
						if (helper.canProcessMeasure()) {
							button = btnAddMeasureOn;
							data = new FormData();
							data.top = new FormAttachment(btnAddAggregateOn, 0, SWT.BOTTOM);
							data.left = new FormAttachment(btnAddAggregateOn, 0, SWT.LEFT);
							data.width = Math.max(btnWidth,
									btnAddMeasureOn.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
							btnAddMeasureOn.setLayoutData(data);

						} else {
							data = new FormData();
							data.height = 0;
							data.width = 0;
							btnAddMeasureOn.setLayoutData(data);
						}
					}
				}
			}

			data = new FormData();
			data.top = new FormAttachment(button, 0, SWT.BOTTOM);
			data.left = new FormAttachment(button, 0, SWT.LEFT);
			data.width = Math.max(btnWidth, btnEdit.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
			btnEdit.setLayoutData(data);
		}

		FormData data = new FormData();
		data.top = new FormAttachment(btnEdit, 0, SWT.BOTTOM);
		data.left = new FormAttachment(btnEdit, 0, SWT.LEFT);
		data.width = Math.max(60, btnDel.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		btnDel.setLayoutData(data);

		if (btnRefresh != null) {
			data = new FormData();
			data.top = new FormAttachment(btnDel, 0, SWT.BOTTOM);
			data.left = new FormAttachment(btnDel, 0, SWT.LEFT);
			data.width = Math.max(60, btnRefresh.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
			btnRefresh.setLayoutData(data);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage#
	 * setInput(java.util.List)
	 */
	public void setInput(List elements) {
		super.setInput(elements);

		if (elements.size() > 0) {
			Object element = elements.get(0);
			setBindingObject((ReportElementHandle) element);
			fullLayout();
			checkButtonsEnabled();
		}

	}

	private void checkButtonsEnabled() {
		if (((DataSetColumnBindingsFormHandleProvider) provider).canAggregation()) {
			if (!btnAddAggregateOn.isDisposed())
				btnAddAggregateOn.setEnabled(provider.isEditable());
			if (!btnAddMeasureOn.isDisposed())
				btnAddMeasureOn.setEnabled(provider.isEditable());
		}
		if (!btnRefresh.isDisposed())
			btnRefresh.setEnabled(provider.isEditable());
	}

	private void setBindingObject(ReportElementHandle bindingObject) {
		((DataSetColumnBindingsFormHandleProvider) provider).setBindingObject(bindingObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage#
	 * elementChanged(org.eclipse.birt.report.model.api.DesignElementHandle,
	 * org.eclipse.birt.report.model.api.activity.NotificationEvent)
	 */
	public void elementChanged(DesignElementHandle elementHandle, NotificationEvent event) {
		checkButtonsEnabled();
	}

	public void generateAllBindingColumns() {
		((DataSetColumnBindingsFormHandleProvider) provider).generateAllBindingColumns();
	}

	public void generateBindingColumns(Object[] columns) {
		((DataSetColumnBindingsFormHandleProvider) provider).generateBindingColumns(columns);
	}
}
