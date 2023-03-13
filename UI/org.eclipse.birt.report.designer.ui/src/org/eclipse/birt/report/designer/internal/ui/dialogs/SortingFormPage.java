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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.GroupHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.ISortingFormHandleProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.core.runtime.Platform;
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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * The contained page of Filters, Sorting, Groups and Hight-lights.The page
 * provides all table-based operations such as moving, adding, deleting. The
 * Filters, Sorting, Groups and Hight-lights will use FormPage as UI and
 * provides corresponding Model processors.
 *
 *
 */
public class SortingFormPage extends Composite implements Listener {

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
	 * The UI type.
	 */
	private int style;

	/**
	 * When Add button is clicked, whether invoke dialogs.
	 */
	private boolean bAddWithDialog;

	/**
	 * The prompted text that present the FormPage served as Filters, Sorting,
	 * Groups and Hight-lights.
	 */
	protected Label title;
	/**
	 * Buttons for modifying operations.
	 */
	protected Button btnAdd, btnDel, btnEdit;

	/**
	 * The table widget that present the data of Filters, Sorting, Groups or
	 * Hight-lights.
	 */
	protected Table table;

	/**
	 * The TableViewer of the table widget.
	 */
	private TableViewer tableViewer;

	/**
	 * The data provider and processor.
	 */
	protected ISortingFormHandleProvider provider;

	/**
	 * The current selection index.
	 */
	private int selectIndex;

	protected List input = new ArrayList();

	public static final int QUICK_BUTTON_HEIGHT = Platform.getOS().equals(Platform.OS_WIN32) ? 20 : 22;

	/**
	 * Constructor
	 *
	 * @param parent   A widget which will be the parent of the new instance (cannot
	 *                 be null)
	 * @param style    The style of widget to construct
	 * @param provider The data provider and processor
	 */
	public SortingFormPage(Composite parent, int style, ISortingFormHandleProvider provider, boolean bAddWithDialog) {
		super(parent, SWT.NONE);
		assert provider != null;
		this.provider = provider;
		this.style = style;
		this.bAddWithDialog = bAddWithDialog;
		createControl();
	}

	protected TableViewer getTableViewer() {
		return tableViewer;
	}

	/**
	 * Constructor
	 *
	 * @param parent   A widget which will be the parent of the new instance (cannot
	 *                 be null)
	 * @param style    The style of widget to construct
	 * @param provider The data provider and processor
	 */
	public SortingFormPage(Composite parent, int style, ISortingFormHandleProvider provider) {
		this(parent, style, provider, false);
	}

	private void enableUI(boolean enabled) {
		if (tableViewer != null) {
			table.setEnabled(enabled);
			btnAdd.setEnabled(enabled);
			btnDel.setEnabled(enabled);

			if (style == FULL_FUNCTION || style == FULL_FUNCTION_HORIZONTAL) {
				btnEdit.setEnabled(enabled);
			}

			if (enabled) {
				updateArraw();
			}
		}
	}

	private void editableUI(boolean editable) {
		if (tableViewer != null) {
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
	public void setInput(List elements) {
		if (elements.size() != 1) {
			enableUI(false);
			return;
		}

		enableUI(true);
		editableUI(provider.isEditable());
		deRegisterListeners();
		input = elements;
		tableViewer.setInput(elements);
		refresh();
		registerListeners();
		updateArraw();
		updateBindingParameters();
	}

	protected void createControl() {
		title = new Label(this, SWT.NONE);
		title.setText(provider.getTitle());

		table = new Table(this, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		org.eclipse.swt.widgets.Listener sortListener = new org.eclipse.swt.widgets.Listener() {

			@Override
			public void handleEvent(Event e) {
				int modelPos = -1;
				if (table.getSelectionIndex() > -1) {
					modelPos = provider.getOriginalIndex(table.getSelectionIndex());
				}
				// determine new sort column and direction
				TableColumn sortColumn = table.getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = table.getSortDirection();
				if (sortColumn == currentColumn) {
					dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
				} else {
					table.setSortColumn(currentColumn);
					dir = SWT.UP;
					for (int i = 0; i < table.getColumnCount(); i++) {
						if (currentColumn == table.getColumn(i)) {
							provider.setSortingColumnIndex(i);
							break;
						}
					}
				}
				// update data displayed in table
				provider.setSortDirection(dir);
				table.setSortDirection(dir);
				tableViewer.refresh();

				if (modelPos > -1) {
					table.setSelection(provider.getShowIndex(modelPos));
				}
			}

		};

		String[] columnNames = provider.getColumnNames();
		for (int i = 0; i < columnNames.length; i++) {
			TableColumn column = new TableColumn(table, SWT.LEFT);
			if (i == 0) {
				table.setSortColumn(column);
			}
			column.setText(columnNames[i]);
			column.setWidth(provider.getColumnWidths()[i]);
			column.addListener(SWT.Selection, sortListener);
		}

		table.setSortDirection(SWT.UP);

		table.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (!provider.isEditable()) {
					return;
				}
				if (e.keyCode == SWT.DEL) {
					int itemCount = table.getItemCount();
					int pos = table.getSelectionIndex();
					if (selectIndex == itemCount - 1) {
						selectIndex--;
					}
					try {
						provider.doDeleteItem(pos);
					} catch (Exception e1) {
						WidgetUtil.processError(SortingFormPage.this.getShell(), e1);
					}
				} else if (e.character == '\r') // return is pressed
				{
					if (style == FULL_FUNCTION || style == FULL_FUNCTION_HORIZONTAL) {
						edit();
					}
				}
			}
		});

		table.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectIndex = table.getSelectionIndex();
				updateArraw();
			}
		});
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (!provider.isEditable()) {
					return;
				}
				if (style == FULL_FUNCTION || style == FULL_FUNCTION_HORIZONTAL) {
					edit();
				}
			}
		});
		createTableViewer();

		btnDel = new Button(this, SWT.PUSH);
		btnDel.setText(Messages.getString("FormPage.Button.Delete")); //$NON-NLS-1$

		btnDel.addSelectionListener(new SelectionAdapter() {

			@Override
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
					WidgetUtil.processError(SortingFormPage.this.getShell(), e1);
				}
				updateArraw();
			}
		});
		btnAdd = new Button(this, SWT.PUSH);
		if (bAddWithDialog) {
			btnAdd.setText(Messages.getString("FormPage.Button.AddWithDialog")); //$NON-NLS-1$
		} else {
			btnAdd.setText(Messages.getString("FormPage.Button.Add")); //$NON-NLS-1$
		}

		btnAdd.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				CommandStack stack = getActionStack();
				stack.startTrans(Messages.getString("FormPage.Menu.ModifyProperty")); //$NON-NLS-1$
				int pos = table.getSelectionIndex();
				boolean sucess = false;
				try {
					sucess = provider.doAddItem(pos);
				} catch (Exception e1) {
					stack.rollback();
					WidgetUtil.processError(SortingFormPage.this.getShell(), e1);
					return;
				}
				if (sucess) {
					stack.commit();
				} else {
					stack.rollback();
				}
				table.setSelection(provider.getShowIndex(table.getItemCount() - 1));
				updateArraw();

			}
		});

		if (style == FULL_FUNCTION || style == FULL_FUNCTION_HORIZONTAL) {
			btnEdit = new Button(this, SWT.PUSH);
			if (bAddWithDialog) {
				btnEdit.setText(Messages.getString("FormPage.Button.EditWithDialog")); //$NON-NLS-1$
			} else {
				btnEdit.setText(Messages.getString("FormPage.Button.Edit")); //$NON-NLS-1$
			}

			btnEdit.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					edit();
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
		}
	}

	private void edit() {
		int pos = table.getSelectionIndex();
		int modelPos = provider.getOriginalIndex(pos);
		if (pos == -1) {
			table.setFocus();
			return;
		}
		CommandStack stack = getActionStack();
		stack.startTrans(Messages.getString("FormPage.Menu.ModifyProperty")); //$NON-NLS-1$
		if (!provider.doEditItem(pos)) {
			stack.rollback();
			return;
		}
		stack.commit();
		table.setSelection(provider.getShowIndex(modelPos));

	}

	private void updateArraw() {
		if (!provider.isEditable() || (style == SIMPLE_FUNCTION)) {
			return;
		}
		int selectIndex = table.getSelectionIndex();
		int min = 0;
		int max = table.getItemCount() - 1;
		// if ( !( provider instanceof GroupHandleProvider ) )
		// {
		// max--;
		// }
		if ((min <= selectIndex) && (selectIndex <= max)) {
			btnDel.setEnabled(true);
			if (btnEdit != null) {
				btnEdit.setEnabled(true);
			}
		} else {
			btnDel.setEnabled(false);
			if (btnEdit != null) {
				btnEdit.setEnabled(false);
			}
		}
	}

	private void updateBindingParameters() {
		if (provider instanceof FilterHandleProvider) {
			ParamBindingHandle[] bindingParams = null;

			if (input.get(0) instanceof ReportItemHandle) {
				ReportItemHandle inputHandle = (ReportItemHandle) input.get(0);
				List list = new ArrayList();
				for (Iterator iterator = inputHandle.paramBindingsIterator(); iterator.hasNext();) {
					ParamBindingHandle handle = (ParamBindingHandle) iterator.next();
					list.add(handle);
				}
				bindingParams = new ParamBindingHandle[list.size()];
				list.toArray(bindingParams);
			}
			((FilterHandleProvider) provider).setBindingParams(bindingParams);
		}
	}

	/**
	 * Creates the TableViewer and set all kinds of processors.
	 *
	 */
	private void createTableViewer() {
		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		tableViewer.setColumnProperties(provider.getColumnNames());
		tableViewer.setContentProvider(new FormContentProvider());
		tableViewer.setLabelProvider(new FormLabelProvider());
	}

	protected void simpleLayout() {
		FormLayout layout = new FormLayout();
		layout.marginHeight = WidgetUtil.SPACING;
		layout.marginWidth = WidgetUtil.SPACING;
		layout.spacing = WidgetUtil.SPACING;
		setLayout(layout);
		// int height = QUICK_BUTTON_HEIGHT - 2;
		FormData data = new FormData();
		data.left = new FormAttachment(title, 50, SWT.RIGHT);
		// data.height = Math.max( btnAdd.computeSize( -1, -1 ).y, height );
		btnAdd.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(btnAdd, 0, SWT.RIGHT);
		// data.height = data.height = Math.max( btnDel.computeSize( -1, -1 ).y,
		// height );
		btnDel.setLayoutData(data);

		data = new FormData();
		data.top = new FormAttachment(btnAdd, 0, SWT.BOTTOM);
		data.left = new FormAttachment(title, 0, SWT.LEFT);
		int width[] = provider.getColumnWidths();
		int dataWidth = 0;
		for (int i = 0; i < width.length; i++) {
			dataWidth += width[i];
		}
		data.right = new FormAttachment(title, dataWidth + 15, SWT.LEFT);
		data.bottom = new FormAttachment(100);
		table.setLayoutData(data);
	}

	/**
	 * Layouts widgets for simple UI type.
	 *
	 */
	protected void normallLayout() {
		FormLayout layout = new FormLayout();
		layout.marginHeight = WidgetUtil.SPACING;
		layout.marginWidth = WidgetUtil.SPACING;
		layout.spacing = WidgetUtil.SPACING;
		setLayout(layout);
		// int height = QUICK_BUTTON_HEIGHT - 2;
		FormData data = new FormData();
		data.right = new FormAttachment(100);
		btnDel.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(btnDel, 0, SWT.LEFT);
		// data.height = height;
		btnAdd.setLayoutData(data);

		data = new FormData();
		data.top = new FormAttachment(btnDel, 0, SWT.BOTTOM);
		data.left = new FormAttachment(title, 0, SWT.LEFT);
		data.right = new FormAttachment(100);
		data.bottom = new FormAttachment(100);
		table.setLayoutData(data);

	}

	protected void fullLayoutHorizontal() {
		FormLayout layout = new FormLayout();
		layout.marginHeight = WidgetUtil.SPACING;
		layout.marginWidth = WidgetUtil.SPACING;
		layout.spacing = WidgetUtil.SPACING;
		setLayout(layout);
		// int height = QUICK_BUTTON_HEIGHT - 2;
		FormData data = new FormData();
		data.right = new FormAttachment(100);
		btnEdit.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(btnEdit, 0, SWT.LEFT);
		// data.height = height;
		btnDel.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(btnDel, 0, SWT.LEFT);
		// data.height = height;
		btnAdd.setLayoutData(data);

		data = new FormData();
		data.top = new FormAttachment(btnEdit, 0, SWT.BOTTOM);
		data.left = new FormAttachment(title, 0, SWT.LEFT);
		data.right = new FormAttachment(100);
		data.bottom = new FormAttachment(100);
		table.setLayoutData(data);
	}

	/**
	 * Layouts widgets for Full UI type.
	 *
	 */
	protected void fullLayout() {
		FormLayout layout = new FormLayout();
		layout.marginHeight = WidgetUtil.SPACING;
		layout.marginWidth = WidgetUtil.SPACING;
		layout.spacing = WidgetUtil.SPACING;
		setLayout(layout);

		int btnWidth = 60;
		int height = QUICK_BUTTON_HEIGHT - 2;
		FormData data = new FormData();
		data.right = new FormAttachment(90);
		data.top = new FormAttachment(0, height + WidgetUtil.SPACING * 2);
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
		data.left = new FormAttachment(title, 0, SWT.LEFT);
		data.right = new FormAttachment(btnAdd, 0, SWT.LEFT);
		table.setLayoutData(data);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.model.core.Listener#elementChanged(org.eclipse.birt.
	 * model.core.DesignElement, org.eclipse.birt.model.activity.NotificationEvent)
	 */
	@Override
	public void elementChanged(DesignElementHandle arg0, NotificationEvent event) {
		if (provider.needRefreshed(event)) {
			if (!this.isDisposed()) {
				refresh();
			}
		}
	}

	public void refresh() {
		tableViewer.refresh();
		table.select(selectIndex);
		table.setFocus();
		updateArraw();
		updateBindingParameters();
		editableUI(provider.isEditable());
		title.setText(provider.getTitle());
	}

	private class FormLabelProvider extends LabelProvider implements ITableLabelProvider {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java
		 * .lang.Object, int)
		 */
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.
		 * lang.Object, int)
		 */
		@Override
		public String getColumnText(Object element, int columnIndex) {
			return provider.getColumnText(element, columnIndex);
		}
	}

	private class FormContentProvider implements IStructuredContentProvider {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(
		 * java.lang.Object)
		 */
		@Override
		public Object[] getElements(Object inputElement) {
			Object[] elements = provider.getElements(inputElement);
			for (int i = 0; i < elements.length; i++) {
				if (elements[i] instanceof DesignElementHandle) {
					DesignElementHandle element = (DesignElementHandle) elements[i];
					element.removeListener(SortingFormPage.this);
					element.addListener(SortingFormPage.this);
				}
			}
			return elements;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		@Override
		public void dispose() {

			if (!(provider instanceof GroupHandleProvider)) {
				return;
			}

			Object[] elements = provider.getElements(input);

			if (elements == null) {
				return;
			}
			for (int i = 0; i < elements.length; i++) {
				if (elements[i] instanceof DesignElementHandle) {
					DesignElementHandle element = (DesignElementHandle) elements[i];
					element.removeListener(SortingFormPage.this);
				}
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse
		 * .jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * AttributePage#buildUI()
	 */
	protected void buildUI() {

	}

	/**
	 * Gets the DE CommandStack instance
	 *
	 * @return CommandStack instance
	 */
	private CommandStack getActionStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	protected void registerListeners() {
		if (input == null) {
			return;
		}
		for (int i = 0; i < input.size(); i++) {
			Object obj = input.get(i);
			if (obj instanceof DesignElementHandle) {
				DesignElementHandle element = (DesignElementHandle) obj;
				element.addListener(this);
			}
		}
	}

	protected void deRegisterListeners() {
		if (input == null) {
			return;
		}
		for (int i = 0; i < input.size(); i++) {
			Object obj = input.get(i);
			if (obj instanceof DesignElementHandle) {
				DesignElementHandle element = (DesignElementHandle) obj;
				element.removeListener(this);
			}
		}
	}
}
