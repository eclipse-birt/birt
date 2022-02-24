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

import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IFastConsumerProcessor;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.util.ModelEventInfo;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFormHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IFormProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * The contained page of Filters, Sorting, Groups and High-lights.The page
 * provides all table-based operations such as moving, adding, deleting. The
 * Filters, Sorting, Groups and High-lights will use FormPage as UI and provides
 * corresponding Model processors.
 * 
 * 
 */
public class FormPropertyDescriptor extends PropertyDescriptor implements IFastConsumerProcessor {

	/**
	 * The simple UI type,not support moving and editing operations.
	 */
	public final static int SIMPLE_FUNCTION = 1;

	/**
	 * The normal function UI type.
	 */
	public final static int NORMAL_FUNCTION = 2;

	/**
	 * The full function UI type with editing operations
	 */
	public final static int FULL_FUNCTION = 3;

	/**
	 * The full function UI type with editing operations, and the buttons are
	 * horizontal
	 */
	public final static int FULL_FUNCTION_HORIZONTAL = 4;

	/**
	 * The full function UI type with editing operations, and the buttons are
	 * horizontal
	 */
	public final static int NO_UP_DOWN = 5;

	/**
	 * The UI type.
	 */
	private int style;

	/**
	 * The index of the button group created in the same page.
	 */
	private int index = 0;

	/**
	 * Set the index of this form created in the same page.
	 * 
	 * @param index
	 */
	public void setButtonGroupIndex(int index) {
		this.index = index;
	}

	/**
	 * When Add button is clicked, whether invoke dialogs.
	 */
	protected boolean bAddWithDialog = false;

	/**
	 * Buttons for moving operations.
	 */
	protected Button btnUp, btnDown;

	/**
	 * Buttons for modifying operations.
	 */
	protected Button btnAdd, btnDel, btnEdit;

	/**
	 * The table widget that present the data of Filters, Sorting, Groups or
	 * High-lights.
	 */
	protected Table table;

	/**
	 * The TableViewer of the table widget.
	 */
	protected TableViewer tableViewer;

	/**
	 * The current selection index.
	 */
	private int selectIndex;

	private Composite formPanel;

	/**
	 * Constructor
	 * 
	 * @param parent   A widget which will be the parent of the new instance (cannot
	 *                 be null)
	 * @param style    The style of widget to construct
	 * @param provider The data provider and processor
	 */
	public FormPropertyDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public void setButtonWithDialog(boolean withDialog) {
		this.bAddWithDialog = withDialog;
	}

	protected TableViewer getTableViewer() {
		return tableViewer;
	}

	private void enableUI(boolean enabled) {
		if (tableViewer != null) {
			table.setEnabled(enabled);

			if (btnUp != null)
				btnUp.setEnabled(enabled);
			if (btnDown != null)
				btnDown.setEnabled(enabled);

			btnAdd.setEnabled(enabled);
			btnDel.setEnabled(enabled);

			if (btnEdit != null)
				btnEdit.setEnabled(enabled);

			if (enabled) {
				updateArraw();
			}
		}
	}

	private void editableUI(boolean editable) {
		if (tableViewer != null) {
			if (style != SIMPLE_FUNCTION && style != NO_UP_DOWN) {
				btnUp.setEnabled(editable);
				btnDown.setEnabled(editable);
			}
			btnAdd.setEnabled(editable);
			btnDel.setEnabled(editable);

			if (style == FULL_FUNCTION || style == FULL_FUNCTION_HORIZONTAL) {
				btnEdit.setEnabled(editable);
			}

			if (editable) {
				updateArraw();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.page.TabPage
	 * #setInput(java.util.List)
	 */
	public void setInput(Object input) {
		this.input = input;
		getDescriptorProvider().setInput(input);
	}

	public void load() {
		if (getDescriptorProvider() instanceof IFormProvider) {
			boolean enable = ((AbstractFormHandleProvider) getDescriptorProvider()).isEnable();
			boolean editable = ((IFormProvider) getDescriptorProvider()).isEditable();
			if (!enable) {
				enableUI(false);
				return;
			}

			tableViewer.setInput(input);
			enableUI(true);
			editableUI(editable);
			updateBindingParameters();
		}

	}

	public Control getControl() {
		return formPanel;
	}

	/**
	 * Creates UI widgets
	 * 
	 */
	public Control createControl(Composite parent) {
		assert getDescriptorProvider() != null;
		assert getDescriptorProvider() instanceof IFormProvider;
		formPanel = FormWidgetFactory.getInstance().createComposite(parent);

		if (isFormStyle())
			table = FormWidgetFactory.getInstance().createTable(formPanel,
					SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		else
			table = new Table(formPanel, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		String[] columnNames = ((IFormProvider) getDescriptorProvider()).getColumnNames();
		for (int i = 0; i < columnNames.length; i++) {
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setText(columnNames[i]);
			column.setWidth(((IFormProvider) getDescriptorProvider()).getColumnWidths()[i]);
		}
		table.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				handleTableKeyPressEvent(e);
			}
		});

		table.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleTableSelectEvent();
			}
		});
		table.addMouseListener(new MouseAdapter() {

			public void mouseDoubleClick(MouseEvent e) {
				handleTableMouseDoubleClickEvent();
			}
		});
		createTableViewer();

		if (isFormStyle())
			btnDel = FormWidgetFactory.getInstance().createButton(formPanel, "", //$NON-NLS-1$
					SWT.PUSH);
		else
			btnDel = new Button(formPanel, SWT.BORDER);

		if (descriptorProvider.getDisplayName()
				.equals(Messages.getString("ReportPageGenerator.List.Resources.PropertiesFile"))) //$NON-NLS-1$
		{
			btnDel.setText(Messages.getString("FormPage.Button.DeleteFile")); //$NON-NLS-1$
		} else if (descriptorProvider.getDisplayName()
				.equals(Messages.getString("ReportPageGenerator.List.Resources.JarFile"))) //$NON-NLS-1$
		{
			btnDel.setText(Messages.getString("FormPage.Button.DeleteFile.Alt1")); //$NON-NLS-1$
		} else if (descriptorProvider.getDisplayName()
				.equals(Messages.getString("ReportPageGenerator.List.Resources.JsFile"))) //$NON-NLS-1$
		{
			btnDel.setText(Messages.getString("FormPage.Button.DeleteFile.Alt2")); //$NON-NLS-1$
		} else {
			btnDel.setText(Messages.getString("FormPage.Button.Delete")); //$NON-NLS-1$
		}

		btnDel.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleDelSelectEvent();
			}
		});

		if (isFormStyle())
			btnAdd = FormWidgetFactory.getInstance().createButton(formPanel, "", //$NON-NLS-1$
					SWT.PUSH);
		else
			btnAdd = new Button(formPanel, SWT.BORDER);
		if (bAddWithDialog == true) {
			if (descriptorProvider.getDisplayName()
					.equals(Messages.getString("ReportPageGenerator.List.Resources.PropertiesFile"))) //$NON-NLS-1$
			{
				btnAdd.setText(Messages.getString("FormPage.Button.AddWithDialog.PROP")); //$NON-NLS-1$
			} else if (descriptorProvider.getDisplayName()
					.equals(Messages.getString("ReportPageGenerator.List.Resources.JsFile"))) //$NON-NLS-1$
			{
				btnAdd.setText(Messages.getString("FormPage.Button.AddWithDialog.JS")); //$NON-NLS-1$
			} else if (descriptorProvider.getDisplayName()
					.equals(Messages.getString("ReportPageGenerator.List.Resources.JarFile"))) //$NON-NLS-1$
			{
				btnAdd.setText(Messages.getString("FormPage.Button.AddWithDialog.JAR")); //$NON-NLS-1$
			} else {
				btnAdd.setText(Messages.getString("FormPage.Button.AddWithDialog")); //$NON-NLS-1$
			}
		} else {
			btnAdd.setText(Messages.getString("FormPage.Button.Add")); //$NON-NLS-1$
		}

		btnAdd.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleAddSelectEvent();

			}
		});

		if (style == FULL_FUNCTION || style == FULL_FUNCTION_HORIZONTAL || style == NO_UP_DOWN) {
			if (isFormStyle())
				btnEdit = FormWidgetFactory.getInstance().createButton(formPanel, "", SWT.PUSH); //$NON-NLS-1$
			else
				btnEdit = new Button(formPanel, SWT.BORDER);
			if (bAddWithDialog == true) {
				btnEdit.setText(Messages.getString("FormPage.Button.EditWithDialog")); //$NON-NLS-1$
			} else {
				btnEdit.setText(Messages.getString("FormPage.Button.Edit")); //$NON-NLS-1$
			}

			btnEdit.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					handleEditSelectEvent();
				}
			});
		}

		if (style != SIMPLE_FUNCTION && style != NO_UP_DOWN) {
			if (isFormStyle())
				btnUp = FormWidgetFactory.getInstance().createButton(formPanel, "", SWT.PUSH); //$NON-NLS-1$
			else
				btnUp = new Button(formPanel, SWT.BORDER);

			if (0 == index) {
				btnUp.setText(Messages.getString("FormPage.Button.Up")); //$NON-NLS-1$
			} else if (1 == index) {
				btnUp.setText(Messages.getString("FormPage.Button.Up.Alt1")); //$NON-NLS-1$
			} else if (2 == index) {
				btnUp.setText(Messages.getString("FormPage.Button.Up.Alt2")); //$NON-NLS-1$
			}
			btnUp.setToolTipText(Messages.getString("FormPage.toolTipText.Up")); //$NON-NLS-1$
			btnUp.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					handleUpSelectEvent();
				}
			});
			if (isFormStyle())
				btnDown = FormWidgetFactory.getInstance().createButton(formPanel, "", SWT.PUSH); //$NON-NLS-1$
			else
				btnDown = new Button(formPanel, SWT.BORDER);
			if (0 == index) {
				btnDown.setText(Messages.getString("FormPage.Button.Down")); //$NON-NLS-1$
			} else if (1 == index) {
				btnDown.setText(Messages.getString("FormPage.Button.Down.Alt1")); //$NON-NLS-1$
			} else if (2 == index) {
				btnDown.setText(Messages.getString("FormPage.Button.Down.Alt2")); //$NON-NLS-1$
			}

			btnDown.setToolTipText(Messages.getString("FormPage.toolTipText.Down")); //$NON-NLS-1$
			btnDown.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					handleDownSelectEvent();
				}
			});

		}
		switch (style) {
		case SIMPLE_FUNCTION:
			simpleLayout();
			break;
		case NORMAL_FUNCTION:
			normallLayout();
			break;
		case FULL_FUNCTION:
			fullLayout();
			break;
		case FULL_FUNCTION_HORIZONTAL:
			fullLayoutHorizontal();
			break;
		case NO_UP_DOWN:
			noUpDownLayout();
			break;
		default:
			break;
		}
		return formPanel;
	}

	private void edit() {
		int pos = table.getSelectionIndex();
		if (pos == -1) {
			table.setFocus();
			return;
		}

		if (!((AbstractFormHandleProvider) getDescriptorProvider()).edit(pos))
			return;

		if (table.isDisposed())
			return;
		table.setSelection(pos);

	}

	protected void updateArraw() {
		if (!((IFormProvider) getDescriptorProvider()).isEditable())
			return;
		if (style == SIMPLE_FUNCTION) {
			return;
		}
		int selectIndex = table.getSelectionIndex();
		int min = 0;
		int max = table.getItemCount() - 1;
		// if ( !( provider instanceof GroupHandleProvider ) )
		// {
		// max--;
		// }

		if (style != NO_UP_DOWN) {

			if (selectIndex <= 0)
				btnUp.setEnabled(false);
			else
				btnUp.setEnabled(true);
			if (selectIndex >= max || (selectIndex == -1)) {
				btnDown.setEnabled(false);
				if (selectIndex > max)
					btnUp.setEnabled(false);
			} else
				btnDown.setEnabled(true);
		}

		if ((min <= selectIndex) && (selectIndex <= max)) {
			btnDel.setEnabled(true);
			if (btnEdit != null)
				btnEdit.setEnabled(true);
		} else {
			btnDel.setEnabled(false);
			if (btnEdit != null)
				btnEdit.setEnabled(false);
		}

		if (getDescriptorProvider() instanceof IFormProvider) {
			IFormProvider provider = (IFormProvider) getDescriptorProvider();
			if (provider.isEnable()) {
				if (btnAdd.isEnabled())
					btnAdd.setEnabled(provider.isAddEnable(tableViewer.getSelection()));
				if (btnEdit.isEnabled())
					btnEdit.setEnabled(provider.isEditEnable(tableViewer.getSelection()));
				if (btnDel.isEnabled())
					btnDel.setEnabled(provider.isDeleteEnable(tableViewer.getSelection()));
				if (style != NO_UP_DOWN) {
					if (btnUp.isEnabled())
						btnUp.setEnabled(provider.isUpEnable(tableViewer.getSelection()));
					if (btnDown.isEnabled())
						btnDown.setEnabled(provider.isDownEnable(tableViewer.getSelection()));
				}
			}
		}

		if (getDescriptorProvider() instanceof FilterHandleProvider) {
			btnUp.setVisible(false);
			btnDown.setVisible(false);
		}
	}

	private void updateBindingParameters() {
		if (getDescriptorProvider() instanceof FilterHandleProvider) {
			((FilterHandleProvider) getDescriptorProvider()).updateBindingParameters();
		}
	}

	/**
	 * Changes the position of one item to a new location.
	 * 
	 * @param oldPos The old position
	 * @param newPos The new Position
	 */
	private void moveItem(int oldPos, int newPos) {
		try {
			((IFormProvider) getDescriptorProvider()).doMoveItem(oldPos, newPos);
		} catch (Exception e) {
			WidgetUtil.processError(formPanel.getShell(), e);
		}
	}

	/**
	 * Creates the TableViewer and set all kinds of processors.
	 * 
	 */
	private void createTableViewer() {

		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		tableViewer.setColumnProperties(((IFormProvider) getDescriptorProvider()).getColumnNames());
		CellEditor[] editors = ((IFormProvider) getDescriptorProvider()).getEditors(table);
		if (editors != null && editors.length > 0) {
			tableViewer.setCellEditors(((IFormProvider) getDescriptorProvider()).getEditors(table));
			tableViewer.setCellModifier(new FormCellModifier());
		} else {
			tableViewer.setCellModifier(new FormCellModifier() {

				public boolean canModify(Object element, String property) {
					return false;
				}
			});
		}
		tableViewer.setContentProvider(((AbstractFormHandleProvider) getDescriptorProvider())
				.getFormContentProvider(this, getDescriptorProvider()));
		tableViewer.setLabelProvider(new FormLabelProvider());

	}

	protected void simpleLayout() {
		FormLayout layout = new FormLayout();
		layout.marginWidth = WidgetUtil.SPACING;
		layout.marginBottom = WidgetUtil.SPACING;
		layout.marginTop = 0;
		layout.spacing = WidgetUtil.SPACING;
		formPanel.setLayout(layout);

		int width[] = ((IFormProvider) getDescriptorProvider()).getColumnWidths();
		int dataWidth = 0;
		for (int i = 0; i < width.length; i++) {
			dataWidth += width[i];
		}

		// int height = QUICK_BUTTON_HEIGHT - 2;
		FormData data = new FormData();
		data.left = new FormAttachment(0, (dataWidth + 15 - layout.spacing) / 2
				- Math.max(btnWidth, btnAdd.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x));
		data.width = Math.max(btnWidth, btnAdd.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		// data.height = Math.max( btnAdd.computeSize( -1, -1 ).y, height );
		btnAdd.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(btnAdd, 0, SWT.RIGHT);
		data.width = Math.max(btnWidth, btnDel.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		// data.height = data.height = Math.max( btnDel.computeSize( -1, -1 ).y,
		// height );
		btnDel.setLayoutData(data);

		data = new FormData();
		data.top = new FormAttachment(btnAdd, 0, SWT.BOTTOM);
		data.left = new FormAttachment(0, 0);

		data.right = new FormAttachment(0, dataWidth + 15);
		data.bottom = new FormAttachment(100);
		table.setLayoutData(data);
	}

	/**
	 * Layouts widgets for simple UI type.
	 * 
	 */
	protected void normallLayout() {
		FormLayout layout = new FormLayout();
		layout.marginBottom = WidgetUtil.SPACING;
		layout.marginTop = 0;
		layout.marginWidth = WidgetUtil.SPACING;
		layout.spacing = WidgetUtil.SPACING;
		formPanel.setLayout(layout);
		// int height = QUICK_BUTTON_HEIGHT - 2;
		FormData data = new FormData();
		data.right = new FormAttachment(100);
		data.width = Math.max(btnWidth, btnDown.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		// data.height = height;
		btnDown.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(btnDown, 0, SWT.LEFT);
		data.width = Math.max(btnWidth, btnUp.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		// data.height = height;
		btnUp.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(btnUp, 0, SWT.LEFT);
		data.width = Math.max(btnWidth, btnDel.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		// data.height = height;
		btnDel.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(btnDel, 0, SWT.LEFT);
		data.width = Math.max(btnWidth, btnAdd.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		// data.height = height;
		btnAdd.setLayoutData(data);

		data = new FormData();
		data.top = new FormAttachment(btnUp, 0, SWT.BOTTOM);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100);
		data.bottom = new FormAttachment(100);
		table.setLayoutData(data);

	}

	protected void fullLayoutHorizontal() {
		FormLayout layout = new FormLayout();
		layout.marginBottom = WidgetUtil.SPACING;
		layout.marginTop = 0;
		layout.marginWidth = WidgetUtil.SPACING;
		layout.spacing = WidgetUtil.SPACING;
		formPanel.setLayout(layout);
		// int height = QUICK_BUTTON_HEIGHT - 2;
		FormData data = new FormData();
		data.right = new FormAttachment(100);
		data.width = Math.max(btnWidth, btnDown.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		// data.height = height;
		btnDown.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(btnDown, 0, SWT.LEFT);
		data.width = Math.max(btnWidth, btnUp.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		// data.height = height;
		btnUp.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(btnUp, 0, SWT.LEFT);
		data.width = Math.max(btnWidth, btnEdit.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		// data.height = height;
		btnEdit.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(btnEdit, 0, SWT.LEFT);
		data.width = Math.max(btnWidth, btnDel.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		// data.height = height;
		btnDel.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(btnDel, 0, SWT.LEFT);
		data.width = Math.max(btnWidth, btnAdd.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		// data.height = height;
		btnAdd.setLayoutData(data);

		data = new FormData();
		data.top = new FormAttachment(btnUp, 0, SWT.BOTTOM);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100);
		data.bottom = new FormAttachment(100);
		table.setLayoutData(data);
	}

	int btnWidth = 60;

	protected void noUpDownLayout() {
		FormLayout layout = new FormLayout();
		layout.marginBottom = WidgetUtil.SPACING;
		layout.marginTop = 1;
		layout.marginWidth = WidgetUtil.SPACING;
		layout.spacing = WidgetUtil.SPACING;
		formPanel.setLayout(layout);

		FormData data = new FormData();
		data.right = new FormAttachment(90);
		data.top = new FormAttachment(0, 0);
		data.width = Math.max(btnWidth, btnAdd.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		btnAdd.setLayoutData(data);

		data = new FormData();
		data.top = new FormAttachment(btnAdd, 0, SWT.BOTTOM);
		data.left = new FormAttachment(btnAdd, 0, SWT.LEFT);
		data.width = Math.max(btnWidth, btnEdit.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		btnEdit.setLayoutData(data);

		data = new FormData();
		data.top = new FormAttachment(btnEdit, 0, SWT.BOTTOM);
		data.left = new FormAttachment(btnEdit, 0, SWT.LEFT);
		data.width = Math.max(btnWidth, btnDel.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		btnDel.setLayoutData(data);

		data = new FormData();
		data.top = new FormAttachment(btnAdd, 0, SWT.TOP);
		data.bottom = new FormAttachment(100);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(btnAdd, 0, SWT.LEFT);
		table.setLayoutData(data);
	}

	protected void fullLayout() {
		FormLayout layout = new FormLayout();
		layout.marginBottom = WidgetUtil.SPACING;
		layout.marginTop = 1;
		layout.marginWidth = WidgetUtil.SPACING;
		layout.spacing = WidgetUtil.SPACING;
		formPanel.setLayout(layout);

		FormData data = new FormData();
		data.right = new FormAttachment(90);
		data.top = new FormAttachment(0, 0);
		data.width = Math.max(btnWidth, btnAdd.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		btnAdd.setLayoutData(data);

		data = new FormData();
		data.top = new FormAttachment(btnAdd, 0, SWT.BOTTOM);
		data.left = new FormAttachment(btnAdd, 0, SWT.LEFT);
		data.width = Math.max(btnWidth, btnEdit.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		btnEdit.setLayoutData(data);

		data = new FormData();
		data.top = new FormAttachment(btnEdit, 0, SWT.BOTTOM);
		data.left = new FormAttachment(btnEdit, 0, SWT.LEFT);
		data.width = Math.max(btnWidth, btnDel.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		btnDel.setLayoutData(data);

		data = new FormData();
		data.top = new FormAttachment(btnDel, 0, SWT.BOTTOM);
		data.left = new FormAttachment(btnDel, 0, SWT.LEFT);
		data.width = Math.max(btnWidth, btnUp.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		btnUp.setLayoutData(data);

		data = new FormData();
		data.top = new FormAttachment(btnUp, 0, SWT.BOTTOM);
		data.left = new FormAttachment(btnUp, 0, SWT.LEFT);
		data.width = Math.max(btnWidth, btnDown.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		btnDown.setLayoutData(data);

		data = new FormData();
		data.top = new FormAttachment(btnAdd, 0, SWT.TOP);
		data.bottom = new FormAttachment(100);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(btnAdd, 0, SWT.LEFT);
		table.setLayoutData(data);
	}

	private class FormLabelProvider extends LabelProvider implements ITableLabelProvider {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java
		 * .lang.Object, int)
		 */
		public Image getColumnImage(Object element, int columnIndex) {
			return ((IFormProvider) getDescriptorProvider()).getImage(element, columnIndex);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.
		 * lang.Object, int)
		 */
		public String getColumnText(Object element, int columnIndex) {
			return ((IFormProvider) getDescriptorProvider()).getColumnText(element, columnIndex);
		}
	}

	private class FormCellModifier implements ICellModifier {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object,
		 * java.lang.String)
		 */
		public boolean canModify(Object element, String property) {
			return ((IFormProvider) getDescriptorProvider()).canModify(element, property);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object,
		 * java.lang.String)
		 */
		public Object getValue(Object element, String property) {
			return ((IFormProvider) getDescriptorProvider()).getValue(element, property);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object,
		 * java.lang.String, java.lang.Object)
		 */
		public void modify(Object element, String property, Object value) {

			TableItem item = (TableItem) element;
			try {
				((AbstractFormHandleProvider) getDescriptorProvider()).transModify(item.getData(), property, value);
			} catch (Exception e) {
				WidgetUtil.processError(table.getShell(), e);
			}
		}
	}

	public void setHidden(boolean isHidden) {
		WidgetUtil.setExcludeGridData(formPanel, isHidden);
	}

	public void setVisible(boolean isVisible) {
		formPanel.setVisible(isVisible);
	}

	protected void handleAddSelectEvent() {
		int pos = table.getSelectionIndex();
		try {
			((AbstractFormHandleProvider) getDescriptorProvider()).add(pos);
		} catch (Exception e) {
			WidgetUtil.processError(btnAdd.getShell(), e);
			return;
		}
		if (table.isDisposed())
			return;
		if (table.getItemCount() > 0)
			table.setSelection(table.getItemCount() - 1);
		updateArraw();
	}

	protected void handleDelSelectEvent() {
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
			((IFormProvider) getDescriptorProvider()).doDeleteItem(pos);
		} catch (Exception e1) {
			WidgetUtil.processError(btnDel.getShell(), e1);
		}
		updateArraw();
	}

	protected void handleTableMouseDoubleClickEvent() {
		if (!((IFormProvider) getDescriptorProvider()).isEditable())
			return;
		if (style == FULL_FUNCTION || style == FULL_FUNCTION_HORIZONTAL || style == NO_UP_DOWN) {
			edit();
		}
	}

	protected void handleTableSelectEvent() {
		selectIndex = table.getSelectionIndex();
		updateArraw();
	}

	protected void handleTableKeyPressEvent(KeyEvent e) {
		if (!((IFormProvider) getDescriptorProvider()).isEditable())
			return;
		if (e.keyCode == SWT.DEL) {
			int itemCount = table.getItemCount();
			int pos = table.getSelectionIndex();

			if (pos < 0) // select nothing
			{
				return;
			}
			if (selectIndex == itemCount - 1) {
				selectIndex--;
			}
			try {
				((IFormProvider) getDescriptorProvider()).doDeleteItem(pos);
			} catch (Exception e1) {
				WidgetUtil.processError(table.getShell(), e1);
			}
		} else if (e.character == '\r') // return is pressed
		{
			if (style == FULL_FUNCTION || style == FULL_FUNCTION_HORIZONTAL) {
				edit();
			}
		}
	}

	protected void handleEditSelectEvent() {
		edit();
	}

	protected void handleUpSelectEvent() {
		if (tableViewer.isCellEditorActive()) {
			tableViewer.cancelEditing();
		}
		int oldPos = table.getSelectionIndex();

		selectIndex = oldPos - 1;
		moveItem(oldPos, oldPos - 1);
		updateArraw();
	}

	protected void handleDownSelectEvent() {
		if (tableViewer.isCellEditorActive()) {
			tableViewer.cancelEditing();
		}
		int oldPos = table.getSelectionIndex();

		selectIndex = oldPos + 1;
		moveItem(oldPos, oldPos + 1);
		updateArraw();
	}

	public void save(Object obj) throws SemanticException {
		// TODO Auto-generated method stub

	}

	private List eventList = new LinkedList();

	public void addElementEvent(DesignElementHandle focus, NotificationEvent ev) {
		ModelEventInfo event = new ModelEventInfo(focus, ev);
		eventList.add(event);
	}

	public void clear() {
		eventList.clear();
	}

	public boolean isOverdued() {
		return getControl() == null || getControl().isDisposed();
	}

	public void postElementEvent() {
		while (eventList.size() > 0) {
			if (((IFormProvider) getDescriptorProvider())
					.needRefreshed(((ModelEventInfo) eventList.get(0)).getEvent())) {
				if (getControl() != null && !getControl().isDisposed()) {
					tableViewer.refresh();
					table.select(selectIndex);
					// table.setFocus( );
					updateArraw();
					updateBindingParameters();
				}
			}
			eventList.remove(0);
		}
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

}
