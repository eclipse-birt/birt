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

package org.eclipse.birt.report.designer.ui.cubebuilder.dialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class FilterListDialog extends BaseDialog implements Listener {

	private Button btnAdd, btnEdit, btnDel;

	private Table table;

	private TableViewer tableViewer;

	private FilterHandleProvider provider;

	private int selectIndex;

	private List inputList = new ArrayList();

	private ReportElementHandle input;

	public FilterListDialog(FilterHandleProvider provider) {
		super(Messages.getString("FilterListDialog.Shell.Title")); //$NON-NLS-1$
		this.provider = provider;
	}

	public void setInput(ReportElementHandle input) {
		this.input = input;
	}

	protected Control createDialogArea(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.CUBE_FILTER_LIST_DIALOG);

		Composite dialogArea = (Composite) super.createDialogArea(parent);

		Composite content = new Composite(dialogArea, SWT.NONE);
		content.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		content.setLayout(layout);
		createFilterArea(content);

		List inputs = new ArrayList();
		inputs.add(input);

		if (inputs.size() != 1) {
			enableUI(false);
			return null;
		}
		enableUI(true);
		editableUI(provider.isEditable());
		inputList = inputs;
		tableViewer.setInput(inputs);
		refresh();
		updateBtnStatus();
		updateBindingParameters();

		return dialogArea;
	}

	private void createFilterArea(final Composite comp) {
		table = new Table(comp, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		String[] columnNames = provider.getColumnNames();
		for (int i = 0; i < columnNames.length; i++) {
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setText(columnNames[i]);
			column.setWidth(100);
			// column.setWidth( provider.getColumnWidths( )[i] );
		}
		table.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (!provider.isEditable())
					return;
				if (e.keyCode == SWT.DEL) {
					int itemCount = table.getItemCount();
					int pos = table.getSelectionIndex();
					if (selectIndex == itemCount - 1) {
						selectIndex--;
					}
					try {
						provider.doDeleteItem(pos);
					} catch (Exception e1) {
						WidgetUtil.processError(comp.getShell(), e1);
					}
				} else if (e.character == '\r') // return is pressed
				{
					edit();
				}
			}
		});

		table.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				selectIndex = table.getSelectionIndex();
				updateBtnStatus();
			}
		});
		table.addMouseListener(new MouseAdapter() {

			public void mouseDoubleClick(MouseEvent e) {
				if (!provider.isEditable()) {
					return;
				}
				edit();
			}
		});

		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		tableViewer.setColumnProperties(provider.getColumnNames());
		tableViewer.setContentProvider(new TableContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());

		btnAdd = new Button(comp, SWT.PUSH);
		btnAdd.setText(Messages.getString("FilterListDialog.Button.AddWithDialog")); //$NON-NLS-1$
		btnAdd.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
				stack.startTrans(Messages.getString("FilterListDialog.Menu.ModifyProperty")); //$NON-NLS-1$
				int pos = table.getSelectionIndex();
				boolean sucess = false;
				try {
					sucess = provider.doAddItem(pos);
				} catch (Exception e1) {
					stack.rollback();
					WidgetUtil.processError(comp.getShell(), e1);
					return;
				}
				if (sucess) {
					stack.commit();
				} else {
					stack.rollback();
				}
				table.setSelection(table.getItemCount() - 1);
				updateBtnStatus();

			}
		});

		btnEdit = new Button(comp, SWT.PUSH);
		btnEdit.setText(Messages.getString("FilterListDialog.Button.EditWithDialog")); //$NON-NLS-1$
		btnEdit.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				edit();
			}
		});

		btnDel = new Button(comp, SWT.PUSH);
		btnDel.setText(Messages.getString("FilterListDialog.Button.Delete")); //$NON-NLS-1$
		btnDel.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (tableViewer.isCellEditorActive()) {
					tableViewer.cancelEditing();
				}
				int pos = table.getSelectionIndex();
				if (pos == -1) {
					table.setFocus();
					return;
				}
				selectIndex = pos;
				int itemCount = table.getItemCount();
				if (selectIndex == itemCount - 1) {
					selectIndex--;
				}
				try {
					provider.doDeleteItem(pos);
				} catch (Exception e1) {
					WidgetUtil.processError(comp.getShell(), e1);
				}
				updateBtnStatus();
			}
		});

		setLayout(comp);
	}

	protected void setLayout(Composite comp) {
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = WidgetUtil.SPACING;
		layout.marginWidth = WidgetUtil.SPACING;
		layout.horizontalSpacing = WidgetUtil.SPACING;
		layout.verticalSpacing = WidgetUtil.SPACING;
		comp.setLayout(layout);

		GridData data = new GridData(GridData.FILL_BOTH);
		data.verticalSpan = 5;
		data.minimumWidth = 430;
		data.minimumHeight = 200;
		table.setLayoutData(data);

		setButtonLayoutData(btnAdd);
		setButtonLayoutData(btnEdit);
		setButtonLayoutData(btnDel);

	}

	private void enableUI(boolean enabled) {
		if (tableViewer != null) {
			table.setEnabled(enabled);
			btnAdd.setEnabled(enabled);
			btnEdit.setEnabled(enabled);
			btnDel.setEnabled(enabled);

			if (enabled) {
				updateBtnStatus();
			}
		}
	}

	private void editableUI(boolean editable) {
		if (tableViewer != null) {
			btnAdd.setEnabled(editable);
			btnDel.setEnabled(editable);
			btnEdit.setEnabled(editable);

			if (editable) {
				updateBtnStatus();
			}
		}
	}

	private void edit() {
		int pos = table.getSelectionIndex();
		if (pos == -1) {
			table.setFocus();
			return;
		}
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans(Messages.getString("FilterListDialog.Menu.ModifyProperty")); //$NON-NLS-1$
		if (!provider.doEditItem(pos)) {
			stack.rollback();
			return;
		}
		stack.commit();
		table.setSelection(pos);

	}

	protected void refresh() {
		if (tableViewer.getTable().isDisposed())
			return;
		tableViewer.refresh();
		table.select(selectIndex);
		table.setFocus();
		updateBtnStatus();
		updateBindingParameters();
		editableUI(provider.isEditable());
	}

	private void updateBtnStatus() {
		if (!provider.isEditable())
			return;

		int selectIndex = table.getSelectionIndex();
		int min = 0;
		int max = table.getItemCount() - 1;

		if ((min <= selectIndex) && (selectIndex <= max)) {
			btnDel.setEnabled(true);
			if (btnEdit != null)
				btnEdit.setEnabled(true);
		} else {
			btnDel.setEnabled(false);
			if (btnEdit != null)
				btnEdit.setEnabled(false);
		}
	}

	private void updateBindingParameters() {
		ParamBindingHandle[] bindingParams = null;

		if (inputList.get(0) instanceof ReportItemHandle) {
			ReportItemHandle inputHandle = (ReportItemHandle) inputList.get(0);
			List list = new ArrayList();
			for (Iterator iterator = inputHandle.paramBindingsIterator(); iterator.hasNext();) {
				ParamBindingHandle handle = (ParamBindingHandle) iterator.next();
				list.add(handle);
			}
			bindingParams = new ParamBindingHandle[list.size()];
			list.toArray(bindingParams);
		}
		provider.setBindingParams(bindingParams);
	}

	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return provider.getImage(element, columnIndex);
		}

		public String getColumnText(Object element, int columnIndex) {
			return provider.getColumnText(element, columnIndex);
		}
	}

	private class TableContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			Object[] elements = provider.getElements(inputElement);
			Object model = DEUtil.getInputFirstElement(input);
			if (model instanceof ReportElementHandle) {
				ReportElementHandle element = (ReportElementHandle) model;
				element.addListener(FilterListDialog.this);
			}
			return elements;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	public void elementChanged(DesignElementHandle focus, NotificationEvent event) {
		if (provider.needRefreshed(event)) {
			refresh();
		}
	}
}
